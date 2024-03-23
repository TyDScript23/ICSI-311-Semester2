import java.util.*;

public class Parser {
    private final TokenManager tokenman;
    private final ProgramNode pnode;
    private boolean forStatementPresent = false;
    private boolean whileStatementPresent = false;

    Parser(LinkedList<Token> lltok) throws Exception{
        tokenman= new TokenManager(lltok);
        pnode = Parse();
    }

    /*
    While TokenManager contains more tokens this function will loop
    attempting to find more expressions with separators in between them.
    It then will return a ProgramNode.
     */

    public ProgramNode Parse() throws Exception{
        ProgramNode program = new ProgramNode();

        AcceptSeparators();

        while (tokenman.MoreTokens()) {
            Optional<StatementsNode> line = Statements();

            line.ifPresent(program::makeStatementBlock);
        }

        return program;
    }

    /*
    Accepts any number of separators (ENDOFLINE) and returns true
    if it finds at least one
     */

    boolean AcceptSeparators() {
        boolean isPresent = false;

        int i = 0;
        int size = tokenman.getList().size();



        while(i < size){
            if(tokenman.getType(i) == Token.TokenType.ENDOFLINE)
            {
                isPresent = true;
                tokenman.remove(i);
                size--;
            }
            else{
                i++;
            }
        }

        return isPresent;
    }

    /*
    This method accepts statements and stores them until no more are found and returns
    a node that stores the list of statements
     */
    Optional<StatementsNode> Statements() throws Exception {
        StatementsNode statementlist = new StatementsNode();
        StatementNode statement = Statement();

        while(statement != null)
        {
            statementlist.addStatement(statement);
            statement = Statement();
        }

        return Optional.of(statementlist);
    }

    /*
    This function returns a single statement which is either a print or assignment statement
     */

    StatementNode Statement() throws Exception {

        Optional<Token> t;

        t = tokenman.MatchAndRemove(Token.TokenType.NUMBER);

        if(t.isPresent())
        {
            throw new Exception("A lone number was encountered!");
        }

        t = tokenman.MatchAndRemove(Token.TokenType.STRINGLITERAL);

        if(t.isPresent())
        {
            throw new Exception("A lone string was encountered!");
        }

        t = tokenman.MatchAndRemove(Token.TokenType.LEFTPARENTHESIS);

        if(t.isPresent())
        {
            throw new Exception("Expressions must be wrapped in a statement!");
        }

        t = tokenman.MatchAndRemove(Token.TokenType.THEN);

        if(t.isPresent())
        {
            throw new Exception("THEN statements cannot occur by themselves!");
        }

        t = tokenman.MatchAndRemove(Token.TokenType.TO);

        if(t.isPresent())
        {
            throw new Exception("TO statements cannot occur by themselves!");
        }

        t = tokenman.MatchAndRemove(Token.TokenType.STEP);

        if(t.isPresent())
        {
            throw new Exception("STEP statements cannot occur by themselves!");
        }

        if(!forStatementPresent)
        {
            t = tokenman.MatchAndRemove(Token.TokenType.NEXT);

            if(t.isPresent())
            {
                throw new Exception("NEXT statements cannot occur by themselves!");
            }
        }

        LabeledStatementNode label = LabelStatement();

        if(label != null)
        {
            return label;
        }

        PrintNode prints = PrintStatement();

        if(prints != null)
        {
            return prints;
        }

        AssignmentNode assigns = Assignment();

        if(assigns != null)
        {
            return assigns;
        }

        ReadNode read = ReadStatement();

        if(read != null)
        {
            return read;
        }

        DataNode data = DataStatement();

        if(data != null)
        {
            return data;
        }
        
        InputNode input = InputStatement();

        if(input != null)
        {
            return input;
        }

        GoSubNode gosub = GoSubStatement();

        if(gosub != null)
        {
            return gosub;
        }

        ForNode forstat = ForStatement();

        if(forstat != null)
        {
            return forstat;
        }

        EndNode end = EndStatement();

        if(end != null){

            return end;
        }

        IfNode ifstat = IfStatement();

        if(ifstat != null){

            return ifstat;
        }

        return WhileStatement();
    }

