package hello.writer;

import hello.formatter.CachedSimpleDateFormat;
import hello.formatter.FixedMarkupDecimalFormatter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.file.transform.LineAggregator;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class FixedMarkupLineAggregator<T> implements LineAggregator<T> {

    private List<Function<T, String>> mappingItem = new LinkedList<>();

    public synchronized FixedMarkupLineAggregator<T> add(Function<T, String> mappingElement) {
        mappingItem.add(mappingElement);
        return this;
    }

    @Override
    public String aggregate(final T t) {
        return mappingItem.stream().map(f -> f.apply(t)).reduce("", (a, b) -> a + b);
    }

    public static String lpad(String str,
                              int size,
                              String padStr) {
        String result = StringUtils.leftPad(str, size, padStr);
        return result.length() > size ? result.substring(0, size) : result;
    }

    public static String lpad(String str,
                              int size) {
        return lpad(str, size, " ");
    }

    public static String rpad(String str,
                              int size,
                              String padStr) {
        String result = StringUtils.rightPad(str, size, padStr);
        return result.length() > size ? result.substring(0, size) : result;
    }

    public static String rpad(String str,
                              int size) {
        return rpad(str, size, " ");
    }

    public static String format(Double val, String mask) {
        return FixedMarkupDecimalFormatter.format(val,mask);
    }

    public static String format(Date val, String mask) {
        return CachedSimpleDateFormat.format(val, mask);
    }
}
