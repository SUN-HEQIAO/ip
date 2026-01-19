import java.util.ArrayList;
import java.util.Scanner;

public class Sun {
    public static void main(String[] args) {
        System.out.println("Hello! I'm Sun.");
        System.out.println("What can I do for you?");

        Scanner scanner = new Scanner(System.in);
        ArrayList<String> tasks = new ArrayList<>();

        while (true) {
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("bye")) {
                break;
            }

            if (input.equals("list")) {
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println(String.format("%d. %s", i+1, tasks.get(i)));
                }
            } else {
                tasks.add(input);
                System.out.println(String.format("added: %s", input));
            }
        }
        System.out.println("Bye. Hope to see you again soon!");
    }
}
