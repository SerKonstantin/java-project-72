package hexlet.code.util;

import java.time.format.DateTimeFormatter;

public class TimestampFormatter {
    public static String getString(java.sql.Timestamp createdAt) {
        var createdAtLocalDateTime = createdAt.toLocalDateTime();
        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return createdAtLocalDateTime.format(formatter);
    }
}
