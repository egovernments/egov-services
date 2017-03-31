package org.egov.pgr.write.entity;

import lombok.*;

import javax.persistence.*;

@Entity(name = "receiving_center_write")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "egpgr_receiving_center", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@SequenceGenerator(name = ReceivingCenter.SEQ_RECEIVINGCENTER, sequenceName = ReceivingCenter.SEQ_RECEIVINGCENTER, allocationSize = 1)
public class ReceivingCenter extends AbstractPersistable<Long> {

    private static final long serialVersionUID = -1568590266889348235L;
    public static final String SEQ_RECEIVINGCENTER = "SEQ_EGPGR_RECEIVING_CENTER";

    @Id
    @GeneratedValue(generator = SEQ_RECEIVINGCENTER, strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @Column(name="iscrnrequired")
    private boolean crnRequired;
    
    @Column(name ="orderno")
    private Long orderNo;

    @Override
    public Long getId() {
        return id;
    }

    @Override
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
