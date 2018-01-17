/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */

package org.egov.asset.service;

import java.util.Date;
import java.util.List;

import org.egov.asset.exception.Error;
import org.egov.asset.exception.ErrorResponse;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.enums.Sequence;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AssetCommonService {

    @Autowired
    private SequenceGenService sequenceGenService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ErrorResponse populateErrors(final BindingResult errors) {
        final ErrorResponse errRes = new ErrorResponse();

        final ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
        errRes.setResponseInfo(responseInfo);

        final Error error = new Error();
        error.setCode(1);
        error.setDescription("Error while binding request");
        if (errors.hasFieldErrors())
            for (final FieldError errs : errors.getFieldErrors())
                error.getFields().put(errs.getField(), errs.getRejectedValue());
        errRes.setError(error);
        return errRes;
    }

    public Double getDepreciationRate(final Double depreciationRate) {
        if (depreciationRate != null) {
            final Double deprRate = Math.round(depreciationRate * 100.0) / 100.0;
            log.debug("Depreciation Rate ::" + deprRate);
            return deprRate;
        } else
            return null;
    }

    public void validateDepreciationRateValue(final Double depreciationRate) {
        if (depreciationRate != null && Double.compare(depreciationRate, Double.valueOf("0.0")) < 0)
            throw new RuntimeException("Depreciation rate can not be negative.");
    }

    public AuditDetails getAuditDetails(final RequestInfo requestInfo) {
        final String id = requestInfo.getUserInfo().getId().toString();
        final Long time = new Date().getTime();
        return AuditDetails.builder().createdBy(id).lastModifiedBy(id).createdDate(time).lastModifiedDate(time).build();
    }

    public String getCode(final String codeFormat, final Sequence sequence) {
        final List<Long> codeSequence = sequenceGenService.getIds(1, sequence.toString());
        log.debug("code sequence result :: " + codeSequence);
        if (codeSequence != null && !codeSequence.isEmpty()) {
            final StringBuilder code = new StringBuilder(String.format(codeFormat, codeSequence.get(0)));
            log.debug("Generated code :: " + code);
            return code.toString();
        } else
            throw new RuntimeException("Code is not generated.");
    }

    public Long getNextId(final Sequence sequence) {

        final String query = "SELECT nextval('" + sequence.toString() + "')";
        Integer result = null;
        try {
            result = jdbcTemplate.queryForObject(query, Integer.class);
            log.debug("result:" + result);
            return result.longValue();
        } catch (final Exception ex) {
            throw new RuntimeException("Next id is not generated.");
        }
    }
}
