package org.egov.common;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.model.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

import static org.springframework.util.StringUtils.isEmpty;

@Repository
public abstract class JdbcRepository {
    public static final Map<String, List<String>> allInsertFields = new HashMap<String, List<String>>();
    public static final Map<String, List<String>> allUpdateFields = new HashMap<String, List<String>>();
    public static final Map<String, List<String>> allIdentitiferFields = new HashMap<String, List<String>>();
    public static final Map<String, String> allInsertQuery = new HashMap<>();
    public static final Map<String, String> allUpdateQuery = new HashMap<>();
    public static final Map<String, String> allSearchQuery = new HashMap<>();
    public static final Map<String, String> getByIdQuery = new HashMap<>();
    private static final Logger LOG = LoggerFactory.getLogger(JdbcRepository.class);
    @Autowired
    public JdbcTemplate jdbcTemplate;
    @Autowired
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

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

    public static String insertQuery(List<String> fields, String tableName, List<String> uniqueFields) {
        String iQuery = "insert into :tableName (:fields) values (:params) ";
        StringBuilder fieldNames = new StringBuilder();
        StringBuilder paramNames = new StringBuilder();
        String persister = new String();
        StringBuilder persisterQ = new StringBuilder();
        int i = 0;
        for (String s : fields) {
            if (i > 0) {
                fieldNames.append(", ");
                paramNames.append(", ");
                persisterQ.append(", ");
            }
            fieldNames.append(s);
            paramNames.append(":").append(s);
            persisterQ.append("?");
            i++;
        }

        for (String s : uniqueFields) {
            if (i > 0) {
                fieldNames.append(", ");
                paramNames.append(", ");
                persisterQ.append(", ");
            }
            fieldNames.append(s);
            paramNames.append(":").append(s);
            persisterQ.append("?");

            i++;
        }

        // System.out.println(fields);
        //System.out.println(uniqueFields);
        persister = iQuery.toString();

        persister = persister.replace(":fields", fieldNames.toString()).replace(":params", persisterQ)
                .replace(":tableName", tableName).toString();
        System.out.println("persister   " + persister);
        iQuery = iQuery.replace(":fields", fieldNames.toString()).replace(":params", paramNames.toString())
                .replace(":tableName", tableName).toString();
        System.out.println(tableName + "----" + iQuery);
        return iQuery;
    }

    public static List<String> fetchFields(Class ob) {
        List<String> fields = new ArrayList<>();
        for (Field f : ob.getDeclaredFields()) {
            if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
                continue;
            }

            if (f.getType().getSimpleName().equals(AuditDetails.class.getSimpleName())) {
                for (Field ff : AuditDetails.class.getDeclaredFields()) {

                    if (java.lang.reflect.Modifier.isStatic(ff.getModifiers())) {
                        continue;
                    }
                    if (ff.getName().equalsIgnoreCase("deleteReason")) {
                        continue;
                    }
                    fields.add(f.getName());
                }
            }

            fields.add(f.getName());
        }


