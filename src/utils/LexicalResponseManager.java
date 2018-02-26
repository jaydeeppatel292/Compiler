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
    private PrintWriter derivationWriterFile;

    public static LexicalResponseManager getInstance() {
        return ourInstance;
    }


    private LexicalResponseManager() {
        try {
            tokenWriterFile = new PrintWriter(Constants.OUTPUT_TOKEN_GENERATED_FILE_PATH, "UTF-8");
            aTOccWriterFile = new PrintWriter(Constants.OUTPUT_ATOCC_FILE_NAME_PATH, "UTF-8");
            errorWriterFile = new PrintWriter(Constants.OUTPUT_ERROR_FILE_NAME_PATH, "UTF-8");
            derivationWriterFile = new PrintWriter(Constants.OUTPUT_DERIVATION, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {

    }

    public void finisheWriting() {
        tokenWriterFile.close();
        aTOccWriterFile.close();
        errorWriterFile.close();
        derivationWriterFile.close();
    }

    public void addDerivationToFIle(List<String> dataList){
        for(String data : dataList){
            derivationWriterFile.print(data+" ");
        }
        derivationWriterFile.println();
    }

    public void writeLexicalResponseToFile(Token token) {
        errorWriterFile.println("Format: ErrorType , TokenValue , TokenLineNumber : TokenColumnNumber");
        tokenWriterFile.println("Format: TokenType , TokenValue , TokenLineNumber : TokenColumnNumber");

        if (token.getTokenType() == TokenType.INVALID_IDENTIFIER || token.getTokenType() == TokenType.INVALID_NUMBER) {
            errorWriterFile.print(token.getTokenType().getTokenType());
            errorWriterFile.print(",");
            errorWriterFile.print(token.getTokenValue());
            errorWriterFile.print(",");
            errorWriterFile.print(token.getLineNumber());
            errorWriterFile.print(":");
            errorWriterFile.print(token.getColumnNumber());
            errorWriterFile.println();

            return;
        }

        // write atocc format to atocc file
        if (!token.getTokenValue().equals("/*") && !token.getTokenValue().equals("*/") && !token.getTokenValue().equals("//")) {
            if (token.getTokenType().equals(TokenType.ID) || token.getTokenType().equals(TokenType.INTEGER) || token.getTokenType().equals(TokenType.FLOAT)) {
                aTOccWriterFile.print(token.getTokenType().getTokenType());
            } else {
                aTOccWriterFile.print(token.getTokenValue());
            }
            aTOccWriterFile.print(" ");
        }

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
}
