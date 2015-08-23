package hello.formatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by victorvorontsov on 23.08.15.
 */
public class CachedSimpleDateFormat {

    private static final ThreadLocal<HashMap<String,DateFormat>> maskToFormatCache = new ThreadLocal<>();

    public static String format(Date date, String mask) {
        DateFormat formatter = getFormat(mask);
        return formatter.format(date);
    }

    private static DateFormat getFormat(String mask) {
        DateFormat format = getCache().get(mask);
        if(null == format) {
            format = new SimpleDateFormat(mask);
            HashMap<String,DateFormat> cache = getCache();
            cache.put(mask, format);
            maskToFormatCache.set(cache);
        }
        return format;
    }

    private static HashMap<String,DateFormat> getCache() {
        HashMap<String,DateFormat> cache = maskToFormatCache.get();
        if(null == cache) {
            cache = new HashMap<>();
            maskToFormatCache.set(cache);
        }
        return cache;
    }

}