    LabeledStatementNode LabelStatement() throws Exception {
        Optional<StatementsNode> statements;
        Optional<Token> t;
        String labelname;

        if(!whileStatementPresent)
        {
            t = tokenman.MatchAndRemove(Token.TokenType.LABEL);

            if(t.isPresent())
            {
                labelname = t.get().getValue();
                statements = Statements();

                if(statements.isPresent())
                {
                    t = tokenman.MatchAndRemove(Token.TokenType.RETURN);

                    if(t.isPresent()){
                        return new LabeledStatementNode(labelname,statements.get().getStatementList());
                    }
                    else{
                        throw new Exception("Your method is missing a return statement!");
                    }
                }
                else{
                    throw new Exception("A method label must contain statements!");
                }
            }
            else{
                return null;
            }
        }
        else{
            return null;
        }
    }

    /*
    This function generates a PrintNode as soon as it finds a PRINT token and PrintNode
    will store the list of arguments found after
     */
    PrintNode PrintStatement() throws Exception {
        Optional<Token> t;

        t = tokenman.MatchAndRemove(Token.TokenType.PRINT);

        if(t.isPresent())
        {
            return new PrintNode(PrintList());
        }
        else {
            return null;
        }
    }

    /*
    This function will parse a list of expressions, which are comma separated,
    found after a print statement. It will first call expression to parse the first
    expression, and then it will check for a comma. If found, it will parse the next expression
    and check for another comma afterward. This process is repeated until a comma is not encountered
    , in which case a list containing the arguments for the print values will be returned.
     */

    List<Node> PrintList() throws Exception {

        List<Node> prints = new ArrayList<>();

        Optional<Node> next = Expression();

        if(next.isPresent())
        {
            prints.add(next.get());

            Optional<Token> t;

            t = tokenman.MatchAndRemove(Token.TokenType.COMMA);

            while(t.isPresent())
            {
                next = Expression();

                if(next.isPresent()) {
                    prints.add(next.get());
                }
                else{
                    throw new Exception("A comma must be followed by another printable argument!!!");
                }

                t = tokenman.MatchAndRemove(Token.TokenType.COMMA);
            }

            return prints;
        }
       else {
           throw new Exception("PRINT statements must have arguments following them!!!");
        }
    }

    /*
    This function will parse an assignment statement. It first will look for a word
    (to represent the variable) and , if found, will create a Variable, assigning the word
    as the variables name in VariableNode. It then will look for an equal sign (the assignment)
    and ,if found, in will parse the expression found after. If found, an AssignmentNode will be
    created, storing the variable name and the expression value, and returned.
     */
    AssignmentNode Assignment() throws Exception {
        VariableNode variable;
        Optional<Node> expression;
        Optional<Token> t;

        t = tokenman.MatchAndRemove(Token.TokenType.WORD);

        if(t.isPresent())
        {
            variable = new VariableNode(t.get().getValue());

            t = tokenman.MatchAndRemove(Token.TokenType.EQUALS);

            if(t.isPresent())
            {
                expression = Expression();

                if(expression.isPresent())
                {
                    return new AssignmentNode(variable,expression.get());
                }
                else{
                    throw new Exception("An equals sign must be followed by an expression!!!");
                }
            }
            else {
                throw new Exception("A lone word was encountered!!!");
            }
        }
        else{
            return null;
        }
    }

    /*
    This function generates a ReadNode as soon as it finds a READ token and ReadNode
    will store the list of arguments found after
     */

   ReadNode ReadStatement() throws Exception {
        Optional<Token> t;

        t = tokenman.MatchAndRemove(Token.TokenType.READ);

        if(t.isPresent())
        {
            return new ReadNode(ReadList());
        }
        else {
            return null;
        }
    }

    /*
    This function will parse a list of expressions, which are comma separated,
    found after a read statement. It will first call expression to parse the first
    expression, and then it will check for a comma. If found, it will parse the next expression
    and check for another comma afterward. This process is repeated until a comma is not encountered
    , in which case a list containing the arguments for the read values will be returned.
     */

