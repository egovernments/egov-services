package org.egov.pgr.write.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static org.egov.pgr.write.entity.ComplaintStatus.SEQ_COMPLAINTSTATUS;

@Entity(name = "complaintstatus_write")
@Table(name = "egpgr_complaintstatus")
@SequenceGenerator(name = SEQ_COMPLAINTSTATUS, sequenceName = SEQ_COMPLAINTSTATUS, allocationSize = 1)
public class ComplaintStatus extends AbstractPersistable<Long> {
    public static final String SEQ_COMPLAINTSTATUS = "SEQ_EGPGR_COMPLAINTSTATUS";
    private static final long serialVersionUID = -9009821412847211632L;
    @Id
    @GeneratedValue(generator = SEQ_COMPLAINTSTATUS, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private String name;

    public Long getId() {
        return id;
    }

    protected void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

}
