package org.egov.asset.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class AssetRepositoryUtils {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final StringBuilder Base_query=new StringBuilder("Select id from where ");
	
	public Integer checkDataExists(String table, List<String> cloumns, List<Object> values,String tenantId) {
		/*StringBuilder sql = Base_query;
		sql.append(table);
		for(String column:cloumns){
			sql.append(column);
			if(cloumns.size())
			sql.append(" = ?,");
		}*/
		
		String sql = "select * from egasset_assetcategory where name = ?";
		return jdbcTemplate.update(sql.toString(), values.toArray());
		
	}

}
