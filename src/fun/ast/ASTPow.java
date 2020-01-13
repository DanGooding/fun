package fun.ast;

import java.math.BigInteger;

public class ASTPow extends ASTArith {

    public ASTPow(ASTNode left, ASTNode right) {
        super(left, right);
    }

    // TODO: rethrow exception as EvaluationException on negative power
    @Override
    BigInteger operator(BigInteger base, BigInteger power) {
        return base.pow(power.intValueExact());
    }

    @Override
    String operatorSymbol() {
        return "^";
    }
}
