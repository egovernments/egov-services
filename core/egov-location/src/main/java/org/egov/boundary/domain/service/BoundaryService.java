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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.egov.boundary.domain.model.Boundary;
import org.egov.boundary.domain.model.BoundarySearchRequest;
import org.egov.boundary.exception.CustomException;
import org.egov.boundary.persistence.repository.BoundaryRepository;
import org.egov.boundary.persistence.repository.MdmsRepository;
import org.egov.boundary.util.BoundaryConstants;
import org.egov.boundary.util.BoundaryUtilities;
import org.egov.boundary.web.contract.BoundaryRequest;
import org.egov.boundary.web.contract.BoundaryType;
import org.egov.boundary.web.contract.CrossHierarchy;
import org.egov.boundary.web.contract.MdmsBoundary;
import org.egov.boundary.web.contract.MdmsTenantBoundary;
import org.egov.boundary.web.contract.TenantBoundary;
import org.egov.boundary.web.contract.TenantBoundarySearch;
import org.egov.common.contract.request.RequestInfo;
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
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

import net.minidev.json.JSONArray;

@Service
public class BoundaryService {

	private static final Logger LOG = LoggerFactory.getLogger(BoundaryService.class);

	private CrossHierarchyService crossHierarchyService;
	private BoundaryTypeService boundaryTypeService;
	private BoundaryRepository boundaryRepository;
	private MdmsRepository mdmsRepository;
	private BoundaryUtilities boundaryUtilities;

	@Autowired
	public BoundaryService(BoundaryTypeService boundaryTypeService, CrossHierarchyService crossHierarchyService,
			BoundaryRepository boundaryRepository, MdmsRepository mdmsRepository, BoundaryUtilities boundaryUtilities) {
		this.boundaryTypeService = boundaryTypeService;
		this.crossHierarchyService = crossHierarchyService;
		this.boundaryRepository = boundaryRepository;
		this.mdmsRepository = mdmsRepository;
		this.boundaryUtilities = boundaryUtilities;
	}

	public Boundary findByTenantIdAndId(Long id, String tenantId) {
		return boundaryRepository.findByTenantIdAndId(tenantId, id);
	}

	public Boundary findByTenantIdAndCode(String tenantId, String code) {
		// return boundaryRepository.findByTenantIdAndCode(tenantId, code);
		List<Boundary> boundaryList = findAllByTenantIdAndHierarchyType(tenantId, null);
		boundaryList = boundaryList.parallelStream().filter(p -> code.equals(p.getCode())).collect(Collectors.toList());
		return boundaryList.get(0);
	}

	public List<Boundary> findByTenantIdAndCodes(String tenantId, List<String> codes) {
		// return boundaryRepository.findByTenantIdAndCodes(tenantId, codes);
		List<Boundary> boundaryList = findAllByTenantIdAndHierarchyType(tenantId, null);
		boundaryList = boundaryList.parallelStream().filter(p -> codes.contains(p.getCode()))
				.collect(Collectors.toList());
		return boundaryList;
	}

