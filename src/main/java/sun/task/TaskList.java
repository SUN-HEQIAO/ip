package sun.task;

import sun.exception.InvalidFindException;
import sun.exception.InvalidTaskNumberException;
import sun.exception.InvalidTodoException;
import sun.exception.InvalidDeadlineException;
import sun.exception.InvalidEventException;
import sun.parser.DateTimeParser;
import sun.ui.Ui;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Represents a list of tasks in the Sun application.
 * <p>
 * Provides methods to add, remove, and retrieve tasks, as well as to mark
 * them as done or not done. Also provides specialized methods to add
 * {@link Todo}, {@link Deadline}, and {@link Event} tasks.
 * <p>
 * Handles input validation and prints status messages via {@link Ui}.
 *
 * @see Task
 * @see Todo
 * @see Deadline
 * @see Event
 * @see Ui
 * @see DateTimeParser
 */
public class TaskList {
    /** Internal storage for the tasks */
    private ArrayList<Task> tasks;
    /** UI instance used to print messages */
    private Ui ui = new Ui();

    /**
     * Creates an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a TaskList with an initial list of tasks.
     *
     * @param task an ArrayList of tasks to initialize the list with
     */
    public TaskList(ArrayList<Task> task) {
        this.tasks = task;
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return the size of the task list
     */
    public int sizeTasks() {
        return this.tasks.size();
    }

    /**
     * Returns the task at the specified index.
     *
     * @param index the index of the task (0-based)
     * @return the task at the specified index
     */
    public Task getTask(int index) {
        return this.tasks.get(index);
    }

    /**
     * Returns the internal list of tasks.
     *
     * @return the ArrayList of tasks
     */
    public ArrayList<Task> getTasks() {
        return this.tasks;
    }

    /**
     * Adds a task to the task list.
     *
     * @param task the task to add
     */
    public void addTask(Task task) {
        this.tasks.add(task);
    }

    /**
     * Removes the task at the specified index.
     *
     * @param index the index of the task to remove
     * @return the removed task
     */
    public Task removeTask(int index) {
        return this.tasks.remove(index);
    }

    /**
     * Prints all tasks in the task list to the user.
     */
    public void printTasks() {
        ui.printLine("Here are the tasks in your list:");

        for (int i = 0; i < this.sizeTasks(); i++)  {
            ui.printLine(String.format("%d. %s", i + 1, this.getTask(i)));
        }
    }

    /**
     * Marks or unmarks a task as done.
     * <p>
     * @param rest the task number as a string (1-based)
     * @param isDone true to mark as done, false to mark as not done
     * @throws InvalidTaskNumberException if the task number is missing, invalid, or out of range
     */
    public void mark(String rest, boolean isDone)
            throws InvalidTaskNumberException {
        int index = this.parseTaskNumber(rest);
        Task targetTask = this.getTask(index);
        targetTask.setIsDone(isDone);
        ui.printLine(isDone
                ? "Nice! I've marked this task as done:"
                : "OK, I've marked this task as not done yet:");
        ui.printLine(targetTask.toString());
    }

    public Task markAndReturn(String rest, boolean isDone)
            throws InvalidTaskNumberException {
        int index = this.parseTaskNumber(rest);
        Task targetTask = this.getTask(index);
        targetTask.setIsDone(isDone);
        return targetTask;
    }

    /**
     * Adds a Todo task to the task list.
     * <p>
     * @param rest the description of the Todo
     * @throws InvalidTodoException if the description is empty
     * @see Todo
     */
    public void addTodo(String rest)
            throws InvalidTodoException {
        // todoTask with no description
        if (rest.isEmpty()) {
            throw new InvalidTodoException("OOPS!!! The description of a todo cannot be empty.");
        }

        Task todoTask = new Todo(rest);
        this.addTask(todoTask);

        printTaskAdded(todoTask);
    }

    public Task addTodoAndReturn(String rest)
            throws InvalidTodoException {
        if (rest.isEmpty()) {
            throw new InvalidTodoException("OOPS!!! The description of a todo cannot be empty.");
        }

        Task todoTask = new Todo(rest);
        this.addTask(todoTask);
        return todoTask;
    }

    /**
     * Adds a Deadline task to the task list.
     * <p>
     * @param rest the description and due date in the format "desc /by date"
     * @throws InvalidDeadlineException if description or date is empty
     * @see Deadline
     * @see DateTimeParser#parseDateTime(String)
     */
    public void addDeadline(String rest)
            throws InvalidDeadlineException {
        String[] parts = rest.split(" /by ", 2);

        // Deadline with no description or due-date
        if (parts.length < 2 || parts[0].isEmpty() || parts[1].isEmpty()) {
            throw new InvalidDeadlineException("OOPS!!! The description or due date of a deadline cannot be empty.");
        }

        String description = parts[0];
        String byString = parts[1];

        LocalDateTime byDateTime = DateTimeParser.parseDateTime(byString);
        Task deadlineTask = new Deadline(description, byDateTime);
        this.addTask(deadlineTask);

        printTaskAdded(deadlineTask);
    }

    public Task addDeadlineAndReturn(String rest)
            throws InvalidDeadlineException {
        String[] parts = rest.split(" /by ", 2);

        if (parts.length < 2 || parts[0].isEmpty() || parts[1].isEmpty()) {
            throw new InvalidDeadlineException("OOPS!!! The description or due date of a deadline cannot be empty.");
        }

        String description = parts[0];
        String byString = parts[1];

        LocalDateTime byDateTime = DateTimeParser.parseDateTime(byString);
        Task deadlineTask = new Deadline(description, byDateTime);
        this.addTask(deadlineTask);

        return deadlineTask;
    }

    /**
     * Adds an Event task to the task list.
     * <p>
     * @param rest the description and times in the format "desc /from start /to end"
     * @throws InvalidEventException if description, start, or end times are empty
     * @see Event
     * @see DateTimeParser#parseDateTime(String)
     */
    public void addEvent(String rest)
            throws InvalidEventException {
        String[] descriptionSplit = rest.split(" /from ", 2);

        // Event with no description or start
        if (descriptionSplit.length < 2 || descriptionSplit[0].isEmpty() || descriptionSplit[1].isEmpty()) {
            throw new InvalidEventException("OOPS!!! The description or start time of an event cannot be empty.");
        }
        String description = descriptionSplit[0];

        String[] fromSplit = descriptionSplit[1].split(" /to ", 2);

        // Event with no start or end
        if (fromSplit.length < 2 || fromSplit[0].isEmpty() || fromSplit[1].isEmpty()) {
            throw new InvalidEventException("OOPS!!! The start or end time of an event cannot be empty.");
        }

        String from = fromSplit[0];
        String to = fromSplit[1];

        LocalDateTime fromDateTime = DateTimeParser.parseDateTime(from);
        LocalDateTime toDateTime = DateTimeParser.parseDateTime(to);
        Task eventTask = new Event(description, fromDateTime, toDateTime);
        this.addTask(eventTask);

        printTaskAdded(eventTask);
    }

    public Task addEventAndReturn(String rest)
            throws InvalidEventException {
        String[] descriptionSplit = rest.split(" /from ", 2);

        // Event with no description or start
        if (descriptionSplit.length < 2 || descriptionSplit[0].isEmpty() || descriptionSplit[1].isEmpty()) {
            throw new InvalidEventException("OOPS!!! The description or start time of an event cannot be empty.");
        }
        String description = descriptionSplit[0];

        String[] fromSplit = descriptionSplit[1].split(" /to ", 2);

        // Event with no start or end
        if (fromSplit.length < 2 || fromSplit[0].isEmpty() || fromSplit[1].isEmpty()) {
            throw new InvalidEventException("OOPS!!! The start or end time of an event cannot be empty.");
        }

        String from = fromSplit[0];
        String to = fromSplit[1];

        LocalDateTime fromDateTime = DateTimeParser.parseDateTime(from);
        LocalDateTime toDateTime = DateTimeParser.parseDateTime(to);
        Task eventTask = new Event(description, fromDateTime, toDateTime);
        this.addTask(eventTask);

        return eventTask;
    }

    /**
     * Deletes a task from the task list by its number.
     * <p>
     * @param rest the task number as a string (1-based)
     * @throws InvalidTaskNumberException if the task number is missing, invalid, or out of range
     */
    public void delete(String rest)
            throws InvalidTaskNumberException {
        int index = parseTaskNumber(rest);
        Task targetTask = this.getTask(index);
        this.removeTask(index);

        ui.printLine("Noted. I've removed this task:");
        ui.printLine(targetTask.toString());
        ui.printLine(String.format("Now you have %d tasks left", this.sizeTasks()));
    }

    public Task deleteAndReturn(String rest)
            throws InvalidTaskNumberException {
        int index = this.parseTaskNumber(rest);
        Task targetTask = this.getTask(index);
        this.removeTask(index);

        return targetTask;
    }

    /**
     * Searches the task list for tasks whose descriptions contain the given keyword.
     * <p>
     * The search is case-insensitive and prints all matching tasks in the order they appear
     * in the list. If no tasks match, a message is displayed indicating no matches were found.
     * <p>
     * @param rest the keyword or phrase to search for in task descriptions
     * @throws InvalidFindException if the search keyword is empty
     * @see sun.task.Task#getDescription()
     * @see sun.ui.Ui#printLine(String)
     */
    public void find(String rest)
            throws InvalidFindException {
        if (rest.isEmpty()) {
            throw new InvalidFindException("OOPS!!! Please provide task to find.");
        }

        boolean found = false;
        ui.printLine("Here are the matching tasks in your list:");

        for (int i = 0; i < this.sizeTasks(); i++) {
            Task targetTask = this.getTask(i);
            if (targetTask.getDescription().toLowerCase().contains(rest.toLowerCase())) {
                ui.printLine(String.format("%d. %s", i + 1, targetTask));
                found = true;
            }
        }

        if (!found) {
            ui.printLine("No matching tasks found.");
        }
    }

    public ArrayList<Task> findAndReturn(String rest)
            throws InvalidFindException {
        if (rest.isEmpty()) {
            throw new InvalidFindException("OOPS!!! Please provide task to find.");
        }

        ArrayList<Task> matches = new ArrayList<>();
        for (Task task : this.getTasks()) {
            if (task.getDescription().toLowerCase().contains(rest.toLowerCase())) {
                matches.add(task);
            }
        }
        return matches;
    }



    //Helper method
    /**
     * Parses a task number from a string.
     * <p>
     * @param rest the task number string (1-based)
     * @return the zero-based index of the task
     * @throws InvalidTaskNumberException if the task number is missing, invalid, or out of range
     */
    private int parseTaskNumber(String rest)
            throws InvalidTaskNumberException {
        // No Task Number
        if (rest.isEmpty()) {
            throw new InvalidTaskNumberException("OOPS!!! Task number missing.");
        }

        // Task Number not numerical
        int index;
        try {
            index = Integer.parseInt(rest) - 1;
        } catch (NumberFormatException e) {
            throw new InvalidTaskNumberException("OOPS!!! Please enter a valid numerical task number.");
        }

        // Task Number out of range
        if (index < 0 || index >= this.sizeTasks()) {
            throw new InvalidTaskNumberException("OOPS!!! Task number out of range.");
        }

        return index;
    }

    //Helper method
    /**
     * Prints the task that has just been added.
     * <p>
     * @param task the task that was added
     */
    private void printTaskAdded(Task task) {
        ui.printLine("Got it. I've added this task:");
        ui.printLine(task.toString());
        ui.printLine("Now you have " + this.sizeTasks() + " tasks in the list.");
    }

}
