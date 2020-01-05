package fun.ast;

import fun.eval.Environment;
import fun.eval.EvaluationException;
import fun.eval.Thunk;
import fun.values.TupleValue;
import fun.values.Value;

import java.util.List;
import java.util.stream.Collectors;

// TODO: tuple base class - extended by this, TuplePattern & fun.values.TupleValue
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
    public Value evaluate(Environment env) {
        List<Thunk> elementThunks = elements.stream().map(e -> new Thunk(e, env)).collect(Collectors.toList());
        return new TupleValue(elementThunks);
    }

    @Override
    public String toString() {
        return elements.stream()
            .map(ASTNode::toString)
            .collect(Collectors.joining(", ", "(", ")"));
    }
}
