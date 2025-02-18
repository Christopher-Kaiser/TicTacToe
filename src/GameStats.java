public class GameStats {
    private int playerWins;
    private int aiWins;
    private int draws;
    private int gamesPlayed;

    public void updateStats(GameState state, char winner) {
        gamesPlayed++;
        if (state == GameState.WINNER) {
            if (winner == Symbol.X.getValue()) {
                playerWins++;
            } else {
                aiWins++;
            }
        } else if (state == GameState.DRAW) {
            draws++;
        }
    }

    public String getStatsDisplay() {
        return String.format("Games: %d | Player X: %d | Player O: %d | Draws: %d", 
            gamesPlayed, playerWins, aiWins, draws);
    }
}