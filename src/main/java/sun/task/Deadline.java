package sun.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a Deadline task in the Sun application.
 * <p>
 * A <code>Deadline</code> is a type of {@link Task} that has a due date and time.
 * <p>
 * The due time is stored as a {@link LocalDateTime} object and displayed/formatted
 * using the pattern "MMM dd yyyy HHmm".
 *
 * @see Task
 */
public class Deadline extends Task {
    /** The due date and time of the deadline */
    private LocalDateTime by;
    // OUTPUT_FORMAT reformats LocalDateTime into desired format stored in .txt file or printed
    /** Formatter for displaying and storing the deadline */
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy HHmm");

    /**
     * Creates a new Deadline task with the given description and due date/time.
     * <p>
     * The task is initially marked as not done.
     *
     * @param description the description of the deadline
     * @param by the due date and time of the deadline
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the string representation of the Deadline task for display.
     * <p>
     * The format is: "[D][status] description (by: dueTime)".
     *
     * @return a formatted string representing the Deadline task
     */
    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(), this.by.format(OUTPUT_FORMAT));
    }

    /**
     * Returns the string representation of the Deadline task for saving to a file.
     * <p>
     * The format is: "D | 1 | description | dueTime" if done,
     * or "D | 0 | description | dueTime" if not done.
     *
     * @return a formatted string suitable for file storage
     */
    @Override
    public String toFileString() {
        return String.format("D | %d | %s | %s", this.getIsDoneAsX().equals("X") ? 1 : 0, this.getDescription(),
                this.by.format(OUTPUT_FORMAT));
    }

}
