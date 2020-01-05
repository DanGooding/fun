package fun.ast;

import java.math.BigInteger;

public class ASTMult extends ASTArith {

    public ASTMult(ASTNode left, ASTNode right) {
        super(left, right);
    }

    @Override
    BigInteger operator(BigInteger leftOperand, BigInteger rightOperand) {
        return leftOperand.multiply(rightOperand);
    }

    @Override
    String operatorSymbol() {
        return "*";
    }
}
