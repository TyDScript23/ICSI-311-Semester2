import java.util.ArrayList;
import java.util.List;

public class ForNode extends StatementNode{
    private final String startvariable;
    private final String startcount;
    private Node endcount;
    private final String step;
    private final boolean stepPresent;

    private final List<StatementNode> contents;

    ForNode(){
        startvariable = "";
        startcount = "";
        step = "";
        contents = new ArrayList<>();
        stepPresent = false;
    }

    ForNode(String sv, String sc, Node ec, String st, List<StatementNode> c)
    {
        startvariable = sv;
        startcount = sc;
        endcount = ec;
        step = st;
        contents = c;
        stepPresent = true;
    }

    ForNode(String sv, String sc, Node ec, List<StatementNode> c)
    {
        startvariable = sv;
        startcount = sc;
        endcount = ec;
        contents = c;
        step = "1";
        stepPresent = false;
    }

    String getStartVariable(){
        return startvariable;
    }

    String getStartCount(){
        return startcount;
    }

    String getEndCount(){
        return startcount;
    }

    String getStepValue(){
        return step;
    }

    public String toString(){

        StringBuilder inside = new StringBuilder();

        if(contents.get(0) != null)
        {
            inside.append("\t").append(contents.get(0));
        }

        for(int i = 1; i < contents.size(); i++)
        {
            inside.append("\n").append("\t").append(contents.get(i));
        }

        if(stepPresent)
        {
            return "FOR " + startvariable + " = " + startcount + " TO " + endcount + " STEP " + step + " \n" +
                    inside + " \n" + "NEXT " + startvariable;
        }
        else
        {
            return "FOR " + startvariable + " = " + startcount + " TO " + endcount + " \n" +
                    inside + " \n" + "NEXT " + startvariable;
        }
    }
}
