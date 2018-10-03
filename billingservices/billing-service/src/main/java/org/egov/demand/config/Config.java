package org.egov.demand.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Config {

	@Value("${debit.decimal.taxheadcodes}")
	private String debitTaxHeadCodes;

	@Value("${credit.decimal.taxheadcodes}")
	private String creditTaxHeadCodes;

	Map<String, String> debitDecimalMap;

	Map<String, String> creditDecimalMap;

	@PostConstruct
	public void enrichDecimalTaxHeadMaps() {

	if(StringUtils.isEmpty(creditTaxHeadCodes) || StringUtils.isEmpty(debitTaxHeadCodes)) {

		log.info(" returning due to empty variable");
		debitDecimalMap = new HashMap<>();
		creditDecimalMap = new HashMap<>();
		return;
	}

		String[] debitTaxHeads = debitTaxHeadCodes.split("\\|");
		String[] creditTaxHeads = creditTaxHeadCodes.split("\\|");

		Map<String, String> debitMap = new HashMap<>();
		Map<String, String> creditMap = new HashMap<>();

		for (int i = 0; i < debitTaxHeads.length; i++) {

			String[] keyValue = debitTaxHeads[i].split(":");
			debitMap.put(keyValue[0], keyValue[1]);
		}

		for (int i = 0; i < creditTaxHeads.length; i++) {

			String[] keyValue = creditTaxHeads[i].split(":");
			creditMap.put(keyValue[0], keyValue[1]);
		}

		debitDecimalMap = Collections.unmodifiableMap(debitMap);
		creditDecimalMap = Collections.unmodifiableMap(creditMap);

		log.info(" debit map " + debitDecimalMap);
		log.info(" credit map " + creditDecimalMap);
	}

	public Map<String, String> getDebitDecimalMap() { return debitDecimalMap; }

	public Map<String, String> getCreditDecimalMap() { return creditDecimalMap; }

}
