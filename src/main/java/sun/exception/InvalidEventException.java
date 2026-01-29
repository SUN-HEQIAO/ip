package sun.exception;


/**
 * Exception thrown when an event task is invalid.
 * <p>
 * This exception is used by the {@link sun.task.TaskList} class
 * when adding an event fails due to a missing description, start time, or end time.
 *
 * @see sun.task.TaskList#addEvent(String)
 */
public class InvalidEventException extends Exception {
    public InvalidEventException(String message) {
        super(message);
    }
}