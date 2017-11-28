package org.egov.inv.persistence.repository;

import static org.springframework.util.StringUtils.isEmpty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.JdbcRepository;
import org.egov.common.Pagination;
import org.egov.inv.model.PriceList;
import org.egov.inv.model.PriceListDetails;
import org.egov.inv.model.PriceListDetailsSearchRequest;
import org.egov.inv.model.PriceListSearchRequest;
import org.egov.inv.model.SupplierGetRequest;
import org.egov.inv.persistence.entity.PriceListEntity;
import org.egov.tracer.model.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class PriceListJdbcRepository extends JdbcRepository {

	private static final Logger LOG = LoggerFactory.getLogger(PriceListJdbcRepository.class);
 

	static {
		LOG.debug("init priceList");
		init(PriceListEntity.class);
		LOG.debug("end init priceList");
	}
	
	public static synchronized void init(Class T) {
        String TABLE_NAME = "";

        List<String> insertFields = new ArrayList<>();
        List<String> updateFields = new ArrayList<>();
        List<String> uniqueFields = new ArrayList<>();

        String insertQuery = "";
        String updateQuery = "";
        String searchQuery = "";

        try {

            TABLE_NAME = (String) T.getDeclaredField("TABLE_NAME").get(null);
        } catch (Exception e) {

        }
        insertFields.addAll(fetchFields(T));
//        uniqueFields.add("rateContractNumber");
        uniqueFields.add("tenantId");
        insertFields.removeAll(uniqueFields);
        allInsertQuery.put(T.getSimpleName(), insertQuery(insertFields, TABLE_NAME, uniqueFields));
        updateFields.addAll(insertFields);
        updateFields.remove("createdBy");
        updateQuery = updateQuery(updateFields, TABLE_NAME, uniqueFields);
        System.out.println(T.getSimpleName() + "--------" + insertFields);
        allInsertFields.put(T.getSimpleName(), insertFields);
        allUpdateFields.put(T.getSimpleName(), updateFields);
        allIdentitiferFields.put(T.getSimpleName(), uniqueFields);
        // allInsertQuery.put(T.getSimpleName(), insertQuery);
        allUpdateQuery.put(T.getSimpleName(), updateQuery);
        getByIdQuery.put(T.getSimpleName(), getByIdQuery(TABLE_NAME, uniqueFields));
        System.out.println(allInsertQuery);
    }

    @Autowired
    PriceListDetailJdbcRepository priceListDetailJdbcRepository;
    
    @Autowired
    SupplierJdbcRepository supplierJdbcRepository;

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

        String orderBy = "order by supplier asc, rateType asc, rateContractDate desc";
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
        
        if (priceListSearchRequest.getSupplierName()!=null) {
        	if (params.length() > 0) {
        		params.append(" and ");
        	}
        	params.append("supplier =:supplierName");
        	paramValues.put("supplierName", priceListSearchRequest.getSupplierName());
        }

        if (priceListSearchRequest.getRateType()!=null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("rateType =:rateType");
            paramValues.put("rateType", priceListSearchRequest.getRateType());
        }
        
        if (priceListSearchRequest.getRateContractNumber()!=null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("rateContractNumber =:rateContractNumber");
            paramValues.put("rateContractNumber", priceListSearchRequest.getRateContractNumber().toUpperCase());
        }
        
        if (priceListSearchRequest.getAgreementNumber()!=null) {
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
		
        if (priceListSearchRequest.getAgreementDate()!=null) {
        	if (params.length() > 0) {
        		params.append(" and ");
        	}
        	params.append("agreementDate::bigint =:agreementDate");
        	paramValues.put("agreementDate", priceListSearchRequest.getAgreementDate());
        }
        
        if (priceListSearchRequest.getRateContractDate()!=null) {
        	if (params.length() > 0) {
        		params.append(" and ");
        	}
        	params.append("rateContractDate::bigint =:rateContractDate");
        	paramValues.put("rateContractDate", priceListSearchRequest.getRateContractDate());
        }
        
        if (priceListSearchRequest.getAgreementStartDate()!=null) {
        	if (params.length() > 0) {
        		params.append(" and ");
        	}
        	params.append("agreementStartDate::bigint =:agreementStartDate");
        	paramValues.put("agreementStartDate", priceListSearchRequest.getAgreementStartDate());
        }
        
        if (priceListSearchRequest.getAgreementEndDate()!=null) {
        	if (params.length() > 0) {
        		params.append(" and ");
        	}
        	params.append("agreementEndDate::bigint =:agreementEndDate");
        	paramValues.put("agreementEndDate", priceListSearchRequest.getAgreementEndDate());
        }
        
        if (priceListSearchRequest.getActive()!=null) {
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


//        List<PriceList> priceListList = priceListEntities.stream().map(PriceListEntity::toDomain)
//                .collect(Collectors.toList());
        
        List<PriceList> priceListList = new ArrayList<>();
        
        PriceList pl;
        PriceListDetailsSearchRequest pdlsr;
        SupplierGetRequest sgr;
        
        for(PriceListEntity priceListEntity : priceListEntities){
        	pl = priceListEntity.toDomain();
        	String id = priceListEntity.getId();
        	String supCode = priceListEntity.getSupplier();
        	pdlsr = new PriceListDetailsSearchRequest();
        	sgr = new SupplierGetRequest();
        	pdlsr.setPriceList(id);
        	pdlsr.setDeleted(false);
        	pdlsr.setActive(true);
        	sgr.setCode(Arrays.asList(supCode));
        	pdlsr.setTenantId(priceListEntity.getTenantId());
        	pl.setPriceListDetails(priceListDetailJdbcRepository.search(pdlsr).getPagedData());
        	pl.setSupplier(supplierJdbcRepository.search(sgr).getPagedData().get(0));
        	priceListList.add(pl);
        }

        page.setTotalResults(priceListList.size());

        page.setPagedData(priceListList);

        return page;
    }

    public boolean isDuplicateContract(List<PriceList> priceLists){
		for(PriceList pl: priceLists){
			
			for(PriceListDetails plds: pl.getPriceListDetails()){
				
				String searchQuery = "select count(*) from pricelist where id in ( select pricelist from pricelistdetails where material = '" + plds.getMaterial().getCode() + "' and active=true and ( fromdate::bigint >= " + pl.getAgreementStartDate() + " and fromdate::bigint <= " + pl.getAgreementEndDate() + " ) )and active=true and rateType='" + pl.getRateType().toString() + "'";
				Long count = namedParameterJdbcTemplate.queryForObject(searchQuery, new HashMap(), Long.class);
				if(count!=0l){
					return true;
				}
				String searchQuery1 = "select count(*) from pricelist where id in ( select pricelist from pricelistdetails where material = '" + plds.getMaterial().getCode() + "' and active=true and ( todate::bigint >= " + pl.getAgreementStartDate() + " and todate::bigint <= " + pl.getAgreementEndDate() + " ) )and active=true and rateType='" + pl.getRateType().toString() + "'";
				Long count1 = namedParameterJdbcTemplate.queryForObject(searchQuery, new HashMap(), Long.class);
				if(count1!=0l){
					return true;
				}
			}
			
		}
    	return false;
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
