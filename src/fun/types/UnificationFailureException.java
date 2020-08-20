package fun.types;

public class UnificationFailureException extends TypeErrorException {
    private Type left;
    private Type right;
    UnificationFailureException(Type left, Type right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "UnificationFailure{" +
            "left=" + left +
            ", right=" + right +
            '}';
    }

    public Type getLeft() {
        return left;
    }

    public Type getRight() {
        return right;
    }
}
