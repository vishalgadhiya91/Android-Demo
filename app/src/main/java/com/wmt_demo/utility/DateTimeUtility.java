package com.wmt_demo.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Vishal on 17-12-2017.
 */

public class DateTimeUtility {

    // Return current date & time in YYYY-mm-dd hh:mm:ss
    public static String getCurrentDateTime() {
        String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
        DateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
        return dateFormatter.format(new Date());
    }
}
