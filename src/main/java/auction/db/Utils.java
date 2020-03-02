package main.java.auction.db;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Utils {

   /**
    * Help to print Exception Details
    *
    * @param e Exception
    * @return Output details
    */
   public static String exceptionToString(Exception e) {
      StringWriter sw = new StringWriter();
      e.printStackTrace(new PrintWriter(sw));
      return sw.toString();
   }
}
