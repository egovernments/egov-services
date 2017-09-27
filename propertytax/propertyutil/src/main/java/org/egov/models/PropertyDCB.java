package org.egov.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PropertyDCB {

    @JsonProperty("upicNumber")
    @Size(min = 6, max = 128)
    private String upicNumber;

    @JsonProperty("oldUpicNumber")
    @Size(min = 4, max = 128)
    private String oldUpicNumber;

    @JsonProperty("Demands")
    private List<Demand> demands;
}
