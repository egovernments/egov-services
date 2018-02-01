/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.eis.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.config.PropertiesManager;
import org.egov.eis.model.Department;
import org.egov.eis.model.DepartmentDesignation;
import org.egov.eis.model.Designation;
import org.egov.eis.model.Position;
import org.egov.eis.model.PositionSync;
import org.egov.eis.repository.PositionRepository;
import org.egov.eis.web.contract.DesignationGetRequest;
import org.egov.eis.web.contract.Pagination;
import org.egov.eis.web.contract.PositionBulkRequest;
import org.egov.eis.web.contract.PositionGetRequest;
import org.egov.eis.web.contract.PositionRequest;
import org.egov.eis.web.contract.PositionResponse;
import org.egov.eis.web.contract.PositionSyncRequest;
import org.egov.eis.web.contract.RequestInfoWrapper;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.egov.eis.web.errorhandlers.InvalidDataException;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PositionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PositionService.class);

	@Value("${kafka.topics.position.db_persist.name}")
	private String positionDBPersistTopic;

	@Value("${kafka.topics.position.create.name}")
	private String positionCreateTopic;

	@Value("${kafka.topics.position.update.name}")
	private String positionUpdateTopic;

	@Autowired
	private PositionRepository positionRepository;

	@Autowired
	private DepartmentDesignationService deptDesigService;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private DesignationService designationService;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private PropertiesManager propertiesManager;
	
	@Autowired
    private ApplicationProperties applicationProperties;

	public List<Position> getPositions(PositionGetRequest positionGetRequest) {
		return positionRepository.findForCriteria(positionGetRequest);
	}

	public Map<String, Object> getPaginatedPosistions(PositionGetRequest positionGetRequest, RequestInfo requestInfo)
			throws CloneNotSupportedException {
		List<Position> positions = getPositions(positionGetRequest);
		if (isEmpty(positions))
			return getResponseForNoRecords(positionGetRequest.getPageSize(), positionGetRequest.getPageNumber());
		
		return getResponseForExistingRecords(positionGetRequest.getPageSize(), positionGetRequest.getPageNumber(),
				positions.size(), positionGetRequest, positions);
	}

	private Map<String, Object> getResponseForExistingRecords(Integer pageSize, Integer pageNumber,
			Integer recordsFetched, PositionGetRequest positionGetRequest, List<Position> positions) {
		pageSize = isEmpty(pageSize) ? Integer.parseInt(applicationProperties.hrSearchPageSizeDefault())
				: pageSize;
		pageNumber = isEmpty(pageNumber) ? 1 : pageNumber;

		Integer totalDBRecords = positionRepository.getTotalDBRecords(positionGetRequest);
		Integer totalpages = (int) Math.ceil((double) totalDBRecords / pageSize);
		Pagination page = Pagination.builder().totalResults(totalDBRecords).totalPages(totalpages)
				.currentPage(pageNumber).pageNumber(pageNumber).pageSize(pageSize).build();

		return new LinkedHashMap<String, Object>() {
			{
				put("Position", positions);
				put("Page", page);
			}
		};
	}

	private Map<String, Object> getResponseForNoRecords(Integer pageSize, Integer pageNumber) {
		Pagination page = Pagination.builder().totalResults(0).totalPages(0).currentPage(0).pageNumber(pageNumber)
				.pageSize(pageSize).build();
		return new LinkedHashMap<String, Object>() {
			{
				put("Position", Collections.EMPTY_LIST);
				put("Page", page);
			}
		};
	}

	public ResponseEntity<?> createPosition(PositionRequest positionRequest) {
		List<Position> positions = positionRequest.getPosition();
		validateDepartment(positionRequest);
		validateDesignation(positionRequest);
		kafkaTemplate.send(positionDBPersistTopic, positionRequest);
		return getSuccessResponseForCreate(positions, positionRequest.getRequestInfo());
	}

	public ResponseEntity<?> createBulkPositions(PositionBulkRequest positionBulkRequest) {
		PositionRequest positionRequest = getPositionRequest(positionBulkRequest);
		return createPosition(positionRequest);
	}

	public ResponseEntity<?> updatePosition(PositionRequest positionRequest) {
		List<Position> positions = positionRequest.getPosition();
		kafkaTemplate.send(positionUpdateTopic, positionRequest);
		return getSuccessResponseForCreate(positions, positionRequest.getRequestInfo());
	}

	/**
	 * Populate PositionResponse object & returns ResponseEntity of type
	 * PositionResponse containing ResponseInfo & array of Position objects
	 *
	 * @param positionList
	 * @param requestInfo
	 * @return ResponseEntity<?>
	 */
	public ResponseEntity<?> getSuccessResponseForCreate(List<Position> positionList, RequestInfo requestInfo) {
		PositionResponse positionResponse = new PositionResponse();
		positionResponse.getPosition().addAll(positionList);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		positionResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<PositionResponse>(positionResponse, HttpStatus.OK);
	}

	@Transactional
	public void create(PositionRequest positionRequest) {
		populateCreatePositionDetails(positionRequest);
		List<Long> ids = positionRequest.getPosition().stream().map(Position::getId).collect(Collectors.toList());
		int index = 1;
		for (Position position : positionRequest.getPosition()) {

			String name = positionRepository.generatePositionNameWithMultiplePosition(position.getName(),
					position.getDeptdesig().getId(), position.getTenantId(), index);
			position.setName(name);
			index++;
		}

		positionRepository.create(positionRequest);
		PositionGetRequest positionGetRequest = new PositionGetRequest();
		positionGetRequest.setId(ids);
		// FIXME : Assuming request will come from same tenant & so, adding
		// common tenantId for all positions search.
		positionGetRequest.setTenantId(positionRequest.getRequestInfo().getUserInfo().getTenantId());
		List<Position> positions = positionRepository.findForCriteria(positionGetRequest);
		positionRequest.setPosition(positions);
		kafkaTemplate.send(positionCreateTopic, positionRequest);
		if (propertiesManager.getDataSyncPositionRequired()) {
			for (Position position : positionRequest.getPosition()) {
				PositionSync positionSync = PositionSync.builder().name(position.getName())
						.tenantId(position.getTenantId()).build();
				PositionSyncRequest positionSyncRequest = PositionSyncRequest.builder().positionSync(positionSync)
						.build();
				kafkaTemplate.send(propertiesManager.getSavePositionTopic(), positionSyncRequest);
			}
		}

	}

	@Transactional
	public void update(PositionRequest positionRequest) {
		positionRepository.update(positionRequest);
	}

	private void populateCreatePositionDetails(PositionRequest positionRequest) {
		List<Position> positions = positionRequest.getPosition();
		RequestInfo requestInfo = positionRequest.getRequestInfo();
		RequestInfoWrapper requestInfoWrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
		List<Position> positionsList = new ArrayList<>();
		for (int i = 0; i < positions.size(); i++) {
			DepartmentDesignation deptDesig = positions.get(i).getDeptdesig();
			String tenantId = positions.get(i).getTenantId();
			Designation designation = new Designation();
			Department department = departmentService
					.getDepartments(Arrays.asList(deptDesig.getDepartmentId()), tenantId, requestInfoWrapper).get(0);

			System.out.println("department Id" + department.getId());
			System.out.println("department tenantId" + department.getTenantId());

			if (null != deptDesig.getDesignation().getId()) {
				DesignationGetRequest designationGetRequest = DesignationGetRequest.builder()
						.id(Arrays.asList(deptDesig.getDesignation().getId())).tenantId(tenantId).build();
				designation = designationService.getDesignations(designationGetRequest).get(0);
			}

			System.out.println("designation Id" + designation.getId());
			System.out.println("designation tenantId" + designation.getTenantId());

			deptDesig = deptDesigService.getByDepartmentAndDesignation(department.getId(), designation.getId(),
					tenantId);

			if (isEmpty(deptDesig)) {
				deptDesig = DepartmentDesignation.builder().departmentId(department.getId()).designation(designation)
						.tenantId(tenantId).build();
				deptDesigService.create(deptDesig);
			}
			deptDesig = deptDesigService.getByDepartmentAndDesignation(department.getId(), designation.getId(),
					tenantId);

			System.out.println("deptDesig Id" + deptDesig.getId());
			System.out.println("deptDesig tenantId" + deptDesig.getTenantId());

			List<Long> sequences;
			if (positions.get(i).getNoOfPositions() != null)
				sequences = positionRepository.generateSequences(positions.get(i).getNoOfPositions());
			else {
				sequences = positionRepository.generateSequences(1);
				positions.get(0).setId(sequences.get(0));
			}

			if (positions.get(i).getNoOfPositions() != null) {
				for (int j = 0; j < positions.get(i).getNoOfPositions(); j++) {
					String name = department.getCode().toUpperCase() + "_" + designation.getCode().toUpperCase() + "_";
					Position pos = Position.builder().id(sequences.get(j)).name(name).active(true).deptdesig(deptDesig)
							.isPostOutsourced(positions.get(i).getIsPostOutsourced())
							.tenantId(positions.get(i).getTenantId()).build();
					positionsList.add(pos);
				}
			}
			if (positions.get(i).getNoOfPositions() != null)
				positionRequest.setPosition(positionsList);

		}
	}

	private PositionRequest getPositionRequest(PositionBulkRequest positionBulkRequest) {
		RequestInfo requestInfo = positionBulkRequest.getRequestInfo();
		RequestInfoWrapper requestInfoWrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();

		List<Position> positions = positionBulkRequest.getPosition().stream().map(position -> {
			Department department = departmentService.getDepartmentByCode(position.getDeptdesig().getDepartmentCode(),
					position.getTenantId(), requestInfoWrapper);
			if (department == null || isEmpty(department)) {

				throw new InvalidDataException("department",
						"The field {0} should have a valid value which exists in the system", "null");

			}
			DesignationGetRequest designationGetRequest = DesignationGetRequest.builder()
					.code(position.getDeptdesig().getDesignation().getCode()).tenantId(position.getTenantId()).build();

			List<Designation> designations = designationService.getDesignations(designationGetRequest);

			if (designations.isEmpty()) {

				throw new InvalidDataException("designation",
						"The designation should have a valid value which exists in the system", "null");

			}
			final Designation designation = designations.get(0);
			DepartmentDesignation deptDesig = DepartmentDesignation.builder().id(position.getDeptdesig().getId())
					.departmentId(department.getId()).designation(designation)
					.tenantId(position.getDeptdesig().getTenantId()).build();

			return Position.builder().id(position.getId()).name(position.getName()).active(position.getActive())
					.isPostOutsourced(position.getIsPostOutsourced()).noOfPositions(position.getNoOfPositions())
					.deptdesig(deptDesig).tenantId(position.getTenantId()).build();
		}).collect(Collectors.toList());

		return PositionRequest.builder().position(positions).requestInfo(requestInfo).build();
	}

	private void validateDepartment(PositionRequest positionRequest) {
		List<Position> positions = positionRequest.getPosition();
		RequestInfo requestInfo = positionRequest.getRequestInfo();
		RequestInfoWrapper requestInfoWrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
		for (int i = 0; i < positions.size(); i++) {
			DepartmentDesignation deptDesig = positions.get(i).getDeptdesig();
			String tenantId = positions.get(i).getTenantId();

			List<Department> departments = departmentService.getDepartments(Arrays.asList(deptDesig.getDepartmentId()),
					tenantId, requestInfoWrapper);
			if (departments == null || departments.size() < 1) {
				throw new InvalidDataException("department",
						"The department should have a valid value which exists in the system", "null");
			}
		}
	}

	private void validateDesignation(PositionRequest positionRequest) {

		List<Position> positions = positionRequest.getPosition();
		for (int i = 0; i < positions.size(); i++) {
			if (null == positions.get(i).getDeptdesig().getDesignation().getId()) {
				throw new InvalidDataException("designation",
						"The designation should have a valid value which exists in the system", "null");

			}
			DepartmentDesignation deptDesig = positions.get(i).getDeptdesig();
			String tenantId = positions.get(i).getTenantId();
			if (null != deptDesig.getDesignation().getId()) {
				DesignationGetRequest designationGetRequest = DesignationGetRequest.builder()
						.id(Arrays.asList(deptDesig.getDesignation().getId())).tenantId(tenantId).build();
				List<Designation> designations = designationService.getDesignations(designationGetRequest);

				if (designations == null || designations.size() < 1) {

					throw new InvalidDataException("designations",
							"designations should have a valid value which exists in the system", "null");
				}
			}
		}
	}
}
