package models;

public class Token {
    private TokenType tokenType;
    private String tokenValue;
    private int lineNumber;
    private int columnNumber;

    public Token(TokenType tokenType, String tokenValue, int lineNumber, int columnNumber) {
        this.tokenType = tokenType;
        this.tokenValue = tokenValue;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
        updateColumnNumber();
        checkForReservedToken();
    }

    private void updateColumnNumber() {
        this.columnNumber -= tokenValue.length();
    }

    private void checkForReservedToken() {
        // Check if tokenvalue consist of punctuation like and,or,not
        if(this.tokenValue.equals(ReservedToken.AND.getReservedTokenType()) || this.tokenValue.equals(ReservedToken.NOT.getReservedTokenType()) || this.tokenValue.equals(ReservedToken.OR.getReservedTokenType())){
            this.tokenType = TokenType.OPERATOR;
            return;
        }

        // Check if tokenvalue consist of reserved word ...
        for(ReservedToken reservedToken : ReservedToken.values()){
            if(reservedToken.getReservedTokenType().equals(this.tokenValue)){
                this.tokenType = TokenType.RESERVED;
                return;
            }
        }
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }
}
