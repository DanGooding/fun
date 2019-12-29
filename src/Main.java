import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {

        try {
            String content = Files.readString(Paths.get("program.txt"));

            Parser p = new Parser(content);

            ASTNode expr = p.parseExpr();

            System.out.println(expr);

            try {
                Value result = expr.evaluate();
                System.out.println(result);

            } catch (EvaluationException e) {
                System.out.println(e);
            }

        } catch (IOException e) {
            System.out.println(e);
        }

    }
}
