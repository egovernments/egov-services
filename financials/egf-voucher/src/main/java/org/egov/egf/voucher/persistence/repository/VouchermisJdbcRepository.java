package org.egov.egf.voucher.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.domain.model.Pagination;
import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.voucher.domain.model.Vouchermis;
import org.egov.egf.voucher.domain.model.VouchermisSearch;
import org.egov.egf.voucher.persistence.entity.VouchermisEntity;
import org.egov.egf.voucher.persistence.entity.VouchermisSearchEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class VouchermisJdbcRepository extends JdbcRepository {
	private static final Logger LOG = LoggerFactory.getLogger(VouchermisJdbcRepository.class);

	static {
		LOG.debug("init vouchermis");
		init(VouchermisEntity.class);
		LOG.debug("end init vouchermis");
	}

	public VouchermisJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public VouchermisEntity create(VouchermisEntity entity) {
		super.create(entity);
		return entity;
	}

	public VouchermisEntity update(VouchermisEntity entity) {
		super.update(entity);
		return entity;

	}

	public Pagination<Vouchermis> search(VouchermisSearch domain) {
		VouchermisSearchEntity vouchermisSearchEntity = new VouchermisSearchEntity();
		vouchermisSearchEntity.toEntity(domain);

		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (vouchermisSearchEntity.getSortBy() != null && !vouchermisSearchEntity.getSortBy().isEmpty()) {
			validateSortByOrder(vouchermisSearchEntity.getSortBy());
			validateEntityFieldName(vouchermisSearchEntity.getSortBy(), VouchermisEntity.class);
		}

		String orderBy = "order by name";
		if (vouchermisSearchEntity.getSortBy() != null && !vouchermisSearchEntity.getSortBy().isEmpty()) {
			orderBy = "order by " + vouchermisSearchEntity.getSortBy();
		}

		searchQuery = searchQuery.replace(":tablename", VouchermisEntity.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specfic search
if( vouchermisSearchEntity.getId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "id =:id");
paramValues.put("id" ,vouchermisSearchEntity.getId());} 
if( vouchermisSearchEntity.getBillNumber()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "billNumber =:billNumber");
paramValues.put("billNumber" ,vouchermisSearchEntity.getBillNumber());} 
if( vouchermisSearchEntity.getFunctionId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "function =:function");
paramValues.put("function" ,vouchermisSearchEntity.getFunctionId());} 
if( vouchermisSearchEntity.getFundsourceId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "fundsource =:fundsource");
paramValues.put("fundsource" ,vouchermisSearchEntity.getFundsourceId());} 
if( vouchermisSearchEntity.getSchemeId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "scheme =:scheme");
paramValues.put("scheme" ,vouchermisSearchEntity.getSchemeId());} 
if( vouchermisSearchEntity.getSubSchemeId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "subScheme =:subScheme");
paramValues.put("subScheme" ,vouchermisSearchEntity.getSubSchemeId());} 
if( vouchermisSearchEntity.getFunctionaryId()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "functionary =:functionary");
paramValues.put("functionary" ,vouchermisSearchEntity.getFunctionaryId());} 
if( vouchermisSearchEntity.getSourcePath()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "sourcePath =:sourcePath");
paramValues.put("sourcePath" ,vouchermisSearchEntity.getSourcePath());} 
if( vouchermisSearchEntity.getBudgetCheckRequired()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "budgetCheckRequired =:budgetCheckRequired");
paramValues.put("budgetCheckRequired" ,vouchermisSearchEntity.getBudgetCheckRequired());} 
if( vouchermisSearchEntity.getBudgetAppropriationNo()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "budgetAppropriationNo =:budgetAppropriationNo");
paramValues.put("budgetAppropriationNo" ,vouchermisSearchEntity.getBudgetAppropriationNo());} 
if( vouchermisSearchEntity.getIds()!=null) {
if (params.length() > 0) 
params.append(" and "); 
params.append( "ids =:ids");
paramValues.put("ids" ,vouchermisSearchEntity.getIds());} 

		if (vouchermisSearchEntity.getId() != null) {
                    if (params.length() > 0) {
                            params.append(" and ");
                    }
                    params.append("id =:id");
                    paramValues.put("id", vouchermisSearchEntity.getId());
                }
                if (vouchermisSearchEntity.getCode() != null) {
                        if (params.length() > 0) {
                                params.append(" and ");
                        }
                        params.append("code =:code");
                        paramValues.put("code", vouchermisSearchEntity.getCode());
                }
                if (vouchermisSearchEntity.getName() != null) {
                        if (params.length() > 0) {
                                params.append(" and ");
                        }
                        params.append("name =:name");
                        paramValues.put("name", vouchermisSearchEntity.getName());
                }
                if (vouchermisSearchEntity.getIdentifier() != null) {
                        if (params.length() > 0) {
                                params.append(" and ");
                        }
                        params.append("description =:description");
                        paramValues.put("description", vouchermisSearchEntity.getIdentifier());
                }
                if (vouchermisSearchEntity.getActive() != null) {
                        if (params.length() > 0) {
                                params.append(" and ");
                        }
                        params.append("active =:active");
                        paramValues.put("active", vouchermisSearchEntity.getActive());
                }
                if (vouchermisSearchEntity.getLevel() != null) {
                        if (params.length() > 0) {
                                params.append(" and ");
                        }
                        params.append("type =:type");
                        paramValues.put("type", vouchermisSearchEntity.getLevel());
                }

		Pagination<Vouchermis> page = new Pagination<>();
		if (vouchermisSearchEntity.getOffset() != null) {
			page.setOffset(vouchermisSearchEntity.getOffset());
		}
		if (vouchermisSearchEntity.getPageSize() != null) {
			page.setPageSize(vouchermisSearchEntity.getPageSize());
		}

		
		if (params.length() > 0) {
		
		searchQuery = searchQuery.replace(":condition", " where " +
		params.toString());
		
		} else 
		
		searchQuery = searchQuery.replace(":condition", "");

		searchQuery = searchQuery.replace(":orderby", orderBy);

		page = (Pagination<Vouchermis>) getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination",
				"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(VouchermisEntity.class);

		List<VouchermisEntity> vouchermisEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);

		page.setTotalResults(vouchermisEntities.size());

		List<Vouchermis> vouchermises = new ArrayList<>();
		for (VouchermisEntity vouchermisEntity : vouchermisEntities) {

			vouchermises.add(vouchermisEntity.toDomain());
		}
		page.setPagedData(vouchermises);

		return page;
	}

	public VouchermisEntity findById(VouchermisEntity entity) {
		List<String> list = allIdentitiferFields.get(entity.getClass().getSimpleName());

		Map<String, Object> paramValues = new HashMap<>();

		for (String s : list) {
			paramValues.put(s, getValue(getField(entity, s), entity));
		}

		List<VouchermisEntity> vouchermises = namedParameterJdbcTemplate.query(
				getByIdQuery.get(entity.getClass().getSimpleName()).toString(), paramValues,
				new BeanPropertyRowMapper(VouchermisEntity.class));
		if (vouchermises.isEmpty()) {
			return null;
		} else {
			return vouchermises.get(0);
		}

	}

}