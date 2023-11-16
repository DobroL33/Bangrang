package com.ssafy.bangrang.domain.batch.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/batch")
public class BatchApi {
    private final Job myRegionRankingJob;
    private final JobLauncher jobLauncher;

    @GetMapping("/job")
    public String startJob(){
        log.info("Job Starts!");
//        jobLauncher.run("my_job");
        try {
            JobExecution jobExecution = jobLauncher.run(myRegionRankingJob, new JobParameters());
            return "Batch Job executed successfully with status: " + jobExecution.getStatus();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error executing Batch Job: " + e.getMessage();
        }
    }

}
