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

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;

import org.egov.boundary.persistence.entity.Boundary;
import org.egov.boundary.persistence.entity.BoundaryType;
import org.egov.boundary.persistence.entity.HierarchyType;
import org.egov.boundary.persistence.repository.BoundaryJpaRepository;
import org.egov.boundary.persistence.repository.BoundaryRepository;
import org.egov.boundary.web.contract.BoundaryRequest;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

@Service
@Transactional(readOnly = true)
public class BoundaryService {

	private static final Logger LOG = LoggerFactory.getLogger(BoundaryService.class);

	private final BoundaryJpaRepository boundaryJpaRepository;

	private CrossHierarchyService crossHierarchyService;

	private BoundaryTypeService boundaryTypeService;

	private EntityManager entityManager;

	private BoundaryRepository boundaryRepository;

	@Autowired
	public BoundaryService(final BoundaryJpaRepository boundaryJpaRepository, EntityManager entityManager,
			BoundaryTypeService boundaryTypeService, CrossHierarchyService crossHierarchyService,
			BoundaryRepository boundaryRepository) {
		this.boundaryJpaRepository = boundaryJpaRepository;
		this.entityManager = entityManager;
		this.boundaryTypeService = boundaryTypeService;
		this.crossHierarchyService = crossHierarchyService;
		this.boundaryRepository = boundaryRepository;
	}

	@Transactional
	public Boundary createBoundary(final Boundary boundary) {
		boundary.setHistory(false);
		boundary.setMaterializedPath(getMaterializedPath(null, boundary.getParent()));
		if (boundary.getTenantId() != null && !boundary.getTenantId().isEmpty() && boundary.getBoundaryType() != null
				&& boundary.getBoundaryType().getId() != null) {
			boundary.setBoundaryType(boundaryTypeService.findByIdAndTenantId(boundary.getBoundaryType().getId(),
					boundary.getTenantId()));
		}
		return boundaryJpaRepository.save(boundary);
	}

	@Transactional
	public Boundary updateBoundary(final Boundary boundary) {
		boundary.setHistory(false);
		boundary.setMaterializedPath(getMaterializedPath(boundary, boundary.getParent()));
		return boundaryJpaRepository.save(boundary);
	}

	public Boundary getBoundaryById(final Long id) {
		return boundaryJpaRepository.findOne(id);
	}

	public List<Boundary> getAllBoundariesOrderByBoundaryNumAsc(BoundaryType boundaryType) {
		return boundaryJpaRepository.findByBoundaryTypeOrderByBoundaryNumAsc(boundaryType);
	}

	public List<Boundary> getAllBoundariesByBoundaryTypeIdAndTenantId(final Long boundaryTypeId,
			final String tenantId) {

		return boundaryRepository.getAllBoundariesByBoundaryTypeIdAndTenantId(boundaryTypeId, tenantId);
	}

	public List<Boundary> getPageOfBoundaries(final Long boundaryTypeId, final String tenantId) {

		return boundaryJpaRepository.findBoundariesByBoundaryType_IdAndBoundaryType_TenantIdAndTenantId(boundaryTypeId,
				tenantId, tenantId);
	}

	public Boundary getBoundaryByTypeAndNo(final BoundaryType boundaryType, final Long boundaryNum) {
		return boundaryJpaRepository.findBoundarieByBoundaryTypeAndBoundaryNum(boundaryType, boundaryNum);
	}

	public List<Boundary> getParentBoundariesByBoundaryId(final Long boundaryId) {
		List<Boundary> boundaryList = new ArrayList<>();
		final Boundary bndry = getBoundaryById(boundaryId);
		if (bndry != null) {
			boundaryList.add(bndry);
			if (bndry.getParent() != null)
				boundaryList = getParentBoundariesByBoundaryId(bndry.getParent().getId());
		}
		return boundaryList;
	}

	public List<Boundary> getActiveBoundariesByBoundaryTypeId(final Long boundaryTypeId) {
		return boundaryJpaRepository.findActiveBoundariesByBoundaryTypeId(boundaryTypeId);
	}

	public List<Boundary> getTopLevelBoundaryByHierarchyType(final HierarchyType hierarchyType) {
		return boundaryJpaRepository.findActiveBoundariesByHierarchyTypeAndLevelAndAsOnDate(hierarchyType, 1l,
				new Date());
	}

	public List<Boundary> getActiveChildBoundariesByBoundaryId(final Long boundaryId) {
		return boundaryJpaRepository.findActiveChildBoundariesByBoundaryIdAndAsOnDate(boundaryId, new Date());
	}

