package org.egov.access.domain.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class RoleSearchCriteria {
    private String codes;

    public List<String> getCodes() {
        if (StringUtils.isNotBlank(codes)) {
            return Arrays.stream(codes.split(",")).map(String::trim).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
