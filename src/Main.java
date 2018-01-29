import global.Constants;
import models.BufferReadResponse;
import models.Token;
import models.TokenType;
import utils.BufferManager;
import utils.LexicalResponseManager;
import utils.TableParserBuilder;

import java.io.File;

public class Main {

    public static void main(String[] args) {

        initializeParser();

        Laxer laxer = new Laxer();

        laxer.getNextToken();

        for(Token token: laxer.getGeneratedTokenList()){
            System.out.println(token.getTokenType().getTokenType()+" "+token.getTokenValue()+" L:"+token.getLineNumber()+" C:"+token.getColumnNumber());
        }

        LexicalResponseManager.getInstance().writeLexicalResponseToFile(laxer.getGeneratedTokenList());
    }

    public static void initializeParser(){
        TableParserBuilder.getInstance().buildParser();
        BufferManager.getInstance().initialize(new File(Constants.INPUT_FILE_PATH));
    }
}



