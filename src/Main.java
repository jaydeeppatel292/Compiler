import global.Constants;
import models.Token;
import utils.BufferManager;
import utils.LexicalResponseManager;
import utils.TableParserBuilder;

import java.io.File;

public class Main {

    public static void main(String[] args) {

        initializeParser(args);

        Laxer laxer = new Laxer();

        laxer.generateNextToken();

        for (Token token : laxer.getGeneratedTokenList()) {
//            System.out.println(token.getTokenType()+", \""+token.getTokenValue()+"\",  "+token.getLineNumber()+"  ,   "+token.getColumnNumber());
            System.out.println("[" + token.getTokenType().getTokenType() + ": " + token.getTokenValue()+"]");
//            System.out.println(token.getTokenValue());
        }

        LexicalResponseManager.getInstance().writeLexicalResponseToFile(laxer.getGeneratedTokenList());
    }

    public static void initializeParser(String[] args) {

        // get filepath from user input args
        File inputFile = null;
        if (args.length > 0) {
            inputFile = new File(args[0]);
        }
        // check if file is valid or not
        // if file is not valid take file from res/input/program.txt
        if (inputFile == null || !inputFile.isFile()) {
            inputFile = new File(Constants.INPUT_FILE_PATH);
        }
        TableParserBuilder.getInstance().buildParser();
        BufferManager.getInstance().initialize(inputFile);
    }
}



