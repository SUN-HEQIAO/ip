import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateParser {
    private static final DateTimeFormatter[] FORMATS = new DateTimeFormatter[] {    DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("d/M/yyyy"),
            DateTimeFormatter.ofPattern("d-M-yyyy"),
            DateTimeFormatter.ofPattern("dd-M-yyyy"),
            DateTimeFormatter.ofPattern("dd/M/yyyy"),
            DateTimeFormatter.ofPattern("d-MM-yyyy"),
            DateTimeFormatter.ofPattern("d/MM/yyyy"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("MMM d yyyy"),
            DateTimeFormatter.ofPattern("d MMM yyyy"),
            DateTimeFormatter.ofPattern("MMMM d yyyy"),
            DateTimeFormatter.ofPattern("d MMMM yyyy"),
    };

    public static LocalDate parse(String input) {
        for (DateTimeFormatter format: FORMATS) {
            try {
                // Return LocalDate Format
                return LocalDate.parse(input, format);
            } catch (DateTimeParseException e) {
                // Ignore the exception to prevent crashing, continue trying
            }
        }

        // If all formats don't match
        throw new IllegalArgumentException("OOPS!!! Invalid date format. Please use yyyy-MM-dd.");
    }
}
