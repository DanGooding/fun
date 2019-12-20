import java.util.List;

public class Main {

    public static void main(String[] args) {

        // let (3, f, False, x, _) = (1 + 2, Fun (a, b) -> a * b, 1 == 2, 7, 0)
        // in f x 11

        ASTNode ast =
            new ASTLet(
                new ASTTuple(List.of(
                    new ASTLiteralInt(3),
                    new ASTVar("f"),
                    new ASTLiteralBool(false),
                    new ASTVar("x"),
                    new ASTUnderscore()
                )),

                new ASTTuple(List.of(
                    new ASTPlus(
                        new ASTLiteralInt(1),
                        new ASTLiteralInt(2)
                    ),
                    new ASTFun(
                        new ASTTuple(List.of(
                            new ASTVar("a"),
                            new ASTVar("b")
                        )),
                        new ASTMult(
                            new ASTVar("a"),
                            new ASTVar("b")
                        )
                    ),
                    new ASTEquals(
                        new ASTLiteralInt(1),
                        new ASTLiteralInt(2)
                    ),
                    new ASTLiteralInt(7),
                    new ASTLiteralInt(0)
                )),

                new ASTApply(
                    new ASTVar("f"),
                    new ASTTuple(List.of(
                        new ASTVar("x"),
                        new ASTLiteralInt(11)
                    ))
                )
            );

        System.out.println(ast);

        try {
            Value result = ast.evaluate();
            System.out.println(result);

        } catch (EvaluationException e) {
            System.out.println(e);
        }

    }
}
