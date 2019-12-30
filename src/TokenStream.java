import java.util.Map;
import java.util.function.Function;

public class TokenStream {

    private static final Map<String, TokenType> keywords =
        Map.of(
            "let", TokenType.LET,
            "in", TokenType.IN,

            "if", TokenType.IF,
            "then", TokenType.THEN,
            "else", TokenType.ELSE,

            "lambda", TokenType.LAMBDA
        );

    private static final String operatorChars = "+-*=>";


    private CharStream inputStream;

    public TokenStream(String input) {
        inputStream = new CharStream(input);
    }

    /**
     * get the next token, advancing. returns EOF when reaches end of input
     */
    Token nextToken() {
        if (inputStream.atEOF()) {
            return new Token(TokenType.EOF);
        }

        readWhile(this::isWhitespace);

        char c = inputStream.peekChar();
        if (isDigitChar(c)) {
            return readInteger();

        }else if (isNameStartChar(c)) {
            return readName();

        }else if (isOperatorChar(c)) {
            return readOperator();

        }else if (c == '(') {
            inputStream.nextChar(); // advance
            return new Token(TokenType.OPEN_BRACKET);

        }else if (c == ')') {
            inputStream.nextChar();
            return new Token(TokenType.CLOSE_BRACKET);

        }else if (c == ',') {
            inputStream.nextChar();
            return new Token(TokenType.COMMA);

        }else if (c == '_') { // TODO: combine this with readName ?
            inputStream.nextChar();
            return new Token(TokenType.UNDERSCORE);

        }else if (c == '\n') { // TODO: allow windows CRLF \r\n (just discard \r ?)
            inputStream.nextChar();
            return new Token(TokenType.NEWLINE);

        } else {
            // TODO: proper exception class here
            throw new RuntimeException("unknown token");
        }
        // TODO: allow comments

    }


    boolean isWhitespace(char c) {
        return c == ' ';
    }


    private boolean isDigitChar(char c) {
        return Character.isDigit(c);
    }

    private Token readInteger() {
        return new Token(TokenType.INTEGER, readWhile(this::isDigitChar));
    }


    private boolean isNameStartChar(char c) {
        return Character.isAlphabetic(c);
    }

    private boolean isNameChar(char c) {
        // TODO: use a regex ?
        return Character.isAlphabetic(c) || Character.isDigit(c) || c == '\'' || c == '_';
    }

    private Token readName() {
        // just use readWhile ?
        String name = readAtLeastOneWhile(this::isNameChar);

        if (keywords.containsKey(name)) {
            return new Token(keywords.get(name));
        }

        // hacked in boolean literals - no need
//        if (name.equals("True")) {
//            return new Token(TokenType.TRUE, name);
//        }else if (name.equals("False")) {
//            return new Token(TokenType.FALSE, name);
//        }

        return new Token(TokenType.NAME, name);
    }


    private boolean isOperatorChar(char c) {
        return operatorChars.indexOf(c) >= 0;
    }

    private Token readOperator() {
        String s = readWhile(this::isOperatorChar);
        if (s.equals("->")) {
            return new Token(TokenType.ARROW);
        }else if (s.equals("=")) {
            return new Token(TokenType.ASSIGN_EQUALS);
        }
        // allow user defined operators
        // check this is one once their declarations
        // have been parsed
        return new Token(TokenType.OPERATOR, s);
    }


    private String readWhile(Function<Character, Boolean> predicate) {
        StringBuilder stringBuilder = new StringBuilder();
        while (!inputStream.atEOF() && predicate.apply(inputStream.peekChar())) {
            stringBuilder.append(inputStream.nextChar());
        }
        return stringBuilder.toString();
    }

    private String readAtLeastOneWhile(Function<Character, Boolean> predicate) {
        char first = inputStream.nextChar();
        String others = readWhile(predicate);
//        return String.format("%c%s", first, others);
        return first + others;
    }

}
