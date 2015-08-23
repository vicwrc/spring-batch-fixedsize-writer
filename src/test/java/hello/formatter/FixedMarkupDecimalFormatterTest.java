package hello.formatter;

import org.junit.Test;

import static org.junit.Assert.*;

public class FixedMarkupDecimalFormatterTest {


    @Test
    public void testFormat() throws Exception {
        assertEquals("00123123",FixedMarkupDecimalFormatter.format(123.123d,"nnnnnfff"));

    }
}