    List<Node> ReadList() throws Exception {
        List<Node> reads = new ArrayList<>();
        Optional<Token> t;
        boolean isPresent;

        isPresent = tokenman.MoreTokens();

        if(isPresent) {
            t = tokenman.Peek(0);

            if (t.isPresent() && (t.get().getType() == Token.TokenType.WORD)) {

                Optional<Node> next = Expression();

                if (next.isPresent()) {
                    reads.add(next.get());

                    t = tokenman.MatchAndRemove(Token.TokenType.COMMA);

                    while (t.isPresent()) {
                        t = tokenman.Peek(0);

                        if (t.isPresent() && t.get().getType() == Token.TokenType.COMMA) {
                            throw new Exception("Commas must be followed by another readable argument!!!");
                        } else {

                            if (t.isPresent() && t.get().getType() == Token.TokenType.WORD) {
                                next = Expression();

                                next.ifPresent(reads::add);
                                t = tokenman.MatchAndRemove(Token.TokenType.COMMA);
                            } else {
                                throw new Exception("READ lists must only contain variables!!!");
                            }
                        }
                    }

                    return reads;
                } else {
                    throw new Exception("READ statements must have arguments following them!!!");
                }
            } else {
                throw new Exception("READ lists must only contain variables!!!");
            }
        }
        else{
            throw new Exception("READ statements must have arguments following them!!!");
        }
    }

    /*
    This function generates a DataNode as soon as it finds a DATA token and DataNode
    will store the list of arguments found after
     */

    DataNode DataStatement() throws Exception {
        Optional<Token> t;

        t = tokenman.MatchAndRemove(Token.TokenType.DATA);

        if(t.isPresent())
        {
            return new DataNode(DataList());
        }
        else {
            return null;
        }
    }

    /*
    This function will parse a list of expressions, which are comma separated,
    found after a data statement. It will first call expression to parse the first
    expression, and then it will check for a comma. If found, it will parse the next expression
    and check for another comma afterward. This process is repeated until a comma is not encountered
    , in which case a list containing the arguments for the data values will be returned.
     */

    List<Node> DataList() throws Exception {
        List<Node> data = new ArrayList<>();
        Optional<Token> t;
        boolean isPresent;

        isPresent = tokenman.MoreTokens();

        if(isPresent) {

            t = tokenman.Peek(0);

            if (t.isPresent() && (t.get().getType() == Token.TokenType.NUMBER || t.get().getType() == Token.TokenType.STRINGLITERAL)) {

                Optional<Node> next = Expression();

                if (next.isPresent()) {
                    data.add(next.get());

                    t = tokenman.MatchAndRemove(Token.TokenType.COMMA);

                    while (t.isPresent()) {

                        t = tokenman.Peek(0);

                        if (t.isPresent() && (t.get().getType() == Token.TokenType.COMMA)) {
                            throw new Exception("Commas must be followed by another readable argument!!!");
                        } else {
                            if (t.isPresent() && (t.get().getType() == Token.TokenType.NUMBER || t.get().getType() == Token.TokenType.STRINGLITERAL)) {

                                next = Expression();

                                next.ifPresent(data::add);
                                t = tokenman.MatchAndRemove(Token.TokenType.COMMA);
                            } else {
                                throw new Exception("DATA lists must only contain string literals, integers, and floats!!!");
                            }
                        }
                    }

                    return data;
                } else {
                    throw new Exception("DATA statements must have arguments following them!!!");
                }
            } else {
                throw new Exception("DATA lists must only contain string literals, integers, and floats!!!");
            }
        }
        else {
            throw new Exception("DATA statements must have arguments following them!!!");
        }
    }

    /*
    This function generates a InputNode as soon as it finds a INPUT token and InputNode
    will store the list of arguments found after
     */

    InputNode InputStatement() throws Exception {
        Optional<Token> t;

        t = tokenman.MatchAndRemove(Token.TokenType.INPUT);

        if(t.isPresent())
        {
            return new InputNode(InputList());
        }
        else {
            return null;
        }
    }

