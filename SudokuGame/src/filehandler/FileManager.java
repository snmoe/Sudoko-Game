package filehandler;

import java.io.File;
import java.io.IOException;
import model.*;

public class FileManager {

    private static FileManager instance = null;
    private String basePath = "resources";

    private FileManager() {
        initializeFolders();
    }

    public synchronized static FileManager getInstance() {

        if (instance == null) {
            instance = new FileManager();
        }
        return instance;
    }

    public void saveGame(DifficultyEnum diff, Game game) throws IOException {
        
        String folderPath = basePath + "/" + diff.name().toLowerCase();
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String filename = "game_" + System.currentTimeMillis() + ".csv";
        String fullPath = folderPath + "/" + filename;
        CSVFileHandler.CSVWriter(fullPath, game.getGrid());
    }

    public Game loadGame(DifficultyEnum diff) {
        return null;
    }

    public Game loadCurrentGame() {
        return null;
    }

    public void deleteGame(DifficultyEnum level, String filename) {

    }

    public void saveCurrentGame(Game game) {

    }

    public boolean hasGameOfLevel(DifficultyEnum level) {
        return true;
    }

    private void initializeFolders() {
        String[] folders = {"easy", "medium", "hard", "incomplete"};
        for (String folder : folders) {
            File dir = new File(basePath + "/" + folder);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
    }

}
