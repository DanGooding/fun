public class ASTLambda extends ASTNode {

    private final ASTMatchable parameterPattern;
    private final ASTNode body;

    ASTLambda(ASTMatchable parameterPattern, ASTNode body) {
        this.parameterPattern = parameterPattern;
        this.body = body;
    }

    ASTLambda(String name, ASTNode body) {
        this(new ASTVar(name), body);
    }

    @Override
    FunctionValue evaluate(Environment env) {
        return new FunctionValue(parameterPattern, body, env);
    }

    @Override
    public String toString() {
        return String.format("(lambda %s -> %s)", parameterPattern, body);
    }
}
