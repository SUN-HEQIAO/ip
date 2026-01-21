import java.util.ArrayList;
import java.util.Scanner;

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
                    goThroughList((tasks));;
                    break;
                case "mark":
                    if (inputs.length == 2) {
                        int index = Integer.parseInt(inputs[1]) - 1;
                        tasks.get(index).setIsDone(true);
                        goThroughList(tasks);
                    }
                    break;
                case "unmark":
                    if (inputs.length == 2) {
                        int index = Integer.parseInt(inputs[1]) - 1;
                        tasks.get(index).setIsDone(false);
                        goThroughList(tasks);
                    }
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
