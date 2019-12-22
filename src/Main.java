import java.util.List;

public class Main {

    public static void main(String[] args) {


        // let (3, f, False, x, _) = (1 + 2, Fun (a, b) -> a * b, 1 == 2, 7, 0)
        // in f x 11

        ASTNode patternMatchDemo =
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
                    new ASTLambda(
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

        // let fac =
        // (let fac_open fac n -> if n == 0 then 1 else n * fac fac (n - 1) in fac_open fac_open)
        // in fac 6

        ASTNode factorialDemo =
            new ASTLet(
                "fac",
                new ASTLet(
                    "fac_open",
                    new ASTLambda(
                        "fac",
                        new ASTLambda(
                            "n",
                            new ASTIf(
                                new ASTEquals(
                                    new ASTVar("n"),
                                    new ASTLiteralInt(0)
                                ),
                                new ASTLiteralInt(1),
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
                        new ASTVar("fac_open"),
                        new ASTVar("fac_open")
                    )
                ),
                new ASTApply(
                    new ASTVar("fac"),
                    new ASTLiteralInt(6)
                )
            );


        // let length =
        // (let length_open length li =
        //    case li of
        //      (_, tl) -> 1 + length length tl
        //      ()      -> 0
        // in length_open length_open)
        // in length (1, (2, (3, ())))

        ASTNode listLengthDemo =
            new ASTLet(
                "length",
                new ASTLet(
                    "length_open",
                    new ASTLambda(
                        "length",
                        new ASTLambda(
                            "li",
                            new ASTCase(
                                new ASTVar("li"),
                                List.of(
                                    new ASTCaseOption(
                                        new ASTTuple(List.of(
                                            new ASTUnderscore(),
                                            new ASTVar("tl")
                                        )),
                                        new ASTPlus(
                                            new ASTLiteralInt(1),
                                            new ASTApply(
                                                new ASTApply(
                                                    new ASTVar("length"),
                                                    new ASTVar("length")
                                                ),
                                                new ASTVar("tl")
                                            )
                                        )
                                    ),
                                    new ASTCaseOption(
                                        new ASTTuple(List.of()),
                                        new ASTLiteralInt(0)
                                    )
                                )
                            )
                        )
                    ),
                    new ASTApply(
                        new ASTVar("length_open"),
                        new ASTVar("length_open")
                    )
                ),
                new ASTApply(
                    new ASTVar("length"),
                    new ASTTuple(List.of(
                        new ASTLiteralInt(1),
                        new ASTTuple(List.of(
                            new ASTLiteralInt(2),
                            new ASTTuple(List.of(
                                new ASTLiteralInt(3),
                                new ASTTuple(List.of(
                                    new ASTLiteralInt(4),
                                    new ASTTuple(List.of())
                                ))
                            ))
                        ))
                    ))
                )
            );

        ASTNode ast = listLengthDemo;

        System.out.println(ast);

        try {
            Value result = ast.evaluate();
            System.out.println(result);

        } catch (EvaluationException e) {
            System.out.println(e);
        }

    }
}
