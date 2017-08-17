package org.egov.tradelicense.common.persistense.repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.egov.tradelicense.common.util.TimeStampUtil;
import org.egov.tradelicense.persistence.entity.TradeLicenseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public abstract class JdbcRepository {

	@Autowired
	public JdbcTemplate jdbcTemplate;

	@Autowired
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public static final Map<String, List<String>> allInsertFields = new HashMap<String, List<String>>();
	public static final Map<String, String> allInsertQuery = new HashMap<>();
	public static final Map<String, List<String>> allIdentitiferFields = new HashMap<String, List<String>>();
	public static final Map<String, List<String>> allUpdateFields = new HashMap<String, List<String>>();
	public static final Map<String, String> allUpdateQuery = new HashMap<>();
	public static final Map<String, String> getByIdQuery = new HashMap<>();

	public static synchronized void init(Class T) {
		String TABLE_NAME = "";

		List<String> insertFields = new ArrayList<>();
		List<String> uniqueFields = new ArrayList<>();
		List<String> updateFields = new ArrayList<>();

		String insertQuery = "";
		String updateQuery = "";

		try {

			TABLE_NAME = (String) T.getDeclaredField("TABLE_NAME").get(null);
		} catch (Exception e) {

		}
		insertFields.addAll(fetchFields(T));
		uniqueFields.add("id");
		// uniqueFields.add("tenantId");
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

	public static List<String> fetchFields(Class ob) {
		List<String> fields = new ArrayList<>();
		for (Field f : ob.getDeclaredFields()) {
			if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
				continue;
			}
			fields.add(f.getName());
		}

		return fields;
	}

	public static String insertQuery(List<String> fields, String tableName, List<String> uniqueFields) {
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
		System.out.println(tableName + "----" + iQuery);
		return iQuery;
	}

	@Transactional
	public Object create(Object ob) {
		// System.out.println(allInsertQuery);
		/*
		 * ((AuditableEntity) ob).setCreatedDate(new Date()); ((AuditableEntity)
		 * ob).setLastModifiedDate(new Date());
		 */

		String obName = ob.getClass().getSimpleName();
		List<Map<String, Object>> batchValues = new ArrayList<>();
		batchValues.add(paramValues(ob, allInsertFields.get(obName)));
		batchValues.get(0).putAll(paramValues(ob, allIdentitiferFields.get(obName)));
		System.out.println(obName + "----" + allInsertQuery.get(obName));
		System.out.println(namedParameterJdbcTemplate);
		namedParameterJdbcTemplate.batchUpdate(allInsertQuery.get(obName),
				batchValues.toArray(new Map[batchValues.size()]));
		return ob;
	}

	public Map<String, Object> paramValues(Object ob, List<String> fields) {
		Map<String, Object> paramValues = new LinkedHashMap<>();

		for (String s : fields) {
			Field f = null;

			try {
				f = getField(ob, s);
			} catch (Exception e) {
			}
			/*
			 * try { f = ob.getClass().getSuperclass().getDeclaredField(s); }
			 * catch (NoSuchFieldException e1) { System.out.println(
			 * "Unable to find the field in this class and its super class for field"
			 * + s); } }
			 */
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

	public static Field getField(Object obj, String s) {
		System.out.println(s);
		Field declaredField = null;
		Object ob1 = obj;
		while (declaredField == null) {
			try {
				declaredField = ob1.getClass().getDeclaredField(s);
			} catch (Exception e) {
				try {
					declaredField = ob1.getClass().getSuperclass().getDeclaredField(s);
				} catch (Exception e1) {
					break;
				}
			}

		}
		if (declaredField != null) {
			declaredField.setAccessible(true);
		}
		return declaredField;
	}

	public String getSequence(String seqName) {
		String seqQuery = "select nextval('" + seqName + "')";
		return String.valueOf(jdbcTemplate.queryForObject(seqQuery, Long.class) + 1);
	}

	/*
	 * public String getSequence(String seqName) { String seqQuery =
	 * "select nextval('" + seqName + "')"; return
	 * String.valueOf(jdbcTemplate.queryForObject(seqQuery, Long.class) + 1); }
	 * 
	 * public TradeLicenseEntity create(TradeLicenseEntity entity) {
	 * 
	 * final String INSERT_LICENSE_QUERY = "INSERT INTO egtl_license" +
	 * "( id, tenantId, applicationType, applicationNumber," +
	 * " licenseNumber, applicationDate, adhaarNumber, mobileNumber , ownerName, fatherSpouseName, emailId,"
	 * +
	 * " ownerAddress, propertyAssesmentNo, localityId, revenueWardId, tradeAddress, ownerShipType, tradeTitle,"
	 * +
	 * " tradeType, categoryId, subCategoryId, uomId, quantity, remarks, tradeCommencementDate, agreementDate,"
	 * +
	 * " agreementNo, isLegacy, active, expiryDate, createdBy, lastModifiedBy, createdTime, lastModifiedTime )"
	 * +
	 * "  VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
	 * ;
	 * 
	 * return entity; }
	 */

	public static String updateQuery(List<String> fields, String tableName, List<String> uniqueFields) {
		String uQuery = "update :tableName set :fields where  :uniqueField ";
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
		i = 0;
		for (String s : uniqueFields) {
			if (i > 0) {
				uniqueFieldNameAndParams.append(" and ");

			}
			uniqueFieldNameAndParams.append(s).append("=").append(":").append(s);
			i++;
		}

		uQuery = uQuery.replace(":fields", fieldNameAndParams.toString())
				.replace(":uniqueField", uniqueFieldNameAndParams.toString()).replace(":tableName", tableName)
				.toString();
		return uQuery;
	}

	public static String getByIdQuery(String tableName, List<String> uniqueFields) {
		String uQuery = "select * from  :tableName where  :uniqueField ";
		// StringBuilder fieldNameAndParams = new StringBuilder();
		StringBuilder uniqueFieldNameAndParams = new StringBuilder();
		int i = 0;

		for (String s : uniqueFields) {
			if (i > 0) {
				uniqueFieldNameAndParams.append(" and ");

			}
			uniqueFieldNameAndParams.append(s).append("=").append(":").append(s);
			i++;
		}

		uQuery = uQuery.replace(":uniqueField", uniqueFieldNameAndParams.toString()).replace(":tableName", tableName)
				.toString();
		return uQuery;
	}

	public List<TradeLicenseEntity> executeSearchQuery(String query, List<Object> preparedStatementValues) {

		List<TradeLicenseEntity> tradeLicenses = new ArrayList<TradeLicenseEntity>();
		try {
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, preparedStatementValues.toArray());

			for (Map<String, Object> row : rows) {

				TradeLicenseEntity license = new TradeLicenseEntity();
				license.setId(getLong(row.get("id")));
				license.setTenantId(getString(row.get("tenantId")));
				license.setApplicationType(getString(row.get("applicationType")));
				license.setApplicationDate(TimeStampUtil.getTimeStampFromDB(getString(row.get("applicationDate"))));
				license.setApplicationNumber(getString(row.get("applicationNumber")));
				license.setLicenseNumber(getString(row.get("licenseNumber")));
				license.setOldLicenseNumber(getString(row.get("oldLicenseNumber")));
				license.setAdhaarNumber(getString(row.get("adhaarNumber")));
				license.setMobileNumber(getString(row.get("mobileNumber")));
				license.setOwnerName(getString(row.get("ownerName")));
				license.setFatherSpouseName(getString(row.get("fatherSpouseName")));
				license.setEmailId(getString(row.get("emailId")));
				license.setOwnerAddress(getString(row.get("ownerAddress")));
				license.setPropertyAssesmentNo(getString(row.get("propertyAssesmentNo")));
				license.setLocalityId(Integer.valueOf(getString((row.get("localityId")))));
				license.setRevenueWardId(Integer.valueOf((getString(row.get("revenueWardId")))));
				license.setAdminWardId(Integer.valueOf((getString(row.get("revenueWardId")))));
				license.setStatus(Long.valueOf((getString(row.get("status")))));
				license.setTradeAddress(getString(row.get("tradeAddress")));
				license.setOwnerShipType(getString(row.get("ownerShipType")));
				license.setTradeTitle(getString(row.get("tradeTitle")));
				license.setTradeType((getString(row.get("tradeType"))));
				license.setCategoryId(getLong(row.get("categoryId")));
				license.setSubCategoryId(getLong(row.get("subCategoryId")));
				license.setUomId(getLong(row.get("uomId")));
				license.setQuantity(getDouble(row.get("quantity")));
				license.setRemarks(getString(row.get("remarks")));
				license.setTradeCommencementDate(
						TimeStampUtil.getTimeStampFromDB(getString(row.get("tradeCommencementDate"))));
				license.setLicenseValidFromDate(
						TimeStampUtil.getTimeStampFromDB(getString(row.get("licenseValidFromDate"))));
				license.setAgreementDate(TimeStampUtil.getTimeStampFromDB(getString(row.get("agreementDate"))));
				license.setAgreementNo(getString(row.get("agreementNo")));
				license.setIsLegacy(getBoolean(row.get("isLegacy")));
				license.setActive(getBoolean(row.get("active")));
				license.setExpiryDate(TimeStampUtil.getTimeStampFromDB(getString(row.get("expiryDate"))));
				license.setCreatedBy(getString(row.get("createdBy")));
				license.setLastModifiedBy(getString(row.get("lastModifiedBy")));
				license.setLastModifiedTime(getLong(row.get("lastModifiedTime")));
				license.setCreatedTime(getLong(row.get("createdTime")));

				tradeLicenses.add(license);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tradeLicenses;
	}

	/**
	 * This method will cast the given object to String
	 * 
	 * @param object
	 *            that need to be cast to string
	 * @return {@link String}
	 */
	private String getString(Object object) {
		return object == null ? "" : object.toString();
	}

	/**
	 * This method will cast the given object to Long
	 * 
	 * @param object
	 *            that need to be cast to Long
	 * @return {@link Long}
	 */
	private Long getLong(Object object) {
		return object == null ? 0 : Long.parseLong(object.toString());
	}

	/**
	 * This method will cast the given object to double
	 * 
	 * @param object
	 *            that need to be cast to Double
	 * @return {@link Double}
	 */
	@SuppressWarnings("unused")
	private Double getDouble(Object object) {
		return object == null ? 0.0 : Double.parseDouble(object.toString());
	}

	/**
	 * This method will cast the given object to Boolean
	 * 
	 * @param object
	 *            that need to be cast to Boolean
	 * @return {@link boolean}
	 */
	private Boolean getBoolean(Object object) {
		return object == null ? Boolean.FALSE : (Boolean) object;
	}

}
