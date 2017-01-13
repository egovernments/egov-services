package org.egov.pgr.rest.web.numbergeneration;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.utils.DBSequenceGenerator;
import org.egov.infra.persistence.utils.SequenceNumberGenerator;
import org.egov.infra.utils.DateUtils;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.sql.SQLException;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.StringUtils.upperCase;

@Service
public class SevaNumberGeneratorImpl implements SevaNumberGenerator {

	 private static final String APP_NUMBER_SEQ_PREFIX = "SEQ_APPLICATION_NUMBER%s";
	   
	    @Autowired
	    private DBSequenceGenerator dbSequenceGenerator;

	    @Autowired
	    private SequenceNumberGenerator sequenceNumberGenerator;

	    @Transactional
	    public String generate() {
	        try {
	            final String currentYear = DateUtils.currentDateToYearFormat();
	            final String sequenceName = String.format(APP_NUMBER_SEQ_PREFIX, currentYear);
	            Serializable sequenceNumber;
	            try {
	                sequenceNumber = sequenceNumberGenerator.getNextSequence(sequenceName);
	            } catch (final SQLGrammarException e) {
	                sequenceNumber = dbSequenceGenerator.createAndGetNextSequence(sequenceName);
	            }
	            return String.format("%05d-%s-%s", sequenceNumber, currentYear, upperCase(randomAlphabetic(2)));
	        } catch (final SQLException e) {
	            throw new ApplicationRuntimeException("Error occurred while generating Application Number", e);
	        }
	    }
}
