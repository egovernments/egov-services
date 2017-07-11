package org.egov.egf.master.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AccountEntitySearchContract extends AccountEntityContract {
	private Integer pageSize;
	private Integer offSet;
}