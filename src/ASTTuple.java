import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// TODO: tuple base class - extended by this, TuplePattern & TupleValue
public class ASTTuple extends ASTNode {

    private final List<ASTNode> elements;

    public ASTTuple(List<ASTNode> elements) {
//        if (contents.length == 1) {
//            // not really a 'type' error
//            throw new UnsupportedOperationException("1-tuples not possible");
//        }

        this.elements = List.copyOf(elements);
    }

    public int size() {
        return elements.size();
    }

    @Override
    Value evaluate(Environment env) throws EvaluationException {
//        List<Value> elementValues = elements.stream().map(e -> e.evaluate(env)).collect(Collectors.toList());
        List<Value> elementValues = new ArrayList<>(elements.size());
        for (ASTNode e : elements) {
            elementValues.add(e.evaluate(env));
        }
        return new TupleValue(elementValues);
    }

    @Override
    public String toString() {
        return elements.stream()
            .map(ASTNode::toString)
            .collect(Collectors.joining(", ", "(", ")"));
    }
}
