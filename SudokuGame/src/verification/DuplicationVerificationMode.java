
package verification;


import java.util.ArrayList;
import model.Game;

public class DuplicationVerificationMode {

    public ArrayList<FailedVerificationResult> verify(Game board) {

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
