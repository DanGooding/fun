public class Main {

    public static void main(String[] args) {

        ASTNode ast = new ASTLiteral(1);

        System.out.println(ast);

        ConstantValue result = (ConstantValue) ast.evaluate();

        System.out.println(result.getValue());
    }
}
