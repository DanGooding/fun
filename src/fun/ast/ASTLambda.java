package fun.ast;

import fun.eval.Environment;
import fun.values.FunctionValue;

public class ASTLambda extends ASTNode {

    private final ASTMatchable parameterPattern;
    private final ASTNode body;

    public ASTLambda(ASTMatchable parameterPattern, ASTNode body) {
        this.parameterPattern = parameterPattern;
        this.body = body;
    }

    /**
     * convenience constructor for tests
     * @param name the name of the ASTVar to bind to
     */
    public ASTLambda(String name, ASTNode body) {
        this(new ASTVar(name), body);
    }

    @Override
    public FunctionValue evaluate(Environment env) {
        return new FunctionValue(parameterPattern, body, env);
    }

    @Override
    public String toString() {
        return String.format("(lambda %s -> %s)", parameterPattern, body);
    }
}
