abstract class ASTArith extends ASTNode {

    private final ASTNode left;
    private final ASTNode right;

    ASTArith(ASTNode left, ASTNode right) {
        this.left = left;
        this.right = right;
    }

    // TODO: should this operate on ConstantValue ?
    abstract int operator(int leftOperand, int rightOperand);

    abstract String operatorSymbol();

    @Override
    ConstantValue evaluate(Environment env) {
        ConstantValue leftValue = left.evaluate(env);
        ConstantValue rightValue = right.evaluate(env);

        return new ConstantValue(operator(leftValue.getValue(), rightValue.getValue()));
    }

    @Override
    public String toString() {
        return String.format("(%s %s %s)", left, operatorSymbol(), right);
    }
}
