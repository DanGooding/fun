package fun.ast;

import fun.eval.*;
import fun.types.*;
import fun.values.BoolValue;
import fun.values.Value;

import java.util.Map;

public class ASTLiteralBool extends ASTNode implements ASTMatchable {
    private final boolean value;

    public ASTLiteralBool(boolean value) {
        this.value = value;
    }

    @Override
    public BoolValue evaluate(Environment<Thunk> env) {
        return new BoolValue(value);
    }

    @Override
    public Type inferType(Inferer inferer, TypeEnvironment env) {
        return new TypeBool();
    }

    @Override
    public void bindMatch(Thunk subject, Environment<Thunk> env) throws PatternMatchFailedException, EvaluationException {
        Value subjectValue = subject.force();
        if (!(subjectValue instanceof BoolValue)) {
            throw new RuntimeTypeErrorException(
                String.format("cannot match %s against Bool", subjectValue.getClass().getSimpleName()));
        }
        BoolValue boolSubject = (BoolValue)subjectValue;
        if (boolSubject.getValue() != this.value) {
            throw new PatternMatchFailedException(this.toString());
        }
    }

    @Override
    public Type inferPatternType(Inferer inferer, Map<String, Type> bindings) {
        return new TypeBool();
    }

    @Override
    public String toString() {
        return value ? "True" : "False";
    }
}
// TODO: boolean operators