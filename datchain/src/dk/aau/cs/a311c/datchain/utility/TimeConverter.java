package dk.aau.cs.a311c.datchain.utility;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeConverter {

    //declare desired format of time and local timezone as constants
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss (O)");
    private static final ZoneId localTimeZone = ZoneId.of("GMT+2");

    //returns string from UNIX timestamp
    public static String getDate(long timestamp) {
        return Instant.ofEpochSecond(timestamp)
                .atZone(localTimeZone)
                .format(formatter);
    }
}
