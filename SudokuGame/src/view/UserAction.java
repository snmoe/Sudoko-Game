package view;

public class UserAction {
    private int row; // x coordinate (0-8)
    private int col; // y coordinate (0-8)
    private int newValue; // the value the user entered
    private int prevValue; // the previous value (for undo)
    private long timestamp; // when the action occurred

    public UserAction(int row, int col, int newValue, int prevValue) {
        this.row = row;
        this.col = col;
        this.newValue = newValue;
        this.prevValue = prevValue;
        this.timestamp = System.currentTimeMillis();
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getNewValue() {
        return newValue;
    }

    public int getPrevValue() {
        return prevValue;
    }

    public long getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        return "(" + row + ", " + col + ", " + newValue + ", " + prevValue + ")";
    }

    public static UserAction fromString(String logLine) {
        String cleaned = logLine.trim().replace("(", "").replace(")", "");
        String[] parts = cleaned.split(",");

        int row = Integer.parseInt(parts[0].trim());
        int col = Integer.parseInt(parts[1].trim());
        int newValue = Integer.parseInt(parts[2].trim());
        int prevValue = Integer.parseInt(parts[3].trim());

        return new UserAction(row, col, newValue, prevValue);
    }

   
    public UserAction inverse() {
        return new UserAction(row, col, prevValue, newValue);
    }
}