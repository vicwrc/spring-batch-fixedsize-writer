package hello.writer;

import hello.Person;
import org.junit.Test;

import static hello.writer.FixedMarkupLineAggregator.*;
import static org.junit.Assert.*;

public class FixedMarkupLineAggregatorTest {

    FixedMarkupLineAggregator<Person> aggregator = new FixedMarkupLineAggregator<>();

    @Test
    public void testAggregate() {
        aggregator
                .add(p -> p.getFirstName())
                .add(p -> p.getLastName())
                .add(p -> "123");

        assertEquals("qqqwww123", aggregator.aggregate(new Person("qqq", "www", 1 , Double.valueOf(1.123f))));
    }

    @Test
    public void testLeftPadding() throws Exception {
        aggregator
                .add(p -> lpad(p.getFirstName(), 5))
                .add(p -> lpad(p.getLastName(), 5, "0"))
                .add(p -> lpad(p.getAge().toString(), 5, "0"))
                .add(p -> "123");

        assertEquals("  qqq00www00001123", aggregator.aggregate(new Person("qqq", "www", 1 , Double.valueOf(1.123f))));
    }

    @Test
    public void testLeftPaddingCut() throws Exception {
        aggregator
                .add(p -> lpad(p.getFirstName(), 5))
                .add(p -> lpad(p.getLastName(), 3, "0"))
                .add(p -> format(p.getMoney(), "nnnff"))
                .add(p -> format(p.getBirthday(), "yyyy"))
                .add(p -> "123");

        assertEquals("  qqqwww001122015123", aggregator.aggregate(new Person("qqq", "wwwww", 1 , Double.valueOf(1.123f))));
    }

    @Test
    public void testRightPadding() throws Exception {
        aggregator
                .add(p -> rpad(p.getFirstName(), 5))
                .add(p -> rpad(p.getLastName(), 5, "0"))
                .add(p -> "123");

        assertEquals("qqq  www00123", aggregator.aggregate(new Person("qqq", "www", 1 , Double.valueOf(1.123f))));
    }

    @Test
    public void testRightPaddingCut() throws Exception {
        aggregator
                .add(p -> rpad(p.getFirstName(), 5))
                .add(p -> rpad(p.getLastName(), 3, "0"))
                .add(p -> "123");

        assertEquals("qqq  www123", aggregator.aggregate(new Person("qqq", "wwwww", 1 , Double.valueOf(1.123f))));
    }

}