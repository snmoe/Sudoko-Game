package ui;

import adapter.ViewAdapter;
import view.UserAction;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public class SudokuGUI extends JFrame {

    private static final Color FIXED_BG = new Color(230, 240, 248);
    private static final Color EDIT_BG = Color.WHITE;
    private static final Color SELECT_BG = new Color(200, 220, 240);
    private static final Color INVALID_BG = new Color(255, 200, 200);
    private static final Color GRID_COLOR = new Color(60, 80, 120);
    private static final Color TEXT_COLOR = new Color(40, 60, 90);

    private final JTextField[][] cells = new JTextField[9][9];
    private final boolean[][] fixed = new boolean[9][9];

    private final ViewAdapter adapter = new ViewAdapter();

    private JButton solveBtn, undoBtn, verifyBtn;
    private boolean internalEdit = false;
    private int previousValue = 0;

    public SudokuGUI() {
        setTitle("Sudoku");
        setSize(600, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createBoard(), BorderLayout.CENTER);
        add(createControls(), BorderLayout.SOUTH);

        startup();
        setVisible(true);
    }

    private void startup() {
        boolean[] catalog = adapter.getCatalog();

        try {
            if (catalog[0]) {
                int choice = JOptionPane.showConfirmDialog(
                        this,
                        "An unfinished game was found. Do you want to continue it?",
                        "Continue Game?",
                        JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    int[][] current = adapter.getCurrentGame();
                    int[][] initial = adapter.getInitialGame();
                    setBoard(current, initial);
                    return;
                }
            }

            if (catalog[1]) {
                int[][] game = adapter.getGame(askDifficulty());
                adapter.saveInitialGame(game);
                adapter.updateCurrentGame(game); 
                setBoard(game);
                return;
            }

            JOptionPane.showMessageDialog(
                    this,
                    "No unfinished game and missing difficulty levels.\n" +
                            "Please select a solved Sudoku file\n" +
                            "to generate difficulty levels.",
                    "Solved Game Required",
                    JOptionPane.INFORMATION_MESSAGE);
            adapter.driveGames(askSolvedFilePath());

            int[][] game = adapter.getGame(askDifficulty());
            adapter.saveInitialGame(game);
            adapter.updateCurrentGame(game);
            setBoard(game);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Startup Error: " + e.getMessage());

        }
    }

    private JPanel createBoard() {
        JPanel panel = new JPanel(new GridLayout(9, 9));

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {

                JTextField tf = new JTextField();
                tf.setHorizontalAlignment(JTextField.CENTER);
                tf.setFont(new Font("Segoe UI", Font.BOLD, 20));
                tf.setForeground(TEXT_COLOR);
                tf.setBorder(createCellBorder(r, c));

                ((AbstractDocument) tf.getDocument())
                        .setDocumentFilter(new DigitFilter());

                int row = r, col = c;

                tf.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        previousValue = getCellValue(row, col);
                        if (!fixed[row][col])
                            tf.setBackground(SELECT_BG);
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        if (internalEdit || fixed[row][col])
                            return;

                        int current = getCellValue(row, col);
                        if (current != previousValue) {
                            logAction(new UserAction(
                                    row, col, current, previousValue));
                            saveCurrentGame();
                        }

                        verify();
                        updateSolveButton();
                    }
                });

                cells[r][c] = tf;
                panel.add(tf);
            }
        }

        return panel;
    }

    private JPanel createControls() {
        JPanel panel = new JPanel();

        verifyBtn = new JButton("Verify");
        solveBtn = new JButton("Solve");
        undoBtn = new JButton("Undo");

        solveBtn.setEnabled(false);

        verifyBtn.addActionListener(e -> verify());
        solveBtn.addActionListener(e -> solve());
        undoBtn.addActionListener(e -> undo());

        panel.add(verifyBtn);
        panel.add(solveBtn);
        panel.add(undoBtn);

        return panel;
    }

    private void solve() {
        try {
            internalEdit = true;
            setBoard(adapter.solveGame(getBoard()));
            internalEdit = false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void undo() {
        try {
            UserAction action = adapter.undo();
            if (action == null)
                return; 

            if (fixed[action.getRow()][action.getCol()])
                return; 

            internalEdit = true;
            cells[action.getRow()][action.getCol()]
                    .setText(action.getPrevValue() == 0 ? "" : String.valueOf(action.getPrevValue()));
            internalEdit = false;

            saveCurrentGame();
            verify();
            updateSolveButton();

        } catch (Exception ignored) {
        }
    }

    private void verify() {
        boolean[][] invalid = adapter.verifyGame(getBoard());
        boolean isFull = true;
        boolean isCorrect = true;

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (invalid[r][c])
                    isCorrect = false;
                if (cells[r][c].getText().isEmpty())
                    isFull = false;

                cells[r][c].setBackground(
                        invalid[r][c] ? INVALID_BG : fixed[r][c] ? FIXED_BG : EDIT_BG);
            }
        }

        if (isCorrect && isFull) {
            JOptionPane.showMessageDialog(this, "Congratulations! You have solved the puzzle!");
            try {
                adapter.removeCurrentGame();
            } catch (Exception ignored) {
            }
        }
    }

    private void updateSolveButton() {
        int empty = 0;
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++)
                if (!fixed[r][c] && cells[r][c].getText().isEmpty())
                    empty++;

        solveBtn.setEnabled(empty == 5);
    }

    private void setBoard(int[][] grid) {
        setBoard(grid, grid); 
    }

    private void setBoard(int[][] current, int[][] initial) {
        internalEdit = true;

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                fixed[r][c] = initial[r][c] != 0;

                cells[r][c].setText(
                        current[r][c] == 0 ? "" : String.valueOf(current[r][c]));
                cells[r][c].setEditable(!fixed[r][c]);
                cells[r][c].setBackground(
                        fixed[r][c] ? FIXED_BG : EDIT_BG);
            }
        }

        internalEdit = false;
        updateSolveButton();
    }

    private int[][] getBoard() {
        int[][] g = new int[9][9];
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++)
                g[r][c] = cells[r][c].getText().isEmpty()
                        ? 0
                        : Integer.parseInt(cells[r][c].getText());
        return g;
    }

    private int getCellValue(int r, int c) {
        return cells[r][c].getText().isEmpty()
                ? 0
                : Integer.parseInt(cells[r][c].getText());
    }

    private void saveCurrentGame() {
        try {
            adapter.updateCurrentGame(getBoard());
        } catch (Exception ignored) {
        }
    }

    private void logAction(UserAction ua) {
        try {
            adapter.logUserAction(ua);
        } catch (Exception ignored) {
        }
    }

    private char askDifficulty() {

        JDialog dialog = new JDialog(this, "Select Difficulty", true);
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.setResizable(false);

        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(GRID_COLOR);
        JLabel title = new JLabel("Choose Difficulty");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        titlePanel.add(title);

        JPanel buttons = new JPanel(new GridLayout(1, 3, 15, 0));
        buttons.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buttons.setBackground(Color.WHITE);

        final char[] result = new char[1];

        JButton easy = createDiffButton("Easy");
        JButton medium = createDiffButton("Medium");
        JButton hard = createDiffButton("Hard");

        easy.addActionListener(e -> {
            result[0] = 'E';
            dialog.dispose();
        });
        medium.addActionListener(e -> {
            result[0] = 'M';
            dialog.dispose();
        });
        hard.addActionListener(e -> {
            result[0] = 'H';
            dialog.dispose();
        });

        buttons.add(easy);
        buttons.add(medium);
        buttons.add(hard);

        dialog.add(titlePanel, BorderLayout.NORTH);
        dialog.add(buttons, BorderLayout.CENTER);
        dialog.setVisible(true);

        return result[0];
    }

    private JButton createDiffButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setBackground(FIXED_BG);
        b.setForeground(TEXT_COLOR);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createLineBorder(GRID_COLOR, 2));
        return b;
    }

    private String askSolvedFilePath() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
            System.exit(0);
        return chooser.getSelectedFile().getAbsolutePath();
    }

    private Border createCellBorder(int r, int c) {
        return BorderFactory.createMatteBorder(
                r % 3 == 0 ? 2 : 1,
                c % 3 == 0 ? 2 : 1,
                r == 8 ? 2 : 1,
                c == 8 ? 2 : 1,
                GRID_COLOR);
    }

    private static class DigitFilter extends DocumentFilter {
        @Override
        public void replace(FilterBypass fb, int offset, int length,
                String text, AttributeSet attrs)
                throws BadLocationException {

            if (text.isEmpty() ||
                    (text.matches("[1-9]") && fb.getDocument().getLength() == 0)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SudokuGUI::new);
    }
}
