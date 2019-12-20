public class ASTApply extends ASTNode {

    private final ASTNode function;  // its definition / a reference by its name
    private final ASTNode argument;

    public ASTApply(ASTNode function, ASTNode argument) {
        this.function = function;
        this.argument = argument;
    }

    @Override
    Value evaluate(Environment env) throws EvaluationException {
        Value argumentValue = argument.evaluate(env);
        Value possibleFunctionValue = function.evaluate(env);

        if (possibleFunctionValue instanceof FunctionValue) {
            // TODO: this is awful naming
            FunctionValue funObj = (FunctionValue) possibleFunctionValue;

            Environment internalEnv = new Environment(funObj.getCapturedEnv());

            funObj.getParameterPattern().bindMatch(argumentValue, internalEnv);

            return funObj.getBody().evaluate(internalEnv);

        }else {

            throw new TypeErrorException("cannot apply a non-FunctionValue");
        }
    }

    @Override
    public String toString() {
        return String.format("(%s %s)", function, argument);
    }
}
