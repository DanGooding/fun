package fun.types;

import fun.ast.*;
import org.junit.Test;

import java.util.*;

import static com.google.common.truth.Truth.assertThat;

public class InfererTest {

    void assertEquivalent(Scheme left, Scheme right) {
        Scheme standardLeft = left.refreshVariableNames(new VariableNameRefresher());
        Scheme standardRight = right.refreshVariableNames(new VariableNameRefresher());
        assertThat(standardLeft).isEqualTo(standardRight);
    }

    @Test
    public void infersCorrectType_forIdentityLambda() throws TypeErrorException {
        // ARRANGE
        ASTNode expr =
            new ASTLambda(
                "x",
                new ASTVar("x")
            );

        // ACT
        Scheme inferred = Inferer.inferType(expr);

        // ASSERT
        Scheme idScheme =
            new Scheme(
                List.of("a"),
                new TypeArrow(
                    new TypeVariable("a"),
                    new TypeVariable("a")
                )
            );
        assertEquivalent(inferred, idScheme);
    }

    @Test
    public void infersCorrectType_forConstFunction() throws TypeErrorException {
        // ARRANGE
        // lambda x -> lambda y -> x
        ASTNode expr =
            new ASTLambda("x",
                new ASTLambda("y",
                    new ASTVar("x")
                )
            );

        // ACT
        Scheme inferred = Inferer.inferType(expr);

        // ASSERT
        Scheme correct =
            new Scheme(
                List.of("a", "b"),
                new TypeArrow(
                    new TypeVariable("a"),
                    new TypeArrow(
                        new TypeVariable("b"),
                        new TypeVariable("a")
                    )
                )
            );
        assertEquivalent(inferred, correct);
    }

    @Test(expected = TypeErrorException.class)
    public void inferType_throws_forOmegaCombinator() throws TypeErrorException {
        // ARRANGE
        ASTNode expr =
            new ASTLambda("x",
                new ASTApply(
                    new ASTVar("x"),
                    new ASTVar("x")
                )
            );

        // ACT
        Inferer.inferType(expr);
    }

    @Test(expected = TypeErrorException.class)
    public void inferType_throws_forUnboundVariable() throws TypeErrorException {
        // ARRANGE
        ASTNode expr = new ASTVar("x");

        // ACT
        Inferer.inferType(expr);
    }

    @Test
    public void inferType_infersInt_withArithmeticOperators() throws TypeErrorException {
        // ARRANGE
        ASTNode expr =
            new ASTLambda("x",
                new ASTLambda("y",
                    new ASTLambda("z",
                        new ASTPlus(
                            new ASTVar("x"),
                            new ASTMult(
                                new ASTVar("y"),
                                new ASTVar("z")
                            )
                        )
                    )
                )
            );

        // ACT
        Scheme inferred = Inferer.inferType(expr);

        // ASSERT
        Scheme correct =
            new Scheme(
                List.of(),
                new TypeArrow(
                    new TypeInteger(),
                    new TypeArrow(
                        new TypeInteger(),
                        new TypeArrow(
                            new TypeInteger(),
                            new TypeInteger()
                        )
                    )
                )
            );

        assertEquivalent(inferred, correct);
    }

    @Test
    public void inferType_handlesConvolutedId() throws TypeErrorException {
        // ARRANGE
        // let id = (lambda x -> let y = x in y) in id id
        ASTNode expr =
            new ASTLet("id",
                new ASTLambda("x",
                    new ASTLet("y",
                        new ASTVar("x"),
                        new ASTVar("y")
                    )
                ),
                new ASTApply(
                    new ASTVar("id"),
                    new ASTVar("id")
                )
            );

        // ACT
        Scheme inferred = Inferer.inferType(expr);

        // ASSERT
        Scheme correct =
            new Scheme(
                List.of("a"),
                new TypeArrow(
                    new TypeVariable("a"),
                    new TypeVariable("a")
                )
            );

        assertEquivalent(inferred, correct);
    }

