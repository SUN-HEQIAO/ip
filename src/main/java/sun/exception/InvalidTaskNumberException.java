package sun.exception;

/**
 * Exception thrown when a task number provided by the user is invalid.
 * <p>
 * This exception is used by the {@link sun.task.TaskList} class
 * when a task number is missing, not numerical, or out of range.
 *
 * @see sun.task.TaskList#mark(String, boolean)
 * @see sun.task.TaskList#delete(String)
 */
public class InvalidTaskNumberException extends Exception {
    public InvalidTaskNumberException(String message) { super(message); }
}