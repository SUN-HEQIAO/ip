package sun.task;

import sun.exception.InvalidFindException;
import sun.exception.InvalidTaskNumberException;
import sun.exception.InvalidTodoException;
import sun.exception.InvalidDeadlineException;
import sun.exception.InvalidEventException;
import sun.parser.DateTimeParser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Represents a list of tasks and provides operations to manage them,
 * including adding, deleting, marking, finding, and undoing changes.
 * <p>
 * Maintains a history stack to support undo functionality by storing
 * snapshots of previous task list states.
 */
public class TaskList {
    private ArrayList<Task> tasks;
    private Stack<ArrayList<Task>> history;

    /**
     * Constructs an empty {@code TaskList}.
     * <p>
     * Initializes the internal task storage and history stack.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
        assert this.tasks != null : "Task list should be initialised";

        this.history = new Stack<>();
    }

    /**
     * Constructs a {@code TaskList} with an existing list of tasks.
     * <p>
     * The provided list must not be null and becomes the internal task storage.
     *
     * @param task The initial list of tasks.
     */
    public TaskList(ArrayList<Task> task) {
        assert task != null : "Task list passed in should not be null";
        this.tasks = task;
        this.history = new Stack<>();
    }

    /**
     * Returns the number of tasks in the list.
     * <p>
     * The value reflects the current state of the task collection.
     *
     * @return The number of tasks.
     */
    public int size() {
        return this.tasks.size();
    }

    /**
     * Returns the underlying list of tasks.
     * <p>
     * Modifications to the returned list affect the internal state.
     *
     * @return The list of tasks.
     */
    public ArrayList<Task> getTasks() {
        return this.tasks;
    }

    /**
     * Returns all tasks currently stored.
     * <p>
     * The returned list represents the current state of the task collection.
     *
     * @return A list of all tasks.
     */
    public ArrayList<Task> listTasks() {
        ArrayList<Task> allTasks = this.tasks;

        return allTasks;
    }

    /**
     * Marks or unmarks a task as done.
     * <p>
     * Saves the current state before updating the task to allow undo.
     *
     * @param rest The task number as a string.
     * @param isDone Whether the task should be marked as done.
     * @return The updated task.
     * @throws InvalidTaskNumberException If the task number is invalid.
     */
    public Task mark(String rest, boolean isDone) throws InvalidTaskNumberException {
        int index = this.parseTaskNumber(rest);
        assert index >= 0 && index < this.size() : "Parsed task index should be valid";

        Task targetTask = tasks.get(index);
        assert targetTask != null : "Target task should exist";

        saveState();

        targetTask.setIsDone(isDone);

        return targetTask;
    }

    /**
     * Adds a new todo task to the list.
     * <p>
     * Saves the current state before adding the task to allow undo.
     *
     * @param rest The task description.
     * @return The newly created todo task.
     * @throws InvalidTodoException If the description is empty.
     */
    public Task addTodo(String rest) throws InvalidTodoException {
        if (rest.isEmpty()) {
            throw new InvalidTodoException("OOPS!!! The description of a todo cannot be empty.");
        }

        int oldSize = this.size();

        saveState();

        Task todoTask = new Todo(rest);
        tasks.add(todoTask);

        assert this.size() == oldSize + 1 : "Task list should increase by 1 after adding a todo task";

        return todoTask;
    }

    /**
     * Adds a new deadline task to the list.
     * <p>
     * Parses the description and due date, then saves state before insertion.
     *
     * @param rest The raw input containing description and deadline.
     * @return The newly created deadline task.
     * @throws InvalidDeadlineException If the input format is invalid.
     */
    public Task addDeadline(String rest) throws InvalidDeadlineException {
        String[] parts = rest.split(" /by ", 2);

        validateDeadlineParts(parts);

        String description = parts[0];
        String by = parts[1];

        LocalDateTime byDateTime = DateTimeParser.parseDateTime(by);

        saveState();

        Task deadlineTask = new Deadline(description, byDateTime);
        tasks.add(deadlineTask);

        return deadlineTask;
    }

    /**
     * Adds a new event task to the list.
     * <p>
     * Parses the description and time range, then saves state before insertion.
     *
     * @param rest The raw input containing description and event times.
     * @return The newly created event task.
     * @throws InvalidEventException If the input format is invalid.
     */
    public Task addEvent(String rest) throws InvalidEventException {
        String[] descriptionSplit = rest.split(" /from ", 2);
        validateEventDescription(descriptionSplit);

        String description = descriptionSplit[0];

        String[] timeSplit = descriptionSplit[1].split(" /to ", 2);
        validateEventTimes(timeSplit);

        String from = timeSplit[0];
        String to = timeSplit[1];

        LocalDateTime fromDateTime = DateTimeParser.parseDateTime(from);
        LocalDateTime toDateTime = DateTimeParser.parseDateTime(to);

        saveState();

        Task eventTask = new Event(description, fromDateTime, toDateTime);
        tasks.add(eventTask);

        return eventTask;
    }

