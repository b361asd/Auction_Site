package rutgers.cs336.gui;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class HelperDatetime {

	public static String getDatetimeSZ(int delta) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
		//
		java.util.Date today   = new java.util.Date();
		long           ltime   = today.getTime() + 7 * 24 * 60 * 60 * 1000;         //One week
		java.util.Date today_7 = new java.util.Date(ltime);
		//
		return formatter.format(today_7);
	}

	public static String getDatetimeSZ(java.util.Date date) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
		//
		return formatter.format(date);
	}


	public static java.util.Date getDatetime(String input) {
		java.util.Date output = null;
		//
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
		try {
			output = (java.util.Date) formatter.parse(input);
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
		System.out.println(HelperDatetime.convertToSQLDate(HelperDatetime.getDatetime(datetimeSZString)));
		//
		System.out.println(getDatetimeSZ(7));
		//
		System.out.println(getDatetime(""));
		//
		System.out.println(convertToSQLDate(getDatetime("")));
		//
		System.out.println(convertToUTILDate(convertToSQLDate(getDatetime(""))));
	}

}
