package sun.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * Class for parsing date and time strings into {@link LocalDateTime} objects.
 * <p>
 * The {@link DateTimeParser} supports multiple input formats for user convenience,
 * including formats with or without time, 24-hour or 12-hour clocks, and the
 * storage format used in <code>sun.txt</code>.
 * <p>
 * The parser attempts the formats in order defined in {@link #FORMATS}:
 * <ul>
 *     <li>MMM dd yyyy HHmm (loaded from file)</li>
 *     <li>yyyy-MM-dd HHmm</li>
 *     <li>yyyy-MM-dd HH:mm</li>
 *     <li>yyyy-MM-dd h:mma</li>
 *     <li>yyyy-MM-dd</li>
 *     <li>HHmm</li>
 *     <li>HH:mm</li>
 *     <li>h:mma</li>
 *     <li>ha</li>
 * </ul>
 * <p>
 * Parsing is attempted as {@link LocalDateTime} first, then {@link LocalDate} (converted
 * to start-of-day {@link LocalDateTime}), and finally {@link LocalTime} (combined with
 * today's date to form a {@link LocalDateTime}).
 */
public class DateTimeParser {
    /** Array of supported date/time formats */
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

    /**
     * Parses a string into a {@link LocalDateTime}.
     * <p>
     * The method tries all supported formats in order. If a format fails:
     * <ul>
     *     <li>Attempts to parse as {@link LocalDateTime}</li>
     *     <li>If that fails, attempts {@link LocalDate} and converts to start-of-day {@link LocalDateTime}</li>
     *     <li>If that fails, attempts {@link LocalTime} and combines with today's date</li>
     * </ul>
     * <p>
     * If none of the formats match, an {@link IllegalArgumentException} is thrown
     * with guidance on valid input formats.
     *
     * @param input the user input string representing a date and/or time
     * @return a {@link LocalDateTime} representing the parsed date and/or time
     * @throws IllegalArgumentException if the input does not match any supported formats
     */
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
