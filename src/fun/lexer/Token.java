package fun.lexer;

public class Token {

    // TODO: should these be private with getters?
    public final TokenType type;
    public final String string;
    // position of the first char of this token
    public final FilePosition position;

    Token(TokenType type, String string, FilePosition position) {
        this.type = type;
        this.string = string;
        this.position = position;
    }

    Token(TokenType type, FilePosition position) {
        this(type, null, position);
    }

    @Override
    public String toString() {
        if (string == null) {
            return String.format("%s", type);
        }
        return String.format("%s \"%s\"", type, string);
    }
}
