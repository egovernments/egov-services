package org.egov.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

import lombok.Getter;

@Configuration
@PropertySource(value = { "classpath:config/application-config.properties" }, ignoreResourceNotFound = true)
@Order(0)
@Getter
public class ApplicationProperties {

    private final String searchPageSizeDefault = "default.page.size";
    private final String searchPagenoMax = "search.pageno.max";
    private final String searchPageSizeMax = "search.pagesize.max";

    public String getSearchPageSizeDefault() {
        return environment.getProperty(searchPageSizeDefault);
    }

    public String getSearchPagenoMax() {
        return environment.getProperty(searchPagenoMax);
    }

    public String getSearchPageSizeMax() {
        return environment.getProperty(searchPageSizeMax);
    }
    
    public String getBootstrapServer() {
        return environment.getProperty("spring.kafka.bootstrap.servers");
    }
    
    @Value("${app.timezone}")
    private String timeZone;
   
    @Value("${kafka.topics.save.asset}")
    private String createAssetTopicName;
    
    private String createAssetTopicNameTemp = "save-asset-maha";

    @Value("${kafka.topics.update.asset}")
    private String updateAssetTopicName;
    
    @Value("${kafka.topics.save.revaluation}")
    private String revaluationSaveTopic;
    
    @Value("${kafka.topics.update.revaluation}")
    private String revaluationUpdateTopic;
    
    @Value("${kafka.topics.save.disposal}")
    private String disposalSaveTopicName;
    
    @Value("${kafka.topics.save.currentvalue}")
    private String saveCurrentvalueTopic;
    
    @Value("${mdms.master.assetcategory}")
    private String mdMsMasterAssetCategory;
    
    @Value("${mdms.modulename}")
    private String mdMsModuleName;
    
    @Value("${kafka.topics.save.depreciation}")
    private String saveDepreciationTopic;
    
    @Value("${mdms.service.host}")
    private String mdmsServiceHost;
    
    @Value("${mdms.service.basepath}")
    private String mdmsServiceBasePath;
     
    @Autowired
    private Environment environment;

}