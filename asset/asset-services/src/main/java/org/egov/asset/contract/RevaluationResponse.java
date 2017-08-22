package org.egov.asset.contract;

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.model.Revaluation;
import org.egov.common.contract.response.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class RevaluationResponse {
    @JsonProperty("ResposneInfo")
    private ResponseInfo resposneInfo = null;

    @JsonProperty("Revaluations")
    private List<Revaluation> revaluations = new ArrayList<Revaluation>();

}
