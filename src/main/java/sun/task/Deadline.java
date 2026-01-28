package sun.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Deadline extends Task {
    protected LocalDateTime by;
    // OUTPUT_FORMAT reformats LocalDateTime into desired format stored in .txt file or printed
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy HHmm");

    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(), this.by.format(OUTPUT_FORMAT));
    }

    @Override
    public String toFileString() {
        return String.format("D | %d | %s | %s", this.getIsDone().equals("X") ? 1 : 0, this.getDescription(),
                this.by.format(OUTPUT_FORMAT));
    }

}
