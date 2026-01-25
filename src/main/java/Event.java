import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task{
    protected LocalDateTime from;
    protected LocalDateTime to;
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy HHmm");

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)", super.toString(),
                this.from.format(OUTPUT_FORMAT), this.to.format(OUTPUT_FORMAT));
    }

    @Override
    public String toFileString() {
        return String.format("E | %d | %s | %s | %s", this.getIsDone().equals("X") ? 1 : 0, this.getDescription(),
                this.from.format(OUTPUT_FORMAT), this.to.format(OUTPUT_FORMAT));
    }
}
