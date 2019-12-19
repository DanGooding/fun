public class ASTMinus extends ASTArith {

    ASTMinus(ASTNode left, ASTNode right) {
        super(left, right);
    }

    @Override
    int operator(int leftOperand, int rightOperand) {
        return  leftOperand - rightOperand;
    }

    @Override
    String operatorSymbol() {
        return "-";
    }
}
