package org.egov.infra.persist.repository;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.persist.web.contract.JsonMap;
import org.egov.infra.persist.web.contract.TypeEnum;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;

@Repository
@Slf4j
public class PersistRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void persist(String query, List<JsonMap> jsonMaps, String jsonData, String basePath) {
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(jsonData);

		if (basePath != null && basePath.contains("*"))
			persistList(query, jsonMaps, basePath, document);
		else
			persistObject(query, jsonMaps, jsonData, document);

	}

	public void persistList(String query, List<JsonMap> jsonMaps, String basePath, Object document) {
		
		String basePathForNullCheck = null;
		
		if(!basePath.endsWith("*")) {
			basePathForNullCheck = new String();
			basePathForNullCheck = basePathForNullCheck.concat(basePath);
			log.info("basePathForNullCheck before Substr::"+basePathForNullCheck);
			basePathForNullCheck = basePathForNullCheck.substring(basePath.lastIndexOf("*.")+2, basePathForNullCheck.length());
			log.info("basePathForNullCheck after Substr::"+basePathForNullCheck);
		}
		
		
		log.debug("persistList arrObjJsonPath:" + basePath);
		basePath=basePath.substring(0, basePath.lastIndexOf(".*")+2);
		log.debug("basePath::"+basePath);
		List<LinkedHashMap<String, Object>> list = null;
		if (basePath != null && basePath.length() != 0)
			list = JsonPath.read(document, basePath);

		log.debug("persistList list::" + list);

		Object[] obj = null;
		List<Object[]> dbObjArray = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			obj = new Object[jsonMaps.size()];
			LinkedHashMap<String, Object> linkedHashMap = (LinkedHashMap<String, Object>) list.get(i);
			if(linkedHashMap == null) 
				continue;
			
			boolean isChildObjNull = false;
			if(basePathForNullCheck != null) {
				String [] baseObjectsForNullCheck = basePathForNullCheck.split("\\.");
				LinkedHashMap<String, Object> mapOfData = new LinkedHashMap<>();
				mapOfData.putAll(linkedHashMap);
				for(String baseObjectForNullCheck : baseObjectsForNullCheck) {
					mapOfData = (LinkedHashMap<String, Object>)mapOfData.get(baseObjectForNullCheck);
					if(mapOfData == null) {
						isChildObjNull = true;
						break;
					}
				}
			}
			if(isChildObjNull) 
				continue;
			
			log.info("i:" + i + "linkedHashMap:" + linkedHashMap);
			for (int j = 0; j < jsonMaps.size(); j++) {
				JsonMap jsonMap = jsonMaps.get(j);
				String jsonPath = jsonMap.getJsonPath();
				TypeEnum type = jsonMap.getType();
				TypeEnum dbType = jsonMap.getDbType();
				System.out.println("jsonPath:" + jsonPath);
				boolean isJsonPathList = false;
				Object value = null;
				log.debug("type:" + type + " dbType:" + dbType);
				
				if(type == null) {
					type = TypeEnum.STRING;
				}

				if (jsonPath != null && jsonPath.contains("{")) {
					//String parentPath = jsonMap.getJsonPath()()
					String attribute = jsonPath.substring(jsonPath.indexOf("{") + 1, jsonPath.indexOf("}"));
					log.info("persistList atribute:" + attribute);
					jsonPath = jsonPath.replace("{".concat(attribute).concat("}"),"\""+linkedHashMap.get(attribute).toString()+"\"");
					log.info("persistList parentPath:" + jsonPath);
					JSONArray jsonArray = JsonPath.read(document, jsonPath);
					log.info("ParentPath jsonArray:" + jsonArray);
					obj[j] = jsonArray.get(0);
					/*String[] objDepth = jsonPath.split("\\.");
					value = filterList.get(0).get(objDepth[objDepth.length-1]);
					//value = documentContext.read(jsonPath);
					log.info("ParentPath value:" + value);*/
					continue;
					
				} else if ((type.toString().equals(TypeEnum.ARRAY.toString()))
						&& dbType.toString().equals(TypeEnum.STRING.toString())) {
					List<Object> list1 = JsonPath.read(document, jsonPath);
					value = StringUtils.join(list1.get(i), ",");
					value=value.toString().substring(2, value.toString().lastIndexOf("]")-1).replace("\"", "");
				} else if (type.toString().equals(TypeEnum.CURRENTDATE.toString())) {
					if (dbType.toString().equals(TypeEnum.DATE.toString()))
						obj[j] = new Date();
					else if (dbType.toString().equals(TypeEnum.LONG.toString()))
						obj[j] = new Date().getTime();
					continue;
				} else if (jsonPath.contains("*.")) {
					jsonPath = jsonPath.substring(jsonPath.lastIndexOf("*.") + 2);
					log.info("jsonPath.contains(*) jsonpath:" + jsonPath);
					isJsonPathList = true;
				} else if (!(type.toString().equals(TypeEnum.CURRENTDATE.toString())
						|| jsonPath.startsWith("default"))) {
					value = JsonPath.read(document, jsonPath);
					log.debug("else block value:" + value);
				}

				log.debug("substring jsonPath:" + jsonPath);
				String[] objDepth = jsonPath.split("\\.");
				log.debug("objDepth:" + Arrays.toString(objDepth));
				LinkedHashMap<String, Object> linkedHashMap1 = null;

				if (isJsonPathList && value == null) {
					for (int k = 0; k < objDepth.length; k++) {
						log.debug("k:" + k + "linkedHashMap1:" + linkedHashMap1);

						if (objDepth.length > 1 && k != objDepth.length - 1) {
							log.info("objDepth[k]"+objDepth[k]);
							if(linkedHashMap1 == null)
								linkedHashMap1 = (LinkedHashMap<String, Object>) linkedHashMap.get(objDepth[k]);
							else
								linkedHashMap1 = (LinkedHashMap<String, Object>) linkedHashMap1.get(objDepth[k]);	
							log.debug("k:" + k + "linkedHashMap1>>>" + linkedHashMap1);
							if(linkedHashMap1 == null){
								value = null;
								break;
							}
								
						}

						if (k == objDepth.length - 1) {
							log.debug("if block :");
							if (linkedHashMap1 != null)
								value = linkedHashMap1.get(objDepth[k]);
							else
								value = linkedHashMap.get(objDepth[k]);
						}
						log.debug("value::" + value);
					}
				}

				if (jsonPath.startsWith("default"))
					obj[j] = null;
				else if (type.toString().equals(TypeEnum.JSON.toString())
						&& dbType.toString().equals(TypeEnum.STRING.toString())) {
					ObjectMapper mapper = new ObjectMapper();
					try {
						String json = mapper.writeValueAsString(value);
						log.debug("JSON = " + json);
						obj[j] = json;
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}
				} else if (type.toString().equals(TypeEnum.JSON.toString())
						&& dbType.toString().equals(TypeEnum.JSONB.toString())) {
					ObjectMapper mapper = new ObjectMapper();
					try {
						String json = mapper.writeValueAsString(value);
						// DocumentContext documentContext = JsonPath.parse(json);
						PGobject factorsObject = new PGobject();
						factorsObject.setType("jsonb");
						factorsObject.setValue(json);
						obj[j] = factorsObject;
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (type.toString().equals(TypeEnum.LONG.toString())) {
					if (dbType == null)
						obj[j] = value;
					else if (dbType.equals(TypeEnum.DATE))
						obj[j] = new java.sql.Date(Long.parseLong(value.toString()));
				} else if (type.toString().equals(TypeEnum.DATE.toString()) & value!= null) {
					
					String date = value.toString();
					DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					java.util.Date startDate = null;
					try {
						startDate = df.parse(date);
						String newDateString = df.format(startDate);
						log.debug("newDateString:" + newDateString);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					obj[j] = startDate;
				} else
					obj[j] = value;

			}
			log.debug("Obj>>>" + Arrays.toString(obj));
			dbObjArray.add(obj);
		}

		try {
			log.debug("query:" + query + ",object:" + dbObjArray.toString());
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
			log.debug("jsonPath:" + jsonPath);
			boolean isJsonPathList = false;
			Object value = null;
			
			if(type == null) {
				type = TypeEnum.STRING;
			}

			if (type.toString().equals(TypeEnum.CURRENTDATE.toString())) {
				log.debug("CURRENTDATE type:" + type + "," + "dbtype:" + dbType);
				if (dbType.toString().equals(TypeEnum.DATE))
					obj[j] = new Date();
				else if (dbType.toString().equals(TypeEnum.LONG.toString()))
					obj[j] = new Date().getTime();

				continue;
			}
			if (jsonPath.contains("*.")) {
				jsonPath = jsonPath.substring(jsonPath.lastIndexOf("*.") + 2);
				isJsonPathList = true;
				log.debug("jsonPath:" + jsonPath);
			} else if (!(type.toString().equals(TypeEnum.CURRENTDATE.toString()) || jsonPath.startsWith("default"))) {
				value = JsonPath.read(document, jsonPath);
				log.debug("else block value:" + value);
			}

			System.out.println("substring jsonPath:" + jsonPath);

			if ((type.toString().equals(TypeEnum.ARRAY.toString()))
					&& dbType.toString().equals(TypeEnum.STRING.toString())) {
				List<Object> list1 = JsonPath.read(document, jsonPath);
				obj[j] = StringUtils.join(list1, ",");
				log.debug("value::"+value);
			}else if (type.toString().equals(TypeEnum.JSON.toString())
					&& dbType.toString().equals(TypeEnum.STRING.toString())) {
				ObjectMapper mapper = new ObjectMapper();
				try {
					String json = mapper.writeValueAsString(value);
					log.debug("JSON = " + json);
					obj[j] = json;
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			} else if (type.toString().equals(TypeEnum.JSON.toString())
					&& dbType.toString().equals(TypeEnum.JSONB.toString())) {
				ObjectMapper mapper = new ObjectMapper();
				try {
					String json = mapper.writeValueAsString(value);
					// DocumentContext documentContext = JsonPath.parse(json);
					PGobject factorsObject = new PGobject();
					factorsObject.setType("jsonb");
					factorsObject.setValue(json);
					obj[j] = factorsObject;
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (jsonPath.startsWith("default"))
				obj[j] = null;

			else if (type.toString().equals(TypeEnum.LONG.toString())) {
				if (dbType == null)
					obj[j] = value;
				else if (dbType.equals(TypeEnum.DATE.toString()))
					obj[j] = new java.sql.Date(Long.parseLong(value.toString()));
			} else if (type.toString().equals(TypeEnum.DATE.toString())) {
				String date = value.toString();
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				java.util.Date startDate = null;
				try {
					startDate = df.parse(date);
					String newDateString = df.format(startDate);
					log.debug("newDateString:" + newDateString);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				obj[j] = startDate;
			} else {
				log.debug("else value:" + value);
				obj[j] = value;
			}

		}
		log.debug("Obj>>>" + Arrays.toString(obj));
		dbObjArray.add(obj);

		try {
			log.debug("query:" + query + ",object:" + dbObjArray.toString());
			jdbcTemplate.batchUpdate(query, dbObjArray);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}

	}

}