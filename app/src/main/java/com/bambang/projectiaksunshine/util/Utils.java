package com.bambang.projectiaksunshine.util;

import android.content.Context;

import com.bambang.projectiaksunshine.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by bambanghs on 5/24/2017.
 */

public class Utils {

    /**
     * Returns a drawable resource id related to name informed.
     *
     * @param context - The context
     * @param name - drawable resource name
     * @return drawable resource id
     */
    public static int getDrawableResourceIdByName(Context context, String name) {
        return context.getResources().getIdentifier(name, Constants.DRAWABLE_RESOURCE, context.getPackageName());
    }

    /**
     * Converts the time of data calculation in unix (UTC) to date in string format dd/MM/yyyy hh:mm:ss.
     *
     * @param unixTimeUtc - time of data calculation in unix
     * @return date in String format dd/MM/yyyy hh:mm:ss
     */
    public static String convertUnixTimeUtcToDateString(long unixTimeUtc, String dateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(unixTimeUtc * Constants.SECONDS_TO_MILISECONDS); // multiply by 1000 to convert in miliseconds
        SimpleDateFormat simpleDataFormat = new SimpleDateFormat(dateFormat); // date format: dd/MM/yyyy hh:mm:ss
        return simpleDataFormat.format(calendar.getTime());
    }

}
