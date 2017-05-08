package app;

import app.config.ColumnConfig;
import app.config.SyncInfo;

import java.util.*;

public class RowColumnValueMapper {
    private Map<String, String> colValMap = new HashMap<>();

    public RowColumnValueMapper(SyncInfo syncInfo, CustomResultSet rs) {
        for (ColumnConfig column : syncInfo.getColumns()) {
            if (column.isShouldSync()) {
                String value;
                if (column.isShouldSource()) {
                    value = String.format("%s", rs.get(column.getSource()));
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

    public Set<String> getCommaSeparatedColumnNames() {
        return colValMap.keySet();
    }

    public List<String> getCommaSeparatedColumnValues() {
        List<String> colVals = new ArrayList<>();
        for (String colName : getCommaSeparatedColumnNames()) {
            colVals.add(String.format("'%s'", colValMap.get(colName)));
        }
        return colVals;
    }

    public List<String> getColumnNameValuePairForUpdate() {
        List<String> colValPair = new ArrayList<>();
        for (String colName : getCommaSeparatedColumnNames()) {
            colValPair.add(String.format("%s = '%s'", colName, colValMap.get(colName)));
        }
        return colValPair;
    }
}
