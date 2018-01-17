package org.egov.inv.persistence.repository;

import static org.springframework.util.StringUtils.isEmpty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.Constants;
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
@SuppressWarnings({ "unchecked", "rawtypes" })
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
        uniqueFields.add("id");
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

    public Long getTenderQty(String supplier, String material, String ratetype) {
        String tenderQtyQuery = "select quantity from pricelistdetails where active=true and deleted is not true and material = :material and pricelist in (select id from pricelist where ratetype=:rateType and supplier= :supplier and extract(epoch from now())::bigint * 1000 between agreementstartdate and agreementenddate ) ";
	    Map params=new HashMap<String,Object>();
		params.put("material",material);
		params.put("rateType",ratetype);
		params.put("supplier",supplier);
        Long tenderQty = namedParameterJdbcTemplate.queryForObject(tenderQtyQuery, params, Long.class);
        return tenderQty;
    }

    public List<PriceList> searchPriceList(PriceListSearchRequest priceListSearchRequest) {
        Map<String, Object> paramValues = new HashMap<>();
        StringBuffer params = new StringBuffer();

        String searchQuery = "select * from pricelist   :condition  :orderby ";
        String orderBy = " order by supplier asc, rateType asc, rateContractDate desc";

        params.append(" active=true  ");
        if (priceListSearchRequest.getTenantId() != null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("tenantid =:tenantId");
            paramValues.put("tenantId", priceListSearchRequest.getTenantId());
        }
        if (priceListSearchRequest.getMaterialCode() != null || priceListSearchRequest.getAsOnDate() != null) {
            // TODO: APPEND IS ACTIVE AT DETAIL LEVEL ALSO.
            if (priceListSearchRequest.getMaterialCode() != null
                    && priceListSearchRequest.getAsOnDate() != null) {
                params.append(" and  id in ( select pricelist from pricelistdetails "
                        + " where material=:materialCode  and :asOnDate between fromdate and todate )    ");
                //todate >= (extract(epoch from now())::bigint * 1000) 
                paramValues.put("materialCode", priceListSearchRequest.getMaterialCode());
                paramValues.put("asOnDate", priceListSearchRequest.getAsOnDate());
            } else if (priceListSearchRequest.getMaterialCode() != null) {
                params.append(
                        "  and id in ( select pricelist from pricelistdetails " + "where material=:materialCode )  ");
                paramValues.put("materialCode", priceListSearchRequest.getMaterialCode());
            } else if (priceListSearchRequest.getAsOnDate() != null) {
                params.append(" and id in ( select pricelist from pricelistdetails "
                        + "where  :asOnDate between fromdate and todate   )  ");
                //and todate >= (extract(epoch from now())::bigint * 1000)
                paramValues.put("asOnDate", priceListSearchRequest.getAsOnDate());

            }

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
            paramValues.put("ids", priceListSearchRequest.getIds());
        }
        
        if (priceListSearchRequest.getSuppliers() != null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("supplier in (:suppliers)");
            paramValues.put("suppliers", priceListSearchRequest.getSuppliers());
        }

        if (priceListSearchRequest.getSupplierName() != null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("supplier =:supplierName");
            paramValues.put("supplierName", priceListSearchRequest.getSupplierName());
        }

        if (priceListSearchRequest.getRateType() != null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("rateType =:rateType");
            paramValues.put("rateType", priceListSearchRequest.getRateType());
        }

        if (params.length() > 0) {

            searchQuery = searchQuery.replace(":condition", " where isdeleted is not true and " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);
        BeanPropertyRowMapper row = new BeanPropertyRowMapper(PriceListEntity.class);

        List<PriceListEntity> priceListEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues,
                row);

        List<PriceList> priceListList = new ArrayList<>();

        PriceList pl;
        PriceListDetailsSearchRequest pdlsr;
        SupplierGetRequest sgr;

        for (PriceListEntity priceListEntity : priceListEntities) {
            pl = priceListEntity.toDomain();
            String supCode = priceListEntity.getSupplier();
            pdlsr = new PriceListDetailsSearchRequest();
            sgr = new SupplierGetRequest();
            pdlsr.setPriceList(priceListEntity.getId());
            pdlsr.setDeleted(false);
            pdlsr.setActive(true);
            sgr.setCode(Arrays.asList(supCode));
            pdlsr.setTenantId(priceListEntity.getTenantId());
            if (priceListSearchRequest.getMaterialCode() != null )
            	pdlsr.setMaterial(priceListSearchRequest.getMaterialCode());
            pl.setPriceListDetails(priceListDetailJdbcRepository.search(pdlsr).getPagedData());
            pl.setSupplier(supplierJdbcRepository.search(sgr).getPagedData().get(0));
            priceListList.add(pl);
        }

        return priceListList;
    }
    
	public PriceListEntity findById(PriceListEntity entity) {
		List<String> list = allIdentitiferFields.get(entity.getClass().getSimpleName());

		Map<String, Object> paramValues = new HashMap<>();

		for (String s : list) {
			paramValues.put(s, getValue(getField(entity, s), entity));
		}

		List<PriceListEntity> priceLists = namedParameterJdbcTemplate.query(
				getByIdQuery.get(entity.getClass().getSimpleName()).toString(), paramValues,
				new BeanPropertyRowMapper(PriceListEntity.class));
		if (priceLists.isEmpty()) {
			return null;
		} else {
			return priceLists.get(0);
		}

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
        
        
        if (priceListSearchRequest.getMaterialCode() != null || priceListSearchRequest.getAsOnDate() != null) {
            // TODO: APPEND IS ACTIVE AT DETAIL LEVEL ALSO.
          /*  if (priceListSearchRequest.getMaterialCode() != null
                    && priceListSearchRequest.getAsOnDate() != null) {
                params.append(" and  id in ( select pricelist from pricelistdetails "
                        + " where material=:materialCode  and :asOnDate between fromdate and todate )    ");
                //todate >= (extract(epoch from now())::bigint * 1000) 
                paramValues.put("materialCode", priceListSearchRequest.getMaterialCode());
                paramValues.put("asOnDate", priceListSearchRequest.getAsOnDate());
            } else*/ if (priceListSearchRequest.getMaterialCode() != null) {
            	if (params.length() > 0) {
                    params.append(" and ");
                }
                params.append(" id in ( select pricelist from pricelistdetails " + "where material=:materialCode )  ");
                paramValues.put("materialCode", priceListSearchRequest.getMaterialCode());
            }/* else if (priceListSearchRequest.getAsOnDate() != null) {
                params.append(" and id in ( select pricelist from pricelistdetails "
                        + "where  :asOnDate between fromdate and todate   )  ");
                //and todate >= (extract(epoch from now())::bigint * 1000)
                paramValues.put("asOnDate", priceListSearchRequest.getAsOnDate());

            }*/

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
            paramValues.put("ids",priceListSearchRequest.getIds());
        }

        if (priceListSearchRequest.getSuppliers() != null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("supplier in (:suppliers)");
            paramValues.put("suppliers", priceListSearchRequest.getSuppliers());
        }

        if (priceListSearchRequest.getRateType() != null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("rateType =:rateType");
            paramValues.put("rateType", priceListSearchRequest.getRateType());
        }

        if (priceListSearchRequest.getRateContractNumber() != null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("rateContractNumber =:rateContractNumber");
            paramValues.put("rateContractNumber", priceListSearchRequest.getRateContractNumber().toUpperCase());
        }

        if (priceListSearchRequest.getRateContractNumbers() != null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("rateContractNumber in(:rateContractNumbers) ");
            paramValues.put("rateContractNumbers",
                    new ArrayList<String>(Arrays.asList(priceListSearchRequest.getRateContractNumbers().split(","))));
        }

        if (priceListSearchRequest.getAgreementNumber() != null) {
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
            paramValues.put("agreementNumbers",priceListSearchRequest.getAgreementNumbers());
        }

        if (priceListSearchRequest.getAgreementDate() != null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("agreementDate =:agreementDate");
            paramValues.put("agreementDate", priceListSearchRequest.getAgreementDate());
        }
        
        if (priceListSearchRequest.getAsOnDate() != null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("agreementStartDate  <=:asOnDate and  agreementEndDate >=:asOnDate");
            paramValues.put("asOnDate", priceListSearchRequest.getAsOnDate());
        }


        if (priceListSearchRequest.getRateContractDate() != null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("rateContractDate =:rateContractDate");
            paramValues.put("rateContractDate", priceListSearchRequest.getRateContractDate());
        }

        if (priceListSearchRequest.getAgreementStartDate() != null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("agreementStartDate =:agreementStartDate");
            paramValues.put("agreementStartDate", priceListSearchRequest.getAgreementStartDate());
        }

        if (priceListSearchRequest.getAgreementEndDate() != null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("agreementEndDate =:agreementEndDate");
            paramValues.put("agreementEndDate", priceListSearchRequest.getAgreementEndDate());
        }

        if (priceListSearchRequest.getActive() != null) {
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

        for (PriceListEntity priceListEntity : priceListEntities) {
            pl = priceListEntity.toDomain();
            String id = priceListEntity.getId();
            String supCode = priceListEntity.getSupplier();
            pdlsr = new PriceListDetailsSearchRequest();
            sgr = new SupplierGetRequest();
            pdlsr.setPriceList(id);
            pdlsr.setDeleted(false);
            sgr.setCode(Arrays.asList(supCode));
            if (priceListSearchRequest.getMaterialCode() != null) {
            	pdlsr.setMaterial(priceListSearchRequest.getMaterialCode());
            }
            pdlsr.setTenantId(priceListEntity.getTenantId());
            pl.setPriceListDetails(priceListDetailJdbcRepository.search(pdlsr).getPagedData());
            pl.setSupplier(supplierJdbcRepository.search(sgr).getPagedData().size() > 0 ? supplierJdbcRepository.search(sgr).getPagedData().get(0) : null);
            priceListList.add(pl);
        }

        page.setTotalResults(priceListList.size());

        page.setPagedData(priceListList);

        return page;
    }

    public boolean isDuplicateContract(List<PriceList> priceLists, String method) {
        for (PriceList pl : priceLists) {

            for (PriceListDetails plds : pl.getPriceListDetails()) {

                Map params=new HashMap<String,Object>();
                params.put("material", plds.getMaterial().getCode());
        		params.put("fromdate", pl.getAgreementEndDate());
        		params.put("todate", pl.getAgreementStartDate());
        		params.put("rateType", pl.getRateType().toString());
        		params.put("supplier", pl.getSupplier().getCode());
            	
                String dupeQuery = "select count(*) from pricelist where id in ( select pricelist from pricelistdetails where material = :material and active=true and ( fromdate <= :fromdate and todate >= :todate ) )and active=true and rateType=:rateType and supplier=:supplier";
                Long dupeCount = namedParameterJdbcTemplate.queryForObject(dupeQuery, params, Long.class);
                if (dupeCount != 0l && method.equals(Constants.ACTION_CREATE)) {
                    return true;
                } else if (dupeCount > 1l && method.equals(Constants.ACTION_UPDATE)) {
                    return true;
                }

                String searchQuery = "select count(*) from pricelist where id in ( select pricelist from pricelistdetails where material = :material and active=true and ( fromdate >= :todate and fromdate <= :fromdate ) )and active=true and rateType=:rateType and supplier=:supplier";
                Long count = namedParameterJdbcTemplate.queryForObject(searchQuery, params, Long.class);
                if (count != 0l && method.equals(Constants.ACTION_CREATE)) {
                    return true;
                } else if (count > 1l && method.equals(Constants.ACTION_UPDATE)) {
                    return true;
                }
                
                String searchQuery1 = "select count(*) from pricelist where id in ( select pricelist from pricelistdetails where material = :material and active=true and ( todate >= :todate and todate <= :fromdate ) )and active=true and rateType=:rateType and supplier=:supplier";
                Long count1 = namedParameterJdbcTemplate.queryForObject(searchQuery1, params, Long.class);
                if (count1 != 0l && method.equals(Constants.ACTION_CREATE)) {
                    return true;
                } else if (count1 > 1l && method.equals(Constants.ACTION_UPDATE)) {
                    return true;
                }
                
                String searchQuery2 = "select count(*) from pricelist where id in ( select pricelist from pricelistdetails where material = :material and active=true and ( fromdate <= :todate and todate >= :todate ) )and active=true and rateType=:rateType and supplier=:supplier";
                Long count2 = namedParameterJdbcTemplate.queryForObject(searchQuery2, params, Long.class);
                if (count2 != 0l && method.equals(Constants.ACTION_CREATE)) {
                    return true;
                } else if (count2 > 1l && method.equals(Constants.ACTION_UPDATE)) {
                    return true;
                }
                
                String searchQuery3 = "select count(*) from pricelist where id in ( select pricelist from pricelistdetails where material = :material and active=true and ( fromdate <= :fromdate and todate >= :fromdate ) )and active=true and rateType=:rateType and supplier=:supplier";
                Long count3 = namedParameterJdbcTemplate.queryForObject(searchQuery3, params, Long.class);
                if (count3 != 0l && method.equals(Constants.ACTION_CREATE)) {
                    return true;
                } else if (count3 > 1l && method.equals(Constants.ACTION_UPDATE)) {
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
