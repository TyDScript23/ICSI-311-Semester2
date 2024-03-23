import java.util.*;

public class Lexer {

    private final LinkedList<Token> tokenlist = new LinkedList<>();
    private int linenumber;
    private int charposition;

    private final HashMap<String, Token.TokenType> keywordhashmap = new HashMap<>();
    private final HashMap<String, Token.TokenType> doublehashmap = new HashMap<>();
    private final HashMap<String, Token.TokenType> singlehashmap = new HashMap<>();

    /*
    * Default constructor for our Lexer object
    *
    * Actions:
    * - initializes the CodeHandler
    * - calls the Lex function with the CodeHandler object
    * - initializes the line number and character position to 0
    */

    public Lexer(){
        keywordHashmap();
        singleSymHashmap();
        doubleSymHashmap();
        linenumber = 0;
        charposition = 0;
    }

    //this function lexes the information from the CodeHandler in the following manner:

    /*
    1.) If the next character is a space or tab (\t), nothing will happen and the character
    position will be incremented

    2.) If the character is a linefeed (\n), a ENDOFLINE token will be created and added
    to our LinkedList

    3.) If the character is a return (\r), it will be ignored

    4.) If the character is a letter, we will call the "ProcessWord"
    method and will send the result to our list of tokens

    5.) if the character is a digit, we will call the "ProcessDigit" method and add
    the result to our list of tokens

    6.) If anything else not fitting the above criteria is found, we shall call ProcessSymbol
    and other unrecognized symbols will be handled accordingly

    the character position is moved forward after each character is checked unless
    told otherwise
     */

    void Lex(CodeHandler codeh) throws Exception {
        linenumber = 1;

        while (!(codeh.IsDone())){
            char next = codeh.getChar();
            charposition++;

            if (next == ' ' || next == '\t');
            else if (next == '\n')
            {
                tokenlist.add(new Token(Token.TokenType.ENDOFLINE, linenumber, charposition));
                charposition = 0;
                ++linenumber;
            }
            else if (next == '\r');
            else if (Character.isLetter(next))
            {
                tokenlist.add(ProcessWord(codeh));
            }
            else if (Character.isDigit(next))
            {
                tokenlist.add(ProcessDigit(codeh));
            }
            else if (next == '\"'){
                tokenlist.add(HandleStringLiteral(codeh));
            }
            else{
                tokenlist.add(ProcessSymbol(codeh));
            }
        }
    }

    private void keywordHashmap() {
        keywordhashmap.put("PRINT", Token.TokenType.PRINT);
        keywordhashmap.put("READ", Token.TokenType.READ);
        keywordhashmap.put("INPUT", Token.TokenType.INPUT);
        keywordhashmap.put("DATA", Token.TokenType.DATA);
        keywordhashmap.put("GOSUB", Token.TokenType.GOSUB);
        keywordhashmap.put("FOR", Token.TokenType.FOR);
        keywordhashmap.put("TO", Token.TokenType.TO);
        keywordhashmap.put("STEP", Token.TokenType.STEP);
        keywordhashmap.put("NEXT", Token.TokenType.NEXT);
        keywordhashmap.put("RETURN", Token.TokenType.RETURN);
        keywordhashmap.put("IF", Token.TokenType.IF);
        keywordhashmap.put("THEN", Token.TokenType.THEN);
        keywordhashmap.put("FUNCTION", Token.TokenType.FUNCTION);
        keywordhashmap.put("WHILE", Token.TokenType.WHILE);
        keywordhashmap.put("END", Token.TokenType.END);
    }

    private void doubleSymHashmap() {
        doublehashmap.put("<=", Token.TokenType.LESSTHANOREQUALTO);
        doublehashmap.put(">=", Token.TokenType.GREATERTHANOREQUALTO);
        doublehashmap.put("<>", Token.TokenType.NOTEQUALS);
    }

    private void singleSymHashmap() {
        singlehashmap.put("=", Token.TokenType.EQUALS);
        singlehashmap.put("<", Token.TokenType.LESSTHAN);
        singlehashmap.put(">", Token.TokenType.GREATERTHAN);
        singlehashmap.put("(", Token.TokenType.LEFTPARENTHESIS);
        singlehashmap.put(")", Token.TokenType.RIGHTPARENTHESIS);
        singlehashmap.put("+", Token.TokenType.ADD);
        singlehashmap.put("-", Token.TokenType.SUBTRACT);
        singlehashmap.put("*", Token.TokenType.MULTIPLY);
        singlehashmap.put("/", Token.TokenType.DIVIDE);
        singlehashmap.put(",", Token.TokenType.COMMA);
    }

