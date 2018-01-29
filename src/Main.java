import models.BufferReadResponse;
import models.Token;
import models.TokenType;
import utils.BufferManager;
import utils.TableParserBuilder;

import java.io.File;

public class Main {

    public static void main(String[] args) {

        initializeParser();
        BufferManager.getInstance().initialize(new File("res/input/program1.txt"));
        Laxer laxer = new Laxer();

        laxer.getNextToken();

        for(Token token: laxer.getGeneratedTokenList()){
            System.out.println(token.getTokenType().getTokenType()+" "+token.getTokenValue()+" L:"+token.getLineNumber()+" C:"+token.getColumnNumber());
        }
    }

    public static void initializeParser(){
        TableParserBuilder.getInstance().buildParser();
    }
}



