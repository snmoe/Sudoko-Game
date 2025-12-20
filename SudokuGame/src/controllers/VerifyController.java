package controllers;

import java.util.ArrayList;
import model.Game;
import verification.FailedVerificationResult;
import verification.DuplicationVerificationMode;

public class VerifyController {

    private DuplicationVerificationMode verifier;

    public VerifyController() {
        this.verifier = new DuplicationVerificationMode();
    }

    public String verify(Game game) {
        ArrayList<FailedVerificationResult> failures = verifier.verify(game);

        if (!failures.isEmpty()) {
            StringBuilder sb = new StringBuilder(" INVALID ");
            for (FailedVerificationResult f : failures) {
                sb.append(f.toString()).append(" ");
            }
            return sb.toString().trim();
        }
        if (!game.isFull()) {
            return "INCOMPLETE";
        }
        return "VALID";

    }
}
