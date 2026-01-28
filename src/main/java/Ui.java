// The UI class will take in user inputs, show user inputs, show errors.

import java.util.Scanner;

public class Ui {
    private Scanner scanner;

    public Ui() {
        scanner = new Scanner(System.in);
    }

    public void printWelcome() {
        System.out.println("Hello! I'm Sun.");
        System.out.println("What can I do for you?");
    }

    public String readLine() {
        return scanner.nextLine();
    }

    public void printLine(String message) {
        System.out.println(message);
    }

    public void printError(String message) {
        System.out.println(message);
    }

    public void printBye() {
        System.out.println("Bye. Hope to see you again soon!");
    }
}
