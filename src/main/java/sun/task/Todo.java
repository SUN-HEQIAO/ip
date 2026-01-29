package sun.task;

/**
 * Represents a Todo task in the application.
 * <p>
 * A <code>Todo</code> is a type of {@link Task} that only has a description
 * and no associated date or time.
 */
public class Todo extends Task {
    /**
     * Creates a new <code>Todo</code> task with the given description.
     * <p>
     * The task is initially marked as not done.
     *
     * @param description the description of the Todo task
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the string representation of the Todo task for display.
     * <p>
     * The format is: "[T]" followed by the string representation of the
     * parent {@link Task}.
     *
     * @return a formatted string representing the Todo task
     */
    @Override
    public String toString() {
        return String.format("[T]%s", super.toString());
    }

    /**
     * Returns the string representation of the Todo task for saving to a file.
     * <p>
     * The format is: "T | 1 | description" if done, or "T | 0 | description" if not done.
     *
     * @return a formatted string suitable for file storage
     */
    @Override
    public String toFileString() {
        return String.format("T | %d | %s", this.getIsDone().equals("X") ? 1 : 0, this.getDescription());
    }
}
