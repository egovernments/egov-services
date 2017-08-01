package org.egov.tradelicense.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.ToString;

@Configuration
@Getter
@ToString
public class PropertiesManager {

	@Value("${spring.kafka.bootstrap.servers}")
	private String kafkaServerConfig;

	@Value("${auto.offset.reset.config}")
	private String kafkaOffsetConfig;

	@Value("${invalid.input}")
	private String invalidInput;

	@Value("${duplicate.code}")
	private String duplicateCode;
	
	@Value("${invalid.range.code}")
	private String invalidRangeCode;
	
	@Value("${success.status}")
	private String successStatus;
	
	@Value("${failed.status}")
	private String failedStatus;
	
	@Value("${default.page.size}")
	private String defaultPageSize;
	
	@Value("${default.page.number}")
	private String defaultPageNumber;
	
	@Value("${default.offset}")
	private String defaultOffset;
	
	@Value("${duplicate.documentType.name}")
	private String documentTypeCustomMsg;
	
	@Value("${duplicate.uom.code}")
	private String uomCustomMsg;
	
	@Value("${duplicate.category.code}")
	private String categoryCustomMsg;
	
	@Value("${duplicate.subcategory.code}")
	private String subCategoryCustomMsg;
	
	
	@Value("${duplicate.businessnature.code}")
	private String businessNatureCustomMsg;
}