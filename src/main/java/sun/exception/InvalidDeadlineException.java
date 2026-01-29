package sun.exception;


/**
 * Exception thrown when a deadline task is invalid.
 * <p>
 * This exception is used by the {@link sun.task.TaskList} class
 * when adding a deadline fails due to a missing description or due date.
 *
 * @see sun.task.TaskList#addDeadline(String)
 */
public class InvalidDeadlineException extends Exception {
    public InvalidDeadlineException(String message) { super(message); }
}