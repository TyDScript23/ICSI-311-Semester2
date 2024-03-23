import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FunctionNode extends Node{
    private final List<Node> nodelist;
    String functionname;

    FunctionNode(){
        nodelist = new ArrayList<>();
        functionname = "";
    }

    FunctionNode(String name)
    {
        functionname = name;
        nodelist = new ArrayList<>();
    }

    FunctionNode(String name, List<Node> list)
    {
        functionname = name;
        nodelist = list;
    }

    String getName(){
        return functionname;
    }

    List<Node> getNodelist(){
        return nodelist;
    }

    public String toString(){

        if(!nodelist.isEmpty())
        {
            StringBuilder result = new StringBuilder();
            Node node;

            for (int i = 0; i < Objects.requireNonNull(nodelist).size() - 1; i++) {
                node = nodelist.get(i);
                result.append(node).append(" , ");
            }

            result.append(nodelist.get(nodelist.size() - 1));

            return "FUNCTION " + functionname + " (" + result + ") ";
        }
        else{
            return "FUNCTION " + functionname + "() ";
        }
    }
}
