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

        checkForReservedToken();
    }

    private void checkForReservedToken() {
        // Check if tokenvalue consist of reserved word ...
        for(ReservedToken reservedToken : ReservedToken.values()){
            if(reservedToken.getReservedTokenType().equals(this.tokenValue)){
                this.tokenType = TokenType.RESERVED;
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
