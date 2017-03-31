package org.egov.pgr.common.entity;

import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "egpgr_receivingmode")
@SequenceGenerator(name = ReceivingMode.SEQ_RECEIVINGMODE,
    sequenceName = ReceivingMode.SEQ_RECEIVINGMODE,
    allocationSize = 1)
public class ReceivingMode extends AbstractPersistable<Long> {

    protected static final String SEQ_RECEIVINGMODE = "seq_egpgr_receivingmode";

    @Id
    @GeneratedValue(generator = SEQ_RECEIVINGMODE, strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    private String code;

    @Column(name = "visible")
    private boolean visible;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    protected void setId(final Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return StringUtils.capitalize(name);
    }
}