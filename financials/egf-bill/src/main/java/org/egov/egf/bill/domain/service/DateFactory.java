package org.egov.egf.bill.domain.service;

import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class DateFactory {

	public Date create() {
		return new Date();
	}

}
