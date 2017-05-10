package app;

import app.config.ColumnConfig;
import app.config.SyncInfo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.*;
import java.util.stream.Collectors;

public class RowColumnValueMapper {
    private Map<String, String> colValMap = new HashMap<>();

    public RowColumnValueMapper(SyncInfo syncInfo, CustomResultSet rs, JdbcTemplate jdbcTemplate) {
        for (ColumnConfig column : syncInfo.getColumns()) {
            if (column.isShouldSync()) {
                String value;
                if (column.isShouldSource()) {
                    if (Objects.equals(column.getSource(), "custom-query")) {
                        List<String> query_elements = Arrays.stream(column.getQueryElements().split(",")).map(c -> String.format("%s", rs.get(c))).collect(Collectors.toList());

                        SqlRowSet res = jdbcTemplate.queryForRowSet(
                                column.getQuery(), query_elements);
                        value = res.getString(0);
                    } else {
                        value = String.format("%s", rs.get(column.getSource()));
                    }
                    if (!Objects.equals(value, "null")) {
                        value = String.format("'%s'", rs.get(column.getSource()));
                    }
                } else {
                    value = column.getDefaultValue();
                }
                colValMap.put(column.getDestination(), value);
            }
        }
    }

    public Map<String, String> getMap() {
        return colValMap;
    }

    public List<String> getCommaSeparatedColumnNames() {
        List<String> colNames = new ArrayList<>();
        for (String colName : colValMap.keySet()) {
            colNames.add(colName);
        }
        return colNames;
    }

    public List<String> getCommaSeparatedColumnValues() {
        List<String> colVals = new ArrayList<>();
        for (String colName : getCommaSeparatedColumnNames()) {
            colVals.add(String.format("%s", colValMap.get(colName)));
        }
        return colVals;
    }

    public List<String> getColumnNameValuePairForUpdate() {
        List<String> colValPair = new ArrayList<>();
        for (String colName : getCommaSeparatedColumnNames()) {
            colValPair.add(String.format("%s = %s", colName, colValMap.get(colName)));
        }
        return colValPair;
    }
}
