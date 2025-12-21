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
        DuplicationVerificationMode verifier = new DuplicationVerificationMode();
        ArrayList<FailedVerificationResult> failures = verifier.verify(game);

        if (status == VerificationEnum.VALID) {
            return "VALID";
        }
        if (status == VerificationEnum.INCOMPLETE) {
            return "INCOMPLETE";
        }
        StringBuilder sb = new StringBuilder("INVALID");
        for (FailedVerificationResult f : failures) {
            sb.append(f.toString()).append(" ");
        }
        return sb.toString().trim();
    }

}
