package org.egov.pgr.write.service;

import org.egov.pgr.write.model.ComplaintRecord;
import org.egov.pgr.write.repository.ComplaintWriteRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ComplaintWriteServiceTest {

    @InjectMocks
    private ComplaintWriteService complaintWriteService;

    @Mock
    private ComplaintWriteRepository complaintWriteRepository;

    @Test
    public void test_should_persist_complaint_record() {
        final ComplaintRecord complaintRecord = ComplaintRecord.builder().build();

        complaintWriteService.updateOrInsert(complaintRecord);

        verify(complaintWriteRepository).updateOrInsert(complaintRecord);
    }


}