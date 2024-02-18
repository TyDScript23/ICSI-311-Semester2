public class Token {

    //NOTE: Use linenumber and charposition for error messages in the future!

    //create the enum storing object labels
    enum TokenType{
        WORD,NUMBER,ENDOFLINE,

        PRINT,READ,INPUT,DATA,GOSUB,FOR,TO,STEP,NEXT,RETURN,IF,THEN,FUNCTION,WHILE,END,

        STRINGLITERAL, LABEL,

        LESSTHANOREQUALTO,GREATERTHANOREQUALTO,NOTEQUALS,

        EQUALS, LESSTHAN, GREATERTHAN,LEFTPARENTHESIS,RIGHTPARENTHESIS, ADD, SUBTRACT, MULTIPLY, DIVIDE,
    }

    private String tokenvalue;
    private int linenumber;
    private int charposition;
    private TokenType tokentype;

    //initialize a token without a value
    public Token(TokenType ttype, int linenum, int charpos)
    {
        tokentype = ttype;
        linenumber = linenum;
        charposition = charpos;
        tokenvalue = "";
    }

    //initialize a token with a value
    public Token(TokenType ttype, int linenum, int charpos, String value)
    {
        tokentype = ttype;
        linenumber = linenum;
        charposition = charpos;
        tokenvalue = value;
    }

    //used for comparing TokenType values in our unit tests
    TokenType getType()
    {
        return tokentype;
    }

    //return the value of the token in the form of a string
    public String toString() {
        return tokentype + "(" + tokenvalue + ")";
    }
}
