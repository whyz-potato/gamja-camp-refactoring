package whyzpotato.gamjacamp.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Utils {

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
}
