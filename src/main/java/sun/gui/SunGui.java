package sun.gui;

import java.io.IOException;
import java.util.ArrayList;

import sun.Sun;
import sun.exception.InvalidCommandException;
import sun.exception.InvalidDeadlineException;
import sun.exception.InvalidEventException;
import sun.exception.InvalidFindException;
import sun.exception.InvalidTaskNumberException;
import sun.exception.InvalidTodoException;
import sun.parser.InputParser;
import sun.task.Task;

/**
 * Handles GUI interactions by processing user input, delegating commands to
 * the {@link Sun} instance, and returning formatted response strings for display.
 * <p>
 * Acts as the bridge between the JavaFX UI and the core application logic.
 */
public class SunGui {
    private Sun sun;

    /**
     * Constructs a {@code SunGui} with the default storage file path.
     * <p>
     * Initializes the underlying {@link Sun} instance responsible for task
     * management and persistence.
     */
    public SunGui() {
        this.sun = new Sun("./data/sun.txt");
    }

    /**
     * Processes the given user input and returns the corresponding response string.
     * <p>
     * The input is normalized and parsed into a command and arguments,
     * executed via the {@link Sun} instance, and any resulting message or
     * exception message is returned for display.
     *
     * @param input Raw user input from the GUI.
     * @return A response message to be displayed in the GUI.
     */
    public String getResponse(String input) {
        String normalizedInput = InputParser.normalizeInput(input);

        String[] splits = InputParser.splitInput(normalizedInput);
        String command = splits[0];
        String rest = splits[1];

        try {
            switch (command) {
            case "list":
                return taskListString(sun.getTasks().listTasks());

            case "mark":
                Task markTask = sun.getTasks().mark(rest, true);
                sun.getStorage().save(sun.getTasks());
                return taskMarkedString(markTask, true);

            case "unmark":
                Task unmarkTask = sun.getTasks().mark(rest, false);
                sun.getStorage().save(sun.getTasks());
                return taskMarkedString(unmarkTask, false);

            case "todo":
                Task todoTask = sun.getTasks().addTodo(rest);
                sun.getStorage().save(sun.getTasks());
                return taskAddedString(todoTask);

            case "deadline":
                Task deadlineTask = sun.getTasks().addDeadline(rest);
                sun.getStorage().save(sun.getTasks());
                return taskAddedString(deadlineTask);

            case "event":
                Task eventTask = sun.getTasks().addEvent(rest);
                sun.getStorage().save(sun.getTasks());
                return taskAddedString(eventTask);

            case "delete":
                Task deleteTask = sun.getTasks().delete(rest);
                sun.getStorage().save(sun.getTasks());
                return taskRemoveString(deleteTask);

            case "find":
                return taskFoundString(sun.getTasks().find(rest));

            case "undo":
                sun.getTasks().undo();
                sun.getStorage().save(sun.getTasks());
                return "Undo Success!";

            case "bye":
                return "BYE_SIGNAL";

            default:
                throw new InvalidCommandException("OOPS!!! I'm sorry, but I don't know what that command means :-(");
            }
        } catch (InvalidTodoException | InvalidDeadlineException | InvalidEventException |
                 InvalidTaskNumberException | InvalidFindException | IOException |
                 IllegalStateException | IllegalArgumentException | InvalidCommandException e) {
            return e.getMessage();
        }
    }




    // Helper Method
    /**
     * Formats a list of tasks into a numbered string for display.
     * <p>
     * Returns a message indicating no tasks if the list is empty.
     *
     * @param tasks The list of tasks to format.
     * @return A formatted string representation of the tasks.
     */
    private String taskListString(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            return "There are no tasks yet.";
        }
        StringBuilder builder = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            builder.append(String.format("%d. %s\n", i + 1, tasks.get(i)));
        }
        return builder.toString();
    }

    // Helper Method
    /**
     * Returns a confirmation message after marking or unmarking a task.
     * <p>
     * The message differs depending on whether the task is marked as done
     * or not done.
     *
     * @param task The task that was updated.
     * @param isDone {@code true} if marked done, {@code false} otherwise.
     * @return A formatted confirmation message.
     */
    private String taskMarkedString(Task task, boolean isDone) {
        String status = isDone
                ? "Nice! I've marked this task as done:"
                : "OK, I've marked this task as not done yet:";
        return String.format("""
                %s
                %s
                """, status, task);
    }

    // Helper Method
    /**
     * Returns a confirmation message after adding a task.
     * <p>
     * Includes the updated number of tasks currently in the list.
     *
     * @param task The task that was added.
     * @return A formatted confirmation message.
     */
    public String taskAddedString(Task task) {
        return String.format("""
                                Noted. I've added this task:
                                %s
                                Now you have %d tasks in the list.
                                """, task, sun.getTasks().size());
    }

    // Helper Method
    /**
     * Returns a confirmation message after deleting a task.
     * <p>
     * Includes the updated number of tasks remaining in the list.
     *
     * @param task The task that was removed.
     * @return A formatted confirmation message.
     */
    private String taskRemoveString(Task task) {
        return String.format("""
                Noted. I've removed this task:
                %s
                Now you have %d tasks in the list.
                """, task, sun.getTasks().size());
    }

    // Helper Method
    /**
     * Formats a list of matching tasks into a numbered string for display.
     * <p>
     * Returns a message indicating no matches if the list is empty.
     *
     * @param matches The list of matching tasks.
     * @return A formatted string representation of the matching tasks.
     */
    private String taskFoundString(ArrayList<Task> matches) {
        if (matches.isEmpty()) {
            return "No matching tasks found.";
        }
        StringBuilder builder = new StringBuilder("Here are the matching tasks in your list:\n");
        for (int i = 0; i < matches.size(); i++) {
            builder.append(String.format("%d. %s\n", i + 1, matches.get(i)));
        }
        return builder.toString();
    }
}

