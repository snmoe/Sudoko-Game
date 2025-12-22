package ui;

import adapter.ViewAdapter;
import view.UserAction;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class SudokuGUI extends JFrame {

    /* ================= COLORS ================= */

    private static final Color FIXED_BG   = new Color(230, 240, 248);
    private static final Color EDIT_BG    = Color.WHITE;
    private static final Color SELECT_BG  = new Color(200, 220, 240);
    private static final Color INVALID_BG = new Color(255, 200, 200);
    private static final Color GRID_COLOR = new Color(60, 80, 120);
    private static final Color TEXT_COLOR = new Color(40, 60, 90);

    /* ================= STATE ================= */

    private final JTextField[][] cells = new JTextField[9][9];
    private final boolean[][] fixed = new boolean[9][9];

    private final ViewAdapter adapter = new ViewAdapter();

    private boolean internalEdit = false;
    private JButton solveBtn;

    /* ================= CONSTRUCTOR ================= */

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

    /* ================= STARTUP ================= */

    private void startup() {
        boolean[] catalog = adapter.getCatalog();

        try {
            if (catalog[0]) {
                setBoard(adapter.getCurrentGame());
                return;
            }

            if (!catalog[1]) {
                JOptionPane.showMessageDialog(
                        this,
                        "No solved Sudoku found.\nPlease select one.",
                        "Solved Game Required",
                        JOptionPane.INFORMATION_MESSAGE
                );
                adapter.driveGames(askSolvedFilePath());
            }

            setBoard(adapter.getGame(askDifficulty()));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            System.exit(0);
        }
    }

    /* ================= BOARD ================= */

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

                int row = r;
                int col = c;

                /* Focus coloring only */
                tf.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        if (!fixed[row][col])
                            tf.setBackground(SELECT_BG);
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        tf.setBackground(fixed[row][col] ? FIXED_BG : EDIT_BG);
                    }
                });

                /* REAL logging happens here */
                tf.getDocument().addDocumentListener(new DocumentListener() {

                    private int lastValue = 0;

                    private void changed() {
                        if (internalEdit || fixed[row][col]) return;

                        int current = getCellValue(row, col);
                        if (current != lastValue) {
                            try {
                                adapter.logUserAction(
                                        new UserAction(row, col, current, lastValue)
                                );
                            } catch (IOException ignored) {}

                            lastValue = current;
                            saveCurrentGame();
                            verify();
                            updateSolveButton();
                        }
                    }

                    @Override public void insertUpdate(DocumentEvent e) { changed(); }
                    @Override public void removeUpdate(DocumentEvent e) { changed(); }
                    @Override public void changedUpdate(DocumentEvent e) {}
                });

                cells[r][c] = tf;
                panel.add(tf);
            }
        }
        return panel;
    }

    /* ================= CONTROLS ================= */

    private JPanel createControls() {
        JPanel panel = new JPanel();

        JButton easy = new JButton("Easy");
        JButton medium = new JButton("Medium");
        JButton hard = new JButton("Hard");
        JButton verify = new JButton("Verify");
        JButton undo = new JButton("Undo");
        solveBtn = new JButton("Solve");

        solveBtn.setEnabled(false);

        easy.addActionListener(e -> loadGame('E'));
        medium.addActionListener(e -> loadGame('M'));
        hard.addActionListener(e -> loadGame('H'));
        verify.addActionListener(e -> verify());
        undo.addActionListener(e -> undo());
        solveBtn.addActionListener(e -> solve());

        panel.add(easy);
        panel.add(medium);
        panel.add(hard);
        panel.add(verify);
        panel.add(solveBtn);
        panel.add(undo);

        return panel;
    }

    /* ================= UNDO ================= */

    private void undo() {
        internalEdit = true;
        try {
            UserAction action = adapter.undoLastAction();
            if (action == null) return;

            UserAction undo = action.inverse();

            if (!fixed[undo.getRow()][undo.getCol()]) {
                cells[undo.getRow()][undo.getCol()]
                        .setText(undo.getNewValue() == 0 ? "" :
                                String.valueOf(undo.getNewValue()));
            }

            saveCurrentGame();
            verify();
            updateSolveButton();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            internalEdit = false;
        }
    }

    /* ================= GAME OPS ================= */

    private void loadGame(char level) {
        try {
            setBoard(adapter.getGame(level));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void solve() {
        internalEdit = true;
        try {
            setBoard(adapter.solveGame(getBoard()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } finally {
            internalEdit = false;
        }
    }

    private void verify() {
        boolean[][] invalid = adapter.verifyGame(getBoard());
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++)
                cells[r][c].setBackground(
                        invalid[r][c] ? INVALID_BG :
                        fixed[r][c] ? FIXED_BG : EDIT_BG
                );
    }

    private void updateSolveButton() {
        int empty = 0;
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++)
                if (!fixed[r][c] && cells[r][c].getText().isEmpty())
                    empty++;
        solveBtn.setEnabled(empty == 5);
    }

    /* ================= UTIL ================= */

    private void setBoard(int[][] grid) {
        internalEdit = true;
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++) {
                fixed[r][c] = grid[r][c] != 0;
                cells[r][c].setText(grid[r][c] == 0 ? "" : String.valueOf(grid[r][c]));
                cells[r][c].setEditable(!fixed[r][c]);
                cells[r][c].setBackground(fixed[r][c] ? FIXED_BG : EDIT_BG);
            }
        internalEdit = false;
        updateSolveButton();
    }

    private int[][] getBoard() {
        int[][] g = new int[9][9];
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++)
                g[r][c] = cells[r][c].getText().isEmpty()
                        ? 0 : Integer.parseInt(cells[r][c].getText());
        return g;
    }

    private int getCellValue(int r, int c) {
        return cells[r][c].getText().isEmpty()
                ? 0 : Integer.parseInt(cells[r][c].getText());
    }

    private void saveCurrentGame() {
        try {
            adapter.updateCurrentGame(getBoard());
        } catch (IOException ignored) {}
    }

    /* ================= UI HELPERS ================= */

    private char askDifficulty() {
        String[] options = {"Easy", "Medium", "Hard"};
        int res = JOptionPane.showOptionDialog(
                this,
                "Choose Difficulty",
                "Difficulty",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
        return res == 0 ? 'E' : res == 1 ? 'M' : 'H';
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
                GRID_COLOR
        );
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
