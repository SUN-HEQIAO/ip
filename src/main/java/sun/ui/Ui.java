// The UI class will take in user inputs, show user inputs, show errors.
package sun.ui;

import java.util.ArrayList;
import java.util.Scanner;

import sun.task.Task;

/**
 * Handles all input and output interactions with the user.
 * <p>
 * The <code>Ui</code> class is responsible for reading user input,
 * displaying messages, printing errors, and showing welcome/exit messages.
 * <p>
 * It wraps a <code>{@link Scanner}</code> for reading input from
 * <code>{@link System#in}</code>.
 *
 * @see sun.Sun
 * @see sun.task.TaskList
 * @see sun.parser.InputParser
 */
public class Ui {
    /** Scanner used to read input from the user */
    private Scanner scanner;

    /**
     * Creates a new <code>Ui</code> instance.
     * <p>
     * Initializes the internal <code>{@link Scanner}</code> for reading input from the console.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Prints the welcome message when the application starts.
     * <p>
     * Displays a greeting and asks the user for input.
     */
    public void printWelcome() {
        printLine("Hello! I'm Sun.");
        printLine("What can I do for you?");
        printLine("");
        printLine("Psst... need help getting started?");
        printLine("Type \"help\".");
    }

    /**
     * Reads a line of input from the user.
     * <p>
     * Waits for the user to enter a line and returns it.
     * <p>
     * @return the input line as a <code>String</code>
     */
    public String readLine() {
        return scanner.nextLine();
    }

    /**
     * Prints a message to the console.
     * <p>
     * @param message the message to print
     */
    public void printLine(String message) {
        System.out.println(message);
    }

    /**
     * Prints an error message to the console.
     * <p>
     * @param message the error message to print
     */
    public void printError(String message) {
        System.out.println(message);
    }

    /**
     * Prints the exit message when the application terminates.
     * <p>
     * Displays a goodbye message to the user.
     */
    public void printBye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Prints a confirmation message when a new task is successfully added.
     *
     * @param task The task that was added to the list
     * @param totalTasks The updated total number of tasks in the list
     */
    public void printTaskAdded(Task task, int totalTasks) {
        printLine("Got it. I've added this task:");
        printLine(task.toString());
        printLine("Now you have " + totalTasks + " tasks in the list.");
    }

    /**
     * Displays all tasks in the task list with their corresponding index numbers.
     * Each task is shown in the format: "index. [task details]"
     *
     * @param tasks The ArrayList containing all tasks to be displayed
     */
    public void printTaskList(ArrayList<Task> tasks) {
        printLine("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            printLine(String.format("%d. %s", i + 1, tasks.get(i)));
        }
    }

    /**
     * Displays the list of tasks that match a search query.
     * Each matching task is shown with its index number from the filtered list.
     *
     * @param tasks The ArrayList containing tasks that matched the search criteria
     */
    public void printFoundTaskList(ArrayList<Task> tasks) {
        printLine("Here are the matching tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            printLine(String.format("%d. %s", i + 1, tasks.get(i)));
        }
    }

    /**
     * Prints a confirmation message when a task's completion status is toggled.
     * The message varies based on whether the task was marked as done or not done.
     *
     * @param task The task whose status was changed
     * @param isDone {@code true} if the task was marked as completed,
     *               {@code false} if it was marked as not completed
     */
    public void printMarkedMessage(Task task, boolean isDone) {
        printLine(isDone ? "Nice! I've marked this task as done:" : "OK, I've marked this task as not done yet:");
        printLine(task.toString());
    }

    /**
     * Prints a confirmation message when a task is successfully deleted from the list.
     *
     * @param targetTask The task that was removed from the list
     * @param totalTasks The updated total number of tasks remaining in the list
     */
    public void printTaskRemoved(Task targetTask, int totalTasks) {
        printLine("Noted. I've removed this task:");
        printLine(targetTask.toString());
        printLine(String.format("Now you have %d tasks left", totalTasks));
    }

    /**
     * Displays the help menu containing all available commands and their usage.
     * The help information is organized into categories:
     * <ul>
     *   <li>Adding tasks (todo, deadline, event) with date/time format specifications</li>
     *   <li>Viewing tasks (list, find)</li>
     *   <li>Managing tasks (mark, unmark, delete)</li>
     *   <li>Other commands (undo, help, bye)</li>
     * </ul>
     */
    public void printHelp() {
        printLine(
                """
                Here are the available commands:
                
                1. Adding tasks:
                   * todo <description>
                   * deadline <description> /by <end time>
                   * event <description> /from <start time> /to <end time>
                   (Date/time formats: yyyy-MM-dd HHmm | yyyy-MM-dd | HHmm)
                
                2. Viewing tasks:
                   * list
                   * find <keyword>
                
                3. Managing tasks:
                   * mark <task number>
                   * unmark <task number>
                   * delete <task number>
                
                4. Other commands:
                   * undo
                   * help
                   * bye
                """
        );
    }
}
