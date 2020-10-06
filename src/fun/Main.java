package fun;

import fun.ast.ASTNode;
import fun.eval.EvaluationException;
import fun.parser.ParseErrorException;
import fun.parser.Parser;
import fun.types.*;
import fun.values.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {

        if (args.length == 0) {
            System.err.println("no argument, expected filename");
            return;
        }
        String filename = args[0];

        try {
            String content = Files.readString(Paths.get(filename));

            ASTNode expr = Parser.parseWholeExpr(content);

            Scheme type =
                Inferer
                    .inferType(expr)
                    .refreshVariableNames(new VariableNameRefresher());

            System.out.println(type.prettyPrint());

            Value result = expr.evaluate();
            result.fullyForce();

            System.out.println(result);

        } catch (IOException e) {
            System.err.printf("unable to read file '%s'%n", filename);
        } catch (ParseErrorException e) {
            System.err.println(e.getMessage());
        } catch (TypeErrorException | EvaluationException e) {
            System.err.println(e);
        }

    }
}
