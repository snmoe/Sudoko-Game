package controllers;

import java.util.ArrayList;
import model.Game;
import verification.FailedVerificationResult;
import verification.DuplicationVerificationMode;
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
        }
        else{
            return "INVALID";
        }
        
      
    }

}
