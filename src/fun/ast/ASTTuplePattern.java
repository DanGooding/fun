package fun.ast;

import fun.eval.Environment;
import fun.eval.EvaluationException;
import fun.eval.PatternMatchFailedException;
import fun.eval.Thunk;
import fun.values.TupleValue;
import fun.values.Value;

import java.util.List;
import java.util.stream.Collectors;

public class ASTTuplePattern implements ASTMatchable {

    private final List<ASTMatchable> elements;

    public ASTTuplePattern(List<ASTMatchable> elements) {
        this.elements = List.copyOf(elements);
    }

    public int size() {
        return elements.size();
    }

    @Override
    public void bindMatch(Thunk subject, Environment<Thunk> env) throws PatternMatchFailedException, EvaluationException {
        Value subjectValue = subject.force();

        if (!(subjectValue instanceof TupleValue)) {
            throw new PatternMatchFailedException("tuple expected, got " + subject);
        }
        TupleValue tupleSubject = (TupleValue) subjectValue;
        if (tupleSubject.size() != this.size()) {
            throw new PatternMatchFailedException(String.format(
                "%d-tuple expected, got %d-tuple", this.size(), tupleSubject.size()));
        }
        for (int i = 0; i < size(); i++) {
            elements.get(i).bindMatch(tupleSubject.getElement(i), env);
        }
    }

    @Override
    public String toString() {
        return elements.stream()
            .map(ASTMatchable::toString)
            .collect(Collectors.joining(", ", "(", ")"));
    }
}
