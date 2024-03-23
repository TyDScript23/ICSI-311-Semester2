import java.util.ArrayList;
import java.util.List;

public class LabeledStatementNode extends StatementNode{
    private final String label;
    private final List<StatementNode> contents;

    LabeledStatementNode(){
        label = "";
        contents = new ArrayList<>();
    }

    LabeledStatementNode(String l, List<StatementNode> s) {
        label = l;
        contents = s;
    }

    String getLabel(){
        return label;
    }

    List<StatementNode> getStatement(){
        return contents;
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

        return label + " \n" + inside + " \nRETURN";
    }
}
