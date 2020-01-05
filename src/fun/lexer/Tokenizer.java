package fun.lexer;

import java.util.Map;
import java.util.function.Function;

public class Tokenizer implements TokenStream {

    private static final Map<String, TokenType> keywords =
        Map.of(
            "let", TokenType.LET,
            "in", TokenType.IN,

            "if", TokenType.IF,
            "then", TokenType.THEN,
            "else", TokenType.ELSE,

            "lambda", TokenType.LAMBDA,

            "case", TokenType.CASE,
            "of", TokenType.OF,

            "True", TokenType.TRUE,
            "False", TokenType.FALSE
        );

    private static final String operatorChars = "+-*^=>";


    private final CharStream inputStream;

    public Tokenizer(String input) {
        inputStream = new CharStream(input);
    }

    /**
     * get the next token, advancing. returns EOF when reaches end of input
     */
    @Override
    public Token nextToken() {
        if (inputStream.atEOF()) {
            return new Token(TokenType.EOF, inputStream.getPosition());
        }

        readWhile(this::isWhitespace);

        char c = inputStream.peekChar();

        FilePosition position = inputStream.getPosition();

        if (isDigitChar(c)) {
            return readInteger();

        } else if (isNameStartChar(c)) {
            return readName();

        } else if (isOperatorChar(c)) {
            return readOperator();

        } else if (isNewlineStartChar(c)) {
            return readNewline();

        } else if (c == '(') {
            inputStream.advance();
            return new Token(TokenType.OPEN_BRACKET, position);

        } else if (c == ')') {
            inputStream.advance();
            return new Token(TokenType.CLOSE_BRACKET, position);

        } else if (c == ',') {
            inputStream.advance();
            return new Token(TokenType.COMMA, position);

        } else if (c == '_') { // TODO: combine this with readName ?
            inputStream.advance();
            return new Token(TokenType.UNDERSCORE, position);
        }
        // TODO: proper exception class here
        throw new RuntimeException(String.format("unknown token %s", position));

        // TODO: allow comments
    }


    private boolean isWhitespace(char c) {
        return c == ' ';
    }


    private boolean isDigitChar(char c) {
        return Character.isDigit(c);
    }

    private Token readInteger() {
        FilePosition position = inputStream.getPosition();
        return new Token(TokenType.INTEGER, readWhile(this::isDigitChar), position);
    }


    private boolean isNameStartChar(char c) {
        return Character.isAlphabetic(c);
    }

    private boolean isNameChar(char c) {
        // TODO: use a regex ?
        return Character.isAlphabetic(c) || Character.isDigit(c) || c == '\'' || c == '_';
    }

    private Token readName() {
        FilePosition position = inputStream.getPosition();
        String name = readWhile(this::isNameChar);

        if (keywords.containsKey(name)) {
            return new Token(keywords.get(name), position);
        }

        return new Token(TokenType.NAME, name, position);
    }


    private boolean isOperatorChar(char c) {
        return operatorChars.indexOf(c) >= 0;
    }

    private Token readOperator() {
        FilePosition position = inputStream.getPosition();

        String s = readWhile(this::isOperatorChar);
        if (s.equals("->")) {
            return new Token(TokenType.ARROW, position);
        } else if (s.equals("=")) {
            return new Token(TokenType.ASSIGN_EQUALS, position);
        }
        // allow user defined operators
        // check this is one once their declarations
        // have been parsed
        return new Token(TokenType.OPERATOR, s, position);
    }

    private boolean isNewlineStartChar(char c) {
        return c == '\n' || c == '\r';
    }

    private Token readNewline() {
        char c = inputStream.peekChar();

        FilePosition position = inputStream.getPosition();

        if (c == '\n') {
            inputStream.advance();
            return new Token(TokenType.NEWLINE, position);
        }else if (c == '\r') {
            inputStream.advance();
            if (inputStream.peekChar() == '\n') {
                inputStream.advance();
                return new Token(TokenType.NEWLINE, position);
            }
        }
        throw new RuntimeException(String.format("bad newline %s", position));
    }

    private String readWhile(Function<Character, Boolean> predicate) {
        StringBuilder stringBuilder = new StringBuilder();
        while (!inputStream.atEOF() && predicate.apply(inputStream.peekChar())) {
            stringBuilder.append(inputStream.popChar());
        }
        return stringBuilder.toString();
    }

}
