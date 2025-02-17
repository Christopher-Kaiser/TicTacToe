import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
    private static final Color BACKGROUND_COLOR = new Color(102, 51, 153);
    private static final Color BUTTON_COLOR = new Color(200, 150, 255);

    public GameGUI() {
        game = new Game(new HumanPlayer(Symbol.X), new AIPlayer(Symbol.O));
        buttons = new JButton[3][3];
        statusLabel = new JLabel("Player X's turn");
        
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Tic Tac Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);

        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.CENTER);
        
        statusLabel.setForeground(Color.WHITE);
        add(statusLabel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
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
                statusLabel.setText("Player " + game.getCurrentPlayer().getSymbol() + " wins!");
                break;
            case DRAW:
                statusLabel.setText("Draw!");
                break;
        }
    }
}