    
package verification;

import java.util.ArrayList;
import model.Game;


public class GameValidator {
    public static VerificationEnum validate(Game game){
        
        int[][] grid = game.getGrid();
        if(!game.isFull()){
            return VerificationEnum.INCOMPLETE;
        }
        
        DuplicationVerificationMode verifier = new DuplicationVerificationMode();
        ArrayList<FailedVerificationResult> failures = verifier.verify(game);
        
        if(!failures.isEmpty()){
            return VerificationEnum.INVALID;
        }
        
        
        return VerificationEnum.VALID;
    }
}
