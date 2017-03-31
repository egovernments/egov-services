
package org.egov.pgr.write.entity;

import lombok.Getter;
import lombok.Setter;
import org.egov.pgr.common.entity.AbstractPersistable;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

import static org.egov.pgr.write.entity.Complainant.SEQ_COMPLAINANT;

@Entity(name = "complainant_write")
@Setter
@Getter
@Table(name = "egpgr_complainant")
@SequenceGenerator(name = SEQ_COMPLAINANT, sequenceName = SEQ_COMPLAINANT, allocationSize = 1)
public class Complainant extends AbstractPersistable<Long> {

    public static final String SEQ_COMPLAINANT = "SEQ_EGPGR_COMPLAINANT";
    private static final long serialVersionUID = 5691022600220045218L;
    @Id
    @GeneratedValue(generator = SEQ_COMPLAINANT, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Length(max = 150)
    private String name;

    @Length(max = 20)
    private String mobile;

    @Length(max = 100)
    private String email;

    @Column(name = "userdetail")
    private Long userDetail;

    @Length(max = 256)
    private String address;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    protected void setId(final Long id) {
        this.id = id;
    }

}
