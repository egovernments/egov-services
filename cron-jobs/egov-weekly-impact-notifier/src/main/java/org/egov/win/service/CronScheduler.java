package org.egov.win.service;

import java.util.Timer;

import org.springframework.beans.factory.annotation.Value;

public class CronScheduler {
	
	@Value("${weekly.impact.notifier.delay}")
	private Long delay;
	
	public void scheduleJob() {
		Timer task = new Timer();
		CronJob job = new CronJob();
		task.scheduleAtFixedRate(job, 0, delay);	
	}

}
