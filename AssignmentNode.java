public class AssignmentNode extends StatementNode{
    private final VariableNode variable;
    private Node value;

    AssignmentNode(){
        variable = new VariableNode();
    }

    AssignmentNode(VariableNode var, Node assign)
    {
        variable = var;
        value = assign;
    }

    VariableNode getVariable() {
        return variable;
    }

    Node getAssignedValue() {
        return value;
    }

    public String toString(){
        return variable + " = " + value;
    }
}
