package fun.lexer;

/**
 * an immutable object representing the location of a character in a source file
 * row is line number, from START_ROW
 * column is char number on that line, from START_COLUMN
 */
public class FilePosition {

    static final int START_ROW = 1;
    static final int START_COLUMN = 1;

    // both zero indexed
    private final int row;
    private final int column;

    private FilePosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    static FilePosition startPosition() {
        return new FilePosition(START_ROW, START_COLUMN);
    }

    FilePosition nextLine() {
        return new FilePosition(row + 1, START_COLUMN);
    }

    FilePosition nextColumn() {
        return new FilePosition(row, column + 1);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return String.format("(ln %d, col %d)", row, column);
    }

    // TODO: use to give evaluation & parse errors too
}
