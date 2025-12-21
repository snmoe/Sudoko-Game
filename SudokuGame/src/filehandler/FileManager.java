package filehandler;

import exceptions.NotFoundException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import model.*;

public class FileManager {

    private static String basePath = "resources";

    public FileManager() {
        initializeFolders();
    }
    
   

    public static void saveGame(DifficultyEnum diff, Game game) throws IOException {
        
        String folderPath = basePath + "/" + diff.name().toLowerCase();
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String filename = "game_" + System.currentTimeMillis() + ".csv";
        String fullPath = folderPath + "/" + filename;
        CSVFileHandler.CSVWriter(fullPath, game.getGrid());
    }

    public static Game loadGame(DifficultyEnum diff) throws NotFoundException, IOException {
        
       String folderPath = basePath + "/" + diff.name().toLowerCase();
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

    public static Game loadCurrentGame() throws NotFoundException, IOException {
         String folderPath = basePath + "/current";
        File gameFile = new File(folderPath + "/current_game.csv");

        if (!gameFile.exists()) {
            throw new NotFoundException("No current game found");
        }

        int[][] board = CSVFileHandler.CSVReader(gameFile.getAbsolutePath());
        return new Game(board);
    }

    public static void deleteGame(DifficultyEnum level, String filename) {
     String folderPath = basePath + "/" + level.name().toLowerCase();
    File gameFile = new File(folderPath + "/" + filename);
    if (gameFile.exists()) {
        gameFile.delete();
    }
        
    }

    public static void saveCurrentGame(Game game) throws IOException {
        
        String folderPath = basePath + "/current";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String fullPath = folderPath + "/current_game.csv";
        CSVFileHandler.CSVWriter(fullPath, game.getGrid());
    }

    public static boolean hasGameOfLevel(DifficultyEnum level){
        
        
        String folderPath = basePath + "/" + level.name().toLowerCase();
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
    
    
    public static Game loadGameFromPath(String path) throws IOException {
        int[][] board = CSVFileHandler.CSVReader(path);
        return new Game(board);
    }

}
