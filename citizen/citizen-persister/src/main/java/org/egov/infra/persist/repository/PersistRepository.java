package org.egov.infra.persist.repository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.egov.infra.persist.web.contract.JsonMap;
import org.egov.infra.persist.web.contract.TypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class PersistRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void persist(String query, List<JsonMap> jsonMaps, String jsonData) {
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(jsonData);
		JsonMap rootObj = jsonMaps.get(0);
		System.out.println("persist rootObj:" + rootObj);
		String rootObjPath = rootObj.getJsonPath();
		System.out.println("persist rootObjPath:" + rootObjPath);

		if (rootObjPath.contains("*"))
			persistList(query, jsonMaps, jsonData, document);
		else
			persistObject(query, jsonMaps, jsonData, document);

	}

	public void persistList(String query, List<JsonMap> jsonMaps, String jsonData, Object document) {

		// Object document =
		// Configuration.defaultConfiguration().jsonProvider().parse(jsonData);
		JsonMap rootObj = jsonMaps.get(0);
		System.out.println("persist rootObj:" + rootObj);
		String rootObjPath = rootObj.getJsonPath();
		System.out.println("persist rootObjPath:" + rootObjPath);

		String arrObjJsonPath = rootObjPath.substring(0, rootObjPath.lastIndexOf("*") + 1);
		System.out.println("persist arrObjJsonPath:" + arrObjJsonPath);
		List<LinkedHashMap<String, Object>> list = null;
		if (arrObjJsonPath != null && arrObjJsonPath.length() != 0)
			list = JsonPath.read(document, arrObjJsonPath);

		// JsonPath.parse(document).put("", "", "");
		System.out.println("list>>" + list);

		Object[] obj = null;
		List<Object[]> dbObjArray = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			obj = new Object[jsonMaps.size()];
			LinkedHashMap<String, Object> linkedHashMap = (LinkedHashMap<String, Object>) list.get(i);
			System.out.println("i:" + i + "linkedHashMap:" + linkedHashMap);
			for (int j = 0; j < jsonMaps.size(); j++) {
				JsonMap jsonMap = jsonMaps.get(j);
				String jsonPath = jsonMap.getJsonPath();
				TypeEnum type = jsonMap.getType();
				TypeEnum dbType = jsonMap.getDbType();
				System.out.println("jsonPath:" + jsonPath);
				boolean isJsonPathList = false;
				Object value = null;
				if (jsonPath.contains("*.")) {
					jsonPath = jsonPath.substring(jsonPath.lastIndexOf("*.") + 2);
					isJsonPathList = true;
				} else if (!(type.toString().equals(TypeEnum.CURRENTDATE.toString())
						|| jsonPath.startsWith("default"))) {
					value = JsonPath.read(document, jsonPath);
					System.out.println("else block value:" + value);
				}

				System.out.println("substring jsonPath:" + jsonPath);
				String[] objDepth = jsonPath.split("\\.");
				System.out.println("objDepth:" + Arrays.toString(objDepth));
				LinkedHashMap<String, Object> linkedHashMap1 = null;

				if (isJsonPathList) {
					for (int k = 0; k < objDepth.length; k++) {
						System.out.println("k:" + k + "linkedHashMap1:" + linkedHashMap1);

						if (objDepth.length > 1 && k != objDepth.length - 1) {
							linkedHashMap1 = (LinkedHashMap<String, Object>) linkedHashMap.get(objDepth[k]);
							System.out.println("k:" + k + "linkedHashMap1>>>" + linkedHashMap1);

							// System.out.println("linkedHashMap1:"+linkedHashMap1);
						}

						if (k == objDepth.length - 1) {
							System.out.println("if block :");
							if (linkedHashMap1 != null)
								value = linkedHashMap1.get(objDepth[k]);
							else
								value = linkedHashMap.get(objDepth[k]);
						}
						System.out.println("value::" + value);
					}
				}

				if (jsonPath.startsWith("default"))
					obj[j] = null;
				else if (type.toString().equals(TypeEnum.CURRENTDATE.toString()))
					obj[j] = new Date();
				else if (type.toString().equals(TypeEnum.LONG.toString())) {
					if (dbType == null)
						obj[j] = value;
					else if (dbType.equals(TypeEnum.DATE))
						obj[j] = new java.sql.Date(Long.parseLong(value.toString()));
				} else if (type.toString().equals(TypeEnum.DATE.toString())) {
					String date = value.toString();
					DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					java.util.Date startDate = null;
					try {
						startDate = df.parse(date);
						String newDateString = df.format(startDate);
						System.out.println("newDateString:" + newDateString);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					obj[j] = startDate;
				} else
					obj[j] = value;

			}
			System.out.println("Obj>>>" + Arrays.toString(obj));
			dbObjArray.add(obj);
		}

		try {
			System.out.println("query:" + query + ",object:" + dbObjArray.toString());
			jdbcTemplate.batchUpdate(query, dbObjArray);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}

	}

	public void persistObject(String query, List<JsonMap> jsonMaps, String jsonData, Object document) {

		Object[] obj = new Object[jsonMaps.size()];
		List<Object[]> dbObjArray = new ArrayList<>();
		for (int j = 0; j < jsonMaps.size(); j++) {
			JsonMap jsonMap = jsonMaps.get(j);
			String jsonPath = jsonMap.getJsonPath();
			TypeEnum type = jsonMap.getType();
			TypeEnum dbType = jsonMap.getDbType();
			System.out.println("jsonPath:" + jsonPath);
			boolean isJsonPathList = false;
			Object value = null;
			if (jsonPath.contains("*.")) {
				jsonPath = jsonPath.substring(jsonPath.lastIndexOf("*.") + 2);
				isJsonPathList = true;
			} else if (!(type.toString().equals(TypeEnum.CURRENTDATE.toString()) || jsonPath.startsWith("default"))) {
				value = JsonPath.read(document, jsonPath);
				System.out.println("else block value:" + value);
			}

			System.out.println("substring jsonPath:" + jsonPath);

			if (type.toString().equals(TypeEnum.JSON.toString())){
				ObjectMapper mapper = new ObjectMapper();
		        try {
		            String json = mapper.writeValueAsString(value);
		            System.out.println("JSON = " + json);
		            obj[j] = json;
		        } catch (JsonProcessingException e) {
		            e.printStackTrace();
		        }
			}
			else if (jsonPath.startsWith("default"))
				obj[j] = null;
			else if (type.toString().equals(TypeEnum.CURRENTDATE.toString())){
				System.out.println("CURRENTDATE type:"+type+","+"dbtype:"+dbType);
				if(dbType.toString().equals(TypeEnum.DATE))
				     obj[j] = new Date();
				else if(dbType.toString().equals(TypeEnum.LONG.toString()))
					 obj[j] = new Date().getTime();
			}
			else if (type.toString().equals(TypeEnum.LONG.toString())) {
				if (dbType == null)
					obj[j] = value;
				else if (dbType.equals(TypeEnum.DATE))
					obj[j] = new java.sql.Date(Long.parseLong(value.toString()));
			} else if (type.toString().equals(TypeEnum.DATE.toString())) {
				String date = value.toString();
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				java.util.Date startDate = null;
				try {
					startDate = df.parse(date);
					String newDateString = df.format(startDate);
					System.out.println("newDateString:" + newDateString);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				obj[j] = startDate;
			} else {
				
				obj[j] = value;
			}

		}
		System.out.println("Obj>>>" + Arrays.toString(obj));
		dbObjArray.add(obj);

		try {
			System.out.println("query:" + query + ",object:" + dbObjArray.toString());
			jdbcTemplate.batchUpdate(query, dbObjArray);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}

	}

}
