package org.egov.boundary.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.egov.boundary.domain.model.Boundary;
import org.egov.boundary.web.contract.BoundaryType;
import org.springframework.stereotype.Service;

@Service
public class BoundaryUtilities {


	public List<Boundary> prepareListOfBoundaries(Boundary boundary) {
		List<Boundary> boundaryList = new ArrayList<>();
		if (boundary.getChildren() != null) {
			for (Boundary bndry : boundary.getChildren()) {
				boundaryList.add(bndry);
			}
		}
		List<Boundary> boundaryList1 = new ArrayList<>();
		if (boundaryList != null && !boundaryList.isEmpty()) {
			for (Boundary bndry : boundaryList) {
				if (bndry.getChildren() != null) {
					for (Boundary bndry1 : bndry.getChildren()) {
						boundaryList1.add(bndry1);
					}
				}
			}
		}
		if (boundaryList1 != null && !boundaryList1.isEmpty()) {
			for (Boundary boundary12 : boundaryList1) {
				boundaryList.add(boundary12);
			}
		}
		List<Boundary> boundaryList2 = new ArrayList<>();
		if (boundaryList1 != null && !boundaryList1.isEmpty()) {
			for (Boundary bndry : boundaryList1) {
				if (bndry.getChildren() != null) {
					for (Boundary bndry1 : bndry.getChildren()) {
						boundaryList2.add(bndry1);
					}
				}
			}
		}
		if (boundaryList2 != null && !boundaryList2.isEmpty()) {
			for (Boundary boundary12 : boundaryList2) {
				boundaryList.add(boundary12);
			}
		}
		return boundaryList;
	}
	
	public List<Boundary> addParentAndBoundaryType(List<Boundary> boundaryList, String tenantId) {
		Set<Boundary> set = new HashSet<Boundary>();
		if (boundaryList != null && !boundaryList.isEmpty()) {
			for (Boundary parent : boundaryList) {
				BoundaryType boundaryType1 = new BoundaryType();
				boundaryType1.setName(parent.getLabel());
				boundaryType1.setId(parent.getLabelid().toString());
				boundaryType1.setTenantId(tenantId);
				parent.setBoundaryType(boundaryType1);
				parent.setTenantId(tenantId);
				set.add(parent);
				Set<Boundary> childList = parent.getChildren();
				if (childList != null && !childList.isEmpty()) {
					for (Boundary boundary : childList) {
						boundary.setParent(parent);
						BoundaryType boundaryType = new BoundaryType();
						boundaryType.setName(boundary.getLabel());
						boundaryType.setId(boundary.getLabelid().toString());
						boundary.setBoundaryType(boundaryType);
						boundaryType.setTenantId(tenantId);
						set.add(boundary);
					}
				}
			}
		}
		List<Boundary> list1 = new ArrayList<>(set);

		return list1;
	}
	
}
