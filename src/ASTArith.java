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
        Value leftValue = left.evaluate(env);
        Value rightValue = right.evaluate(env);

        if (leftValue instanceof ConstantValue && rightValue instanceof ConstantValue){
            int leftOperand = ((ConstantValue) leftValue).getValue();
            int rightOperand = ((ConstantValue) rightValue).getValue();
            return new ConstantValue(operator(leftOperand, rightOperand));
        }else {
            throw new RuntimeException("can only perform arithmetic on constant value");
        }
    }

    @Override
    public String toString() {
        return String.format("(%s %s %s)", left, operatorSymbol(), right);
    }
}
