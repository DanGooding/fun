import java.util.List;
import java.util.stream.Collectors;

public class TupleValue implements ConstantValue {

    private final List<Value> elements;

    TupleValue(List<Value> elements) {
        this.elements = List.copyOf(elements);
    }

    public int size() {
        return elements.size();
    }

    public Value getElement(int i) {
        return elements.get(i);
    }

    @Override
    public String toString() {
        return elements.stream()
            .map(Value::toString)
            .collect(Collectors.joining(", ", "(", ")"));
    }
}
