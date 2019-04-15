package rutgers.cs336.gui;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class HelperDatetime {
	static DateFormat        formatter      = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	static DateTimeFormatter formatterzoned = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

	public static String getDatetimeSZ(int delta) {
		Instant       instant      = Instant.now();
		ZoneId        zoneId       = ZoneId.of("America/New_York");
		ZonedDateTime zdt          = ZonedDateTime.ofInstant(instant, zoneId);
		ZonedDateTime zdtDaysLater = zdt.plus(delta, ChronoUnit.DAYS);
		//
		return zdtDaysLater.format(formatterzoned);
	}

	public static String _getDatetimeSZ(int delta) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		//
		java.util.Date today   = new java.util.Date();
		long           ltime   = today.getTime() + 7 * 24 * 60 * 60 * 1000;         //One week
		java.util.Date today_7 = new java.util.Date(ltime);
		//
		return formatter.format(today_7);
	}


	public static String getDatetimeSZ(java.util.Date date) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		//
		return formatter.format(date);
	}


	public static java.util.Date getDatetime(String input) {
		java.util.Date output;
		//
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");         //yyyy-MM-dd'T'HH:mm:ss
		try {
			output = formatter.parse(input);
		}
		catch (ParseException e) {
			e.printStackTrace();
			//
			java.util.Date today = new java.util.Date();
			long           ltime = today.getTime() + 7 * 24 * 60 * 60 * 1000;         //One week
			output = new java.util.Date(ltime);
		}
		//
		return output;
	}


	public static java.sql.Date convertToSQLDate(java.util.Date input) {
		return new java.sql.Date(input.getTime());
	}


	public static java.util.Date convertToUTILDate(java.sql.Date input) {
		return new java.util.Date(input.getTime());
	}


	public static void main(String[] args) {
		String datetimeSZString = "2019-04-07T11:01:20";
		//
		System.out.println(getDatetimeSZ(7));
		//
		System.out.println(getDatetimeSZ(7));
		//
		//System.out.println(HelperDatetime.convertToSQLDate(HelperDatetime.getDatetime(datetimeSZString)));
		//
		//System.out.println(getDatetimeSZ(7));
		//
		//System.out.println(getDatetime(""));
		//
		//System.out.println(convertToSQLDate(getDatetime("")));
		//
		//System.out.println(convertToUTILDate(convertToSQLDate(getDatetime(""))));
	}

}
