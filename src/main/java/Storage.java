import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    // IOException != File not exist / File is empty

    public ArrayList<Task> load() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        // Return empty list of Task if file does not exist
        if (!file.exists()) {
            return tasks;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                tasks.add(Task.fromFileString(scanner.nextLine()));
            }
        }

        return tasks;
    }

    public void save(TaskList tasks) throws IOException {
        File file = new File(filePath);
        File parent = file.getParentFile();

        //"parent != null" just means there is a folder in the filePath String
        //After that, "!parent.exists()" checks if the folder exists physically on disk, and "mkdirs()" creates it if missing.
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (FileWriter writer = new FileWriter(filePath)) {
            for (Task task : tasks.getAll()) {
                writer.write(task.toFileString());
                writer.write(System.lineSeparator());
            }
        }
    }
}
