
package model;

import java.util.Arrays;

public class Game implements Cloneable {

    private int[][] board;

    public Game(int[][] grid) {
        this.board = board;
    }

    public Game clone() {
        int[][] newGrid = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(this.board[i], 0, newGrid[i], 0, 9);
        }
        return new Game(newGrid);
    }

    public void setCell(int row, int col, int value) {
        board[row][col] = value;
    }

    public int getCell(int row, int col) {
        return board[row][col];
    }

    public boolean isFull() {
        for (int[] row : board) {
            for (int cell : row) {
                if (cell == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public int[][] getGrid() {
        return board;
    }

    public int[] getRow(int i) {
        int[] row = new int[9];
        System.arraycopy(board[i], 0, row, 0, 9);
        return row;
    }

    public int[] getColumn(int i) {
        int[] col = new int[9];
        for (int j = 0; j < 9; j++) {
            col[j] = board[j][i];
        }
        return col;
    }

    public int[] getSubgrid(int i) {
        int[] sub = new int[9];
        int rowStart = (i / 3) * 3;
        int colStart = (i % 3) * 3;
        int index = 0;
        for (int r = rowStart; r < rowStart + 3; r++) {
            for (int c = colStart; c < colStart + 3; c++) {
                sub[index++] = board[r][c];
            }
        }
        return sub;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] row : board) {
            sb.append(Arrays.toString(row)).append("\n");
        }
        return sb.toString();
    }
}
