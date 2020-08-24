package fun.ast;

import fun.eval.Environment;
import fun.eval.Thunk;
import fun.types.*;
import fun.values.FunctionValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        Map<String, Type> newBindings = new HashMap<>();
        Type parameterType = parameterPattern.inferPatternType(inferer, newBindings);

        TypeEnvironment bodyEnv = new TypeEnvironment(env);
        for (String boundName : newBindings.keySet()) {
            bodyEnv.bind(boundName, newBindings.get(boundName)); // introduce a monotype
        }

        Type bodyType = body.inferType(inferer, bodyEnv);

        return new TypeArrow(parameterType, bodyType);
    }

    @Override
    public String toString() {
        return String.format("(lambda %s -> %s)", parameterPattern, body);
    }
}
