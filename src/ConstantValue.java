class ConstantValue implements Value {

    private final int value;

    ConstantValue(int value) {
        this.value = value;
    }

    int getValue() {
        return value;
    }
}
