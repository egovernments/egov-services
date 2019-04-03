package org.egov;

import org.egov.win.service.CronService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CronTrigger implements ApplicationRunner {

	@Autowired
	private CronService service;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("Job STARTS......................");
		try {
			service.fetchData();
		}catch(Exception e) {
			log.error("Job failed!: ", e);
		}
	}

}
