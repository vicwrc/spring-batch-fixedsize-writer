package hello;

import javax.sql.DataSource;

import hello.writer.FixedMarkupLineAggregator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.repository.support.SimpleJobRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;


import static hello.writer.FixedMarkupLineAggregator.rpad;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {


    @Value("classpath:org/springframework/batch/core/schema-drop-hsqldb.sql")
    private Resource dropSchemaBatch;

    @Value("classpath:org/springframework/batch/core/schema-hsqldb.sql")
    private Resource createSchemaBatch;

    // job repository override - need to check logic here
    @Bean
    public JobRepository jobRepository(DataSource dataSource, PlatformTransactionManager transactionManager) throws Exception {
        JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();

        jobRepositoryFactoryBean.setDataSource(dataSource);
        jobRepositoryFactoryBean.setDatabaseType("HSQL");
        jobRepositoryFactoryBean.setTransactionManager(transactionManager);

        return jobRepositoryFactoryBean.getObject();
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(dropSchemaBatch);
        populator.addScript(createSchemaBatch);

        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(populator);
        return initializer;
    }

    // tag::readerwriterprocessor[]
    @Bean
    public ItemReader<Person> reader() {
        FlatFileItemReader<Person> reader = new FlatFileItemReader<Person>();
        reader.setResource(new ClassPathResource("sample-data.csv"));
        reader.setLineMapper(new DefaultLineMapper<Person>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[]{"firstName", "lastName"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
                setTargetType(Person.class);
            }});
        }});
        return reader;
    }

    @Bean
    public ItemProcessor<Person, Person> processor() {
        return new PersonItemProcessor();
    }

    @Bean
    @JobScope
    public FlatFileFooterCallback footer(@Value("#{jobExecutionContext[footer]}") String footerValue) {
        return writer -> writer.write(
                new FixedMarkupLineAggregator<Person>()
                .add(p -> rpad(footerValue, 5))
                .add(p -> rpad(footerValue, 10, "0"))
                .add(p -> "123").aggregate(null));
    }

    @Bean
    @JobScope
    public FlatFileHeaderCallback header(@Value("#{jobExecutionContext[header]}") String headerValue) {
        return writer -> writer.write(
                new FixedMarkupLineAggregator<Person>()
                .add(p -> rpad(headerValue, 10, "0"))
                .add(p -> "123").aggregate(null));
    }

    @Bean
    @JobScope
    public FlatFileItemWriter<Person> writer(DataSource dataSource, FlatFileFooterCallback footer, FlatFileHeaderCallback header) {
        FlatFileItemWriter<Person> writer = new FlatFileItemWriter<>();
        writer.setLineAggregator(
                new FixedMarkupLineAggregator<Person>()
                        .add(p -> rpad(p.getFirstName(), 5))
                        .add(p -> rpad(p.getLastName(), 5, "0"))
                        .add(p -> "123")
        );
        writer.setResource(new FileSystemResource("out.dat"));
        writer.setHeaderCallback(header);
        writer.setFooterCallback(footer);
        return writer;
    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job importUserJob(JobBuilderFactory jobs, Step s1, JobExecutionListener listener) {
        return jobs.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(s1)
                .end()
                .build();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<Person> reader,
                      ItemWriter<Person> writer, ItemProcessor<Person, Person> processor) {
        return stepBuilderFactory.get("step1")
                .<Person, Person>chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
    // end::jobstep[]

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
