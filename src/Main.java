public class Main {

    public static void main(String[] args) {

        // achieving recursion without the Y combinator
        // using a let binding

        ASTNode ast =
            new ASTLet(
                "fac",
                new ASTFun(
                    "fac",
                    new ASTFun(
                        "n",

                        new ASTIf(
                            new ASTEquals(
                                new ASTVar("n"),
                                new ASTLiteralInt(0)
                            ),

                            new ASTLiteralInt(1),

                            // n * fac fac (n - 1)
                            new ASTMult(
                                new ASTVar("n"),
                                new ASTApply(
                                    new ASTApply(
                                        new ASTVar("fac"),
                                        new ASTVar("fac")
                                    ),
                                    new ASTMinus(
                                        new ASTVar("n"),
                                        new ASTLiteralInt(1)
                                    )
                                )
                            )
                        )
                    )
                ),

                new ASTApply(
                    new ASTApply(
                        new ASTVar("fac"),
                        new ASTVar("fac")
                    ),
                    new ASTLiteralInt(30)
                )
            );

        System.out.println(ast);

        IntValue result = (IntValue) ast.evaluate();

        System.out.println(result.getValue());
    }
}
