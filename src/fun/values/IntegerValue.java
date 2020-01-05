package fun.values;

import java.math.BigInteger;

public class IntegerValue implements ConstantValue {
    private final BigInteger value;

    public IntegerValue(BigInteger value) {
        this.value = value;
    }

    public BigInteger getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
