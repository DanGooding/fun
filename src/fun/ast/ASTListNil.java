package fun.ast;

import fun.eval.*;
import fun.types.*;
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
        return new TypeList(inferer.freshVariable());
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
