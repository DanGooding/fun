package fun.ast;

import fun.eval.Environment;
import fun.eval.EvaluationException;
import fun.eval.TypeErrorException;
import fun.values.IntegerValue;
import fun.values.Value;

import java.math.BigInteger;

abstract class ASTArith extends ASTNode {

    private final ASTNode left;
    private final ASTNode right;

    ASTArith(ASTNode left, ASTNode right) {
        this.left = left;
        this.right = right;
    }

    abstract BigInteger operator(BigInteger leftOperand, BigInteger rightOperand);

    abstract String operatorSymbol();

    @Override
    public IntegerValue evaluate(Environment env) throws EvaluationException {
        Value leftValue = left.evaluate(env);
        Value rightValue = right.evaluate(env);

        if (leftValue instanceof IntegerValue && rightValue instanceof IntegerValue){
            BigInteger leftOperand = ((IntegerValue) leftValue).getValue();
            BigInteger rightOperand = ((IntegerValue) rightValue).getValue();
            return new IntegerValue(operator(leftOperand, rightOperand));
        }else {
            throw new TypeErrorException("can only perform arithmetic on constant int value");
        }
    }

    @Override
    public String toString() {
        return String.format("(%s %s %s)", left, operatorSymbol(), right);
    }
}