    /*
    This function will parse a list of expressions, which are comma separated,
    found after a input statement. It will first call expression to parse the first
    expression, and then it will check for a comma. If found, it will parse the next expression
    and check for another comma afterward. This process is repeated until a comma is not encountered
    , in which case a list containing the arguments for the input values will be returned.
     */

    List<Node> InputList() throws Exception {
        List<Node> inputs = new ArrayList<>();
        Optional<Token> t;
        boolean isPresent;

        isPresent = tokenman.MoreTokens();

        if(isPresent) {

            t = tokenman.Peek(0);

            if (t.isPresent() && (t.get().getType() == Token.TokenType.STRINGLITERAL || t.get().getType() == Token.TokenType.WORD)) {

                Optional<Node> next = Expression();

                if (next.isPresent()) {
                    inputs.add(next.get());

                    t = tokenman.MatchAndRemove(Token.TokenType.COMMA);

                    while (t.isPresent()) {

                        t = tokenman.Peek(0);

                        if (t.isPresent() && (t.get().getType() == Token.TokenType.COMMA)) {
                            throw new Exception("Commas must be followed by another readable argument!!!");
                        } else {
                            if (t.isPresent() && (t.get().getType() == Token.TokenType.STRINGLITERAL || t.get().getType() == Token.TokenType.WORD)) {

                                next = Expression();

                                next.ifPresent(inputs::add);
                                t = tokenman.MatchAndRemove(Token.TokenType.COMMA);
                            } else {
                                throw new Exception("INPUT lists must only contain variables and constant strings!!!");
                            }
                        }
                    }

                    return inputs;
                } else {
                    throw new Exception("INPUT statements must have arguments following them!!!");
                }
            } else {
                throw new Exception("INPUT lists must only contain variables and constant strings!!!");
            }
        }
        else {
            throw new Exception("INPUT statements must have arguments following them!!!");
        }
    }

    GoSubNode GoSubStatement() throws Exception {
        Optional<Token> t;

        t = tokenman.MatchAndRemove(Token.TokenType.GOSUB);

        if(t.isPresent())
        {
            t = tokenman.MatchAndRemove(Token.TokenType.WORD);

            if(t.isPresent())
            {
                return new GoSubNode(t.get().getValue());
            }
            else {
                throw new Exception("GOSUB must be followed by a process name!");
            }
        }
        else{
            return null;
        }
    }

