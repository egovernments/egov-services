package org.egov.commons.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.commons.model.AuthenticatedUser;
import org.egov.commons.model.BusinessCategory;
import org.egov.commons.model.BusinessCategoryCriteria;
import org.egov.commons.repository.builder.BusinessCategoryQueryBuilder;
import org.egov.commons.repository.rowmapper.BusinessCategoryRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class BusinessCategoryRepository {
    public static final Logger LOGGER = LoggerFactory.getLogger(BusinessCategoryRepository.class);

	public static final String INSERT_SERVICECATEGORY_QUERY="INSERT INTO eg_servicecategory"
			+"(id,name,code,active,tenantId,createdBy,createdDate,lastModifiedBy,lastModifiedDate)"
			+" VALUES(nextval('seq_eg_servicecategory'),?,?,?,?,?,?,?,?)";
	
	public static final String GET_SERVICECATEGORY_BY_CODE_AND_TENANTID="Select * from eg_servicecategory"
			+" Where code=? and tenantId=?";
	
	public static final String UPDATE_SERVICECATEGORY="Update eg_servicecategory"
			+" set name=?,code=?,active=?,tenantId=?,lastModifiedBy=?,lastModifiedDate=?"
			+ " where id=?";
	
	public static final String GET_CATEGORY_BY_NAME_AND_TENANTID="Select * from eg_servicecategory"
			+" where name=? and tenantId=?";
	
	public static final String GET_CATEGORY_BY_CODE_AND_TENANTID="Select * from eg_servicecategory"
			+" where code=? and tenantId=?";
	
	
    @Autowired 
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private BusinessCategoryRowMapper businessCategoryRowMapper;
    
    @Autowired
    private BusinessCategoryQueryBuilder businessCategoryQueryBuilder;


	public BusinessCategory create(BusinessCategory category,AuthenticatedUser user) {
	

		Object[] obj=new Object[]{category.getName(),category.getCode()
				,category.getIsactive(),category.getTenantId(),user.getId()
				,new Date(new java.util.Date().getTime()),
				user.getId(),new Date(new java.util.Date().getTime())};
	   jdbcTemplate.update(INSERT_SERVICECATEGORY_QUERY,obj);
	    return category;
	}
	
	

	public BusinessCategory update(String businessCategoryCode, BusinessCategory category,AuthenticatedUser user) {
		
		final List<Object> preparedStatementValues = new ArrayList<>();
		preparedStatementValues.add(businessCategoryCode);
		preparedStatementValues.add(category.getTenantId());
		 List<BusinessCategory> categoryFromdb = jdbcTemplate.query(GET_SERVICECATEGORY_BY_CODE_AND_TENANTID,
				preparedStatementValues.toArray(),businessCategoryRowMapper);
		 BusinessCategory dbCategory= categoryFromdb.get(0);
        Object[] obj=new Object[]{category.getName(),category.getCode()
						,category.getIsactive(),category.getTenantId(),user.getId()
						,new Date(new java.util.Date().getTime()),dbCategory.getId()};
		 jdbcTemplate.update(UPDATE_SERVICECATEGORY,obj);
		 return category;
	}

    public List<BusinessCategory> getForCriteria(BusinessCategoryCriteria criteria) {
		List<Object> preparedStatementValues = new ArrayList<>();
		String queryStr = businessCategoryQueryBuilder.getQuery(criteria, preparedStatementValues);
		return jdbcTemplate.query(queryStr,
				preparedStatementValues.toArray(),businessCategoryRowMapper);
	}

    public boolean checkCategoryByNameAndTenantIdExists(String name, String tenantId) {
    	   final List<Object> preparedStatementValues = new ArrayList<>();
           preparedStatementValues.add(name);
           preparedStatementValues.add(tenantId);
          List<BusinessCategory> categoryFromDb= jdbcTemplate.query(GET_CATEGORY_BY_NAME_AND_TENANTID,
        		   preparedStatementValues.toArray(),businessCategoryRowMapper);
          if(!categoryFromDb.isEmpty())
          return false;
          else
          return true;
       }



	public boolean checkCategoryByCodeAndTenantIdExists(String code, String tenantId) {
 	   final List<Object> preparedStatementValues = new ArrayList<Object>();
       preparedStatementValues.add(code);
       preparedStatementValues.add(tenantId);
      List<BusinessCategory> categoryFromDb= jdbcTemplate.query(GET_CATEGORY_BY_CODE_AND_TENANTID,
    		   preparedStatementValues.toArray(),businessCategoryRowMapper);
      if(!categoryFromDb.isEmpty())
      return false;
      else
    	  return true;
	}
}