    @Test
    public void inferType_introducesPolyType_inLetBinding() throws TypeErrorException {
        // ARRANGE
        // let id = lambda x -> x in (id 1, id True)
        ASTNode expr =
            new ASTLet(
                new ASTVar("id"),
                new ASTLambda("x",
                    new ASTVar("x")
                ),
                new ASTTuple(List.of(
                    new ASTApply(
                        new ASTVar("id"),
                        new ASTLiteralInteger(2)
                    ),
                    new ASTApply(
                        new ASTVar("id"),
                        new ASTLiteralBool(false)
                    )
                ))
            );

        // ACT
        Scheme inferred = Inferer.inferType(expr);

        // ASSERT
        Scheme correct =
            new Scheme(
                List.of(),
                new TypeTuple(List.of(
                    new TypeInteger(),
                    new TypeBool()
                ))
            );

        assertEquivalent(inferred, correct);
    }

    @Test(expected = TypeErrorException.class)
    public void inferType_doesntIntroducePolyType_inLambda() throws TypeErrorException {
        // ARRANGE
        // (lambda id -> (id 2, id False)) (lambda x -> x)
        ASTNode expr =
            new ASTApply(
                new ASTLambda(
                    "id",
                    new ASTTuple(List.of(
                        new ASTApply(
                            new ASTVar("id"),
                            new ASTLiteralInteger(2)
                        ),
                        new ASTApply(
                            new ASTVar("id"),
                            new ASTLiteralBool(false)
                        )
                    ))
                ),
                new ASTLambda("x",
                    new ASTVar("x")
                )
            );

        // ACT
        Inferer.inferType(expr);
    }

    @Test
    public void inferType_correctlyTypes_recursiveFunction() throws TypeErrorException {
        // ARRANGE
        // let fact = lambda n -> if n == 0 then 1 else n * fact(n - 1) in fact
        ASTNode expr =
            new ASTLet(
                new ASTVar("fact"),
                new ASTLambda("n",
                    new ASTIf(
                        new ASTEquals(
                            new ASTVar("n"),
                            new ASTLiteralInteger(0)
                        ),
                        new ASTLiteralInteger(1),
                        new ASTMult(
                            new ASTVar("n"),
                            new ASTApply(
                                new ASTVar("fact"),
                                new ASTMinus(
                                    new ASTVar("n"),
                                    new ASTLiteralInteger(1)
                                )
                            )
                        )
                    )
                ),
                new ASTVar("fact")
            );

        // ACT
        Scheme inferred = Inferer.inferType(expr);

        // ASSERT
        // int -> int
        Scheme correct =
            new Scheme(List.of(),
                new TypeArrow(
                    new TypeInteger(),
                    new TypeInteger()
                )
            );

        assertEquivalent(inferred, correct);
    }

    @Test
    public void inferType_infersIfConditionBoolean() throws TypeErrorException {
        // ARRANGE
        ASTNode expr =
            new ASTLambda("c",
                new ASTIf(
                    new ASTVar("c"),
                    new ASTLiteralInteger(1),
                    new ASTLiteralInteger(0)
                )
            );

        // ACT
        Scheme inferred = Inferer.inferType(expr);

        // ASSERT
        Scheme correct =
            new Scheme(
                List.of(),
                new TypeArrow(
                    new TypeBool(),
                    new TypeInteger()
                )
            );

        assertEquivalent(inferred, correct);
    }

    @Test
    public void inferType_infersIfBranchesSame() throws TypeErrorException {
        // ARRANGE
        ASTNode expr =
            new ASTLambda("y",
                new ASTLambda("n",
                    new ASTIf(
                        new ASTLiteralBool(false),
                        new ASTVar("y"),
                        new ASTVar("n")
                    )
                )
            );

        // ACT
        Scheme inferred = Inferer.inferType(expr);

        // ASSERT
        Scheme correct =
            new Scheme(
                List.of("a"),
                new TypeArrow(
                    new TypeVariable("a"),
                    new TypeArrow(
                        new TypeVariable("a"),
                        new TypeVariable("a")
                    )
                )
            );

        assertEquivalent(inferred, correct);
    }

