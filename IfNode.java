public class IfNode extends StatementNode{
    private final StatementNode content;
    private final Node booleanexpression;

    IfNode(){
        booleanexpression = null;
        content = null;
    }

    IfNode(Node boolexp, StatementNode statement)
    {
        content = statement;
        booleanexpression = boolexp;
    }

    public String toString(){
        return "IF (" + booleanexpression + ") THEN " + content;
    }
}
