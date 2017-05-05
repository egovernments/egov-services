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

package org.egov.boundary.persistence.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.egov.boundary.persistence.entity.Boundary;
import org.egov.boundary.persistence.entity.BoundaryType;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoundaryRepository {

	private EntityManager entityManager;

	@Autowired
	public BoundaryRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public List<Boundary> getBoundariesByBndryTypeNameAndHierarchyTypeNameAndTenantId(final String boundaryTypeName,
			final String hierarchyTypeName, final String tenantId) {
		Session currentSession = entityManager.unwrap(Session.class);

		String sql = "select b.* from eg_Boundary b where b.boundarytype="
				+ "(select id from eg_boundary_Type t where upper(t.name)=upper(:boundaryTypeName) and t.hierarchyType="
				+ "(select id from eg_hierarchy_type h where upper(name)=upper(:hierarchyTypeName) and h.tenantId=:tenantId) and t.tenantId=:tenantId)  "
				+ "and b.tenantid=:tenantId";
		SQLQuery createSQLQuery = currentSession.createSQLQuery(sql).addScalar("id", LongType.INSTANCE)
				.addScalar("name").addScalar("boundaryNum", LongType.INSTANCE).addScalar("tenantId");

		createSQLQuery.setString("boundaryTypeName", boundaryTypeName);
		createSQLQuery.setString("hierarchyTypeName", hierarchyTypeName);
		createSQLQuery.setString("tenantId", tenantId);
		createSQLQuery.setResultTransformer(Transformers.aliasToBean(Boundary.class));

		return createSQLQuery.list();
	}

	public List<Boundary> getAllBoundariesByBoundaryTypeIdAndTenantId(final Long boundaryTypeId,
			final String tenantId) {
		Session currentSession = entityManager.unwrap(Session.class);

		String sql = "select b.id as id ,b.name as name, b.boundaryNum as boundaryNum,b.tenantId as tenantId ,b.parent as \"parent.id\",bt.id as \"boundaryType.id\" ,bt.name as \"boundaryType.name\" from eg_boundary b,eg_boundary_Type bt where bt.id=:id and b.tenantId=:tenantId and b.boundarytype=bt.id and bt.tenantid=:tenantId";

		SQLQuery createSQLQuery = currentSession.createSQLQuery(sql).addScalar("id", LongType.INSTANCE)
				.addScalar("name").addScalar("boundaryNum", LongType.INSTANCE)
				.addScalar("boundaryType.id", LongType.INSTANCE).addScalar("boundaryType.name")
				.addScalar("parent.id", LongType.INSTANCE).addScalar("tenantId");

		createSQLQuery.setLong("id", boundaryTypeId);
		createSQLQuery.setString("tenantId", tenantId);

		return mapToBoundary(createSQLQuery.list());
	}

	public List<Boundary> getBoundariesByIdAndTenantId(final Long id, final String tenantId) {
		Session currentSession = entityManager.unwrap(Session.class);

		String sql = "select b.id as id ,b.name as name, b.boundaryNum as boundaryNum,b.tenantId as tenantId ,b.parent as \"parent.id\",bt.id as \"boundaryType.id\" ,bt.name as \"boundaryType.name\" from eg_boundary b,eg_boundary_Type bt where b.id=:id and b.tenantId=:tenantId and b.boundarytype=bt.id and bt.tenantid=:tenantId";

		SQLQuery createSQLQuery = currentSession.createSQLQuery(sql).addScalar("id", LongType.INSTANCE)
				.addScalar("name").addScalar("boundaryNum", LongType.INSTANCE)
				.addScalar("boundaryType.id", LongType.INSTANCE).addScalar("boundaryType.name")
				.addScalar("parent.id", LongType.INSTANCE).addScalar("tenantId");

		createSQLQuery.setLong("id", id);
		createSQLQuery.setString("tenantId", tenantId);
		return mapToBoundary(createSQLQuery.list());
	}

	private List<Boundary> mapToBoundary(List<Object[]> boundarylist) {
		List<Boundary> boundaryList = new ArrayList<Boundary>();
		for (Object[] b : boundarylist) {
			Boundary boundary = new Boundary();
			boundary.setId(b[0] != null ? Long.valueOf(b[0].toString()) : null);
			boundary.setName(b[1] != null ? b[1].toString() : "");
			boundary.setBoundaryNum(b[2] != null ? Long.valueOf(b[2].toString()) : null);
			boundary.setBoundaryType(new BoundaryType());
			boundary.getBoundaryType().setId(b[3] != null ? Long.valueOf(b[3].toString()) : null);
			boundary.getBoundaryType().setName(b[4] != null ? b[4].toString() : "");
			boundary.setParent(new Boundary());
			boundary.getParent().setId(b[5] != null ? Long.valueOf(b[5].toString()) : null);
			boundary.setTenantId(b[6] != null ? b[6].toString() : "");
			boundaryList.add(boundary);
		}
		return boundaryList;
	}
}