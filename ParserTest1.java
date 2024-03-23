import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertThrows;

/*
This JUnit Test is implemented for the following parts:
        Parser 1
 */

public class ParserTest1 {
    //-------------------------------------------------------------------------------------
    //      Parser 1

    @Test
    public void makeSureTheBaseLineParserCodeWorks() throws Exception {
        var file = "3 + 4 * 5";
        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Parser parse = new Parser(tokens);

        String result = parse.toString();
        Assertions.assertEquals(result, "(3 + (4 * 5))");
    }

    @Test
    public void anotherInputTestCase() throws Exception {
        var file = "3 + 4 * 5 - 6 / 7";
        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Parser parse = new Parser(tokens);

        String result = parse.toString();
        Assertions.assertEquals(result, "(3 + ((4 * 5) - (6 / 7)))");
    }

    @Test
    public void aThirdInputTestCase() throws Exception {
        var file = "3 / 4 * 5 + 6";
        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Parser parse = new Parser(tokens);

        String result = parse.toString();
        Assertions.assertEquals(result, "((3 / (4 * 5)) + 6)");
    }

    @Test
    public void aFourthInputTestCase() throws Exception {
        var file = "3 - 4 + 5";
        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Parser parse = new Parser(tokens);

        String result = parse.toString();
        Assertions.assertEquals(result, "(3 - (4 + 5))");
    }

    @Test
    public void parserShouldPickUpParenthesis() throws Exception {
        var file = "(3 * 4) + 5";
        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Parser parse = new Parser(tokens);

        String result = parse.toString();
        Assertions.assertEquals(result, "((3 * 4) + 5)");
    }

    @Test
    public void parserShouldHandleNumbersWithDecimalPoints() throws Exception {
        var file = "3.4 * 4.5 + 5.6 - 6.7";
        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Parser parse = new Parser(tokens);

        String result = parse.toString();
        Assertions.assertEquals(result, "((3.4 * 4.5) + (5.6 - 6.7))");
    }

    @Test
    public void parserShouldHandleMultipleMathematicalExpressions() throws Exception {
        var file = "3.4 * 4.5 5.6 - 6.7";
        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Parser parse = new Parser(tokens);

        String result = parse.toString();
        Assertions.assertEquals(result, "(3.4 * 4.5)\n" +
                "(5.6 - 6.7)");
    }

    @Test
    public void parserShouldCatchUnmatchedRightParenthesisInMathEquations() {
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "3 * 4) + 5";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "Right parenthesis does not have a matched left parenthesis!");
    }

    @Test
    public void parserShouldCatchUnmatchedLeftParenthesisInMathEquations() {
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "(3 * 4 + 5";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "Left parenthesis does not have a matching right parenthesis!");
    }

    @Test
    public void NestedParenthesisShouldBeHandled() throws Exception {
        var file = "((3 * 4) + 5)";
        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Parser parse = new Parser(tokens);

        String result = parse.toString();
        Assertions.assertEquals(result, "((3 * 4) + 5)");
    }

    @Test
    public void unhandledFactorsShouldBeCaught() {
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "3 * 4 + a";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "Encountered an unhandled factor!!!");
    }
}