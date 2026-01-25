import java.io.IOException;
import java.util.Scanner;

public class Sun {
    // Main
    public static void main(String[] args) {
        // Opening Message
        System.out.println("Hello! I'm Sun.");
        System.out.println("What can I do for you?");

        // Prepare Scanner
        Scanner scanner = new Scanner(System.in);

        //Prepare Storage
        Storage storage = new Storage("./data/sun.txt");

        //Prepare TaskList
        TaskList tasks;

        // Load tasks from disk
        try {
            tasks = new TaskList(storage.load());
        } catch (IOException e) {
            System.out.println("Failed to load tasks. Starting with empty list.");
            tasks = new TaskList();
        }

        // Input loop using Switch Statements
        while (true) {
            // Trim user input
            String input = scanner.nextLine().trim();

            // Skip empty inputs ("")
            if (input.isEmpty()) {
                continue;
            }

            // Normalise spaces
            input = input.replaceAll("\\s+", " ");

            // If user inputs "bye"
            if (input.equalsIgnoreCase("bye")) {
                break;
            }

            // Process inputs
            try {
                processInputs(input, tasks, storage);
            } catch (InvalidCommandException | InvalidTaskNumberException |
                     InvalidTodoException | InvalidDeadlineException |
                     InvalidEventException | IOException e) {
                System.out.println(e.getMessage());
            }

        }

        System.out.println("Bye. Hope to see you again soon!");
    }


    //Starter Motor
    private static void processInputs(String input, TaskList tasks, Storage storage)
            throws InvalidCommandException, InvalidTodoException, InvalidDeadlineException, InvalidEventException,
            InvalidTaskNumberException, IOException {

        // Only split into 2 parts first
        String[] inputs = input.split(" ", 2);
        String command = inputs[0].toLowerCase(); //only for command, the first part of input
        String rest = (inputs.length > 1) ? inputs[1] : "";

        switch (command) {
            case "list":
                System.out.println("Here are the tasks in your list:");
                goThroughList(tasks);
                break;

            case "mark":
                handleMark(tasks, rest, true);
                storage.save(tasks);
                break;

            case "unmark":
                handleMark(tasks, rest, false);
                storage.save(tasks);
                break;

            case "todo":
                handleTodo(tasks, rest);
                storage.save(tasks);
                break;

            case "deadline":
                handleDeadline(tasks, rest);
                storage.save(tasks);
                break;

            case "event":
                handleEvent(tasks, rest);
                storage.save(tasks);
                break;

            case "delete":
                handleDeletion(tasks, rest);
                storage.save(tasks);
                break;

            default:
                throw new InvalidCommandException("OOPS!!! I'm sorry, but I don't know what that command means :-(");
        }
    }


    // Helper Methods
    private static void goThroughList(TaskList tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(String.format("%d. %s", i + 1, tasks.get(i)));
        }
    }

    private static void handleMark(TaskList tasks, String rest, boolean isDone)
            throws InvalidTaskNumberException {
        int index = parseTaskNumber(tasks.size(), rest);
        Task targetTask = tasks.get(index);
        targetTask.setIsDone(isDone);
        System.out.println(isDone ? "Nice! I've marked this task as done:"
                                  : "OK, I've marked this task as not done yet:");
        System.out.println(targetTask);
    }

    private static void printTaskAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println(task);
        System.out.println(String.format("Now you have %d tasks in the list.", taskCount));
    }

    private static void handleTodo(TaskList tasks, String rest)
            throws InvalidTodoException {
        // todoTask with no description
        if (rest.isEmpty()) {
            throw new InvalidTodoException("OOPS!!! The description of a todo cannot be empty.");
        }

        Task todoTask = new Todo(rest);
        tasks.add(todoTask);
        printTaskAdded(todoTask, tasks.size());
    }

    private static void handleDeadline(TaskList tasks, String rest)
            throws InvalidDeadlineException {
        String[] parts = rest.split(" /by ", 2);

        // Deadline with no description or due-date
        if (parts.length < 2 || parts[0].isEmpty() || parts[1].isEmpty()) {
            throw new InvalidDeadlineException("OOPS!!! The description or due date of a deadline cannot be empty.");
        }

        String description = parts[0];
        String by = parts[1];
        Task deadlineTask = new Deadline(description, by);
        tasks.add(deadlineTask);
        printTaskAdded(deadlineTask, tasks.size());
    }

    private static void handleEvent(TaskList tasks, String rest)
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

        Task eventTask = new Event(description, from, to);
        tasks.add(eventTask);
        printTaskAdded(eventTask, tasks.size());
    }

    private static void handleDeletion(TaskList tasks, String rest)
            throws InvalidTaskNumberException {
        int index = parseTaskNumber(tasks.size(), rest);
        Task targetTask = tasks.get(index);
        tasks.remove(index);
        System.out.println("Noted. I've removed this task:");
        System.out.println(targetTask);
        System.out.println(String.format("Now you have %d tasks left", tasks.size()));

    }

    private static int parseTaskNumber(int tasksCount, String rest)
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
        if (index < 0 || index >= tasksCount) {
            throw new InvalidTaskNumberException("OOPS!!! Task number out of range.");
        }

        return index;
    }
}
