package fun;

import fun.ast.ASTNode;
import fun.eval.EvaluationException;
import fun.parser.ParseErrorException;
import fun.parser.Parser;
import fun.types.*;
import fun.values.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

import picocli.CommandLine;

@CommandLine.Command(name = "execute", mixinStandardHelpOptions = true, description = "executes a program")
public class Main implements Callable<Integer> {

    @CommandLine.Parameters(index = "0", description = "the .fun source file to execute.")
    private File file;

    @Override
    public Integer call() throws Exception {
        try {
            String content = Files.readString(file.toPath());

            ASTNode expr = Parser.parseWholeExpr(content);

            Scheme type =
                    Inferer
                            .inferType(expr)
                            .refreshVariableNames(new VariableNameRefresher());

            System.out.println(type.prettyPrint());

            Value result = expr.evaluate();
            result.fullyForce();

            System.out.println(result);
            return 0;

        } catch (IOException e) {
            System.err.printf("unable to read file '%s'%n", file.toString());
            return 1;
        } catch (ParseErrorException e) {
            System.err.println(e.getMessage());
            return 1;
        } catch (TypeErrorException | EvaluationException e) {
            System.err.println(e);
            return 1;
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}
