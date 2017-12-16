package org.egov.dataupload.repository;

import java.util.Date;

import org.egov.dataupload.model.UploadJob;
import org.egov.dataupload.model.UploadJob.StatusEnum;
import org.egov.dataupload.model.UploaderRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UploadRegistryRepository {
	
	public static final Logger logger = LoggerFactory.getLogger(UploadRegistryRepository.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void createJob(UploaderRequest uploaderRequest){
		String query="insert into EGDU_UPLOADREGISTRY(CODE, TENANTID, REQUESTFILE_PATH, MODULE_NAME, DEF_NAME, REQUESTER_NAME,"
				+ "STATUS,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
		UploadJob uploadJob = uploaderRequest.getUploadJobs().get(0);
		try{
			jdbcTemplate.update(query, new Object[] {uploadJob.getCode(), uploadJob.getTenantId(), uploadJob.getRequestFilePath(),
					uploadJob.getModuleName(), uploadJob.getDefName(), uploadJob.getRequesterName(), (StatusEnum.valueOf("NEW")).toString(),
					uploaderRequest.getRequestInfo().getUserInfo().getId(), new Date().getTime(), uploaderRequest.getRequestInfo().getUserInfo().getId(), new Date().getTime()});
		}catch(Exception e){
			logger.error("Exception while creating job in db for job code: "+uploadJob.getCode(), e);
		}
	}
}
