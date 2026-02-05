package sun.parser;

import sun.exception.InvalidCommandException;
import sun.exception.InvalidFindException;
import sun.exception.InvalidTaskNumberException;
import sun.exception.InvalidTodoException;
import sun.exception.InvalidDeadlineException;
import sun.exception.InvalidEventException;
import sun.storage.Storage;
import sun.task.TaskList;

import java.io.IOException;

public class InputParser {

    public static boolean isBye(String input) {
        return input.equalsIgnoreCase("bye");
    }

    public static void parseInput(String input, TaskList tasks, Storage storage)
            throws InvalidCommandException, InvalidTodoException, InvalidDeadlineException,
            InvalidEventException, InvalidTaskNumberException, IOException,
            IllegalArgumentException, InvalidFindException {

        // Clean up user inputs
        input = input.trim().replaceAll("\\s+", " ");

        // Only split into 2 parts first
        String[] inputs = input.split(" ", 2);
        String command = inputs[0].toLowerCase();
        String rest = (inputs.length > 1) ? inputs[1] : "";

        switch (command) {
        case "list":
            tasks.listTasks();
            break;

        case "mark":
            tasks.mark(rest, true);
            storage.save(tasks);
            break;

        case "unmark":
            tasks.mark(rest, false);
            storage.save(tasks);
            break;

        case "todo":
            tasks.addTodo(rest);
            storage.save(tasks);
            break;

        case "deadline":
            tasks.addDeadline(rest);
            storage.save(tasks);
            break;

        case "event":
            tasks.addEvent(rest);
            storage.save(tasks);
            break;

        case "delete":
            tasks.delete(rest);
            storage.save(tasks);
            break;

        case "find":
            tasks.find(rest);
            break;

        default:
            throw new InvalidCommandException("OOPS!!! I'm sorry, but I don't know what that command means :-(");
        }
    }
}
