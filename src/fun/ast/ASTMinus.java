package fun.ast;

import java.math.BigInteger;

public class ASTMinus extends ASTArith {

    public ASTMinus(ASTNode left, ASTNode right) {
        super(left, right);
    }

    @Override
    BigInteger operator(BigInteger leftOperand, BigInteger rightOperand) {
        return leftOperand.subtract(rightOperand);
    }

    @Override
    String operatorSymbol() {
        return "-";
    }
}
