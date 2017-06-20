package org.egov.asset.service;

import org.egov.asset.model.CreateVoucherHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class VoucherService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void createVoucher(CreateVoucherHelper createVoucherHelper){
		
		
	}
	
}
