package org.egov.pgrrest.common.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Entity(name = "complainttypecategory_write")
@Table(name = "egpgr_complainttype_category")
@SequenceGenerator(name = ServiceTypeCategory.SEQ_SERVICE_TYPE_CATEGORY,
    sequenceName = ServiceTypeCategory.SEQ_SERVICE_TYPE_CATEGORY,
    allocationSize = 1)
public class ServiceTypeCategory extends AbstractPersistable<Long> {
    private static final long serialVersionUID = 2739365086791183614L;

    public static final String SEQ_SERVICE_TYPE_CATEGORY = "SEQ_EGPGR_COMPLAINTTYPE_CATEGORY";

    @Id
    @GeneratedValue(generator = SEQ_SERVICE_TYPE_CATEGORY, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Length(min = 5, max = 100)
    private String name;

    @Length(max = 250)
    private String description;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JsonIgnore
    private List<ServiceType> complaintTypes;
    
    @Column(name = "tenantid")
    private String tenantId;

    @Column(name = "localname")
    private String localName;

}
