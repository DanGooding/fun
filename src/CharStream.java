public class CharStream {

    private final String input;

    private int position = 0;
    // TODO: track row & column for error messages

    CharStream(String input) {
        this.input = input;
    }

    /**
     * return next char, advancing
     */
    char nextChar() {
        return input.charAt(position++);
    }

    /**
     * return next char
     */
    char peekChar() {
        return input.charAt(position);
    }

    /**
     * has the end of the stream been reached
     */
    boolean atEOF() {
        return position == input.length();
    }

}
