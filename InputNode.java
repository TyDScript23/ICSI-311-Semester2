import java.util.List;
import java.util.Objects;

public class InputNode extends StatementNode{
    private final List<Node> variables;

    InputNode(){
        variables = null;
    }

    InputNode(List<Node> input){
        variables = input;
    };

    List<Node> getInputList() {
        return variables;
    }

    public String toString(){
        StringBuilder result = new StringBuilder();
        Node node;

        for (int i = 0; i < Objects.requireNonNull(variables).size() - 1; i++) {
            node = variables.get(i);
            result.append(node).append(" , ");
        }

        result.append(variables.get(variables.size() - 1));

        return "Input " + result;
    }
}
