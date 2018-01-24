package org.egov.dataupload.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.dataupload.model.JobSearchRequest;
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
	
	@Autowired
	private UploadJobRowMapper uploadJobRowMapper;
	
	@Autowired
	private DataUploadQueryBuilder dataUploadQueryBuilder;

	public void createJob(UploaderRequest uploaderRequest){
		String query="Insert into EGDU_UPLOADREGISTRY(CODE, TENANTID, REQUESTFILE_PATH, MODULE_NAME, DEF_NAME, REQUESTER_NAME,"
				+ "STATUS,FILE_NAME,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
		UploadJob uploadJob = uploaderRequest.getUploadJobs().get(0);
		try{
			jdbcTemplate.update(query, new Object[] {uploadJob.getCode(), uploadJob.getTenantId(), uploadJob.getRequestFilePath(),
					uploadJob.getModuleName(), uploadJob.getDefName(), uploadJob.getRequesterName(), (StatusEnum.valueOf("NEW")).toString(), uploadJob.getRequestFileName(),
					uploaderRequest.getRequestInfo().getUserInfo().getId(), new Date().getTime(), uploaderRequest.getRequestInfo().getUserInfo().getId(), new Date().getTime()});
		}catch(Exception e){
			logger.error("Exception while creating job in db for job code: "+uploadJob.getCode(), e);
		}
	}
	
	public void updateJob(UploaderRequest uploaderRequest){
		String query="Update EGDU_UPLOADREGISTRY set START_TIME=?, END_TIME=?, TOTAL_ROWS=?, SUCCESS_ROWS=?, FAILED_ROWS=?, RESPONSEFILE_PATH=?, STATUS=?, REASON_FOR_FAILURE = ? where CODE=? AND TENANTID=?";
		UploadJob uploadJob = uploaderRequest.getUploadJobs().get(0);
		try{
			jdbcTemplate.update(query, new Object[] {uploadJob.getStartTime(), uploadJob.getEndTime(), uploadJob.getTotalRows(), uploadJob.getSuccessfulRows(),
					uploadJob.getFailedRows(), uploadJob.getResponseFilePath(), uploadJob.getStatus().toString(), uploadJob.getReasonForFailure(), uploadJob.getCode(), uploadJob.getTenantId()});
		}catch(Exception e){
			logger.error("Exception while updating job in db for job code: "+uploadJob.getCode(), e);
		}
	}
	
	public List<UploadJob> searchJob(JobSearchRequest jobSearchRequest){
		List preparedStatementValues= new ArrayList<>();
		String query = dataUploadQueryBuilder.getQuery(jobSearchRequest, preparedStatementValues);
		List<UploadJob> uploadJobs = new ArrayList<>();
		uploadJobs = jdbcTemplate.query(query, preparedStatementValues.toArray(), uploadJobRowMapper);
		
		return uploadJobs;
	}
}
