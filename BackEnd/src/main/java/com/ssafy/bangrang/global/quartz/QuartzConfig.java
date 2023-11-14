package com.ssafy.bangrang.global.quartz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class QuartzConfig {

    private final Job myRegionRankingJob;
    private final JobLauncher jobLauncher;

//    @Scheduled(c ron = "0 0 0 * * ?")
    @Scheduled(cron = "0/10 * * * * *")
    public void executeJob() {
        try {

            log.info("myRegionRankingJob start");
            jobLauncher.run(
                    myRegionRankingJob,
                    new JobParametersBuilder()
                            .addString("datetime", LocalDateTime.now().toString())
                            .toJobParameters()  // job parameter 설정
            );
            log.info("myRegionRankingJob end");

        } catch (JobExecutionException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
}
