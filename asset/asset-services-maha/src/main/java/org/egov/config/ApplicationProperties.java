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
    
    @Value("${errcode.asset.wifrefnumber}")
    private String wifRefNumber;
    
    @Value("${errcode.asset.warranty}")
    private String warranty;
    
    @Value("${errcode.asset.parentcategory}")
    private String parentCategory;
    
    @Value("${errcode.asset.dateofcreation}")
    private String dateOfCreation;
    
    @Value("${errcode.asset.orderdate}")
    private String orderDate;
    
    @Value("${errcode.asset.openingdate}")
    private String openingDate;
    
    @Value("${errcode.asset.acquisitiondate}")
    private String acquisitionDate;
    
    @Value("${errcode.asset.originalvalue}")
    private String originalValue;
    
    @Value("${errcode.asset.grossvalue}")
    private String grossValue;
    
    @Value("${errcode.asset.accumulateddepreciation}")
    private String accumulatedDepreciation;
    
    @Value("${errcode.asset.assetcategory}")
    private String assetCategory;
    
    @Value("${errcode.asset.departmant}")
    private String departmant;
    
    @Value("${errcode.asset.fundsource}")
    private String fundSource;
    
    @Value("${errcode.asset.anticipatedlife}")
    private String anticipatedLife;
    
    @Value("${errcode.asset.depreciationrate}")
    private String assetdepreciationRate;
    
    @Value("${errcode.asset.landdetails}")
    private String landDetails;
    
    @Value("${errcode.asset.update}")
    private String assetModify;
    
    @Value("${errcode.asset}")
    private String asset;
    
    @Value("${errcode.revaluation}")
    private String revaluation;
    
    @Value("${errcode.revaluation.revaluationamount}")
    private String revaluationAmount;
    
    @Value("${errcode.revaluation.revaluationdate}")
    private String revaluationDate;
    
    @Value("${errcode.revaluation.orderdate}")
    private String revaluationOrderDate;
    
    @Value("${errcode.revaluation.valueafterrevaluation}")
    private String valueAfterRevaluation;
    
    @Value("${errcode.revaluation.valuationamount}")
    private String valuationAmount;
    
    @Value("${errcode.disposal.asset}")    
    private String disposalAsset;
    
    @Value("${errcode.disposal.saleamount}")
    private String saleAmount;
    
    @Value("${errcode.disposal.date}")
    private String date;
    
    @Value("${errcode.disposal.orderdate}")
    private String disposalOrderDate;
    
    @Value("${errcode.depreciation.depreciationrate}")
    private String assetcategoryDepreciationRate;
    
    @Value("${errcode.depreciation.assetcategory}")
    private String depreciationAssetCategory;
    
    @Value("${errcode.depreciation.financialyear}")
    private String financialYear;
    
    @Value("${errcode.depreciation.depreciationdate}")
    private String depreciationDate;
    
/*    @Value("${kafka.topics.save.workflow}")
    private String startWfAssetTopicName;
    
    @Value("${kafka.topics.update.workflow}")
    private String updateWfAssetTopicName;*/
    
    @Value("${errcode.asset.description}")
    private String description;
    
    @Value("${errcode.asset.modeofacquisition}")
    private String modeOfAcquisition;
    
    
    @Autowired
    private Environment environment;

}