	public Boundary createBoundary(final Boundary boundary) {
		boundary.setHistory(false);
		boundary.setMaterializedPath(getMaterializedPath(null, boundary.getParent()));
		if (boundary.getBoundaryType() != null && boundary.getBoundaryType().getCode() != null)
			boundary.setBoundaryType(boundaryTypeService.findByTenantIdAndCode(boundary.getTenantId(),
					boundary.getBoundaryType().getCode()));
		if (boundary.getParent() != null && boundary.getParent().getCode() != null)
			boundary.setParent(findByTenantIdAndCode(boundary.getTenantId(), boundary.getParent().getCode()));
		Boundary bndry = null;
		try {
			bndry = boundaryRepository.save(boundary);
		} catch (Exception e) {

			LOG.error("Exception while creating Boundary: ", e);
			throw new CustomException(Long.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.toString()),
					BoundaryConstants.BOUNDARY_CREATE_EXCEPTION_MSG, BoundaryConstants.BOUNDARY_CREATE_EXCEPTION_DESC);
		}
		return bndry;
	}

	public Boundary updateBoundary(final Boundary boundary) {
		boundary.setHistory(false);
		boundary.setMaterializedPath(getMaterializedPath(boundary, boundary.getParent()));
		if (boundary.getBoundaryType() != null && boundary.getBoundaryType().getCode() != null)
			boundary.setBoundaryType(boundaryTypeService.findByTenantIdAndCode(boundary.getTenantId(),
					boundary.getBoundaryType().getCode()));
		if (boundary.getParent() != null && boundary.getParent().getCode() != null)
			boundary.setParent(findByTenantIdAndCode(boundary.getTenantId(), boundary.getParent().getCode()));
		Boundary bndry = null;
		try {
			bndry = boundaryRepository.update(boundary);
		} catch (Exception e) {
			LOG.error("Exception while updating Boundary: ", e);
			throw new CustomException(Long.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.toString()),
					BoundaryConstants.BOUNDARY_UPDATE_EXCEPTION_MSG, BoundaryConstants.BOUNDARY_UPDATE_EXCEPTION_DESC);
		}
		return bndry;
	}

	public boolean checkBoundaryExistByTypeAndNumber(Long boundaryNumber, Long boundaryTypeId) {
		List<Boundary> bndryList = boundaryRepository.getBoundaryByTypeAndNumber(boundaryNumber, boundaryTypeId);
		if (bndryList != null && !bndryList.isEmpty()) {
			return true;
		}
		return false;
	}

	public List<Boundary> getAllBoundariesByBoundaryTypeIdAndTenantId(final Long boundaryTypeId,
			final String tenantId) {
		// return
		// boundaryRepository.getAllBoundariesByBoundaryTypeIdAndTenantId(boundaryTypeId,
		// tenantId);
		List<Boundary> boundaryList = findAllByTenantIdAndHierarchyType(tenantId, null);
		boundaryList = boundaryList.parallelStream()
				.filter(p -> boundaryTypeId.toString().equals(p.getBoundaryType().getId()))
				.collect(Collectors.toList());
		return boundaryList;

	}

	public Boundary getBoundaryByTypeAndNo(final Long boundaryTypeId, final Long boundaryNum, final String tenantId) {
		// return
		// boundaryRepository.findBoundarieByBoundaryTypeAndBoundaryNum(boundaryTypeId,
		// boundaryNum, tenantId);
		List<Boundary> boundaryList = findAllByTenantIdAndHierarchyType(tenantId, null);
		boundaryList = boundaryList.parallelStream()
				.filter(p -> boundaryTypeId.toString().equals(p.getBoundaryType().getId()))
				.collect(Collectors.toList());
		boundaryList = boundaryList.parallelStream().filter(p -> boundaryNum == p.getBoundaryNum())
				.collect(Collectors.toList());
		if (!boundaryList.isEmpty())
			return boundaryList.get(0);
		return null;
	}

	public List<Boundary> getBoundariesByBndryTypeNameAndHierarchyTypeNameAndTenantId(final String boundaryTypeName,
			final String hierarchyTypeName, final String tenantId) {
		JSONArray responseJSONArray;
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		responseJSONArray = mdmsRepository.getBoundariesByBndryTypeNameAndHierarchyTypeNameAndTenantId(tenantId,
				hierarchyTypeName, new RequestInfo());
		List<TenantBoundarySearch> boundaryList = null;
		if (responseJSONArray != null && responseJSONArray.size() > 0) {
			boundaryList = mapper.convertValue(responseJSONArray, new TypeReference<List<TenantBoundarySearch>>() {
			});
		}
		List<Boundary> bndryList = boundaryUtilities.prepareListOfBoundaries(boundaryList.get(0).getBoundary());
		bndryList.add(boundaryList.get(0).getBoundary());
		bndryList = bndryList.stream().filter(p -> boundaryTypeName.equals(p.getLabel())).collect(Collectors.toList());
		bndryList.forEach(f -> f.setTenantId(tenantId));
		return bndryList;
	}