	public List<Boundary> getChildBoundariesByBoundaryId(final Long boundaryId) {
		return boundaryJpaRepository.findChildBoundariesByBoundaryIdAndAsOnDate(boundaryId, new Date());
	}

	public Boundary getActiveBoundaryByBndryNumAndTypeAndHierarchyTypeCode(final Long bndryNum,
			final String boundaryType, final String hierarchyTypeCode) {
		return boundaryJpaRepository.findActiveBoundaryByBndryNumAndTypeAndHierarchyTypeCodeAndAsOnDate(bndryNum,
				boundaryType, hierarchyTypeCode, new Date());
	}

	public List<Boundary> getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(final String boundaryTypeName,
			final String hierarchyTypeName) {
		return boundaryJpaRepository.findActiveBoundariesByBndryTypeNameAndHierarchyTypeName(boundaryTypeName,
				hierarchyTypeName);
	}

	public List<Boundary> getBoundariesByBndryTypeNameAndHierarchyTypeNameAndTenantId(final String boundaryTypeName,
			final String hierarchyTypeName, final String tenantId) {

		return boundaryRepository.getBoundariesByBndryTypeNameAndHierarchyTypeNameAndTenantId(boundaryTypeName,
				hierarchyTypeName, tenantId);
	}

	public Boundary getBoundaryByBndryTypeNameAndHierarchyTypeName(final String boundaryTypeName,
			final String hierarchyTypeName) {
		return boundaryJpaRepository.findBoundaryByBndryTypeNameAndHierarchyTypeName(boundaryTypeName,
				hierarchyTypeName);
	}

	public List<Boundary> getBondariesByNameAndTypeOrderByBoundaryNumAsc(final String boundaryName,
			final Long boundaryTypeId) {
		return boundaryJpaRepository.findByNameAndBoundaryTypeOrderByBoundaryNumAsc(boundaryName, boundaryTypeId);
	}

	public Boolean validateBoundary(final BoundaryType boundaryType) {
		return Optional.ofNullable(boundaryJpaRepository.findByBoundaryTypeNameAndHierarchyTypeNameAndLevel(
				boundaryType.getName(), boundaryType.getHierarchyType().getName(), 1L)).isPresent();
	}

	public List<Boundary> getBondariesByNameAndBndryTypeAndHierarchyType(final String boundaryTypeName,
			final String hierarchyTypeName, final String name) {
		return boundaryJpaRepository.findActiveBoundariesByNameAndBndryTypeNameAndHierarchyTypeName(boundaryTypeName,
				hierarchyTypeName, name);
	}

	public List<Map<String, Object>> getBoundaryDataByTenantIdAndNameLike(final String tenantId, final String name) {
		final List<Map<String, Object>> list = new ArrayList<>();

		crossHierarchyService.getChildBoundaryNameAndBndryTypeAndHierarchyTypeAndTenantId("Locality", "Location",
				"Administration", '%' + name + '%', tenantId).stream().forEach(location -> {
					final Map<String, Object> res = new HashMap<>();
					res.put("id", location.getId());
					res.put("name", location.getChild().getName() + " - " + location.getParent().getName());
					list.add(res);
				});
		return list;
	}

	public List<Boundary> findActiveChildrenWithParent(final Long parentBoundaryId) {
		return boundaryJpaRepository.findActiveChildrenWithParent(parentBoundaryId);
	}

	public List<Boundary> findActiveBoundariesForMpath(final Set<String> mpath) {
		return boundaryJpaRepository.findActiveBoundariesForMpath(mpath);
	}

	public String getMaterializedPath(final Boundary child, final Boundary parent) {
		String mpath = "";
		int childSize = 0;
		if (null == parent)
			mpath = String.valueOf(boundaryJpaRepository.findAllParents().size() + 1);
		else
			childSize = boundaryJpaRepository.findActiveImmediateChildrenWithOutParent(parent.getId()).size();
		if (mpath.isEmpty())
			if (null != child) {
				if (parent != null && !child.getMaterializedPath()
						.equalsIgnoreCase(parent.getMaterializedPath() + "." + childSize)) {
					childSize += 1;
					mpath = parent.getMaterializedPath() + "." + childSize;
				} else
					mpath = child.getMaterializedPath();
			} else if (parent != null) {
				childSize += 1;
				mpath = parent.getMaterializedPath() + "." + childSize;
			}

		return mpath;
	}

