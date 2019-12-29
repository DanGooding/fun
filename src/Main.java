import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {

        try {
            String content = Files.readString(Paths.get("program.txt"));

            TokenStream ts = new TokenStream(content);

            Token t;
            do {
                t = ts.nextToken();
                System.out.println(t);

            } while (t.type != TokenType.EOF);

        }catch (IOException e) {
            System.out.println(e);
        }

    }
}
