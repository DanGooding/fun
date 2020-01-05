package fun.parser;

class OpInfo {
    final int precedence;
    final Assoc associativity;

    OpInfo(int precedence, Assoc associativity) {
        this.precedence = precedence;
        this.associativity = associativity;
    }

    @Override
    public String toString() {
        return String.format("precedence %d, associativity %s", precedence, associativity);
    }
}
