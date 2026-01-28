import java.io.IOException;

public class Sun {
    // Attributes/Fields
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    // Constructor
    public Sun(String filePath) {
        // Prepare Ui Instance
        ui = new Ui();

        // Prepare Storage
        storage = new Storage(filePath);

        // Load tasks from disk
        try {
            this.tasks = new TaskList(storage.load());
        } catch (IOException e) {
            System.out.println("Failed to load tasks. Starting with empty list.");
            this.tasks = new TaskList();
        }
    }

    // Loop in "main()" is moved here
    public void run() {
        ui.printWelcome();

        while (true) {
            // Trim user input
            String input = ui.readLine();

            // Skip empty inputs ("")
            if (input.isEmpty()) {
                continue;
            }

            if (Parser.isBye(input)) {
                break;
            }

            // Process inputs
            try {
                Parser.parseInputs(input, tasks, storage, ui);
            } catch (InvalidCommandException | InvalidTaskNumberException |
                     InvalidTodoException | InvalidDeadlineException |
                     InvalidEventException | IOException | IllegalArgumentException e) {
                ui.printError(e.getMessage());
            }
        }

        ui.printBye();
    }

    // Main
    public static void main(String[] args) {
        new Sun("./data/sun.txt").run();
    }
}
