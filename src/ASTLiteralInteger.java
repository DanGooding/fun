import java.math.BigInteger;

class ASTLiteralInteger extends ASTMatchable {
    private final BigInteger value;

    ASTLiteralInteger(BigInteger value) {
        this.value = value;
    }
    ASTLiteralInteger(int value) {
        this(BigInteger.valueOf(value));
    }

    @Override
    IntegerValue evaluate(Environment env) {
        return new IntegerValue(this.value);
    }

    @Override
    void bindMatch(Value subject, Environment env) throws PatternMatchFailedException {
        if (!(subject instanceof IntegerValue) ||
            !this.value.equals(((IntegerValue) subject).getValue())) {
            // not an IntegerValue, or has a different value to this:
            throw new PatternMatchFailedException(this.toString());
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
