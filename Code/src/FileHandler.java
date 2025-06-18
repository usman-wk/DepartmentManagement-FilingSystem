
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class FileHandler {
    public static <T> void saveToFile(String filename, List<T> data) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (T item : data) {
                writer.println(item.toString());
            }
        } catch (IOException e) {
            System.out.println("Error saving to " + filename);
        }
    }

    public static <T> List<T> loadFromFile(String filename, Function<String, T> fromStringFn) {
        List<T> list = new ArrayList<>();
        File file = new File(filename);

        if (!file.exists()) return list;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    list.add(fromStringFn.apply(line));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading from " + filename);
        }
        return list;
    }
}
