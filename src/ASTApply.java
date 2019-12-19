public class ASTApply extends ASTNode {

    private final ASTNode function;  // its definition / a reference by its name
    private final ASTNode argument;

    public ASTApply(ASTNode function, ASTNode argument) {
        this.function = function;
        this.argument = argument;
    }

    @Override
    Value evaluate(Environment env) {
        Value argumentValue = argument.evaluate(env);
        Value possibleFunctionValue = function.evaluate(env);

        if (possibleFunctionValue instanceof FunctionValue) {
            // TODO: this is awful naming
            FunctionValue funObj = (FunctionValue) possibleFunctionValue;

            Environment internalEnv =
                funObj.getCapturedEnv().withBinding(funObj.getParameter(), argumentValue);

            return funObj.getBody().evaluate(internalEnv);

        }else {

            throw new TypeErrorException("a ConstantValue is not a function, it cannot be applied");
        }
    }

    @Override
    public String toString() {
        return String.format("(%s %s)", function, argument);
    }
}