    /**
     * Deletes a task from the list.
     * <p>
     * Saves the current state before removal to allow undo.
     *
     * @param rest The task number as a string.
     * @return The removed task.
     * @throws InvalidTaskNumberException If the task number is invalid.
     */
    public Task delete(String rest) throws InvalidTaskNumberException {
        int oldSize = this.size();

        int index = this.parseTaskNumber(rest);
        Task targetTask = tasks.get(index);

        saveState();

        tasks.remove(index);

        assert this.size() == oldSize - 1 : "Task list size should decrease by 1 after deletion";

        return targetTask;
    }

    /**
     * Finds tasks whose descriptions contain the given keyword.
     * <p>
     * Matching is case-insensitive.
     *
     * @param rest The keyword to search for.
     * @return A list of matching tasks.
     * @throws InvalidFindException If the search keyword is empty.
     */
    public ArrayList<Task> find(String rest) throws InvalidFindException {
        if (rest.isEmpty()) {
            throw new InvalidFindException("OOPS!!! Please provide task to find.");
        }

        ArrayList<Task> matches = new ArrayList<>();
        for (Task task : this.tasks) {
            if (task.getDescription().toLowerCase().contains(rest.toLowerCase())) {
                matches.add(task);
            }
        }

        return matches;
    }

    /**
     * Restores the previous state of the task list.
     * <p>
     * Replaces the current task list with the most recent snapshot.
     *
     * @throws IllegalStateException If there is no previous state to restore.
     */
    public void undo() throws IllegalStateException{
        if (history.isEmpty()) {
            throw new IllegalStateException("Nothing to undo.");
        }

        this.tasks = history.pop();
    }




    //Helper method
    /**
     * Parses and validates a task number from input.
     * <p>
     * Converts the input to a zero-based index and checks bounds.
     *
     * @param rest The task number as a string.
     * @return The zero-based task index.
     * @throws InvalidTaskNumberException If the number is missing, invalid, or out of range.
     */
    private int parseTaskNumber(String rest) throws InvalidTaskNumberException {
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
        if (index < 0 || index >= tasks.size()) {
            throw new InvalidTaskNumberException("OOPS!!! Task number out of range.");
        }

        return index;
    }

    //Helper method
    /**
     * Validates the description and start time components of an event.
     * <p>
     * Ensures required fields are present and non-empty.
     *
     * @param parts The split input components.
     * @throws InvalidEventException If validation fails.
     */
    private void validateEventDescription(String[] parts) throws InvalidEventException {
        if (parts.length < 2 || parts[0].isEmpty() || parts[1].isEmpty()) {
            throw new InvalidEventException(
                    "OOPS!!! The description or start time of an event cannot be empty.");
        }
    }

    //Helper method
    /**
     * Validates the start and end time components of an event.
     * <p>
     * Ensures required time fields are present and non-empty.
     *
     * @param parts The split time components.
     * @throws InvalidEventException If validation fails.
     */
    private void validateEventTimes(String[] parts) throws InvalidEventException {
        if (parts.length < 2 || parts[0].isEmpty() || parts[1].isEmpty()) {
            throw new InvalidEventException(
                    "OOPS!!! The start or end time of an event cannot be empty.");
        }
    }

    //Helper method
    /**
     * Validates the description and due date components of a deadline.
     * <p>
     * Ensures required fields are present and non-empty.
     *
     * @param parts The split input components.
     * @throws InvalidDeadlineException If validation fails.
     */
    private void validateDeadlineParts(String[] parts) throws InvalidDeadlineException {
        if (parts.length < 2 || parts[0].isEmpty() || parts[1].isEmpty()) {
            throw new InvalidDeadlineException(
                    "OOPS!!! The description or due date of a deadline cannot be empty.");
        }
    }

    //Helper method
    /**
     * Saves a snapshot of the current task list for undo support.
     * <p>
     * Creates a deep copy of all tasks and pushes it onto the history stack.
     */
    private void saveState() {
        // Deep Copy
        ArrayList<Task> snapshot = new ArrayList<>();
        for (Task task : this.tasks) {
            snapshot.add(task.clone());
        }
        //You push a NEW ArrayList BASED ON the old ArrayList
        history.push(snapshot);
    }
}
