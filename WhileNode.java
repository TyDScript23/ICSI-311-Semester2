import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WhileNode extends StatementNode{
    private final Node booleanexpression;
    private final List<StatementNode> contents;
    private final String endLabel;

    WhileNode(){
        booleanexpression = null;
        contents = new ArrayList<>();
        endLabel = " ";
    }

    WhileNode(Node boolexp, List<StatementNode> statements, String endl)
    {
        booleanexpression = boolexp;
        contents = statements;
        endLabel = endl;
    }

    List<StatementNode> getStatements(){
        return contents;
    }

    Node getBooleanexpression(){
        return booleanexpression;
    }

    public String toString(){

        StringBuilder inside = new StringBuilder();

        if(contents.get(0) != null)
        {
            inside.append("\t").append(contents.get(0));
        }

        for(int i = 1; i < contents.size(); i++)
        {
            inside.append("\n").append("\t").append(contents.get(i));
        }

        return "WHILE (" + booleanexpression + ") " + endLabel + " \n" + inside + " \n" + endLabel;
    }
}
