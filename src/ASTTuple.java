import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ASTTuple extends ASTMatchable {

    private List<ASTNode> elements;

    public ASTTuple(List<ASTNode> elements) {
//        if (contents.length == 1) {
//            // not really a 'type' error
//            throw new UnsupportedOperationException("1-tuples not possible");
//        }

        this.elements = List.copyOf(elements);
    }

    public static ASTTuple unit() {
        return new ASTTuple(List.of());
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
    void bindMatch(Value subject, Environment env) throws PatternMatchException {
        if (!(subject instanceof TupleValue)) {
            throw new PatternMatchException("tuple expected, got " + subject);
        }
        TupleValue tupleSubject = (TupleValue) subject;
        if (tupleSubject.size() != this.size()) {
            throw new PatternMatchException(String.format(
                "%d-tuple expected, got %d-tuple", this.size(), ((TupleValue) subject).size()));
        }
        for (int i = 0; i < size(); i++) {
            ASTNode element = elements.get(i);
            if (!(element instanceof ASTMatchable)) {
                // TODO: this is an error not a pattern match exception
                //  the user's code is wrong if they try and
                throw new PatternMatchException("cannot match against " + subject);
            }
            ((ASTMatchable) element).bindMatch(tupleSubject.getElement(i), env);
        }
    }

    @Override
    public String toString() {
        return elements.stream()
            .map(ASTNode::toString)
            .collect(Collectors.joining(", ", "(", ")"));
    }
}
