package fun.lexer;

public class Token {

    // TODO: should these be private with getters?
    private final TokenType type;
    private final String string;
    // position of the first char of this token
    private final FilePosition position;

    Token(TokenType type, String string, FilePosition position) {
        this.type = type;
        this.string = string;
        this.position = position;
    }

    Token(TokenType type, FilePosition position) {
        this(type, null, position);
    }

    public TokenType getType() {
        return type;
    }

    public String getString() {
        return string;
    }

    public FilePosition getPosition() {
        return position;
    }

    @Override
    public String toString() {
        if (string == null) {
            return String.format("%s", type);
        }
        return String.format("%s \"%s\"", type, string);
    }
}
