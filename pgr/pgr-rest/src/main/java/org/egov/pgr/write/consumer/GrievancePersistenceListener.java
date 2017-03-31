package org.egov.pgr.write.consumer;

import org.egov.pgr.common.repository.UserRepository;
import org.egov.pgr.write.contracts.grievance.SevaRequest;
import org.egov.pgr.write.entity.Complaint;
import org.egov.pgr.write.model.ComplaintBuilder;
import org.egov.pgr.write.repository.PositionRepository;
import org.egov.pgr.write.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class GrievancePersistenceListener {

    private ComplaintTypeWriteService complaintTypeWriteService;
    private ComplaintStatusWriteService complaintStatusWriteService;
    private ComplaintWriteService complaintWriteService;
    private PositionRepository positionRepository;
    private UserRepository userWriteRepository;
    private ReceivingCenterWriteService receivingCenterWriteService;
    private ReceivingModeWriteService receivingModeWriteService;

    @Autowired
    public GrievancePersistenceListener(ComplaintTypeWriteService complaintTypeWriteService,
                                        ComplaintStatusWriteService complaintStatusWriteService,
                                        ComplaintWriteService complaintWriteService,
                                        PositionRepository positionRepository,
                                        UserRepository userRepository,
                                        ReceivingCenterWriteService receivingCenterWriteService,
                                        ReceivingModeWriteService receivingModeWriteService) {
        this.complaintWriteService = complaintWriteService;
        this.complaintTypeWriteService = complaintTypeWriteService;
        this.complaintStatusWriteService = complaintStatusWriteService;
        this.complaintWriteService = complaintWriteService;
        this.positionRepository = positionRepository;
        this.userWriteRepository = userRepository;
        this.receivingCenterWriteService = receivingCenterWriteService;
        this.receivingModeWriteService = receivingModeWriteService;

    }

    @KafkaListener(topics = "${kafka.topics.pgr.workflowupdated.name}")
    public void processMessage(HashMap<String, Object> sevaRequestMap) {
        SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);
        persistComplaint(sevaRequest);
    }

    private void persistComplaint(SevaRequest sevaRequest) {
        String complaintCrn = sevaRequest.getServiceRequest().getCrn();
        Complaint complaintByCrn = complaintWriteService.findByCrn(complaintCrn);
        Complaint complaint = new ComplaintBuilder(complaintByCrn, sevaRequest, complaintTypeWriteService,
            complaintStatusWriteService, positionRepository, userWriteRepository, receivingCenterWriteService,
            receivingModeWriteService).build();
        complaintWriteService.save(complaint);
    }
}
