package models;

public enum ReservedToken {
    AND ("and","and"),
    NOT ("not","not"),
    OR ("or","or"),
    ID("if","if"),
    THEN("then","then"),
    ELSE("else","else"),
    FOR("for","for"),
    CLASS("class","class"),
    INT("int","int"),
    FLOAT("float","float"),
    GET("get","get"),
    PUT("put","put"),
    RETURN("return","return"),
    PROGRAM("program","program"),
    EQ ("==","eq"),
    NEQ ("<>","neq"),
    LT ("<","lt"),
    GT (">","gt"),
    LEQ ("<=","leq"),
    GEQ (">=","geq"),
    SR ("::","sr");

    private String reservedTokenType;
    private String tokenType;

    ReservedToken(String tokenType, String reservedTokenType) {
        this.reservedTokenType = reservedTokenType;
        this.tokenType = tokenType;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getReservedTokenType() {
        return reservedTokenType;
    }
}