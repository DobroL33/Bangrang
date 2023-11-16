package com.ssafy.bangrang.global.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
public class BatchExecuteJob implements Job {


    @Autowired
    private org.springframework.batch.core.Job myRegionRankingJob;
    @Autowired
    private JobLauncher jobLauncher;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        LocalDateTime startDateTime = LocalDateTime.now();
        log.info("Batch Job을 실행합니다..!"+ startDateTime);

        try {
            JobExecution jobExecution = jobLauncher.run(myRegionRankingJob, new JobParameters());
            log.info("Batch Job executed successfully with status: " + jobExecution.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error executing Batch Job: " + e.getMessage());
        }
//        log.info(myRegionRankingJob.getName());
//        log.info(jobLauncher.toString());
        LocalDateTime endDateTime = LocalDateTime.now();
        Duration duration = Duration.between(startDateTime, endDateTime);

        long seconds = duration.getSeconds();

        log.info("Batch Job을 완료했습니다..!"+seconds);

    }
}
