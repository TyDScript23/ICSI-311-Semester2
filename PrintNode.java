import java.util.List;
import java.util.Objects;

public class PrintNode extends StatementNode{
    private final List<Node> nodelist;

    PrintNode(){
        nodelist = null;
    }

    PrintNode(List<Node> input){
        nodelist = input;
    }

    List<Node> getPrintList() {
        return nodelist;
    }

    public String toString(){
        StringBuilder result = new StringBuilder();
        Node node;

        for (int i = 0; i < Objects.requireNonNull(nodelist).size() - 1; i++) {
            node = nodelist.get(i);
            result.append(node).append(" , ");
        }

        result.append(nodelist.get(nodelist.size() - 1));

        return "PRINT " + result;
    }
}
