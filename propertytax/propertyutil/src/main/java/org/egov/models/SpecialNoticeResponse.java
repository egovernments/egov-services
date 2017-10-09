package org.egov.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
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

	private SpecialNotice notice;

}
