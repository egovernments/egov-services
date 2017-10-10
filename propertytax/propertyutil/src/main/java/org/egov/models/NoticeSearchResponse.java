package org.egov.models;

import lombok.*;

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
