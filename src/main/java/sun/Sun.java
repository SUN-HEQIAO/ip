package sun;

import sun.exception.InvalidCommandException;
import sun.exception.InvalidFindException;
import sun.exception.InvalidTaskNumberException;
import sun.exception.InvalidTodoException;
import sun.exception.InvalidDeadlineException;
import sun.exception.InvalidEventException;
import sun.parser.InputParser;
import sun.storage.Storage;
import sun.task.TaskList;
import sun.ui.Ui;

import java.io.IOException;

/**
 * The main class of the Sun application.
 * <p>
 * This class initializes the application, loads tasks from storage,
 * and manages the main loop that handles user input.
 * <p>
 * It coordinates the key components of the program:
 * <code>{@link Storage}</code>, <code>{@link TaskList}</code>,
 * <code>{@link Ui}</code>, and <code>{@link InputParser}</code>.
 * <p>
 * User input is read continuously in a loop until the "bye" command is entered.
 * Any invalid input is caught and displayed without stopping the program.
 *
 * <p>
 * Example usage:
 * <pre>
 *     new Sun("./data/sun.txt").run();
 * </pre>
 *
 * @see sun.parser.InputParser
 * @see sun.storage.Storage
 * @see sun.task.TaskList
 * @see sun.ui.Ui
 */
public class Sun {
    // Attributes/Fields
    /** Handles saving and loading tasks from disk */
    private Storage storage;

    /** Stores the list of tasks in memory */
    private TaskList tasks;

    /** Handles all input/output with the user */
    private Ui ui = new Ui();;

    // Constructor
    /**
     * Creates a new Sun application instance.
     * <p>
     * Initializes storage at the specified file path and loads tasks
     * from disk. If loading fails due to an {@link IOException}, an
     * empty task list is created.
     *
     * @param filePath the path to the file where tasks are stored
     */
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
    /**
     * Starts the main loop of the application.
     * <p>
     * Displays a welcome message, reads user input continuously, parses
     * and executes commands using {@link InputParser#parseInput(String, TaskList, Storage, Ui)},
     * and prints responses via {@link Ui}.
     * <p>
     * The loop terminates when the user enters a "bye" command.
     * <p>
     * Errors that can occur during command processing include:
     * <ul>
     *     <li>{@link InvalidCommandException}</li>
     *     <li>{@link InvalidTaskNumberException}</li>
     *     <li>{@link InvalidTodoException}</li>
     *     <li>{@link InvalidDeadlineException}</li>
     *     <li>{@link InvalidEventException}</li>
     *     <li>{@link InvalidFindException}</li>
     *     <li>{@link IOException}</li>
     * </ul>
     * These exceptions are caught and displayed without stopping the loop.
     *
     * @see InputParser#parseInput(String, TaskList, Storage, Ui)
     */
    public void run() {
        ui.printWelcome();

        while (true) {
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
                     InvalidEventException | IOException | IllegalArgumentException |
                     InvalidFindException e) {
                ui.printError(e.getMessage());
            }
        }

        ui.printBye();
    }

    // Main
    /**
     * The entry point of the Sun application.
     * <p>
     * Creates a <code>Sun</code> instance with the default data file path
     * ("./data/sun.txt") and starts the main loop.
     *
     * @param args command-line arguments (ignored)
     */
    public static void main(String[] args) {
        new Sun("./data/sun.txt").run();
    }
}
