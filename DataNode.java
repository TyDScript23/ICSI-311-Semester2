import java.util.List;
import java.util.Objects;

public class DataNode extends StatementNode{
    private final List<Node> data;

    DataNode(){
        data = null;
    }

    DataNode(List<Node> input){
        data = input;
    };

    List<Node> getDataList() {
        return data;
    }

    public String toString(){
        StringBuilder result = new StringBuilder();
        Node datapoint;

        for (int i = 0; i < Objects.requireNonNull(data).size() - 1; i++) {
            datapoint = data.get(i);
            result.append(datapoint).append(" , ");
        }

        result.append(data.get(data.size() - 1));

        return "DATA " + result;
    }
}
