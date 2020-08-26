package fun.ast;

import fun.eval.*;
import fun.types.Inferer;
import fun.types.Type;
import fun.types.TypeErrorException;
import fun.types.TypeTuple;
import fun.values.TupleValue;
import fun.values.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
            throw new RuntimeTypeErrorException(
                String.format("cannot match %s against Tuple", subjectValue.getClass().getSimpleName()));
        }
        TupleValue tupleSubject = (TupleValue) subjectValue;
        if (tupleSubject.size() != this.size()) {
            throw new RuntimeTypeErrorException(String.format(
                "cannot match %d-tuple against %d-tuple", tupleSubject.size(),this.size()));
        }
        for (int i = 0; i < size(); i++) {
            elements.get(i).bindMatch(tupleSubject.getElement(i), env);
        }
    }

    @Override
    public Type inferPatternType(Inferer inferer, Map<String, Type> bindings) throws TypeErrorException {
        List<Type> elementTypes = new ArrayList<>();
        for (ASTMatchable element : elements) {
            elementTypes.add(element.inferPatternType(inferer, bindings));
        }
        return new TypeTuple(elementTypes);
    }

    @Override
    public String toString() {
        return elements.stream()
            .map(ASTMatchable::toString)
            .collect(Collectors.joining(", ", "(", ")"));
    }
}
