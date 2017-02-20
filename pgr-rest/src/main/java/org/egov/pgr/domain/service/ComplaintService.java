package org.egov.pgr.domain.service;

import org.egov.pgr.domain.model.Complaint;
import org.egov.pgr.domain.model.ComplaintSearchCriteria;
import org.egov.pgr.persistence.queue.contract.SevaRequest;
import org.egov.pgr.persistence.repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplaintService {

    private ComplaintRepository complaintRepository;
    private SevaNumberGeneratorService sevaNumberGeneratorService;

    @Autowired
    public ComplaintService(ComplaintRepository complaintRepository,
                            SevaNumberGeneratorService sevaNumberGeneratorService) {
        this.complaintRepository = complaintRepository;
        this.sevaNumberGeneratorService = sevaNumberGeneratorService;
    }

    public List<Complaint> findAll(ComplaintSearchCriteria complaintSearchCriteria) {
        return complaintRepository.findAll(complaintSearchCriteria);
    }

    public void save(Complaint complaint, SevaRequest sevaRequest) {
        complaint.validate();
        final String crn = sevaNumberGeneratorService.generate();
        complaint.setCrn(crn);
        sevaRequest.update(complaint);
        complaintRepository.save(sevaRequest);
    }

    public void update(Complaint complaint, SevaRequest sevaRequest) {
        complaint.validate();
        sevaRequest.update(complaint);
        complaintRepository.update(sevaRequest);
    }

}

