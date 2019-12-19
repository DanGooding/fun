public class ASTFun extends ASTNode {

    private final String parameter;
    private final ASTNode body;

    public ASTFun(String parameter, ASTNode body) {
        this.parameter = parameter;
        this.body = body;
    }

    @Override
    FunctionValue evaluate(Environment env) {
        return new FunctionValue(parameter, body, env);
    }

    @Override
    public String toString() {
        return String.format("(fun %s -> %s)", parameter, body);
    }
}
