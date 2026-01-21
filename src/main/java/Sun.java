import java.util.ArrayList;
import java.util.Scanner;

public class Sun {
    public static void main(String[] args) {
        // Opening Message
        System.out.println("Hello! I'm Sun.");
        System.out.println("What can I do for you?");

        // Prepare Scanner and ArrayList<Task>
        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();

        // Input loop using Switch Statements
        while (true) {
            // Parse input into 2 parts first.
            // Goal is to get command first
            String input = scanner.nextLine();
            String[] inputs = input.split(" ", 2);
            String command = inputs[0];
            String rest = (inputs.length > 1) ? inputs[1] : "";

            if (command.equalsIgnoreCase("bye")) {
                break;
            }

            switch (command) {
                case "list":
                    System.out.println("Here are the tasks in your list:");
                    goThroughList(tasks);
                    break;

                case "mark":
                    handleMark(tasks, rest, true);
                    break;

                case "unmark":
                    handleMark(tasks, rest, false);
                    break;

                case "todo":
                    handleTodo(tasks, rest);
                    break;

                case "deadline":
                    handleDeadline(tasks, rest);
                    break;

                case "event":
                    handleEvent(tasks, rest);
                    break;

                default:
                    System.out.println("Please state your command");
            }
        }
        System.out.println("Bye. Hope to see you again soon!");
    }

    private static void goThroughList(ArrayList<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(String.format("%d. %s", i + 1, tasks.get(i)));
        }
    }

    private static void handleMark(ArrayList<Task> tasks, String rest, boolean isDone) {
        int index = Integer.parseInt(rest) - 1;
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

    private static void handleTodo(ArrayList<Task> tasks, String rest) {
        Task todoTask = new Todo(rest);
        tasks.add(todoTask);
        printTaskAdded(todoTask, tasks.size());
    }

    private static void handleDeadline(ArrayList<Task> tasks, String rest) {
        String[] parts = rest.split(" /by ", 2);
        String description = parts[0];
        String by = parts[1];
        Task deadlineTask = new Deadline(description, by);
        tasks.add(deadlineTask);
        printTaskAdded(deadlineTask, tasks.size());
    }

    private static void handleEvent(ArrayList<Task> tasks, String rest) {
        String[] descriptionSplit = rest.split(" /from ", 2);
        String description = descriptionSplit[0];

        String[] fromSplit = descriptionSplit[1].split(" /to ", 2);
        String from = fromSplit[0];
        String to = fromSplit[1];

        Task eventTask = new Event(description, from, to);
        tasks.add(eventTask);
        printTaskAdded(eventTask, tasks.size());
    }
}
