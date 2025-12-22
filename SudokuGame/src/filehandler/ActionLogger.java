package filehandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class ActionLogger{

    private static final String LOG_PATH = "resources/incomplete/log.txt";

     public static void logAction(String actionString) throws IOException {
        File logFile = new File(LOG_PATH);
        logFile.getParentFile().mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            writer.write(actionString);
            writer.newLine();
        }
    }

  
    public static String getLastAction() throws IOException {
        List<String> lines = readAllLines();
        if (lines.isEmpty()) {
            return null;
        }
        return lines.get(lines.size() - 1);
    }

    
    public static String removeLastAction() throws IOException {
        List<String> lines = readAllLines();
        if (lines.isEmpty()) {
            return null;
        }

        String lastLine = lines.remove(lines.size() - 1);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_PATH))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }

        return lastLine;
    }


    public static void clearLog() throws IOException {
        File logFile = new File(LOG_PATH);
        if (logFile.exists()) {
            logFile.delete();
        }
    }


    public static boolean hasActions() throws IOException {
        File logFile = new File(LOG_PATH);
        if (!logFile.exists()) {
            return false;
        }
        List<String> lines = readAllLines();
        return !lines.isEmpty();
    }

    private static List<String> readAllLines() throws IOException {
        List<String> lines = new ArrayList<>();
        File logFile = new File(LOG_PATH);

        if (!logFile.exists()) {
            return lines;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        }
        return lines;
    }
}
