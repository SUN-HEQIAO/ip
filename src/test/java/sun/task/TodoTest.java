package sun.task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

//Format: unitBeingTested_descriptionOfTestInputs_expectedOutcome
public class TodoTest {
    @Test
    public void constructor_setsDescriptionCorrectly_descriptionSetCorrectly() {
        Todo todo = new Todo("read book");
        assertEquals("read book", todo.getDescription());
    }

    @Test
    public void toString_notDone_correctFormat() {
        Todo todo = new Todo("read book");
        assertEquals("[T][ ] read book", todo.toString());
    }

    @Test
    public void toFileString_notDone_correctFormat() {
        Todo todo = new Todo("read book");
        assertEquals("T | 0 | read book", todo.toFileString());
    }

    @Test
    public void toFileString_done_correctFormat() {
        Todo todo = new Todo("read book");
        todo.setIsDone(true); // assuming Task has this
        assertEquals("T | 1 | read book", todo.toFileString());
    }
}