    ForNode ForStatement() throws Exception {
        Optional<Token> t;
        String variable;
        String startcount;
        Optional<Node> endcount;
        String step;
        Optional<StatementsNode> contents;

        t = tokenman.MatchAndRemove(Token.TokenType.FOR);

        if(t.isPresent())
        {
            forStatementPresent = true;
            t = tokenman.MatchAndRemove(Token.TokenType.WORD);

            if(t.isPresent()) {
                variable = t.get().getValue();
                t = tokenman.MatchAndRemove(Token.TokenType.EQUALS);

                if(t.isPresent())
                {
                    t = tokenman.MatchAndRemove(Token.TokenType.NUMBER);

                    if(t.isPresent()){
                        startcount = t.get().getValue();
                        t = tokenman.MatchAndRemove(Token.TokenType.TO);

                        if(t.isPresent())
                        {
                            t = tokenman.Peek(0);

                            if(t.isPresent() && (t.get().getType() == Token.TokenType.NUMBER || t.get().getType() == Token.TokenType.WORD))
                            {
                                endcount = Expression();

                                if(endcount.isPresent())
                                {
                                    t = tokenman.MatchAndRemove(Token.TokenType.STEP);

                                    if(t.isPresent())
                                    {
                                        t = tokenman.MatchAndRemove(Token.TokenType.NUMBER);

                                        if(t.isPresent())
                                        {
                                            step = t.get().getValue();

                                            contents = Statements();

                                            if(contents.isPresent())
                                            {
                                                t = tokenman.MatchAndRemove(Token.TokenType.NEXT);

                                                if(t.isPresent())
                                                {
                                                    t = tokenman.MatchAndRemove(Token.TokenType.WORD);

                                                    if(t.isPresent())
                                                    {
                                                        if(Objects.equals(t.get().getValue(), variable))
                                                        {
                                                            forStatementPresent = false;
                                                            return new ForNode(variable,startcount,endcount.get(),step,contents.get().getStatementList());
                                                        }
                                                        else{
                                                            throw new Exception("The counting variable used in a FOR statement must match in the NEXT statement!");
                                                        }
                                                    }
                                                    else {
                                                        throw new Exception("NEXT statement must contain a variable name!");
                                                    }
                                                }
                                                else{
                                                    throw new Exception("A FOR statement must have a NEXT statement at its end!");
                                                }
                                            }
                                            else {
                                                throw new Exception("A FOR statement must have contents!");
                                            }
                                        }
                                        else{
                                            throw new Exception("STEP in FOR statement must be followed by a number!");
                                        }
                                    }
                                    else{
                                        contents = Statements();

                                        if(contents.isPresent())
                                        {
                                            t = tokenman.MatchAndRemove(Token.TokenType.NEXT);

                                            if(t.isPresent())
                                            {
                                                t = tokenman.MatchAndRemove(Token.TokenType.WORD);

                                                if(t.isPresent())
                                                {
                                                    if(Objects.equals(t.get().getValue(), variable))
                                                    {
                                                        forStatementPresent = false;
                                                        return new ForNode(variable,startcount,endcount.get(),contents.get().getStatementList());
                                                    }
                                                    else {
                                                        throw new Exception("The counting variable used in a FOR statement must match in the NEXT statement!");
                                                    }
                                                }
                                                else {
                                                    throw new Exception("NEXT statement must contain a variable name!");
                                                }
                                            }
                                            else{
                                                throw new Exception("A FOR statement must have a NEXT statement at its end!");
                                            }
                                        }
                                        else {
                                            throw new Exception("A FOR statement must have contents!");
                                        }
                                    }
                                }
                                else{
                                    throw new Exception("TO Statement must be followed by a variable or number!");
                                }
                            }
                            else{
                                throw new Exception("End bound must be number or integer variable!");
                            }
                        }
                        else{
                            throw new Exception("FOR statement does not contain a TO!");
                        }
                    }
                    else{
                        throw new Exception("An equals sign must be followed by an number or expression!");
                    }
                }
                else{
                    throw new Exception("The variables value in the FOR statement might not have been set!");
                }
            }
            else{
                throw new Exception("FOR keyword must be followed by an initialization expression! (ex. x = 1)");
            }
        }
        else{
            return null;
        }
    }

    EndNode EndStatement(){
        Optional<Token> t;

        t = tokenman.MatchAndRemove(Token.TokenType.END);

        if(t.isPresent())
        {
            return new EndNode();
        }
        else {
            return null;
        }
    }

    IfNode IfStatement() throws Exception {
        Optional<Token> t;
        Optional<Node> condition;
        StatementNode contents;

        t = tokenman.MatchAndRemove(Token.TokenType.IF);

        if(t.isPresent())
        {
            t = tokenman.MatchAndRemove(Token.TokenType.LEFTPARENTHESIS);

            if(t.isPresent()) {
                condition = ParseBoolean();

                if (condition.isPresent()) {
                    t = tokenman.MatchAndRemove(Token.TokenType.RIGHTPARENTHESIS);

                    if(t.isPresent())
                    {
                        t = tokenman.MatchAndRemove(Token.TokenType.THEN);

                        if(t.isPresent())
                        {
                            contents = Statement();

                            if(contents != null)
                            {
                                return new IfNode(condition.get(), contents);
                            }
                            else{
                                throw new Exception("IF statement must contain valid contents!");
                            }
                        }
                        else{
                            throw new Exception("IF must contain a THEN statement!");
                        }
                    }
                    else{
                        throw new Exception("Left parenthesis must have a matching right parenthesis!");
                    }
                }
                else{
                    throw new Exception("Parenthesis must contain an boolean expression!");
                }
            }
            else{
                throw new Exception("IF keyword must be followed by a boolean expression!");
            }
        }
        else{
            return null;
        }
    }

