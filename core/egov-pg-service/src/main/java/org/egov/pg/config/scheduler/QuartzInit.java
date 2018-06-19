package org.egov.pg.config.scheduler;

import org.flywaydb.core.api.callback.Callback;
import org.flywaydb.core.api.callback.Context;
import org.flywaydb.core.api.callback.Event;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.List;

/**
 * Start quartz scheduler after flyway scripts have executed
 */
@Configuration
public class QuartzInit implements Callback {

    @Autowired
    private SchedulerFactoryBean quartzScheduler;

    @Autowired
    private List<Trigger> triggersList;

    @Override
    public boolean supports(Event event, Context context) {
        return event.equals(Event.AFTER_MIGRATE);
    }

    @Override
    public boolean canHandleInTransaction(Event event, Context context) {
        return true;
    }

    @Override
    public void handle(Event event, Context context) {
        Trigger[] triggers = (Trigger[]) triggersList.toArray();
        quartzScheduler.setTriggers(triggers);
    }
}
