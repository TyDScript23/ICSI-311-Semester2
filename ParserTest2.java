import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertThrows;

public class ParserTest2 {
    //-------------------------------------------------------------------------------------
    //      Parser 2

    @Test
    public void commasShouldNowBeHandledByLexer() throws Exception {
        var file = "PRINT argument1,argument2,5";
        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Assertions.assertEquals(6, tokens.size());
        Assertions.assertEquals(Token.TokenType.PRINT, tokens.get(0).getType());
        Assertions.assertEquals(Token.TokenType.WORD, tokens.get(1).getType());
        Assertions.assertEquals(Token.TokenType.COMMA, tokens.get(2).getType());
        Assertions.assertEquals(Token.TokenType.WORD, tokens.get(3).getType());
        Assertions.assertEquals(Token.TokenType.COMMA, tokens.get(4).getType());
        Assertions.assertEquals(Token.TokenType.NUMBER, tokens.get(5).getType());
    }

    @Test
    public void printStatementsShouldBeParsedAsExpected() throws Exception{
        var file = "PRINT argument1,(4 + 3),5";
        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Parser parse = new Parser(tokens);

        String result = parse.toString();

        Assertions.assertEquals(result, "PRINT argument1 , (4 + 3) , 5");
    }

    @Test
    public void assignmentStatementsShouldBeParserAsExpected() throws Exception{
        var file = "myVariable = ((3 * 4) + 5)";
        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Parser parse = new Parser(tokens);

        String result = parse.toString();

        Assertions.assertEquals(result,"myVariable = ((3 * 4) + 5)");
    }

