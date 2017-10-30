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
package org.egov.inv.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.swagger.model.Store;
import io.swagger.model.StoreGetRequest;
import io.swagger.model.Pagination;

import org.egov.inv.persistence.entity.StoresEntity;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class StoreJdbcRepository {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Pagination<Store> search(StoreGetRequest storeGetRequest) {
        String searchQuery = "select * from stores :condition :orderby";
        StringBuffer params = new StringBuffer();
        Map<String, Object> paramValues = new HashMap<>();
        if (storeGetRequest.getSortBy() != null && !storeGetRequest.getSortBy().isEmpty()) {
            validateSortByOrder(storeGetRequest.getSortBy());
            validateEntityFieldName(storeGetRequest.getSortBy(), StoreGetRequest.class);
        }
        String orderBy = "order by code";
        if (storeGetRequest.getSortBy() != null && !storeGetRequest.getSortBy().isEmpty()) {
            orderBy = "order by " + storeGetRequest.getSortBy();
        }
        if (storeGetRequest.getIds() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("id in (:ids)");
            paramValues.put("ids", storeGetRequest.getIds());
        }
        if (storeGetRequest.getCode() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("code = :code");
            paramValues.put("code", storeGetRequest.getCode());
        }
        if (storeGetRequest.getName() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("name = :name");
            paramValues.put("name", storeGetRequest.getName());
        }
        if (storeGetRequest.getDescription() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("description = :description");
            paramValues.put("description", storeGetRequest.getDescription());
        }
        if (storeGetRequest.getDepartment() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("department = :department");
            paramValues.put("department", storeGetRequest.getDepartment());
        }
        if(storeGetRequest.getBillingAddress() != null){
        	if(params.length() > 0)
        		params.append(" and ");
        	params.append("billingaddress = :billingaddress");
            paramValues.put("billingaddress", storeGetRequest.getBillingAddress());	
        }
        
        if(storeGetRequest.getDeliveryAddress() != null){
        	if(params.length() > 0)
        		params.append(" and ");
        	params.append("deliveryaddress = :deliveryaddress");
            paramValues.put("deliveryaddress", storeGetRequest.getDeliveryAddress());	
        }
        if (storeGetRequest.getContactNo1() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("contactno1 = :contactno1");
            paramValues.put("contactno1", storeGetRequest.getContactNo1());
        }
        
        if (storeGetRequest.getContactNo2() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("contactno2 = :contactno2");
            paramValues.put("contactno2", storeGetRequest.getContactNo2());
        }
        if (storeGetRequest.getEmail() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("email = :email");
            paramValues.put("email", storeGetRequest.getEmail());
        }
        if (storeGetRequest.getActive() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("active = :active");
            paramValues.put("active", storeGetRequest.getActive());

        }
        if (storeGetRequest.getIsCentralStore() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("iscentralstore = :iscentralstore");
            paramValues.put("iscentralstore", storeGetRequest.getIsCentralStore());
        }
        if (storeGetRequest.getStoreInCharge() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("storeincharge = :storeincharge");
            paramValues.put("storeincharge", storeGetRequest.getStoreInCharge());
        }
        if (storeGetRequest.getTenantId() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("tenantId = :tenantId");
            paramValues.put("tenantId", storeGetRequest.getTenantId());
        }
        Pagination<Store> page = new Pagination<>();
        if (storeGetRequest.getPageSize() != null)
            page.setPageSize(storeGetRequest.getPageSize());
        if (storeGetRequest.getOffset() != null)
            page.setOffset(storeGetRequest.getOffset());
        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else
            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);
        page = (Pagination<Store>) getPagination(searchQuery, page, paramValues);

        searchQuery = searchQuery + " :pagination";
        searchQuery = searchQuery.replace(":pagination", "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());
        BeanPropertyRowMapper row = new BeanPropertyRowMapper(StoresEntity.class);

        List<Store> storesList = new ArrayList<>();

        List<StoresEntity> storesEntities = namedParameterJdbcTemplate
                        .query(searchQuery.toString(), paramValues, row);

        for (StoresEntity storesEntity : storesEntities) {

            storesList.add(storesEntity.toDomain());
        }

        page.setTotalResults(storesList.size());

        page.setPagedData(storesList);

        return page;
    }

    public Pagination<?> getPagination(String searchQuery, Pagination<?> page, Map<String, Object> paramValues) {
        String countQuery = "select count(*) from (" + searchQuery + ") as x";
        Long count = namedParameterJdbcTemplate.queryForObject(countQuery.toString(), paramValues, Long.class);
        Integer totalpages = (int) Math.ceil((double) count / page.getPageSize());
        page.setTotalPages(totalpages);
        page.setCurrentPage(page.getOffset());
        return page;
    }

    public void validateSortByOrder(final String sortBy) {
        List<String> sortByList = new ArrayList<String>();
        if (sortBy.contains(",")) {
            sortByList = Arrays.asList(sortBy.split(","));
        } else {
            sortByList = Arrays.asList(sortBy);
        }
        for (String s : sortByList) {
            if (s.contains(" ")
                    && (!s.toLowerCase().trim().endsWith("asc") && !s.toLowerCase().trim().endsWith("desc"))) {

                throw new CustomException(s.split(" ")[0],
                        "Please send the proper sortBy order for the field " + s.split(" ")[0]);
            }
        }

    }

    public void validateEntityFieldName(String sortBy, final Class<?> object) {
        List<String> sortByList = new ArrayList<String>();
        if (sortBy.contains(",")) {
            sortByList = Arrays.asList(sortBy.split(","));
        } else {
            sortByList = Arrays.asList(sortBy);
        }
        Boolean isFieldExist = Boolean.FALSE;
        for (String s : sortByList) {
            for (int i = 0; i < object.getDeclaredFields().length; i++) {
                if (object.getDeclaredFields()[i].getName().equals(s.contains(" ") ? s.split(" ")[0] : s)) {
                    isFieldExist = Boolean.TRUE;
                    break;
                } else {
                    isFieldExist = Boolean.FALSE;
                }
            }
            if (!isFieldExist) {
                throw new CustomException(s.contains(" ") ? s.split(" ")[0] : s, "Please send the proper Field Names ");

            }
        }

    }

   /* public String checkCodeExistsQuery() {
       return "select id from stores where code = :code and tenantid = :tenantid";        
    }*/

}
