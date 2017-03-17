package org.egov.pgr.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.egov.pgr.config.PersistenceProperties;
import org.egov.pgr.contracts.email.EmailMessage;
import org.egov.pgr.contracts.grievance.SevaRequest;
import org.egov.pgr.contracts.sms.SmsMessage;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.producer.GrievanceProducer;
import org.egov.pgr.repository.PositionRepository;
import org.egov.pgr.repository.Resources;
import org.egov.pgr.service.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.trimou.util.ImmutableMap;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GrievancePersistenceListenerTest {

    @Mock
    private ComplaintTypeService complaintTypeService;
    @Mock
    private ComplaintStatusService complaintStatusService;
    @Mock
    private ComplaintService complaintService;
    @Mock
    private EscalationService escalationService;
    @Mock
    private GrievanceProducer producer;
    @Mock
    private PositionRepository positionRepository;
    @Mock
    private TemplateService templateService;
    @Mock
    private PersistenceProperties persistenceProperties;
    private Complaint complaint;
    @InjectMocks
    private GrievancePersistenceListener listener;
    private String key = "sevaReq";
    private long offset = 1L;
    private String topic = "temp";
    private int partition = 1;
    private String complaintCrn = "12-1532-AA";
    private ComplaintType complaintType;

    @Before
    public void setUp() throws Exception {
        complaint = new Complaint();
        complaint.setCrn(complaintCrn);
        complaint.setCreatedDate(new Date());
        complaintType = new ComplaintType();
        complaintType.setName("Some horrible thing!");

        when(complaintService.findByCrn(complaintCrn)).thenReturn(complaint);
        when(complaintService.save(complaint)).thenReturn(complaint);
        when(complaintTypeService.findByCode("PGDBC")).thenReturn(complaintType);
        when(persistenceProperties.getSmsTopic()).thenReturn("smsTopic");
        when(persistenceProperties.getEmailTopic()).thenReturn("emailTopic");
        when(persistenceProperties.getIndexingTopic()).thenReturn("indexingTopic");
        when(templateService.loadByName(eq("sms_en"), any(Map.class))).thenReturn("sms body");
        when(templateService.loadByName(eq("email_subject_en"), any(Map.class))).thenReturn("email subject");
        when(templateService.loadByName(eq("email_body_en"), any(ImmutableMap.ImmutableMapBuilder.class))).thenReturn("email body");
    }

    @Test
    public void testSmsMessageIsSentAsNotificationToSmsService() throws Exception {
        String sevaRequestStr = new Resources().getFileContents("sevaRequest.json");
        HashMap<String, Object> sevaRequestMap = new ObjectMapper().readValue(sevaRequestStr, HashMap.class);
        listener.processMessage(sevaRequestMap);
        ArgumentCaptor<SmsMessage> captor = ArgumentCaptor.forClass(SmsMessage.class);

        verify(producer, atLeastOnce()).sendMessage(eq("smsTopic"), captor.capture());
        SmsMessage value = captor.getValue();
        assertEquals("sms body", value.getMessage());
        assertEquals("9988776655", value.getMobileNumber());
    }

    @Test
    public void testEmailMesageIsSentToEmailService() throws Exception {
        String sevaRequestStr = new Resources().getFileContents("sevaRequest.json");
        HashMap<String, Object> sevaRequestMap = new ObjectMapper().readValue(sevaRequestStr, HashMap.class);
        listener.processMessage(sevaRequestMap);
        ArgumentCaptor<EmailMessage> captor = ArgumentCaptor.forClass(EmailMessage.class);

        verify(producer, atLeastOnce()).sendMessage(eq("emailTopic"), captor.capture());
        EmailMessage value = captor.getValue();
        assertEquals("email body", value.getBody());
        assertEquals("email subject", value.getSubject());
        assertEquals(StringUtils.EMPTY, value.getSender());
        assertEquals("jim@maildrop.cc", value.getEmail());
    }

    @Test
    public void testIndexerIsNotifiedWithASevaRequestMap() throws Exception {
        String sevaRequestStr = new Resources().getFileContents("sevaRequest.json");
        HashMap<String, Object> sevaRequestMap = new ObjectMapper().readValue(sevaRequestStr, HashMap.class);
        listener.processMessage(sevaRequestMap);
        ArgumentCaptor<HashMap> captor = ArgumentCaptor.forClass(HashMap.class);

        verify(producer, atLeastOnce()).sendMessage(eq("indexingTopic"), captor.capture());
        HashMap<String, HashMap> value = captor.getValue();
        assertEquals("12-1532-AA", value.get("ServiceRequest").get("service_request_id"));
    }


}