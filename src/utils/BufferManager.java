package utils;

import models.BufferReadResponse;

import java.io.File;
import java.util.Scanner;

public class BufferManager {
    private static BufferManager ourInstance = new BufferManager();
    private static Scanner scanner;
    private byte[] buffer1;
    private byte[] buffer2;
    private int lexemeForward = 0;
    private int lexemeEnd = -1;
    private int bufferIndex = 1;
    private boolean isEOF = false;
    private int currentLineNumber = 1;

    public static BufferManager getInstance() {
        if (scanner == null) {
            scanner = new Scanner(System.in);
        }
        return ourInstance;
    }

    private BufferManager() {
    }

    public boolean isEOF() {
        return isEOF;
    }

    public static BufferManager getOurInstance() {
        return ourInstance;
    }

    public static Scanner getScanner() {
        return scanner;
    }

    public int getLexemeForward() {
        return lexemeForward;
    }

    public int getCurrentLineNumber() {
        return currentLineNumber;
    }

    public void initialize(File file) {
        FileManager.getInstance().setupFile(file);
        BufferReadResponse bufferReadResponse = FileManager.getInstance().readNextBuffer();
        if (bufferReadResponse.getNoOfBytesRead() > 0) {
            buffer1 = bufferReadResponse.getBuffer();
        }
        bufferReadResponse = FileManager.getInstance().readNextBuffer();
        if (bufferReadResponse.getNoOfBytesRead() > 0) {
            buffer2 = bufferReadResponse.getBuffer();
        }

    }

    public char getNextChar() {
        return scanner.next().charAt(0);
    }

    public char getNextCharFromBuffer() {
        char charfromBuffer = 0;
        switch (bufferIndex) {
            case 1:
                if (buffer1 != null) {
                    charfromBuffer = (char) buffer1[lexemeForward++];
                    if (lexemeForward >= buffer1.length) {
                        lexemeForward = 0;
                        bufferIndex = 2;
                        buffer1 = null;
                        BufferReadResponse bufferReadResponse = FileManager.getInstance().readNextBuffer();
                        if (bufferReadResponse.getNoOfBytesRead() > 0) {
                            buffer1 = bufferReadResponse.getBuffer();
                        }
                    }
                } else {
                    isEOF = true;
                }

                break;
            case 2:
                if (buffer2 != null) {
                    charfromBuffer = (char) buffer2[lexemeForward++];
                    if (lexemeForward >= buffer2.length) {
                        lexemeForward = 0;
                        bufferIndex = 1;
                        buffer2 = null;
                        BufferReadResponse bufferReadResponse = FileManager.getInstance().readNextBuffer();
                        if (bufferReadResponse.getNoOfBytesRead() > 0) {
                            buffer2 = bufferReadResponse.getBuffer();
                        }
                    }
                } else {
                    isEOF = true;
                }

                break;
        }
        if(charfromBuffer=='\n'){
            currentLineNumber++;
        }
        return charfromBuffer;
    }
}
