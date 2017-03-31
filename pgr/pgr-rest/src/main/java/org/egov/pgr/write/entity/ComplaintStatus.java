package org.egov.pgr.write.entity;

import lombok.Getter;
import lombok.Setter;
import org.egov.pgr.common.entity.AbstractPersistable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static org.egov.pgr.write.entity.ComplaintStatus.SEQ_COMPLAINTSTATUS;

@Getter
@Setter
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

    @Override
    public Long getId() {
        return id;
    }

    @Override
    protected void setId(final Long id) {
        this.id = id;
    }
}
