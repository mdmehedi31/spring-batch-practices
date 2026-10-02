package com.sb.config;


import com.sb.entity.UserEntity;
import com.sb.respository.UserRepository;
import org.apache.catalina.User;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.data.RepositoryItemWriter;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.LineMapper;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.infrastructure.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.infrastructure.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class UserBatchConfig {


    private UserRepository userRepository;

    public UserBatchConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Bean
    @StepScope
    public FlatFileItemReader<UserEntity> reader(@Value("#{jobParameters['campaignId']}") String campaignId) {

        System.out.println("Config::  campaignId: " + campaignId);
        return new FlatFileItemReaderBuilder<UserEntity>()
                .name("userItemReader1")
                .resource(new ClassPathResource("people-1000.csv"))
                .linesToSkip(1)
                .lineMapper(lineMapper())
                .targetType(UserEntity.class)
                .build();
    }

    private LineMapper<UserEntity> lineMapper() {
        DefaultLineMapper<UserEntity> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "userId", "firstName", "lastName", "gender", "email", "phone", "dateOfBirth", "jobTitle");

        BeanWrapperFieldSetMapper<UserEntity> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(UserEntity.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

    @Bean
    public UserProcessor processor() {
        return new UserProcessor();
    }

    @Bean
    RepositoryItemWriter<UserEntity> writer() {

        RepositoryItemWriter<UserEntity> writer = new RepositoryItemWriter<>(userRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Job job(JobRepository jobRepository, Step step) {
        return new JobBuilder("importPersons1", jobRepository)
                .start(step)
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepository) {



        return  new StepBuilder("csv-import-step1", jobRepository)
                .<UserEntity, UserEntity>chunk(10)
                .reader(reader(null))
                .processor(processor())
                .writer(writer())
                .build();

    }


}
