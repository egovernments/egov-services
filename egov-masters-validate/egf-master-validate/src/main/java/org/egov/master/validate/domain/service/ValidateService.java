package org.egov.master.validate.domain.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.egov.master.validate.domain.model.Config;
import org.egov.master.validate.domain.model.JsonFile;
import org.egov.master.validate.domain.model.Master;
import org.egov.master.validate.domain.model.MasterConfiguration;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

@Service
public class ValidateService {
	//@Autowired
	//private MasterConfiguration masterConfig;

	public void validate() throws JsonParseException, JsonMappingException, IOException {

		JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
		URL url =null; //new URL(config.getSchema());

		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		ObjectMapper jsonMapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		mapper.setSerializationInclusion(Include.NON_NULL);
		ObjectMapper jsonWriter = new ObjectMapper();
		ClassLoader classLoader = getClass().getClassLoader();
		
		MasterConfiguration masterConfig = getConfiguration(mapper, jsonWriter, classLoader);

		for (Config config :masterConfig.getConfigs()) {

			for(JsonFile  jFile:config.getJsonFile())
			{

				JsonNode allMasters = getMastersJson(jsonMapper, jsonWriter, classLoader, jFile);
				
				for(Master master:jFile.getMaster())
				{
					JsonNode jsonNode = allMasters.get(master.dataKey);
					validateSchemaAndData(factory, mapper, jsonWriter, jsonNode, master);
					 
					}
				}
			
			}



			}

	private JsonNode getMastersJson(ObjectMapper jsonMapper, ObjectMapper jsonWriter, ClassLoader classLoader,
			JsonFile jFile) {
		JsonNode allMasters =null;
		File f = new File(classLoader.getResource(jFile.name).getFile());
		try {
			Object confObj = jsonMapper.readValue(f, Object.class);
			String confStr = jsonWriter.writeValueAsString(confObj);
			allMasters=JsonLoader.fromString(confStr);
		} catch (IOException e1) {	
			e1.printStackTrace();
		}
		return allMasters;
	}

	private void validateSchemaAndData(JsonSchemaFactory factory, ObjectMapper mapper, ObjectMapper jsonWriter,
			JsonNode jsonNode, Master master) {
		URL url;
		JsonSchema objectSchema;
		ProcessingReport report;
		String ptr;
		String schema;
		try {


			if (master.name != null) {
				 url =new URL(master.schema);
				 Object readValue = mapper.readValue(url, Object.class);
				 schema = jsonWriter.writeValueAsString(readValue);
				 JsonNode actualObj = mapper.readTree(schema);// .findPath("#/definitions/Fund");
				 System.out.println(actualObj.asText());
				 ptr="/definitions/"+master.name.trim()+"";
				 objectSchema = factory.getJsonSchema(actualObj,ptr);
				
				 for(int i=0;i<jsonNode.size();i++)
				 {
				 report = objectSchema.validate(jsonNode.get(i));
					if (!report.isSuccess()) {
						String msg = "";
						for (final ProcessingMessage reportLine : report) {
							msg += reportLine.toString() + "\n";
						}
						System.err.println(msg);
					}
					else
					{
						System.out.println(master.name+"["+i+"] is Valid");
						System.out.println(jsonNode.get(i));
					}
				 }
				JsonNode schemaNode=actualObj.findValue(master.name);
				JsonNode uniqueFields = schemaNode.get("x-unique");
					
				if(uniqueFields!=null)
				{
					for(JsonNode node:uniqueFields)
					{
						List<String> fieldStr = jsonNode.findValuesAsText(node.textValue());
						//handle comma seperated
						Set<String> nonDuplicate=new HashSet<>();
						nonDuplicate.addAll(fieldStr);
						if(fieldStr.size()>nonDuplicate.size())
						{
						System.err.println("Duplicate values in "+master.name);
						throw new RuntimeException("Duplicate values in "+master.name +" for the fields "+fieldStr);
						}
						
						
					}
				}else
				{
					String[] uniqueFieldsFromConfig = master.getUniqueFields();
					if(uniqueFieldsFromConfig!=null)
					{
						List<String> fieldStr=new ArrayList<>();
						for(String node:uniqueFieldsFromConfig)
						{
							
							if(node.contains(","))
							{
								String[] fields = node.split(",");  
								for(String s:fields)
								{
									if(fieldStr.isEmpty())
										fieldStr=  jsonNode.findValuesAsText(s); 
									else
									{
										List<String> secondFieldList = jsonNode.findValuesAsText(s);
										int i=0;
										for(String ss:secondFieldList)
										{
											if(fieldStr.get(i)==null ||fieldStr.get(i).isEmpty())
											{
												fieldStr.set(i,"-");
											}
											fieldStr.set(i, fieldStr.get(i)+"-"+ss);
											i++;
										}
										
									}
								}
								 
							} else
							{
							 fieldStr = jsonNode.findValuesAsText(node);
							}
							//jsonNode.
							//handle comma seperated
							
							Set<String> nonDuplicate=new HashSet<>();
							nonDuplicate.addAll(fieldStr);
							if(fieldStr.size()>nonDuplicate.size())
							{
							    System.err.println("Duplicate values in "+master.name +" for the fields "+fieldStr);
							    throw new RuntimeException("Duplicate values in "+master.name +" for the fields "+fieldStr);
							}
							
							
						}
					}
				}
				
				 
			}
			}catch(Exception e)
			{
				throw new RuntimeException(e.getMessage());


			}
	}

	private MasterConfiguration getConfiguration(ObjectMapper mapper, ObjectMapper jsonWriter, ClassLoader classLoader)
			throws IOException, JsonParseException, JsonMappingException, JsonProcessingException {
		File confFile = new File(classLoader.getResource("application.yaml").getFile());
		Object confObj = mapper.readValue(confFile, Object.class);
		String confStr = jsonWriter.writeValueAsString(confObj);
		JsonNode conf=JsonLoader.fromString(confStr);
		MasterConfiguration masterConfig = mapper.readValue(conf.toString(), MasterConfiguration.class);
		return masterConfig;
	}
}
