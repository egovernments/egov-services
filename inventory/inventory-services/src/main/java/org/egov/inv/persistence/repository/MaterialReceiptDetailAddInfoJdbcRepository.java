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
import org.egov.inv.model.MaterialReceiptAddInfoSearch;
import org.egov.inv.model.MaterialReceiptDetailAddnlinfo;
import org.egov.inv.model.MaterialReceiptDetailSearch;
import org.egov.inv.persistence.entity.MaterialDetailAddInfoEntity;
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
public class MaterialReceiptDetailAddInfoJdbcRepository extends JdbcRepository {

    private static final Logger LOG = LoggerFactory.getLogger(MaterialReceiptDetailAddnlinfo.class);

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    static {
        init(MaterialReceiptDetailAddnlinfo.class);
    }

    public Pagination<MaterialReceiptDetailAddnlinfo> search(MaterialReceiptAddInfoSearch materialReceiptAddInfoSearch) {

        String searchQuery = "select * from materialreceiptdetailaddnlinfo" +
                " :condition :orderby";
        StringBuffer params = new StringBuffer();
        Map<String, Object> paramValues = new HashMap<>();

        if (materialReceiptAddInfoSearch.getSortBy() != null && !materialReceiptAddInfoSearch.getSortBy().isEmpty()) {
            validateSortByOrder(materialReceiptAddInfoSearch.getSortBy());
            validateEntityFieldName(materialReceiptAddInfoSearch.getSortBy(), MaterialReceiptDetailSearch.class);
        }

        String orderBy = "order by receiptdetailid";

        if (materialReceiptAddInfoSearch.getSortBy() != null && !materialReceiptAddInfoSearch.getSortBy().isEmpty()) {
            orderBy = "order by " + materialReceiptAddInfoSearch.getSortBy();
        }

        if (materialReceiptAddInfoSearch.getId() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("id in (:ids)");
            paramValues.put("ids", materialReceiptAddInfoSearch.getId());
        }

        if (materialReceiptAddInfoSearch.getLotNo() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("lotno in (:lotNo)");
            paramValues.put("lotNo", materialReceiptAddInfoSearch.getLotNo());
        }

        if (materialReceiptAddInfoSearch.getSerialNo() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("serialno = :serialNo");
            paramValues.put("serialNo", materialReceiptAddInfoSearch.getSerialNo());
        }

        if (materialReceiptAddInfoSearch.getManufactureDate() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("manufacturedate = :manufactureDate");
            paramValues.put("manufactureDate", materialReceiptAddInfoSearch.getManufactureDate());
        }

        if (materialReceiptAddInfoSearch.getOldReceiptNumber() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("oldreceiptnumber = :oldReceiptNumber");
            paramValues.put("oldReceiptNumber", materialReceiptAddInfoSearch.getOldReceiptNumber());
        }

        if (materialReceiptAddInfoSearch.getExpiryDate() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("expirydate = :expiryDate");
            paramValues.put("expiryDate", materialReceiptAddInfoSearch.getExpiryDate());
        }

        if (materialReceiptAddInfoSearch.getReceiptDetailId() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("receiptdetailid = :receiptDetailId");
            paramValues.put("receiptDetailId", materialReceiptAddInfoSearch.getReceiptDetailId());
        }

        if (materialReceiptAddInfoSearch.getTenantId() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("tenantId = :tenantId");
            paramValues.put("tenantId", materialReceiptAddInfoSearch.getTenantId());
        }

        Pagination<MaterialReceiptDetailAddnlinfo> page = new Pagination<>();
        if (materialReceiptAddInfoSearch.getPageSize() != null)
            page.setPageSize(materialReceiptAddInfoSearch.getPageSize());
        if (materialReceiptAddInfoSearch.getOffset() != null)
            page.setOffset(materialReceiptAddInfoSearch.getOffset());
        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where deleted is not true and " + params.toString());
        else
            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);
        page = (Pagination<MaterialReceiptDetailAddnlinfo>) getPagination(searchQuery, page, paramValues);

        searchQuery = searchQuery + " :pagination";
        searchQuery = searchQuery.replace(":pagination", "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());
        BeanPropertyRowMapper row = new BeanPropertyRowMapper(MaterialDetailAddInfoEntity.class);

        List<MaterialReceiptDetailAddnlinfo> detailAddnlinfos = new ArrayList<>();

        List<MaterialDetailAddInfoEntity> addlnInfoEntities = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        for (MaterialDetailAddInfoEntity addlnInfoEntity : addlnInfoEntities) {

            detailAddnlinfos.add(addlnInfoEntity.toDomain());
        }

        page.setTotalResults(detailAddnlinfos.size());

        page.setPagedData(detailAddnlinfos);

        return page;
    }


}
