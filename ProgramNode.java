import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProgramNode {
    private final List<Node> statements;

    public ProgramNode() {
        statements = new ArrayList<>();
    }

    void makeStatementBlock(Node input)
    {
        statements.add(input);
    }

    public String toString(){
        StringBuilder result = new StringBuilder();

        if(!statements.isEmpty()) {
            for(int i = 0; i < statements.size() - 1; i++)
            {
                result.append(statements.get(i).toString()).append("\n");
            }

            result.append(statements.get(statements.size() - 1));

            return result.toString();
        }
        else {
            return "There are no statements to return!!!";
        }
    }
}
