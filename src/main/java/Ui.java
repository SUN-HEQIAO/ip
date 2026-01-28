// The UI class will take in user inputs, show user inputs, show errors.

import java.util.Scanner;

public class Ui {
    private Scanner scanner;

    public Ui() {
        scanner = new Scanner(System.in);
    }

    public void showWelcome() {
        System.out.println("Hello! I'm Sun.");
        System.out.println("What can I do for you?");
    }

    public String readLine() {
        return scanner.nextLine();
    }

    public void showLine(String message) {
        System.out.println(message);
    }

    public void showError(String message) {
        System.out.println(message);
    }

    public void showBye() {
        System.out.println("Bye. Hope to see you again soon!");
    }
}
