package app.config;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SyncInfo {
    private String sourceTable;
    private String destinationTable;
    private List<ColumnConfig> columns;

    public String getSourceTable() {
        return sourceTable;
    }

    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }

    public List<ColumnConfig> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnConfig> columns) {
        this.columns = columns;
    }

    public String getDestinationTable() {
        return destinationTable;
    }

    public void setDestinationTable(String destinationTable) {
        this.destinationTable = destinationTable;
    }

    public List<String> getSourceColumnNamesToReadFrom() {
        return getSourceColumnConfigsToReadFrom().stream().map(ColumnConfig::getSource).collect(Collectors.toList());
    }

    public List<ColumnConfig> getSourceColumnConfigsToReadFrom() {
        return columns.stream().filter(ColumnConfig::isShouldSource).collect(Collectors.toList());
    }
}
