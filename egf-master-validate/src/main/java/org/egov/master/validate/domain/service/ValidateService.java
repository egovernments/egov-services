package org.egov.master.validate.domain.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.egov.master.validate.domain.model.Config;
import org.egov.master.validate.domain.model.MasterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

@Service
public class ValidateService {
	@Autowired
	private MasterConfig masterConfig;

	public void validate() {

		for (Config config : masterConfig.getMasters())
			try {
				if (config.getName() != null) {
					final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
					URL uri = new URL(config.getSchema());
					// JsonLoader.fromPath(config.getSchema()
					ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
					mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
					mapper.setSerializationInclusion(Include.NON_NULL);
					Object readValue = mapper.readValue(uri, Object.class);
					ObjectMapper jsonWriter = new ObjectMapper();
					String schema = jsonWriter.writeValueAsString(readValue);
					ObjectMapper mapper1 = new ObjectMapper();
					JsonNode actualObj = mapper.readTree(schema);// .findPath("#/definitions/Fund");
					System.err.println(actualObj.asText());
					String ptr="/definitions/"+config.getName().trim()+"";
					final JsonSchema jsonSchema = factory.getJsonSchema(actualObj,ptr );

					ClassLoader classLoader = getClass().getClassLoader();
					File f = new File(classLoader.getResource("fund.json").getFile());
					//JsonNode fromFile = JsonLoader.fromFile(f);
					
				 
					final ProcessingReport report = jsonSchema.validate(JsonLoader.fromFile(f));
					if (!report.isSuccess()) {
						String msg = "";
						for (final ProcessingMessage reportLine : report) {
							msg += reportLine.toString() + "\n";
						}
						System.err.println(msg);
					}
					else
					{
						System.err.println(config.getName()+" is Valid");
					}
				}
			} catch (ProcessingException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
