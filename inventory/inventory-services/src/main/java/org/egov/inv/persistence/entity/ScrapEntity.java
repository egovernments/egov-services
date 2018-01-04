package org.egov.inv.persistence.entity;

import static org.springframework.util.StringUtils.isEmpty;

import org.egov.inv.model.AuditDetails;
import org.egov.inv.model.Scrap;
import org.egov.inv.model.Store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScrapEntity {
	public static final String TABLE_NAME = "scrap";
    public static final String SEQUENCE_NAME = "seq_scrap";
    public static final String ALIAS = "scrap";
    
    private String id;
    
    private String scrapNumber;
    
    private String storeCode;
    
    private Long scrapDate;
    
    private String scrapStatus;
    
    private Integer stateId;
    
    private String createdBy;

    private Long createdTime;

    private String lastModifiedBy;

    private Long lastModifiedTime;

    private String tenantId;

    public Scrap toDomain() {
    	Scrap scrap = new Scrap();

        return scrap.id(id)
                .scrapNumber(scrapNumber)
                .scrapDate(scrapDate)
                .store(!isEmpty(storeCode)? buildStore(storeCode) : null)
                .scrapStatus(Scrap.ScrapStatusEnum.fromValue(scrapStatus))
                .tenantId(tenantId)
                .auditDetails(buildAuditDetails());

    }
    private Store buildStore(String storeCode) {
        Store store = new Store();
        return store.code(storeCode);
    }
    
    private AuditDetails buildAuditDetails() {
        AuditDetails auditDetails = new AuditDetails();
        return auditDetails
                .createdBy(createdBy)
                .createdTime(createdTime)
                .lastModifiedBy(lastModifiedBy)
                .lastModifiedTime(lastModifiedTime);
    }
}
