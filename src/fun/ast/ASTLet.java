package fun.ast;

import fun.eval.Environment;
import fun.eval.EvaluationException;
import fun.eval.PatternMatchFailedException;
import fun.eval.Thunk;
import fun.values.Value;

public class ASTLet extends ASTNode {
    // let <matchable> = <ast> in <ast>

    private final ASTMatchable pattern;
    private final ASTNode assigned;
    private final ASTNode body;

    public ASTLet(ASTMatchable pattern, ASTNode assigned, ASTNode body) {
        this.pattern = pattern;
        this.assigned = assigned;
        this.body = body;
    }

    public ASTLet(String name, ASTNode assigned, ASTNode body) {
        this(new ASTVar(name), assigned, body);
    }

    @Override
    public Value evaluate(Environment env) throws EvaluationException {

        // the environment the body will be evaluated in
        // has all the bindings of `env`, plus everything bound in the pattern match
        Environment bodyEnv = new Environment(env);

        try {
            pattern.bindMatch(new Thunk(assigned, bodyEnv), bodyEnv);

        } catch (PatternMatchFailedException e) {
            throw new EvaluationException(e.getMessage());
        }

        return body.evaluate(bodyEnv);
    }

    @Override
    public String toString() {
        return String.format("(let %s = %s in %s)", pattern, assigned, body);
    }
}
