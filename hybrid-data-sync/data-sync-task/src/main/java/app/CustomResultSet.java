package app;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.config.*;

public class CustomResultSet {
    private Map resultMap = new HashMap();
    private List<ColumnConfig> columnConfigs;
    private ResultSet rs;

    public CustomResultSet(ResultSet rs, List<ColumnConfig> columnConfigs) throws SQLException {
        this.rs = rs;
        this.columnConfigs = columnConfigs;
        fillResultMap();
    }

    private void fillResultMap() throws SQLException {
        for (ColumnConfig columnConfig : columnConfigs) {
            resultMap.put(columnConfig.getSource(), get(columnConfig.getSource(), columnConfig.getType()));
        }
    }

    public boolean next() throws SQLException {
        return rs.next();
    }

    public Object get(String columnLabel, String type) throws SQLException {
        Object value;
        switch (type) {
            case "Integer":
                value = this.rs.getLong(columnLabel);
                break;
            case "String":
                value = this.rs.getString(columnLabel);
                break;
            case "Boolean":
            	value = this.rs.getBoolean(columnLabel);
            	break;
            default:
                throw new SQLException(String.format("Column type: %s is not valid", type));
        }
        return value;
    }

    public Object get(String columnLabel) {
        return resultMap.get(columnLabel);
    }
}
