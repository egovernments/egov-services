package org.egov.collection.indexer.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CityResponse {

    @JsonProperty("City")
    private City city;

}