import models.Token;
import models.TokenType;
import org.junit.Before;
import org.junit.Test;
import utils.BufferManager;
import utils.TableParserBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LexerTest {
    List<Token> testDataList = new ArrayList<>();

    @Test
    public void testLexer() {
        File file = new File("res//assets//test.txt");
        TableParserBuilder.getInstance().buildParser();
        BufferManager.getInstance().initialize(file);

        Lexer lexer = new Lexer();

        lexer.generateNextToken();

        for(int tokenIndex = 0; tokenIndex< lexer.getGeneratedTokenList().size(); tokenIndex++){
            Token token = lexer.getGeneratedTokenList().get(tokenIndex);
            Token comareToken = testDataList.get(tokenIndex);
            assertEquals(token.getTokenType(),comareToken.getTokenType());
            assertEquals(token.getTokenValue(),comareToken.getTokenValue());
        }


    }
    @Before
    public void buildTestData(){
        testDataList.add(new Token(TokenType.RESERVED, "program",  1  ,   1));
        testDataList.add(new Token(TokenType.OPERATOR, ":",  1  ,   8));
        testDataList.add(new Token(TokenType.PUNCTUATION, "{",  1  ,   9));
        testDataList.add(new Token(TokenType.RESERVED, "int",  2  ,   5));
        testDataList.add(new Token(TokenType.ID, "abc",  2  ,   9));
        testDataList.add(new Token(TokenType.OPERATOR, "=",  2  ,   13));
        testDataList.add(new Token(TokenType.INTEGER, "123",  2  ,   15));
        testDataList.add(new Token(TokenType.OPERATOR, ";",  2  ,   18));
        testDataList.add(new Token(TokenType.ID, "intabc",  3  ,   5));
        testDataList.add(new Token(TokenType.OPERATOR, "=",  3  ,   12));
        testDataList.add(new Token(TokenType.INTEGER, "123",  3  ,   14));
        testDataList.add(new Token(TokenType.OPERATOR, ";",  3  ,   17));
        testDataList.add(new Token(TokenType.RESERVED, "float",  4  ,   5));
        testDataList.add(new Token(TokenType.ID, "abc",  4  ,   11));
        testDataList.add(new Token(TokenType.OPERATOR, "=",  4  ,   14));
        testDataList.add(new Token(TokenType.FLOAT, "123.2",  4  ,   15));
        testDataList.add(new Token(TokenType.OPERATOR, ";",  4  ,   20));
        testDataList.add(new Token(TokenType.RESERVED, "float",  5  ,   5));
        testDataList.add(new Token(TokenType.ID, "abc",  5  ,   11));
        testDataList.add(new Token(TokenType.OPERATOR, "=",  5  ,   14));
        testDataList.add(new Token(TokenType.INVALID_NUMBER, "123.20",  5  ,   15));
        testDataList.add(new Token(TokenType.OPERATOR, ";",  5  ,   21));
        testDataList.add(new Token(TokenType.RESERVED, "float",  6  ,   5));
        testDataList.add(new Token(TokenType.ID, "abc",  6  ,   11));
        testDataList.add(new Token(TokenType.OPERATOR, "=",  6  ,   14));
        testDataList.add(new Token(TokenType.FLOAT, "123.2E23",  6  ,   15));
        testDataList.add(new Token(TokenType.OPERATOR, ";",  6  ,   23));
        testDataList.add(new Token(TokenType.RESERVED, "float",  7  ,   5));
        testDataList.add(new Token(TokenType.PUNCTUATION, "[",  7  ,   10));
        testDataList.add(new Token(TokenType.PUNCTUATION, "]",  7  ,   11));
        testDataList.add(new Token(TokenType.ID, "abc",  7  ,   13));
        testDataList.add(new Token(TokenType.OPERATOR, "=",  7  ,   16));
        testDataList.add(new Token(TokenType.PUNCTUATION, "{",  7  ,   17));
        testDataList.add(new Token(TokenType.FLOAT, "1.2E1",  7  ,   18));
        testDataList.add(new Token(TokenType.OPERATOR, ",",  7  ,   23));
        testDataList.add(new Token(TokenType.FLOAT, "0.2E-1",  7  ,   24));
        testDataList.add(new Token(TokenType.OPERATOR, ",",  7  ,   30));
        testDataList.add(new Token(TokenType.FLOAT, "12.2E-2",  7  ,   31));
        testDataList.add(new Token(TokenType.PUNCTUATION, "}",  7  ,   38));
        testDataList.add(new Token(TokenType.OPERATOR, ";",  7  ,   39));
        testDataList.add(new Token(TokenType.PUNCTUATION, "}",  9  ,   1));
    }
}