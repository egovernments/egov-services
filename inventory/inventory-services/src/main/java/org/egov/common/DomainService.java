package org.egov.common;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.egov.inv.domain.service.UomService;
import org.egov.inv.model.AuditDetails;
import org.egov.inv.model.Page;
import org.egov.inv.model.RequestInfo;
import org.egov.inv.model.ResponseInfo;
import org.egov.inv.model.ResponseInfo.StatusEnum;
import org.egov.inv.model.Uom;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DomainService {

    @Autowired
    protected LogAwareKafkaTemplate<String, Object> kafkaQue;
    
    protected SimpleDateFormat ddMMYYYYHHMMSS=new SimpleDateFormat("dd/MM/yyyy hh:mm::ss");
	protected SimpleDateFormat ddMMYYYY=new SimpleDateFormat("dd/MM/yyyy");
	
    private static final Logger LOG = LoggerFactory.getLogger(DomainService.class);
   
    @Value("${app.timezone}")
    private String timeZone;

    @Autowired
    private UomService uomService;
    
    private String assetSearchUrl="";
    
    private String assetListFiled="assets";
    
   
    

    public AuditDetails mapAuditDetails(RequestInfo requestInfo) {

        return AuditDetails.builder()
                .createdBy(requestInfo.getUserInfo().getId().toString())
                .lastModifiedBy(requestInfo.getUserInfo().getId().toString())
                .createdTime(requestInfo.getTs())
                .lastModifiedTime(requestInfo.getTs()).build();

    }

    public AuditDetails mapAuditDetailsForUpdate(RequestInfo requestInfo
    ) {

        return AuditDetails.builder()
                .lastModifiedBy(requestInfo.getUserInfo().getId().toString())
                .lastModifiedTime(requestInfo.getTs()).build();
    }


    public ResponseInfo getResponseInfo(RequestInfo requestInfo) {
        return new ResponseInfo().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
                .resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status(StatusEnum.SUCCESSFUL);
    }

    protected <T> Page getPage(Pagination<T> search) {
        return new Page().currentPage(search.getCurrentPage() + 1).pageSize(search.getPageSize())
                .totalResults(search.getTotalResults()).totalPages(search.getTotalPages());
    }

    public AuditDetails getAuditDetails(RequestInfo requestInfo, String action) {
        AuditDetails auditDetails = new AuditDetails();
        if (action.equalsIgnoreCase(Constants.ACTION_CREATE)) {
            if (requestInfo.getUserInfo() != null && requestInfo.getUserInfo().getId() != null) {
                auditDetails.createdBy(requestInfo.getUserInfo().getId().toString());
                auditDetails.lastModifiedBy(requestInfo.getUserInfo().getId().toString());
            }
            auditDetails.createdTime(new Date().getTime());
            auditDetails.lastModifiedTime(new Date().getTime());
            return auditDetails;
        } else {
            if (requestInfo.getUserInfo() != null && requestInfo.getUserInfo().getId() != null) {
                auditDetails.lastModifiedBy(requestInfo.getUserInfo().getId().toString());
            }
            auditDetails.lastModifiedTime(new Date().getTime());
            return auditDetails;
        }
    }

    public Uom getUom(String tenantId, String uomCode, RequestInfo requestInfo) {
        return uomService.getUom(tenantId, uomCode, requestInfo);
    }
    
 

    public Double getSaveConvertedQuantity(Double quantity, Double conversionFactor) {
        return quantity * conversionFactor;
    }

    public Double getSearchConvertedQuantity(Double quantity, Double conversionFactor) {
        return quantity / conversionFactor;
    }
    
    public Double getSaveConvertedRate(Double rate, Double conversionFactor) {
        return rate / conversionFactor;
    }

    public Double getSearchConvertedRate(Double rate, Double conversionFactor) {
        return rate * conversionFactor;
    }
    
    public  String 	toDateStr(Long epoch)
  	{
  		Date date=new Date(epoch);
  		String dateStr = ddMMYYYYHHMMSS.format(date);
  		LOG.info("date for epoch "+epoch+" is: "+dateStr);
  		return dateStr;
  	}
    
    public Long currentEpochWithoutTime()   
    {
  	  try {
  		  String dateStr = ddMMYYYY.format(new Date());
  		  Date date = ddMMYYYY.parse(dateStr);
  		  return date.getTime();
  	} catch (ParseException e) {
  		// TODO Auto-generated catch block
  		e.printStackTrace();
  		return null;
  	}
  	 
    }
}
