import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Font;

public class GameGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private final Game game;
    private final JButton[][] buttons;
    private final JLabel statusLabel;
    private final JLabel statsLabel;
    private final JButton replayButton;
    private final GameStats gameStats;
    private final Timer animationTimer;
    private static final Color BACKGROUND_COLOR = new Color(102, 51, 153);
    private static final Color BUTTON_COLOR = new Color(200, 150, 255);

    public GameGUI() {
        DifficultyDialog dialog = new DifficultyDialog(this);
        dialog.setVisible(true);
        
        if (!dialog.shouldStartGame()) {
            System.exit(0);
        }
        
        Difficulty selectedDifficulty = dialog.getSelectedDifficulty();
        game = new Game(
            new HumanPlayer(Symbol.X), 
            new AIPlayer(Symbol.O, selectedDifficulty)
        );

        buttons = new JButton[3][3];
        statusLabel = new JLabel("Player X's turn");
        statsLabel = new JLabel("Games: 0 | Player X: 0 | Player O: 0 | Draws: 0");
        replayButton = new JButton("Play Again");
        gameStats = new GameStats();
        animationTimer = new Timer(50, null);  // Initialize timer in constructor
        animationTimer.setRepeats(true);
        
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Tic Tac Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Add stats panel at the top
        JPanel statsPanel = new JPanel();
        statsPanel.setBackground(BACKGROUND_COLOR);
        statsLabel.setForeground(Color.WHITE);
        statsLabel.setFont(new Font("Arial", Font.BOLD, 12));
        statsPanel.add(statsLabel);
        add(statsPanel, BorderLayout.NORTH);

        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(BACKGROUND_COLOR);
        
        statusLabel.setForeground(Color.WHITE);
        bottomPanel.add(statusLabel, BorderLayout.CENTER);
        
        replayButton.setBackground(BUTTON_COLOR);
        replayButton.setForeground(Color.WHITE);
        replayButton.setFont(new Font("Arial", Font.BOLD, 14));
        replayButton.addActionListener(e -> confirmAndResetGame());
        bottomPanel.add(replayButton, BorderLayout.EAST);
        
        add(bottomPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void confirmAndResetGame() {
        if (game.getState() == GameState.IN_PROGRESS) {
            int result = JOptionPane.showConfirmDialog(
                this,
                "Current game is still in progress. Are you sure you want to restart?",
                "Confirm Restart",
                JOptionPane.YES_NO_OPTION
            );
            if (result != JOptionPane.YES_OPTION) {
                return;
            }
        }
        
        // Update stats before resetting
        if (game.getState() != GameState.IN_PROGRESS) {
            gameStats.updateStats(game.getState(), game.getCurrentPlayer().getSymbol());
            statsLabel.setText(gameStats.getStatsDisplay());
        }
        
        animateReset();
    }

    private void animateReset() {
        replayButton.setEnabled(false);
        final int[] currentFrame = {0};
        final int totalFrames = 10;
        
        for (var listener : animationTimer.getActionListeners()) {
            animationTimer.removeActionListener(listener);
        }
        
        animationTimer.addActionListener(e -> {
            if (currentFrame[0] < totalFrames) {
                float alpha = 1.0f - (float)currentFrame[0] / totalFrames;
                for (JButton[] buttonRow : buttons) {
                    for (JButton button : buttonRow) {
                        button.setForeground(new Color(1f, 1f, 1f, alpha));
                    }
                }
                currentFrame[0]++;
            } else {
                animationTimer.stop();
                finalizeReset();
            }
        });
        
        animationTimer.start();
    }

    private void finalizeReset() {
        game.reset();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setForeground(Color.WHITE);
            }
        }
        statusLabel.setText("Player X's turn");
        replayButton.setEnabled(true);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 3));
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = createButton(i, j);
                panel.add(buttons[i][j]);
            }
        }
        
        return panel;
    }

    private JButton createButton(int row, int col) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(100, 100));
        button.setFont(new Font("Arial", Font.PLAIN, 40));
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        
        button.addActionListener(e -> handleButtonClick(row, col));
        
        return button;
    }

    private void handleButtonClick(int row, int col) {
        if (game.getState() == GameState.IN_PROGRESS) {
            game.makeMove(row, col);
            updateGUI();
            
            // AI move
            if (game.getState() == GameState.IN_PROGRESS) {
                AIPlayer aiPlayer = (AIPlayer) game.getCurrentPlayer();
                Move aiMove = aiPlayer.getMove(game.getBoard());
                game.makeMove(aiMove.row(), aiMove.col());
                updateGUI();
            }
        }
    }

    private void updateGUI() {
        Board board = game.getBoard();
        char[][] grid = board.getGrid();
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText(String.valueOf(grid[i][j]));
            }
        }
        
        updateStatusLabel();
    }

    private void updateStatusLabel() {
        switch (game.getState()) {
            case IN_PROGRESS:
                statusLabel.setText("Player " + game.getCurrentPlayer().getSymbol() + "'s turn");
                break;
            case WINNER:
                String winner = "Player " + game.getCurrentPlayer().getSymbol();
                statusLabel.setText(winner + " wins!");
                break;
            case DRAW:
                statusLabel.setText("Draw!");
                break;
        }
    }
}