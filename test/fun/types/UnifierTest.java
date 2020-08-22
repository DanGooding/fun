package fun.types;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

public class UnifierTest {

    @Test(expected = UnificationFailureException.class)
    public void unifyTypes_throws_whenOccursCheckFails() throws UnificationFailureException {
        // ARRANGE
        Type left = new TypeVariable("a");
        Type right = new TypeArrow(new TypeVariable("b"), new TypeVariable("a"));

        // ACT
        Unifier.unifyTypes(left, right);
    }

    @Test
    public void unifyTypes_givesEmptySubstitution_forTypeVariableAndSelf() throws UnificationFailureException {
        // ARRANGE
        Type left = new TypeVariable("a");
        Type right = new TypeVariable("a");

        // ACT
        Substitution s = Unifier.unifyTypes(left, right);

        // ASSERT
        Map<String, Type> m = s.toMap();
        assertThat(m).isEmpty();
    }

    @Test
    public void unifyTypes_unifiesDistinctVariables() throws UnificationFailureException {
        // ARRANGE
        Type left = new TypeVariable("a");
        Type right = new TypeVariable("b");

        // ACT
        Substitution s = Unifier.unifyTypes(left, right);

        // ASSERT
        assertThat(left.applySubstitution(s)).isEqualTo(right.applySubstitution(s));
    }

    @Test
    public void unifyTypes_givesEmptySubstitution_forIntInt() throws UnificationFailureException {
        // ARRANGE
        Type left = new TypeInteger();
        Type right = new TypeInteger();

        // ACT
        Substitution s = Unifier.unifyTypes(left, right);

        // ASSERT
        Map<String, Type> m = s.toMap();
        assertThat(m).isEmpty();
    }
    @Test
    public void unifyTypes_givesEmptySubstitution_forBoolBool() throws UnificationFailureException {
        // ARRANGE
        Type left = new TypeBool();
        Type right = new TypeBool();

        // ACT
        Substitution s = Unifier.unifyTypes(left, right);

        // ASSERT
        Map<String, Type> m = s.toMap();
        assertThat(m).isEmpty();
    }

    @Test
    public void unifyTypes_unifiesChildren_forArrows() throws UnificationFailureException {
        // ARRANGE
        Type left = new TypeArrow(
            new TypeVariable("x"),
            new TypeArrow(
                new TypeInteger(),
                new TypeVariable("z")
            )
        );
        Type right = new TypeArrow(
            new TypeBool(),
            new TypeArrow(
                new TypeVariable("y"),
                new TypeBool()
            )
        );

        // ACT
        Substitution s = Unifier.unifyTypes(left, right);

        // ASSERT
        assertThat(s.get("x")).isInstanceOf(TypeBool.class);
        assertThat(s.get("y")).isInstanceOf(TypeInteger.class);
        assertThat(s.get("z")).isInstanceOf(TypeBool.class);
    }

    @Test
    public void unifyTypes_substitutesArrow_forVariable() throws UnificationFailureException {
        // ARRANGE
        Type left = new TypeArrow(
            new TypeVariable("b"),
            new TypeVariable("c")
        );
        Type right = new TypeVariable("a");

        // ACT
        Substitution s = Unifier.unifyTypes(left, right);

        // ASSERT
        assertThat(s.get("a")).isEqualTo(left);
    }

    @Test(expected = UnificationFailureException.class)
    public void unifyTypes_throws_forDifferentSizeTuples() throws UnificationFailureException {
        // ARRANGE
        Type left = new TypeTuple(List.of(
            new TypeInteger(),
            new TypeVariable("a"),
            new TypeBool()
        ));

        Type right = new TypeTuple(List.of(
            new TypeInteger(),
            new TypeBool()
        ));

        // ACT
        Unifier.unifyTypes(left, right);
    }

    @Test
    public void unifyTypes_unifies_forChainInTuple() throws UnificationFailureException {
        // ARRANGE
        Type left = new TypeTuple(List.of(
            new TypeVariable("a"),
            new TypeVariable("b"),
            new TypeVariable("c"),
            new TypeVariable("d"),
            new TypeVariable("e")
        ));
        Type right = new TypeTuple(List.of(
            new TypeVariable("b"),
            new TypeVariable("c"),
            new TypeVariable("d"),
            new TypeVariable("e"),
            new TypeInteger()
        ));

        // ACT
        Substitution s = Unifier.unifyTypes(left, right);

        // ASSERT
        Map<String, Type> m = s.toMap();

        assertThat(m.get("a")).isInstanceOf(TypeInteger.class);
        assertThat(m.get("b")).isInstanceOf(TypeInteger.class);
        assertThat(m.get("c")).isInstanceOf(TypeInteger.class);
        assertThat(m.get("d")).isInstanceOf(TypeInteger.class);
        assertThat(m.get("e")).isInstanceOf(TypeInteger.class);
    }

}
