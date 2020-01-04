import java.math.BigInteger;

class ASTLiteralInteger extends ASTNode implements ASTMatchable {
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
    public void bindMatch(Thunk subject, Environment env) throws PatternMatchFailedException, EvaluationException {
        Value subjectValue = subject.force();
        if (!(subjectValue instanceof IntegerValue) ||
            !this.value.equals(((IntegerValue) subjectValue).getValue())) {
            // not an IntegerValue, or has a different value to this:
            throw new PatternMatchFailedException(this.toString());
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
