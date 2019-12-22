class ASTLiteralInt extends ASTMatchable {
    private final int value;

    ASTLiteralInt(int value) {
        this.value = value;
    }

    @Override
    IntValue evaluate(Environment env) {
        return new IntValue(this.value);
    }

    @Override
    void bindMatch(Value subject, Environment env) throws PatternMatchFailedException {
        if (!(subject instanceof IntValue) ||
            ((IntValue) subject).getValue() != this.value) {
            // not an IntValue, or has a different value to this:
            throw new PatternMatchFailedException(this.toString());
        }
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
