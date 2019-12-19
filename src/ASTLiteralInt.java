class ASTLiteralInt extends ASTNode {
    private final int value;

    ASTLiteralInt(int value) {
        this.value = value;
    }

    @Override
    IntValue evaluate(Environment env) {
        return new IntValue(this.value);
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
