public class ASTLiteralBool extends ASTMatchable {
    private final boolean value;

    public ASTLiteralBool(boolean value) {
        this.value = value;
    }

    @Override
    BoolValue evaluate(Environment env) {
        return new BoolValue(value);
    }

    @Override
    void bindMatch(Value subject, Environment env) throws PatternMatchException {
        if (!(subject instanceof BoolValue) ||
            ((BoolValue) subject).getValue() != this.value) {
            throw new PatternMatchException(this.toString());
        }
    }

    @Override
    public String toString() {
        return value ? "True" : "False";
    }
}
// TODO: boolean operators