package fun.types;

import java.util.*;
import java.util.stream.Collectors;

public class TypeTuple extends Type {
    private List<Type> elements;

    public TypeTuple(List<Type> elements) {
        this.elements = List.copyOf(elements);
    }

    @Override
    public TypeTuple applySubstitution(Substitution s) {
        List<Type> modifiedElements =
            elements.stream().map(e -> e.applySubstitution(s)).collect(Collectors.toList());
        return new TypeTuple(modifiedElements);
    }

    @Override
    public Set<String> freeTypeVariables() {
        Set<String> free = new HashSet<>();
        for (Type element : elements) {
            free.addAll(element.freeTypeVariables());
        }
        return free;
    }

    @Override
    UnifyingAction dispatchUnify(Type t) throws UnificationFailureException {
        return t.unifyWithTuple(this);
    }

    @Override
    UnifyingAction unifyWithTuple(TypeTuple t) throws UnificationFailureException {
        if (elements.size() != t.elements.size()) {
            throw new UnificationFailureException(this, t);
        }

        List<Constraint> constraints = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++) {
            constraints.add(new Constraint(
                elements.get(i),
                t.elements.get(i)
            ));
        }

        return UnifyingAction.furtherUnify(constraints);
    }

    @Override
    public TypeTuple refreshVariableNames(VariableNameRefresher v) {
        List<Type> modifiedElements =
            elements.stream().map(e -> e.refreshVariableNames(v)).collect(Collectors.toList());
        return new TypeTuple(modifiedElements);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeTuple typeTuple = (TypeTuple) o;
        return elements.equals(typeTuple.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elements);
    }

    @Override
    public String toString() {
        return elements.stream()
            .map(Object::toString)
            .collect(Collectors.joining(", ", "(", ")"));
    }
}
