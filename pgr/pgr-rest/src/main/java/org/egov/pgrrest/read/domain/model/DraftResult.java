package org.egov.pgrrest.read.domain.model;


import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class DraftResult {
    private List<Draft> drafts;
}