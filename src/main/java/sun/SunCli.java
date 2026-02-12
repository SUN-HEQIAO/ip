// CLI → SunCli + Ui
//SunCli only deals with user interaction.
//Sun handles task operations and storage via "sun" instance
//This keeps CLI separate from logic: SunCli just interacts with the user, delegates all task operations to Sun.

// GUI → SunBackend + JavaFX
package sun;

import java.io.IOException;
import java.util.ArrayList;

import sun.exception.InvalidCommandException;
import sun.exception.InvalidFindException;
import sun.exception.InvalidTaskNumberException;
import sun.exception.InvalidTodoException;
import sun.exception.InvalidDeadlineException;
import sun.exception.InvalidEventException;
import sun.parser.InputParser;
import sun.task.Task;
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
                    ArrayList<Task> allTasks = sun.getTasks().listTasks();

                    if (allTasks.isEmpty()) {
                        ui.printLine("There are no tasks yet.");
                    } else {
                        ui.printTaskList(allTasks);
                    }

                    break;

                case "mark":
                    Task markTask  = sun.getTasks().mark(rest, true);
                    sun.getStorage().save(sun.getTasks());

                    ui.printMarkedMessage(markTask, true);

                    break;

                case "unmark":
                    Task unmarkTask = sun.getTasks().mark(rest, false);
                    sun.getStorage().save(sun.getTasks());

                    ui.printMarkedMessage(unmarkTask, false);

                    break;

                case "todo":
                    Task todoTask = sun.getTasks().addTodo(rest);
                    sun.getStorage().save(sun.getTasks());

                    ui.printTaskAdded(todoTask, sun.getTasks().size());

                    break;

                case "deadline":
                    Task deadlineTask = sun.getTasks().addDeadline(rest);
                    sun.getStorage().save(sun.getTasks());

                    ui.printTaskAdded(deadlineTask, sun.getTasks().size());

                    break;

                case "event":
                    Task eventTask = sun.getTasks().addEvent(rest);
                    sun.getStorage().save(sun.getTasks());

                    ui.printTaskAdded(eventTask, sun.getTasks().size());

                    break;

                case "delete":
                    Task deleteTask = sun.getTasks().delete(rest);
                    sun.getStorage().save(sun.getTasks());

                    ui.printTaskRemoved(deleteTask, sun.getTasks().size());

                    break;

                case "find":
                    ArrayList<Task> matches = sun.getTasks().find(rest);

                    if (matches.isEmpty()) {
                        ui.printLine("No matching tasks found.");
                    } else {
                        ui.printFoundTaskList(matches);
                    }

                    break;

                case "undo":
                    sun.getTasks().undo();
                    sun.getStorage().save(sun.getTasks());

                    ui.printLine("Undo Success!");

                    break;

                default:
                    throw new InvalidCommandException("OOPS!!! I'm sorry, but I don't know what that command means :-(");
                }
            } catch (InvalidCommandException | InvalidTaskNumberException | InvalidTodoException |
                     InvalidDeadlineException | InvalidEventException | IOException |
                     IllegalArgumentException | InvalidFindException | IllegalStateException e) {
                ui.printError(e.getMessage());
            }
        }

        ui.printBye();
    }

    public static void main(String[] args) {
        new SunCli("./data/sun.txt").run();
    }
}
