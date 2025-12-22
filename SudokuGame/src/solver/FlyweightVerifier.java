package solver;

import model.Game;
import verification.DuplicationVerificationMode;

public class FlyweightVerifier {

    private final DuplicationVerificationMode verifier;

    public FlyweightVerifier() {
        this.verifier = new DuplicationVerificationMode();
    }

    public boolean isValidWith(Game game, int[][] positions, int[] values) {

       
        for (int i = 0; i < positions.length; i++) {
            int row = positions[i][0];
            int col = positions[i][1];
            game.setCell(row, col, values[i]);
        }

        boolean valid = verifier.verify(game).isEmpty();

       
        for (int i = 0; i < positions.length; i++) {
            int row = positions[i][0];
            int col = positions[i][1];
            game.setCell(row, col, 0);
        }

        return valid;
    }
}