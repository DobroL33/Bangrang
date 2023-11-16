package com.ssafy.bangrang;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBatchTest
@SpringBootTest
public class BatchTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Disabled
    @Test
    @DisplayName("tasklet step success")
    void tasklet_step_success() throws Exception{
        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("first step");

        //then
        System.out.println("jobExecution.getStatus() = " + jobExecution.getStatus());
        assertEquals(BatchStatus.COMPLETED,jobExecution.getStatus());
    }

    @Test
    @DisplayName("chunk step1 success")
    void chunk_step1_success() throws Exception {
        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("region_map_area_step");

        //then
        //then
        System.out.println("jobExecution.getStatus() = " + jobExecution.getStatus());
        assertEquals(BatchStatus.COMPLETED,jobExecution.getStatus());
    }

    @Test
    @DisplayName("chunk step2 success")
    void chunk_step2_success() throws Exception {
        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("member_ranking_step");

        //then
        //then
        System.out.println("jobExecution.getStatus() = " + jobExecution.getStatus());
        assertEquals(BatchStatus.COMPLETED,jobExecution.getStatus());
    }

    @Test
    @DisplayName("chunk step3 success")
    void chunk_step3_success() throws Exception {
        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("post_fcm_step");

        //then
        System.out.println("jobExecution.getStatus() = " + jobExecution.getStatus());
        assertEquals(BatchStatus.COMPLETED,jobExecution.getStatus());
    }
}
