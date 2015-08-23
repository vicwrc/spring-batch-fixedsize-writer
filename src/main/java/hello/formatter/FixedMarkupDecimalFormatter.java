package hello.formatter;

import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by victorvorontsov on 23.08.15.
 */
public class FixedMarkupDecimalFormatter {

    private static final ThreadLocal<HashMap<String,DecimalFormat>> maskToFormatCache = new ThreadLocal<>();

    /*
    * allowed mask: nnnnnnffff -> nnnnn.fff
    * nnnnn - number part,
    * fff - fractial part
    * */
    public static String format(Double val, String mask) {
        DecimalFormat formatter = getFormat(mask);
        return formatter.format(val).replace(".","");
    }

    private static DecimalFormat getFormat(String mask) {
        DecimalFormat format = getCache().get(mask);
        if(null == format) {
            format = createDecimalFormat(mask);
            HashMap<String,DecimalFormat> cache = getCache();
            cache.put(mask, format);
            maskToFormatCache.set(cache);
        }
        return format;
    }

    private static HashMap<String,DecimalFormat> getCache() {
        HashMap<String,DecimalFormat> cache = maskToFormatCache.get();
        if(null == cache) {
            cache = new HashMap<>();
            maskToFormatCache.set(cache);
        }
        return cache;
    }

    private static DecimalFormat createDecimalFormat(String mask) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator('.');
        DecimalFormat df = new DecimalFormat();
        df.setDecimalFormatSymbols(otherSymbols);
        int fractionDigits = StringUtils.countOccurrencesOf(mask, "f");
        int integerDigits = StringUtils.countOccurrencesOf(mask, "n");
        df.setMinimumFractionDigits(fractionDigits);
        df.setMaximumFractionDigits(fractionDigits);
        df.setMinimumIntegerDigits(integerDigits);
        df.setMaximumIntegerDigits(integerDigits);
        return df;
    }



}
