/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package verification;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class FailedVerificationResult {

    private String unitType; 
    private int unitIndex; 
    private int value; 
    private int[] positions; 

    public FailedVerificationResult(String unitType, int unitIndex, int value, int[] positions) {
        this.unitType = unitType;
        this.unitIndex = unitIndex;
        this.value = value;
        this.positions = positions;
    }

    public String getUnitType() {
        return unitType;
    }

    public int getUnitIndex() {
        return unitIndex;
    }

    public int[] getPositions() {
        return positions;
    }

  
    public List<int[]> getAbsolutePositions() {
        List<int[]> result = new ArrayList<>();

        for (int pos : positions) {
            int actualPos = pos - 1; 
            int row, col;

            if (unitType.equals("row")) {
                row = unitIndex;
                col = actualPos;
            } else if (unitType.equals("column")) {
                row = actualPos;
                col = unitIndex;
            } else { // box
                int boxRow = (unitIndex / 3) * 3;
                int boxCol = (unitIndex % 3) * 3;
                row = boxRow + (actualPos / 3);
                col = boxCol + (actualPos % 3);
            }

            result.add(new int[] {row, col});
        }

        return result;
    }

    @Override
    public String toString() {
        return String.format(
                "%s %d, #%d, %s",
                unitType.toUpperCase(),
                unitIndex + 1,
                value,
                Arrays.toString(positions));
    }
}
