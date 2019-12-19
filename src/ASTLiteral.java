class ASTLiteral extends ASTNode {
    private final int value;

    ASTLiteral(int value) {
        this.value = value;
    }

    @Override
    ConstantValue evaluate(Environment env) {
        return new ConstantValue(this.value);
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
