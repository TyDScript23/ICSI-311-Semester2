import java.util.ArrayList;
import java.util.List;

public class StatementsNode extends Node{

    private final List<StatementNode> statementlist;

    StatementsNode() {
        statementlist = new ArrayList<>();
    }
    StatementsNode (List<StatementNode> input){
      statementlist = input;
    }

    List<StatementNode> getStatementList() {
        return statementlist;
    }

    void addStatement(StatementNode input)
    {
        statementlist.add(input);
    }

    public String toString(){
        StringBuilder result = new StringBuilder();
        Node node;

        result.append(statementlist.get(0));

        for (int i = 1; i < statementlist.size(); i++) {
            node = statementlist.get(i);
            result.append("\n").append(node);
        }

        return result.toString();
    }
}
