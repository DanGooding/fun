package fun.ast;

import fun.eval.Environment;
import fun.eval.EvaluationException;
import fun.eval.PatternMatchFailedException;
import fun.eval.Thunk;
import fun.types.*;
import fun.values.Value;

import java.util.HashMap;
import java.util.Map;

public class ASTLet extends ASTNode {
    // let <matchable> = <ast> in <ast>

    private final ASTMatchable pattern;
    private final ASTNode subject;
    private final ASTNode body;

    public ASTLet(ASTMatchable pattern, ASTNode subject, ASTNode body) {
        this.pattern = pattern;
        this.subject = subject;
        this.body = body;
    }

    public ASTLet(String name, ASTNode subject, ASTNode body) {
        this(new ASTVar(name), subject, body);
    }

    @Override
    public Value evaluate(Environment<Thunk> env) throws EvaluationException {

        // the environment the body will be evaluated in
        // has all the bindings of `env`, plus everything bound in the pattern match
        Environment<Thunk> bodyEnv = new Environment<>(env);

        try {
            pattern.bindMatch(new Thunk(subject, bodyEnv), bodyEnv);

        } catch (PatternMatchFailedException e) {
            throw new EvaluationException(e.getMessage());
        }

        return body.evaluate(bodyEnv);
    }

    @Override
    public Type inferType(Inferer inferer, TypeEnvironment env) throws TypeErrorException {

        Map<String, Type> newBindings = new HashMap<>();
        Type patternType = pattern.inferPatternType(inferer, newBindings);

        // bound variables available when typing subject to allow recursion
        TypeEnvironment subjectEnv = new TypeEnvironment(env);
        for (String boundName : newBindings.keySet()) {
            subjectEnv.bind(boundName, newBindings.get(boundName));
        }

        Type subjectType = subject.inferType(inferer, subjectEnv);
        inferer.unify(patternType, subjectType);

        TypeEnvironment bodyEnv = new TypeEnvironment(env);
        for (String boundName : newBindings.keySet()) {
            Type boundType = newBindings.get(boundName);
            Scheme boundPolytype = inferer.generalise(boundType, env);
            bodyEnv.bind(boundName, boundPolytype);
        }

        return body.inferType(inferer, bodyEnv);
    }

    @Override
    public String toString() {
        return String.format("(let %s = %s in %s)", pattern, subject, body);
    }
}
