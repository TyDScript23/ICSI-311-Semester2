import java.util.Optional;

public class MathOpNode extends Node{
    private Node left;
    private final Optional<Node> right;
    Operations operation;

    enum Operations{ADD,SUBTRACT,MULTIPLY,DIVIDE}

    MathOpNode (Node l, Operations op, Optional<Node> r) {
        left = l;
        operation = op;
        right = r;
    }

    MathOpNode (Operations op, Optional<Node> r)
    {
        operation = op;
        right = r;
    }

    public String toString() {
        return right.map(node -> switch (operation) {
            case ADD -> "(" + left + " + " + node + ")";
            case SUBTRACT -> "(" + left + " - " + node + ")";
            case MULTIPLY -> "(" + left + " * " + node + ")";
            case DIVIDE -> "(" + left + " / " + node + ")";
        }).orElseGet(() -> switch (operation) {
            case ADD -> left + " + ";
            case SUBTRACT -> left + " - ";
            case MULTIPLY -> left + " * ";
            case DIVIDE -> left + " / ";
        });
    }
}
