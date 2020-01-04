import java.util.List;
import java.util.stream.Collectors;

public class TupleValue implements ConstantValue {

    private final List<Thunk> elements;

    TupleValue(List<Thunk> elements) {
        this.elements = List.copyOf(elements);
    }

    int size() {
        return elements.size();
    }

    Thunk getElement(int i) {
        return elements.get(i);
    }

    @Override
    public void fullyForce() throws EvaluationException {
        for (Thunk t : elements) {
            t.force().fullyForce();
        }
    }

    @Override
    public String toString() {
        return elements.stream()
            .map(Thunk::toString)
            .collect(Collectors.joining(", ", "(", ")"));
    }
}
