
// TODO: add not equals ? or just use boolean not along with this?
public class ASTEquals extends ASTNode {

    private final ASTNode left;
    private final ASTNode right;

    public ASTEquals(ASTNode left, ASTNode right) {
        this.left = left;
        this.right = right;
    }

    @Override
    BoolValue evaluate(Environment env) throws EvaluationException {
        Value leftValue = left.evaluate(env);
        Value rightValue = right.evaluate(env);

        if (leftValue instanceof ConstantValue && rightValue instanceof ConstantValue) {
            // TODO: atrocious, have a method on ConstantValue or something
            // type tag + lookup table of equatable types + method

            if (leftValue instanceof BoolValue && rightValue instanceof BoolValue) {
                return new BoolValue(((BoolValue) leftValue).getValue() == ((BoolValue) rightValue).getValue());

            }else if (leftValue instanceof IntValue && rightValue instanceof IntValue) {
                return new BoolValue(((IntValue) leftValue).getValue() == ((IntValue) rightValue).getValue());

            }else {
                throw new TypeErrorException("cannot equate values of different type");
            }
        }else {
            throw new TypeErrorException("functions are not equatable");
        }
    }

    @Override
    public String toString() {
        return String.format("(%s == %s)", left, right);
    }
}
