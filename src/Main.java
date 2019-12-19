public class Main {

    public static void main(String[] args) {
        // let x = 1 + 2 in let y = x * x in x * y
        ASTNode ast =
            new ASTLet(
                "x",
                new ASTPlus(
                    new ASTLiteral(1),
                    new ASTLiteral(2)
                ),
                new ASTLet(
                    "y",
                    new ASTMult(
                        new ASTVar("x"),
                        new ASTVar("x")
                    ),
                    new ASTMult(
                        new ASTVar("x"),
                        new ASTVar("y")
                    )

                )
            );

        System.out.println(ast);

        ConstantValue result = ast.evaluate();

        System.out.println(result.getValue());
    }
}
