package fun;

import fun.ast.ASTNode;
import fun.eval.EvaluationException;
import fun.parser.Parser;
import fun.types.*;
import fun.values.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {

        String filename;
        if (args.length == 0) {
            filename = "code-samples/program.txt";
        }else {
            filename = args[0];
        }

        try {
            String content = Files.readString(Paths.get(filename));

            Parser p = new Parser(content);

            ASTNode expr = p.parseExpr();

            try {
                Scheme type =
                    Inferer
                        .inferType(expr)
                        .refreshVariableNames(new VariableNameRefresher());

                System.out.println(type.prettyPrint());

                Value result = expr.evaluate();
                result.fullyForce();

                System.out.println(result);

            } catch (TypeErrorException | EvaluationException e) {
                System.out.println(e);
            }

        } catch (IOException e) {
            System.out.println(e);
        }

    }
}
