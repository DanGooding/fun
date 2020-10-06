package fun.lexer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * removes NEWLINE tokens, and inserts BLOCK_BEGIN, BLOCK_DELIM and BLOCK_END
 * to enclose blocks
 */
public class BlockMaker implements TokenStream {

    private static final List<TokenType> blockStartTokens =
        List.of(TokenType.OF);  // TODO: also LET if using haskell let blocks

    private final Tokenizer tokenStream;
    private final Deque<Token> nextTokens; // a queue
    private final List<Integer> indentationLevels; // a stack

    public BlockMaker(Tokenizer tokenStream) {
        this.tokenStream = tokenStream;
        nextTokens = new ArrayDeque<>();
        indentationLevels = new ArrayList<>();
//        indentationLevels.add(0);  // TODO: for top level blocks
    }

    @Override
    public Token nextToken() {
        if (nextTokens.size() > 0) {
            return nextTokens.removeFirst();
        }

        Token token = tokenStream.nextToken();

        if (blockStartTokens.contains(token.getType())) { // beginning a block
            Token next;
            do { // skip over newlines
                next = tokenStream.nextToken();
            } while (next.getType() == TokenType.NEWLINE);

            nextTokens.addLast(new Token(TokenType.BLOCK_BEGIN, next.getPosition()));
            nextTokens.addLast(next);

            indentationLevels.add(next.getPosition().getColumn());

        } else if (token.getType() == TokenType.NEWLINE || token.getType() == TokenType.EOF) {
            // possibly continuing / ending block

            while (token.getType() == TokenType.NEWLINE) { // skip over newlines
                token = tokenStream.nextToken();
            }

            // tokens to insert into the stream, before `token`
            List<Token> tokensToInsert = new ArrayList<>();

            while (indentationLevels.size() > 0) {
                int currentIndentation = indentationLevels.get(indentationLevels.size() - 1);

                if (token.getPosition().getColumn() < currentIndentation || token.getType() == TokenType.EOF) {
                    // ending one block, maybe more

                    // position is position of first token not in the block
                    tokensToInsert.add(new Token(TokenType.BLOCK_END, token.getPosition()));

                    indentationLevels.remove(indentationLevels.size() - 1); // pop

                } else if (token.getPosition().getColumn() == currentIndentation) {
                    // next item in the block

                    tokensToInsert.add(new Token(TokenType.BLOCK_DELIM, token.getPosition()));
                    break;

                } else { // greater indentation, continuing a block item, no extra tokens needed
                    break;
                }
            }

            if (tokensToInsert.size() > 0) {
                // want to emit `tokensToInsert` in order and then `token`:
                for (int i = 1; i < tokensToInsert.size(); i++) {
                    nextTokens.addLast(tokensToInsert.get(i));
                }
                nextTokens.addLast(token);
                return tokensToInsert.get(0);

            }else {
                return token;
            }

        }

        return token;
    }

}
