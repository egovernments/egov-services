package org.egov.win.service;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.egov.win.model.Body;
import org.egov.win.model.Email;
import org.egov.win.model.PGR;
import org.egov.win.model.PT;
import org.egov.win.model.StateWide;
import org.egov.win.model.TL;
import org.egov.win.model.WaterAndSewerage;

public class CronService {
	
	
	public void formatEmail(Email email) {
        Template t = getVelocityTemplate();
        VelocityContext context = new VelocityContext();
        buildEmailBody(email.getBody(), context);
        StringWriter writer = new StringWriter();
        t.merge( context, writer );
        send(email, writer);
	}
	
	private void send(Email email, StringWriter writer) {
        System.out.println(writer.toString()); 

	}
	
	
	private Template getVelocityTemplate() {
        VelocityEngine ve = new VelocityEngine();
        ve.init();
        Template t = ve.getTemplate( "helloworld.vm" );
        
        return t;
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
		if(null != body.getWaterAndSewerage())
			enrichWSData(body.getWaterAndSewerage(), context);
		
	}
	
	private void enrichStateWideData(StateWide stateWide, VelocityContext context) {
		fillData(stateWide.getCitizenAppDownloads(), context);
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
	
	private void enrichWSData(WaterAndSewerage ws, VelocityContext context) {
		fillData(ws.getRevenueCollected(), context);
		fillData(ws.getServiceApplied(), context);
		fillData(ws.getUlbCovered(), context);
	}
	
	private void fillData(List<Map<String, String>> dataFromQuery, VelocityContext context) {
		dataFromQuery.forEach(record -> {
			for(String key: record.keySet())
				context.put(key, record.get(key));
		});
	}

}
