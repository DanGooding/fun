package fun.ast;

import fun.eval.*;
import fun.types.*;
import fun.values.IntegerValue;
import fun.values.Value;

import java.math.BigInteger;
import java.util.Map;

public class ASTLiteralInteger extends ASTNode implements ASTMatchable {
    private final BigInteger value;

    public ASTLiteralInteger(BigInteger value) {
        this.value = value;
    }
    public ASTLiteralInteger(int value) {
        this(BigInteger.valueOf(value));
    }

    @Override
    public IntegerValue evaluate(Environment<Thunk> env) {
        return new IntegerValue(this.value);
    }

    @Override
    public Type inferType(Inferer inferer, TypeEnvironment env) {
        return new TypeInteger();
    }

    @Override
    public void bindMatch(Thunk subject, Environment<Thunk> env) throws PatternMatchFailedException, EvaluationException {
        Value subjectValue = subject.force();
        if (!(subjectValue instanceof IntegerValue)) {
            throw new RuntimeTypeErrorException(
                String.format("cannot match %s against Integer", subjectValue.getClass().getSimpleName()));
        }
        IntegerValue integerSubject = (IntegerValue)subjectValue;
        if (!this.value.equals(integerSubject.getValue())) {
            throw new PatternMatchFailedException(this.toString());
        }
    }

    @Override
    public Type inferPatternType(Inferer inferer, Map<String, Type> bindings) {
        return new TypeInteger();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
