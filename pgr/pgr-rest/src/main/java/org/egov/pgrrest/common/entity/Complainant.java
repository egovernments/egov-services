
package org.egov.pgrrest.common.entity;

import lombok.*;
import org.egov.pgrrest.common.model.Requester;

import javax.persistence.*;

import static org.egov.pgrrest.common.entity.Complainant.SEQ_COMPLAINANT;

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

    public Requester toDomain() {
        return Requester.builder()
            .firstName(name)
            .mobile(mobile)
            .email(email)
            .address(address)
            .tenantId(tenantId)
            .build();
    }

}
