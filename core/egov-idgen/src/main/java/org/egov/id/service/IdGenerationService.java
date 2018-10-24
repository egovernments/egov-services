package org.egov.id.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.egov.id.config.PropertiesManager;
import org.egov.id.model.CityCodeNotFoundException;
import org.egov.id.model.IDSeqNotFoundException;
import org.egov.id.model.IDSeqOverflowException;
import org.egov.id.model.IdGenerationRequest;
import org.egov.id.model.IdGenerationResponse;
import org.egov.id.model.IdRequest;
import org.egov.id.model.IdResponse;
import org.egov.id.model.InvalidIDFormatException;
import org.egov.id.model.RequestInfo;
import org.egov.id.model.ResponseInfoFactory;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
	PropertiesManager propertiesManager;
	
	@Autowired
	private ResponseInfoFactory responseInfoFactory;
	
	@Autowired
	private MdmsService mdmsService;

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
		String idFormat = getIdFormat(idRequest, requestInfo);
		if (StringUtils.isEmpty(idFormat))
			throw new CustomException("ID_NOT_FOUND",
					"No Format/sequence is avavilable in the db for the given name and tenant");
		idRequest.setFormat(idFormat);
		return getFormattedId(idRequest, requestInfo);
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
			if (attributeName.substring(0, 3).equalsIgnoreCase("seq")) {
				idFormat = idFormat.replace("[" + attributeName + "]",
						generateSequenceNumber(attributeName, requestInfo));
			} else if (attributeName.substring(0, 2).equalsIgnoreCase("fy")) {
				idFormat = idFormat.replace("[" + attributeName + "]",
						generateFinancialYearDateFormat(attributeName, requestInfo));
			} else if (attributeName.substring(0, 2).equalsIgnoreCase("cy")) {
				idFormat = idFormat.replace("[" + attributeName + "]",
						generateCurrentYearDateFormat(attributeName, requestInfo));
			} else if(attributeName.substring(0, 4).equalsIgnoreCase("city")) {
                idFormat = idFormat.replace("[" + attributeName + "]",
                        getCityCode(idRequest.getTenantId(),requestInfo));
            } else {
				idFormat = idFormat.replace("[" + attributeName + "]", generateRandomText(attributeName, requestInfo));
			}
		}
		formattedId = idFormat.toString().toUpperCase();
		return formattedId;
	}

	/**
	 * Description : This method to generate current financial year in given
	 * format
	 * 
	 * @param dateFormat
	 * @param requestInfo
	 * @return formattedDate
	 */
	private String generateFinancialYearDateFormat(String financialYearFormat, RequestInfo requestInfo) {
		try {

			Date date = new Date();
			financialYearFormat = financialYearFormat.substring(financialYearFormat.indexOf(":") + 1);
			financialYearFormat = financialYearFormat.trim();
			String currentFinancialYear = null;
			String[] financialYearPatternArray;
			financialYearPatternArray = financialYearFormat.split("-");
			int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
			int preYear = 0;
			int postYear = 0;

			for (String yearPattern : financialYearPatternArray) {

				String formattedYear = null;
				SimpleDateFormat formatter = new SimpleDateFormat(yearPattern.trim());
				formattedYear = formatter.format(date);

				if (financialYearPatternArray[0] == yearPattern) {
					if (month > 3) {
						preYear = Integer.valueOf(formattedYear);
					} else {
						preYear = Integer.valueOf(formattedYear) - 1;
					}
				} else {
					if (month > 3) {
						postYear = Integer.valueOf(formattedYear) + 1;
					} else {
						postYear = Integer.valueOf(formattedYear);
					}
				}
			}
			currentFinancialYear = preYear + "-" + postYear;
			return currentFinancialYear;

		} catch (Exception e) {

			throw new InvalidIDFormatException(propertiesManager.getInvalidIdFormat(), requestInfo);

		}
	}

	/**
	 * Description : This method to generate current year date in given format
	 * 
	 * @param dateFormat
	 * @param requestInfo
	 * @return formattedDate
	 */
	private String generateCurrentYearDateFormat(String dateFormat, RequestInfo requestInfo) {
		try {

			Date date = new Date();
			dateFormat = dateFormat.trim();
			dateFormat = dateFormat.substring(dateFormat.indexOf(":") + 1);
			dateFormat = dateFormat.trim();
			SimpleDateFormat formatter = new SimpleDateFormat(dateFormat.trim());
			String formattedDate = formatter.format(date);
			return formattedDate;

		} catch (Exception e) {

			throw new InvalidIDFormatException(propertiesManager.getInvalidIdFormat(), requestInfo);

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
			throw new InvalidIDFormatException(propertiesManager.getInvalidIdFormat(), requestInfo);
		}
		Matcher matcher = Pattern.compile("\\{(.*?)\\}").matcher(regex);
		while (matcher.find()) {
			matchList.add(matcher.group(1));
		}
		if (matchList.size() > 0) {
			length = Integer.parseInt(matchList.get(0));
		}
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < length; i++) {
			stringBuilder.append(random.nextInt(25));
		}
		String randomTxt = stringBuilder.toString();
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
				throw new IDSeqNotFoundException(propertiesManager.getIdSequenceNotFound(), requestInfo);
			} else {
				throw new IDSeqOverflowException(propertiesManager.getIdSequenceOverflow(), requestInfo);
			}
		} finally {
			conn.close();
		}
		StringBuilder seqNumber = new StringBuilder(String.format("%06d", seqId));
		return seqNumber.toString();
	}


    /**
     * Description : method to get
     * @param requestInfo
     * @return city code
     * @throws Exception
     */
    private String getCityCode(String tenantId, RequestInfo requestInfo) throws Exception {
       return mdmsService.getCity(requestInfo, tenantId);
    }
}
