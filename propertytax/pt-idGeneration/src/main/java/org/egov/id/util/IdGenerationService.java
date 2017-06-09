package org.egov.id.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.egov.id.model.IdResultSet;
import org.egov.models.AttributeNotFoundException;
import org.egov.models.IdAttribute;
import org.egov.models.IdGenerationRequest;
import org.egov.models.IdRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

/**
 * Description : IdGenerationService have methods related to the IdGeneration
 * @author Pavan Kumar Kamma
 */
@Service
public class IdGenerationService {

	@Autowired
	DataSource dataSource;

	@Autowired
	private Environment environment;

	/**
	 * Description : This method for generating Id for property
	 * @param idGenReq
	 * @throws Exception
	 * @return generatedId
	 */
	public String generateId(IdGenerationRequest idGenReq) throws Exception {

		IdRequest idRequest = idGenReq.getIdRequest();
		List<IdAttribute> attributes = new ArrayList<IdAttribute>(idRequest.getAttributes());
		IdResultSet resultSet = new IdResultSet();
		//connection and prepared statement
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
		String generatedId = null;
		try{
			conn = DataSourceUtils.getConnection(dataSource);
			conn.setAutoCommit(false);
			//select the id type from the id generation table 
			StringBuffer idSelectQuery = new StringBuffer();
			idSelectQuery.append( "SELECT * FROM ") .append( environment.getProperty("id.generation.table"))
			.append(" WHERE (idtype=? and entity=? and tenentid=?) FOR UPDATE" );
			pst = conn.prepareStatement(idSelectQuery.toString());
			pst.setString(1,idRequest.getIdType());
			pst.setString(2,idRequest.getEntity());
			pst.setString(3,idRequest.getTenentId());
			rs = pst.executeQuery();
			while(rs.next()){
				resultSet.setTenentid(rs.getString("tenentid"));
				resultSet.setCurrentseqnum(rs.getString("currentseqnum"));
				resultSet.setEntity(rs.getString("entity"));
				resultSet.setId(rs.getString("id"));
				resultSet.setIdtype(rs.getString("idtype"));
				resultSet.setRegex(rs.getString("regex"));
			}
			generatedId = ValidateAttributesAndGetId(attributes, resultSet);
			//updating the sequence number of the id type
			StringBuffer idUpdateQuery = new StringBuffer();
			idUpdateQuery.append("UPDATE ").append(environment.getProperty("id.generation.table"))
			.append(" SET currentseqnum=? WHERE (idtype=? and entity=? and tenentid=?)");
			pst = conn.prepareStatement(idUpdateQuery.toString());
			pst.setInt(1,Integer.parseInt(resultSet.getCurrentseqnum())+1);
			pst.setString(2,idRequest.getIdType());
			pst.setString(3,idRequest.getEntity());
			pst.setString(4,idRequest.getTenentId());
			pst.executeUpdate();
			//committing the transaction
			conn.commit();
			conn.setAutoCommit(true);
		} finally{
			conn.close();
		}
		return generatedId;
	}

	/**
	 * Description : This method to check the attribute exists or not
	 * @param attributeList
	 * @param key
	 * @return true/false
	 */
	public boolean containsAttribute(List<IdAttribute> attributeList, String key) {

		for (IdAttribute attribute : attributeList) {
			if (attribute.getKey().equals(key)) {
				return true;
			}
		}
		return false;

	}

	/**
	 * Description : This method to generate random text
	 * @return randomTxt
	 */
	public String generateRandomText(){

		Random random = new Random();
		int k = random.nextInt(25)+65;
		int l = random.nextInt(25)+65;
		String randomTxt = Character.toString((char) k)+(char)l;
		return randomTxt;

	}

	/**
	 * Description : This method to validate the attributes and returns the generated id
	 * @param attributes
	 * @param resultSet
	 * @return regex
	 */
	public String ValidateAttributesAndGetId( List<IdAttribute> attributes, IdResultSet resultSet ){

		String regex = resultSet.getRegex();
		List<String> matchList = new ArrayList<String>();
		Pattern regExpPattern = Pattern.compile("\\{(.*?)\\}");
		Matcher regexMatcher = regExpPattern.matcher(regex);
		while (regexMatcher.find()) {//Finds Matching Pattern in String
			matchList.add(regexMatcher.group(1));//Fetching Group from String
		}
		for(String attributeName:matchList) {
			if(attributeName.equalsIgnoreCase("randNo") || attributeName.equalsIgnoreCase("seqNumber")){
				if(attributeName.equalsIgnoreCase("randNo")){
					regex = regex.replace("{randNo}", generateRandomText());
				} else {
					regex = regex.replace("{seqNumber}", resultSet.getCurrentseqnum());
				}
			} else {
				if(containsAttribute(attributes, attributeName)){
					for(IdAttribute attribute:attributes){  
						if(attribute.getKey().equalsIgnoreCase(attributeName)){
							regex = regex.replace("{"+attributeName+"}", attribute.getValue());
						} 
					}
				} else {
					//throw the exception for attribute not found
					AttributeNotFoundException attException = new AttributeNotFoundException();
					attException.setCustomMsg("attribute "+ attributeName +" Not found");
					throw attException;
				}
			}
		}
		return regex.toString().toUpperCase();

	}
}
