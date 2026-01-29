package sun.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an Event task in the Sun application.
 * <p>
 * An <code>Event</code> is a type of {@link Task} that has a start time
 * and an end time.
 * <p>
 * The start and end times are stored as {@link LocalDateTime} objects
 * and displayed/formatted using the pattern "MMM dd yyyy HHmm".
 *
 * @see Task
 */
public class Event extends Task {
    /** The start time of the event */
    protected LocalDateTime from;
    /** The end time of the event */
    protected LocalDateTime to;
    /** Formatter for displaying dates and times */
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy HHmm");

    /**
     * Creates a new Event task with the given description, start time, and end time.
     * <p>
     * The task is initially marked as not done.
     *
     * @param description the description of the event
     * @param from the start time of the event
     * @param to the end time of the event
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the string representation of the Event task for display.
     * <p>
     * The format is: "[E][status] description (from: startTime to: endTime)".
     *
     * @return a formatted string representing the Event task
     */
    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)", super.toString(),
                this.from.format(OUTPUT_FORMAT), this.to.format(OUTPUT_FORMAT));
    }

    /**
     * Returns the string representation of the Event task for saving to a file.
     * <p>
     * The format is: "E | 1 | description | startTime | endTime" if done,
     * or "E | 0 | description | startTime | endTime" if not done.
     *
     * @return a formatted string suitable for file storage
     */
    @Override
    public String toFileString() {
        return String.format("E | %d | %s | %s | %s", this.getIsDone().equals("X") ? 1 : 0, this.getDescription(),
                this.from.format(OUTPUT_FORMAT), this.to.format(OUTPUT_FORMAT));
    }
}
