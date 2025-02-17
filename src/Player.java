public abstract class Player {
    protected final Symbol symbol;

    protected Player(Symbol symbol) {
        this.symbol = symbol;
    }

    public abstract Move getMove(Board board);

    public char getSymbol() {
        return symbol.getValue();
    }
}