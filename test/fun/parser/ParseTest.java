package fun.parser;

import fun.ast.ASTNode;
import fun.eval.EvaluationException;
import fun.values.BoolValue;
import fun.values.IntegerValue;
import fun.values.Value;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class ParseTest {

    @Test
    public void worksCorrectly_forLeftAssoc() throws EvaluationException, ParseErrorException {
        // ARRANGE
        String input = "1 + 2 * 3 - 4 * 5";

        // ACT
        ASTNode ast = Parser.parseWholeExpr(input);
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntegerValue.class);
        assertThat(((IntegerValue) result).getValue().intValueExact()).isEqualTo(-13);
    }

    @Test
    public void worksCorrectly_forRightAssoc() throws EvaluationException, ParseErrorException {
        // ARRANGE
        String input = "4 ^ 3 ^ 2 ^ 1";

        // ACT
        ASTNode ast = Parser.parseWholeExpr(input);
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntegerValue.class);
        assertThat(((IntegerValue) result).getValue().intValueExact()).isEqualTo(262144);
    }

    @Test
    public void worksCorrectly_forAssocMixture() throws EvaluationException, ParseErrorException {
        // ARRANGE
        String input = "1 + 2 - 3 * 4 * 5 ^ 2 * 3 - 4 ^ 2 ^ 0 - 1 + 1000 == 10 * 9 + 2 ^ 3";

        // ACT
        ASTNode ast = Parser.parseWholeExpr(input);
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(BoolValue.class);
        assertThat(((BoolValue) result).getValue()).isEqualTo(true);
    }

    @Test(expected = ParseErrorException.class)
    public void throws_forMultipleNonAssociative() throws EvaluationException, ParseErrorException {
        // ARRANGE
        String input = "1 == 2 == 3";

        // ACT
        ASTNode ast = Parser.parseWholeExpr(input);
        Value result = ast.evaluate();

    }

    // TODO: tuples, function application (inc multiple args)

    @Test
    public void parsesCons_inPatterns() throws EvaluationException, ParseErrorException {
        // ARRANGE
        String input = "let (x:y:z:zs) = [1,2,3,4,5] in z";

        // ACT
        Value result = Parser.parseWholeExpr(input).evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntegerValue.class);
        assertThat(((IntegerValue)result).getValue().intValueExact()).isEqualTo(3);
    }

    @Test
    public void doesntRequireBrackets_aroundCons_inCasePattern() throws ParseErrorException {
        // ARRANGE
        String input =
            "case [1,2,3] of\n" +
                "    x : y : _ : [] -> y\n" +
                "    x : xs -> x\n";

        // ACT
        Parser.parseWholeExpr(input);
    }

    @Test
    public void allowsLeadingAndTrailingWhitespace() throws ParseErrorException {
        // ARRANGE
        String input = "     1 + 2 * 3    ";

        // ACT
        Parser.parseWholeExpr(input);
    }

    @Test
    public void allowsComments() throws EvaluationException, ParseErrorException {
        // ARRANGE
        String input = "" +
            "# this adds one and two\n" +
            "1  # this is one\n" +
            "+  # this is the addition operator\n" +
            "2  # this is two\n" +
            "# the answer\n" +
            "# will be three\n";

        // ACT
        ASTNode expr = Parser.parseWholeExpr(input);
        Value result = expr.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntegerValue.class);
        assertThat(((IntegerValue)result).getValue().intValueExact()).isEqualTo(3);
    }


}
