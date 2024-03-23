public class StringNode extends StatementNode{

    private final String value;

    StringNode() {
        value = "";
    }

    StringNode(String input)
    {
        value = input;
    }

    public String toString()
    {
        return value;
    }

    String getValue(){
        return value;
    }
}
