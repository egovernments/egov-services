package org.egov.eis.model;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@ToString
@Data
public class AttendanceType {

    @NotNull
    private Long id;

    private String code;

    private String description;
}
