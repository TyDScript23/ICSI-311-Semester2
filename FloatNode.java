public class FloatNode extends Node{
    private final float number;

    FloatNode() {
        number = 0;
    }

    FloatNode(float input)
    {
        number = input;
    }

    public float getNumber() {
        return number;
    }

    public String toString(){
        return "" + number;
    }
}
