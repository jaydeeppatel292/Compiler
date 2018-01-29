package models;

public enum ReservedToken {
    AND ("and"),
    NOT ("not"),
    OR ("or"),
    ID("if"),
    THEN("then"),
    ELSE("else"),
    FOR("for"),
    CLASS("class"),
    INT("int"),
    FLOAT("float"),
    GET("get"),
    PUT("put"),
    RETURN("return"),
    PROGRAM("program");
    private String reservedTokenType;
    ReservedToken(String reservedTokenType) {
        this.reservedTokenType = reservedTokenType;
    }

    public String getReservedTokenType() {
        return reservedTokenType;
    }
}