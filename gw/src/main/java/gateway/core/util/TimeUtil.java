package gateway.core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author dungla
 */
public final class TimeUtil {

    private TimeUtil(){}

    /**
     * Trả về thời gian hiện hành theo pattern
     * @param pattern - vd : yyyyMMdd hh:mm:ss
     * @return String
     */
    public static String getCurrentTime(String pattern){
        return getTime(pattern, 0);
    }

    /**
     * Trả về thời gian hiện hành - 1 theo pattern (tức là ngày hôm qua)
     * @param pattern - vd : yyyyMMdd hh:mm:ss
     * @return String
     */
    public static String getYesterday(String pattern){
        return getTime(pattern, -1);
    }

    /**
     * Trả về thời gian hiện hành + 1 theo pattern (tức là ngày mai)
     * @param pattern - vd : yyyyMMdd hh:mm:ss
     * @return String
     */
    public static String getTomorrow(String pattern){
        return getTime(pattern, 1);
    }

    /**
     *
     * @param time - times
     * @param patternOfTime - pattern of times
     * @return time UTC Z
     * @throws ParseException
     */
    public static String getUTCTime(String time, String patternOfTime) throws ParseException {
        return utcTime(time, patternOfTime, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    }

    /**
     *
     * @param time - times
     * @param patternOfTime - pattern of times
     * @return time UTC with + 7:00
     * @throws ParseException
     */
    public static String getUTCTimePlus7(String time, String patternOfTime) throws ParseException {
        return utcTime(time,patternOfTime, "yyyy-MM-dd'T'HH:mm:ss.SSS+7:00" );
    }

    private static String utcTime(String time, String patternOfTime, String utcPattern) throws ParseException {
        SimpleDateFormat format1 = new SimpleDateFormat(patternOfTime);
        Date date1 = format1.parse(time);
        SimpleDateFormat format = new SimpleDateFormat(utcPattern);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(date1);
    }

    private static String getTime(String pattern, int set){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,set);
        return formatDate(pattern, calendar);
    }

    private static String formatDate(String pattern, Calendar calendar){
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(calendar.getTime());
    }
}
