package sun;

import sun.exception.InvalidCommandException;
import sun.exception.InvalidTaskNumberException;
import sun.exception.InvalidTodoException;
import sun.exception.InvalidDeadlineException;
import sun.exception.InvalidEventException;
import sun.parser.InputParser;
import sun.storage.Storage;
import sun.task.TaskList;
import sun.ui.Ui;

import java.io.IOException;

public class Sun {
    // Attributes/Fields
    private Storage storage;
    private TaskList tasks;
    private Ui ui = new Ui();;

    // Constructor
    public Sun(String filePath) {
        // Prepare Storage
        storage = new Storage(filePath);

        // Load tasks from disk
        try {
            this.tasks = new TaskList(storage.load());
        } catch (IOException e) {
            System.out.println("Failed to load tasks. Starting with empty list.");
            this.tasks = new TaskList();
        }
    }

    // Loop in "main()" is moved here
    public void run() {
        ui.printWelcome();

        while (true) {
            // Trim user input
            String input = ui.readLine();

            // Skip empty inputs ("")
            if (input.isEmpty()) {
                continue;
            }

            if (InputParser.isBye(input)) {
                break;
            }

            // Process inputs
            try {
                InputParser.parseInput(input, tasks, storage, ui);
            } catch (InvalidCommandException | InvalidTaskNumberException |
                     InvalidTodoException | InvalidDeadlineException |
                     InvalidEventException | IOException | IllegalArgumentException e) {
                ui.printError(e.getMessage());
            }
        }

        ui.printBye();
    }

    // Main
    public static void main(String[] args) {
        new Sun("./data/sun.txt").run();
    }
}
