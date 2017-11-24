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

import org.egov.common.JdbcRepository;
import org.egov.common.Pagination;
import org.egov.inv.model.MaterialReceiptDetail;
import org.egov.inv.model.MaterialReceiptDetailSearch;
import org.egov.inv.persistence.entity.MaterialReceiptDetailEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MaterialReceiptDetailJdbcRepository extends JdbcRepository {

    private static final Logger LOG = LoggerFactory.getLogger(MaterialReceiptDetail.class);

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    static {
        init(MaterialReceiptDetailEntity.class);
    }

    public Pagination<MaterialReceiptDetail> search(MaterialReceiptDetailSearch materialReceiptDetailSearch) {
        String searchQuery = "select * from materialreceiptdetail" +
                " :condition :orderby";
        StringBuffer params = new StringBuffer();
        Map<String, Object> paramValues = new HashMap<>();

        if (materialReceiptDetailSearch.getSortBy() != null && !materialReceiptDetailSearch.getSortBy().isEmpty()) {
            validateSortByOrder(materialReceiptDetailSearch.getSortBy());
            validateEntityFieldName(materialReceiptDetailSearch.getSortBy(), MaterialReceiptDetailSearch.class);
        }

        String orderBy = "order by mrnnumber";

        if (materialReceiptDetailSearch.getSortBy() != null && !materialReceiptDetailSearch.getSortBy().isEmpty()) {
            orderBy = "order by " + materialReceiptDetailSearch.getSortBy();
        }

        if (materialReceiptDetailSearch.getIds() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("id in (:ids)");
            paramValues.put("ids", materialReceiptDetailSearch.getIds());
        }

        if (materialReceiptDetailSearch.getMrnNumber() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("mrnnumber in (:mrnNumber)");
            paramValues.put("mrnNumber", materialReceiptDetailSearch.getMrnNumber());
        }

        if (materialReceiptDetailSearch.getUomNo() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("uomno = :uomNo");
            paramValues.put("uomNo", materialReceiptDetailSearch.getUomNo());
        }

        if (materialReceiptDetailSearch.getMaterial() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("material = :material");
            paramValues.put("material", materialReceiptDetailSearch.getMaterial());
        }

        if (materialReceiptDetailSearch.getOrderNumber() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("ordernumber = :ordernumber");
            paramValues.put("ordernumber", materialReceiptDetailSearch.getOrderNumber());
        }

        if (materialReceiptDetailSearch.getPoDetailId() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("podetailid = :poDetailId");
            paramValues.put("poDetailId", materialReceiptDetailSearch.getPoDetailId());
        }

        if (materialReceiptDetailSearch.getReceivedQuantity() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("receivedqty = :receivedOty");
            paramValues.put("receivedQty", materialReceiptDetailSearch.getReceivedQuantity());
        }

        if (materialReceiptDetailSearch.getAcceptedQuantity() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("acceptedqty = :acceptedQty");
            paramValues.put("acceptedQty", materialReceiptDetailSearch.getAcceptedQuantity());
        }

        if (materialReceiptDetailSearch.getUnitRate() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("unitrate = :unitRate");
            paramValues.put("unitRate", materialReceiptDetailSearch.getUnitRate());
        }

        if (materialReceiptDetailSearch.getAsset() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("asset = :asset");
            paramValues.put("asset", materialReceiptDetailSearch.getAsset());
        }

        if (materialReceiptDetailSearch.getVoucherHeader() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("voucherheader = :voucherHeader");
            paramValues.put("voucherHeader", materialReceiptDetailSearch.getVoucherHeader());
        }

        if (materialReceiptDetailSearch.getScrapItem() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("isscrapitem = :isScrapItem");
            paramValues.put("isScrapItem", materialReceiptDetailSearch.getScrapItem());
        }

        if (materialReceiptDetailSearch.getBatchNo() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("batchno = :batchNo");
            paramValues.put("batchNo", materialReceiptDetailSearch.getBatchNo());
        }

        if (materialReceiptDetailSearch.getTenantId() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("tenantId = :tenantId");
            paramValues.put("tenantId", materialReceiptDetailSearch.getTenantId());
        }

        Pagination<MaterialReceiptDetail> page = new Pagination<>();
        if (materialReceiptDetailSearch.getPageSize() != null)
            page.setPageSize(materialReceiptDetailSearch.getPageSize());
        if (materialReceiptDetailSearch.getOffset() != null)
            page.setOffset(materialReceiptDetailSearch.getOffset());
        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where deleted is not true and " + params.toString());
        else
            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);
        page = (Pagination<MaterialReceiptDetail>) getPagination(searchQuery, page, paramValues);

        searchQuery = searchQuery + " :pagination";
        searchQuery = searchQuery.replace(":pagination", "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());
        BeanPropertyRowMapper row = new BeanPropertyRowMapper(MaterialReceiptDetailEntity.class);

        List<MaterialReceiptDetail> materialReceipts = new ArrayList<>();

        List<MaterialReceiptDetailEntity> materialReceiptEntities = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        for (MaterialReceiptDetailEntity materialReceiptEntity : materialReceiptEntities) {

            materialReceipts.add(materialReceiptEntity.toDomain());
        }

        page.setTotalResults(materialReceipts.size());

        page.setPagedData(materialReceipts);

        return page;
    }


}
