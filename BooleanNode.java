public class BooleanNode extends StatementNode{
    private final Node left;
    private final Token.TokenType operation;
    private final Node right;

    BooleanNode(){
        left = null;
        operation = null;
        right = null;
    }

    BooleanNode(Node l, Token.TokenType op, Node r){
        left = l;
        operation = op;
        right = r;
    }

    Node getLeft(){
        return left;
    }

    Node getRight(){
        return right;
    }

    String getOperator(){
        assert operation != null;
        return operation.name();
    }

    public String toString(){

        if (operation == Token.TokenType.GREATERTHAN)
        {
            return left + " > " + right;
        }
        if (operation == Token.TokenType.GREATERTHANOREQUALTO)
        {
            return left + " >= " + right;
        }
        if (operation == Token.TokenType.LESSTHAN)
        {
            return left + " < " + right;
        }
        if (operation == Token.TokenType.LESSTHANOREQUALTO)
        {
            return left + " <= " + right;
        }
        if (operation == Token.TokenType.NOTEQUALS)
        {
            return left + " <> " + right;
        }
        if (operation == Token.TokenType.EQUALS)
        {
            return left + " = " + right;
        }
        else{
            try {
                throw new Exception("An unhandled boolean operator was found!");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
