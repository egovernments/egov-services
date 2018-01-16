package org.egov.works.workorder.domain.repository;

import org.apache.commons.lang3.StringUtils;
import org.egov.works.common.persistence.repository.JdbcRepository;
import org.egov.works.workorder.persistence.helper.NoticeHelper;
import org.egov.works.workorder.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class NoticeJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egw_notice notice";
    public static final String LOA_ESTIMATESEARCH_EXTENTION = "egw_letterofacceptanceestimate loaestimate";
    @Autowired
    private WorksMastersRepository worksMastersRepository;

    @Autowired
    private EstimateRepository estimateRepository;

    @Autowired
    private NoticeDetailRepository noticeDetailRepository;

    @Autowired
    private LetterOfAcceptanceRepository letterOfAcceptanceRepository;

    public List<Notice> searchNotices(final NoticeSearchContract noticeSearchContract, final RequestInfo requestInfo) {

        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        StringBuilder tableName = new StringBuilder(TABLE_NAME);

        Map<String, Object> paramValues = new HashMap<>();
        StringBuilder params = new StringBuilder();

        if (noticeSearchContract.getSortBy() != null && !noticeSearchContract.getSortBy().isEmpty()) {
            validateSortByOrder(noticeSearchContract.getSortBy());
            validateEntityFieldName(noticeSearchContract.getSortBy(), Notice.class);
        }

        if ((noticeSearchContract.getDetailedEstimateNumbers() != null
                && !noticeSearchContract.getDetailedEstimateNumbers().isEmpty()))
            tableName.append(LOA_ESTIMATESEARCH_EXTENTION);

        StringBuilder orderBy = new StringBuilder("order by notice.createdtime");
        if (noticeSearchContract.getSortBy() != null && !noticeSearchContract.getSortBy().isEmpty()) {
            orderBy.delete(0, orderBy.length()).append("order by notice.").append(noticeSearchContract.getSortBy());
        }

        searchQuery = searchQuery.replace(":tablename", tableName);

        searchQuery = searchQuery.replace(":selectfields", " * ");

        if (noticeSearchContract.getTenantId() != null) {
            addAnd(params);
            params.append("notice.tenantId =:tenantId");
            paramValues.put("tenantId", noticeSearchContract.getTenantId());
        }
        if (noticeSearchContract.getIds() != null) {
            addAnd(params);
            params.append("notice.id in(:ids) ");
            paramValues.put("ids", noticeSearchContract.getIds());
        }

        if (noticeSearchContract.getStatuses() != null) {
            addAnd(params);
            params.append("notice.status in(:statuses) ");
            paramValues.put("statuses", noticeSearchContract.getStatuses());
        }

        if (noticeSearchContract.getNoticeNumbers() != null) {
            addAnd(params);
            params.append("notice.noticenumber in(:noticenumbers) ");
            paramValues.put("noticenumbers", noticeSearchContract.getNoticeNumbers());
        }

        if (StringUtils.isNotBlank(noticeSearchContract.getNoticeNumberLike())) {
            addAnd(params);
            params.append("upper(notice.noticenumber) =:noticeNumberLike ");
            paramValues.put("noticeNumberLike", noticeSearchContract.getNoticeNumberLike().toUpperCase());
        }

        if (noticeSearchContract.getWorkOrderNumbers() != null) {
            addAnd(params);
            params.append(
                    "notice.letterofacceptance in (select wo.letterofacceptance from egw_workorder wo where wo.workordernumber in (:workordernumbers) and wo.tenantId=:tenantId and wo.deleted = false)");
            paramValues.put("workordernumbers", noticeSearchContract.getWorkOrderNumbers());
        }

        if (noticeSearchContract.getLoaNumbers() != null) {
            addAnd(params);
            params.append(
                    "notice.letterofacceptance in (select loa.id from egw_letterofacceptance loa where loa.loanumber in (:loaNumbers) and loa.tenantId=:tenantId)");
            paramValues.put("loaNumbers", noticeSearchContract.getLoaNumbers());
        }

        if (StringUtils.isNotBlank(noticeSearchContract.getWorkOrderNumberLike())) {
            addAnd(params);
            params.append(
                    "notice.letterofacceptance in (select wo.letterofacceptance from egw_workorder wo where upper(wo.workordernumber) =:workorderNumberLike and wo.tenantId=:tenantId and wo.deleted = false)");
            paramValues.put("workorderNumberLike", noticeSearchContract.getWorkOrderNumberLike().toUpperCase());
        }

        if (StringUtils.isNotBlank(noticeSearchContract.getLoaNumberLike())) {
            addAnd(params);
            params.append(
                    "notice.letterofacceptance in (select loa.id from egw_letterofacceptance loa where upper(loa.loanumber) =:loaNumberLike and loa.tenantId=:tenantId)");
            paramValues.put("loaNumberLike", noticeSearchContract.getLoaNumberLike().toUpperCase());
        }

        if (noticeSearchContract.getContractorCodes() != null) {
            addAnd(params);
            params.append(
                    "notice.letterofacceptance in (select loa.id from egw_letterofacceptance loa where loa.contractor in (:contractorcodes))");
            paramValues.put("contractorcodes", noticeSearchContract.getContractorCodes());
        }

        if (StringUtils.isNotBlank(noticeSearchContract.getContractorCodeLike())) {
            addAnd(params);
            params.append(
                    "notice.letterofacceptance in (select loa.id from egw_letterofacceptance loa where upper(loa.contractor) =:contractorCodeLike)");
            paramValues.put("contractorCodeLike", noticeSearchContract.getContractorCodeLike().toUpperCase());
        }

        List<String> contractorCodes = new ArrayList<>();
        if (noticeSearchContract.getContractorNames() != null && !noticeSearchContract.getContractorNames().isEmpty()) {
            List<Contractor> contractors = worksMastersRepository.searchContractors(noticeSearchContract.getTenantId(),
                    noticeSearchContract.getContractorNames(), requestInfo);
            for (Contractor contractor : contractors)
                contractorCodes.add(contractor.getCode());
            if (!contractorCodes.isEmpty()) {
                if (contractorCodes != null) {
                    addAnd(params);
                    params.append(
                            "notice.letterofacceptance in (select loa.id from egw_letterofacceptance loa where loa.contractor in (:contractorcodes))");
                    paramValues.put("contractorcodes", contractorCodes);
                }
            }
        }

        List<String> contractorCodesLike = new ArrayList<>();
        if (StringUtils.isNotBlank(noticeSearchContract.getContractorNameLike())) {
            List<Contractor> contractors = worksMastersRepository.searchContractors(noticeSearchContract.getTenantId(),
                    Arrays.asList(noticeSearchContract.getContractorNameLike()), requestInfo);
            for (Contractor contractor : contractors)
                contractorCodesLike.add(contractor.getCode().toUpperCase());
            if (!contractorCodesLike.isEmpty()) {
                if (contractorCodesLike != null) {
                    addAnd(params);
                    params.append(
                            "notice.letterofacceptance in (select loa.id from egw_letterofacceptance loa where upper(loa.contractor) =:contractorcodeslike)");
                    paramValues.put("contractorcodeslike", contractorCodes);
                }
            }
        }

        if (noticeSearchContract.getDetailedEstimateNumbers() != null) {
            addAnd(params);
            params.append(
                    "loaestimate.letterofacceptance = notice.letterofacceptance and loaestimate.detailedestimate in (:detailedestimatenumber) and loaestimate.tenantid=:tenantid ");
            paramValues.put("detailedestimatenumber", noticeSearchContract.getDetailedEstimateNumbers());
            paramValues.put("tenantid", noticeSearchContract.getTenantId());
        }

        if (StringUtils.isNotBlank(noticeSearchContract.getDetailedEstimateNumberLike())) {
            addAnd(params);
            params.append(
                    "loaestimate.letterofacceptance = notice.letterofacceptance and upper(loaestimate.detailedestimate) =:detailedEstimateNumberLike and loaestimate.tenantid=:tenantid ");
            paramValues.put("detailedEstimateNumberLike", noticeSearchContract.getDetailedEstimateNumberLike());
            paramValues.put("tenantid", noticeSearchContract.getTenantId());
        }

        List<String> winEstimateNumbers = new ArrayList<>();
        if (noticeSearchContract.getWorkIdentificationNumbers() != null
                && !noticeSearchContract.getWorkIdentificationNumbers().isEmpty()) {
            List<DetailedEstimate> detailedEstimates = estimateRepository.searchDetailedEstimatesByProjectCode(
                    noticeSearchContract.getWorkIdentificationNumbers(), noticeSearchContract.getTenantId(),
                    requestInfo);
            for (DetailedEstimate detailedEstimate : detailedEstimates)
                winEstimateNumbers.add(detailedEstimate.getEstimateNumber());

            addAnd(params);
            params.append(
                    "loaestimate.letterofacceptance = notice.letterofacceptance and loaestimate.detailedestimate in (:detailedestimatenumber) and loaestimate.tenantid=:tenantid");
            paramValues.put("detailedestimatenumber", winEstimateNumbers);
            paramValues.put("tenantid", noticeSearchContract.getTenantId());
        }

        List<String> winEstimateNumberLike = new ArrayList<>();
        if (StringUtils.isNotBlank(noticeSearchContract.getWorkIdentificationNumberLike())) {
            List<DetailedEstimate> detailedEstimates = estimateRepository.searchDetailedEstimatesByProjectCode(
                    Arrays.asList(noticeSearchContract.getWorkIdentificationNumberLike()), noticeSearchContract.getTenantId(),
                    requestInfo);
            for (DetailedEstimate detailedEstimate : detailedEstimates)
                winEstimateNumberLike.add(detailedEstimate.getEstimateNumber().toUpperCase());
            if (!winEstimateNumberLike.isEmpty()) {
                addAnd(params);
                params.append(
                        "loaestimate.letterofacceptance = notice.letterofacceptance and upper(loaestimate.detailedestimate) =:detailedestimatenumberlike and loaestimate.tenantid=:tenantid");
                paramValues.put("detailedestimatenumberlike", winEstimateNumberLike);
                paramValues.put("tenantid", noticeSearchContract.getTenantId());
            }

        }

        params.append(" and notice.deleted = false");

        if (params.length() > 0) {

            searchQuery = searchQuery.replace(":condition", " where " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(NoticeHelper.class);

        List<NoticeHelper> noticeHelpers = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);

        List<Notice> notices = new ArrayList<>();
        NoticeDetailSearchContract noticeDetailSearchContract = new NoticeDetailSearchContract();
        for (NoticeHelper noticeHelper : noticeHelpers) {

            Notice notice = noticeHelper.toDomain();
            noticeDetailSearchContract.setTenantId(noticeSearchContract.getTenantId());
            noticeDetailSearchContract.setNotices(Arrays.asList(noticeHelper.getId()));
            notice.setNoticeDetails(noticeDetailRepository.searchNoticeDetails(noticeDetailSearchContract));

            LetterOfAcceptanceSearchContract letterOfAcceptanceSearchContract = new LetterOfAcceptanceSearchContract();
            letterOfAcceptanceSearchContract.setTenantId(noticeSearchContract.getTenantId());
            letterOfAcceptanceSearchContract.setIds(Arrays.asList(noticeHelper.getLetterOfAcceptance()));

            List<LetterOfAcceptance> letterOfAcceptanceList = letterOfAcceptanceRepository
                    .searchLOAs(letterOfAcceptanceSearchContract, requestInfo);
            LetterOfAcceptance letterOfAcceptance = new LetterOfAcceptance();
            if (letterOfAcceptanceList != null && !letterOfAcceptanceList.isEmpty())
                letterOfAcceptance = letterOfAcceptanceList.get(0);
            notice.setLetterOfAcceptance(letterOfAcceptance);
            notices.add(notice);
        }

        return notices;
    }

}
