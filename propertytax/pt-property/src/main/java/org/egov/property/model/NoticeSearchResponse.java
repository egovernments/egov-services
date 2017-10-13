package org.egov.property.model;

import java.util.List;

import org.egov.models.Notice;
import org.egov.models.ResponseInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NoticeSearchResponse {

	private ResponseInfo responseInfo;

	private List<Notice> notices;
}