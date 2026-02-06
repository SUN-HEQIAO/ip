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

    public Task getTask(int index) {
        return this.tasks.get(index);
    }

    public ArrayList<Task> getTasks() {
        return this.tasks;
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public Task removeTask(int index) {
        return this.tasks.remove(index);
    }


    public ArrayList<Task> listTasks(boolean printOutput) {
        ArrayList<Task> allTasks = this.getTasks();

        if (printOutput) {
            if (allTasks.isEmpty()) {
                ui.printLine("There are no tasks yet.");
            } else {
                ui.printLine("Here are the tasks in your list:");
                for (int i = 0; i < allTasks.size(); i++) {
                    ui.printLine(String.format("%d. %s", i + 1, allTasks.get(i)));
                }
            }
        }

        return allTasks;
    }

    public Task mark(String rest, boolean isDone, boolean printOutput)
            throws InvalidTaskNumberException {
        int index = this.parseTaskNumber(rest);
        Task targetTask = this.getTask(index);
        targetTask.setIsDone(isDone);

        if (printOutput) {
            String message = isDone
                    ? "Nice! I've marked this task as done:"
                    : "OK, I've marked this task as not done yet:";
            ui.printLine(message);
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
        this.addTask(todoTask);

        if (printOutput) {
            printTaskAdded(todoTask);
        }

        return todoTask;
    }

    public Task addDeadline(String rest, boolean printOutput)
            throws InvalidDeadlineException {
        String[] parts = rest.split(" /by ", 2);

        if (parts.length < 2 || parts[0].isEmpty() || parts[1].isEmpty()) {
            throw new InvalidDeadlineException("OOPS!!! The description or due date of a deadline cannot be empty.");
        }

        String description = parts[0];
        String byString = parts[1];

        LocalDateTime byDateTime = DateTimeParser.parseDateTime(byString);
        Task deadlineTask = new Deadline(description, byDateTime);
        this.addTask(deadlineTask);

        if (printOutput) {
            printTaskAdded(deadlineTask);
        }

        return deadlineTask;
    }

    public Task addEvent(String rest, boolean printOutput)
            throws InvalidEventException {
        String[] descriptionSplit = rest.split(" /from ", 2);

        if (descriptionSplit.length < 2 || descriptionSplit[0].isEmpty() || descriptionSplit[1].isEmpty()) {
            throw new InvalidEventException("OOPS!!! The description or start time of an event cannot be empty.");
        }
        String description = descriptionSplit[0];

        String[] fromSplit = descriptionSplit[1].split(" /to ", 2);

        if (fromSplit.length < 2 || fromSplit[0].isEmpty() || fromSplit[1].isEmpty()) {
            throw new InvalidEventException("OOPS!!! The start or end time of an event cannot be empty.");
        }

        String from = fromSplit[0];
        String to = fromSplit[1];

        LocalDateTime fromDateTime = DateTimeParser.parseDateTime(from);
        LocalDateTime toDateTime = DateTimeParser.parseDateTime(to);
        Task eventTask = new Event(description, fromDateTime, toDateTime);
        this.addTask(eventTask);

        if (printOutput) {
            printTaskAdded(eventTask);
        }

        return eventTask;
    }

    public Task delete(String rest, boolean printOutput)
            throws InvalidTaskNumberException {
        int index = this.parseTaskNumber(rest);
        Task targetTask = this.getTask(index);
        this.removeTask(index);

        if (printOutput) {
            ui.printLine("Noted. I've removed this task:");
            ui.printLine(targetTask.toString());
            ui.printLine(String.format("Now you have %d tasks left", this.sizeTasks()));
        }

        return targetTask;
    }

    public ArrayList<Task> find(String rest, boolean printOutput)
            throws InvalidFindException {
        if (rest.isEmpty()) {
            throw new InvalidFindException("OOPS!!! Please provide task to find.");
        }

        ArrayList<Task> matches = new ArrayList<>();
        for (Task task : this.getTasks()) {
            if (task.getDescription().toLowerCase().contains(rest.toLowerCase())) {
                matches.add(task);
            }
        }

        if (printOutput) {
            if (matches.isEmpty()) {
                ui.printLine("No matching tasks found.");
            } else {
                ui.printLine("Here are the matching tasks in your list:");
                for (int i = 0; i < matches.size(); i++) {
                    ui.printLine(String.format("%d. %s", i + 1, matches.get(i)));
                }
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
        if (index < 0 || index >= this.sizeTasks()) {
            throw new InvalidTaskNumberException("OOPS!!! Task number out of range.");
        }

        return index;
    }

    //Helper method
    private void printTaskAdded(Task task) {
        ui.printLine("Got it. I've added this task:");
        ui.printLine(task.toString());
        ui.printLine("Now you have " + this.sizeTasks() + " tasks in the list.");
    }

}
