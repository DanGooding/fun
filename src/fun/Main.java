package fun;

import fun.ast.ASTNode;
import fun.eval.EvaluationException;
import fun.parser.Parser;
import fun.values.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {

        String filename;
        if (args.length == 0) {
            filename = "program.txt";
        }else {
            filename = args[0];
        }

        try {
            String content = Files.readString(Paths.get(filename));

            Parser p = new Parser(content);

            ASTNode expr = p.parseExpr();

            System.out.println(expr);
            System.out.println();

            try {
                Value result = expr.evaluate();
                result.fullyForce();

                System.out.println(result);

            } catch (EvaluationException e) {
                System.out.println(e);
            }

        } catch (IOException e) {
            System.out.println(e);
        }

    }
}