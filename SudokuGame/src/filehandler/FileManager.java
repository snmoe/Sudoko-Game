package filehandler;

import exceptions.NotFoundException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
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
    
      private String getBasePath() {
        return basePath;
    }

    

    public void saveGame(DifficultyEnum diff, Game game) throws IOException {
        
        String folderPath = getBasePath() + "/" + diff.name().toLowerCase();
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String filename = "game_" + System.currentTimeMillis() + ".csv";
        String fullPath = folderPath + "/" + filename;
        CSVFileHandler.CSVWriter(fullPath, game.getGrid());
    }

    public Game loadGame(DifficultyEnum diff) throws NotFoundException, IOException {
        
       String folderPath = getBasePath() + "/" + diff.name().toLowerCase();
        File folder = new File(folderPath);
        File[] allFiles = folder.listFiles();
        
        ArrayList<File> csvFiles = new ArrayList<>();
        
        if (allFiles != null) {
            for (File file : allFiles) {
                if (file.getName().endsWith(".csv")) {
                    csvFiles.add(file);
                }
            }
        }

        if (csvFiles.isEmpty()) {
            throw new NotFoundException("No games found for difficulty: " + diff);
        }

       
        Random rand = new Random();
        File chosen = csvFiles.get(rand.nextInt(csvFiles.size()));

        int[][] board = CSVFileHandler.CSVReader(chosen.getAbsolutePath());
        return new Game(board);
    }

    public Game loadCurrentGame() throws NotFoundException, IOException {
         String folderPath = getBasePath() + "/current";
        File gameFile = new File(folderPath + "/current_game.csv");

        if (!gameFile.exists()) {
            throw new NotFoundException("No current game found");
        }

        int[][] board = CSVFileHandler.CSVReader(gameFile.getAbsolutePath());
        return new Game(board);
    }

    public void deleteGame(DifficultyEnum level, String filename) {

    }

    public void saveCurrentGame(Game game) throws IOException {
        
        String folderPath = getBasePath() + "/current";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String fullPath = folderPath + "/current_game.csv";
        CSVFileHandler.CSVWriter(fullPath, game.getGrid());
    }

    public boolean hasGameOfLevel(DifficultyEnum level){
        
        
        String folderPath = getBasePath() + "/" + level.name().toLowerCase();
        File folder = new File(folderPath);
        File[] allFiles = folder.listFiles();

        if (allFiles == null) {
            return false;
        }

        for (File file : allFiles) {
            if (file.getName().endsWith(".csv")) {
                return true;
            }
        }
        return false;
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
