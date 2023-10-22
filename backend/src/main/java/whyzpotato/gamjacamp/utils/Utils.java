package whyzpotato.gamjacamp.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss.SSSSSS");

    // return date in [시작, 끝)
    public static Boolean isBetween(LocalDate date, LocalDate before, LocalDate after) {
        return date.isEqual(before) || (date.isAfter(before) && date.isBefore(after));
    }

    public static Boolean isBetween(LocalDate date, LocalDateTime before, LocalDateTime after) {
        return date.isEqual(before.toLocalDate()) || (date.isAfter(before.toLocalDate()) && date.isBefore(after.toLocalDate()));
    }

    public static Boolean isBetween(LocalDateTime date, LocalDate before, LocalDate after) {
        return date.isEqual(before.atStartOfDay()) || (date.isAfter(before.atStartOfDay()) && date.isBefore(after.atStartOfDay()));
    }

    public static Boolean isBetween(LocalDateTime date, LocalDateTime before, LocalDateTime after) {
        return date.isEqual(before) || (date.isAfter(before) && date.isBefore(after));
    }

    public static LocalDateTime toSqlDateTime(LocalDateTime time) {
        return LocalDateTime.parse(time.plusNanos(500).format(formatter), formatter);
    }

}
