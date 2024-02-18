import java.nio.file.Files;
import java.nio.file.Paths;

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
 */

public class Basic {
    public static void main(String[] args) throws Exception {

        String fileinput;
        String filename;

        if(args.length != 1)
        {
            Error(2,"You may only input 1 argument to main!!!");
        }
        else{
            filename = args[0];
            fileinput = new String(Files.readAllBytes(Paths.get(filename)));

            Lexer lexinst = new Lexer(fileinput);
            System.out.println(lexinst);
        }
    }

    public static void Error(int linenumber, String reason) throws Exception {
        throw new Exception("Error:\n\tLine number: " + linenumber + " - " + reason);
    }
}