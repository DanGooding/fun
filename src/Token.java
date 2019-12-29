public class Token {

    final TokenType type;
    final String string;

    public Token(TokenType type, String string) {
        this.type = type;
        this.string = string;
    }

    public Token(TokenType type) {
        this(type, null);
    }

    @Override
    public String toString() {
        if (string == null) {
            return type.toString();
        }
        return String.format("%s \"%s\"", type, string);
    }
}
