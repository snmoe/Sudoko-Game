package controllers;

import java.util.ArrayList;
import model.Game;
import verification.FailedVerificationResult;
import verification.SequentialVerificationMode;

public class VerifyController {

    private SequentialVerificationMode verifier;

    public VerifyController() {
        this.verifier = new SequentialVerificationMode();
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
