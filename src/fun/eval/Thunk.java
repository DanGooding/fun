package fun.eval;

import fun.ast.ASTNode;
import fun.values.Value;

public class Thunk {

    private boolean computed;

    private ASTNode expr;
    private Environment<Thunk> env;

    private Value value;

    public Thunk(ASTNode expr, Environment<Thunk> env) {
        computed = false;
        this.expr = expr;
        this.env = env;
    }

    public Value force() throws EvaluationException {
        if (!computed) {
            value = expr.evaluate(env);
            computed = true;
            // TODO: null out expr & env ?
        }
        return value;
    }

    @Override
    public String toString() {
        if (computed) {
            return value.toString();
        }else {
//            return expr.toString(); // TODO: include env ?
            return String.format("fun.eval.Thunk(%s)", expr);
        }
    }
}
