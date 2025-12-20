    
package verification;

import java.util.ArrayList;
import model.Game;


public class GameValidator {
    public static VerificationEnum validateFromResults(Game game){
        
         int[][] grid = game.getGrid();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grid[i][j] == 0) {
                    return VerificationEnum.INCOMPLETE;
                }
            }
        }
        SequentialVerificationMode verifier = new SequentialVerificationMode();
        ArrayList<FailedVerificationResult> failures = verifier.verify(game);
        if(!failures.isEmpty()){
            return VerificationEnum.INVALID;
        }
        
        
        return VerificationEnum.VALID;
    }
}
