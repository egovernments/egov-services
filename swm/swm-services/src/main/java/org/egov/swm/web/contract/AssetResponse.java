package org.egov.swm.web.contract;

import java.util.ArrayList;
import java.util.List;

import org.egov.swm.domain.model.Asset;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class AssetResponse {

    @JsonProperty("Assets")
    private final List<Asset> assets = new ArrayList<>();

}
