// "SunBackend" holds a "Sun" instance BUT doesn’t run the CLI loop.

package sun.gui;

import java.io.IOException;
import java.util.ArrayList;

import sun.Sun;
import sun.exception.InvalidDeadlineException;
import sun.exception.InvalidEventException;
import sun.exception.InvalidFindException;
import sun.exception.InvalidTaskNumberException;
import sun.exception.InvalidTodoException;
import sun.parser.InputParser;
import sun.task.Task;


public class SunBackend {
    private Sun sun;

    public SunBackend() {
        this.sun = new Sun("./data/sun.txt");
    }

    public String getResponse(String input) {
        String normalizedInput = InputParser.normalizeInput(input);

        String[] splits = InputParser.splitInput(normalizedInput);
        String command = splits[0];
        String rest = splits[1];

        try {
            switch (command) {
            case "list":
                return taskListString(sun.getTasks().listTasks());

            case "mark":
                Task markTask = sun.getTasks().mark(rest, false);
                sun.getStorage().save(sun.getTasks());

                return taskMarkedString(markTask, true);

            case "unmark":
                Task unmarkTask = sun.getTasks().mark(rest, false);
                sun.getStorage().save(sun.getTasks());
                return taskMarkedString(unmarkTask, false);

            case "todo":
                Task todoTask = sun.getTasks().addTodo(rest);
                sun.getStorage().save(sun.getTasks());
                return taskAddedString(todoTask);

            case "deadline":
                Task deadlineTask = sun.getTasks().addDeadline(rest);
                sun.getStorage().save(sun.getTasks());
                return taskAddedString(deadlineTask);

            case "event":
                Task eventTask = sun.getTasks().addEvent(rest);
                sun.getStorage().save(sun.getTasks());
                return taskAddedString(eventTask);

            case "delete":
                Task deleteTask = sun.getTasks().delete(rest);
                sun.getStorage().save(sun.getTasks());
                return taskDeleteString(deleteTask);

            case "find":
                return taskFoundString(sun.getTasks().find(rest));

            case "bye":
                return "BYE_SIGNAL";

            default:
                return "OOPS!!! I'm sorry, but I don't know what that command means :-(";
            }
        } catch (InvalidTodoException | InvalidDeadlineException | InvalidEventException |
                 InvalidTaskNumberException | InvalidFindException | IOException e) {
            return e.getMessage();
        }
    }




    // Helper Method
    private String taskListString(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            return "There are no tasks yet.";
        }
        StringBuilder builder = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            builder.append(String.format("%d. %s\n", i + 1, tasks.get(i)));
        }
        return builder.toString();
    }

    // Helper Method
    private String taskMarkedString(Task task, boolean isDone) {
        String status = isDone
                ? "Nice! I've marked this task as done:"
                : "OK, I've marked this task as not done yet:";
        return String.format("""
                %s
                %s
                """, status, task);
    }

    // Helper Method
    public String taskAddedString(Task task) {
        return String.format("""
                                Noted. I've added this task:
                                %s
                                Now you have %d tasks in the list.
                                """, task, sun.getTasks().size());
    }

    // Helper Method
    private String taskDeleteString(Task task) {
        return String.format("""
                Noted. I've removed this task:
                %s
                Now you have %d tasks in the list.
                """, task, sun.getTasks().size());
    }

    // Helper Method
    private String taskFoundString(ArrayList<Task> matches) {
        if (matches.isEmpty()) {
            return "No matching tasks found.";
        }
        StringBuilder builder = new StringBuilder("Here are the matching tasks in your list:\n");
        for (int i = 0; i < matches.size(); i++) {
            builder.append(String.format("%d. %s\n", i + 1, matches.get(i)));
        }
        return builder.toString();
    }
}

