
package verification;

import java.util.ArrayList;
import java.util.HashMap;
import model.Game;

public class UnitChecker { //units are either the rows, columns or boxes

    public static ArrayList<FailedVerificationResult> checkRow(int row, Game board) {
        ArrayList<FailedVerificationResult> results = new ArrayList<>();
        int[] rowData = board.getRow(row);

        // Plain map from value -> list of indices
        HashMap<Integer, ArrayList<Integer>> positions = new HashMap<>();

        for (int col = 0; col < 9; col++) {
            int value = rowData[col];
            if (value < 1 || value > 9) {
                continue;
            }

            // Check if the map already has this value
            if (!positions.containsKey(value)) {
                positions.put(value, new ArrayList<>());
            }

            positions.get(value).add(col + 1);
        }

        // Check for duplicates
        for (Integer value : positions.keySet()) {
            ArrayList<Integer> cols = positions.get(value);
            if (cols.size() > 1) {
                // convert ArrayList<Integer> to int[]
                int[] indices = new int[cols.size()];
                for (int i = 0; i < cols.size(); i++) {
                    indices[i] = cols.get(i);
                }
                results.add(new FailedVerificationResult("row", row, value, indices));
            }
        }

        return results;
    }

    public static ArrayList<FailedVerificationResult> checkColumn(int col, Game board) {
        ArrayList<FailedVerificationResult> results = new ArrayList<>();
        int[] colData = board.getColumn(col);

        HashMap<Integer, ArrayList<Integer>> positions = new HashMap<>();

        for (int row = 0; row < 9; row++) {
            int value = colData[row];

            if (!positions.containsKey(value)) {
                positions.put(value, new ArrayList<>());
            }

            positions.get(value).add(row + 1);
        }

        for (Integer value : positions.keySet()) {
            ArrayList<Integer> rows = positions.get(value);
            if (rows.size() > 1) {
                int[] indices = new int[rows.size()];
                for (int i = 0; i < rows.size(); i++) {
                    indices[i] = rows.get(i);
                }
                results.add(new FailedVerificationResult("column", col, value, indices));
            }
        }

        return results;
    }

    public static ArrayList<FailedVerificationResult> checkBox(int boxIndex, Game board) {
        ArrayList<FailedVerificationResult> results = new ArrayList<>();
        int[] boxData = board.getSubgrid(boxIndex);

        HashMap<Integer, ArrayList<Integer>> positions = new HashMap<>();

        for (int pos = 0; pos < 9; pos++) {
            int value = boxData[pos];

            if (!positions.containsKey(value)) {
                positions.put(value, new ArrayList<>());
            }

            positions.get(value).add(pos + 1);
        }

        for (Integer value : positions.keySet()) {
            ArrayList<Integer> posList = positions.get(value);
            if (posList.size() > 1) {
                int[] indices = new int[posList.size()];
                for (int i = 0; i < posList.size(); i++) {
                    indices[i] = posList.get(i);
                }
                results.add(new FailedVerificationResult("box", boxIndex, value, indices));
            }
        }

        return results;
    }
}
