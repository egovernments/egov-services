package org.egov.inv.persistense.repository;

import static org.springframework.util.StringUtils.isEmpty;
import io.swagger.model.Pagination;
import io.swagger.model.PriceList;
import io.swagger.model.PriceListSearchRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.inv.persistense.repository.entity.PriceListEntity;
import org.egov.tracer.model.CustomException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class PriceListJdbcRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PriceListJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Pagination<PriceList> search(PriceListSearchRequest priceListSearchRequest) {
        String searchQuery = "select * from pricelist :condition  :orderby ";

        Map<String, Object> paramValues = new HashMap<>();
        StringBuffer params = new StringBuffer();

        if (!isEmpty(priceListSearchRequest.getSortBy())) {
            validateSortByOrder(priceListSearchRequest.getSortBy());
            validateEntityFieldName(priceListSearchRequest.getSortBy(), PriceListSearchRequest.class);
        }

        String orderBy = "order by id";
        if (!isEmpty(priceListSearchRequest.getSortBy())) {
            orderBy = "order by " + priceListSearchRequest.getSortBy();
        }

        if (priceListSearchRequest.getTenantId() != null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("tenantid =:tenantId");
            paramValues.put("tenantId", priceListSearchRequest.getTenantId());
        }

        if (priceListSearchRequest.getId() != null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("id =:id");
            paramValues.put("id", priceListSearchRequest.getId());
        }
        
		if (priceListSearchRequest.getIds() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("id in(:ids) ");
			paramValues.put("ids",
					new ArrayList<String>(Arrays.asList(priceListSearchRequest.getIds().split(","))));
		}
        
        if (!isEmpty(priceListSearchRequest.getSupplier())) {
        	if (params.length() > 0) {
        		params.append(" and ");
        	}
        	params.append("supplier =:supplier");
        	paramValues.put("supplier", priceListSearchRequest.getSupplier());
        }

        if (!isEmpty(priceListSearchRequest.getRateType())) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("rateType =:rateType");
            paramValues.put("rateType", priceListSearchRequest.getRateType());
        }
        
        if (!isEmpty(priceListSearchRequest.getRateContractNumber())) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("rateContractNumber =:rateContractNumber");
            paramValues.put("rateContractNumber", priceListSearchRequest.getRateContractNumber());
        }
        
        if (!isEmpty(priceListSearchRequest.getAgreementNumber())) {
        	if (params.length() > 0) {
        		params.append(" and ");
        	}
        	params.append("agreementNumber =:agreementNumber");
        	paramValues.put("agreementNumber", priceListSearchRequest.getAgreementNumber());
        }
        
		if (priceListSearchRequest.getAgreementNumbers() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("agreementNumber in(:agreementNumbers) ");
			paramValues.put("agreementNumbers",
					new ArrayList<String>(Arrays.asList(priceListSearchRequest.getAgreementNumbers().split(","))));
		}
        
        if (!isEmpty(priceListSearchRequest.getAgreementStartDate())) {
        	if (params.length() > 0) {
        		params.append(" and ");
        	}
        	params.append("agreementStartDate =:agreementStartDate");
        	paramValues.put("agreementStartDate", priceListSearchRequest.getAgreementStartDate());
        }
        
        if (!isEmpty(priceListSearchRequest.getAgreementEndDate())) {
        	if (params.length() > 0) {
        		params.append(" and ");
        	}
        	params.append("agreementEndDate =:agreementEndDate");
        	paramValues.put("agreementEndDate", priceListSearchRequest.getAgreementEndDate());
        }
        
        if (!isEmpty(priceListSearchRequest.getActive())) {
        	if (params.length() > 0) {
        		params.append(" and ");
        	}
        	params.append("active =:active");
        	paramValues.put("active", priceListSearchRequest.getActive());
        }
        
        Pagination<PriceList> page = new Pagination<>();
        if (priceListSearchRequest.getOffSet() != null) {
            page.setOffset(priceListSearchRequest.getOffSet());
        }
        if (priceListSearchRequest.getPageSize() != null) {
            page.setPageSize(priceListSearchRequest.getPageSize());
        }

        if (params.length() > 0) {

            searchQuery = searchQuery.replace(":condition", " where " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        page = (Pagination<PriceList>) getPagination(searchQuery, page, paramValues);
        searchQuery = searchQuery + " :pagination";

        searchQuery = searchQuery.replace(":pagination",
                "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(PriceListEntity.class);

        List<PriceListEntity> priceListEntities = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);


        List<PriceList> priceListList = priceListEntities.stream().map(PriceListEntity::toDomain)
                .collect(Collectors.toList());

        page.setTotalResults(priceListList.size());

        page.setPagedData(priceListList);

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
}
