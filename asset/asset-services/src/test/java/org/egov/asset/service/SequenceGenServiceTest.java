package org.egov.asset.service;

import org.egov.asset.model.enums.Sequence;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class SequenceGenServiceTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private SequenceGenService sequenceGenService;

    @Test
    public void test_getIds() {
        sequenceGenService.getIds(1, Sequence.CURRENTVALUESEQUENCE.toString());
    }

}
