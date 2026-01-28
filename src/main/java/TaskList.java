import java.time.LocalDateTime;
import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> task) {
        this.tasks = task;
    }

    public int size() {
        return this.tasks.size();
    }

    public Task get(int index) {
        return this.tasks.get(index);
    }

    public ArrayList<Task> getAllTasks() {
        return this.tasks;
    }

    public void add(Task task) {
        this.tasks.add(task);
    }

    public Task remove(int index) {
        return this.tasks.remove(index);
    }

    public void printTasks(Ui ui) {
        for (int i = 0; i < this.size(); i++)  {
            ui.printLine(String.format("%d. %s", i + 1, this.get(i)));
        }
    }

    public void mark(String rest, boolean isDone, Ui ui)
            throws InvalidTaskNumberException {
        int index = this.parseTaskNumber(rest);
        Task targetTask = this.get(index);
        targetTask.setIsDone(isDone);
        ui.printLine(isDone
                ? "Nice! I've marked this task as done:"
                : "OK, I've marked this task as not done yet:");
        ui.printLine(targetTask.toString());
    }

    public void addTodo(String rest, Ui ui)
            throws InvalidTodoException {
        // todoTask with no description
        if (rest.isEmpty()) {
            throw new InvalidTodoException("OOPS!!! The description of a todo cannot be empty.");
        }

        Task todoTask = new Todo(rest);
        this.add(todoTask);

        printTaskAdded(todoTask, ui);
    }

    public void addDeadline(String rest, Ui ui)
            throws InvalidDeadlineException {
        String[] parts = rest.split(" /by ", 2);

        // Deadline with no description or due-date
        if (parts.length < 2 || parts[0].isEmpty() || parts[1].isEmpty()) {
            throw new InvalidDeadlineException("OOPS!!! The description or due date of a deadline cannot be empty.");
        }

        String description = parts[0];
        String byString = parts[1];

        LocalDateTime byDateTime = DateParser.parse(byString);
        Task deadlineTask = new Deadline(description, byDateTime);
        this.add(deadlineTask);

        printTaskAdded(deadlineTask, ui);
    }

    public void addEvent(String rest, Ui ui)
            throws InvalidEventException {
        String[] descriptionSplit = rest.split(" /from ", 2);

        // Event with no description or start
        if (descriptionSplit.length < 2 || descriptionSplit[0].isEmpty() || descriptionSplit[1].isEmpty()) {
            throw new InvalidEventException("OOPS!!! The description or start time of an event cannot be empty.");
        }
        String description = descriptionSplit[0];

        String[] fromSplit = descriptionSplit[1].split(" /to ", 2);

        // Event with no start or end
        if (fromSplit.length < 2 || fromSplit[0].isEmpty() || fromSplit[1].isEmpty())
            throw new InvalidEventException("OOPS!!! The start or end time of an event cannot be empty.");

        String from = fromSplit[0];
        String to = fromSplit[1];
        LocalDateTime fromDateTime = DateParser.parse(from);
        LocalDateTime toDateTime = DateParser.parse(to);
        Task eventTask = new Event(description, fromDateTime, toDateTime);
        this.add(eventTask);

        printTaskAdded(eventTask, ui);
    }

    public void delete(String rest, Ui ui)
            throws InvalidTaskNumberException {
        int index = parseTaskNumber(rest);
        Task targetTask = this.get(index);
        this.remove(index);

        ui.printLine("Noted. I've removed this task:");
        ui.printLine(targetTask.toString());
        ui.printLine(String.format("Now you have %d tasks left", tasks.size()));
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
        if (index < 0 || index >= this.size()) {
            throw new InvalidTaskNumberException("OOPS!!! Task number out of range.");
        }

        return index;
    }

    //Helper method
    private void printTaskAdded(Task task, Ui ui) {
        ui.printLine("Got it. I've added this task:");
        ui.printLine(task.toString());
        ui.printLine("Now you have " + this.size() + " tasks in the list.");
    }

}
