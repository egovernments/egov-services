package org.egov.win.service;

import java.util.List;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.egov.win.model.Body;
import org.egov.win.model.Email;
import org.egov.win.model.PGR;
import org.egov.win.model.PT;
import org.egov.win.model.StateWide;
import org.egov.win.model.TL;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {
	
	public VelocityContext formatEmail(Email email) {
        VelocityContext context = new VelocityContext();
        buildEmailBody(email.getBody(), context);
        buildEmailHeader(email);
        return context;
	}
	

	
	private void buildEmailHeader(Email email) {
		email.setFrom("abc@abc.com");
		email.setTo("xyz@xyz.com");
		email.setSubject("Impact by Rainmaker");
		email.setBcc("mno@mno.com");
	}
	
	private void buildEmailBody(Body body, VelocityContext context) {
		if(null != body.getStateWide()) 
			enrichStateWideData(body.getStateWide(), context);
		if(null != body.getPgr())
			enrichPGRData(body.getPgr(), context);
		if(null != body.getPt())
			enrichPTData(body.getPt(), context);
		if(null != body.getTl())
			enrichTLData(body.getTl(), context);
	}
	
	private void enrichStateWideData(StateWide stateWide, VelocityContext context) {
		fillData(stateWide.getNoOfCitizensResgistered(), context);
		fillData(stateWide.getRevenueCollected(), context);
		fillData(stateWide.getServicesApplied(), context);
		fillData(stateWide.getUlbCovered(), context);
	}
	
	private void enrichPGRData(PGR pgr, VelocityContext context) {
		fillData(pgr.getRedressal(), context);
		fillData(pgr.getTotalComplaints(), context);
		fillData(pgr.getUlbCovered(), context);
		fillData(pgr.getChannelBreakup().getIvr(), context);
		fillData(pgr.getChannelBreakup().getMobileApp(), context);
		fillData(pgr.getChannelBreakup().getWebApp(), context);
	}
	
	private void enrichPTData(PT pt, VelocityContext context) {
		fillData(pt.getNoOfProperties(), context);
		fillData(pt.getRevenueCollected(), context);
		fillData(pt.getUlbCovered(), context);
	}
	
	private void enrichTLData(TL tl, VelocityContext context) {
		fillData(tl.getLicenseIssued(), context);
		fillData(tl.getUlbCovered(), context);
	}
		
	private void fillData(List<Map<String, Object>> dataFromQuery, VelocityContext context) {
		log.info("dataFromQuery: "+dataFromQuery);
		dataFromQuery.forEach(record -> {
			for(String key: record.keySet())
				context.put(key, record.get(key));
		});
	}

}
