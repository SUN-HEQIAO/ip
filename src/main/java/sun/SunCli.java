// CLI → SunCli + Ui
// GUI → SunBackend + JavaFX
package sun;

import java.io.IOException;

import sun.exception.InvalidCommandException;
import sun.exception.InvalidFindException;
import sun.exception.InvalidTaskNumberException;
import sun.exception.InvalidTodoException;
import sun.exception.InvalidDeadlineException;
import sun.exception.InvalidEventException;
import sun.parser.InputParser;
import sun.ui.Ui;

public class SunCli {
    private Ui ui;
    private Sun sun;

    public SunCli(String filePath) {
        this.ui = new Ui();
        this.sun = new Sun(filePath);
    }

    public void run() {
        ui.printWelcome();

        while (true) {
            String input = ui.readLine();

            // Skip empty inputs ("")
            if (input.isEmpty()) {
                continue;
            }

            if (InputParser.isBye(input)) {
                break;
            }

            // Process inputs
            try {
                String normalizedInput = InputParser.normalizeInput(input);

                String[] splits = InputParser.splitInput(normalizedInput);
                String command = splits[0];
                String rest = splits[1];

                switch (command) {
                case "list":
                    var allTasks = sun.getTasks().listTasks();

                    if (allTasks.isEmpty()) {
                        ui.printLine("There are no tasks yet.");
                    } else {
                        ui.printTaskList(allTasks);
                    }

                    break;

                case "mark":
                    var markTask  = sun.getTasks().mark(rest, true);
                    sun.getStorage().save(sun.getTasks());

                    ui.printMarkedMessage(markTask, true);

                    break;

                case "unmark":
                    var unmarkTask = sun.getTasks().mark(rest, false);
                    sun.getStorage().save(sun.getTasks());

                    ui.printMarkedMessage(unmarkTask, true);

                    break;

                case "todo":
                    var todoTask = sun.getTasks().addTodo(rest);
                    sun.getStorage().save(sun.getTasks());

                    ui.printTaskAdded(todoTask, sun.getTasks().size());

                    break;

                case "deadline":
                    var deadlineTask = sun.getTasks().addDeadline(rest);
                    sun.getStorage().save(sun.getTasks());

                    ui.printTaskAdded(deadlineTask, sun.getTasks().size());

                    break;

                case "event":
                    var eventTask = sun.getTasks().addEvent(rest);
                    sun.getStorage().save(sun.getTasks());

                    ui.printTaskAdded(eventTask, sun.getTasks().size());

                    break;

                case "delete":
                    var deleteTask = sun.getTasks().delete(rest);
                    sun.getStorage().save(sun.getTasks());

                    ui.printTaskRemoved(deleteTask, sun.getTasks().size());

                    break;

                case "find":
                    var matches = sun.getTasks().find(rest);

                    if (matches.isEmpty()) {
                        ui.printLine("No matching tasks found.");
                    } else {
                        ui.printFoundTaskList(matches);
                    }

                    break;

                default:
                    throw new InvalidCommandException("OOPS!!! I'm sorry, but I don't know what that command means :-(");
                }
            } catch (InvalidCommandException | InvalidTaskNumberException | InvalidTodoException |
                     InvalidDeadlineException | InvalidEventException | IOException |
                     IllegalArgumentException | InvalidFindException e) {
                ui.printError(e.getMessage());
            }
        }

        ui.printBye();
    }

    public static void main(String[] args) {
        new SunCli("./data/sun.txt").run();
    }
}
