package org.egov.pgrrest.read.domain.model;

import lombok.*;

import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class UpdateDraft {
    private Long id;
    private HashMap<String, Object> draft;
}
