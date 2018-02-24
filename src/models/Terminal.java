package models;

public enum Terminal {
    PROGRAM(1,"program"),
    SEMICOLON(2,";"),
    CLASS(3,"class"),
    id(4,"id"),
    OPEN_CURELY_BRAC(5,"{"),
    CLOSE_CURELY_BRAC(6,"}"),
    COLON(7,":"),
    COMMA(8,","),
    OPEN_BRAC(9,"("),
    CLOSE_BRAC(10,")"),
    INT(11,"int"),
    FLOAT(12,"float"),
    OPEN_SQUAR_BRAC(13,"["),
    INTEGER(14,"integer"),
    CLOSE_SQUAR_BRAC(15,"]"),
    SR(16,"sr"),
    IF(17,"if"),
    THEN(18,"then"),
    ELSE(19,"else"),
    GET(20,"get"),
    PUT(21,"put"),
    RETURN(22,"return"),
    FOR(23,"for"),
    NOT(24,"not"),
    DOT(25,"."),
    ADD(26,"+"),
    MINUS(27,"-"),
    OR(28,"or"),
    EQ(29,"eq"),
    NEQ(30,"neq"),
    LT(31,"lt"),
    GT(32,"gt"),
    LEQ(33,"leq"),
    GEQ(34,"geq"),
    MUL(35,"*"),
    DIV(36,"/"),
    AND(37,"and"),
    EQUAL(38,"="),
    DOLLAR(39,"$"),
    EPSILON(40,"EPSILON");
    private final int index;
    private final String data;

    public int getIndex() {
        return index;
    }

    public String getData() {
        return data;
    }

    Terminal(int index, String s) {
        this.index = index;
        this.data = s;
    }

    public static int getTerminalIndexFromString(String input){
        for(Terminal terminal : Terminal.values()){
            if(terminal.getData().equals(input)){
                return terminal.getIndex();
            }
        }
        return -1;
    }
}
