package org.egov.filter.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.filter.model.FinalResponse;
import org.egov.filter.model.ResponseParam;
import org.egov.filter.model.TypeEnum;
import org.springframework.stereotype.Component;

import com.jayway.jsonpath.DocumentContext;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ResponseFieldDataConverterUtil {
	
	public void setResponse(DocumentContext finalResObjDc, ResponseParam responseParam, FinalResponse finalResponse,
			DocumentContext responseDc, int index) {

		String sourceType = (responseParam.getSourceType() == null) ? null : responseParam.getSourceType().toString();
		String destType = (responseParam.getDestType() == null) ? null : responseParam.getDestType().toString();
		Object value = responseDc.read(responseParam.getSource().replace("*", String.valueOf(index)));
		List<String> valueMapping = responseParam.getValueMapping();
		
		String destPath = responseParam.getDestination();

		if(finalResponse.getBasePath() != null)
			destPath = destPath.replace(finalResponse.getBasePath(), "$");
		
		if (sourceType != null && destType != null && valueMapping == null) {
			if (destType.equals(TypeEnum.STRING.toString())) {
				if (value instanceof Integer)
					value = String.valueOf(value);
				else if (value instanceof Double)
					value = String.valueOf(((Double) value).intValue());

			}
		} else if (valueMapping != null) {
			Map<String, Object> map = new HashMap<>();
			for (String valueStr : valueMapping) {
				String[] valueArr = valueStr.split("=");
				map.put(valueArr[0], valueArr[1]);
			}
			
			if (sourceType == null && destType == null) {
				value = map.get(value.toString());
			}
			else {

				if (sourceType.equals(TypeEnum.INT.toString()) && destType.equals(TypeEnum.BOOLEAN.toString())) {
					if (value instanceof Integer)
						value = String.valueOf(value);
					else if (value instanceof Double)
						value = String.valueOf(((Double) value).intValue());
					
					value = Boolean.parseBoolean(String.valueOf(map.get(value.toString())));
				}

			}
		}
		finalResObjDc.set(destPath, value);

	}

}
