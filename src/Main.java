import global.Constants;
import models.Token;
import utils.BufferManager;
import utils.LexicalResponseManager;
import utils.TableParserBuilder;

import java.io.File;

public class Main {

    public static void main(String[] args) {

        initializeParser(args);

        Lexer lexer = new Lexer();

//        lexer.generateNextToken();

        /*for (Token token : lexer.getGeneratedTokenList()) {
            System.out.println("[" + token.getTokenType().getTokenType() + ": " + token.getTokenValue()+"]");
        }*/
        Token token;
        while (true){
            token =TokenGenerator.getInstance().getNextToken();
            if(token==null)
                break;
            System.out.println("[" + token.getTokenType().getTokenType() + ": " + token.getTokenValue()+"]");
        }

        LexicalResponseManager.getInstance().writeLexicalResponseToFile(lexer.getGeneratedTokenList());
    }

    public static void initializeParser(String[] args) {

        createRequiredFolders();

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

    private static void createRequiredFolders() {
        File resDir = new File("res");
        if(!resDir.isFile()){
            resDir.mkdir();
        }

        File inputDir = new File("res/input");
        if(!inputDir.isFile()){
            inputDir.mkdir();
        }

        File outputDir = new File("res/output");
        if(!outputDir.isFile()){
            outputDir.mkdir();
        }
        File assetsDir = new File("res/assets");
        if(!assetsDir.isFile()){
            assetsDir.mkdir();
        }
    }
}



