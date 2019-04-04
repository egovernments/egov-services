package org.egov.win.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.egov.win.model.Body;
import org.egov.win.model.Email;
import org.egov.win.model.EmailRequest;
import org.egov.win.model.PGR;
import org.egov.win.model.PGRChannelBreakup;
import org.egov.win.model.PT;
import org.egov.win.model.SearcherRequest;
import org.egov.win.model.StateWide;
import org.egov.win.model.TL;
import org.egov.win.producer.Producer;
import org.egov.win.repository.CronRepository;
import org.egov.win.utils.CronConstants;
import org.egov.win.utils.CronUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CronService {

	@Autowired
	private EmailService emailService;

	@Autowired
	private CronRepository repository;

	@Autowired
	private CronUtils utils;
	
	@Autowired
	private Producer producer;

	@Value("${egov.core.notification.email.topic}")
	private String emailTopic;

	@Value("${egov.impact.emailer.interval.in.secs}")
	private Long timeInterval;
	
	@Value("${egov.impact.emailer.email.to.address}")
	private String toAddress;
	
	@Value("${egov.impact.emailer.email.subject}")
	private String subject;

	public void fetchData() {
		Email email = getDataFromDb();
		String content = emailService.formatEmail(email);
		send(email, content);
	}

	private Email getDataFromDb() {
		Body body = new Body();
		enrichHeadersOfTheTable(body);
		enrichBodyWithStateWideData(body);
		enrichBodyWithPGRData(body);
		enrichBodyWithPTData(body);
		enrichBodyWithTLData(body);
		return Email.builder().body(body).build();
	}

	private void enrichHeadersOfTheTable(Body body) {
		String prefix = "Week";
		Integer noOfWeeks = 6;
		List<Map<String, Object>> header = new ArrayList<>();
		for (int week = 0; week < noOfWeeks; week++) {
			Map<String, Object> date = new HashMap<>();
			date.put(prefix + week,
					utils.getDayAndMonth((System.currentTimeMillis() - ((timeInterval * 1000) * week))));
			header.add(date);
		}
		body.setHeader(header);
	}

	private void enrichBodyWithStateWideData(Body body) {
		List<Map<String, Object>> data = getData(CronConstants.SEARCHER_SW);
		List<Map<String, Object>> ulbCovered = new ArrayList<>();
		List<Map<String, Object>> revenueCollected = new ArrayList<>();
		List<Map<String, Object>> servicesApplied = new ArrayList<>();
		List<Map<String, Object>> noOfCitizensResgistered = new ArrayList<>();
		for (Map<String, Object> record : data) {
			Map<String, Object> ulbCoveredPerWeek = new HashMap<>();
			Map<String, Object> revenueCollectedPerWeek = new HashMap<>();
			Map<String, Object> servicesAppliedPerWeek = new HashMap<>();
			Map<String, Object> noOfCitizensResgisteredPerWeek = new HashMap<>();
			String prefix = "Week";
			Integer noOfWeeks = 6;
			for (int week = 0; week < noOfWeeks; week++) {
				if (record.get("day").equals(prefix + week)) {
					ulbCoveredPerWeek.put("w" + week + "ulbc", record.get("ulbcovered"));
					revenueCollectedPerWeek.put("w" + week + "revcoll", record.get("revenuecollected"));
					servicesAppliedPerWeek.put("w" + week + "serapp", record.get("servicesapplied"));
					noOfCitizensResgisteredPerWeek.put("w" + week + "citreg", record.get("noofusersregistered"));
				}
			}
			ulbCovered.add(ulbCoveredPerWeek);
			revenueCollected.add(revenueCollectedPerWeek);
			servicesApplied.add(servicesAppliedPerWeek);
			noOfCitizensResgistered.add(noOfCitizensResgisteredPerWeek);
		}

		StateWide stateWide = StateWide.builder().noOfCitizensResgistered(noOfCitizensResgistered)
				.revenueCollected(revenueCollected).servicesApplied(servicesApplied).ulbCovered(ulbCovered).build();
		body.setStateWide(stateWide);
	}

	private void enrichBodyWithPGRData(Body body) {
		List<Map<String, Object>> data = getData(CronConstants.SEARCHER_PGR);
		List<Map<String, Object>> ulbCovered = new ArrayList<>();
		List<Map<String, Object>> totalComplaints = new ArrayList<>();
		List<Map<String, Object>> redressal = new ArrayList<>();
		for (Map<String, Object> record : data) {
			Map<String, Object> ulbCoveredPerWeek = new HashMap<>();
			Map<String, Object> totalComplaintsPerWeek = new HashMap<>();
			Map<String, Object> redressalPerWeek = new HashMap<>();
			String prefix = "Week";
			Integer noOfWeeks = 6;
			for (int week = 0; week < noOfWeeks; week++) {
				if (record.get("day").equals(prefix + week)) {
					ulbCoveredPerWeek.put("w" + week + "pgrulbc", record.get("ulbcovered"));
					totalComplaintsPerWeek.put("w" + week + "pgrtcmp", record.get("totalcomplaints"));
					redressalPerWeek.put("w" + week + "pgrredd", record.get("redressal"));
				}
			}
			ulbCovered.add(ulbCoveredPerWeek);
			totalComplaints.add(totalComplaintsPerWeek);
			redressal.add(redressalPerWeek);
		}
		PGR pgr = PGR.builder().redressal(redressal).totalComplaints(totalComplaints).ulbCovered(ulbCovered).build();
		enrichBodyWithPGRChannelData(body, pgr);
		body.setPgr(pgr);
	}

	private void enrichBodyWithPGRChannelData(Body body, PGR pgr) {
		List<Map<String, Object>> data = getData(CronConstants.SEARCHER_PGR_CHANNEL);
		List<Map<String, Object>> ivr = new ArrayList<>();
		List<Map<String, Object>> mobiileApp = new ArrayList<>();
		List<Map<String, Object>> webApp = new ArrayList<>();
		for (Map<String, Object> record : data) {
			Map<String, Object> ivrPerWeek = new HashMap<>();
			Map<String, Object> mobileAppPerWeek = new HashMap<>();
			Map<String, Object> webAppPerWeek = new HashMap<>();
			String prefix = "Week";
			Integer noOfWeeks = 6;
			for (int week = 0; week < noOfWeeks; week++) {
				if (record.get("day").equals(prefix + week)) {
					ivrPerWeek.put("w" + week + "pgrchnlivr", record.get("ivr"));
					mobileAppPerWeek.put("w" + week + "pgrchnlmapp", record.get("mobileapp"));
					webAppPerWeek.put("w" + week + "pgrchnlweb", record.get("webapp"));
				}
			}
			ivr.add(ivrPerWeek);
			mobiileApp.add(mobileAppPerWeek);
			webApp.add(webAppPerWeek);
		}

		PGRChannelBreakup channel = PGRChannelBreakup.builder().ivr(ivr).mobileApp(mobiileApp).webApp(webApp).build();
		pgr.setChannelBreakup(channel);
	}

	private void enrichBodyWithPTData(Body body) {
		List<Map<String, Object>> data = getData(CronConstants.SEARCHER_PT);
		List<Map<String, Object>> ulbCovered = new ArrayList<>();
		List<Map<String, Object>> revenueCollected = new ArrayList<>();
		List<Map<String, Object>> noOfProperties = new ArrayList<>();
		for (Map<String, Object> record : data) {
			Map<String, Object> ulbCoveredPerWeek = new HashMap<>();
			Map<String, Object> revenueCollectedPerWeek = new HashMap<>();
			Map<String, Object> noOfPropertiesPerWeek = new HashMap<>();
			String prefix = "Week";
			Integer noOfWeeks = 6;
			for (int week = 0; week < noOfWeeks; week++) {
				if (record.get("day").equals(prefix + week)) {
					ulbCoveredPerWeek.put("w" + week + "ptulbc", record.get("ulbcovered"));
					revenueCollectedPerWeek.put("w" + week + "ptrevcoll", record.get("revenuecollected"));
					noOfPropertiesPerWeek.put("w" + week + "ptnoofprp", record.get("noofpropertiescreated"));
				}
			}
			ulbCovered.add(ulbCoveredPerWeek);
			revenueCollected.add(revenueCollectedPerWeek);
			noOfProperties.add(noOfPropertiesPerWeek);
		}

		PT pt = PT.builder().noOfProperties(noOfProperties).ulbCovered(ulbCovered).revenueCollected(revenueCollected)
				.build();
		body.setPt(pt);
	}

	private void enrichBodyWithTLData(Body body) {
		List<Map<String, Object>> data = getData(CronConstants.SEARCHER_TL);
		List<Map<String, Object>> ulbCovered = new ArrayList<>();
		List<Map<String, Object>> licenseIssued = new ArrayList<>();
		for (Map<String, Object> record : data) {
			Map<String, Object> ulbCoveredPerWeek = new HashMap<>();
			Map<String, Object> licenseIssuedPerWeek = new HashMap<>();
			String prefix = "Week";
			Integer noOfWeeks = 6;
			for (int week = 0; week < noOfWeeks; week++) {
				if (record.get("day").equals(prefix + week)) {
					ulbCoveredPerWeek.put("w" + week + "tlulbc", record.get("ulbcovered"));
					licenseIssuedPerWeek.put("w" + week + "tllicissued", record.get("licenseissued"));
				}
			}
			ulbCovered.add(ulbCoveredPerWeek);
			licenseIssued.add(licenseIssuedPerWeek);
		}

		TL tl = TL.builder().ulbCovered(ulbCovered).licenseIssued(licenseIssued).build();
		body.setTl(tl);
	}

	private List<Map<String, Object>> getData(String defName) {
		StringBuilder uri = new StringBuilder();
		ObjectMapper mapper = utils.getObjectMapper();
		List<Map<String, Object>> data = new ArrayList<>();
		SearcherRequest request = utils.preparePlainSearchReq(uri, defName);
		Object response = repository.fetchResult(uri, request);
		try {
			log.info("Response from search: " + response);
			List<Object> dataParsedToList = mapper.convertValue(JsonPath.read(response, "$.data"), List.class);
			for (Object record : dataParsedToList) {
				data.add(mapper.convertValue(record, Map.class));
			}
		} catch (Exception e) {
			log.error("Exception while parsing to map: ", e);
			data = null;
		}
		return data;

	}

	private void send(Email email, String content) {
		email.setTo(toAddress);
		email.setSubject(subject);
		log.info("Email: "+email);
		EmailRequest request = EmailRequest.builder()
				.email(email.getTo()).subject(email.getSubject()).isHTML(true).body(content).build();
		producer.push(emailTopic, request);
	}

	public Template getVelocityTemplate() {
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		ve.init();
		Template t = ve.getTemplate("velocity/weeklyimpactflasher.vm");
		return t;
	}

}
