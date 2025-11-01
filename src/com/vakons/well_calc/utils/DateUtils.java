package com.vakons.well_calc.utils;

import java.util.Date;

public class DateUtils {

    //Unix Timestamp = (Excel Timestamp - 25569) * 86400
    //Excel Timestamp =  (Unix Timestamp / 86400) + 25569
    //86400 = Seconds in a day
    //25569 = Days between 1970/01/01 and 1900/01/01 (min date in Windows Excel)

    public static long parseUnixTimeFromExcelDate(String input) {
        try {
            int excelTimestamp = (int) (Float.parseFloat(input));
            return (excelTimestamp - 25569L) * 86400L;
        } catch (Throwable error) {
            throw new RuntimeException("Error parse Excel date string '" + input + "'", error);
        }
    }

    public static String getExcelTimeFromUnix(long unixTime) {
        var excelTimestamp = (unixTime / 86400L) + 25569L;
        return Long.toString(excelTimestamp);
    }

    public static String formatDDMMYYYY(long unixTime) {
        var date = new Date(unixTime * 1000L);
        return twoNum(date.getDate()) + "." + twoNum(date.getMonth() + 1) + "." + (1900 + date.getYear());
    }
    private static String twoNum(int value) {
        if(value < 10) {
            return "0" + value;
        }
        return Integer.toString(value);
    }

    public static int parseYearFromUnix(long unixTime) {
        var date = new Date(unixTime * 1000L);
        return 1900 + date.getYear();
    }
}
