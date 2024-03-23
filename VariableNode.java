public class VariableNode extends StatementNode{
    private final String name;

    VariableNode() {
        name = "";
    }

    VariableNode(String input) {
        name = input;
    }

    String getName() {
        return name;
    }

    public String toString(){
        return name;
    }
}
