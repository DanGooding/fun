package fun.types;

import org.junit.Test;

import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

public class SubstitutionTest {

    @Test
    public void substitution_replacesAllInstances() {
        // ARRANGE
        Type original =
            new TypeArrow(
                new TypeVariable("a"),
                new TypeArrow(
                    new TypeVariable("b"),
                    new TypeVariable("a")
                )
            );
        Substitution s =
            new Substitution("a", new TypeBool());

        // ACT
        Type result = original.applySubstitution(s);

        // ASSERT
        assertThat(result).isEqualTo(
            new TypeArrow(
                new TypeBool(),
                new TypeArrow(
                    new TypeVariable("b"),
                    new TypeBool()
                )
            )
        );
    }

    @Test
    public void composition_givesSameResult_asSeparate() {
        // ARRANGE
        Substitution s1 = new Substitution(Map.of(
            "a",
            new TypeArrow(
                new TypeVariable("c"),
                new TypeArrow(
                    new TypeVariable("d"),
                    new TypeBool()
                )
            ),

            "b",
            new TypeVariable("d")
        ));

        Substitution s2 = new Substitution(Map.of(
            "c", new TypeInteger(),
            "d",
            new TypeArrow(
                new TypeVariable("e"),
                new TypeVariable("e")
            )
        ));

        Type subject =
            new TypeArrow(
                new TypeVariable("a"),
                new TypeVariable("b")
            );

        // ACT
        Type resultSeparate = subject.applySubstitution(s1).applySubstitution(s2);
        Type resultComposed = subject.applySubstitution(s1.compose(s2));

        // ASSERT
        assertThat(resultComposed).isEqualTo(resultSeparate);
    }

}
