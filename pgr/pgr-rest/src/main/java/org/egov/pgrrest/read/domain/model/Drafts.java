package org.egov.pgrrest.read.domain.model;

import lombok.*;

import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Drafts {
    private HashMap<String, Object> drafts;
}
