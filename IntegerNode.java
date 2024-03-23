public class IntegerNode extends Node{
    private final int number;

    IntegerNode() {
        number = 0;
    }

    IntegerNode(int input) {
        number = input;
    }

    public int getNumber() {
        return number;
    }

    public String toString(){
        return "" + number;
    }
}
