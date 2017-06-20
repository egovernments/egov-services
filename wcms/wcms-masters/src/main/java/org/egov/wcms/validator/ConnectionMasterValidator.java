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
package org.egov.wcms.validator;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.response.ErrorField;
import org.egov.wcms.model.CategoryType;
import org.egov.wcms.model.PipeSizeType;
import org.egov.wcms.model.SourceType;
import org.egov.wcms.model.SupplyType;
import org.egov.wcms.repository.DonationRepository;
import org.egov.wcms.repository.builder.CategoryTypeQueryBuilder;
import org.egov.wcms.repository.builder.PipeSizeTypeQueryBuilder;
import org.egov.wcms.repository.builder.SourceTypeQueryBuilder;
import org.egov.wcms.repository.builder.SupplyTypeQueryBuilder;
import org.egov.wcms.repository.rowmapper.CategoryTypeRowMapper;
import org.egov.wcms.repository.rowmapper.PipeSizeTypeRowMapper;
import org.egov.wcms.repository.rowmapper.SourceTypeRowMapper;
import org.egov.wcms.repository.rowmapper.SupplyTypeRowMapper;
import org.egov.wcms.util.WcmsConstants;
import org.egov.wcms.web.contract.WaterConnectionReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ConnectionMasterValidator {

    @Autowired
    private CategoryTypeRowMapper categoryRowMapper;
    @Autowired
    private PipeSizeTypeRowMapper pipeSizeTypeRowMapper;

    @Autowired
    private SourceTypeRowMapper sourceTypeRowMapper;

    @Autowired
    private SupplyTypeRowMapper supplyTypeRowMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static final Logger LOGGER = LoggerFactory.getLogger(DonationRepository.class);

    public List<ErrorField> getMasterValidation(final WaterConnectionReq waterConnectionRequest) {
        final List<ErrorField> errorFields = new ArrayList<>();

        if (!getCategoryTypeByName(waterConnectionRequest)) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.CATEGORY_INVALID_CODE)
                    .message(WcmsConstants.CATEGORY_INVALID_FIELD_NAME)
                    .field(WcmsConstants.CATEGORY_INVALID_ERROR_MESSAGE)
                    .build();
            errorFields.add(errorField);
        }
        if (!getPipesizeTypeByCode(waterConnectionRequest)) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.PIPESIZE_INVALID_CODE)
                    .message(WcmsConstants.PIPESIZE_INVALID_FIELD_NAME)
                    .field(WcmsConstants.PIPESIZE_INVALID_ERROR_MESSAGE)
                    .build();
            errorFields.add(errorField);
        }
        if (!getSourceTypeByName(waterConnectionRequest)) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.SOURCETYPE_INVALID_CODE)
                    .message(WcmsConstants.SOURCETYPE_INVALID_FIELD_NAME)
                    .field(WcmsConstants.SOURCETYPE_INVALID_ERROR_MESSAGE)
                    .build();
            errorFields.add(errorField);
        }
        if (!getSupplyTypeByName(waterConnectionRequest)) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.SUPPLYTYPE_INVALID_CODE)
                    .message(WcmsConstants.SUPPLYTYPE_INVALID_FIELD_NAME)
                    .field(WcmsConstants.SUPPLYTYPE_INVALID_ERROR_MESSAGE)
                    .build();
            errorFields.add(errorField);
        }
        return errorFields;
    }

    public Boolean getCategoryTypeByName(final WaterConnectionReq waterConnectionRequest) {
        Boolean categoryExist = false;
        final List<Object> preparedStatementValues = new ArrayList<>();
        preparedStatementValues.add(waterConnectionRequest.getConnection().getCategoryType().getName());
        preparedStatementValues.add(waterConnectionRequest.getConnection().getTenantId());
        final String query = CategoryTypeQueryBuilder.selectCategoryByNameQuery();
        final List<CategoryType> categories = jdbcTemplate.query(query, preparedStatementValues.toArray(),
                categoryRowMapper);
        if (!categories.isEmpty()) {
            categoryExist = true;
            waterConnectionRequest.getConnection().setCategoryType(categories.get(0));
        }
        return categoryExist;
    }

    public Boolean getPipesizeTypeByCode(final WaterConnectionReq waterConnectionRequest) {
        Boolean pipeSizeExist = false;
        final List<Object> preparedStatementValues = new ArrayList<>();
        preparedStatementValues.add(waterConnectionRequest.getConnection().getHscPipeSizeType().getCode());
        preparedStatementValues.add(waterConnectionRequest.getConnection().getTenantId());
        final String query = PipeSizeTypeQueryBuilder.selectPipesizeByCodeQuery();
        final List<PipeSizeType> pipeSizes = jdbcTemplate.query(query, preparedStatementValues.toArray(),
                pipeSizeTypeRowMapper);
        if (!pipeSizes.isEmpty()) {
            pipeSizeExist = true;
            waterConnectionRequest.getConnection().setHscPipeSizeType(pipeSizes.get(0));
        }

        return pipeSizeExist;
    }

    public Boolean getSourceTypeByName(final WaterConnectionReq waterConnectionRequest) {
        Boolean sourceTypeExist = false;
        final List<Object> preparedStatementValues = new ArrayList<>();
        preparedStatementValues.add(waterConnectionRequest.getConnection().getSourceType().getName());
        preparedStatementValues.add(waterConnectionRequest.getConnection().getTenantId());
        final String query = SourceTypeQueryBuilder.selectSourceTypeByNameQuery();
        final List<SourceType> sourcetypes = jdbcTemplate.query(query, preparedStatementValues.toArray(),
                sourceTypeRowMapper);
        if (!sourcetypes.isEmpty()) {
            sourceTypeExist = true;
            waterConnectionRequest.getConnection().setSourceType(sourcetypes.get(0));
        }

        return sourceTypeExist;
    }

    public Boolean getSupplyTypeByName(final WaterConnectionReq waterConnectionRequest) {
        Boolean sourceTypeExist = false;
        final List<Object> preparedStatementValues = new ArrayList<>();
        preparedStatementValues.add(waterConnectionRequest.getConnection().getSupplyType().getName());
        preparedStatementValues.add(waterConnectionRequest.getConnection().getTenantId());
        final String query = SupplyTypeQueryBuilder.selectSupplytypeByNameQuery();
        final List<SupplyType> supplyTypes = jdbcTemplate.query(query, preparedStatementValues.toArray(),
                supplyTypeRowMapper);
        if (!supplyTypes.isEmpty()) {
            sourceTypeExist = true;
            waterConnectionRequest.getConnection().setSupplyType(supplyTypes.get(0));
        }

        return sourceTypeExist;
    }
}
