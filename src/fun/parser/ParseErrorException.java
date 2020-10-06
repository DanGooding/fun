package fun.parser;

import fun.lexer.FilePosition;

public class ParseErrorException extends Exception {

    ParseErrorException(String message) {
        super(message);
    }

    ParseErrorException(FilePosition position, String message) {
        this(String.format("parse error at %s, %s", position, message));
    }
}
