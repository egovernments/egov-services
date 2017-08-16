package org.egov.tradelicense.domain.service;

import java.io.Serializable;
import java.sql.SQLException;

import org.egov.tradelicense.persistence.util.DBSequenceGenerator;
import org.egov.tradelicense.persistence.util.SequenceNumberGenerator;
import org.egov.tradelicense.persistence.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementation Generator class for Trade License Number
 * 
 * @author Manoj Kulkarni
 *
 */
public class TradeLicenseNumberGeneratorServiceImpl implements TradeLicenseNumberGeneratorService {
    
    @Autowired
    private SequenceNumberGenerator sequenceNumberGenerator;
    
    @Autowired
    private DBSequenceGenerator dbSequenceGenerator;
    
    @Autowired
    private Utils utils;
    
    private static final String LICENSE_NUMBER_SEQ_NAME = "egtl_license_number";
    private static final String LICENSE_NUMBER_FORMAT = "TL/%05d/%s";
    
    /**
     * Implementation Method to generate Trade License Number
     * 
     * @return generated number
     */
    public String generate() {
        Serializable sequenceNumber;
        try {
            final String currentYear = utils.currentDateToYearFormat();
            try {
                sequenceNumber = sequenceNumberGenerator.getNextSequence(LICENSE_NUMBER_SEQ_NAME);
            } catch (final Exception e) {
                sequenceNumber = dbSequenceGenerator.createAndGetNextSequence(LICENSE_NUMBER_SEQ_NAME);
            }

            return String.format(LICENSE_NUMBER_FORMAT, sequenceNumber, currentYear);
        } catch (final SQLException e) {
            throw new RuntimeException("Error occurred while generating Trade License Number", e);
        }
    }
}
