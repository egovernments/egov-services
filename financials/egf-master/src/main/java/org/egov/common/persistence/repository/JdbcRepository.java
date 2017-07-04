package org.egov.common.persistence.repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.domain.model.Pagination;
import org.egov.common.persistence.entity.AuditableEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public abstract class JdbcRepository {

 
	

	@Autowired
	public JdbcTemplate jdbcTemplate;
	@Autowired
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public static final Map<String, List<String>> allInsertFields = new HashMap<String, List<String>>();
	public static final Map<String, List<String>> allUpdateFields = new HashMap<String, List<String>>();
	public static final Map<String, List<String>> allUniqueFields = new HashMap<String, List<String>>();

	public static final Map<String, String> allInsertQuery = new HashMap<>();
	public static final Map<String, String> allUpdateQuery = new HashMap<>();
	public static final Map<String, String> allSearchQuery = new HashMap<>();
	public static final Map<String, String> getByIdQuery = new HashMap<>();

	public static synchronized void init(Class T) {
		String TABLE_NAME = "";

		List<String> insertFields = new ArrayList<>();
		List<String> updateFields = new ArrayList<>();
		List<String> uniqueFields = new ArrayList<>();

		String insertQuery="";
		String updateQuery="";
		String searchQuery="";

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
		System.out.println(T.getSimpleName()+"--------"+insertFields);
		allInsertFields.put(T.getSimpleName(), insertFields);
		allUpdateFields.put(T.getSimpleName(), updateFields);
		allUniqueFields.put(T.getSimpleName(), uniqueFields);
	//	allInsertQuery.put(T.getSimpleName(), insertQuery);
		allUpdateQuery.put(T.getSimpleName(), updateQuery);
		getByIdQuery.put(T.getSimpleName(), getByIdQuery(TABLE_NAME, uniqueFields));
		System.out.println(allInsertQuery);
	}

	private static String insertQuery(List<String> fields, String tableName, List<String> uniqueFields) {
		String iQuery = "insert into :tableName (:fields) values (:params) ";
		StringBuilder fieldNames = new StringBuilder();
		StringBuilder paramNames = new StringBuilder();
		int i = 0;
		for (String s : fields) {
			if (i > 0) {
				fieldNames.append(", ");
				paramNames.append(", ");
			}
			fieldNames.append(s);
			paramNames.append(":").append(s);
			i++;
		}

		for (String s : uniqueFields) {
			if (i > 0) {
				fieldNames.append(", ");
				paramNames.append(", ");
			}
			fieldNames.append(s);
			paramNames.append(":").append(s);
			i++;
		}

		System.out.println(fields);
		System.out.println(uniqueFields);
		iQuery = iQuery.replace(":fields", fieldNames.toString()).replace(":params", paramNames.toString())
				.replace(":tableName", tableName).toString();
		System.out.println(tableName+"----"+iQuery);
		return iQuery;
	}
	
	
	

	public static List<String> fetchFields(Class ob) {
		List<String> fields = new ArrayList<>();
		for (Field f : ob.getDeclaredFields()) {
			if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
				continue;
			}
			fields.add(f.getName());
		}

		for (Field f : AuditableEntity.class.getDeclaredFields()) {

			if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
				continue;
			}
			fields.add(f.getName());
		}
		return fields;
	}

	public static String updateQuery(List<String> fields, String tableName, List<String> uniqueFields) {
		String uQuery = "update :tableName set (:fields) where  :uniqueField ) ";
		StringBuilder fieldNameAndParams = new StringBuilder();
		StringBuilder uniqueFieldNameAndParams = new StringBuilder();
		int i = 0;
		for (String s : fields) {
			if (i > 0) {
				fieldNameAndParams.append(", ");

			}
			fieldNameAndParams.append(s).append("=").append(":").append(s);
			i++;
		}

		for (String s : uniqueFields) {
			if (i > 0) {
				uniqueFieldNameAndParams.append(" and ");

			}
			uniqueFieldNameAndParams.append(s).append("=").append(":").append(s);
			i++;
		}

		uQuery = uQuery.replace(":fields", fieldNameAndParams.toString()).replace(":uniqueField",
				uniqueFieldNameAndParams.toString());
		return uQuery;
	}
	
	public static String getByIdQuery( String tableName, List<String> uniqueFields) {
		String uQuery = "select * from  :tableName where  :uniqueField ) ";
		//StringBuilder fieldNameAndParams = new StringBuilder();
		StringBuilder uniqueFieldNameAndParams = new StringBuilder();
		int i = 0;
		 

		for (String s : uniqueFields) {
			if (i > 0) {
				uniqueFieldNameAndParams.append(" and ");

			}
			uniqueFieldNameAndParams.append(s).append("=").append(":").append(s);
			i++;
		}

		uQuery = uQuery.replace(":uniqueField",
				uniqueFieldNameAndParams.toString());
		return uQuery;
	}
	
	public static String getBasicSearchQuery( String tableName, Object obj) {
		String uQuery = "select * from  :tableName where  :searchFields ) ";
		StringBuilder fieldNameAndParams = new StringBuilder();
		//StringBuilder uniqueFieldNameAndParams = new StringBuilder();
		int i = 0;
		 

		for (String s : allInsertFields.get("FundEntity")) {
			if (i > 0) {
				fieldNameAndParams.append(" and ");
				

			}
			try {
				Field declaredField = getField(obj, s);
				if(getValue(declaredField,obj)!=null)
					
				{
				fieldNameAndParams.append(s).append("=").append(":").append(s);
				i++;
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			
			 
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		uQuery = uQuery.replace(":searchFields",
				fieldNameAndParams.toString());
		return uQuery;
	}

	public static Object getValue(Field declaredField, Object obj) {
		
		Object ob1=obj;
		Object val=null;
		while(ob1!=null)
		{
			try {
				val=	declaredField.get(obj);
				if(val!=null)
					break;
			} catch (Exception e) {
				if(ob1.getClass().getSuperclass()!=null)
					ob1=ob1.getClass().getSuperclass();
					else 
					break;
				
			}
			
		}
		return val;
		
	}

	public static Field getField(Object obj, String s)  {
		System.out.println(s);
		Field declaredField =null;
		Object ob1=obj;
		while(declaredField==null)
		{
			try {
				declaredField=	ob1.getClass().getDeclaredField(s);
			} catch (Exception e) {
				try{
					declaredField=	ob1.getClass().getSuperclass().getDeclaredField(s);
				} catch (Exception e1) {
					break;
				}
			}
			
		}
		if(declaredField!=null)
		declaredField.setAccessible(true);
		return declaredField;
	}

	public Map<String, Object> paramValues(Object ob, List<String> fields) {
		Map<String, Object> paramValues = new LinkedHashMap<>();

		for (String s : fields) {
			Field f = null;

			try {
				f = getField(ob, s);
			} catch (Exception e) {}
				/*try {
					f = ob.getClass().getSuperclass().getDeclaredField(s);
				} catch (NoSuchFieldException e1) {
					System.out.println("Unable to find the field in this class and its super class for field" + s);
				}
			}*/
			try {
				f.setAccessible(true);
				paramValues.put(s, f.get(ob));
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		return paramValues;

	}

	public Object create(Object ob) {
		System.out.println(allInsertQuery);
		((AuditableEntity)ob).setCreatedDate(new Date());
		((AuditableEntity)ob).setLastModifiedDate(new Date());
		
		String obName = ob.getClass().getSimpleName();
		List<Map<String, Object>> batchValues = new ArrayList<>();
		batchValues.add(paramValues(ob, allInsertFields.get(obName)));
		batchValues.get(0).putAll(paramValues(ob, allUniqueFields.get(obName)));
		//System.out.println(obName+"----" +allInsertQuery.get(obName));   
		namedParameterJdbcTemplate.batchUpdate(allInsertQuery.get(obName),
				batchValues.toArray(new Map[batchValues.size()]));
		return ob;
	}
	

	public Object update(Object ob) {
		System.out.println(allUpdateQuery);
		((AuditableEntity)ob).setCreatedDate(new Date());
		((AuditableEntity)ob).setLastModifiedDate(new Date());
		
		String obName = ob.getClass().getSimpleName();
		List<Map<String, Object>> batchValues = new ArrayList<>();
		batchValues.add(paramValues(ob, allUpdateFields.get(obName)));
		batchValues.get(0).putAll(paramValues(ob, allUniqueFields.get(obName)));
		System.out.println(obName+"----" +allUpdateQuery.get(obName));   
		namedParameterJdbcTemplate.batchUpdate(allUpdateQuery.get(obName),
				batchValues.toArray(new Map[batchValues.size()]));
		return ob;
	}
	
	public Pagination getPagination(String searchQuery,Pagination page) {
		String countQuery="select count(*) from ("+searchQuery+") as x";
		Long count = jdbcTemplate.queryForObject(countQuery, Long.class);
		Integer totalpages=(int)Math.ceil((double)count/page.getPageSize());  
		page.setTotalPages(totalpages);   
		page.setCurrentPage(page.getOffSet());
		return page;
	}
}
