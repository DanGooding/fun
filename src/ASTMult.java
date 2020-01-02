import java.math.BigInteger;

class ASTMult extends ASTArith {

    ASTMult(ASTNode left, ASTNode right) {
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
