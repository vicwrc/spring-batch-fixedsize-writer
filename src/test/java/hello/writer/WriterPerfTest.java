package hello.writer;

import hello.Person;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;

import static hello.writer.FixedMarkupLineAggregator.format;
import static hello.writer.FixedMarkupLineAggregator.lpad;

/**
 * Created by victorvorontsov on 23.08.15.
 */
public class WriterPerfTest {

    @Rule
    public ContiPerfRule rule = new ContiPerfRule();

    FixedMarkupLineAggregator<Person> aggregator = new FixedMarkupLineAggregator<Person>()
            .add(p -> lpad(p.getFirstName(), 5))
            .add(p -> lpad(p.getLastName(), 3, "0"))
            .add(p -> format(p.getMoney(), "nnnff"))
            .add(p -> format(p.getBirthday(), "yyyy"))
            .add(p -> lpad(p.getAge().toString(), 5, "0"))
            .add(p -> "123");

    FixedMarkupLineAggregator<Person> aggregatorConst = new FixedMarkupLineAggregator<Person>()
            .add(p -> "123")
            .add(p -> "123")
            .add(p -> "123")
            .add(p -> "123")
            .add(p -> "123")
            .add(p -> "123");

    FixedMarkupLineAggregator<Person> aggregatorString = new FixedMarkupLineAggregator<Person>()
            .add(p -> lpad(p.getFirstName(), 5))
            .add(p -> lpad(p.getLastName(), 3, "0"))
            .add(p -> lpad(p.getFirstName(), 5))
            .add(p -> lpad(p.getFirstName(), 5))
            .add(p -> lpad(p.getFirstName(), 5))
            .add(p -> lpad(p.getFirstName(), 5));

    FixedMarkupLineAggregator<Person> aggregatorInt = new FixedMarkupLineAggregator<Person>()
            .add(p -> lpad(p.getAge().toString(), 5, "0"))
            .add(p -> lpad(p.getAge().toString(), 5, "0"))
            .add(p -> lpad(p.getAge().toString(), 5, "0"))
            .add(p -> lpad(p.getAge().toString(), 5, "0"))
            .add(p -> lpad(p.getAge().toString(), 5, "0"))
            .add(p -> lpad(p.getAge().toString(), 5, "0"));

    FixedMarkupLineAggregator<Person> aggregatorDecimal = new FixedMarkupLineAggregator<Person>()
            .add(p -> format(p.getMoney(), "nnnff"))
            .add(p -> format(p.getMoney(), "nnnff"))
            .add(p -> format(p.getMoney(), "nnnff"))
            .add(p -> format(p.getMoney(), "nnnff"))
            .add(p -> format(p.getMoney(), "nnnff"))
            .add(p -> format(p.getMoney(), "nnnff"));

    FixedMarkupLineAggregator<Person> aggregatorDate = new FixedMarkupLineAggregator<Person>()
            .add(p -> format(p.getBirthday(), "yyyy"))
            .add(p -> format(p.getBirthday(), "yyyyMMdd"))
            .add(p -> format(p.getBirthday(), "yyyy"))
            .add(p -> format(p.getBirthday(), "yyyy"))
            .add(p -> format(p.getBirthday(), "yyyy"))
            .add(p -> format(p.getBirthday(), "yyyy"));


    PassThroughLineAggregator passThroughLineAggregator = new PassThroughLineAggregator();

    @Test
    @PerfTest(invocations=1000000)
    public void testRun() throws Exception {
        aggregator.aggregate(new Person("qqq", "wwwww", 1 , Double.valueOf(1.123f)));
    }

    @Test
    @PerfTest(invocations=1000000)
    public void testRunConstAggregation() throws Exception {
        aggregatorConst.aggregate(new Person("qqq", "wwwww", 1 , Double.valueOf(1.123f)));
    }

    @Test
    @PerfTest(invocations=1000000)
    public void testRunStringAggregation() throws Exception {
        aggregatorString.aggregate(new Person("qqq", "wwwww", 1 , Double.valueOf(1.123f)));
    }

    @Test
    @PerfTest(invocations=1000000)
    public void testRunIntAggregation() throws Exception {
        aggregatorInt.aggregate(new Person("qqq", "wwwww", 1 , Double.valueOf(1.123f)));
    }

    @Test
    @PerfTest(invocations=1000000)
    public void testRunDoubleAggregation() throws Exception {
        aggregatorDecimal.aggregate(new Person("qqq", "wwwww", 1 , Double.valueOf(1.123f)));
    }

    @Test
    @PerfTest(invocations=1000000)
    public void testRunDateAggregation() throws Exception {
        aggregatorDate.aggregate(new Person("qqq", "wwwww", 1 , Double.valueOf(1.123f)));
    }


    @Test
    @PerfTest(invocations=1000000)
    public void testRunSimpleAggregator() throws Exception {
        passThroughLineAggregator.aggregate(new Person("qqq", "wwwww", 1 , Double.valueOf(1.123f)));
    }
}
