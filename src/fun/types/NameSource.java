package fun.types;

public class NameSource {

    private int nameIndex;

    public NameSource() {
        nameIndex = 0;
    }

    public String freshName() {
        StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append((char)('a' + nameIndex % 26));
        if (nameIndex >= 26) {
            nameBuilder.append(nameIndex / 26);
        }
        nameIndex++;
        return nameBuilder.toString();
    }

}
