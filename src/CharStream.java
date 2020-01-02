public class CharStream {

    private final String input;

    private int index;
    private FilePosition position;

    CharStream(String input) {
        this.input = input;
        index = 0;
        position = FilePosition.startPosition();
    }

    /**
     * move on to the next char
     */
    void advance() {
        // TODO: move column & row tracking into TokenStream ?
        if (input.charAt(index) == '\n') {
            position = position.nextLine();
        }else {
            position = position.nextColumn();
        }
        index++;
    }

    /**
     * return current char, then advance
     */
    char popChar() {
        char c = input.charAt(index);
        advance();
        return c;
    }

    /**
     * return current char
     */
    char peekChar() {
        return input.charAt(index);
    }

    /**
     * has the end of the stream been reached ?
     */
    boolean atEOF() {
        return index == input.length();
    }

    public FilePosition getPosition() {
        return position;
    }
}
