// CLI → SunCli + Ui
// GUI → SunBackend + JavaFX
package sun;

import java.io.IOException;
import sun.exception.InvalidCommandException;
import sun.exception.InvalidFindException;
import sun.exception.InvalidTaskNumberException;
import sun.exception.InvalidTodoException;
import sun.exception.InvalidDeadlineException;
import sun.exception.InvalidEventException;
import sun.parser.InputParser;
import sun.ui.Ui;

public class SunCli {
    private Ui ui;
    private Sun sun;

    public SunCli(String filePath) {
        this.ui = new Ui();
        this.sun = new Sun(filePath);
    }

    public void run() {
        ui.printWelcome();

        while (true) {
            String input = ui.readLine();

            // Skip empty inputs ("")
            if (input.isEmpty()) {
                continue;
            }

            if (InputParser.isBye(input)) {
                break;
            }

            // Process inputs
            try {
                InputParser.parseInput(input, sun.getTasks(), sun.getStorage());
            } catch (InvalidCommandException | InvalidTaskNumberException | InvalidTodoException |
                     InvalidDeadlineException | InvalidEventException | IOException |
                     IllegalArgumentException | InvalidFindException e) {
                ui.printError(e.getMessage());
            }
        }

        ui.printBye();
    }

    public static void main(String[] args) {
        new SunCli("./data/sun.txt").run();
    }
}
