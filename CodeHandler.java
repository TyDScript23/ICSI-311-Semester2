import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CodeHandler {
    private String filecontent;
    private int index;

    CodeHandler() {
        filecontent = " ";
        index = 0;
    }

    //initialization of the file content and index
    CodeHandler(String filen) throws IOException {
        filecontent = new String(Files.readAllBytes(Paths.get(filen)));
        index = 0;
    }

    //looks "i" characters ahead and returns that character
    //the index value is not changed!!!
    char Peek(int i)
    {
        return filecontent.charAt(index + i);
    }

    //returns a string of the next "i" characters
    //the index value is not changed!!!
    String PeekString(int i)
    {
        return filecontent.substring(index - 1, index  + i);
    }

    //returns the next character and moves the index
    char getChar() {
        index++;
        return filecontent.charAt(index - 1);
    }

    //moves the index ahead "i" positions
    void Swallow(int i) {
        index += i;
    }

    //returns true if we are at the end of the document
    boolean IsDone(){
        return Remainder().isEmpty();
    }

    //returns the rest of the document as a string
    String Remainder() {
        return filecontent.substring(index);
    }

    void setFileContent(String input)
    {
        filecontent = input + " ";
    }
}
