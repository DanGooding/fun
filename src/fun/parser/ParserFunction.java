package fun.parser;

@FunctionalInterface
interface ParserFunction<T> {
    T parse() throws ParseErrorException;
}
