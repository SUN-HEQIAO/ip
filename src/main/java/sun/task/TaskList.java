package sun.task;

import sun.exception.InvalidFindException;
import sun.exception.InvalidTaskNumberException;
import sun.exception.InvalidTodoException;
import sun.exception.InvalidDeadlineException;
import sun.exception.InvalidEventException;
import sun.parser.DateTimeParser;
import sun.ui.Ui;

import java.time.LocalDateTime;
import java.util.ArrayList;


public class TaskList {
    private ArrayList<Task> tasks;
    private Ui ui = new Ui();

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> task) {
        this.tasks = task;
    }

    public int sizeTasks() {
        return this.tasks.size();
    }

    public ArrayList<Task> getTasks() {
        return this.tasks;
    }

    public ArrayList<Task> listTasks(boolean printOutput) {
        ArrayList<Task> allTasks = this.tasks;

        if (printOutput) {
            if (allTasks.isEmpty()) {
                ui.printLine("There are no tasks yet.");
            } else {
                printTaskList(allTasks);
            }
        }

        return allTasks;
    }

    public Task mark(String rest, boolean isDone, boolean printOutput)
            throws InvalidTaskNumberException {
        int index = this.parseTaskNumber(rest);
        Task targetTask = tasks.get(index);
        targetTask.setIsDone(isDone);

        if (printOutput) {
            ui.printLine(getMarkMessage(isDone));
            ui.printLine(targetTask.toString());
        }

        return targetTask;
    }

    public Task addTodo(String rest, boolean printOutput)
            throws InvalidTodoException {
        if (rest.isEmpty()) {
            throw new InvalidTodoException("OOPS!!! The description of a todo cannot be empty.");
        }

        Task todoTask = new Todo(rest);
        tasks.add(todoTask);

        if (printOutput) {
            printTaskAdded(todoTask);
        }

        return todoTask;
    }

    public Task addDeadline(String rest, boolean printOutput)
            throws InvalidDeadlineException {
        String[] parts = rest.split(" /by ", 2);

        validateDeadlineParts(parts);

        String description = parts[0];
        String by = parts[1];

        LocalDateTime byDateTime = DateTimeParser.parseDateTime(by);

        Task deadlineTask = new Deadline(description, byDateTime);
        tasks.add(deadlineTask);

        if (printOutput) {
            printTaskAdded(deadlineTask);
        }

        return deadlineTask;
    }

    public Task addEvent(String rest, boolean printOutput)
            throws InvalidEventException {
        String[] descriptionSplit = rest.split(" /from ", 2);
        validateEventDescription(descriptionSplit);

        String description = descriptionSplit[0];

        String[] timeSplit = descriptionSplit[1].split(" /to ", 2);
        validateEventTimes(timeSplit);

        String from = timeSplit[0];
        String to = timeSplit[1];

        LocalDateTime fromDateTime = DateTimeParser.parseDateTime(from);
        LocalDateTime toDateTime = DateTimeParser.parseDateTime(to);

        Task eventTask = new Event(description, fromDateTime, toDateTime);
        tasks.add(eventTask);

        if (printOutput) {
            printTaskAdded(eventTask);
        }

        return eventTask;
    }

    public Task delete(String rest, boolean printOutput)
            throws InvalidTaskNumberException {
        int index = this.parseTaskNumber(rest);
        Task targetTask = tasks.get(index);
        tasks.remove(index);

        if (printOutput) {
            printTaskRemoved(targetTask);
        }

        return targetTask;
    }

    public ArrayList<Task> find(String rest, boolean printOutput)
            throws InvalidFindException {
        if (rest.isEmpty()) {
            throw new InvalidFindException("OOPS!!! Please provide task to find.");
        }

        ArrayList<Task> matches = new ArrayList<>();
        for (Task task : this.tasks) {
            if (task.getDescription().toLowerCase().contains(rest.toLowerCase())) {
                matches.add(task);
            }
        }

        if (printOutput) {
            if (matches.isEmpty()) {
                ui.printLine("No matching tasks found.");
            } else {
                printFoundTaskList(matches);
            }
        }

        return matches;
    }




    //Helper method
    private int parseTaskNumber(String rest)
            throws InvalidTaskNumberException {
        // No Task Number
        if (rest.isEmpty()) {
            throw new InvalidTaskNumberException("OOPS!!! Task number missing.");
        }

        // Task Number not numerical
        int index;
        try {
            index = Integer.parseInt(rest) - 1;
        } catch (NumberFormatException e) {
            throw new InvalidTaskNumberException("OOPS!!! Please enter a valid numerical task number.");
        }

        // Task Number out of range
        if (index < 0 || index >= tasks.size()) {
            throw new InvalidTaskNumberException("OOPS!!! Task number out of range.");
        }

        return index;
    }

    //Helper method
    private void printTaskAdded(Task task) {
        ui.printLine("Got it. I've added this task:");
        ui.printLine(task.toString());
        ui.printLine("Now you have " + tasks.size() + " tasks in the list.");
    }

    //Helper method
    private void printTaskList(ArrayList<Task> allTasks) {
        ui.printLine("Here are the tasks in your list:");
        for (int i = 0; i < allTasks.size(); i++) {
            ui.printLine(String.format("%d. %s", i + 1, allTasks.get(i)));
        }
    }

    //Helper method
    private void printFoundTaskList(ArrayList<Task> allTasks) {
        ui.printLine("Here are the matching tasks in your list:");
        for (int i = 0; i < allTasks.size(); i++) {
            ui.printLine(String.format("%d. %s", i + 1, allTasks.get(i)));
        }
    }

    //Helper method
    private void validateEventDescription(String[] parts)
            throws InvalidEventException {
        if (parts.length < 2 || parts[0].isEmpty() || parts[1].isEmpty()) {
            throw new InvalidEventException(
                    "OOPS!!! The description or start time of an event cannot be empty.");
        }
    }

    private void validateEventTimes(String[] parts)
            throws InvalidEventException {
        if (parts.length < 2 || parts[0].isEmpty() || parts[1].isEmpty()) {
            throw new InvalidEventException(
                    "OOPS!!! The start or end time of an event cannot be empty.");
        }
    }

    //Helper method
    private void validateDeadlineParts(String[] parts)
            throws InvalidDeadlineException {
        if (parts.length < 2 || parts[0].isEmpty() || parts[1].isEmpty()) {
            throw new InvalidDeadlineException(
                    "OOPS!!! The description or due date of a deadline cannot be empty.");
        }
    }

    //Helper method
    private String getMarkMessage(boolean isDone) {
        return isDone
                ? "Nice! I've marked this task as done:"
                : "OK, I've marked this task as not done yet:";
    }

    //Helper method
    private void printTaskRemoved(Task targetTask) {
        ui.printLine("Noted. I've removed this task:");
        ui.printLine(targetTask.toString());
        ui.printLine(String.format("Now you have %d tasks left", tasks.size()));
    }




}
