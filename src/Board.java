public class Board {
    private static final int SIZE = 3;
    private final char[][] grid;

    public Board() {
        grid = new char[SIZE][SIZE];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j] = Symbol.EMPTY.getValue();
            }
        }
    }

    public boolean isValidMove(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE && 
               grid[row][col] == Symbol.EMPTY.getValue();
    }

    public void makeMove(int row, int col, char symbol) {
        grid[row][col] = symbol;
    }

    public boolean hasWinner() {
        return checkRows() || checkColumns() || checkDiagonals();
    }

    private boolean checkRows() {
        for (int i = 0; i < SIZE; i++) {
            if (grid[i][0] != Symbol.EMPTY.getValue() && 
                grid[i][0] == grid[i][1] && grid[i][0] == grid[i][2]) {
                return true;
            }
        }
        return false;
    }

    private boolean checkColumns() {
        for (int j = 0; j < SIZE; j++) {
            if (grid[0][j] != Symbol.EMPTY.getValue() && 
                grid[0][j] == grid[1][j] && grid[0][j] == grid[2][j]) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDiagonals() {
        return (grid[0][0] != Symbol.EMPTY.getValue() && 
                grid[0][0] == grid[1][1] && grid[0][0] == grid[2][2]) ||
               (grid[0][2] != Symbol.EMPTY.getValue() && 
                grid[0][2] == grid[1][1] && grid[0][2] == grid[2][0]);
    }

    public boolean isFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j] == Symbol.EMPTY.getValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    public char[][] getGrid() {
        return grid;
    }
}