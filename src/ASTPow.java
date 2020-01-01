public class ASTPow extends ASTArith {

    ASTPow(ASTNode left, ASTNode right) {
        super(left, right);
    }

    @Override
    int operator(int base, int power) {
        if (power < 0) {
            throw new RuntimeException("cannot raise to negative power");
        }
        int result = 1;
        while (power > 0) {
            if (power % 2 == 0) {
                base = base * base;
                power /= 2;
            }else {
                result *= base;
                power -= 1;
            }
        }
        return result;
    }

    @Override
    String operatorSymbol() {
        return "^";
    }
}