	public Long getBoundaryIdFromShapefile(final Double latitude, final Double longitude, String tenantId) {
		Optional<Boundary> boundary = getBoundary(latitude, longitude, tenantId);
		return boundary.isPresent() ? boundary.get().getId() : 0;
	}

	public Optional<Boundary> getBoundary(final Double latitude, final Double longitude, String tenantId) {
		try {
			if (latitude != null && longitude != null) {
				final Map<String, URL> map = new HashMap<>();
				map.put("url", new ClassPathResource("/gis/" + tenantId.replace(".", "/") + "/wards.shp").getURL());
				final DataStore dataStore = DataStoreFinder.getDataStore(map);
				final FeatureCollection<SimpleFeatureType, SimpleFeature> collection = dataStore
						.getFeatureSource(dataStore.getTypeNames()[0]).getFeatures();
				final Iterator<SimpleFeature> iterator = collection.iterator();
				final Point point = JTSFactoryFinder.getGeometryFactory(null)
						.createPoint(new Coordinate(longitude, latitude));
				LOG.debug("Fetching boundary data for coordinates lng {}, lat {}", longitude, latitude);
				try {
					while (iterator.hasNext()) {
						final SimpleFeature feature = iterator.next();
						final Geometry geom = (Geometry) feature.getDefaultGeometry();
						if (geom.contains(point)) {
							LOG.debug("Found coordinates in shape file");
							return getBoundaryByNumberAndType((Long) feature.getAttribute("bndrynum"),
									(String) feature.getAttribute("bndrytype"), tenantId);
						}
					}
				} finally {
					collection.close(iterator);
				}
			}

			return Optional.empty();
		} catch (final Exception e) {
			throw new RuntimeException("Error occurred while fetching boundary from GIS data", e);
		}
	}

	public Optional<Boundary> getBoundaryByNumberAndType(Long boundaryNum, String boundaryTypeName, String tenantId) {
		if (boundaryNum != null && !StringUtils.isEmpty(boundaryTypeName)) {
			final BoundaryType boundaryType = boundaryTypeService
					.getBoundaryTypeByNameAndHierarchyTypeName(boundaryTypeName, "ADMINISTRATION", tenantId);
			final Boundary boundary = this.getBoundaryByTypeAndNo(boundaryType, boundaryNum);
			if (boundary == null) {
				final BoundaryType cityBoundaryType = boundaryTypeService
						.getBoundaryTypeByNameAndHierarchyTypeName("City", "ADMINISTRATION", tenantId);
				return Optional.ofNullable(
						this.getAllBoundariesByBoundaryTypeIdAndTenantId(cityBoundaryType.getId(), tenantId).get(0));
			}
			return Optional.of(boundary);
		}

		return Optional.empty();
	}

	public Boundary findByTenantIdAndCode(String tenantId, String code) {
		return boundaryJpaRepository.findByTenantIdAndBoundaryNum(tenantId, code);
	}

	public List<Boundary> getBoundariesByIdAndTenantId(Long id, String tenantId) {
		return boundaryRepository.getBoundariesByIdAndTenantId(id, tenantId);
	}

	public List<Boundary> getAllBoundary(BoundaryRequest boundaryRequest) {
		List<Boundary> boundaries = new ArrayList<Boundary>();
		if (boundaryRequest.getBoundary().getTenantId() != null
				&& !boundaryRequest.getBoundary().getTenantId().isEmpty()) {
			if (boundaryRequest.getBoundary().getId() != null) {
				boundaries.addAll(getBoundariesByIdAndTenantId(boundaryRequest.getBoundary().getId(),
						boundaryRequest.getBoundary().getTenantId()));
			} else {
				if (!StringUtils.isEmpty(boundaryRequest.getBoundary().getLatitude())
						&& !StringUtils.isEmpty(boundaryRequest.getBoundary().getLongitude())) {
					Optional<Boundary> boundary = getBoundary(boundaryRequest.getBoundary().getLatitude().doubleValue(),
							boundaryRequest.getBoundary().getLongitude().doubleValue(),
							boundaryRequest.getBoundary().getTenantId());
					if (boundary.isPresent())
						boundaries.add(boundary.get());
					else
						boundaries = new ArrayList<Boundary>();
				} else {
					boundaries.addAll(
							boundaryJpaRepository.findAllByTenantId(boundaryRequest.getBoundary().getTenantId()));
				}
			}
		}
		return boundaries;
	}

}