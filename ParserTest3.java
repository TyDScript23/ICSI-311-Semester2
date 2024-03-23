import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertThrows;

public class ParserTest3 {
    @Test
    public void parserShouldBeAbleToHandleGoSubStatementsCorrectly() throws Exception {
        var file = "GOSUB myprogram";
        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Parser parse = new Parser(tokens);

        String result = parse.toString();

        Assertions.assertEquals(result, "GOSUB myprogram");
    }

    @Test
    public void parserShouldCatchIncompleteGoSubStatements(){
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "GOSUB";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "GOSUB must be followed by a process name!");
    }

    @Test
    public void parserShouldBeAbleToHandleForStatementsCorrectly() throws Exception {
        var file = "FOR x = 1 TO count STEP 1\n" +
                "\tREAD name$, grade1, grade2, grade3, grade4, grade5\n" +
                "\taverage = (grade1 + grade2 + grade3 + grade4 + grade5) / 5\n" +
                "\tPRINT name$, \"'s average is \", average\n" +
                "NEXT x";
        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Parser parse = new Parser(tokens);

        String result = parse.toString();

        Assertions.assertEquals(result, "FOR x = 1 TO count STEP 1 \n" +
                "\tREAD name$ , grade1 , grade2 , grade3 , grade4 , grade5\n" +
                "\taverage = ((grade1 + (grade2 + (grade3 + (grade4 + grade5)))) / 5)\n" +
                "\tPRINT name$ , \"'s average is \" , average \n" +
                "NEXT x");
    }

    @Test
    public void forStatementsShouldHaveInitializationStatements(){
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "FOR TO count STEP 1";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "FOR keyword must be followed by an initialization expression! (ex. x = 1)");
    }

    @Test
    public void improperForInitializationStatementsShouldBeCaught(){
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "FOR x = TO count STEP 1";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "An equals sign must be followed by an number or expression!");
    }

    @Test
    public void FORStatementsShouldNotMissTOexpression(){
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "FOR x = 1 count STEP 1";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "FOR statement does not contain a TO!");
    }

    @Test
    public void FORStatementsShouldContainALimitVariableOrNumber(){
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "FOR x = 1 TO STEP 1";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "End bound must be number or integer variable!");
    }

    @Test
    public void FORStatementsMustContainSTEPExpressions(){
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "FOR x = 1 TO count ";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "A FOR statement must have a NEXT statement at its end!");
    }

    @Test
    public void TOStatementsCannotBeParsedAlone(){
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "TO count STEP 1";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "TO statements cannot occur by themselves!");
    }

    @Test
    public void STEPStatementsCannotBeParsedAlone(){
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file =  "STEP 1";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "STEP statements cannot occur by themselves!");
    }



    @Test
    public void STEPexpressionOfAForStatementMustContainAValue(){
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "FOR x = 1 TO count STEP";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "STEP in FOR statement must be followed by a number!");
    }

    @Test
    public void UnsupportedEndCountTypesMustBeCaught(){
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "FOR x = 1 TO \"string literal\" STEP 1";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "End bound must be number or integer variable!");
    }

    @Test
    public void parserShouldRejectLoneNextStatements(){
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "NEXT x";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "NEXT statements cannot occur by themselves!");
    }

    @Test
    public void labelsShouldBeHandledCorrectly() throws Exception {
        var file = "myProgram: FOR x = 1 TO count STEP 1 \n" +
                "\tREAD name$ , grade1 , grade2 , grade3 , grade4 , grade5\n" +
                "\taverage = ((grade1 + (grade2 + (grade3 + (grade4 + grade5)))) / 5)\n" +
                "\tPRINT name$ , \"'s average is \" , average \n" +
                "NEXT x \n" + "RETURN";
        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Parser parse = new Parser(tokens);

        String result = parse.toString();

        Assertions.assertEquals(result, "myProgram: \n\tFOR x = 1 TO count STEP 1 \n" +
                "\tREAD name$ , grade1 , grade2 , grade3 , grade4 , grade5\n" +
                "\taverage = ((grade1 + (grade2 + (grade3 + (grade4 + grade5)))) / 5)\n" +
                "\tPRINT name$ , \"'s average is \" , average \n" +
                "NEXT x \n" +
                "RETURN");
    }

    @Test
    public void labelsCanContainMoreThanOneStatement() throws Exception {
        var file = "isEven:\n" +
                "\tx = num / 2\n" +
                "\tIF ((x*2) = num) THEN num = num / 2\n" +
                "\tIF ((x*2) <> num) THEN num = (num * 3) + 1\n" +
                "RETURN";
        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Parser parse = new Parser(tokens);

        String result = parse.toString();

        Assertions.assertEquals(result, "isEven: \n" +
                "\tx = (num / 2)\n" +
                "\tIF ((x * 2) = num) THEN num = (num / 2)\n" +
                "\tIF ((x * 2) <> num) THEN num = ((num * 3) + 1) \n" +
                "RETURN");
    }

    @Test
    public void endStatementsShouldBeHandledCorrectly() throws Exception {

        var file = "END";
        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Parser parse = new Parser(tokens);

        String result = parse.toString();

        Assertions.assertEquals(result, "END");
    }

    @Test
    public void ifStatementsShouldBeParsedAsExpected() throws Exception {
        var file = "IF (x = 1) THEN PRINT word, word, 5";
        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Parser parse = new Parser(tokens);

        String result = parse.toString();

        Assertions.assertEquals(result, "IF (x = 1) THEN PRINT word , word , 5");
    }

    @Test
    public void IfStatementsMustContainAConditional(){
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "IF THEN PRINT word, word, 5";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "IF keyword must be followed by a boolean expression!");
    }

    @Test
    public void conditionalParenthesisMustContainABooleanExpression(){
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "IF () THEN PRINT word, word, 5";
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
    public void booleanConditionalStatementsMustBeComplete(){
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "IF (x = 1 THEN PRINT word, word, 5";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "Left parenthesis must have a matching right parenthesis!");
    }

    @Test
    public void booleanConditionalStatementsMustBeComplete2(){
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "IF (x > ) THEN PRINT word, word, 5";
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
    public void allIfStatementsMustContainATHEN(){
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "IF (x = 1) ";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "IF must contain a THEN statement!");
    }

    @Test
    public void loneThenStatementsAreNotAllowed(){
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "THEN PRINT word";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "THEN statements cannot occur by themselves!");
    }

    @Test
    public void ifStatementsMustHaveContent(){
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "IF (x = 1) THEN";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "IF statement must contain valid contents!");
    }

    @Test
    public void nestedIfStatementsCanBeParsed() throws Exception {
        var file = "IF (x = 1) THEN IF (x = 2) THEN PRINT word, word, 5";
        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Parser parse = new Parser(tokens);

        String result = parse.toString();

        Assertions.assertEquals(result, "IF (x = 1) THEN IF (x = 2) THEN PRINT word , word , 5");
    }

    @Test
    public void whileStatementsShouldBeParsedNormally() throws Exception {
        var file = "WHILE (x <> 1) endWhileLabel\n" +
                "\tGOSUB isEven\n" +
                "\tcount = count + 1\n" +
                "endWhileLabel:";
        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Parser parse = new Parser(tokens);

        String result = parse.toString();

        Assertions.assertEquals(result, "WHILE (x <> 1) endWhileLabel: \n" +
                "\tGOSUB isEven\n" +
                "\tcount = (count + 1) \n" +
                "endWhileLabel:");
    }

    @Test
    public void whileStatementsMustHaveBooleanExpressions(){
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "WHILE endWhileLabel\n" +
                    "\tGOSUB isEven\n" +
                    "\tcount = count + 1\n" +
                    "endWhileLabel:";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "WHILE statement must be followed by a boolean expression!");
    }

    @Test
    public void whileStatementsBooleanExpressionsMustBeComplete(){
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "WHILE (x 1) endWhileLabel\n" +
                    "\tGOSUB isEven\n" +
                    "\tcount = count + 1\n" +
                    "endWhileLabel:";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "We have encountered an unhandled boolean operator!");
    }

    @Test
    public void whileStatementsBooleanExpressionsMustBeComplete2(){
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "WHILE (x <> 1 endWhileLabel\n" +
                    "\tGOSUB isEven\n" +
                    "\tcount = count + 1\n" +
                    "endWhileLabel:";
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
    public void whileStatementsMustContainAnEndLabelDeclaration(){
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "WHILE (x <> 1) \n" +
                    "\tGOSUB isEven\n" +
                    "\tcount = count + 1\n" +
                    "endWhileLabel:";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "You must declare an ending label before the while loop content!");
    }

    @Test
    public void whileStatementMustHaveAnEndLabel(){
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "WHILE (x <> 1) endWhileLabel\n" +
                    "\tGOSUB isEven\n" +
                    "\tcount = count + 1\n";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "An end label was not provided!");
    }

    @Test
    public void whileStatementLabelsMustMatch(){
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "WHILE (x <> 1) endWhileLabel\n" +
                    "\tGOSUB isEven\n" +
                    "\tcount = count + 1\n" +
                    "endWhile:";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "Both references to the end label in the while loop must match!");
    }

    @Test
    public void forStatementVariablesMustMatch(){
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "FOR x = 1 TO count STEP 1\n" +
                    "\tREAD name$, grade1, grade2, grade3, grade4, grade5\n" +
                    "\taverage = (grade1 + grade2 + grade3 + grade4 + grade5) / 5\n" +
                    "\tPRINT name$, \"'s average is \", average\n" +
                    "NEXT y";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "The counting variable used in a FOR statement must match in the NEXT statement!");
    }

    @Test
    public void forStatementsWithoutStepsMustBeHandled() throws Exception {
        var file = "FOR x = 1 TO count\n" +
                "\tREAD name$, grade1, grade2, grade3, grade4, grade5\n" +
                "\taverage = (grade1 + grade2 + grade3 + grade4 + grade5) / 5\n" +
                "\tPRINT name$, \"'s average is \", average\n" +
                "NEXT x";
        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Parser parse = new Parser(tokens);

        String result = parse.toString();

        Assertions.assertEquals(result, "FOR x = 1 TO count \n" +
                "\tREAD name$ , grade1 , grade2 , grade3 , grade4 , grade5\n" +
                "\taverage = ((grade1 + (grade2 + (grade3 + (grade4 + grade5)))) / 5)\n" +
                "\tPRINT name$ , \"'s average is \" , average \n" +
                "NEXT x");
    }

    @Test
    public void functionStatementsShouldBeHandledProperly() throws Exception {
        var file = "PRINT FUNCTION functionname(variable,5,\"stringliteral\"),variable,variable";
        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Parser parse = new Parser(tokens);

        String result = parse.toString();

        Assertions.assertEquals(result, "PRINT FUNCTION functionname (variable , 5 , \"stringliteral\")  , variable , variable");
    }

    @Test
    public void functionStatementsMustContainFunctionNames(){
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "PRINT FUNCTION (variable,5,\"stringliteral\"),variable,variable";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "FUNCTION statements must be followed by a function name!");
    }

    @Test
    public void functionKeywordsCannotExistOnTheirOwn(){
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "PRINT FUNCTION";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "FUNCTION statements must be followed by a function name!");
    }

    @Test
    public void functionStatementsMustHaveCompleteLists(){
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "PRINT FUNCTION functionname(variable,),variable,variable";
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
    public void functionStatementsMustHaveCompleteLists2(){
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "PRINT FUNCTION functionname(variable,variable,variable,variable";
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
    public void functionStatementsMustHaveLists(){
        Throwable exception = assertThrows(java.lang.Exception.class, () -> {
            var file = "PRINT FUNCTION functionname , variable, variable";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);
        });

        Assertions.assertEquals(exception.getMessage(), "Function names must be followed by a complete list surrounded by parenthesis!");
    }

    @Test
    public void functionStatementsWithoutParametersAreValid() throws Exception {

            var file = "PRINT FUNCTION functionname() , variable , variable";
            var codehand = new CodeHandler();
            codehand.setFileContent(file);
            Lexer lexer = new Lexer();
            lexer.Lex(codehand);

            var tokens = lexer.getContent();

            Parser parse = new Parser(tokens);

            String result = parse.toString();

            Assertions.assertEquals(result, "PRINT FUNCTION functionname()  , variable , variable");
    }

    @Test
    public void program1() throws Exception {

        var file = "count = 0\n" +
                "\n" +
                "INPUT \"Input a number greater than 1: \", num\n" +
                "\n" +
                "WHILE (x <> 1) endWhileLabel\n" +
                "\tGOSUB isEven\n" +
                "\tcount = count + 1\n" +
                "endWhileLabel:\n" +
                "\n" +
                "isEven:\n" +
                "\tx = num / 2\n" +
                "\tIF ((x*2) = num) THEN num = num / 2\n" +
                "\tIF ((x*2) <> num) THEN num = (num * 3) + 1\n" +
                "RETURN\n" +
                "\n" +
                "PRINT \"The number of steps we had to take is: \", count\n" +
                "\n" +
                "END";
        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Parser parse = new Parser(tokens);

        String result = parse.toString();

        Assertions.assertEquals(result, "count = 0\n" +
                "Input \"Input a number greater than 1: \" , num\n" +
                "WHILE (x <> 1) endWhileLabel: \n" +
                "\tGOSUB isEven\n" +
                "\tcount = (count + 1) \n" +
                "endWhileLabel:\n" +
                "isEven: \n" +
                "\tx = (num / 2)\n" +
                "\tIF ((x * 2) = num) THEN num = (num / 2)\n" +
                "\tIF ((x * 2) <> num) THEN num = ((num * 3) + 1) \n" +
                "RETURN\n" +
                "PRINT \"The number of steps we had to take is: \" , count\n" +
                "END");
    }

    @Test
    public void program2() throws Exception {

        var file = "FOR x = 1 TO 100 STEP 1\n" +
                "\tGOSUB ifDivisable\n" +
                "NEXT x\n" +
                "\n" +
                "ifDivisable:\n" +
                "    \ty = x / 3\n" +
                "    \tIF ((y*3) = x) THEN PRINT \"Fizz\"\n" +
                "\tz = x / 5\n" +
                "\tIF ((z * 5) = x) THEN PRINT \"Buzz\"\n" +
                "\ta = x / 15\n" +
                "    \tIF ((a*15) = x) THEN PRINT \"FizzBuzz\"\n" +
                "\tx = x + 1\n" +
                "RETURN\n" +
                "\n" +
                "END";
        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Parser parse = new Parser(tokens);

        String result = parse.toString();

        Assertions.assertEquals(result, "FOR x = 1 TO 100 STEP 1 \n" +
                "\tGOSUB ifDivisable \n" +
                "NEXT x\n" +
                "ifDivisable: \n" +
                "\ty = (x / 3)\n" +
                "\tIF ((y * 3) = x) THEN PRINT \"Fizz\"\n" +
                "\tz = (x / 5)\n" +
                "\tIF ((z * 5) = x) THEN PRINT \"Buzz\"\n" +
                "\ta = (x / 15)\n" +
                "\tIF ((a * 15) = x) THEN PRINT \"FizzBuzz\"\n" +
                "\tx = (x + 1) \n" +
                "RETURN\n" +
                "END");
    }

    @Test
    public void program3() throws Exception {

        var file = "DATA 5, \"Tyler\", 89, 95, 90, 89, 87, \"Jeremy\", 100, 91, 88, 79, 95, \"John\", 97, 84, 91, 92, 93, \"Adam\", 85, 76, 66, 75, 71, \"Justin\", 100, 100, 65, 65, 80\n" +
                "READ count\n" +
                "\n" +
                "FOR x = 1 TO count STEP 1\n" +
                "\tREAD name$, grade1, grade2, grade3, grade4, grade5\n" +
                "\taverage = (grade1 + grade2 + grade3 + grade4 + grade5) / 5\n" +
                "\tPRINT name$, \"'s average is \", average\n" +
                "NEXT x\n" +
                "\n" +
                "END";
        var codehand = new CodeHandler();
        codehand.setFileContent(file);
        Lexer lexer = new Lexer();
        lexer.Lex(codehand);

        var tokens = lexer.getContent();

        Parser parse = new Parser(tokens);

        String result = parse.toString();

        Assertions.assertEquals(result, "DATA 5 , \"Tyler\" , 89 , 95 , 90 , 89 , 87 , \"Jeremy\" , 100 , 91 , 88 , 79 , 95 , \"John\" , 97 , 84 , 91 , 92 , 93 , \"Adam\" , 85 , 76 , 66 , 75 , 71 , \"Justin\" , 100 , 100 , 65 , 65 , 80\n" +
                "READ count\n" +
                "FOR x = 1 TO count STEP 1 \n" +
                "\tREAD name$ , grade1 , grade2 , grade3 , grade4 , grade5\n" +
                "\taverage = ((grade1 + (grade2 + (grade3 + (grade4 + grade5)))) / 5)\n" +
                "\tPRINT name$ , \"'s average is \" , average \n" +
                "NEXT x\n" +
                "END");
    }
}