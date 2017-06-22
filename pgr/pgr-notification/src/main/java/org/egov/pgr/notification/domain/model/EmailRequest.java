package org.egov.pgr.notification.domain.model;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class EmailRequest {
    @NonNull
	private String subject;
    @NonNull
    private String body;
    @NonNull
    private String email;
}