    @Test
    public void inferType_infersLessThanSubjectsInteger() throws TypeErrorException {
        // ARRANGE
        ASTNode expr =
            new ASTLambda("x",
                new ASTLambda("y",
                    new ASTLessThan(
                        new ASTVar("x"),
                        new ASTVar("y")
                    )
                )
            );

        // ACT
        Scheme inferred = Inferer.inferType(expr);

        // ASSERT
        Scheme correct =
            new Scheme(List.of(),
                new TypeArrow(
                    new TypeInteger(),
                    new TypeArrow(
                        new TypeInteger(),
                        new TypeBool()
                    )
                )
            );

        assertEquivalent(inferred, correct);
    }

    @Test
    public void inferType_infersTupleArgument_onPatternMatch() throws TypeErrorException {
        // ARRANGE
        ASTNode expr =
            new ASTLambda(
                new ASTTuplePattern(List.of(
                    new ASTVar("x"),
                    new ASTVar("y"),
                    new ASTVar("z"),
                    new ASTUnderscore()
                )),
                new ASTVar("y")
            );

        // ACT
        Scheme inferred = Inferer.inferType(expr);

        // ASSERT
        Scheme correct =
            new Scheme(
                List.of("a", "b", "c", "d"),
                new TypeArrow(
                    new TypeTuple(List.of(
                        new TypeVariable("a"),
                        new TypeVariable("b"),
                        new TypeVariable("c"),
                        new TypeVariable("d")
                    )),
                    new TypeVariable("b")
                )
            );

        assertEquivalent(inferred, correct);
    }

    @Test
    public void inferType_unifiesAcrossPatterns_forCase() throws TypeErrorException {
        // ASSERT

        // lambda t -> case t of
        //     (0, b) -> b
        //     (a, _) -> a
        // :: (Int, Int) -> Int
        ASTNode expr =
            new ASTLambda("t",
                new ASTCase(
                    new ASTVar("t"),
                    List.of(
                        new ASTCaseOption(
                            new ASTTuplePattern(List.of(
                                new ASTLiteralInteger(0),
                                new ASTVar("b")
                            )),
                            new ASTVar("b")
                        ),
                        new ASTCaseOption(
                            new ASTTuplePattern(List.of(
                                new ASTVar("a"),
                                new ASTUnderscore()
                            )),
                            new ASTVar("a")
                        )
                    )
                )
            );

        // ACT
        Scheme inferred = Inferer.inferType(expr);

        // ASSERT
        Scheme correct =
            new Scheme(List.of(),
                new TypeArrow(
                    new TypeTuple(List.of(
                        new TypeInteger(),
                        new TypeInteger()
                    )),
                    new TypeInteger()
                )
            );

        assertEquivalent(inferred, correct);
    }

    @Test
    public void inferTypes_unifiesAcrossBodies_forCase() throws TypeErrorException {
        // ARRANGE

        // lambda t -> case t of
        //     (1, _) -> 5
        //     (2, x) -> x
        // :: (Int, Int) -> Int
        ASTNode expr =
            new ASTLambda("t",
                new ASTCase(
                    new ASTVar("t"),
                    List.of(
                        new ASTCaseOption(
                            new ASTTuplePattern(List.of(
                                new ASTLiteralInteger(1),
                                new ASTUnderscore()
                            )),
                            new ASTLiteralInteger(5)
                        ),
                        new ASTCaseOption(
                            new ASTTuplePattern(List.of(
                                new ASTLiteralInteger(2),
                                new ASTVar("x")
                            )),
                            new ASTVar("x")
                        )
                    )
                )
            );

        // ACT
        Scheme inferred = Inferer.inferType(expr);

        // ASSERT
        Scheme correct =
            new Scheme(List.of(),
                new TypeArrow(
                    new TypeTuple(List.of(
                        new TypeInteger(),
                        new TypeInteger()
                    )),
                    new TypeInteger()
                )
            );

        assertEquivalent(inferred, correct);
    }

    // TODO: add lots more, incl difficult cases
    // ASTCase (matching options & bodies)
    // in particular, once add lists, can infer e.g.
    //   (x : 1 : y : []) gives x, y :: Integer

}
