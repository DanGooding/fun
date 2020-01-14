package fun.ast;

import fun.eval.Environment;
import fun.eval.EvaluationException;
import fun.eval.TypeErrorException;
import fun.values.BoolValue;
import fun.values.IntegerValue;
import fun.values.Value;

import java.math.BigInteger;

public class ASTLessThan extends ASTNode {

    private final ASTNode left;
    private final ASTNode right;

    public ASTLessThan(ASTNode left, ASTNode right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Value evaluate(Environment env) throws EvaluationException {
        Value leftValue = left.evaluate(env);
        Value rightValue = right.evaluate(env);

        if (!(leftValue instanceof IntegerValue && rightValue instanceof IntegerValue)) {
            throw new TypeErrorException("cannot compare non integer values");
        }

        BigInteger leftInteger = ((IntegerValue) leftValue).getValue();
        BigInteger rightInteger = ((IntegerValue) rightValue).getValue();

        return new BoolValue(leftInteger.compareTo(rightInteger) < 0);
    }

    @Override
    public String toString() {
        return String.format("(%s < %s)", left, right);
    }
}
