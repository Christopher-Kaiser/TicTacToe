public class Game {
    private final Board board;
    private final Player playerX;
    private final Player playerO;
    private Player currentPlayer;
    private GameState state;

    public Game(Player playerX, Player playerO) {
        this.board = new Board();
        this.playerX = playerX;
        this.playerO = playerO;
        this.currentPlayer = playerX;
        this.state = GameState.IN_PROGRESS;
    }

    public void makeMove(int row, int col) {
        if (state != GameState.IN_PROGRESS || !board.isValidMove(row, col)) {
            return;
        }

        board.makeMove(row, col, currentPlayer.getSymbol());
        updateGameState();

        // Only switch players if the game is still in progress
        if (state == GameState.IN_PROGRESS) {
            switchPlayer();
        }
    }

    private void updateGameState() {
        if (board.hasWinner()) {
            state = GameState.WINNER;
        } else if (board.isFull()) {
            state = GameState.DRAW;
        }
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == playerX) ? playerO : playerX;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public GameState getState() {
        return state;
    }

    public Board getBoard() {
        return board;
    }
}