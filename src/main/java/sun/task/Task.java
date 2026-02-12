package sun.task;

import sun.parser.DateTimeParser;
import java.time.LocalDateTime;

/**
 * Represents a generic task in the Sun application.
 * <p>
 * A <code>Task</code> has a description and a completion status.
 * It serves as a base class for specific task types such as
 * {@link Todo}, {@link Deadline}, and {@link Event}.
 */
public abstract class Task {
    /** The description of the task */
    private String description;
    /** Whether the task is done */
    private boolean isDone;

    /**
     * Creates a new task with the given description.
     * <p>
     * The task is initially marked as not done.
     *
     * @param description the description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the description of the task.
     *
     * @return the task description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns the status of the task as a string.
     * <p>
     * Returns "X" if done, or a single space " " if not done.
     *
     * @return "X" if done, " " otherwise
     */
    public String getIsDoneAsX() {
        return this.isDone ? "X" : " ";
    }

    public boolean getIsDone() {
        return this.isDone;
    }

    /**
     * Sets the completion status of the task.
     *
     * @param isDone true to mark as done, false to mark as not done
     */
    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    /**
     * Returns a string representation of the task for display.
     *
     * @return a formatted string showing the task status and description
     */
    @Override
    public String toString() {
        return String.format("[%s] %s", this.getIsDoneAsX(), this.description);
    }

    /**
     * Returns a string representation of the task for saving to a file.
     *
     * @return a string suitable for file storage
     */
    public abstract String toFileString();

    /**
     * Creates a <code>Task</code> object from a string stored in a file.
     * <p>
     * The string must follow the format:
     * <ul>
     *   <li>Todo: "T | 0 or 1 | description"</li>
     *   <li>Deadline: "D | 0 or 1 | description | byDateTime"</li>
     *   <li>Event: "E | 0 or 1 | description | fromDateTime | toDateTime"</li>
     * </ul>
     * <p>
     * The method parses the string and returns the appropriate subclass
     * of <code>Task</code>.
     *
     * @param line the string representation of a task from a file
     * @return a <code>Task</code> object corresponding to the file data
     * @throws IllegalArgumentException if the task type is unknown
     * @see Todo
     * @see Deadline
     * @see Event
     * @see DateTimeParser#parseDateTime(String)
     */
    public static Task fromFileString(String line) {
        String[] parts = line.split(" \\| ");
        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        switch (type) {
        case "T":
            Todo todo = new Todo(description);
            todo.setIsDone(isDone);;
            return todo;
        case "D":
            String by = parts[3];

            LocalDateTime byDateTime = DateTimeParser.parseDateTime(by);

            Deadline deadline = new Deadline(description, byDateTime);
            deadline.setIsDone(isDone);

            return deadline;
        case "E":
            String from = parts[3];
            String to = parts[4];

            LocalDateTime fromDateTime = DateTimeParser.parseDateTime(from);
            LocalDateTime toDateTime = DateTimeParser.parseDateTime(to);

            Event event = new Event(description, fromDateTime, toDateTime);
            event.setIsDone(isDone);

            return event;
        default:
            throw new IllegalArgumentException(String.format("OOPS!!! Unknown task type: %s", type));
        }
    }

    public abstract Task clone();
}
