package hello.formatter;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class CachedSimpleDateFormatTest {

    @Test
    public void testFormat() throws Exception {
        assertEquals("2015",CachedSimpleDateFormat.format(new Date(),"yyyy"));
    }
}