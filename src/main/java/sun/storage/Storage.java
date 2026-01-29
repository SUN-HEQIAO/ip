package sun.storage;

import sun.task.Task;
import sun.task.TaskList;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles reading from and writing to the file that stores tasks.
 * <p>
 * The <code>Storage</code> class is responsible for loading tasks from a file
 * into memory and saving the current task list back to the file.
 * <p>
 * File operations may throw {@link IOException} if reading or writing fails.
 *
 * @see Task
 * @see TaskList
 */
public class Storage {
    /** The file path where tasks are stored */
    private final String filePath;

    /**
     * Creates a Storage object pointing to the given file path.
     * <p>
     * The file may or may not exist yet. No file operations are performed
     * in the constructor.
     *
     * @param filePath the path to the file where tasks are stored
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    // IOException != File not exist / File is empty
    // IOException happens when scanner tries to scan

    /**
     * Loads tasks from the file specified in {@link #filePath}.
     * <p>
     * If the file does not exist, an empty list of tasks is returned.
     * Each line in the file is converted into a {@link Task} object
     * using {@link Task#fromFileString(String)}.
     *
     * @return an {@link ArrayList} of {@link Task} objects loaded from the file
     * @throws IOException if an I/O error occurs while reading the file
     * @see Task#fromFileString(String)
     */
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

    /**
     * Saves the current task list to the file specified in {@link #filePath}.
     * <p>
     * If the file does not exist, it will be created along with any missing
     * parent directories. If it already exists, it will be overwritten.
     * <p>
     * Each {@link Task} in the {@link TaskList} is converted to a string
     * using {@link Task#toFileString()} and written line by line.
     *
     * @param tasks the {@link TaskList} to save
     * @throws IOException if an I/O error occurs while writing to the file
     * @see Task#toFileString()
     */
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
