package filehandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class CSVFileHandler { // singleton design pattern
    
    public static int[][] CSVReader(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        List<String[]> rows = new ArrayList<>();

        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            rows.add(values);
        }
        br.close();

        int[][] array = new int[rows.size()][];
        for (int i = 0; i < rows.size(); i++) {
            String[] stringRow = rows.get(i);
            int[] intRow = new int[stringRow.length];

            for (int j = 0; j < stringRow.length; j++) {
                intRow[j] = Integer.parseInt(stringRow[j].trim());
            }

            array[i] = intRow;
        }

        return array;
    }

   
    public static void CSVWriter(String filename, int[][] data) throws IOException {
        // Create parent directories if they don't exist
        File file = new File(filename);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));

        for (int i = 0; i < data.length; i++) {
            StringBuilder line = new StringBuilder();
            for (int j = 0; j < data[i].length; j++) {
                if (j > 0) {
                    line.append(",");
                }
                line.append(data[i][j]);
            }
            bw.write(line.toString());
            bw.newLine();
        }

        bw.close();
    }

  
    public static void appendToLog(String filename, String line) throws IOException {
        File file = new File(filename);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        PrintWriter pw = new PrintWriter(new FileWriter(filename, true));
        pw.println(line);
        pw.close();
    }


    public static List<String> readLogFile(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        File file = new File(filename);

        if (!file.exists()) {
            return lines;
        }

        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                lines.add(line);
            }
        }
        br.close();

        return lines;
    }

    /**
     * Read the last line from a log file
     */
    public static String readLastLogLine(String filename) throws IOException {
        List<String> lines = readLogFile(filename);
        if (lines.isEmpty()) {
            return null;
        }
        return lines.get(lines.size() - 1);
    }


    public static void removeLastLogLine(String filename) throws IOException {
        List<String> lines = readLogFile(filename);

        if (lines.isEmpty()) {
            return;
        }

        // Remove last line
        lines.remove(lines.size() - 1);

        // Rewrite the file
        PrintWriter pw = new PrintWriter(new FileWriter(filename));
        for (String line : lines) {
            pw.println(line);
        }
        pw.close();
    }


    public static boolean deleteFile(String filename) {
        File file = new File(filename);
        return file.delete();
    }

   

}