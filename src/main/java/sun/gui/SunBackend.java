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
import sun.task.Task;


public class SunBackend {
    private Sun sun;

    public SunBackend() {
        this.sun = new Sun("./data/sun.txt");
    }

    public String getResponse(String input) {
        input = input.trim().replaceAll("\\s+", " ");

        // Only split into 2 parts first
        String[] inputs = input.split(" ", 2);
        String command = inputs[0].toLowerCase();
        String rest = (inputs.length > 1) ? inputs[1] : "";

        switch (command) {
        case "list": {
            ArrayList<Task> tasks = sun.getTasks().listTasksAndReturn();

            if (tasks.isEmpty()) {
                return "There are no tasks yet.";
            }

            StringBuilder builder = new StringBuilder("Here are the tasks in your list:\n");
            for (int i = 0; i < tasks.size(); i++) {
                builder.append(String.format("%d. %s\n", i + 1, tasks.get(i)));
            }

            return builder.toString();
        }

        case "mark": {
            try {
                Task targetTask = sun.getTasks().markAndReturn(rest, true);
                sun.getStorage().save(sun.getTasks());
                return String.format("Nice! I've marked this task as done:\n%s", targetTask);
            } catch (InvalidTaskNumberException | IOException e) {
                return e.getMessage();
            }
        }

        case "unmark": {
            try {
                Task targetTask = sun.getTasks().markAndReturn(rest, false);
                sun.getStorage().save(sun.getTasks());
                return String.format("OK, I've marked this task as not done yet:\n%s", targetTask);
            } catch (InvalidTaskNumberException | IOException e) {
                return e.getMessage();
            }
        }

        case "todo": {
            try {
                Task todoTask = sun.getTasks().addTodoAndReturn(rest);
                sun.getStorage().save(sun.getTasks());

                return printTaskAdded(todoTask);
            } catch (InvalidTodoException | IOException e) {
                return e.getMessage();
            }
        }

        case "deadline": {
            try {
                Task deadLineTask = sun.getTasks().addDeadlineAndReturn(rest);
                sun.getStorage().save(sun.getTasks());
                return printTaskAdded(deadLineTask);
            } catch (InvalidDeadlineException | IOException e) {
                return e.getMessage();
            }
        }

        case "event": {
            try {
                Task eventTask = sun.getTasks().addEventAndReturn(rest);
                sun.getStorage().save(sun.getTasks());
                return printTaskAdded(eventTask);
            } catch (InvalidEventException | IOException e) {
                return e.getMessage();
            }
        }

        case "delete": {
            try {
                Task targetTask = sun.getTasks().deleteAndReturn(rest);
                sun.getStorage().save(sun.getTasks());
                return String.format("""
                                Noted. I've removed this task:
                                %s
                                Now you have %d tasks in the list.
                                """,
                        targetTask,
                        sun.getTasks().sizeTasks());
            } catch (InvalidTaskNumberException | IOException e) {
                return e.getMessage();
            }
        }

        case "find": {
            try {
                ArrayList<Task> matches = sun.getTasks().findAndReturn(rest);
                if (matches.isEmpty()) {
                    return "No matching tasks found.";
                }

                StringBuilder builder = new StringBuilder("Here are the matching tasks in your list:\n");
                for (int i = 0; i < matches.size(); i++) {
                    builder.append(String.format("%d. %s\n", i + 1, matches.get(i)));
                }

                return builder.toString();
            } catch (InvalidFindException e) {
                return e.getMessage();
            }
        }

        case "bye": {
            return "BYE_SIGNAL";
        }

        default: {
            return "OOPS!!! I'm sorry, but I don't know what that command means :-(";
        }
        }
    }

    public String printTaskAdded(Task task) {
        return String.format("""
                                Noted. I've added this task:
                                %s
                                Now you have %d tasks in the list.
                                """,
                task,
                sun.getTasks().sizeTasks());
    }
}

