package rutgers.cs336.db;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Utils {

	public static String exceptionToString(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
}