/*	public List<Map<String, Object>> getBoundaryDataByTenantIdAndNameLike(final String tenantId, final String name) {
		final List<Map<String, Object>> list = new ArrayList<>();
		crossHierarchyService.getChildBoundaryNameAndBndryTypeAndHierarchyTypeAndTenantId("Locality", "Location",
				"Administration", '%' + name + '%', tenantId).stream().forEach(location -> {
					final Map<String, Object> res = new HashMap<>();
					res.put("id", location.getId());
					res.put("name", location.getChild().getName() + " - " + location.getParent().getName() + " - "
							+ location.getChild().getParent().getName());

					list.add(res);
				});
		return list;
	}*/

	public List<Map<String, Object>> getBoundaryDataByTenantIdAndNameLike(final String tenantId,
			final String name) {

		List<CrossHierarchy> list = crossHierarchyService.getAllCrossHierarchys(tenantId, null);

		List<Boundary> childBoundaryList = findAllByTenantIdAndHierarchyType(tenantId, "Location");
		List<Boundary> parentBoundaryList = findAllByTenantIdAndHierarchyType(tenantId, "Administration");
		childBoundaryList = childBoundaryList.parallelStream()
				.filter(p -> "Locality".equals(p.getBoundaryType().getName())).collect(Collectors.toList());
		childBoundaryList = childBoundaryList.parallelStream().filter(p -> p.getName().contains(name))
				.collect(Collectors.toList());
		List<CrossHierarchy> newList = new ArrayList<CrossHierarchy>();

		for (CrossHierarchy crossHierarchy : list) {
			for (Boundary boundary : childBoundaryList) {
				if (crossHierarchy.getChild() != null && crossHierarchy.getChild().getId().equals(boundary.getId())) {
					newList.add(crossHierarchy);
				}
			}
		}
		List<CrossHierarchy> newList1 = new ArrayList<CrossHierarchy>();
		for (CrossHierarchy crossHierarchy : newList) {
			for (Boundary boundary : parentBoundaryList) {
				if (crossHierarchy.getParent() != null && crossHierarchy.getParent().getId().equals(boundary.getId())) {
					newList1.add(crossHierarchy);
				}
			}
		}
		final List<Map<String, Object>> result = new ArrayList<>();
		newList1.stream().forEach(location -> {
			final Map<String, Object> res = new HashMap<>();
			res.put("id", location.getId());
			res.put("name", location.getChild().getName() + " - " + location.getParent().getName() + " - "
					+ location.getChild().getParent().getName());

			result.add(res);
		});
		return result;
	}

	public String getMaterializedPath(final Boundary child, final Boundary parent) {
		String mpath = "";
		int childSize = 0;
		if (null == parent)
			mpath = String.valueOf(boundaryRepository.findAllParents().size() + 1);
		else
			childSize = boundaryRepository.findActiveImmediateChildrenWithOutParent(parent.getId()).size();
		if (mpath.isEmpty())
			if (null != child) {
				if (child.getMaterializedPath() == null) {
					mpath = parent.getMaterializedPath() + "." + childSize;
				} else if (parent != null && !child.getMaterializedPath()
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
			final Boundary boundary = this.getBoundaryByTypeAndNo(Long.valueOf(boundaryType.getId()), boundaryNum,
					tenantId);
			if (boundary == null) {
				final BoundaryType cityBoundaryType = boundaryTypeService
						.getBoundaryTypeByNameAndHierarchyTypeName("City", "ADMINISTRATION", tenantId);
				return Optional.ofNullable(this
						.getAllBoundariesByBoundaryTypeIdAndTenantId(Long.valueOf(cityBoundaryType.getId()), tenantId)
						.get(0));
			}
			return Optional.of(boundary);
		}
		return Optional.empty();
	}

	public Boundary getBoundariesByIdAndTenantId(Long id, String tenantId) {
		// return boundaryRepository.findByTenantIdAndId(tenantId, id);
		List<Boundary> boundaryList = findAllByTenantIdAndHierarchyType(tenantId, null);
		boundaryList = boundaryList.parallelStream().filter(p -> id.equals(p.getId())).collect(Collectors.toList());
		if (!boundaryList.isEmpty())
			return boundaryList.get(0);
		return null;
	}

	public List<Boundary> getAllBoundary(BoundaryRequest boundaryRequest) {
		List<Boundary> boundaries = new ArrayList<Boundary>();
		if (boundaryRequest.getBoundary().getTenantId() != null
				&& !boundaryRequest.getBoundary().getTenantId().isEmpty()) {
			if (boundaryRequest.getBoundary().getId() != null) {
				boundaries.add(getBoundariesByIdAndTenantId(boundaryRequest.getBoundary().getId(),
						boundaryRequest.getBoundary().getTenantId()));
			} else if (boundaryRequest.getBoundary().getCode() != null) {
				boundaries.add(findByTenantIdAndCode(boundaryRequest.getBoundary().getTenantId(),
						boundaryRequest.getBoundary().getCode()));
			} else if (boundaryRequest.getBoundary().getCodes() != null
					&& !boundaryRequest.getBoundary().getCodes().isEmpty()) {
				boundaries.addAll(findByTenantIdAndCodes(boundaryRequest.getBoundary().getTenantId(),
						new ArrayList<>(Arrays.asList(boundaryRequest.getBoundary().getCodes().split(",")))));
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
							findAllByTenantIdAndHierarchyType(boundaryRequest.getBoundary().getTenantId(), null));
				}
			}
		}
		return boundaries;
	}

	public List<Boundary> findAllByTenantIdAndHierarchyType(String tenantId, String hierarchytype) {
		JSONArray responseJSONArray;
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		responseJSONArray = mdmsRepository.getBoundariesByBndryTypeNameAndHierarchyTypeNameAndTenantId(tenantId,
				hierarchytype, new RequestInfo());
		List<TenantBoundarySearch> boundaryList = null;
		if (responseJSONArray != null && responseJSONArray.size() > 0) {
			boundaryList = mapper.convertValue(responseJSONArray, new TypeReference<List<TenantBoundarySearch>>() {
			});
		}
		List<Boundary> list = new ArrayList<Boundary>();
		if (boundaryList != null) {
			for (TenantBoundarySearch tenantBndry : boundaryList) {
				List<Boundary> list1 = boundaryUtilities.prepareListOfBoundaries(tenantBndry.getBoundary());
				list1.add(tenantBndry.getBoundary());
				for (Boundary bndry : list1) {
					list.add(bndry);
				}
			}
		}
		list = boundaryUtilities.addParentAndBoundaryType(list, tenantId);
		return list;
	}

	// TODO: The internal logic of this API returns whether the shape file
	// exists or not will be based on the resource exists in a directory
	// structure <clientId>/<tenant>/wards.shp.
	// Later we need to rewrite the internal logic of this API to consider the
	// meta-data after uploading the Shape file as a filestore (After
	// implementing the uploading of shape file as file store).
	public Boolean checkTenantShapeFileExistOrNot(String tenantId) {
		String path = tenantId.replace(".", "/");
		ClassPathResource file = new ClassPathResource("/gis/" + path + "/wards.shp");
		if (file.exists()) {
			return true;
		}
		return false;
	}

	public Resource fetchShapeFile(String tenantId) throws IOException {
		String path = tenantId.replace(".", "/");
		Resource resource = new ClassPathResource("/gis/" + path + "/wards.kml");

		if (resource.exists())
			return resource;

		return null;
	}

	public List<Boundary> getAllBoundariesByIdsAndTypeAndNumberAndCodeAndTenant(
			BoundarySearchRequest boundarySearchRequest) {
		// return
		// boundaryRepository.getAllBoundariesByIdsAndTypeAndNumberAndCodeAndTenant(boundarySearchRequest);
		List<Boundary> boundaryList = findAllByTenantIdAndHierarchyType(boundarySearchRequest.getTenantId(),
				boundarySearchRequest.getHierarchyTypeName());
		if (boundarySearchRequest.getBoundaryIds() != null && !boundarySearchRequest.getBoundaryIds().isEmpty()) {
			boundaryList = boundaryList.stream().filter(p -> boundarySearchRequest.getBoundaryIds().contains(p.getId()))
					.collect(Collectors.toList());
		}
		if (boundarySearchRequest.getCodes() != null && !boundarySearchRequest.getCodes().isEmpty()) {
			boundaryList = boundaryList.stream().filter(p -> boundarySearchRequest.getCodes().contains(p.getCode()))
					.collect(Collectors.toList());
		}
		if (boundarySearchRequest.getBoundaryNumbers() != null
				&& !boundarySearchRequest.getBoundaryNumbers().isEmpty()) {
			boundaryList = boundaryList.stream()
					.filter(p -> boundarySearchRequest.getBoundaryNumbers().contains(p.getBoundaryNum()))
					.collect(Collectors.toList());
		}
		if (boundarySearchRequest.getBoundaryTypeIds() != null
				&& !boundarySearchRequest.getBoundaryTypeIds().isEmpty()) {
			boundaryList = boundaryList.stream()
					.filter(p -> boundarySearchRequest.getBoundaryTypeName().equals(p.getBoundaryType().getName()))
					.collect(Collectors.toList());
		}
		return boundaryList;
	}

	public List<MdmsTenantBoundary> getBoundariesByTenantAndHierarchyType(BoundarySearchRequest boundarySearchRequest,
			RequestInfo requestInfo) {

		JSONArray responseJSONArray;
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		responseJSONArray = mdmsRepository.getByCriteria(boundarySearchRequest.getTenantId(),
				boundarySearchRequest.getHierarchyTypeName(), requestInfo);
		List<TenantBoundary> tenantBoundary = null;
		if (responseJSONArray != null && responseJSONArray.size() > 0) {
			tenantBoundary = mapper.convertValue(responseJSONArray, new TypeReference<List<TenantBoundary>>() {
			});
		}
		List<MdmsTenantBoundary> boundaryList = new ArrayList<MdmsTenantBoundary>();

		if (tenantBoundary != null) {
			for (TenantBoundary tenantBndry : tenantBoundary) {
				MdmsTenantBoundary mdmsBoundary = MdmsTenantBoundary.builder()
						.tenantId(boundarySearchRequest.getTenantId()).hierarchyType(tenantBndry.getHierarchyType())
						.build();
				List<MdmsBoundary> list = prepareChildBoundaryList(tenantBndry);
				list.add(tenantBndry.getBoundary());
				if (boundarySearchRequest.getCodes() != null && !boundarySearchRequest.getCodes().isEmpty()) {
					list = list.stream().filter(p -> boundarySearchRequest.getCodes().contains(p.getCode()))
							.collect(Collectors.toList());
				}
				if (boundarySearchRequest.getBoundaryTypeName() != null
						&& !boundarySearchRequest.getBoundaryTypeName().isEmpty()) {
					list = list.stream()
							.filter(p -> boundarySearchRequest.getBoundaryTypeName().equalsIgnoreCase(p.getLabel()))
							.collect(Collectors.toList());
				}
				mdmsBoundary.setBoundary(list);
				boundaryList.add(mdmsBoundary);
			}
		}

		List<MdmsTenantBoundary> list2 = new ArrayList<MdmsTenantBoundary>();
		if (boundarySearchRequest.getHierarchyTypeName() == null
				|| boundarySearchRequest.getHierarchyTypeName().isEmpty()) {
			for (MdmsTenantBoundary mdmsBndry : boundaryList) {
				if (!mdmsBndry.getBoundary().isEmpty()) {
					list2.add(mdmsBndry);
				}
			}
			return list2;
		}
		return boundaryList;
	}

	private List<MdmsBoundary> prepareChildBoundaryList(TenantBoundary tenantBndry) {

		List<MdmsBoundary> boundaryList = new ArrayList<>();
		if (tenantBndry.getBoundary().getChildren() != null) {
			for (MdmsBoundary bndry : tenantBndry.getBoundary().getChildren()) {
				boundaryList.add(bndry);
			}
		}
		List<MdmsBoundary> boundaryList1 = new ArrayList<>();
		if (boundaryList != null && !boundaryList.isEmpty()) {
			for (MdmsBoundary bndry : boundaryList) {
				if (bndry.getChildren() != null) {
					for (MdmsBoundary bndry1 : bndry.getChildren()) {
						boundaryList1.add(bndry1);
					}
				}
			}
		}
		if (boundaryList1 != null && !boundaryList1.isEmpty()) {
			for (MdmsBoundary boundary12 : boundaryList1) {
				boundaryList.add(boundary12);
			}
		}
		List<MdmsBoundary> boundaryList2 = new ArrayList<>();
		if (boundaryList1 != null && !boundaryList1.isEmpty()) {
			for (MdmsBoundary bndry : boundaryList1) {
				if (bndry.getChildren() != null) {
					for (MdmsBoundary bndry1 : bndry.getChildren()) {
						boundaryList2.add(bndry1);
					}
				}
			}
		}
		if (boundaryList2 != null && !boundaryList2.isEmpty()) {
			for (MdmsBoundary boundary12 : boundaryList2) {
				boundaryList.add(boundary12);
			}
		}
		return boundaryList;
	}

	public List<Boundary> getchildBoundariesByIdAndTenant(String boundaryId, String tenantId) {

		List<CrossHierarchy> listOfCrossHierarchy = crossHierarchyService.getAllCrossHierarchys(tenantId, null);
		Set<Long> childBoundaryidList = new HashSet<Long>();
		for (CrossHierarchy crossHierarchy : listOfCrossHierarchy) {
			if (crossHierarchy.getParent() != null && crossHierarchy.getChild()!=null
					&& crossHierarchy.getParent().getId().toString().equals(boundaryId)) {
				childBoundaryidList.add(crossHierarchy.getChild().getId());
			}
		}
		List<Long> childBndryList = new ArrayList<Long>(childBoundaryidList);
		List<Boundary> boundaryList = findAllByTenantIdAndHierarchyType(tenantId, null);
		boundaryList = boundaryList.parallelStream().filter(p -> childBndryList.contains(p.getId()))
				.collect(Collectors.toList());
		return boundaryList;

	}
}