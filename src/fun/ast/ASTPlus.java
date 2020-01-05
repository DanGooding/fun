package fun.ast;

import java.math.BigInteger;

public class ASTPlus extends ASTArith {

    public ASTPlus(ASTNode left, ASTNode right) {
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
