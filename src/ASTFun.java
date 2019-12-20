public class ASTFun extends ASTNode {

    private final ASTMatchable parameterPattern;
    private final ASTNode body;

    ASTFun(ASTMatchable parameterPattern, ASTNode body) {
        this.parameterPattern = parameterPattern;
        this.body = body;
    }

    ASTFun(String name, ASTNode body) {
        this(new ASTVar(name), body);
    }

    @Override
    FunctionValue evaluate(Environment env) {
        return new FunctionValue(parameterPattern, body, env);
    }

    @Override
    public String toString() {
        return String.format("(fun %s -> %s)", parameterPattern, body);
    }
}
