package fun.ast;

import fun.eval.Environment;
import fun.eval.EvaluationException;
import fun.eval.PatternMatchFailedException;
import fun.eval.Thunk;
import fun.types.*;
import fun.values.Value;

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

        // TODO: implement pattern type inference
        // TODO: remove ASTVar::getName
        if (!(pattern instanceof ASTVar)) {
            throw new UnsupportedOperationException();
        }

        ASTVar variable = (ASTVar)pattern;

        // TODO: introduce type variable into env for lazy recursion
        Type subjectType = subject.inferType(inferer, env);
        Scheme subjectPolyType = inferer.generalise(subjectType, env);

        TypeEnvironment bodyEnv = new TypeEnvironment(env);
        bodyEnv.bind(variable.getName(), subjectPolyType);

        return body.inferType(inferer, bodyEnv);
    }

    @Override
    public String toString() {
        return String.format("(let %s = %s in %s)", pattern, subject, body);
    }
}
