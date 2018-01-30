package org.egov.works.workorder.config;

import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ramki on 11/11/17.
 */
@Configuration
@Getter
@ToString
public class PropertiesManager {

    @Value("${app.timezone}")
    private String appTimeZone;

    @Value("${egov.services.works.workorder.pageSize.default}")
    private String pageSize;

    @Value("${egov.services.works.workorder.pageNumber.default}")
    private String pageNumber;

    @Value("${egov.services.works.workorder.pagesize.max}")
    private String pageSizeMax;
    
    @Value("${works.loanumber.format}")
    private String loaNumberFormat;
    
    @Value("${works.loanumber}")
    private String worksLOANumber;
    
    @Value("${egov.services.works.loa.create.topic.validated}")
    private String worksLOACreateTopic;

    @Value("${egov.services.works.loa.update.topic.validated}")
    private String worksLOAUpdateTopic;
    
    @Value("${loanumber.prefix}")
    private String loaNumberPrefix;

    @Value("${works.workordernumber.format}")
    private String workOrderNumberFormat;

    @Value("${works.workordernumber}")
    private String worksWorkOrderNumber;

    @Value("${egov.services.works.workorder.createupdate.topic.validated}")
    private String worksWorkOrderCreateTopic;

    @Value("${workordernumber.prefix}")
    private String workOrderNumberPrefix;

    @Value("${egov.services.works.revisionloa.topic}")
    private String worksRevisionLOACreateUpdateTopic;
    
    @Value("${works.noticenumber.format}")
    private String noticeNumberFormat;

    @Value("${works.noticenumber}")
    private String worksNoticeNumber;

    @Value("${egov.services.works.notice.createupdate.topic.validated}")
    private String worksNoticeCreateTopic;

    @Value("${noticenumber.prefix}")
    private String noticeNumberPrefix;

    @Value("${egov.services.works.milestone.saveorupdate.validated}")
    private String worksMilestoneSaveOrUpdateValidatedTopic;

    @Value("${egov.services.works.trackmilestone.saveorupdate.validated}")
    private String worksTrackMilestoneSaveOrUpdateValidatedTopic;
    
    @Value("${egov.services.works.advance.saveorupdate.validated}")
    private String worksAdvanceSaveOrUpdateValidatedTopic;

    @Value("${egov.services.works.detailedestimate.backupdate.oncreateloa.topic}")
    private String worksDetailedEstimateBackupdateOnCreateLoaTopic;

    @Value("${egov.services.works.detailedestimate.backupdate.oncancelloa.topic}")
    private String worksDetailedEstimateBackupdateOnCancelLoaTopic;

}
