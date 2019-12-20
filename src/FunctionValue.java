public class FunctionValue implements Value {

    // TODO: just make public & don't modify, don't use getters ?
    private final ASTMatchable parameterPattern;
    private final ASTNode body;
    private final Environment capturedEnv;

    public FunctionValue(ASTMatchable parameterPattern, ASTNode body, Environment capturedEnv) {
        this.parameterPattern = parameterPattern;
        this.body = body;
        this.capturedEnv = capturedEnv;
    }

    public ASTMatchable getParameterPattern() {
        return parameterPattern;
    }

    public ASTNode getBody() {
        return body;
    }

    public Environment getCapturedEnv() {
        return capturedEnv;
    }
}
