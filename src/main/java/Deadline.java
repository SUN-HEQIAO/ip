import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Deadline extends Task{
    protected LocalDate by;
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyy");

    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(), this.by.format(OUTPUT_FORMAT));
    }

    // "this.by.toString()" -> "yyyy-MM-dd"
    // You save to the disk the "yyyy-MM-dd" format
    // You print to terminal the "MMM dd yy" format
    @Override
    public String toFileString() {
        return String.format("D | %d | %s | %s", this.getIsDone().equals("X") ? 1 : 0, this.getDescription(),
                this.by.toString());
    }

}
