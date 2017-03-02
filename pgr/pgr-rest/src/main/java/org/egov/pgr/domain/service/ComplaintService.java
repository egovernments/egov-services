package org.egov.pgr.domain.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.pgr.domain.model.Complaint;
import org.egov.pgr.domain.model.ComplaintSearchCriteria;
import org.egov.pgr.persistence.queue.contract.SevaRequest;
import org.egov.pgr.persistence.repository.ComplaintRepository;
import org.egov.pgr.persistence.repository.DepartmentRepository;
import org.egov.pgr.web.contract.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComplaintService {

	private ComplaintRepository complaintRepository;
	private DepartmentRepository departmentRepository;
	private SevaNumberGeneratorService sevaNumberGeneratorService;

	@Autowired
	public ComplaintService(ComplaintRepository complaintRepository,
			SevaNumberGeneratorService sevaNumberGeneratorService, DepartmentRepository departmentRepository) {
		this.complaintRepository = complaintRepository;
		this.departmentRepository = departmentRepository;
		this.sevaNumberGeneratorService = sevaNumberGeneratorService;
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
			if (ct.getDepartment() == null) {
				if (ct.getAdditionalValues() == null)
					ct.setAdditionalValues(new HashMap<String, String>());
				ct.getAdditionalValues().put("departmentName", deptIdNameMap.get(ct.getDepartment()));
			}
		}

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
