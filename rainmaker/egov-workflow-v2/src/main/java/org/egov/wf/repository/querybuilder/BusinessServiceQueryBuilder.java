package org.egov.wf.repository.querybuilder;

import org.egov.wf.web.models.BusinessServiceSearchCriteria;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class BusinessServiceQueryBuilder {



     private static final String INNER_JOIN = " INNER JOIN ";

     private static final String LEFT_OUTER_JOIN = " LEFT OUTER JOIN ";

     private static final String BASE_QUERY = "SELECT bs.*,st.*,ac.*,bs.uuid as bs_uuid," +
             " bs.lastModifiedTime as bs_lastModifiedTime,bs.createdTime as bs_createdTime," +
             "bs.createdBy as bs_createdBy,bs.lastModifiedBy as bs_lastModifiedBy,bs.tenantId as bs_tenantId," +
             " st.lastModifiedTime as st_lastModifiedTime,st.createdTime as st_createdTime," +
             "st.tenantId as st_tenantId,st.createdBy as st_createdBy,st.uuid as st_uuid," +
             " st.lastModifiedBy as st_lastModifiedBy,"  +
             " ac.lastModifiedTime as ac_lastModifiedTime,ac.createdTime as ac_createdTime," +
             "ac.createdBy as ac_createdBy,ac.lastModifiedBy as ac_lastModifiedBy," +
             "ac.uuid as ac_uuid,ac.tenantId as ac_tenantId"  +
             " FROM eg_wf_businessService_v2 bs " +
            INNER_JOIN + " eg_wf_state_v2 st ON st.businessServiceId = bs.uuid " +
            LEFT_OUTER_JOIN  + " eg_wf_action_v2 ac ON ac.currentState = st.uuid WHERE ";



    public String getStateAndActionSearchQuery(BusinessServiceSearchCriteria criteria, List<Object> preparedStmtList) {

        StringBuilder builder = new StringBuilder(BASE_QUERY);

        builder.append(" bs.tenantid=? ");
        preparedStmtList.add(criteria.getTenantId());

        builder.append(" AND bs.businessService=? ");
        preparedStmtList.add(criteria.getBusinessService());

        if(criteria.getStateUuid()!=null){
            builder.append(" AND st.uuid=? ");
            preparedStmtList.add(criteria.getStateUuid());
        }
        else {
            builder.append(" AND st.state IS NULL ");
        }

        if(criteria.getAction()!=null){
            builder.append(" AND ac.action=? ");
            preparedStmtList.add(criteria.getAction());
        }

        return builder.toString();
    }


    public String getResultantState(BusinessServiceSearchCriteria criteria, List<Object> preparedStmtList){
        StringBuilder builder = new StringBuilder(BASE_QUERY);
        builder.append(" st.uuid=? ");
        preparedStmtList.add(criteria.getStateUuid());
        return builder.toString();
    }

    public String getBusinessServices(BusinessServiceSearchCriteria criteria, List<Object> preparedStmtList){
        StringBuilder builder = new StringBuilder(BASE_QUERY);
        builder.append(" bs.tenantId=? ");
        preparedStmtList.add(criteria.getTenantId());

        if(criteria.getBusinessService()!=null){
            builder.append(" AND bs.businessService=? ");
            preparedStmtList.add(criteria.getBusinessService());
        }

        return builder.toString();
    }


}
