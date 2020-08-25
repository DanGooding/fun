package fun.eval;

import fun.ast.*;
import fun.eval.EvaluationException;
import fun.values.IntegerValue;
import fun.values.Value;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class EvaluateTest {

    @Test
    public void addition_worksCorrectly() throws EvaluationException {
        // ARRANGE
        ASTNode ast =
            new ASTPlus(
                new ASTLiteralInteger(2),
                new ASTLiteralInteger(3)
            );

        // ACT
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntegerValue.class);
        assertThat(((IntegerValue) result).getValue().intValueExact()).isEqualTo(5);
    }

    @Test
    public void subtraction_worksCorrectly() throws EvaluationException {
        // ARRANGE
        ASTNode ast =
            new ASTMinus(
                new ASTLiteralInteger(5),
                new ASTLiteralInteger(7)
            );

        // ACT
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntegerValue.class);
        assertThat(((IntegerValue) result).getValue().intValueExact()).isEqualTo(-2);
    }

    @Test
    public void multiplication_worksCorrectly() throws EvaluationException {
        // ARRANGE
        ASTNode ast =
            new ASTMult(
                new ASTLiteralInteger(3),
                new ASTLiteralInteger(13)
            );

        // ACT
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntegerValue.class);
        assertThat(((IntegerValue) result).getValue().intValueExact()).isEqualTo(39);
    }

    @Test
    public void nestedLet_worksCorrectly() throws EvaluationException {
        // ARRANGE
        // let x = 2 in let y = x * x in let z = x * y in x + y + z
        ASTNode ast =
            new ASTLet(
                "x",
                new ASTLiteralInteger(2),
                new ASTLet(
                    "y",
                    new ASTMult(
                        new ASTVar("x"),
                        new ASTVar("x")
                    ),
                    new ASTLet(
                        "z",
                        new ASTMult(
                            new ASTVar("x"),
                            new ASTVar("y")
                        ),
                        new ASTPlus(
                            new ASTPlus(
                                new ASTVar("x"),
                                new ASTVar("y")
                            ),
                            new ASTVar("z")
                        )
                    )
                )

            );

        // ACT
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntegerValue.class);
        assertThat(((IntegerValue) result).getValue().intValueExact()).isEqualTo(14);
    }

    @Test
    public void innerLet_doesntAffectOuterLetEnvironment() throws EvaluationException {
        // ARRANGE

        // let x = 1 in (let x = 2 in 0) + x
        ASTNode ast =
            new ASTLet(
                "x",
                new ASTLiteralInteger(1),
                new ASTPlus(
                    new ASTLet(
                        "x",
                        new ASTLiteralInteger(2),
                        new ASTLiteralInteger(0)
                    ),
                    new ASTVar("x")
                )
            );

        // ACT
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntegerValue.class);
        assertThat(((IntegerValue) result).getValue().intValueExact()).isEqualTo(1);
    }

    @Test
    public void currying_worksCorrectly() throws EvaluationException {
        // ARRANGE

        // let add = fun x -> fun y -> x + y
        // in let addOne = add 1
        // in addOne 2

        ASTNode ast =
            new ASTLet(
                "add",
                new ASTLambda(
                    "x",
                    new ASTLambda(
                        "y",
                        new ASTPlus(
                            new ASTVar("x"),
                            new ASTVar("y")
                        )
                    )
                ),
                new ASTLet(
                    "addOne",
                    new ASTApply(
                        new ASTVar("add"),
                        new ASTLiteralInteger(1)
                    ),
                    new ASTApply(
                        new ASTVar("addOne"),
                        new ASTLiteralInteger(2)
                    )
                )
            );

        // ACT
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntegerValue.class);
        assertThat(((IntegerValue) result).getValue().intValueExact()).isEqualTo(3);
    }

    @Test
    public void functionScoping_isStatic() throws EvaluationException {
        // ARRANGE

        // let x = 1
        //  in let f = fun y -> x + y
        //   in let x = 2
        //    in f 0

        ASTNode ast =
            new ASTLet(
                "x",
                new ASTLiteralInteger(1),
                new ASTLet(
                    "f",
                    new ASTLambda(
                        "y",
                        new ASTPlus(
                            new ASTVar("x"),
                            new ASTVar("y")
                        )
                    ),
                    new ASTLet(
                        "x",
                        new ASTLiteralInteger(2),
                        new ASTApply(
                            new ASTVar("f"),
                            new ASTLiteralInteger(0)
                        )
                    )
                )
            );

        // ACT
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntegerValue.class);
        assertThat(((IntegerValue) result).getValue().intValueExact()).isEqualTo(1);

    }

}
