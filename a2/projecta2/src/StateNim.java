public class StateNim extends State{
    public int leftCoin;

    public StateNim() {
        this.leftCoin = 13;
        this.player = 1;
    }

    public StateNim(int initalNum) {
        leftCoin = initalNum;
        player = 1;
    }

    public StateNim(StateNim state) {
        this.leftCoin = state.leftCoin;
        player = state.player;
    }

    public String toString() {
        return  String.valueOf(leftCoin);
    }
}
