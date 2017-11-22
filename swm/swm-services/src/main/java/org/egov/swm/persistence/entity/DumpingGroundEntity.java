package org.egov.swm.persistence.entity;

import java.util.ArrayList;
import java.util.List;

import org.egov.swm.domain.model.DumpingGround;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DumpingGroundEntity {

    private String code = null;

    private String tenantId = null;

    private String name = null;

    private String location = null;

    private Double area = null;

    private Double capacity = null;

    private String address = null;

    private Double latitude = null;

    private Double longitude = null;

    private List<String> wasteTypes = new ArrayList<>();

    private Boolean mpcbAuthorisation = null;

    private Boolean bankGuarantee = null;

    private String bankName = null;

    private Long bankValidityFrom = null;

    private Long bankValidityTo = null;

    private String createdBy = null;

    private String lastModifiedBy = null;

    private Long createdTime = null;

    private Long lastModifiedTime = null;

    public DumpingGround toDomain() {

        final DumpingGround dumpingGround = new DumpingGround();
        dumpingGround.setCode(code);
        dumpingGround.setTenantId(tenantId);
        /*
         * dumpingGround.setId(id); dumpingGround.setTenantId(tenantId); dumpingGround.setName(name);
         * dumpingGround.setWard(Boundary.builder().code(ward).build());
         * dumpingGround.setZone(Boundary.builder().code(zone).build());
         * dumpingGround.setStreet(Boundary.builder().code(street).build());
         * dumpingGround.setColony(Boundary.builder().code(colony).build()); dumpingGround.setArea(area);
         * dumpingGround.setCapacity(capacity); dumpingGround.setAddress(address); dumpingGround.setLatitude(latitude);
         * dumpingGround.setLongitude(longitude); dumpingGround.setMpcbAuthorisation(mpcbAuthorisation);
         * dumpingGround.setBankGuarantee(bankGuarantee); dumpingGround.setBankName(bankName);
         * dumpingGround.setBankValidityFrom(bankValidityFrom); dumpingGround.setBankValidityTo(bankValidityTo);
         * dumpingGround.setWasteTypes(new ArrayList<WasteType>()); if (wasteTypes != null && !wasteTypes.isEmpty()) { for (String
         * wt : wasteTypes) dumpingGround.getWasteTypes().add(WasteType.builder().code(wt).build( )); }
         */
        return dumpingGround;

    }

}
