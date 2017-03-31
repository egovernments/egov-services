package org.egov.pgr.read.persistence.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import static org.egov.pgr.read.persistence.entity.ComplaintStatus.SEQ_COMPLAINTSTATUS;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "egpgr_complaintstatus")
@SequenceGenerator(name = SEQ_COMPLAINTSTATUS, sequenceName = SEQ_COMPLAINTSTATUS, allocationSize = 1)
@JsonIgnoreProperties(value = { "handler", "hibernateLazyInitializer" })
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
