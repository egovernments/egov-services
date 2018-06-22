package org.egov.pg.service.jobs.earlyReconciliation;

import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

/**
 * Scheduled to run every 15 minutes
 */
@Configuration
public class EarlyReconciliationJobConfig {

    @Bean
    JobDetailFactoryBean earlyReconciliationJob() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(EarlyReconciliationJob.class);
        jobDetailFactory.setGroup("status-update");
        jobDetailFactory.setDurability(true);
        return jobDetailFactory;
    }

    @Bean
    @Autowired
    CronTriggerFactoryBean earlyReconciliationTrigger(JobDetail earlyReconciliationJob) {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(earlyReconciliationJob);
        cronTriggerFactoryBean.setCronExpression("0 0/15 * * * ?");
        cronTriggerFactoryBean.setGroup("status-update");
        return cronTriggerFactoryBean;
    }


}
