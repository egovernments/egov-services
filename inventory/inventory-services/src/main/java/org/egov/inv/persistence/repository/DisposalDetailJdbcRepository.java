package org.egov.inv.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.JdbcRepository;
import org.egov.common.Pagination;
import org.egov.inv.model.Disposal;
import org.egov.inv.model.DisposalDetail;
import org.egov.inv.model.DisposalSearchContract;
import org.egov.inv.persistence.entity.DisposalDetailEntity;
import org.egov.inv.persistence.entity.DisposalEntity;
import org.egov.inv.persistence.entity.MaterialIssueDetailEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class DisposalDetailJdbcRepository extends JdbcRepository {
	
	  static {
	        init(DisposalDetailEntity.class);
	    }
	
	public DisposalDetailEntity findById(Object entity, String entityName) {
		List<String> list = allIdentitiferFields.get(entityName);

		Map<String, Object> paramValues = new HashMap<>();

		for (String s : list) {
			paramValues.put(s, getValue(getField(entity, s), entity));
		}

		List<DisposalDetailEntity> disposals = namedParameterJdbcTemplate.query(
				getByIdQuery.get(entity.getClass().getSimpleName()).toString(), paramValues,
				new BeanPropertyRowMapper(DisposalDetailEntity.class));
		if (disposals.isEmpty()) {
			return null;
		} else {
			return disposals.get(0);
		}

	}

	public Pagination<DisposalDetail> search(String disposalNumber, String tenantId) {
        String searchQuery = "select * from disposaldetail :condition";
        StringBuffer params = new StringBuffer();
        Map<String, Object> paramValues = new HashMap<>();
        
        if (disposalNumber != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("disposalnumber = :disposalnumber");
            paramValues.put("disposalnumber", disposalNumber);
        }

        if (tenantId != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("tenantId = :tenantId");
            paramValues.put("tenantId", tenantId);
        }
        Pagination<DisposalDetail> page = new Pagination<>();
        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where deleted is not true and " + params.toString());
        else
            searchQuery = searchQuery.replace(":condition", "");
        BeanPropertyRowMapper row = new BeanPropertyRowMapper(DisposalDetailEntity.class);

        List<DisposalDetail> disposalDetailList = new ArrayList<>();

        List<DisposalDetailEntity> disposalDetailEntities = namedParameterJdbcTemplate.query(searchQuery.toString(),
                paramValues, row);

        for (DisposalDetailEntity disposalDetailEntity : disposalDetailEntities) {

        	disposalDetailList.add(disposalDetailEntity.toDomain());
        }

        page.setTotalResults(disposalDetailList.size());

        page.setPagedData(disposalDetailList);

        return page;
        

	}
	
}

 