    WhileNode WhileStatement() throws Exception {
        Optional<Token> t;
        Optional<Token> s;
        Optional<Node> condition;
        Optional<StatementsNode> contents;
        String endLabel;

        t = tokenman.MatchAndRemove(Token.TokenType.WHILE);

        if(t.isPresent())
        {
            whileStatementPresent = true;
            t = tokenman.MatchAndRemove(Token.TokenType.LEFTPARENTHESIS);

            if(t.isPresent())
            {
                condition = ParseBoolean();

                if(condition.isPresent())
                {
                    t = tokenman.MatchAndRemove(Token.TokenType.RIGHTPARENTHESIS);

                    if(t.isPresent())
                    {
                        s = tokenman.MatchAndRemove(Token.TokenType.WORD);

                        if(s.isPresent())
                        {
                            endLabel = s.get().getValue();
                            contents = Statements();

                            if(contents.isPresent())
                            {
                                t = tokenman.MatchAndRemove(Token.TokenType.LABEL);

                                if(t.isPresent())
                                {
                                    endLabel += ":";

                                    if(endLabel.equals(t.get().getValue()))
                                    {
                                        whileStatementPresent = false;
                                        return new WhileNode(condition.get(), contents.get().getStatementList(), endLabel);
                                    }
                                    else{
                                        throw new Exception("Both references to the end label in the while loop must match!");
                                    }
                                }
                                else{
                                    throw new Exception("An end label was not provided!");
                                }
                            }
                            else{
                                throw new Exception("WHILE loop cannot be empty!");
                            }
                        }
                        else{
                            throw new Exception("You must declare an ending label before the while loop content!");
                        }
                    }
                    else{
                        throw new Exception("Left parenthesis does not have a matching right parenthesis!");
                    }
                }
                else{
                    throw new Exception("WHILE statement parenthesis must contain a boolean expression!");
                }
            }
            else{
                throw new Exception("WHILE statement must be followed by a boolean expression!");
            }
        }
        else{
            return null;
        }
    }

    Optional<Node> ParseBoolean() throws Exception {
        Optional<Token> t;
        Optional<Node> left = Expression();
        Token.TokenType operation;
        Optional<Node> right;



        t = tokenman.MatchAndRemove(Token.TokenType.LESSTHAN);
        if (t.isPresent() && left.isPresent()) {
            right = Expression();
            operation = t.get().getType();

            if(right.isPresent())
            {
                return Optional.of(new BooleanNode(left.get(), operation, right.get()));
            }
            else {
                throw new Exception("A boolean operator must be followed by an expression!");
            }
        }
        t = tokenman.MatchAndRemove(Token.TokenType.LESSTHANOREQUALTO);
        if (t.isPresent() && left.isPresent()) {
            right = Expression();
            operation = t.get().getType();
            if(right.isPresent())
            {
                return Optional.of(new BooleanNode(left.get(), operation, right.get()));
            }
            else {
                throw new Exception("A boolean operator must be followed by an expression!");
            }
        }
        t = tokenman.MatchAndRemove(Token.TokenType.GREATERTHAN);
        if (t.isPresent() && left.isPresent()) {
            right = Expression();
            operation = t.get().getType();

            if(right.isPresent())
            {
                return Optional.of(new BooleanNode(left.get(), operation, right.get()));
            }
            else {
                throw new Exception("A boolean operator must be followed by an expression!");
            }
        }
        t = tokenman.MatchAndRemove(Token.TokenType.GREATERTHANOREQUALTO);
        if (t.isPresent() && left.isPresent()) {
            right = Expression();
            operation = t.get().getType();

            if(right.isPresent())
            {
                return Optional.of(new BooleanNode(left.get(), operation, right.get()));
            }
            else {
                throw new Exception("A boolean operator must be followed by an expression!");
            }
        }
        t = tokenman.MatchAndRemove(Token.TokenType.NOTEQUALS);
        if (t.isPresent() && left.isPresent()) {
            right = Expression();
            operation = t.get().getType();

            if(right.isPresent())
            {
                return Optional.of(new BooleanNode(left.get(), operation, right.get()));
            }
            else {
                throw new Exception("A boolean operator must be followed by an expression!");
            }
        }
        t = tokenman.MatchAndRemove(Token.TokenType.EQUALS);
        if (t.isPresent() && left.isPresent()) {
            right = Expression();
            operation = t.get().getType();

            if(right.isPresent())
            {
                return Optional.of(new BooleanNode(left.get(), operation, right.get()));
            }
            else {
                throw new Exception("A boolean operator must be followed by an expression!");
            }
        }
        else{
            throw new Exception("We have encountered an unhandled boolean operator!");
        }
    }

