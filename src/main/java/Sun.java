import java.util.ArrayList;
import java.util.Scanner;

import static java.util.Arrays.copyOfRange;

public class Sun {
    public static void main(String[] args) {
        System.out.println("Hello! I'm Sun.");
        System.out.println("What can I do for you?");

        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();

        while (true) {
            String input = scanner.nextLine();
            String[] inputs = input.split(" ");
            String command = inputs[0];

            if (command.equalsIgnoreCase("bye")) {
                break;
            }

            switch (command) {
                case "list":
                    System.out.println("Here are the tasks in your list:");
                    goThroughList((tasks));
                    break;

                case "mark":
                    if (inputs.length == 2) {
                        int index = Integer.parseInt(inputs[1]) - 1;
                        Task targetTask = tasks.get(index);
                        targetTask.setIsDone(true);
                        System.out.println("Nice! I've marked this task as done:");
                        System.out.println(targetTask);
                    }
                    break;

                case "unmark":
                    if (inputs.length == 2) {
                        int index = Integer.parseInt(inputs[1]) - 1;
                        Task targetTask = tasks.get(index);
                        targetTask.setIsDone(false);
                        System.out.println("OK, I've marked this task as not done yet:");
                        System.out.println(targetTask);
                    }
                    break;

                case "todo":
                    String description = String.join(" ", copyOfRange(inputs, 1, inputs.length));
                    Task todoTask = new Todo(description);
                    tasks.add(todoTask);
                    System.out.println("Got it. I've added this task:");
                    System.out.println(todoTask);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    break;

                default:
                    tasks.add(new Task(input));
                    System.out.println("added: " + input);

            }
        }
        System.out.println("Bye. Hope to see you again soon!");
    }

    private static void goThroughList(ArrayList<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(String.format("%d. %s", i + 1, tasks.get(i)));
        }
    }
}
