package fun.parser;

enum Assoc {
    LEFT,  // a - b - c  is  (a - b) - c
    RIGHT,  // a ^ b ^ c  is  a ^ (b ^ c)
    NONE  // a == b == c  is an error  TODO: how to detect?
}
