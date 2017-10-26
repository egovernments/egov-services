package org.egov.lcms.models;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This object holds information about the legal case hearingDetails response
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HearingDetailsResponse   {
  @JsonProperty("requestInfo")
  private ResponseInfo requestInfo = null;

  @JsonProperty("hearingDetails")
  private List<LeagleCaseHearingDetails> hearingDetails = null;
}

