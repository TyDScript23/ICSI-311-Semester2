public class GoSubNode extends StatementNode{
    String process;

    GoSubNode(){
        process = "";
    }

    GoSubNode(String p){
        process = p;
    }

    String getProcess(){
        return process;
    }

    public String toString(){
        return "GOSUB " + process;
    }
}
