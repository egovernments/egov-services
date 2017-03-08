package org.egov.pgr.domain.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.pgr.domain.model.Complaint;
import org.egov.pgr.domain.model.ComplaintSearchCriteria;
import org.egov.pgr.persistence.queue.contract.RequestInfo;
import org.egov.pgr.persistence.queue.contract.SevaRequest;
import org.egov.pgr.persistence.repository.ComplaintRepository;
import org.egov.pgr.persistence.repository.DepartmentRepository;
import org.egov.pgr.persistence.repository.UserRepository;
import org.egov.pgr.web.contract.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComplaintService {

	private ComplaintRepository complaintRepository;
	private DepartmentRepository departmentRepository;
	private UserRepository userRepository;
	private SevaNumberGeneratorService sevaNumberGeneratorService;

	@Autowired
	public ComplaintService(ComplaintRepository complaintRepository,
			SevaNumberGeneratorService sevaNumberGeneratorService, DepartmentRepository departmentRepository,
			UserRepository userRepository) {
		this.complaintRepository = complaintRepository;
		this.departmentRepository = departmentRepository;
		this.sevaNumberGeneratorService = sevaNumberGeneratorService;
		this.userRepository = userRepository;
	}

	public List<Complaint> findAll(ComplaintSearchCriteria complaintSearchCriteria) {
		List<Department> departments = departmentRepository.getAll();
		List<Complaint> complaints = complaintRepository.findAll(complaintSearchCriteria);
		populateDepartmentNames(complaints, departments);
		return complaints;
	}

	private void populateDepartmentNames(List<Complaint> complaints, List<Department> departments) {

		Map<Long, String> deptIdNameMap = new HashMap<>();
		for (Department dept : departments) {
			deptIdNameMap.put(dept.getId(), dept.getName());
		}
		for (Complaint ct : complaints) {
			if (ct.getDepartment() != null) {
				if (ct.getAdditionalValues() == null)
					ct.setAdditionalValues(new HashMap<String, String>());
				ct.getAdditionalValues().put("departmentName", deptIdNameMap.get(ct.getDepartment()));
			} else {
				if (ct.getAdditionalValues() == null)
					ct.setAdditionalValues(new HashMap<String, String>());
				ct.getAdditionalValues().put("departmentName", "");
			}
		}

	}

	public void save(Complaint complaint, SevaRequest sevaRequest) {
		complaint.validate();
		final String crn = sevaNumberGeneratorService.generate();
		complaint.setCrn(crn);
		sevaRequest.update(complaint);
		populateRequesterId(sevaRequest, complaint);
		complaintRepository.save(sevaRequest);
	}

	private void populateRequesterId(SevaRequest sevaRequest, Complaint complaint) {

		if (sevaRequest.getRequestInfo() != null && StringUtils.isNotEmpty(sevaRequest.getRequestInfo().getAuthToken()))
			sevaRequest.getRequestInfo().setRequesterId(complaint.getAuthenticatedUser().getId().toString());
		else if (sevaRequest.getRequestInfo() != null) {
			sevaRequest.getRequestInfo()
					.setRequesterId(userRepository.getUserByUserName("anonymous").getId().toString());
		} else {
			sevaRequest.setRequestInfo(new RequestInfo("", "", new Date(), "", "", "", "",
					userRepository.getUserByUserName("anonymous").getId().toString(), ""));
		}
	}

	public void update(Complaint complaint, SevaRequest sevaRequest) {
		complaint.validate();
		sevaRequest.update(complaint);
		complaintRepository.update(sevaRequest);
	}

}
