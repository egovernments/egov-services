
package org.egov.pgr.common.entity;

import lombok.*;

import javax.persistence.*;

import static org.egov.pgr.common.entity.Complainant.SEQ_COMPLAINANT;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "egpgr_complainant")
@SequenceGenerator(name = SEQ_COMPLAINANT, sequenceName = SEQ_COMPLAINANT, allocationSize = 1)
public class Complainant extends AbstractPersistable<Long> {

    public static final String SEQ_COMPLAINANT = "SEQ_EGPGR_COMPLAINANT";
    private static final long serialVersionUID = 5691022600220045218L;
    @Id
    @GeneratedValue(generator = SEQ_COMPLAINANT, strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    private String mobile;

    private String email;

    @Column(name = "userdetail")
    private Long userDetail;

    private String address;
    
    @Column(name = "tenantid")
    private String tenantId;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    protected void setId(final Long id) {
        this.id = id;
    }

    public org.egov.pgr.common.model.Complainant toDomain() {
        return org.egov.pgr.common.model.Complainant.builder()
            .firstName(name)
            .mobile(mobile)
            .email(email)
            .address(address)
            .tenantId(tenantId)
            .build();
    }

}
