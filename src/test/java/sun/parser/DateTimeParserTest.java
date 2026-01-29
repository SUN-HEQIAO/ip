package sun.parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;

public class DateTimeParserTest {

    @Test
    public void parseDateTime_fullDatetime_withMMMddyyyyHHmm() {
        LocalDateTime expected = LocalDateTime.of(2026, 1, 29, 14, 30);
        LocalDateTime actual = DateTimeParser.parseDateTime("Jan 29 2026 1430");
        assertEquals(expected, actual);
    }

    @Test
    public void parseDateTime_fullDatetime_withYYYYMMddHHmm() {
        LocalDateTime expected = LocalDateTime.of(2026, 1, 29, 14, 30);
        assertEquals(expected, DateTimeParser.parseDateTime("2026-01-29 1430"));
    }

    @Test
    public void parseDateTime_dateOnly_dateFormat() {
        LocalDateTime expected = LocalDate.of(2026, 1, 29).atStartOfDay();
        assertEquals(expected, DateTimeParser.parseDateTime("2026-01-29"));
    }

    @Test
    public void parseDateTime_timeOnly_timeFormat() {
        LocalTime time = LocalTime.of(14, 30);
        LocalDateTime actual = DateTimeParser.parseDateTime("1430");
        // Compare only hour and minute with today
        LocalDate today = LocalDate.now();
        assertEquals(LocalDateTime.of(today, time), actual);
    }

    @Test
    public void parseDateTime_invalidInput_throwsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> DateTimeParser.parseDateTime("not a date"));

        String expectedMessage = """
            OOPS!!! Invalid date/time format.
            Please use:
             - yyyy-MM-dd HHmm
             - yyyy-MM-dd
             - HHmm
            """;

        assertEquals(expectedMessage, ex.getMessage());
    }

}
