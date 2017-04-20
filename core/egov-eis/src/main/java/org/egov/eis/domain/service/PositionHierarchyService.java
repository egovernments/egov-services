/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.eis.domain.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.egov.eis.persistence.entity.Position;
import org.egov.eis.persistence.entity.PositionHierarchy;
import org.egov.eis.web.contract.PositionHierarchyRequest;
import org.egov.eis.persistence.repository.PositionHierarchyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PositionHierarchyService {

	private final PositionHierarchyRepository positionHierarchyRepository;

	@Autowired
	public PositionHierarchyService(final PositionHierarchyRepository positionHierarchyRepository) {
		this.positionHierarchyRepository = positionHierarchyRepository;
	}

	@Transactional
	public void createPositionHierarchy(final PositionHierarchy positionHierarchy) {
		positionHierarchyRepository.save(positionHierarchy);
	}

	@Transactional
	public void updatePositionHierarchy(final PositionHierarchy positionHierarchy) {
		positionHierarchyRepository.save(positionHierarchy);
	}

	@Transactional
	public void deletePositionHierarchy(final PositionHierarchy positionHierarchy) {
		positionHierarchyRepository.delete(positionHierarchy);
	}

	public PositionHierarchy getPositionHierarchyByPosAndObjectType(final Long posId, final Integer objectType) {
		return positionHierarchyRepository.getPositionHierarchyByPosAndObjectType(posId, objectType);
	}

	public PositionHierarchy getPosHirByPosAndObjectTypeAndObjectSubType(final Long posId, final String objectType,
			final String objectSubType,final String tenantId) {
		return positionHierarchyRepository.getPosHirByPosAndObjectTypeAndObjectSubType(posId, objectType,
				objectSubType,tenantId);
	}

	public PositionHierarchy getPosHirByFromAndToPosAndObjectTypeAndObjectSubType(final Long fromPosId,
			final Long toPosId, final String objectType, final String objectSubType, final String tenantId) {
		return positionHierarchyRepository.getPosHirByFromAndToPosAndObjectTypeAndObjectSubType(fromPosId, toPosId,
				objectType, objectSubType,tenantId);
	}

	public List<PositionHierarchy> getPosHirByToPosAndObjectTypeAndObjectSubType(final Long toPosId,
			final String objectType, final String objectSubType) {
		return positionHierarchyRepository.getPosHirByToPosAndObjectTypeAndObjectSubType(toPosId, objectType,
				objectSubType);
	}

	public List<PositionHierarchy> getPosHirByObjectTypeAndObjectSubType(final String objectType,
			final String objectSubType, final String tenantId) {
		return positionHierarchyRepository.getPosHirByObjectTypeAndObjectSubType(objectType, objectSubType,tenantId);
	}

	public void deleteAllInBatch(final List<PositionHierarchy> existingPosHierarchy) {
		positionHierarchyRepository.deleteInBatch(existingPosHierarchy);

	}

	public List<PositionHierarchy> getPositionHeirarchyByFromPositionAndObjectType(Long fromPositionId,
			String objectType) {
		return positionHierarchyRepository.getListOfPositionHeirarchyByFromPositionAndObjectType(fromPositionId,
				objectType);
	}

	public List<PositionHierarchy> getListOfPositionHeirarchyByObjectType(String objectType,String tenantId) {
		return positionHierarchyRepository.getListOfPositionHeirarchyByObjectType(objectType,tenantId);
	}

	public List<PositionHierarchy> getListOfPositionHeirarchyByObjectSubType(final String objectSubType) {
		return positionHierarchyRepository.getListOfPositionHeirarchyByObjectSubType(objectSubType);
	}

	public List<PositionHierarchy> getListOfPositionHeirarchyByFromPositionAndObjectTypeAndSubType(Long fromPositionId,
			String objectType, final String objectSubType, final String tenantId) {

		if (fromPositionId != 0 && objectType != null && objectSubType != null)
			return positionHierarchyRepository.getListOfPositionHeirarchyByFromPositionAndObjectTypeAndSubType(
					fromPositionId, objectType, objectSubType);
		else if (fromPositionId == 0 && objectType != null && objectSubType != null)
			return positionHierarchyRepository.getPosHirByObjectTypeAndObjectSubType(objectType, objectSubType,tenantId);
		else if (fromPositionId != 0 && objectType != null && objectSubType == null)
			return positionHierarchyRepository.getListOfPositionHeirarchyByFromPositionAndObjectType(fromPositionId,
					objectType);
		else if (fromPositionId == 0 && objectType != null && objectSubType == null)
			return positionHierarchyRepository.getListOfPositionHeirarchyByObjectType(objectType,tenantId);
		else
			return Collections.emptyList();
	}

	public List<PositionHierarchy> getListOfPositionHeirarchyByPositionObjectTypeSubType(final String objectType,
			final List<String> compTypeCodes, final Position fromPositionId) {
		return positionHierarchyRepository.findPositionHierarchyByComplaintTypesAndFromPosition(objectType,
				compTypeCodes, fromPositionId);
	}

	public List<PositionHierarchy> getPositionHierarchys(PositionHierarchyRequest positionHierarchyRequest) {
		List<PositionHierarchy> positionHierarchys = new ArrayList<PositionHierarchy>();
		if (positionHierarchyRequest.getPositionHierarchy() != null) {
			if ((positionHierarchyRequest.getPositionHierarchy().getObjectType() != null
					&& positionHierarchyRequest.getPositionHierarchy().getObjectType().getType() != null
					&& !positionHierarchyRequest.getPositionHierarchy().getObjectType().getType().isEmpty())
					&& (positionHierarchyRequest.getPositionHierarchy().getObjectSubType() != null
							&& !positionHierarchyRequest.getPositionHierarchy().getObjectSubType().isEmpty())
					&& (positionHierarchyRequest.getPositionHierarchy().getFromPosition() != null
							&& positionHierarchyRequest.getPositionHierarchy().getFromPosition().getId() != null)
					&& (positionHierarchyRequest.getPositionHierarchy().getToPosition() == null
							|| positionHierarchyRequest.getPositionHierarchy().getToPosition().getId() == null)) {
				positionHierarchys.add(getPosHirByPosAndObjectTypeAndObjectSubType(
						positionHierarchyRequest.getPositionHierarchy().getFromPosition().getId(),
						positionHierarchyRequest.getPositionHierarchy().getObjectType().getType(),
						positionHierarchyRequest.getPositionHierarchy().getObjectSubType(),
						positionHierarchyRequest.getPositionHierarchy().getTenantId()));
			}
		}

		return positionHierarchys;
	}

}
