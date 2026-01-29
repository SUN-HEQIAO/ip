package sun.exception;

/**
 * Exception thrown when a todo task is invalid.
 * <p>
 * This exception is used by the {@link sun.task.TaskList} class
 * when adding a todo fails due to a missing description.
 *
 * @see sun.task.TaskList#addTodo(String)
 */
public class InvalidTodoException extends Exception {
    public InvalidTodoException(String message) { super(message); }
}