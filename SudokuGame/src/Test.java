
import exceptions.InvalidGame;
import exceptions.NotFoundException;
import filehandler.FileManager;
import java.io.IOException;
import model.DifficultyEnum;
import model.Game;
import verification.GameValidator;

public class Test {

   public static void main(String[] args)
            throws NotFoundException, IOException, InvalidGame {
       
       Game a = FileManager.loadGame(DifficultyEnum.SOLVED);
     
       System.out.println(GameValidator.validate(a));
    }
}
