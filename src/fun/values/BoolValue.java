package fun.values;

// TODO add AST boolean operations (and, or, not)
public class BoolValue implements ConstantValue {
    private final boolean value;

    public BoolValue(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value ? "True" : "False";
    }
}
