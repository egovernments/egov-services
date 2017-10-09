package org.egov.models;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class NoticeResponse {
    private ResponseInfo responseInfo;

    private Notice notice;
}