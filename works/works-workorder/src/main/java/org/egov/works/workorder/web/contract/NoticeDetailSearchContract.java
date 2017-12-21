package org.egov.works.workorder.web.contract;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDetailSearchContract {

	private List<String> ids;

	private String tenantId;

	private List<String> notices;

	private Integer pageSize;

	private Integer pageNumber;

	private String sortBy;
}
