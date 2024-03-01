import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToe {
    private static final int SIZE = 3;
    private static final char EMPTY = '-';
    private static final char PLAYER_X = 'X';
    private static final char PLAYER_O = 'O';
    private static JButton[][] buttons = new JButton[SIZE][SIZE];
    private static char[][] board = new char[SIZE][SIZE];
    private static JFrame frame;
    private static JLabel statusLabel;
    private static boolean gameEnded = false;

    public static void main(String[] args) {
        initializeBoard();

        // Create custom colors to use in the game visual design
        Color custom = new Color(255, 215, 0);
        Color customII = new Color(200, 150, 255);
        Color customIII = new Color(102, 51, 153);

        // Create the frame
        frame = new JFrame("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(customIII);

        // Create the panel for buttons
        JPanel buttonPanel = new JPanel(new GridLayout(SIZE, SIZE));

        // Add buttons to the panel
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setPreferredSize(new Dimension(100, 100));
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 40));
                buttons[i][j].setBackground(customII);
                buttons[i][j].setForeground(Color.WHITE);
                int row = i;
                int col = j;
                buttons[i][j].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Check if the game is not ended and the button is empty
                        if (!gameEnded && board[row][col] == EMPTY) {
                            makeMove(row, col, PLAYER_X);
                            displayBoard();
                            // If game is not over, computer makes a move
                            if (!isGameOver()) {
                                int[] move = findBestMove(board, PLAYER_O);
                                makeMove(move[0], move[1], PLAYER_O);
                                displayBoard();
                            }
                        }
                    }
                });
                buttonPanel.add(buttons[i][j]);
            }
        }

        // Add the button panel to the frame
        frame.add(buttonPanel, BorderLayout.CENTER);

        // Create and add status label
        statusLabel = new JLabel("Player X's turn");
        statusLabel.setForeground(Color.WHITE);
        frame.add(statusLabel, BorderLayout.SOUTH);

        // Set frame properties and make it visible
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Initialize the game board with empty cells
    private static void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    // Update the GUI to reflect the current state of the board
    private static void displayBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                buttons[i][j].setText(String.valueOf(board[i][j]));
            }
        }
        updateStatusLabel();
    }

    // Update the status label based on the game state
    private static void updateStatusLabel() {
        char winner = determineWinner();
        if (winner == EMPTY) {
            if (isBoardFull()) {
                statusLabel.setText("Draw! Game Over");
                gameEnded = true;
            } else {
                statusLabel.setText("Player " + (getPlayer(board) == PLAYER_X ? "X" : "O") + "'s turn");
            }
        } else {
            statusLabel.setText("Player " + winner + " wins! Game Over");
            gameEnded = true;
        }
    }

    // Determine which player's turn it is based on the number of Xs and Os on the board
    private static char getPlayer(char[][] board) {
        int countX = 0;
        int countO = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == PLAYER_X) {
                    countX++;
                } else if (board[i][j] == PLAYER_O) {
                    countO++;
                }
            }
        }
        return (countX == countO) ? PLAYER_X : PLAYER_O;
    }

    // Check if the game is over (win or draw)
    private static boolean isGameOver() {
        return determineWinner() != EMPTY || isBoardFull();
    }

    // Check if the board is full
    private static boolean isBoardFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    // Determine the winner of the game
    private static char determineWinner() {
        // Check rows
        for (int i = 0; i < SIZE; i++) {
            if (board[i][0] != EMPTY && board[i][0] == board[i][1] && board[i][0] == board[i][2]) {
                return board[i][0];
            }
        }

        // Check columns
        for (int j = 0; j < SIZE; j++) {
            if (board[0][j] != EMPTY && board[0][j] == board[1][j] && board[0][j] == board[2][j]) {
                return board[0][j];
            }
        }

        // Check diagonals
        if (board[0][0] != EMPTY && board[0][0] == board[1][1] && board[0][0] == board[2][2]) {
            return board[0][0];
        }
        if (board[0][2] != EMPTY && board[0][2] == board[1][1] && board[0][2] == board[2][0]) {
            return board[0][2];
        }

        return EMPTY; // No winner yet
    }

    // Make a move on the board
    private static void makeMove(int row, int col, char player) {
        board[row][col] = player;
    }

    // Find the best move for the AI player using minimax algorithm
    private static int[] findBestMove(char[][] board, char player) {
        int bestScore = (player == PLAYER_X) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int[] bestMove = new int[]{-1, -1};

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    board[i][j] = player;
                    int score = minimax(board, 0, false, player == PLAYER_X);
                    board[i][j] = EMPTY;

                    if ((player == PLAYER_X && score > bestScore) || (player == PLAYER_O && score < bestScore)) {
                        bestScore = score;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }

        return bestMove;
    }

    // Minimax algorithm to determine the best move
    private static int minimax(char[][] board, int depth, boolean isMaximizing, boolean isPlayerX) {
        char currentPlayer = isPlayerX ? PLAYER_X : PLAYER_O;
        char opponent = isPlayerX ? PLAYER_O : PLAYER_X;

        if (isGameOver()) {
            char winner = determineWinner();
            if (winner == PLAYER_X) {
                return 10;
            } else if (winner == PLAYER_O) {
                return -10;
            } else {
                return 0; // Draw
            }
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = currentPlayer;
                        int score = minimax(board, depth + 1, false, isPlayerX);
                        board[i][j] = EMPTY;
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = opponent;
                        int score = minimax(board, depth + 1, true, isPlayerX);
                        board[i][j] = EMPTY;
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }
}