    /*
    "ProcessWord" will read letter chars while they are present and
     call the "PeekString" function from CodeHandler to return a substring
     of the next word

     **If a comma is encountered after the word a LABEL token is returned

     At the end, a WORD token is created and returned to our LinkedList
     */
    Token ProcessWord(CodeHandler codeh)
    {
        String wordvalue;
        int readpoint = 0;
        Token wordtoken;

        while(Character.isLetter(codeh.Peek(readpoint)) || Character.isDigit(codeh.Peek(readpoint))) {
            readpoint++;
            charposition++;
        }
        if(codeh.Peek(readpoint) == '$' || codeh.Peek(readpoint) == '%')
        {
            readpoint++;
            charposition++;
        }
        if(codeh.Peek(readpoint) == ':')
        {
            readpoint++;
            charposition++;

            wordvalue = codeh.PeekString(readpoint);
            wordtoken = new Token(Token.TokenType.LABEL, linenumber, charposition, wordvalue);

            codeh.Swallow(readpoint);

            return wordtoken;
        }

        wordvalue = codeh.PeekString(readpoint);

        wordtoken = new Token(keywordhashmap.getOrDefault(wordvalue, Token.TokenType.WORD), linenumber, charposition, wordvalue);

        codeh.Swallow(readpoint);

        return wordtoken;
    }

    /*
    "ProcessDigit" will read digit chars while they are present and
     call the "PeekString" function from CodeHandler to return a substring
     of the next number sequence

     At the end, a NUMBER token is created and returned to our LinkedList
     */
    Token ProcessDigit(CodeHandler codeh) throws Exception {
        String numbervalue;
        boolean foundDecimal = false;
        int readpoint = 0;
        Token numbertoken;

        while (Character.isDigit(codeh.Peek(readpoint)) || codeh.Peek(readpoint) == '.') {

            if (foundDecimal && codeh.Peek(readpoint) == '.'){
                Error(linenumber, charposition, "Two points were found! Invalid number!");
            }
            else if (codeh.Peek(readpoint) == '.'){
                foundDecimal = true;
            }

            readpoint++;
            charposition++;
        }

        numbervalue = codeh.PeekString(readpoint);
        codeh.Swallow(readpoint);

        numbertoken = new Token(Token.TokenType.NUMBER, linenumber, charposition, numbervalue);

        return numbertoken;
    }

    /*
        The following function handles a string literal when it encounters a
        double quote. It will continue to read each character following this quote
        until it encounters another quote (closing quote), then it returns a
        STRINGLITERAL token
     */
    Token HandleStringLiteral(CodeHandler codeh) {
        char thisChar = codeh.Peek(1);
        String literalvalue;
        int stringread = 0;

        while(codeh.Peek(stringread) != '\"') {
            if(thisChar == '\\') {
                stringread++;
                charposition++;
            }

            stringread++;
            charposition++;

            thisChar = codeh.Peek(stringread);
        }

        literalvalue = codeh.PeekString(stringread) + '\"';
        codeh.Swallow(stringread + 1);

        return new Token(Token.TokenType.STRINGLITERAL, linenumber, charposition, literalvalue);
    }

    /* "ProcessSymbol" will be called when no other character condition in the Lex
     * method is met
     *
     * It will begin by peeking 2 spaces ahead of the index and storing the result to a String.
     * If this String matches a supported character in our "doublehashmap" hashmap, it
     * will be assigned its own token and that token will be returned
     *
     * Else we will peek only one space ahead of the index and store the result to a String.
     * 		If this String matches a supported character in our "singlehashmap" hashmap, it
     * 		will be assigned its own token and that token will be returned
     *
     * 		Else we will return an Error saying that the character of the original file is not supported
     *
     */

    Token ProcessSymbol(CodeHandler codeh) throws Exception {
        String value;
        Token symtoken = null;

        value = codeh.PeekString(1);

        if(doublehashmap.containsKey(value)) {
            symtoken = new Token(doublehashmap.get(value),linenumber,charposition,value);
            codeh.Swallow(1);
        }
        else
        {
            value = codeh.PeekString(0);

            if(singlehashmap.containsKey(value)) {
                symtoken = new Token(singlehashmap.get(value),linenumber,charposition,value);
            }
            else
            {
                Error(linenumber,charposition,"The symbol '" + value + "' is not supported!");
            }
        }

        return symtoken;
    }

    public LinkedList<Token> getContent(){
        return tokenlist;
    }

    //output error message with corresponding data in the form of a message printed to the terminal
    private static void Error(int linenum, int charpos, String reason) throws Exception {
        throw new Exception("Error:\n\tLine number: " + linenum + "\tCharacter Position: " + charpos + "\n\tMessage: " + reason);
    }

    public String toString(){
        return "TokenList: \n" + tokenlist;
    }
}