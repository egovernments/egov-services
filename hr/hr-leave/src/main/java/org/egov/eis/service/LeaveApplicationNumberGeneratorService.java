package org.egov.eis.service;

import org.egov.eis.persistance.util.DBSequenceGenerator;
import org.egov.eis.persistance.util.SequenceNumberGenerator;
import org.egov.eis.persistance.util.Utils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.sql.SQLException;

@Service
public class LeaveApplicationNumberGeneratorService {

    private SequenceNumberGenerator sequenceNumberGenerator;
    private DBSequenceGenerator dbSequenceGenerator;
    private Utils utils;

    private final String APP_NUMBER_SEQ_PREFIX = "SEQ_LEAVEAPPLICATION_NUMBER%s";

    public LeaveApplicationNumberGeneratorService(SequenceNumberGenerator sequenceNumberGenerator,
            DBSequenceGenerator dbSequenceGenerator, Utils utils) {
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
            } catch (final Exception e) {
                sequenceNumber = dbSequenceGenerator.createAndGetNextSequence(sequenceName);
            }

            return String.format("%05d-%s-%s", sequenceNumber, currentMonth, currentYear);
        } catch (final SQLException e) {
            throw new RuntimeException("Error occurred while generating Leave Application Number", e);
        }
    }
}
