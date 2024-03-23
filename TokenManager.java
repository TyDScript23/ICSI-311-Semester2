import java.util.LinkedList;
import java.util.Optional;

public class TokenManager {
    private final LinkedList<Token> tokenllist;

    TokenManager(LinkedList<Token> lltok) {
        tokenllist = lltok;
    }

    /*
    If we aren't past the end of the token list, we should look "j"
    units ahead and return a Nullable Optional if we find something. If we
    don't then we should return an empty Optional
     */
    Optional<Token> Peek(int j) {
        if(tokenllist.get(j) != null)
        {
            return Optional.ofNullable(tokenllist.get(j));
        }
        else
        {
            return Optional.empty();
        }
    }

    //if the list is not empty we should return "true"

    boolean MoreTokens() {

        boolean isMore;
        isMore = !tokenllist.isEmpty();

        return isMore;

    }

    /*
    Looks at the head of our LinkedList and compares the TokenType to
    what was passed in. If they are the same, remove the token from the list
    and return it. Otherwise, return an empty Optional
     */

    Optional<Token> MatchAndRemove(Token.TokenType t)
    {
        if(!tokenllist.isEmpty())
        {
            Token first = tokenllist.getFirst();

            if (first.getType() == t)
            {
                tokenllist.remove(first);
                return Optional.of(first);
            }
            else {
                return Optional.empty();
            }
        }
        else {
            return Optional.empty();
        }
    }

    Token.TokenType getType(int index){
        return tokenllist.get(index).getType();
    }

    void remove(int index){
        tokenllist.remove(index);
    }

   LinkedList<Token> getList(){
        return tokenllist;
   }

    public String toString(){
        return tokenllist.toString();
    }
}