package org.egov.works.masters.domain.repository;

import common.persistence.repository.JdbcRepository;
import org.apache.commons.lang3.StringUtils;
import org.egov.works.masters.web.contract.Remarks;
import org.egov.works.masters.web.contract.RemarksDetailsSearchContract;
import org.egov.works.masters.web.contract.RemarksHelper;
import org.egov.works.masters.web.contract.RemarksSearchContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class RemarksRepository extends JdbcRepository {

    @Autowired
    private RemarksDetailsRepository remarksDetailsRepository;

    public static final String TABLE_NAME = "egw_remarks remarks";
    public static final String REMARKS_SEARCH_EXTENSION = ",egw_remarks_detail details";

    public List<Remarks> search(final RemarksSearchContract remarksSearchContract) {

        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        Map<String, Object> paramValues = new HashMap<>();
        StringBuffer params = new StringBuffer();
        String tableName = TABLE_NAME;

        if (remarksSearchContract.getSortBy() != null
                && !remarksSearchContract.getSortBy().isEmpty()) {
            validateSortByOrder(remarksSearchContract.getSortBy());
            validateEntityFieldName(remarksSearchContract.getSortBy(), RemarksHelper.class);
        }

        if(StringUtils.isNotBlank(remarksSearchContract.getRemarksDescription()))
            tableName += REMARKS_SEARCH_EXTENSION;

        String orderBy = "order by remarks.id";
        if (remarksSearchContract.getSortBy() != null
                && !remarksSearchContract.getSortBy().isEmpty()) {
            orderBy = "order by remarks." + remarksSearchContract.getSortBy();
        }

        searchQuery = searchQuery.replace(":tablename", tableName);

        searchQuery = searchQuery.replace(":selectfields", " * ");


        if (StringUtils.isNotBlank(remarksSearchContract.getTenantId())) {
            addAnd(params);
            params.append("remarks.tenantId =:tenantId");
            paramValues.put("tenantId", remarksSearchContract.getTenantId());
        }
        if (remarksSearchContract.getIds() != null) {
            addAnd(params);
            params.append("remarks.id in(:ids) ");
            paramValues.put("ids", remarksSearchContract.getIds());
        }

        if(StringUtils.isNotBlank(remarksSearchContract.getTypeOfDocument())) {
            addAnd(params);
            params.append("remarks.typeOfDocument=:typeOfDocument ");
            paramValues.put("typeOfDocument", remarksSearchContract.getTypeOfDocument());
        }

        if(StringUtils.isNotBlank(remarksSearchContract.getRemarksType())) {
            addAnd(params);
            params.append("remarks.remarksType =:remarksType");
            paramValues.put("remarksType", remarksSearchContract.getRemarksType());

        }

        if(StringUtils.isNotBlank(remarksSearchContract.getRemarksDescription())) {
            addAnd(params);
            params.append("remarks.id = details.details and details.deleted=false and details.remarksDescription=:remarksDescription ");
            paramValues.put("remarksDescription", remarksSearchContract.getRemarksDescription());

        }

        params.append(" and remarks.deleted = false");
        if (params.length() > 0) {

            searchQuery = searchQuery.replace(":condition", " where " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(RemarksHelper.class);

        List<RemarksHelper> remarksHelpers = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        List<Remarks> remarksList = new ArrayList<>();

        for (RemarksHelper remarksHelper : remarksHelpers) {
            Remarks remarks = remarksHelper.toDomain();
            RemarksDetailsSearchContract remarksDetailsSearchContract = RemarksDetailsSearchContract.builder()
                    .tenantId(remarks.getTenantId())
                    .remarks(Arrays.asList(remarks.getId())).build();
            remarks.setRemarksDetails(remarksDetailsRepository.search(remarksDetailsSearchContract));
            remarksList.add(remarks);
        }

        return remarksList;

    }
}
