import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
  /**
   * Get Date Object from string
   * @param timeStr: "yyyy-MM-dd" or "yyyy-MM-ddTHH:mm:ssZ"
   * @return
   */
  public static Date getDateFromISOStr(String timeStr) {
    try {
      String dateStr = timeStr.split("T", 2)[0];
      return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
    } catch (Exception e) {
      System.err.println("Cannot parse " + timeStr + " to Date format");
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Get substring of date from a ISO date string
   * (Fetch yyyy-MM-dd from yyyy-MM-ddTHH:mm:ssZ)
   * @param date
   * @return
   */
  public static String retreiveDate(String date) {
    return date.split("T")[0];
  }
}
