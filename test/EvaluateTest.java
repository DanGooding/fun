import org.junit.Test;

import java.util.function.IntBinaryOperator;

import static com.google.common.truth.Truth.assertThat;

public class EvaluateTest {

    @Test
    public void addition_worksCorrectly() throws EvaluationException {
        // ARRANGE
        ASTNode ast =
            new ASTPlus(
                new ASTLiteralInt(2),
                new ASTLiteralInt(3)
            );

        // ACT
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntValue.class);
        assertThat(((IntValue) result).getValue()).isEqualTo(5);
    }

    @Test
    public void subtraction_worksCorrectly() throws EvaluationException {
        // ARRANGE
        ASTNode ast =
            new ASTMinus(
                new ASTLiteralInt(5),
                new ASTLiteralInt(7)
            );

        // ACT
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntValue.class);
        assertThat(((IntValue) result).getValue()).isEqualTo(-2);
    }

    @Test
    public void multiplication_worksCorrectly() throws EvaluationException {
        // ARRANGE
        ASTNode ast =
            new ASTMult(
                new ASTLiteralInt(3),
                new ASTLiteralInt(13)
            );

        // ACT
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntValue.class);
        assertThat(((IntValue) result).getValue()).isEqualTo(39);
    }

    @Test
    public void nestedLet_worksCorrectly() throws EvaluationException {
        // ARRANGE
        // let x = 2 in let y = x * x in let z = x * y in x + y + z
        ASTNode ast =
            new ASTLet(
                "x",
                new ASTLiteralInt(2),
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
        assertThat(result).isInstanceOf(IntValue.class);
        assertThat(((IntValue) result).getValue()).isEqualTo(14);
    }

    @Test
    public void innerLet_doesntAffectOuterLetEnvironment() throws EvaluationException {
        // ARRANGE

        // let x = 1 in (let x = 2 in 0) + x
        ASTNode ast =
            new ASTLet(
                "x",
                new ASTLiteralInt(1),
                new ASTPlus(
                    new ASTLet(
                        "x",
                        new ASTLiteralInt(2),
                        new ASTLiteralInt(0)
                    ),
                    new ASTVar("x")
                )
            );

        // ACT
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntValue.class);
        assertThat(((IntValue) result).getValue()).isEqualTo(1);
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
                new ASTFun(
                    "x",
                    new ASTFun(
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
                        new ASTLiteralInt(1)
                    ),
                    new ASTApply(
                        new ASTVar("addOne"),
                        new ASTLiteralInt(2)
                    )
                )
            );

        // ACT
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntValue.class);
        assertThat(((IntValue) result).getValue()).isEqualTo(3);
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
                new ASTLiteralInt(1),
                new ASTLet(
                    "f",
                    new ASTFun(
                        "y",
                        new ASTPlus(
                            new ASTVar("x"),
                            new ASTVar("y")
                        )
                    ),
                    new ASTLet(
                        "x",
                        new ASTLiteralInt(2),
                        new ASTApply(
                            new ASTVar("f"),
                            new ASTLiteralInt(0)
                        )
                    )
                )
            );

        // ACT
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntValue.class);
        assertThat(((IntValue) result).getValue()).isEqualTo(1);

    }

}
