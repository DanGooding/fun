package fun.types;

import java.util.*;

public class Scheme implements TypeLike {
    private Set<String> quantified;
    private Type body;

    public Scheme(Collection<String> quantified, Type body) {
        this.quantified = Set.copyOf(quantified);
        this.body = body;
    }

    public Scheme(Type body) {
        this(Set.of(), body);
    }

    @Override
    public Scheme applySubstitution(Substitution s) {
        Substitution restricted = s.without(quantified);
        return new Scheme(quantified, body.applySubstitution(restricted));
    }

    @Override
    public Set<String> freeTypeVariables() {
        Set<String> free = body.freeTypeVariables();
        free.removeAll(quantified);
        return free;
    }

    /**
     * construct a new equivalent Scheme, with type variables named
     * consecutively in order of appearance (in the body)
     */
    @Override
    public Scheme refreshVariableNames(VariableNameRefresher v) {
        Set<String> newQuantified = new HashSet<>();
        // all names (including quantified) are refreshed
        Type newBody = body.refreshVariableNames(v);
        for (String name : quantified) {
            newQuantified.add(v.refreshName(name));
        }
        return new Scheme(newQuantified, newBody);
    }

    @Override
    public Type instantiate(Inferer inferer) {
        return inferer.instantiate(this);
    }

    public Type getBody() {
        return body;
    }

    public List<String> getQuantified() {
        return new ArrayList<>(quantified);
    }

    @Override
    public String toString() {
        return String.format(
            "forall %s. %s",
            String.join(" ", quantified),
            body
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scheme scheme = (Scheme) o;
        return quantified.equals(scheme.quantified) &&
            body.equals(scheme.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantified, body);
    }
}
