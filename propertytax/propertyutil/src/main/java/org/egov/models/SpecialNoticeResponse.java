package org.egov.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
/**
 * 
 * @author Prasad Khandagale
 *
 */
public class SpecialNoticeResponse {

	private ResponseInfo responseInfo;

	private Notice notice;

}
