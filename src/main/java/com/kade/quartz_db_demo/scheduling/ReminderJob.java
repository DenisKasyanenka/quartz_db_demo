package com.kade.quartz_db_demo.scheduling;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.LoggerFactory;

public class ReminderJob implements Job {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(ReminderJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();

        String timeToFireEvent = (String) dataMap.get("setToFireAt");
        String testMessage = (String) dataMap.get("testMessage");

        LOG.info("Fire saved event. Set up to {}. Message: {}", timeToFireEvent, testMessage);

    }
}
