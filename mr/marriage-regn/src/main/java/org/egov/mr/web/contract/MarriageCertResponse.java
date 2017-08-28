package org.egov.mr.web.contract;

import java.util.ArrayList;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.mr.model.MarriageCertificate;
import org.egov.mr.model.Page;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
public class MarriageCertResponse {
	private ResponseInfo responseInfo;

	private List<MarriageCertificate> marriageCerts = new ArrayList<MarriageCertificate>();

	private Page page;
}
