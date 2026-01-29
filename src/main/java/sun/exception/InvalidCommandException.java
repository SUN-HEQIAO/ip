package sun.exception;

/**
 * Exception thrown when the user enters an unknown or invalid command.
 * <p>
 * This exception is used by the {@link sun.parser.InputParser} class
 * to indicate that the input command does not match any supported commands.
 *
 * @see sun.parser.InputParser
 */
public class InvalidCommandException extends Exception {
    /**
     * Constructs a new <code>InvalidCommandException</code> with the specified detail message.
     *
     * @param message the detail message explaining why the command is invalid
     */
    public InvalidCommandException(String message) {
        super(message);
    }
}