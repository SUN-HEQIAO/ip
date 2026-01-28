import java.io.IOException;

public class Parser {
    public static boolean isBye(String input) {
        return input.equalsIgnoreCase("bye");
    }

    public static void parseInputs(String input, TaskList tasks, Storage storage, Ui ui)
            throws InvalidCommandException, InvalidTodoException, InvalidDeadlineException,
            InvalidEventException, InvalidTaskNumberException, IOException, IllegalArgumentException {

        // Clean up user inputs
        input = input.trim().replaceAll("\\s+", " ");

        // Only split into 2 parts first
        String[] inputs = input.split(" ", 2);
        String command = inputs[0].toLowerCase();
        String rest = (inputs.length > 1) ? inputs[1] : "";

        switch (command) {
            case "list":
                ui.printLine("Here are the tasks in your list:");
                tasks.printTasks(ui);
                break;

            case "mark":
                tasks.mark(rest, true, ui);
                storage.save(tasks);
                break;

            case "unmark":
                tasks.mark(rest, false, ui);
                storage.save(tasks);
                break;

            case "todo":
                tasks.addTodo(rest, ui);
                storage.save(tasks);
                break;

            case "deadline":
                tasks.addDeadline(rest, ui);
                storage.save(tasks);
                break;

            case "event":
                tasks.addEvent(rest, ui);
                storage.save(tasks);
                break;

            case "delete":
                tasks.delete(rest, ui);
                storage.save(tasks);
                break;

            default:
                throw new InvalidCommandException("OOPS!!! I'm sorry, but I don't know what that command means :-(");
        }
    }


}
