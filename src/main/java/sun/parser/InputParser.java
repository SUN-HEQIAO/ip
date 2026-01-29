package sun.parser;

import sun.exception.InvalidCommandException;
import sun.exception.InvalidFindException;
import sun.exception.InvalidTaskNumberException;
import sun.exception.InvalidTodoException;
import sun.exception.InvalidDeadlineException;
import sun.exception.InvalidEventException;
import sun.storage.Storage;
import sun.task.TaskList;
import sun.ui.Ui;

import java.io.IOException;

/**
 * Handles parsing and execution of user commands in the Sun application.
 * <p>
 * The <code>InputParser</code> interprets raw user input, determines
 * the command type, and executes the corresponding action on
 * {@link TaskList}, {@link Storage}, and {@link Ui}.
 * <p>
 * Commands include task management operations such as adding todos,
 * deadlines, events, marking/unmarking tasks, deleting tasks, and listing tasks.
 *
 * @see TaskList
 * @see Storage
 * @see Ui
 */
public class InputParser {
    /**
     * Checks whether the given input is the "bye" command.
     * <p>
     * This method is used to determine when the main loop should terminate.
     *
     * @param input the user input string
     * @return true if the input is "bye" (case-insensitive), false otherwise
     */
    public static boolean isBye(String input) {
        return input.equalsIgnoreCase("bye");
    }

    /**
     * Parses and executes a user command.
     * <p>
     * The method first normalizes whitespace, splits the input into
     * the command and arguments, and performs the corresponding action.
     * After modifying tasks, changes are saved to {@link Storage}.
     * <p>
     * Supported commands include:
     * <ul>
     *     <li>list</li>
     *     <li>mark &lt;task number&gt;</li>
     *     <li>unmark &lt;task number&gt;</li>
     *     <li>todo &lt;description&gt;</li>
     *     <li>deadline &lt;description /by dateTime&gt;</li>
     *     <li>event &lt;description /from startTime /to endTime&gt;</li>
     *     <li>delete &lt;task number&gt;</li>
     *     <li>find &lt;description&gt;</li>
     * </ul>
     *
     * @param input the raw user input string
     * @param tasks the {@link TaskList} to operate on
     * @param storage the {@link Storage} object for saving changes
     * @param ui the {@link Ui} object for printing responses
     * @throws InvalidCommandException if the command is unknown
     * @throws InvalidTodoException if a todo task is invalid
     * @throws InvalidDeadlineException if a deadline task is invalid
     * @throws InvalidEventException if an event task is invalid
     * @throws InvalidTaskNumberException if a task number is missing, invalid, or out of range
     * @throws IOException if saving to storage fails
     * @throws IllegalArgumentException if input parsing encounters an unexpected error
     * @throws InvalidFindException if a task to find is invalid
     */
    public static void parseInput(String input, TaskList tasks, Storage storage, Ui ui)
            throws InvalidCommandException, InvalidTodoException, InvalidDeadlineException,
            InvalidEventException, InvalidTaskNumberException, IOException,
            IllegalArgumentException, InvalidFindException {

        // Clean up user inputs
        input = input.trim().replaceAll("\\s+", " ");

        // Only split into 2 parts first
        String[] inputs = input.split(" ", 2);
        String command = inputs[0].toLowerCase();
        String rest = (inputs.length > 1) ? inputs[1] : "";

        switch (command) {
        case "list":
            tasks.printTasks();
            break;

        case "mark":
            tasks.mark(rest, true);
            storage.save(tasks);
            break;

        case "unmark":
            tasks.mark(rest, false);
            storage.save(tasks);
            break;

        case "todo":
            tasks.addTodo(rest);
            storage.save(tasks);
            break;

        case "deadline":
            tasks.addDeadline(rest);
            storage.save(tasks);
            break;

        case "event":
            tasks.addEvent(rest);
            storage.save(tasks);
            break;

        case "delete":
            tasks.delete(rest);
            storage.save(tasks);
            break;

        case "find":
            tasks.find(rest);
            break;

        default:
            throw new InvalidCommandException("OOPS!!! I'm sorry, but I don't know what that command means :-(");
        }
    }
}