        return fields;
    }

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

    /**
     * this api is not used
     *
     * @param tableName
     * @param obj
     * @return
     */
    public static String getBasicSearchQuery(String tableName, Object obj) {
        String uQuery = "select * from  :tableName where  :searchFields ";
        StringBuilder fieldNameAndParams = new StringBuilder();
        // StringBuilder uniqueFieldNameAndParams = new StringBuilder();
        int i = 0;

        for (String s : allInsertFields.get("StoreEntity")) {
            if (i > 0) {
                fieldNameAndParams.append(" and ");

            }
            try {
                Field declaredField = getField(obj, s);
                if (getValue(declaredField, obj) != null)

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

        uQuery = uQuery.replace(":searchFields", fieldNameAndParams.toString()).replace(":tableName", tableName)
                .toString();
        return uQuery;
    }

    public static Object getValue(Field declaredField, Object obj) {

        Object ob1 = obj;
        Object val = null;
        while (ob1 != null) {
            try {
                val = declaredField.get(obj);
                break;
            } catch (Exception e) {
                if (ob1.getClass().getSuperclass() != null) {
                    ob1 = ob1.getClass().getSuperclass();
                } else {
                    break;
                }

            }

        }
        return val;

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

    public Map<String, Object> paramValues(Object ob, List<String> fields) {
        Map<String, Object> paramValues = new LinkedHashMap<>();

        for (String s : fields) {
            Field f = null;

            try {
                f = getField(ob, s);
            } catch (Exception e) {
            }
            /*
             * try { f = ob.getClass().getSuperclass().getDeclaredField(s); } catch (NoSuchFieldException e1) {
             * System.out.println( "Unable to find the field in this class and its super class for field" + s); } }
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

    @Transactional
    public Object create(Object ob) {
        // System.out.println(allInsertQuery);
        /*((AuditableEntity) ob).setCreatedDate(new Date());
        ((AuditableEntity) ob).setLastModifiedDate(new Date());*/

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

    @Transactional
    public Object update(Object ob) {
        System.out.println(allUpdateQuery);
      /*  ((AuditableEntity) ob).setCreatedDate(new Date());
        ((AuditableEntity) ob).setLastModifiedDate(new Date());*/

        String obName = ob.getClass().getSimpleName();
        List<Map<String, Object>> batchValues = new ArrayList<>();
        batchValues.add(paramValues(ob, allUpdateFields.get(obName)));
        batchValues.get(0).putAll(paramValues(ob, allIdentitiferFields.get(obName)));
        System.out.println(obName + "----" + allUpdateQuery.get(obName));
        namedParameterJdbcTemplate.batchUpdate(allUpdateQuery.get(obName),
                batchValues.toArray(new Map[batchValues.size()]));
        return ob;
    }

    @Transactional
    public void delete(String tableName, String id) {
        String delQuery = "delete from " + tableName + " where id = '" + id + "'";
        jdbcTemplate.execute(delQuery);
    }


    @Transactional
    public void updateQuantity(String tableName, String id, String tenantId, HashMap<String, BigDecimal> columns, String operation) {
        String updateQuery = null;

        if (operation.equalsIgnoreCase("ADD")) {
            updateQuery = "update " + tableName + " set " + addColumn(columns, "+") + " where id = '" + id + "' and tenantid = " + tenantId;
        }

        if (operation.equalsIgnoreCase("SUBTRACT")) {
            updateQuery = "update " + tableName + " set " + addColumn(columns, "-") + " where id = '" + "' and tenantid = " + tenantId;
        }

        try {
            jdbcTemplate.execute(updateQuery);
        } catch (DataIntegrityViolationException ex) {
            throw new CustomException("error", ex.getMessage());
        } catch (Exception e) {
            throw new CustomException("error", e.getMessage());
        }
    }

    private String addColumn(HashMap<String, BigDecimal> columns, String sign) {
        Iterator iterator = columns.entrySet().iterator();
        StringBuilder addedColumns = new StringBuilder();
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator.next();
            addedColumns.append(" " + pair.getKey() + " = " + pair.getKey() + sign + pair.getValue() + ",");
        }

        String totalQuery = addedColumns.toString();
        return totalQuery.substring(0, totalQuery.length() - 1);
    }

    public String getSequence(String seqName) {
        String seqQuery = "select nextval('" + seqName + "')";
        return String.valueOf(jdbcTemplate.queryForObject(seqQuery, Long.class) + 1);
    }

    public String getSequence(Object ob) {

        String seqQuery = "select nextval('seq_" + ob.getClass().getSimpleName() + "')";
        return String.valueOf(jdbcTemplate.queryForObject(seqQuery, Long.class) + 1);
    }

    /**
     * Pass the object you want to persist.
     *
     * @param ob
     * @param count
     * @return
     */
    public List<String> getSequence(Object ob, int count) {

        String seqQuery = "select nextval('seq_" + ob.getClass().getSimpleName() + "')  FROM GENERATE_SERIES(1," + count + ")";
        List<String> ids = jdbcTemplate.queryForList(seqQuery, String.class);
        return ids;
    }

    public List<String> getSequence(String className, int count) {

        String seqQuery = "select nextval('seq_" + className + "')  FROM GENERATE_SERIES(1," + count + ")";
        List<String> ids = jdbcTemplate.queryForList(seqQuery, String.class);
        return ids;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createSequence(String seqName) {
        String seqQuery = "create sequence " + seqName + "";
        jdbcTemplate.execute(seqQuery);
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
        InvalidDataException invalidDataException = new InvalidDataException();
        if (sortBy.contains(",")) {
            sortByList = Arrays.asList(sortBy.split(","));
        } else {
            sortByList = Arrays.asList(sortBy);
        }
        for (String s : sortByList) {
            if (s.contains(" ")
                    && (!s.toLowerCase().trim().endsWith("asc") && !s.toLowerCase().trim().endsWith("desc"))) {
                invalidDataException.setFieldName(s.split(" ")[0]);
                invalidDataException
                        .setMessageKey("Please send the proper sortBy order for the field " + s.split(" ")[0]);
                throw invalidDataException;
            }
        }

    }

    public void validateEntityFieldName(String sortBy, final Class<?> object) {
        InvalidDataException invalidDataException = new InvalidDataException();
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
                invalidDataException.setFieldName(s.contains(" ") ? s.split(" ")[0] : s);
                invalidDataException.setMessageKey("Please send the proper Field Names ");
                throw invalidDataException;
            }
        }

    }

    public Boolean uniqueCheck(String fieldName, Object ob) {
        LOG.info("Unique Checking for field " + fieldName);

        String obName = ob.getClass().getSimpleName();
        List<String> identifierFields = allIdentitiferFields.get(obName);
        List<Map<String, Object>> batchValues = new ArrayList<>();

        // batchValues.get(0).putAll(paramValues(ob, allIdentitiferFields.get(obName)));
        Map<String, Object> paramValues = new HashMap<>();
        String table = "";
        try {
            table = FieldUtils.readDeclaredField(ob, "TABLE_NAME").toString();
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Not able to get Table_name from entity" + obName);
        }
        StringBuffer uniqueQuery = new StringBuffer(
                "select count(*) as count from " + table + " where " + fieldName + "=:fieldValue");
        paramValues.put("fieldValue", getValue(getField(ob, fieldName), ob));
        int i = 0;
        for (String s : identifierFields) {

            if (s.equalsIgnoreCase("tenantId")) {
                uniqueQuery.append(" and ");
                uniqueQuery.append(s).append("=").append(":").append(s);
                // implement fallback here
                paramValues.put(s, getValue(getField(ob, s), ob));
                continue;
            }
            if (getValue(getField(ob, s), ob) != null) {
                uniqueQuery.append(" and ");
                uniqueQuery.append(s).append("!=").append(":").append(s);
                paramValues.put(s, getValue(getField(ob, s), ob));
            }
        }

        Long count = namedParameterJdbcTemplate.queryForObject(uniqueQuery.toString(), paramValues, Long.class);
        LOG.info("Record Count for  field " + count);
        return count >= 1 ? false : true;

    }

    public Boolean uniqueCheck(String firstFieldName, String secondFieldName, Object ob) {
        LOG.info("Unique Checking for combination of fields " + firstFieldName + " & " + secondFieldName);

        String obName = ob.getClass().getSimpleName();
        List<String> identifierFields = allIdentitiferFields.get(obName);
        List<Map<String, Object>> batchValues = new ArrayList<>();

        // batchValues.get(0).putAll(paramValues(ob, allIdentitiferFields.get(obName)));
        Map<String, Object> paramValues = new HashMap<>();
        String table = "";
        try {
            table = FieldUtils.readDeclaredField(ob, "TABLE_NAME").toString();
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Not able to get Table_name from entity" + obName);
        }
        StringBuffer uniqueQuery = new StringBuffer("select count(*) as count from " + table + " where " + firstFieldName
                + "=:firstFieldValue" + " and " + secondFieldName + "=:secondFieldValue");
        paramValues.put("firstFieldValue", getValue(getField(ob, firstFieldName), ob));
        paramValues.put("secondFieldValue", getValue(getField(ob, secondFieldName), ob));
        int i = 0;
        for (String s : identifierFields) {

            if (s.equalsIgnoreCase("tenantId")) {
                uniqueQuery.append(" and ");
                uniqueQuery.append(s).append("=").append(":").append(s);
                // implement fallback here
                paramValues.put(s, getValue(getField(ob, s), ob));
                continue;
            }
            if (getValue(getField(ob, s), ob) != null) {
                uniqueQuery.append(" and ");
                uniqueQuery.append(s).append("!=").append(":").append(s);
                paramValues.put(s, getValue(getField(ob, s), ob));
            }
        }

        Long count = namedParameterJdbcTemplate.queryForObject(uniqueQuery.toString(), paramValues, Long.class);
        LOG.info("Record Count for combination of fields " + count);
        return count >= 1 ? false : true;

    }

    /**
     * @param ids
     * @param tenantId
     * @param tableName
     * @param parentPkFieldName
     * @param parentPkValue
     */
    public void markDeleted(List<String> ids, String tenantId, String tableName, String parentPkFieldName, String parentPkValue) {
        Map<String, Object> paramValues = new HashMap<>();
        String query = "update " + tableName + " set deleted=true where id not in (:ids) and tenantId=:tenantId and "
                + parentPkFieldName + "=:parentPkValue";
        paramValues.put("ids", ids);
        paramValues.put("tenantId", tenantId);
        paramValues.put("parentPkValue", parentPkValue);

        namedParameterJdbcTemplate.update(query.toString(), paramValues);
    }

    public void delete(Object entity, String reason) {

        String backupTable = "egf_deletedtxn";

        String obName = entity.getClass().getSimpleName();
        List<String> identifierFields = allIdentitiferFields.get(obName);
        List<Map<String, Object>> batchValues = new ArrayList<>();

        batchValues.add(paramValues(entity, allIdentitiferFields.get(obName)));
        Map<String, Object> paramValues = new LinkedHashMap<>();
        Collection<Object> values = batchValues.get(0).values();
        for (Object value : values) {
            if (value == null)
                throw new RuntimeException("id field is null . Delete cannot be performed");
        }

        String table = "";
        try {
            table = FieldUtils.readDeclaredField(entity, "TABLE_NAME").toString();
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Not able to get Table_name from entity" + obName);
        }
        paramValues.put("tablename", table);
        paramValues.put("reason", reason);

        batchValues.get(0).putAll(paramValues);
        StringBuffer backupQuery = new StringBuffer();
        StringBuffer deleteQuery = new StringBuffer();
        backupQuery.append(
                "insert into " + backupTable + " select '1',:tablename,id,tenantid,:reason,row_to_json(" + table + "),now() "
                        + " from " + table + " where tenantid=:tenantId and id=:id ");
        System.out.println("query.............." + backupQuery);
        namedParameterJdbcTemplate.batchUpdate(backupQuery.toString(), batchValues.toArray(new Map[batchValues.size()]));
        deleteQuery.append("delete from  " + table + " where ");
        int i = 0;
        for (String s : identifierFields) {
            if (i != 0)
                deleteQuery.append(" and ");
            if (s.equalsIgnoreCase("tenantId")) {

                deleteQuery.append(s).append("=").append(":").append(s);
                // implement fallback here
                paramValues.put(s, getValue(getField(entity, s), entity));
                continue;
            }
            if (getValue(getField(entity, s), entity) != null) {

                deleteQuery.append(s).append("=").append(":").append(s);
                paramValues.put(s, getValue(getField(entity, s), entity));
            }
            i++;
        }

        batchValues.get(0).putAll(paramValues);

        namedParameterJdbcTemplate.batchUpdate(deleteQuery.toString(), batchValues.toArray(new Map[batchValues.size()]));

        // paramValues.put("fieldValue", getValue(getField(ob,fieldName ), ob));

    }


    public Object findById(Object entity, String entityName) {
        List<String> list = allIdentitiferFields.get(entityName);

        Map<String, Object> paramValues = new HashMap<>();

        for (String s : list) {
            paramValues.put(s, getValue(getField(entity, s), entity));
        }

        List<Object> indents = namedParameterJdbcTemplate.query(
                getByIdQuery.get(entityName), paramValues,
                new BeanPropertyRowMapper(entity.getClass()));
        if (indents.isEmpty()) {
            return null;
        } else {
            return indents.get(0);
        }

    }

    public Object findByCode(Object entity, String entityName) {
        List<String> list = new ArrayList();
        list.add("code");
        list.add("tenantId");

        Map<String, Object> paramValues = new HashMap<>();

        for (String s : list) {
            paramValues.put(s, getValue(getField(entity, s), entity));
        }

        List<Object> indents = namedParameterJdbcTemplate.query(
                "select * from " + entityName + " where code=:code and tenantid=:tenantId ", paramValues,
                new BeanPropertyRowMapper(entity.getClass()));
        if (indents.isEmpty()) {
            return null;
        } else {
            return indents.get(0);
        }

    }

    @Transactional
    public int changeStatus(Object ob, String status, String tableName, String columnName) {
        StringBuilder updateQuery = new StringBuilder();
        Map<String, Object> paramValues = new HashMap<>();
        try {
            String obName = ob.getClass().getSimpleName();
            List<String> identifierFields = allIdentitiferFields.get(obName);
            if (!status.isEmpty()) {
                updateQuery.append("UPDATE " + tableName + " set " + columnName + "='" + status.toString() + "'");
                for (String s : identifierFields) {
                    if (s.equalsIgnoreCase("tenantId")) {
                        updateQuery.append(" and ");
                        updateQuery.append(s).append("=:").append(s);
                        // implement fallback here
                        paramValues.put(s, getValue(getField(ob, s), ob));
                        continue;
                    }
                    if (getValue(getField(ob, s), ob) != null) {
                        updateQuery.append(" where ");
                        updateQuery.append(s).append("=:").append(s);
                        paramValues.put(s, getValue(getField(ob, s), ob));
                    }
                }

            }
            int count = namedParameterJdbcTemplate.update(updateQuery.toString(), paramValues);
            return count;
        } catch (DataIntegrityViolationException ex) {
            throw new CustomException("error", ex.getMessage());
        } catch (Exception e) {
            throw new CustomException("error", e.getMessage());
        }
    }


    @Transactional
    public int updateColumn(Object ob, String tableName, HashMap<String, String> columns, String condition) {
        StringBuilder updateQuery = new StringBuilder();
        Map<String, Object> paramValues = new HashMap<>();
        try {
            String obName = ob.getClass().getSimpleName();
            List<String> identifierFields = allIdentitiferFields.get(obName);
            updateQuery.append("UPDATE " + tableName + " set ");

            if (columns.size() > 0) {
                String query = "";
                Iterator iterator = columns.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry pair = (Map.Entry) iterator.next();
                    query = query + (pair.getKey() + " = " + pair.getValue() + ",");
                }
                updateQuery.append(query.substring(0, query.length() - 1));
            }

            if (isEmpty(condition)) {
                for (String s : identifierFields) {
                    if (s.equalsIgnoreCase("tenantId")) {
                        updateQuery.append(" and ");
                        updateQuery.append(s).append("=:").append(s);
                        // implement fallback here
                        paramValues.put(s, getValue(getField(ob, s), ob));
                        continue;
                    }
                    if (getValue(getField(ob, s), ob) != null) {
                        updateQuery.append(" where ");
                        updateQuery.append(s).append("=:").append(s);
                        paramValues.put(s, getValue(getField(ob, s), ob));
                    }
                }
            } else {
                updateQuery.append(condition);
            }

            int count = namedParameterJdbcTemplate.update(updateQuery.toString(), paramValues);
            return count;
        } catch (DataIntegrityViolationException ex) {
            throw new CustomException("error", ex.getMessage());
        } catch (Exception e) {
            throw new CustomException("error", e.getMessage());
        }
    }
}