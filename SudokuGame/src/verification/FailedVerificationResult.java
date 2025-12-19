/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package verification;

import java.util.Arrays;

public class FailedVerificationResult {

    private String unitType;  // "row", "column", "box"
    private int unitIndex;    // which row/col/box
    private int value;        // duplicated value
    private int[] positions;  // indices where duplicates occurred

    public FailedVerificationResult(String unitType, int unitIndex, int value, int[] positions) {
        this.unitType = unitType;
        this.unitIndex = unitIndex;
        this.value = value;
        this.positions = positions;
    }

    @Override
    public String toString() {
        return String.format(
                "%s %d, #%d, %s",
                unitType.toUpperCase(),
                unitIndex + 1,
                value,
                Arrays.toString(positions)
        );
    }
}
