package org.egov.id.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.egov.models.IDSeqNotFoundException;
import org.egov.models.IDSeqOverflowException;
import org.egov.models.IdGenerationRequest;
import org.egov.models.IdGenerationResponse;
import org.egov.models.IdRequest;
import org.egov.models.IdResponse;
import org.egov.models.InvalidIDFormatException;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

/**
 * Description : IdGenerationService have methods related to the IdGeneration
 * 
 * @author Pavan Kumar Kamma
 */
@Service
public class IdGenerationService {

	@Autowired
	DataSource dataSource;

	@Autowired
	Environment environment;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	/**
	 * Description : This method to generate idGenerationResponse
	 * 
	 * @param idGenerationRequest
	 * @return idGenerationResponse
	 * @throws Exception
	 */
	public IdGenerationResponse generateIdResponse(IdGenerationRequest idGenerationRequest) throws Exception {

		RequestInfo requestInfo = idGenerationRequest.getRequestInfo();
		List<IdRequest> idRequests = idGenerationRequest.getIdRequests();
		List<IdResponse> idResponses = new ArrayList<>();
		IdGenerationResponse idGenerationResponse = new IdGenerationResponse();

		for (IdRequest idRequest : idRequests) {
			String generatedId = generateIdFromIdRequest(idRequest, requestInfo);
			IdResponse idResponse = new IdResponse();
			idResponse.setId(generatedId);
			idResponses.add(idResponse);
			idGenerationResponse.setIdResponses(idResponses);
		}
		idGenerationResponse.setResponseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true));

		return idGenerationResponse;

	};

	/**
	 * Description : This method to generate id
	 * 
	 * @param idRequest
	 * @param requestInfo
	 * @return generatedId
	 * @throws Exception
	 */
	private String generateIdFromIdRequest(IdRequest idRequest, RequestInfo requestInfo) throws Exception {

		String generatedId = "";

		if (idRequest.getFormat() != null && !idRequest.getFormat().isEmpty()) {
			generatedId = getFormattedId(idRequest, requestInfo);
		} else {
			generatedId = getGeneratedId(idRequest, requestInfo);
		}

		return generatedId;
	}

	/**
	 * Description : This method to generate Id when format is unknown
	 * 
	 * @param idRequest
	 * @param requestInfo
	 * @return generatedId
	 * @throws Exception
	 */
	private String getGeneratedId(IdRequest idRequest, RequestInfo requestInfo) throws Exception {
		String IdFormat = getIdFormat(idRequest, requestInfo);
		idRequest.setFormat(IdFormat);
		String generatedId = getFormattedId(idRequest, requestInfo);
		return generatedId;
	}

	/**
	 * Description : This method to retrieve Id format
	 * 
	 * @param idRequest
	 * @param requestInfo
	 * @return idFormat
	 * @throws Exception
	 */
	private String getIdFormat(IdRequest idRequest, RequestInfo requestInfo) throws Exception {
		// connection and prepared statement
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
		String idFormat = null;
		try {
			conn = DataSourceUtils.getConnection(dataSource);
			conn.setAutoCommit(false);
			String idName = idRequest.getIdName();
			String tenantId = idRequest.getTenantId();
			// select the id format from the id generation table
			StringBuffer idSelectQuery = new StringBuffer();
			idSelectQuery.append("SELECT format FROM id_generator ").append(" WHERE idname=? and tenantid=?");
			pst = conn.prepareStatement(idSelectQuery.toString());
			pst.setString(1, idName);
			pst.setString(2, tenantId);
			rs = pst.executeQuery();
			if (rs.next()) {
				idFormat = rs.getString(1);
			} else {
				// querying for the id format with idname
				StringBuffer idNameQuery = new StringBuffer();
				idNameQuery.append("SELECT format FROM id_generator ").append(" WHERE idname=?");
				pst = conn.prepareStatement(idNameQuery.toString());
				pst.setString(1, idName);
				rs = pst.executeQuery();
				if (rs.next())
					idFormat = rs.getString(1);
			}
			// committing the transaction
			conn.commit();
			conn.setAutoCommit(true);
		} finally {
			conn.close();
		}
		return idFormat;
	}

	/**
	 * Description : This method to generate Id when format is known
	 * 
	 * @param idRequest
	 * @param requestInfo
	 * @return formattedId
	 * @throws Exception
	 */

	private String getFormattedId(IdRequest idRequest, RequestInfo requestInfo) throws Exception {

		String idFormat = idRequest.getFormat();
		List<String> matchList = new ArrayList<String>();
		Pattern regExpPattern = Pattern.compile("\\[(.*?)\\]");
		Matcher regExpMatcher = regExpPattern.matcher(idFormat);
		String formattedId = null;
		while (regExpMatcher.find()) {// Finds Matching Pattern in String
			matchList.add(regExpMatcher.group(1));// Fetching Group from String
		}
		for (String attributeName : matchList) {
			if (matchList.get(0) == attributeName) {
				idFormat = idFormat.replace("[" + attributeName + "]", generateDateFormat(attributeName, requestInfo));
			} else if (matchList.get(matchList.size() - 1) == attributeName) {
				idFormat = idFormat.replace("[" + attributeName + "]", generateRandomText(attributeName, requestInfo));
			} else {
				idFormat = idFormat.replace("[" + attributeName + "]",
						generateSequenceNumber(attributeName, requestInfo));
			}
		}
		formattedId = idFormat.toString().toUpperCase();
		return formattedId;
	}

	/**
	 * Description : This method to generate date in given format
	 * 
	 * @param dateFormat
	 * @param requestInfo
	 * @return formattedDate
	 */
	private String generateDateFormat(String dateFormat, RequestInfo requestInfo) {
		try {
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
			String formattedDate = formatter.format(date);
			return formattedDate;
		} catch (Exception e) {
			throw new InvalidIDFormatException(environment.getProperty("id.invalid.format"), requestInfo);
		}

	}

	/**
	 * Description : This method to generate random text
	 * 
	 * @param regex
	 * @param requestInfo
	 * @return randomTxt
	 */
	private String generateRandomText(String regex, RequestInfo requestInfo) {
		Random random = new Random();
		List<String> matchList = new ArrayList<String>();
		int length = 2;// default digits length
		try {
			Pattern.compile(regex);
		} catch (Exception e) {
			throw new InvalidIDFormatException(environment.getProperty("id.invalid.format"), requestInfo);
		}
		Matcher matcher = Pattern.compile("\\{(.*?)\\}").matcher(regex);
		while (matcher.find()) {
			matchList.add(matcher.group(1));
		}
		if (matchList.size() > 0) {
			length = Integer.parseInt(matchList.get(0));
		}
		int randomNo = random.nextInt(Integer.MAX_VALUE);
		String randomTxt = "" + randomNo;
		randomTxt = randomTxt.substring(0, length);
		return randomTxt;
	}

	/**
	 * Description : This method to generate sequence number
	 * 
	 * @param sequenceName
	 * @param requestInfo
	 * @return seqNumber
	 */
	private String generateSequenceNumber(String sequenceName, RequestInfo requestInfo) throws Exception {

		String sequenceSql = "SELECT nextval('" + sequenceName + "')";
		// connection and prepared statement
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
		Long seqId = null;
		try {
			conn = DataSourceUtils.getConnection(dataSource);
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sequenceSql);
			rs = pst.executeQuery();
			if (rs.next())
				seqId = rs.getLong(1);
			// committing the transaction
			conn.commit();
			conn.setAutoCommit(true);
		} catch (Exception e) {
			if (rs == null) {
				throw new IDSeqNotFoundException(environment.getProperty("id.sequence.notfound"), requestInfo);
			} else {
				throw new IDSeqOverflowException(environment.getProperty("id.sequence.overflow"), requestInfo);
			}
		} finally {
			conn.close();
		}
		StringBuilder seqNumber = new StringBuilder(String.format("%06d", seqId));
		return seqNumber.toString();
	}
}
