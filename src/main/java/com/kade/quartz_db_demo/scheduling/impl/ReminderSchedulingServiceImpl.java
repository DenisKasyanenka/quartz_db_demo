package com.kade.quartz_db_demo.scheduling.impl;

import com.kade.quartz_db_demo.scheduling.ReminderJob;
import com.kade.quartz_db_demo.scheduling.ReminderSchedulingService;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Service
public class ReminderSchedulingServiceImpl implements ReminderSchedulingService {


    @Autowired
    SchedulerFactoryBean schedulerFactoryBean;


    @PostConstruct
    public void init() {
        createSchedulingInstanceInDb();
    }

    @Override
    public void createSchedulingInstanceInDb() {


        for (int i = 0; i < 100; i++) {

            LocalDateTime afterSixSeconds = LocalDateTime.now().plusSeconds(6);

            String firingTimeStr = afterSixSeconds.format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm:ss.SSS"));
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobDetail job = newJob(ReminderJob.class).withIdentity("Test job name: " + firingTimeStr + " # " + i).build();
            job.getJobDataMap().put("setToFireAt", firingTimeStr);
            job.getJobDataMap().put("testMessage", "for test purposes");

            setTrigger(afterSixSeconds, firingTimeStr, scheduler, job);
        }
    }

    private void setTrigger(LocalDateTime date, String firingTimeStr, Scheduler scheduler, JobDetail job) {
        String triggerName = "Test job name: " + firingTimeStr;
        Trigger trigger = newTrigger().withIdentity(triggerName)
                .startAt(Date.from(date.toInstant(OffsetDateTime.now(ZoneId.systemDefault()).getOffset())))
                .withSchedule(simpleSchedule())
                .build();

        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {

        }
        schedulerFactoryBean.setTriggers(trigger);
    }
}
