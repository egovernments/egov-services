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

package org.egov.boundary.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.boundary.persistence.entity.BoundaryType;
import org.egov.boundary.persistence.entity.HierarchyType;
import org.egov.boundary.persistence.repository.BoundaryTypeRepository;
import org.egov.boundary.web.contract.BoundaryTypeRequest;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BoundaryTypeService {

	@Autowired
	private BoundaryTypeRepository boundaryTypeRepository;

	@Autowired
	private HierarchyTypeService hierarchyTypeService;
	
	@PersistenceContext
	private EntityManager entityManager;
	

	@Transactional
	public BoundaryType createBoundaryType(BoundaryType boundaryType) {
		if (boundaryType.getHierarchyType() != null && boundaryType.getHierarchyType().getId() != null
				&& boundaryType.getHierarchyType().getTenantId() != null
				&& !boundaryType.getHierarchyType().getTenantId().isEmpty())
			boundaryType.setHierarchyType(hierarchyTypeService
					.findByIdAndTenantId(boundaryType.getHierarchyType().getId(), boundaryType.getTenantId()));

		if (boundaryType.getParent() != null && boundaryType.getParent().getId() != null
				&& boundaryType.getTenantId() != null && !boundaryType.getTenantId().isEmpty())
			boundaryType.setParent(findByIdAndTenantId(boundaryType.getParent().getId(), boundaryType.getTenantId()));

		return boundaryTypeRepository.save(boundaryType);
	}

	public BoundaryType findByIdAndTenantId(Long id, String tenantId) {
		return boundaryTypeRepository.findByIdAndTenantId(id, tenantId);
	}

	@Transactional
	public BoundaryType updateBoundaryType(final BoundaryType boundaryType) {
		return boundaryTypeRepository.save(boundaryType);
	}

	public BoundaryType getBoundaryTypeById(final Long id) {
		return boundaryTypeRepository.findOne(id);
	}

	public BoundaryType getBoundaryTypeByName(final String name) {
		return boundaryTypeRepository.findByName(name);
	}

	public BoundaryType getBoundaryTypeByHierarchyTypeNameAndLevel(final String name, final Long hierarchyLevel) {
		return boundaryTypeRepository.findByHierarchyTypeNameAndLevel(name, hierarchyLevel);
	}

	public List<BoundaryType> getAllBoundarTypesByHierarchyTypeIdAndTenantId(final Long hierarchyTypeId,
			final String tenantId) {
		return boundaryTypeRepository.findByHierarchyTypeIdAndTenantId(hierarchyTypeId, tenantId);
	}

	public List<BoundaryType> getAllBoundarTypesByHierarchyTypeIdAndTenantName(final String hierarchyTypeName,
			final String tenantId) {
Session currentSession = entityManager.unwrap(Session.class);
		
		String sql="select t.* from eg_boundary_Type t where hierarchytype in "
				+ "(select id from eg_hierarchy_type h where upper(name)=upper(:hierarchyTypeName) and h.tenantId=:tenantId) and t.tenantId=:tenantId  ";
				
			SQLQuery createSQLQuery = currentSession.createSQLQuery(sql).addScalar("id", LongType.INSTANCE)
				.addScalar("name")
				.addScalar("hierarchy",LongType.INSTANCE)
				.addScalar("code")
				.addScalar("tenantId");
		
		createSQLQuery.setString("hierarchyTypeName", hierarchyTypeName);
		createSQLQuery.setString("tenantId", tenantId);
		//createSQLQuery.setsca
		createSQLQuery.setResultTransformer(Transformers.aliasToBean(BoundaryType.class));
		List boundarylist = createSQLQuery.list();
		
		
		return boundarylist;
	}

	public BoundaryType getBoundaryTypeByParent(final Long parentId) {
		return boundaryTypeRepository.findByParent(parentId);
	}

	public BoundaryType getBoundaryTypeByIdAndHierarchyType(final Long id, final Long hierarchyTypeId) {
		return boundaryTypeRepository.findByIdAndHierarchy(id, hierarchyTypeId);
	}

	public BoundaryType setHierarchyLevel(final BoundaryType boundaryType, final String mode) {
		if ("create".equalsIgnoreCase(mode))
			boundaryType.setHierarchy(1l);
		else {
			final Long parentBoundaryTypeId = boundaryType.getParent().getId();
			Long childHierarchy = 0l;
			Long parentHierarchy = boundaryType.getParent().getHierarchy();
			if (parentBoundaryTypeId != null)
				childHierarchy = ++parentHierarchy;
			boundaryType.setHierarchy(childHierarchy);
		}
		return boundaryType;
	}

	public BoundaryType getBoundaryTypeByNameAndHierarchyType(final String name, final HierarchyType hierarchyType) {
		return boundaryTypeRepository.findByNameAndHierarchyType(name, hierarchyType);
	}

	public BoundaryType getBoundaryTypeByNameAndHierarchyTypeName(final String boundaryTypename,
																  final String hierarchyTypeName,
																  String tenantId) {
		return boundaryTypeRepository.findByNameAndHierarchyTypeName(boundaryTypename, hierarchyTypeName, tenantId);
	}

	public List<BoundaryType> getBoundaryTypeByHierarchyTypeName(final String name) {
		return boundaryTypeRepository.findByHierarchyTypeName(name);
	}

	public List<BoundaryType> getBoundaryTypeByHierarchyTypeNames(final Set<String> names) {
		return boundaryTypeRepository.findByHierarchyTypeNames(names);
	}

	public BoundaryType findByTenantIdAndCode(String tenantId, String code) {
		return boundaryTypeRepository.findByTenantIdAndCode(tenantId, code);

	}

	public List<BoundaryType> getAllBoundaryTypes(BoundaryTypeRequest boundarytypeRequest) {
		List<BoundaryType> boundaryTypes = new ArrayList<BoundaryType>();
		if (boundarytypeRequest.getBoundaryType().getTenantId() != null
				&& !boundarytypeRequest.getBoundaryType().getTenantId().isEmpty()) {
			if (boundarytypeRequest.getBoundaryType().getId() != null) {
				boundaryTypes
						.add(boundaryTypeRepository.findByIdAndTenantId(boundarytypeRequest.getBoundaryType().getId(),
								boundarytypeRequest.getBoundaryType().getTenantId()));
			} else {
				if (boundarytypeRequest.getBoundaryType().getCode() != null) {

					boundaryTypes.add(findByTenantIdAndCode(boundarytypeRequest.getBoundaryType().getCode(),
							boundarytypeRequest.getBoundaryType().getTenantId()));

				} else {
					boundaryTypes.addAll(boundaryTypeRepository
							.findAllByTenantId(boundarytypeRequest.getBoundaryType().getTenantId()));
				}
			}
		}
		return boundaryTypes;
	}

}
