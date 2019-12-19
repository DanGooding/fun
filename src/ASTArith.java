import java.util.Iterator;

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
    IntValue evaluate(Environment env) {
        Value leftValue = left.evaluate(env);
        Value rightValue = right.evaluate(env);

        if (leftValue instanceof IntValue && rightValue instanceof IntValue){
            int leftOperand = ((IntValue) leftValue).getValue();
            int rightOperand = ((IntValue) rightValue).getValue();
            return new IntValue(operator(leftOperand, rightOperand));
        }else {
            throw new TypeErrorException("can only perform arithmetic on constant int value");
        }
    }

    @Override
    public String toString() {
        return String.format("(%s %s %s)", left, operatorSymbol(), right);
    }
}
