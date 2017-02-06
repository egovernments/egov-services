package org.egov.pgr.entity;

import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "eg_boundary")
@Immutable
public class Boundary {
    private static final long serialVersionUID = 8904645810221559541L;

    @Id
    private Long id;

    @NotNull
    private String name;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
