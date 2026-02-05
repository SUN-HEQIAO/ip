// Sun MUST NOT decide where messages are shown:
// console/JavaFX dialog/logs etc

//Sun’s job is only:
//load data, hold tasks, expose state.
//It is the UI code (CLI or GUI) that decides how to inform the user.

package sun;

import sun.storage.Storage;
import sun.task.TaskList;

import java.io.IOException;

public class Sun {
    private Storage storage;
    private TaskList tasks;

    public TaskList getTasks() {
        return this.tasks;
    }

    public Storage getStorage() {
        return this.storage;
    }

    public Sun(String filePath) {
        storage = new Storage(filePath);

        try {
            this.tasks = new TaskList(storage.load());
        } catch (IOException e) {
            this.tasks = new TaskList();
        }
    }
}
