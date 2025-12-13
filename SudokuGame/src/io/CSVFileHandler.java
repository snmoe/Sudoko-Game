/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVFileHandler { //singleton design pattern

    private static CSVFileHandler instance = null;

    private CSVFileHandler() {
    }

    public static CSVFileHandler getInstance() {

        if (instance == null) {
            instance = new CSVFileHandler();
        }
        return instance;
    }

    public int[][] CSVReader(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        List<String[]> rows = new ArrayList<>();

        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            rows.add(values);
        }

        int[][] array = new int[rows.size()][];
        for (int i = 0; i < rows.size(); i++) {
            String[] stringRow = rows.get(i);
            int[] intRow = new int[stringRow.length];

            for (int j = 0; j < stringRow.length; j++) {
                intRow[j] = Integer.parseInt(stringRow[j].trim()); // convert each value
            }

            array[i] = intRow;
        }

        return array;

    }
}
