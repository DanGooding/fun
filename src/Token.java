public class Token {

    // TODO: should these be private with getters?
    final TokenType type;
    final String string;
    // position of the first char of this token
    final FilePosition position;

    public Token(TokenType type, String string, FilePosition position) {
        this.type = type;
        this.string = string;
        this.position = position;
    }

    public Token(TokenType type, FilePosition position) {
        this(type, null, position);
    }

    @Override
    public String toString() {
        if (string == null) {
            return String.format("%s %s", type, position);
        }
        return String.format("%s \"%s\" %s", type, string, position);
    }
}
