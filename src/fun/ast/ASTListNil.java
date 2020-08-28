package fun.ast;

import fun.eval.*;
import fun.types.Inferer;
import fun.types.Type;
import fun.types.TypeEnvironment;
import fun.types.TypeErrorException;
import fun.values.ListConsValue;
import fun.values.ListNilValue;
import fun.values.Value;

import java.util.Map;

public class ASTListNil extends ASTNode implements ASTMatchable {
    @Override
    public Value evaluate(Environment<Thunk> env) {
        return new ListNilValue();
    }

    @Override
    public Type inferType(Inferer inferer, TypeEnvironment env) {
        return inferer.freshVariable();
    }

    @Override
    public void bindMatch(Thunk subject, Environment<Thunk> env) throws PatternMatchFailedException, EvaluationException {
        Value subjectValue = subject.force();
        if (subjectValue instanceof ListNilValue) {
            return; // successful match
        }else if (subjectValue instanceof ListConsValue) {
            throw new PatternMatchFailedException(this.toString());
        }else {
            throw new RuntimeTypeErrorException(
                String.format("cannot match %s against ListNil", subjectValue.getClass().getSimpleName()));
        }
    }

    @Override
    public Type inferPatternType(Inferer inferer, Map<String, Type> bindings) throws TypeErrorException {
        return inferer.freshVariable();
    }

    @Override
    public String toString() {
        return "[]";
    }
}
