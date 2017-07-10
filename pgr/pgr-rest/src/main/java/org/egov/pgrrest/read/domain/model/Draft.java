package org.egov.pgrrest.read.domain.model;

import lombok.*;

import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class Draft {
    private Long id;

    private HashMap<String, Object> draft;
}
