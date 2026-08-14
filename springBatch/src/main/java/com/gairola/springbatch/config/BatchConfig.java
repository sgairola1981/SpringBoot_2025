package com.gairola.springbatch.config;

import javax.sql.DataSource;

import com.gairola.springbatch.entity.Customer;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;

import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.infrastructure.item.file.mapping.BeanWrapperFieldSetMapper;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableConfigurationProperties(BatchProperties.class)
public class BatchConfig {

    @Bean
    public FlatFileItemReader<Customer> customerReader() {
        BeanWrapperFieldSetMapper<Customer> fieldSetMapper =
                new BeanWrapperFieldSetMapper<>();

        fieldSetMapper.setTargetType(Customer.class);

        return new FlatFileItemReaderBuilder<Customer>()
                .name("customerReader")
                .resource(new ClassPathResource("customers.csv"))
                .linesToSkip(1)
                .delimited()
                .delimiter(",")
                .names(
                        "customerId",
                        "customerName",
                        "email",
                        "status"
                )
                .fieldSetMapper(fieldSetMapper)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Customer> customerWriter(
            DataSource dataSource) {

        return new JdbcBatchItemWriterBuilder<Customer>()
                .dataSource(dataSource)
                .sql("""
                    INSERT INTO customer
                    (
                        customer_id,
                        customer_name,
                        email,
                        status
                    )
                    VALUES
                    (
                        :customerId,
                        :customerName,
                        :email,
                        :status
                    )
                    """)
                .beanMapped()
                .assertUpdates(true)
                .build();
    }

    @Bean
    public Step customerStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            FlatFileItemReader<Customer> customerReader,
            JdbcBatchItemWriter<Customer> customerWriter,
            BatchProperties properties) {

        return new StepBuilder("customerStep", jobRepository)
                .<Customer, Customer>chunk(properties.getChunkSize())
                .transactionManager(transactionManager)
                .reader(customerReader)
                .writer(customerWriter)
                .build();
    }

    @Bean
    public Job customerJob(
            JobRepository jobRepository,
            Step customerStep) {

        return new JobBuilder("customerJob", jobRepository)
                .start(customerStep)
                .build();
    }
}