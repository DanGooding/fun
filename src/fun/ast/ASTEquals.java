package fun.ast;

import fun.eval.Environment;
import fun.eval.EvaluationException;
import fun.eval.TypeErrorException;
import fun.values.BoolValue;
import fun.values.ConstantValue;
import fun.values.IntegerValue;
import fun.values.Value;

// TODO: add not equals ? or just use boolean not along with this?
public class ASTEquals extends ASTNode {

    private final ASTNode left;
    private final ASTNode right;

    public ASTEquals(ASTNode left, ASTNode right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public BoolValue evaluate(Environment env) throws EvaluationException {
        Value leftValue = left.evaluate(env);
        Value rightValue = right.evaluate(env);

        if (leftValue instanceof ConstantValue && rightValue instanceof ConstantValue) {
            // TODO: atrocious, have a method on fun.values.ConstantValue or something
            // type tag + lookup table of equatable types + method

            if (leftValue instanceof BoolValue && rightValue instanceof BoolValue) {
                return new BoolValue(((BoolValue) leftValue).getValue() == ((BoolValue) rightValue).getValue());

            }else if (leftValue instanceof IntegerValue && rightValue instanceof IntegerValue) {
                return new BoolValue(((IntegerValue) leftValue).getValue().equals(((IntegerValue) rightValue).getValue()));

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
