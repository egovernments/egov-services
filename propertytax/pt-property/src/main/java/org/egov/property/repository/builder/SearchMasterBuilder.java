package org.egov.property.repository.builder;

import org.json.simple.JSONObject;

/**
 * 
 * @author Prasad
 * 
 * This Class will from the search query for all the master services ,based on the given parameters
 *
 */

public class SearchMasterBuilder {

    @SuppressWarnings("unchecked")
    public static String buildSearchQuery(String tableName,
            String tenantId,
            Integer[] ids,
            String name,
            String nameLocal,
            String code,
            Boolean active,
            Boolean isResidential,
            Integer orderNumber, String category,
            Integer pageSize,
            Integer offSet) {

        StringBuffer searchSql = new StringBuffer();

        searchSql.append("select * from " + tableName + " where tenantId = '" + tenantId + "'");

        if (ids != null && ids.length > 0) {

            String searchIds = "";
            int count = 1;
            for (Integer id : ids) {

                if (count < ids.length)
                    searchIds = searchIds + id + ",";
                else
                    searchIds = searchIds + id;

                count++;
            }
            searchSql.append(" AND id IN (" + searchIds + ")");
        }

        if (code != null && !code.isEmpty())
            searchSql.append(" AND code = '" + code + "'");

        JSONObject dataSearch = new JSONObject();

        if (name != null || nameLocal != null || active != null || isResidential != null || orderNumber != null
                || category != null)
            searchSql.append(" AND data @> '");

        if (name != null && !name.isEmpty())
            dataSearch.put("name", name);

        if (nameLocal != null && !nameLocal.isEmpty())
            dataSearch.put("nameLocal", nameLocal);

        if (active != null)
            dataSearch.put("active", active);

        if (isResidential != null)
            dataSearch.put("isResidential", isResidential);

        if (orderNumber != null)
            dataSearch.put("orderNumber", orderNumber);

        if (category != null && !category.isEmpty())
            dataSearch.put("category", category);

        if (name != null || nameLocal != null || active != null || isResidential != null || orderNumber != null
                || category != null)
            searchSql.append(dataSearch.toJSONString() + "'");

        if (pageSize == null)
            pageSize = 30;
        if (offSet == null)
            offSet = 0;
        searchSql.append("offset " + offSet + " limit " + pageSize);

        return searchSql.toString();

    }

}
