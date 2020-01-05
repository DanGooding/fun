package fun.ast;

import fun.eval.Environment;
import fun.eval.EvaluationException;
import fun.eval.PatternMatchFailedException;
import fun.eval.Thunk;
import fun.values.IntegerValue;
import fun.values.Value;

import java.math.BigInteger;

public class ASTLiteralInteger extends ASTNode implements ASTMatchable {
    private final BigInteger value;

    public ASTLiteralInteger(BigInteger value) {
        this.value = value;
    }
    public ASTLiteralInteger(int value) {
        this(BigInteger.valueOf(value));
    }

    @Override
    public IntegerValue evaluate(Environment env) {
        return new IntegerValue(this.value);
    }

    @Override
    public void bindMatch(Thunk subject, Environment env) throws PatternMatchFailedException, EvaluationException {
        Value subjectValue = subject.force();
        if (!(subjectValue instanceof IntegerValue) ||
            !this.value.equals(((IntegerValue) subjectValue).getValue())) {
            // not an fun.values.IntegerValue, or has a different value to this:
            throw new PatternMatchFailedException(this.toString());
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
