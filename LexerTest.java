import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertThrows;

/*
This JUnit Test is implemented for the following parts:
        Lexer 1
        Lexer 2
 */

public class LexerTest {

    //--------------------------------------------------------------------------------------
    //      LEXER 1

    //this tests the readability of multilined files
    @Test
    public void shouldReadMultilinedPrograms() throws Exception {
        var file = "wordone wordtwo 12.34 4.5\n" +
                "more word\t\t3.5 7.4\r";

        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Assertions.assertEquals(9, tokens.size());
        Assertions.assertEquals(Token.TokenType.WORD, tokens.get(0).getType());
        Assertions.assertEquals(Token.TokenType.WORD, tokens.get(1).getType());
        Assertions.assertEquals(Token.TokenType.NUMBER, tokens.get(2).getType());
        Assertions.assertEquals(Token.TokenType.NUMBER, tokens.get(3).getType());
        Assertions.assertEquals(Token.TokenType.ENDOFLINE, tokens.get(4).getType());
        Assertions.assertEquals(Token.TokenType.WORD, tokens.get(5).getType());
        Assertions.assertEquals(Token.TokenType.WORD, tokens.get(6).getType());
        Assertions.assertEquals(Token.TokenType.NUMBER, tokens.get(7).getType());
        Assertions.assertEquals(Token.TokenType.NUMBER, tokens.get(8).getType());
    }

    //this test the readability of single lined files
    @Test
    public void shouldReadSinglelinedPrograms() throws Exception {
        var file = "wordone wordtwo 12.34 4.5";

        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Assertions.assertEquals(4, tokens.size());
        Assertions.assertEquals(Token.TokenType.WORD, tokens.get(0).getType());
        Assertions.assertEquals(Token.TokenType.WORD, tokens.get(1).getType());
        Assertions.assertEquals(Token.TokenType.NUMBER, tokens.get(2).getType());
        Assertions.assertEquals(Token.TokenType.NUMBER, tokens.get(3).getType());
    }

    //this test should make sure that tabs and carriage returns are skipped
    @Test
    public void TabsAndReturnsShouldBeSkipped() throws Exception {
        var file = "wordone \t\t\r wordtwo";

        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Assertions.assertEquals(2, tokens.size());
    }

    //this test makes sure that too many command line arguments are not accepted
    @Test
    public void TooManyCommandLineArgumentsShouldNotBeAccepted() {
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            Basic.main(new String[]{"file1.basic", "file2.basic"});
        });

        Assertions.assertEquals(exception.getMessage(), "Error:\n" +
                "\tLine number: 2 - You may only input 1 argument to main!!!");
    }

    //this test makes sure that a number can't be lexed if it has more than one decimal point

    @Test
    public void ANumberShouldNotHaveMoreThanOneDecimalPoint() {

        var file = "wordone 12.34.5 5.7";

        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);
        });

        Assertions.assertEquals(exception.getMessage(), """
                Error:
                \tLine number: 1\tCharacter Position: 13
                \tMessage: Two points were found! Invalid number!""");
    }

    @Test
    public void wordsWithNumbersInsideAreFine() throws Exception {
        var file = "word123 12.34 5.7";

        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Assertions.assertEquals(3,tokens.size());
        Assertions.assertEquals(Token.TokenType.WORD,tokens.get(0).getType());
        Assertions.assertEquals(Token.TokenType.NUMBER,tokens.get(1).getType());
        Assertions.assertEquals(Token.TokenType.NUMBER,tokens.get(2).getType());
    }

    //--------------------------------------------------------------------------------------
    //      LEXER 2

    //This test makes sure that HandleString method functions as described
    @Test
    public void HandleStringLiteralShouldProcessAStringLiteralWithQuotationMarksInside() throws Exception {
        var file = "wordone wordtwo \"this is a \\\"string\\\" literal\"";

        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Assertions.assertEquals(3, tokens.size());
    }

    //This test makes sure that ProcessSymbol method functions as described
    @Test
    public void ProcessSymbolShouldCreateTokensAsExpected() throws Exception {

        var file = ">= <= <> = < > ( ) + - * /";

        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Assertions.assertEquals(12, tokens.size());

        Assertions.assertEquals(Token.TokenType.GREATERTHANOREQUALTO, tokens.get(0).getType());
        Assertions.assertEquals(Token.TokenType.LESSTHANOREQUALTO, tokens.get(1).getType());
        Assertions.assertEquals(Token.TokenType.NOTEQUALS, tokens.get(2).getType());
        Assertions.assertEquals(Token.TokenType.EQUALS, tokens.get(3).getType());
        Assertions.assertEquals(Token.TokenType.LESSTHAN, tokens.get(4).getType());
        Assertions.assertEquals(Token.TokenType.GREATERTHAN, tokens.get(5).getType());
        Assertions.assertEquals(Token.TokenType.LEFTPARENTHESIS, tokens.get(6).getType());
        Assertions.assertEquals(Token.TokenType.RIGHTPARENTHESIS, tokens.get(7).getType());
        Assertions.assertEquals(Token.TokenType.ADD, tokens.get(8).getType());
        Assertions.assertEquals(Token.TokenType.SUBTRACT, tokens.get(9).getType());
        Assertions.assertEquals(Token.TokenType.MULTIPLY, tokens.get(10).getType());
        Assertions.assertEquals(Token.TokenType.DIVIDE, tokens.get(11).getType());
    }

    //this test makes sure than any unexpected characters throw an error when
    //passed into ProcessSymbol and the proper error message is returned
    @Test
    public void UnsupportedCharactersShouldBeHandled() {
        var file = ">= <= <> = < > ( ) @ - * /";


        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);
        });

        Assertions.assertEquals(exception.getMessage(), """
                Error:
                \tLine number: 1\tCharacter Position: 17
                \tMessage: The symbol '@' is not supported!""");
    }

    @Test
    public void ProcessWordShouldProcessLabelIfColonIsFound() throws Exception {
        var file = "word word label:";

        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Assertions.assertEquals(3, tokens.size());

        Assertions.assertEquals(Token.TokenType.WORD, tokens.get(0).getType());
        Assertions.assertEquals(Token.TokenType.WORD, tokens.get(1).getType());
        Assertions.assertEquals(Token.TokenType.LABEL, tokens.get(2).getType());
    }
}