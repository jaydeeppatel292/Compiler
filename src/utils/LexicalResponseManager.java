package utils;

import global.Constants;
import models.Token;
import models.TokenType;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class LexicalResponseManager {
    private static LexicalResponseManager ourInstance = new LexicalResponseManager();
    private PrintWriter errorWriterFile;
    private PrintWriter aTOccWriterFile;
    private PrintWriter tokenWriterFile;

    public static LexicalResponseManager getInstance() {
        return ourInstance;
    }



    private LexicalResponseManager() {
        try {
            tokenWriterFile = new PrintWriter(Constants.OUTPUT_TOKEN_GENERATED_FILE_PATH, "UTF-8");
            aTOccWriterFile = new PrintWriter(Constants.OUTPUT_ATOCC_FILE_NAME_PATH, "UTF-8");
            errorWriterFile = new PrintWriter(Constants.OUTPUT_ERROR_FILE_NAME_PATH, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void initialize(){

    }

    public void finisheWriting(){
        tokenWriterFile.close();
        aTOccWriterFile.close();
        errorWriterFile.close();
    }
    public void writeLexicalResponseToFile(List<Token> tokenList){
        for(int tokenIndex=0;tokenIndex<tokenList.size();tokenIndex++){
            Token token = tokenList.get(tokenIndex);

            if(token.getTokenType()== TokenType.INVALID_IDENTIFIER || token.getTokenType()== TokenType.INVALID_NUMBER ){
                errorWriterFile.print(token.getTokenType().getTokenType());
                errorWriterFile.print(",");
                errorWriterFile.print(token.getTokenValue());
                errorWriterFile.print(",");
                errorWriterFile.print(token.getLineNumber());
                errorWriterFile.print(":");
                errorWriterFile.print(token.getColumnNumber());
                errorWriterFile.println();

                continue;
            }

            // write atocc format to atocc file
            if(tokenIndex!=0){
                aTOccWriterFile.print("+");
            }
            aTOccWriterFile.print(token.getTokenType().getTokenType());

            // write token to the token file
            tokenWriterFile.print(token.getTokenType().getTokenType());
            tokenWriterFile.print(",");
            tokenWriterFile.print(token.getTokenValue());
            tokenWriterFile.print(",");
            tokenWriterFile.print(token.getLineNumber());
            tokenWriterFile.print(":");
            tokenWriterFile.print(token.getColumnNumber());
            tokenWriterFile.println();

        }
        finisheWriting();
    }
}