    FunctionNode FunctionStatement() throws Exception{
        Optional<Token> t;
        String name;
        Optional<List<Node>> prints;

        t = tokenman.MatchAndRemove(Token.TokenType.FUNCTION);

        if(t.isPresent())
        {
            t = tokenman.MatchAndRemove(Token.TokenType.WORD);

            if(t.isPresent())
            {
                name = t.get().getValue();

                t = tokenman.MatchAndRemove(Token.TokenType.LEFTPARENTHESIS);

                if(t.isPresent())
                {
                    t = tokenman.MatchAndRemove(Token.TokenType.RIGHTPARENTHESIS);
                    if(t.isPresent()){
                        return new FunctionNode(name);
                    }
                    else{
                        prints = FunctionList();

                        if(prints.isPresent())
                        {
                            t = tokenman.MatchAndRemove(Token.TokenType.RIGHTPARENTHESIS);

                            if(t.isPresent())
                            {
                                return new FunctionNode(name,prints.get());
                            }
                            else {
                                throw new Exception("Left parenthesis does not have a matching right parenthesis!");
                            }
                        }
                        else{
                            throw new Exception("Valid arguments must be provided in a function!");
                        }
                    }
                }
                else{
                    throw new Exception("Function names must be followed by a complete list surrounded by parenthesis!");
                }
            }
            else{
                throw new Exception("FUNCTION statements must be followed by a function name!");
            }
        }
        else{
            return null;
        }
    }

    Optional<List<Node>> FunctionList() throws Exception {
        List<Node> inputs = new ArrayList<>();
        Optional<Token> t;
        boolean isPresent;

        isPresent = tokenman.MoreTokens();

        if(isPresent) {

            t = tokenman.Peek(0);

            if (t.isPresent()) {

                Optional<Node> next = Expression();

                if (next.isPresent()) {
                    inputs.add(next.get());

                    t = tokenman.MatchAndRemove(Token.TokenType.COMMA);

                    while (t.isPresent()) {

                        t = tokenman.Peek(0);

                        if (t.isPresent() && (t.get().getType() == Token.TokenType.COMMA)) {
                            throw new Exception("Commas must be followed by another readable argument!!!");
                        } else {
                            next = Expression();

                            next.ifPresent(inputs::add);
                            t = tokenman.MatchAndRemove(Token.TokenType.COMMA);
                        }
                    }

                    return Optional.of(inputs);
                } else {
                    throw new Exception("FUNCTION statements must have arguments following them!!!");
                }
            } else {
                throw new Exception("FUNCTION statements must have arguments following them!!!");
            }
        }
        else {
            throw new Exception("FUNCTION statements must have arguments following them!!!");
        }
    }

    /*
    Expression checks to see if the next token matches the form of a term
    and assigns it to the value of left. It then checks to see if there is an operation
    following it and set the operator accordingly:
    -   If a function statement is caught, return a FunctionNode
    -   If the next token is of type add, the operation is set to ADD
    -   If the next token is of type subtract, the operation is set to SUBTRACT
    -   If else let's just return the Term

    If an operator is found, the right term will be assigned the result of another expression and an OperationNode is
    created consisting of the left expression, operation, and the right expression
     */

    Optional<Node> Expression() throws Exception {
        Optional<Node> left = Term();
        MathOpNode.Operations operation;
        Optional<Node> right;
        Optional<Token> t;

        FunctionNode function = FunctionStatement();

        if(function != null){
            return Optional.of(function);
        }

        t = tokenman.MatchAndRemove(Token.TokenType.ADD);
        operation = MathOpNode.Operations.ADD;
        if(t.isEmpty() && left.isPresent()){
            t = tokenman.MatchAndRemove(Token.TokenType.SUBTRACT);
            operation = MathOpNode.Operations.SUBTRACT;
            if(t.isEmpty()){
                return left;
            }
        }

        if (left.isPresent())
        {
            right = Expression();

            if (right.isPresent())
            {
                return Optional.of(new MathOpNode(left.get(), operation, right));
            }
            else {
                return left;
            }
        }
        else {
            return left;
        }
    }

