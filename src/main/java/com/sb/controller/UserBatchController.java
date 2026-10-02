package com.sb.controller;


import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/start")
public class UserBatchController {

    private JobOperator jobOperator;
    private Job job;

    public UserBatchController(JobOperator jobOperator, Job job) {
        this.jobOperator = jobOperator;
        this.job = job;
    }


    @GetMapping("")
    public String startJobs(){
        try{
            JobParameters jobParameters = new JobParametersBuilder().
                    addString("jobName", "UserBatchController").toJobParameters();
            JobExecution execution= jobOperator.start(job,jobParameters);

            System.out.println("Job started "+execution.getExitStatus().getExitDescription());
            return execution.getExitStatus().getExitDescription();

        }catch (Exception e){
            e.printStackTrace();
            return "Error";
        }
    }
}
