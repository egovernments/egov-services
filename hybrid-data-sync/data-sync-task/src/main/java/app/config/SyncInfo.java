package app.config;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SyncInfo {
    private String sourceTable;
    private String destinationTable;
    private String sourceTimeZone;

    private List<ColumnConfig> columns;

    public String getSourceTimeZone() {
        return sourceTimeZone;
    }

    public void setSourceTimeZone(String sourceTimeZone) {
        this.sourceTimeZone = sourceTimeZone;
    }

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
        return columns.stream().filter(c -> c.isShouldSource() && !Objects.equals(c.getSource(), "custom-query")).collect(Collectors.toList());
    }
}
