package fun.ast;

import fun.eval.*;
import fun.types.*;
import fun.values.FunctionValue;
import fun.values.Value;

public class ASTApply extends ASTNode {

    private final ASTNode function;  // its definition / a reference by its name
    private final ASTNode argument;

    public ASTApply(ASTNode function, ASTNode argument) {
        this.function = function;
        this.argument = argument;
    }

    @Override
    public Value evaluate(Environment<Thunk> env) throws EvaluationException {
        Thunk argumentThunk = new Thunk(argument, env);
        Value possibleFunctionValue = function.evaluate(env);

        if (possibleFunctionValue instanceof FunctionValue) {
            // TODO: this is awful naming
            FunctionValue funObj = (FunctionValue) possibleFunctionValue;

            Environment<Thunk> internalEnv = new Environment<>(funObj.getCapturedEnv());

            try {
                funObj.getParameterPattern().bindMatch(argumentThunk, internalEnv);
            } catch (PatternMatchFailedException e) {
                // TODO: more useful error message / specialised exception
                throw new EvaluationException("pattern match failed in function");
            }

            return funObj.getBody().evaluate(internalEnv);

        }else {
            throw new RuntimeTypeErrorException("cannot apply a non-fun.values.FunctionValue");
        }
    }

    @Override
    public Type inferType(Inferer inferer, TypeEnvironment env) throws TypeErrorException {

        Type functionType = function.inferType(inferer, env);
        Type argumentType = argument.inferType(inferer, env);

        TypeVariable resultType = inferer.freshVariable();

        inferer.unify(functionType, new TypeArrow(argumentType, resultType));

        return resultType;
    }

    @Override
    public String toString() {
        return String.format("(%s %s)", function, argument);
    }
}
