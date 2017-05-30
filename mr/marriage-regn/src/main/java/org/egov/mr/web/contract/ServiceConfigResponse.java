package org.egov.mr.web.contract;

import java.util.ArrayList;
import java.util.List;

import org.egov.mr.model.ServiceConfigKeyValues;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
public class ServiceConfigResponse {
	private ResponseInfo responseInfo;

	private List<ServiceConfigKeyValues> serviceConfiguration = new ArrayList<ServiceConfigKeyValues>();
}
