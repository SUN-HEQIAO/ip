package sun.parser;

/**
 * Provides utility methods for parsing and normalizing raw user input.
 * <p>
 * Responsible for extracting commands and arguments from input strings
 * before they are processed by the application logic.
 */
public class InputParser {

    /**
     * Checks whether the given input represents the exit command.
     * <p>
     * Comparison is case-insensitive.
     *
     * @param input The raw user input.
     * @return {@code true} if the input is "bye", {@code false} otherwise.
     */
    public static boolean isBye(String input) {
        return input.equalsIgnoreCase("bye");
    }

    /**
     * Normalizes the given input string.
     * <p>
     * Trims leading and trailing whitespace and replaces multiple
     * consecutive spaces with a single space.
     *
     * @param input The raw user input.
     * @return A normalized version of the input.
     */
    public static String normalizeInput(String input) {
        return input.trim().replaceAll("\\s+", " ");
    }

    /**
     * Splits the input into a command and its remaining arguments.
     * <p>
     * The first word is treated as the command (converted to lowercase),
     * and the remainder of the string is returned as arguments.
     *
     * @param input The normalized user input.
     * @return A {@code String[]} where index 0 is the command and index 1 is the remaining input.
     */
    public static String[] splitInput(String input) {
        String[] inputs = input.split(" ", 2);
        String command = inputs[0].toLowerCase();
        String rest = (inputs.length > 1) ? inputs[1] : "";

        return new String[] { command, rest };
    }
}
