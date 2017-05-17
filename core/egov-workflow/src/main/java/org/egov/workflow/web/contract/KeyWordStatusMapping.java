package org.egov.workflow.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KeyWordStatusMapping {

    private Long id;
    private String name;
    private String code;

    public KeyWordStatusMapping(org.egov.workflow.domain.model.KeywordStatusMapping keywordStatusMapping){
        this.id = keywordStatusMapping.getId();
        this.name = keywordStatusMapping.getName();
        this.code = keywordStatusMapping.getCode();
    }
}
