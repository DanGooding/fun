import java.util.List;
import java.util.stream.Collectors;

public class ASTTuplePattern implements ASTMatchable {

    private final List<ASTMatchable> elements;

    ASTTuplePattern(List<ASTMatchable> elements) {
        this.elements = List.copyOf(elements);
    }

    int size() {
        return elements.size();
    }

    @Override
    public void bindMatch(Value subject, Environment env) throws PatternMatchFailedException {
        if (!(subject instanceof TupleValue)) {
            throw new PatternMatchFailedException("tuple expected, got " + subject);
        }
        TupleValue tupleSubject = (TupleValue) subject;
        if (tupleSubject.size() != this.size()) {
            throw new PatternMatchFailedException(String.format(
                "%d-tuple expected, got %d-tuple", this.size(), ((TupleValue) subject).size()));
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
