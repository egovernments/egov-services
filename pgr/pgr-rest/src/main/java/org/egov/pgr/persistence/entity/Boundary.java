package org.egov.pgr.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "eg_boundary")
@Immutable
@Getter
@Setter
public class Boundary {
    private static final long serialVersionUID = 8904645810221559541L;

    @Id
    private Long id;

    @NotNull
    private String name;
}
