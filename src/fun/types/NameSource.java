package fun.types;

public class NameSource {

    private int nameIndex;

    NameSource() {
        nameIndex = 0;
    }

    public String freshName() {
        String name = String.format("%c%d", 'a' + nameIndex % 26, nameIndex / 26);
        nameIndex++;
        return name;
    }

}
