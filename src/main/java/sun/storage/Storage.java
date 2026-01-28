package sun.storage;

import sun.task.Task;
import sun.task.TaskList;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    // IOException != File not exist / File is empty
    // IOException happens when scanner tries to scan

    public ArrayList<Task> load() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();

        // Points to where the file should be, NOT the file itself
        // Does NOT open the file & Does NOT read it & Does NOT create the file
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

    // IOException when writing fails
    public void save(TaskList tasks) throws IOException {
        File file = new File(filePath);

        // Does NOT guarantee that the folder exist, it just points to the path
        File parent = file.getParentFile();

        //"parent != null" just means there is a folder in the filePath String
        //After that, "!parent.exists()" checks if the folder exists physically on disk, and "mkdirs()" creates it if missing.
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        // FileWriter creates the file if it doesn't exist, else, it overwrites the file
        try (FileWriter writer = new FileWriter(filePath)) {
            for (Task task : tasks.getTasks()) {
                writer.write(task.toFileString());
                writer.write(System.lineSeparator());
            }
        }
    }
}
