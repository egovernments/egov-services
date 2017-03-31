package org.egov.pgr.read.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.egov.pgr.common.entity.AbstractPersistable;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "egpgr_complainttype_category")
@SequenceGenerator(name = ComplaintTypeCategory.SEQ_COMP_TYPE_CATEGORY, sequenceName = ComplaintTypeCategory.SEQ_COMP_TYPE_CATEGORY, allocationSize = 1)
public class ComplaintTypeCategory extends AbstractPersistable<Long> {
    private static final long serialVersionUID = 2739365086791183614L;

    public static final String SEQ_COMP_TYPE_CATEGORY = "SEQ_EGPGR_COMPLAINTTYPE_CATEGORY";

    @Id
    @GeneratedValue(generator = SEQ_COMP_TYPE_CATEGORY, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @SafeHtml
    @Length(min = 5, max = 100)
    private String name;

    @SafeHtml
    @Length(max = 250)
    private String description;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JsonIgnore
    private List<ComplaintType> complaintTypes;

    @Override
    protected void setId(final Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public List<ComplaintType> getComplaintTypes() {
        return complaintTypes;
    }

    public void setComplaintTypes(final List<ComplaintType> complaintTypes) {
        this.complaintTypes = complaintTypes;
    }
}
