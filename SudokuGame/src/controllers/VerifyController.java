package controllers;

import java.util.ArrayList;
import java.util.List;
import model.Game;
import verification.DuplicationVerificationMode;
import verification.FailedVerificationResult;
import verification.GameValidator;
import verification.VerificationEnum;

public class VerifyController {

    public String verify(Game game) {

        VerificationEnum status = GameValidator.validate(game);

        if (status == VerificationEnum.VALID) {
            return "VALID";
        }
        if (status == VerificationEnum.INCOMPLETE) {
            return "INCOMPLETE";
        } else {
            return "INVALID";
        }
   }

    public List<int[]> getInvalidAbsolutePositions(Game game) {
        
        DuplicationVerificationMode mode = new DuplicationVerificationMode();
        List<FailedVerificationResult> results = mode.verify(game);
        ArrayList<int[]> positions = new ArrayList<>();
        
        for(FailedVerificationResult result : results){
            ArrayList<int[]> resultPositions = (ArrayList<int[]>) result.getAbsolutePositions();
            for(int[] position : resultPositions){
                positions.add(position);
            }
        }

        return positions;
    }

}
