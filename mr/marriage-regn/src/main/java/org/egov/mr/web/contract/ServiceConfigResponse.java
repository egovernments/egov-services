package org.egov.mr.web.contract;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.egov.common.contract.response.ResponseInfo;
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

	private Map<String, List<String>> serviceConfiguration = new HashMap<String, List<String>>();
}
