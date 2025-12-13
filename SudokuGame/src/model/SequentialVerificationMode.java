/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;

public class SequentialVerificationMode {

    public ArrayList<FailedVerificationResult> verify(SudokuBoard board) {

        ArrayList<FailedVerificationResult> allFailures = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            allFailures.addAll(UnitChecker.checkRow(i, board));
        }

        for (int i = 0; i < 9; i++) {
            allFailures.addAll(UnitChecker.checkColumn(i, board));
        }

        for (int i = 0; i < 9; i++) {
            allFailures.addAll(UnitChecker.checkBox(i, board));
        }

        return allFailures;
    }
}
