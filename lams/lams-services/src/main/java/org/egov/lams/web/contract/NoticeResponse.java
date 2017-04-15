package org.egov.lams.web.contract;

import java.util.ArrayList;
import java.util.List;

import org.egov.lams.model.Notice;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 * NoticeResponse
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NoticeResponse   {
	
  @JsonProperty("ResposneInfo")
  private ResponseInfo resposneInfo = null;

  @JsonProperty("Notices")
  private List<Notice> notices = new ArrayList<Notice>();


}

