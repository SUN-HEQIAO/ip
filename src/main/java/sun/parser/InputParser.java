package sun.parser;

public class InputParser {

    public static boolean isBye(String input) {
        return input.equalsIgnoreCase("bye");
    }

    public static String normalizeInput(String input) {
        return input.trim().replaceAll("\\s+", " ");
    }

    public static String[] splitInput(String input) {
        String[] inputs = input.split(" ", 2);
        String command = inputs[0].toLowerCase();
        String rest = (inputs.length > 1) ? inputs[1] : "";

        return new String[] { command, rest };
    }
}
