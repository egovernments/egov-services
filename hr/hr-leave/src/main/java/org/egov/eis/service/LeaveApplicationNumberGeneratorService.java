package org.egov.eis.service;

import java.io.Serializable;
import java.sql.SQLException;

import org.egov.persistance.util.DBSequenceGenerator;
import org.egov.persistance.util.SequenceNumberGenerator;
import org.egov.persistance.util.Utils;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LeaveApplicationNumberGeneratorService {

    private SequenceNumberGenerator sequenceNumberGenerator;
    private DBSequenceGenerator dbSequenceGenerator;
    private Utils utils;

    private final String APP_NUMBER_SEQ_PREFIX = "SEQ_LEAVEAPPLICATION_NUMBER%s";

    public LeaveApplicationNumberGeneratorService(SequenceNumberGenerator sequenceNumberGenerator,
                               DBSequenceGenerator dbSequenceGenerator,
                               Utils utils) {
        this.sequenceNumberGenerator = sequenceNumberGenerator;
        this.dbSequenceGenerator = dbSequenceGenerator;
        this.utils = utils;
    }

    @Transactional
    public String generate() {
        try {
            final String currentYear = utils.currentDateToYearFormat();
            final String currentMonth = utils.currentDateToMonthFormat();
            final String sequenceName = String.format(APP_NUMBER_SEQ_PREFIX, currentYear);

            Serializable sequenceNumber;

            try {
                sequenceNumber = sequenceNumberGenerator.getNextSequence(sequenceName);
            } catch (final SQLGrammarException e) {
                sequenceNumber = dbSequenceGenerator.createAndGetNextSequence(sequenceName);
            }

            return String.format("%05d-%02s-%04s", sequenceNumber, currentMonth, currentYear);
        } catch (final SQLException e) {
            throw new RuntimeException("Error occurred while generating Leave Application Number", e);
        }
    }
}
