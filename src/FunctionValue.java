public class FunctionValue implements Value {

    // TODO: just make public & don't modify, don't use getters ?
    private final String parameter;
    private final ASTNode body;
    private final Environment capturedEnv;

    public FunctionValue(String parameter, ASTNode body, Environment capturedEnv) {
        this.parameter = parameter;
        this.body = body;
        this.capturedEnv = capturedEnv;
    }

    public String getParameter() {
        return parameter;
    }

    public ASTNode getBody() {
        return body;
    }

    public Environment getCapturedEnv() {
        return capturedEnv;
    }
}
