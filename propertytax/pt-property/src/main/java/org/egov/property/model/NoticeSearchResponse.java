package org.egov.property.model;

import lombok.*;
import org.egov.models.Notice;
import org.egov.models.ResponseInfo;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NoticeSearchResponse {

    private ResponseInfo responseInfo;

    private List<Notice> notices;
}
