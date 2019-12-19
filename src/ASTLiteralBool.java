public class ASTLiteralBool extends ASTNode {
    private final boolean value;

    public ASTLiteralBool(boolean value) {
        this.value = value;
    }

    @Override
    BoolValue evaluate(Environment env) {
        return new BoolValue(value);
    }
}
