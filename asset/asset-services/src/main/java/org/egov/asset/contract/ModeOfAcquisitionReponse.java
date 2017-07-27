package org.egov.asset.contract;

import java.util.HashMap;
import java.util.Map;

import org.egov.asset.model.enums.ModeOfAcquisition;
import org.egov.common.contract.response.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class ModeOfAcquisitionReponse {

    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;

    @JsonProperty("ModeOfAcquisition")
    private Map<ModeOfAcquisition, ModeOfAcquisition> modeOfAcquisition = new HashMap<>();

}