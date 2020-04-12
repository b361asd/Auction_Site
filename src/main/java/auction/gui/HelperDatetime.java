package auction.gui;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Helper functions for time
 */
public class HelperDatetime {
   static DateTimeFormatter formatterZoned = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

   public static String getDatetimeSZ(int delta) {
      Instant       instant      = Instant.now();
      ZoneId        zoneId       = ZoneId.of("America/New_York");
      ZonedDateTime zdt          = ZonedDateTime.ofInstant(instant, zoneId);
      ZonedDateTime zdtDaysLater = zdt.plus(delta, ChronoUnit.DAYS);
      //
      return zdtDaysLater.format(formatterZoned);
   }

   public static void main(String[] args) {
      System.out.println(getDatetimeSZ(7));
   }
}