    @Test
    public void anyExtraCommasInPrintStatementsShouldThrowAnError() {

        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "PRINT argument1,(4 + 3),5,";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "A comma must be followed by another printable argument!!!");
    }

    @Test
    public void aMissingArgumentInPrintStatementsShouldBeHandled() {

        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "PRINT argument1, ,5";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "A comma must be followed by another printable argument!!!");
    }

    @Test
    public void allPrintListArgumentsMustBeSeparatedByCommas() {

        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "PRINT argument1 3,5";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "A lone number was encountered!");
    }

    @Test
    public void anyPrintStatementsWithoutArguementsShouldThrowAnError() {
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "PRINT";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "PRINT statements must have arguments following them!!!");
    }

    @Test
    public void loneVariablesInAnAssignmentStatementShouldThrowAnError() {
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "x";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "A lone word was encountered!!!");
    }

    @Test
    public void anAssignemntStatementWithoutAnythingAfterTheEqualSignShouldThrowAnError() {
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "x = ";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "An equals sign must be followed by an expression!!!");
    }

    @Test
    public void parserShouldParseMultipleStatements() throws Exception{
        var file = "x = \n 3 \n * \n4 PRINT \n argument1, \n argument2 \n y \n = expression \n";
        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Parser parse = new Parser(tokens);

        Assertions.assertEquals(parse.toString(),"x = (3 * 4)\n" +
                "PRINT argument1 , argument2\n" +
                "y = expression");
    }

    //-------------------------------------------------------------------------------------
    //      Parser 3

    @Test
    public void makeSureThatStringLiteralsCanBeUsedInPrintStatements() throws Exception{
        var file = "PRINT variablename, \"string literal\", 5.3";
        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Parser parse = new Parser(tokens);

        Assertions.assertEquals(parse.toString(),"PRINT variablename , \"string literal\" , 5.3");
    }

    @Test
    public void makeSureThatReadStatementsAreParsedProperly() throws Exception{
        var file = "READ variable1, variable2, variable3";
        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Parser parse = new Parser(tokens);

        Assertions.assertEquals(parse.toString(),"READ variable1 , variable2 , variable3");
    }

    @Test
    public void makeSureThatDataStatementsAreParsedProperly() throws Exception{
        var file = "DATA \"stringliteral\", 5, 7.8";
        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Parser parse = new Parser(tokens);

        Assertions.assertEquals(parse.toString(), "DATA \"stringliteral\" , 5 , 7.8");
    }

    @Test
    public void makeSureThatInputStatementsAreParsedProperly() throws Exception{
        var file = "INPUT \"stringliteral\",\"stringliteral\",variable";
        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Parser parse = new Parser(tokens);

        Assertions.assertEquals(parse.toString(),"Input \"stringliteral\" , \"stringliteral\" , variable");
    }

    @Test
    public void unhandledDataTypesInReadStatementsShouldThroughErrors() {
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "READ variable1, variable2, 3";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "READ lists must only contain variables!!!");
    }

    @Test
    public void unhandledDataTypesInDataStatementsShouldThroughErrors() {
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "DATA \"stringliteral\", variable, 7.8";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "DATA lists must only contain string literals, integers, and floats!!!");
    }

    @Test
    public void unhandledDataTypesInInputStatementsShouldThroughErrors() {
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "INPUT 3.5,\"stringliteral\",variable";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "INPUT lists must only contain variables and constant strings!!!");
    }

    @Test
    public void readStatementsShouldHAveCompleteLists() {
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "READ variable, ,variable";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "Commas must be followed by another readable argument!!!");
    }

    @Test
    public void readStatementsShouldNotContainUnhandledDataTypes() {
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "READ \"string literal\", variable ,variable";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "READ lists must only contain variables!!!");
    }

    @Test
    public void readTokensMustBeFollowedByAListOfItems() {
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "READ";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "READ statements must have arguments following them!!!");
    }

    @Test
    public void readShouldHaveCommasSeparatingItsList() {
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "READ variable variable, variable";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "A lone word was encountered!!!");
    }

    @Test
    public void dataStatementsShouldHAveCompleteLists() {
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "DATA \"string literal\", , 6.7";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "Commas must be followed by another readable argument!!!");
    }

    @Test
    public void dataStatementsShouldNotContainUnhandledDataTypes() {
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "DATA variable, 3 , 6.5";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "DATA lists must only contain string literals, integers, and floats!!!");
    }

    @Test
    public void dataTokensMustBeFollowedByAListOfItems() {
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "DATA";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "DATA statements must have arguments following them!!!");
    }

    @Test
    public void dataShouldHaveCommasSeparatingItsList() {
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "DATA  \"string literal\" 3, 6.5";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "A lone number was encountered!");
    }

    @Test
    public void inputStatementsShouldHaveCompleteLists() {
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "INPUT \"string literal\", , variable";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "Commas must be followed by another readable argument!!!");
    }

    @Test
    public void inputStatementsShouldNotContainUnhandledDataTypes() {
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "INPUT variable, 3 , variable";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "INPUT lists must only contain variables and constant strings!!!");
    }

    @Test
    public void inputTokensMustBeFollowedByAListOfItems() {
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "INPUT";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "INPUT statements must have arguments following them!!!");
    }

    @Test
    public void inputShouldHaveCommasSeparatingItsList() {
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "INPUT  \"string literal\" \"string literal\", variable";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "A lone string was encountered!");
    }

    @Test
    public void multipleKeywordStatementsMustHaveListsFollowingThem() {
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "READ \n DATA \n INPUT \n";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "READ lists must only contain variables!!!");
    }

    @Test
    public void assignmentExpressionsMustBeValid() {
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "x = = variable";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "An equals sign must be followed by an expression!!!");
    }

    @Test
    public void allStatementsShouldBeHandledCorrectlyTogetherUpToThisPoint() throws Exception{
        var file = "x = \n 3 \n * \n4 PRINT \n argument1, \n argument2 \n y \n = expression \n " +
                "READ variable1, variable2, variable3 " +
                "DATA \"stringliteral\", 5, 7.8" +
                "INPUT \"stringliteral\",\"stringliteral\",variable";
        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Parser parse = new Parser(tokens);

        Assertions.assertEquals(parse.toString(),"x = (3 * 4)\n" +
                "PRINT argument1 , argument2\n" +
                "y = expression\n" +
                "READ variable1 , variable2 , variable3\n" +
                "DATA \"stringliteral\" , 5 , 7.8\n" +
                "Input \"stringliteral\" , \"stringliteral\" , variable");
    }
}
