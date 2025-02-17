public class HumanPlayer extends Player {
    public HumanPlayer(Symbol symbol) {
        super(symbol);
    }

    @Override
    public Move getMove(Board board) {
        // This will be triggered by GUI events
        return null;
    }
}