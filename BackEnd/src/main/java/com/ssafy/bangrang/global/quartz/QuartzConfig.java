package com.ssafy.bangrang.global.quartz;

import org.quartz.*;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.*;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.Date;

@Configuration
public class QuartzConfig {


}
