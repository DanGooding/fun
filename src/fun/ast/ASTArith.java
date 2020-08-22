package fun.ast;

import fun.eval.Environment;
import fun.eval.EvaluationException;
import fun.eval.RuntimeTypeErrorException;
import fun.eval.Thunk;
import fun.types.*;
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
    public IntegerValue evaluate(Environment<Thunk> env) throws EvaluationException {
        Value leftValue = left.evaluate(env);
        Value rightValue = right.evaluate(env);

        if (leftValue instanceof IntegerValue && rightValue instanceof IntegerValue){
            BigInteger leftOperand = ((IntegerValue) leftValue).getValue();
            BigInteger rightOperand = ((IntegerValue) rightValue).getValue();
            return new IntegerValue(operator(leftOperand, rightOperand));
        }else {
            throw new RuntimeTypeErrorException("can only perform arithmetic on constant int value");
        }
    }

    @Override
    public Type inferType(Inferer inferer, TypeEnvironment env) throws TypeErrorException {
        Type leftType = left.inferType(inferer, env);
        Type rightType = right.inferType(inferer, env);
        inferer.unify(leftType, new TypeInteger());
        inferer.unify(rightType, new TypeInteger());
        return new TypeInteger();
    }

    @Override
    public String toString() {
        return String.format("(%s %s %s)", left, operatorSymbol(), right);
    }
}
