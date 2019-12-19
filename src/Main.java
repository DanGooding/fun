public class Main {

    public static void main(String[] args) {

        ASTNode ast = new ASTLiteralInt(1);

        System.out.println(ast);

        IntValue result = (IntValue) ast.evaluate();

        System.out.println(result.getValue());
    }
}
