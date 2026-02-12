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
        System.out.println("Hello! I'm Sun.");
        System.out.println("What can I do for you?");
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

    public void printTaskAdded(Task task, int totalTasks) {
        printLine("Got it. I've added this task:");
        printLine(task.toString());
        printLine("Now you have " + totalTasks + " tasks in the list.");
    }

    public void printTaskList(ArrayList<Task> tasks) {
        printLine("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            printLine(String.format("%d. %s", i + 1, tasks.get(i)));
        }
    }

    public void printFoundTaskList(ArrayList<Task> tasks) {
        printLine("Here are the matching tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            printLine(String.format("%d. %s", i + 1, tasks.get(i)));
        }
    }

    public void printMarkedMessage(Task task, boolean isDone) {
        printLine(isDone ? "Nice! I've marked this task as done:" : "OK, I've marked this task as not done yet:");
        printLine(task.toString());
    }

    public void printTaskRemoved(Task targetTask, int totalTasks) {
        printLine("Noted. I've removed this task:");
        printLine(targetTask.toString());
        printLine(String.format("Now you have %d tasks left", totalTasks));
    }
}
