import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIPlayer extends Player {
    private static final int WIN_SCORE = 10;
    private static final int LOSE_SCORE = -10;
    private static final int DRAW_SCORE = 0;
    private final Difficulty difficulty;
    private final Random random = new Random();

    public AIPlayer(Symbol symbol, Difficulty difficulty) {
        super(symbol);
        this.difficulty = difficulty;
    }

    @Override
    public Move getMove(Board board) {
        switch (difficulty) {
            case EASY:
                return getEasyMove(board);
            case MEDIUM:
                return getMediumMove(board);
            case HARD:
                return findBestMove(board);
            default:
                return findBestMove(board);
        }
    }

    private Move getEasyMove(Board board) {
        // 70% random moves, 30% strategic moves
        if (random.nextDouble() < 0.7) {
            return getRandomMove(board);
        }
        return findBestMove(board);
    }

    private Move getMediumMove(Board board) {
        // 30% random moves, 70% strategic moves
        if (random.nextDouble() < 0.3) {
            return getRandomMove(board);
        }
        return findBestMove(board);
    }

    private Move getRandomMove(Board board) {
        List<Move> availableMoves = new ArrayList<>();
        char[][] grid = board.getGrid();
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (grid[i][j] == Symbol.EMPTY.getValue()) {
                    availableMoves.add(new Move(i, j));
                }
            }
        }
        
        if (!availableMoves.isEmpty()) {
            return availableMoves.get(random.nextInt(availableMoves.size()));
        }
        return null;
    }

    private Move findBestMove(Board board) {
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = null;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        char[][] grid = board.getGrid();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (grid[i][j] == Symbol.EMPTY.getValue()) {
                    // Try this move
                    grid[i][j] = symbol.getValue();
                    
                    // Calculate score for this move
                    int score = minimax(grid, 0, false, alpha, beta, getOpponentSymbol());
                    
                    // Undo the move
                    grid[i][j] = Symbol.EMPTY.getValue();

                    // Update best score
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new Move(i, j);
                    }
                    
                    alpha = Math.max(alpha, bestScore);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
        }
        return bestMove;
    }

    private int minimax(char[][] grid, int depth, boolean isMaximizing, 
                       int alpha, int beta, char opponentSymbol) {
        // Check terminal states
        int score = evaluateBoard(grid);
        if (score == WIN_SCORE) return WIN_SCORE - depth;  // Prefer winning sooner
        if (score == LOSE_SCORE) return LOSE_SCORE + depth;  // Prefer losing later
        if (isBoardFull(grid)) return DRAW_SCORE;

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (grid[i][j] == Symbol.EMPTY.getValue()) {
                        grid[i][j] = symbol.getValue();
                        int currentScore = minimax(grid, depth + 1, false, alpha, beta, opponentSymbol);
                        grid[i][j] = Symbol.EMPTY.getValue();
                        bestScore = Math.max(bestScore, currentScore);
                        alpha = Math.max(alpha, bestScore);
                        if (beta <= alpha) {
                            return bestScore;  // Beta cut-off
                        }
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (grid[i][j] == Symbol.EMPTY.getValue()) {
                        grid[i][j] = opponentSymbol;
                        int currentScore = minimax(grid, depth + 1, true, alpha, beta, opponentSymbol);
                        grid[i][j] = Symbol.EMPTY.getValue();
                        bestScore = Math.min(bestScore, currentScore);
                        beta = Math.min(beta, bestScore);
                        if (beta <= alpha) {
                            return bestScore;  // Alpha cut-off
                        }
                    }
                }
            }
            return bestScore;
        }
    }

    private int evaluateBoard(char[][] grid) {
        // Check rows
        for (int row = 0; row < 3; row++) {
            if (grid[row][0] != Symbol.EMPTY.getValue() &&
                grid[row][0] == grid[row][1] &&
                grid[row][0] == grid[row][2]) {
                return (grid[row][0] == symbol.getValue()) ? WIN_SCORE : LOSE_SCORE;
            }
        }

        // Check columns
        for (int col = 0; col < 3; col++) {
            if (grid[0][col] != Symbol.EMPTY.getValue() &&
                grid[0][col] == grid[1][col] &&
                grid[0][col] == grid[2][col]) {
                return (grid[0][col] == symbol.getValue()) ? WIN_SCORE : LOSE_SCORE;
            }
        }

        // Check diagonals
        if (grid[0][0] != Symbol.EMPTY.getValue() &&
            grid[0][0] == grid[1][1] &&
            grid[0][0] == grid[2][2]) {
            return (grid[0][0] == symbol.getValue()) ? WIN_SCORE : LOSE_SCORE;
        }

        if (grid[0][2] != Symbol.EMPTY.getValue() &&
            grid[0][2] == grid[1][1] &&
            grid[0][2] == grid[2][0]) {
            return (grid[0][2] == symbol.getValue()) ? WIN_SCORE : LOSE_SCORE;
        }

        return 0;  // No winner yet
    }

    private boolean isBoardFull(char[][] grid) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (grid[i][j] == Symbol.EMPTY.getValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    private char getOpponentSymbol() {
        return (symbol == Symbol.X) ? Symbol.O.getValue() : Symbol.X.getValue();
    }
}