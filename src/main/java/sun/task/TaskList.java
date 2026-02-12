package sun.task;

import sun.exception.InvalidFindException;
import sun.exception.InvalidTaskNumberException;
import sun.exception.InvalidTodoException;
import sun.exception.InvalidDeadlineException;
import sun.exception.InvalidEventException;
import sun.parser.DateTimeParser;

import java.time.LocalDateTime;
import java.util.ArrayList;


public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
        assert this.tasks != null : "Task list should be initialised";
    }

    public TaskList(ArrayList<Task> task) {
        assert task != null : "Task list passed in should not be null";
        this.tasks = task;
    }

    public int size() {
        return this.tasks.size();
    }

    public ArrayList<Task> getTasks() {
        return this.tasks;
    }

    public ArrayList<Task> listTasks() {
        ArrayList<Task> allTasks = this.tasks;

        return allTasks;
    }

    public Task mark(String rest, boolean isDone)
            throws InvalidTaskNumberException {
        int index = this.parseTaskNumber(rest);
        assert index >= 0 && index < this.size() : "Parsed task index should be valid";

        Task targetTask = tasks.get(index);
        assert targetTask != null : "Target task should exist";

        targetTask.setIsDone(isDone);

        return targetTask;
    }

    public Task addTodo(String rest)
            throws InvalidTodoException {
        if (rest.isEmpty()) {
            throw new InvalidTodoException("OOPS!!! The description of a todo cannot be empty.");
        }

        int oldSize = this.size();

        Task todoTask = new Todo(rest);
        tasks.add(todoTask);

        assert this.size() == oldSize + 1 : "Task list should increase by 1 after adding a todo task";

        return todoTask;
    }

    public Task addDeadline(String rest)
            throws InvalidDeadlineException {
        String[] parts = rest.split(" /by ", 2);

        validateDeadlineParts(parts);

        String description = parts[0];
        String by = parts[1];

        LocalDateTime byDateTime = DateTimeParser.parseDateTime(by);

        Task deadlineTask = new Deadline(description, byDateTime);
        tasks.add(deadlineTask);

        return deadlineTask;
    }

    public Task addEvent(String rest)
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

        return eventTask;
    }

    public Task delete(String rest)
            throws InvalidTaskNumberException {
        int oldSize = this.size();

        int index = this.parseTaskNumber(rest);
        Task targetTask = tasks.get(index);
        tasks.remove(index);

        assert this.size() == oldSize - 1 : "Task list size should decrease by 1 after deletion";

        return targetTask;
    }

    public ArrayList<Task> find(String rest)
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
}
