package sun.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class DateTimeParser {
    private static final DateTimeFormatter[] FORMATS = new DateTimeFormatter[] {
            DateTimeFormatter.ofPattern("MMM dd yyyy HHmm"), // Loaded from "sun.txt" format
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"), // Reading user input format date WITH time
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"), // Reading user input format date WITH time
            DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma"), // Reading user input format date WITH time
            DateTimeFormatter.ofPattern("yyyy-MM-dd"), // Reading user input format date WITHOUT time
            DateTimeFormatter.ofPattern("HHmm"), // Reading user input as time
            DateTimeFormatter.ofPattern("HH:mm"), // Reading user input as time
            DateTimeFormatter.ofPattern("h:mma"), // Reading user input as time
            DateTimeFormatter.ofPattern("ha") // Reading user input as time
    };

    public static LocalDateTime parseDateTime(String input) {
        for (DateTimeFormatter format : FORMATS) {
            try {
                // Try parsing as LocalDateTime first
                    return LocalDateTime.parse(input, format);
                } catch (DateTimeParseException e1) {
                try {
                    // If that fails, try LocalDate and convert to LocalDateTime with its default 00:00
                    LocalDate date = LocalDate.parse(input, format);
                    return date.atStartOfDay();
                } catch (DateTimeParseException e2) {
                    // If that fails, try LocalTime and convert to LocalDateTime with today's date,
                    // by literally combining LocalTime and LocalDate
                    try {
                        LocalTime time = LocalTime.parse(input, format);
                        return LocalDate.now().atTime(time); // attach today's date
                    } catch (DateTimeParseException ignored) {
                        // Ignore the exception, loop the next format
                    }
                }
            }
        }

        // If ALL formats don't match user input
        throw new IllegalArgumentException("""
            OOPS!!! Invalid date/time format.
            Please use:
             - yyyy-MM-dd HHmm
             - yyyy-MM-dd
             - HHmm
            """);
    }
}
