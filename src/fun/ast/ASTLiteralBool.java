package fun.ast;

import fun.eval.Environment;
import fun.eval.EvaluationException;
import fun.eval.PatternMatchFailedException;
import fun.eval.Thunk;
import fun.types.*;
import fun.values.BoolValue;
import fun.values.Value;

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
        if (!(subjectValue instanceof BoolValue) ||
            ((BoolValue) subjectValue).getValue() != this.value) {
            throw new PatternMatchFailedException(this.toString());
        }
    }

    @Override
    public String toString() {
        return value ? "True" : "False";
    }
}
// TODO: boolean operators