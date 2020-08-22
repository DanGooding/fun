package fun.ast;

import fun.eval.Environment;
import fun.eval.Thunk;
import fun.types.*;
import fun.values.FunctionValue;

import java.util.List;

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
    public FunctionValue evaluate(Environment<Thunk> env) {
        return new FunctionValue(parameterPattern, body, env);
    }

    @Override
    public Type inferType(Inferer inferer, TypeEnvironment env) throws TypeErrorException {

        // TODO: implement pattern type inference
        // TODO: remove ASTVar::getName
        if (!(parameterPattern instanceof ASTVar)) {
            throw new UnsupportedOperationException();
        }

        ASTVar parameter = (ASTVar)parameterPattern;
        TypeVariable parameterType = inferer.freshVariable();

        TypeEnvironment bodyEnv = new TypeEnvironment(env);
        bodyEnv.bind(parameter.getName(), parameterType); // introduce a monotype

        Type bodyType = body.inferType(inferer, bodyEnv);

        return new TypeArrow(parameterType, bodyType);
    }

    @Override
    public String toString() {
        return String.format("(lambda %s -> %s)", parameterPattern, body);
    }
}
