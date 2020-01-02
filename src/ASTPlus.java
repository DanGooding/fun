import java.math.BigInteger;

class ASTPlus extends ASTArith {

    ASTPlus(ASTNode left, ASTNode right) {
        super(left, right);
    }

    @Override
    BigInteger operator(BigInteger leftOperand, BigInteger rightOperand) {
        return leftOperand.add(rightOperand);
    }

    @Override
    String operatorSymbol() {
        return "+";
    }
}
