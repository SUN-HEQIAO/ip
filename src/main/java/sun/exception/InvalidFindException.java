package sun.exception;

/**
 * Exception thrown when a find command is invalid.
 * <p>
 * This exception is used by the {@link sun.task.TaskList#find(String)} method
 * when the search keyword is missing or empty.
 *
 * @see sun.task.TaskList#find(String)
 */
public class InvalidFindException extends Exception {
    public InvalidFindException(String message) {
        super(message);
    }
}