    /*
    Term checks to see if the next token matches the form of a factor
    and assigns it to the value of left. It then checks to see if there is an operation
    following it and set the operator accordingly:
    -   If the next token is of type multiply, the operation is set to MULTIPLY
    -   If the next token is of type divide, the operation is set to DIVIDE
    -   If else let's just return the Factor

    If an operator is found, the right term will be assigned the result of another Term and an OperationNode is
    created consisting of the left expression, operation, and the right expression
     */

    Optional<Node> Term() throws Exception {
        Optional<Node> left = Factor();
        Optional<Node> right;
        MathOpNode.Operations op;
        Optional<Token> t;

        t = tokenman.MatchAndRemove(Token.TokenType.MULTIPLY);
        op = MathOpNode.Operations.MULTIPLY;
        if (t.isEmpty() && left.isPresent()) {
            t = tokenman.MatchAndRemove(Token.TokenType.DIVIDE);
            op = MathOpNode.Operations.DIVIDE;
            if (t.isEmpty()) {
                return left;
            }
        }

        if (left.isPresent())
        {
            right = Term();

            if (right.isPresent()) {
                return Optional.of(new MathOpNode(left.get(), op, right));
            }
            else{
                return left;
            }
        }
        else {
            return left;
        }
    }

    /*
    Term checks to see if the next token matches the form of a number
    according to the following rules:
    -   If the number contains a decimal point a FloatNode is created and initialized with the value
    -   If the number does not contain a decimal point an IntegerNode is created and initialized with the value

    Then it checks for parenthesis in the following manner:
    -   If a right parenthesis is encountered without first encountering a left parenthesis, an exception is thrown
    -   When a left parenthesis is encountered, an Expression is parsed and it checks for a right parenthesis
            ** If no right parenthesis is found, an exception is thrown

    If any data is found to be not a number nor an operation, an exception is thrown for the unhandled data
     */

    Optional<Node> Factor() throws Exception {
        Optional<Node> left;
        Optional<Token> t;
        boolean hasDecimal = false;

        t = tokenman.MatchAndRemove(Token.TokenType.NUMBER);

        if(t.isPresent())
        {
            for(int i = 0; i < t.get().getValue().length(); i++)
            {
                if(t.get().getValue().charAt(i) == '.')
                    hasDecimal = true;
            }

            if(hasDecimal)
            {
                return Optional.of(new FloatNode(Float.parseFloat(t.get().getValue())));
            }
            else {
                return Optional.of(new IntegerNode(Integer.parseInt(t.get().getValue())));
            }
        }

        t = tokenman.MatchAndRemove(Token.TokenType.WORD);

        if (t.isPresent()){
            return Optional.of(new VariableNode(t.get().getValue()));
        }

        t = tokenman.MatchAndRemove(Token.TokenType.STRINGLITERAL);

        if(t.isPresent())
        {
            return Optional.of(new StringNode(t.get().getValue()));
        }
        else {
            t = tokenman.MatchAndRemove(Token.TokenType.RIGHTPARENTHESIS);

            if (t.isPresent()){
               throw new Exception("Right parenthesis does not have a matched left parenthesis!");
            }

            t = tokenman.MatchAndRemove(Token.TokenType.LEFTPARENTHESIS);

            if (t.isPresent()) {
                left = Expression();

                if(left.isPresent()) {
                    t = tokenman.MatchAndRemove(Token.TokenType.RIGHTPARENTHESIS);

                    if(t.isEmpty()) {
                        throw new Exception("Left parenthesis does not have a matching right parenthesis!");
                    }
                    return left;
                }
                else {
                    throw new Exception("Parenthesis does not contain any content!");
                }
            }
            else {
                return Optional.empty();
            }
        }
    }

    //prints out our created program node
    public String toString(){
        return pnode.toString();
    }
}
