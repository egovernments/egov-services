package org.egov.property.repository.builder;

import org.springframework.stereotype.Component;

@Component
public class NoticeQueryBuilder {

    public String getInsertQuery() {
        return "INSERT INTO egpt_notice (tenantid, applicationnumber, noticedate, noticenumber, noticetype, upicnumber, fileStoreId, createdtime, createdby)"
                + "VALUES (:tenantid, :applicationnumber, :noticedate, :noticenumber, :noticetype, :upicnumber, :fileStoreId, :createdtime, :createdby)";
    }
}
