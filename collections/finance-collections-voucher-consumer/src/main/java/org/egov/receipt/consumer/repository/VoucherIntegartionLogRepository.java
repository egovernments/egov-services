package org.egov.receipt.consumer.repository;

import org.egov.receipt.consumer.entity.VoucherIntegrationLog;
import static org.egov.receipt.consumer.repository.builder.VoucherIntegrationLogQueryBuilder.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
@Repository
@Slf4j
public class VoucherIntegartionLogRepository{
	
	@Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public void saveVoucherIntegrationLog(VoucherIntegrationLog vilog){
		try {
			namedParameterJdbcTemplate.update(INSERT_VOUCHER_LOG_SQL, getParametersForVoucherIntegartionLog(vilog));
		} catch (Exception e) {
			log.error("Failed to save the log record"+e.getCause());
			e.printStackTrace();
		}
	}
}
