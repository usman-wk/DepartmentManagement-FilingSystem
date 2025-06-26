package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class FileHandler {
    
    public static <T> void saveToFile(String filename, List<T> data) {
        try {
            // Create directory if it doesn't exist
            File file = new File(filename);
            file.getParentFile().mkdirs();
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
                for (T item : data) {
                    writer.println(item.toString());
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving to " + filename + ": " + e.getMessage());
        }
    }

    public static <T> List<T> loadFromFile(String filename, Function<String, T> fromStringFn) {
        List<T> list = new ArrayList<>();
        File file = new File(filename);

        if (!file.exists()) {
            return list;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    T item = fromStringFn.apply(line);
                    if (item != null) {
                        list.add(item);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading from " + filename + ": " + e.getMessage());
        }
        return list;
    }
}