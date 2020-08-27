package fun.ast;

import fun.eval.*;
import fun.types.Inferer;
import fun.types.Type;
import fun.types.TypeErrorException;
import fun.values.ListConsValue;
import fun.values.ListNilValue;
import fun.values.Value;

import java.util.Map;

public class ASTListConsPattern implements ASTMatchable {

    private final ASTMatchable headPattern;
    private final ASTMatchable tailPattern;

    public ASTListConsPattern(ASTMatchable headPattern, ASTMatchable tailPattern) {
        this.headPattern = headPattern;
        this.tailPattern = tailPattern;
    }

    @Override
    public void bindMatch(Thunk subject, Environment<Thunk> env) throws PatternMatchFailedException, EvaluationException {
        Value subjectValue = subject.force();
        if (subjectValue instanceof ListConsValue) {
            ListConsValue listSubject = (ListConsValue)subjectValue;
            headPattern.bindMatch(listSubject.getHead(), env);
            tailPattern.bindMatch(listSubject.getTail(), env);

        }else if (subjectValue instanceof ListNilValue) {
          throw new PatternMatchFailedException(this.toString());

        } else {
            throw new RuntimeTypeErrorException(
                String.format("cannot match %s against ListCons", subjectValue.getClass().getSimpleName()));
        }

    }

    @Override
    public Type inferPatternType(Inferer inferer, Map<String, Type> bindings) throws TypeErrorException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return String.format("(%s : %s)", headPattern, tailPattern);
    }
}
