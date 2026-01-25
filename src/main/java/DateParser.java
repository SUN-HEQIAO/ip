import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateParser {
    private static final DateTimeFormatter[] FORMATS = new DateTimeFormatter[] {
            DateTimeFormatter.ofPattern("MMM dd yyyy HHmm"), // Load from "sun.txt" format
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"), // Reading user input format date WITH time
            DateTimeFormatter.ofPattern("yyyy-MM-dd"), // Reading user input format date WITHOUT time
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
            DateTimeFormatter.ofPattern("d/M/yyyy HH:mm"),
            DateTimeFormatter.ofPattern("d-M-yyyy h:mma"),
            DateTimeFormatter.ofPattern("d/M/yyyy"),
            DateTimeFormatter.ofPattern("d-M-yyyy")
    };

    public static LocalDateTime parse(String input) {
        for (DateTimeFormatter format : FORMATS) {
            try {
                // Try parsing as LocalDateTime first
                return LocalDateTime.parse(input, format);
            } catch (DateTimeParseException e1) {
                try {
                    // If that fails, try LocalDate and convert to LocalDateTime with its default 00:00
                    LocalDate date = LocalDate.parse(input, format);
                    return date.atStartOfDay();
                } catch (DateTimeParseException ignored) {
                    // Ignore the exception, try again
                }
            }
        }

        // If ALL formats don't match user input
        throw new IllegalArgumentException("OOPS!!! Invalid date format. Please use yyyy-MM-dd HHmm.");
    }
}
