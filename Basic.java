/*
 * Name: Tyler Davis
 * Class: ICSI-311	Principles of Programming
 *
 * Project: Lexer Part 1
 * Date Due: 01/30/2024
 *
 * Summary:
 *          This projects purpose is to mimic the functionality of Lexer
 * tokenizing BASIC. The following code should be able to
 * accept a command line argument for the BASIC file the user wishes to read
 * and read and count each line of the file, splitting the contained text into its
 * individual parts and returning a LinkedList of components.
 *
 * Errors Throw Cases:
 *      - more than 1 argument should not be accepted form the command line
 *      - any character type that cannot currently be read should throw an error
 *      - more than two decimal points in one number should be counted as incorrect syntax
 *      - unmatched parenthesis in a mathematical equation should not be allowed
 *      - unhandled characters should not be present in a mathematical equation
 *      - commas in print statements should have arguments following them
 *      - print statements must be provided arguments
 *      - each print statement's arguments should be separated by commas
 *      - equal signs must be followed by an expression
 *      - variables names called without the data type must be followed by an assignment
 *      - commas in read statements should have arguments following them
 *      - read statements must be provided arguments
 *      - each print read's arguments should be separated by commas
 *      - commas in data statements should have arguments following them
 *      - data statements must be provided arguments
 *      - each data statement's arguments should be separated by commas
 *      - commas in input statements should have arguments following them
 *      - input statements must be provided arguments
 *      - each input statement's arguments should be separated by commas
 *      - lone words, numbers, or strings should not be parsed
 */

public class Basic {
    public static void main(String[] args) throws Exception {

        String filename;

        if(args.length != 1)
        {
            Error(2,"You may only input 1 argument to main!!!");
        }
        else{
            filename = args[0];

            Lexer lexinst = new Lexer();
            CodeHandler codehand = new CodeHandler(filename);
            lexinst.Lex(codehand);
            Parser par = new Parser(lexinst.getContent());
            System.out.println(par);
        }
    }

    public static void Error(int linenumber, String reason) throws Exception {
        throw new Exception("Error:\n\tLine number: " + linenumber + " - " + reason);
    }
}