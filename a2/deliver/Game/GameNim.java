import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class GameNim extends Game{
    int[] take = new int[]{1,2,3};

    int WinningScore = 10;
    int LosingScore = -10;
    int NeutralScore = 0;

    public GameNim() {
        currentState = new StateNim();
    }
    /*
     The function denote cur player is win or not, if there is left only one coin, then the current player lose.
     */
    public boolean isWinState(State state){
        StateNim tstate = (StateNim) state;

        int mark = ((StateNim) state).leftCoin;
        if (mark == 0) {
            return true;
        }
        return false;
    }

    // no coin to take
    public boolean isStuckState(State state){
        if (isWinState(state))
            return false;

        StateNim tstate = (StateNim) state;

        if (tstate.leftCoin >= 0 ) {
            return false;
        }
        return true;
    }

    public Set<State> getSuccessors(State state){
        if(isWinState(state) || isStuckState(state)) {
            return null;
        }
        Set<State> successors = new HashSet<State>();
        StateNim tstate = (StateNim) state;

        StateNim successor_state;

        int mark = ((StateNim) state).leftCoin;

        for (int i = 0; i < take.length; i++) {
            if (mark - take[i] >= 0) {
                successor_state = new StateNim(tstate);
                successor_state.leftCoin = mark - take[i];
                successor_state.player = (state.player==0 ? 1 : 0);

                successors.add(successor_state);
            }
        }
        return successors;
    }

    public double eval(State state){
        if(isWinState(state)) {
            //player who made last move
            int previous_player = (state.player==0 ? 1 : 0);

            if (previous_player == 1) //human wins
                return WinningScore;
            else
                return LosingScore;
        }
        return NeutralScore;
    }

    public static void main(String[] args) throws Exception {
        Game game = new GameNim();
        Search search = new Search(game);
        int depth = 13;

        //needed to get human's move
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        flag:
        while (true) {
            StateNim nextState = null;

            switch ( game.currentState.player ) {
                case 1: //Human

                    //get human's move
                    System.out.print("Enter your *valid* move> ");
                    int pos = Integer.parseInt( in.readLine() );
                    if (pos > 3 || pos <= 0 || pos > ((StateNim) game.currentState).leftCoin) {
                        System.out.print("Please choose 1 2 or 3 coins. And can't over the left coins.\n");
                         continue flag;
                    }

                    nextState = new StateNim((StateNim) game.currentState);
                    nextState.player = 1;
                    nextState.leftCoin =((StateNim) game.currentState).leftCoin - pos;
                    System.out.println("Human: \n" + pos);
                    System.out.println("Coin left:" + nextState.leftCoin);
                    break;

                case 0: //Computer

                    nextState = (StateNim) search.bestSuccessorState(depth);
                    nextState.player = 0;
                    int dif = ((StateNim) game.currentState).leftCoin - nextState.leftCoin;
                    System.out.println("Computer: \n" + dif);
                    System.out.println("Coin left:" + nextState.leftCoin);
                    break;
            }

            game.currentState = nextState;
            //change player
            game.currentState.player = (game.currentState.player==0 ? 1 : 0);

            //Who wins?
            if ( game.isWinState(game.currentState) ) {

                if (game.currentState.player == 0) //i.e. last move was by the computer
                    System.out.println("Computer wins!");
                else
                    System.out.println("You win!");
                break;
            }

            if ( game.isStuckState(game.currentState) ) {
                System.out.println("Cat's game!");
                break;
            }
        }
    }
}
