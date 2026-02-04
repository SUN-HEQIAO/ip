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
    public static void main(String[] args) {
        Ui ui = new Ui();
        Sun sun = new Sun("./data/sun.txt");

        // By this time:
        // sun's Storage and TaskList attribute has been initialized.

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
}
