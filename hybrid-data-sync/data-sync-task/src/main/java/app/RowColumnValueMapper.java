package app;

import app.config.ColumnConfig;
import app.config.SyncInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;
import java.util.stream.Collectors;

public class RowColumnValueMapper {
    private Map<String, String> colValMap = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(RowColumnValueMapper.class);

    public RowColumnValueMapper(SyncInfo syncInfo, CustomResultSet rs, JdbcTemplate jdbcTemplate) {
        for (ColumnConfig column : syncInfo.getColumns()) {
            if (column.isShouldSync()) {
                String value;
                if (column.isShouldSource()) {
                    if (Objects.equals(column.getSource(), "custom-query")) {
                        List<Object> query_elements = column.getQueryElements().stream().map(c -> String.format("%s", rs.get(c))).collect(Collectors.toList());
                        List<Map<String, Object>> res = jdbcTemplate.queryForList(
                                column.getQuery(), query_elements.toArray());
                        value = String.format("%s", res.get(0).values().toArray()[0]);
                    } else {
                        value = String.format("%s", rs.get(column.getSource()));
                    }
                } else {
                    value = column.getDefaultValue();
                }
                if (!Objects.equals(value, "null")) {
                    value = String.format("'%s'", value);
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
