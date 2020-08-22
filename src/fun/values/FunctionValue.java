package fun.values;

import fun.eval.Environment;
import fun.ast.ASTMatchable;
import fun.ast.ASTNode;
import fun.eval.Thunk;

public class FunctionValue implements Value {

    // TODO: just make public & don't modify, don't use getters ?
    private final ASTMatchable parameterPattern;
    private final ASTNode body;
    private final Environment<Thunk> capturedEnv;

    public FunctionValue(ASTMatchable parameterPattern, ASTNode body, Environment<Thunk> capturedEnv) {
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

    public Environment<Thunk> getCapturedEnv() {
        return capturedEnv;
    }
}
