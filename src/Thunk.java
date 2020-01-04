public class Thunk {

    private boolean computed;

    private ASTNode expr;
    private Environment env;

    private Value value;

    Thunk(ASTNode expr, Environment env) {
        computed = false;
        this.expr = expr;
        this.env = env;
    }

    Value force() throws EvaluationException {
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
            return String.format("Thunk(%s)", expr);
        }
    }
}
