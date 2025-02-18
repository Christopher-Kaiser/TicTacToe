import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Font;

public class DifficultyDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private Difficulty selectedDifficulty = Difficulty.MEDIUM;
    private boolean startGame = false;

    public DifficultyDialog(JFrame parent) {
        super(parent, "Select Difficulty", true);
        initializeDialog();
    }

    private void initializeDialog() {
        setLayout(new BorderLayout(10, 10));
        
        // Title panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Welcome to Tic Tac Toe!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titlePanel.add(titleLabel);
        
        // Difficulty selection panel
        JPanel selectionPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        ButtonGroup difficultyGroup = new ButtonGroup();
        
        for (Difficulty difficulty : Difficulty.values()) {
            JRadioButton radioButton = new JRadioButton(difficulty.toString());
            radioButton.setActionCommand(difficulty.name());
            if (difficulty == Difficulty.MEDIUM) {
                radioButton.setSelected(true);
            }
            radioButton.addActionListener(e -> 
                selectedDifficulty = Difficulty.valueOf(e.getActionCommand()));
            difficultyGroup.add(radioButton);
            selectionPanel.add(radioButton);
        }
        
        // Description panel
        JPanel descriptionPanel = new JPanel();
        JLabel descriptionLabel = new JLabel("<html><body style='width: 200px'>" +
            "Easy: AI makes occasional mistakes<br>" +
            "Medium: AI plays with moderate skill<br>" +
            "Hard: AI plays perfectly" +
            "</body></html>");
        descriptionPanel.add(descriptionLabel);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> {
            startGame = true;
            dispose();
        });
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));
        
        buttonPanel.add(startButton);
        buttonPanel.add(exitButton);
        
        // Add all panels to dialog
        add(titlePanel, BorderLayout.NORTH);
        add(selectionPanel, BorderLayout.CENTER);
        add(descriptionPanel, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Dialog properties
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(getOwner());
        setResizable(false);
    }

    public Difficulty getSelectedDifficulty() {
        return selectedDifficulty;
    }

    public boolean shouldStartGame() {
        return startGame;
    }
}