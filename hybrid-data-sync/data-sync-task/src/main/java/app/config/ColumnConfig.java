package app.config;


import java.util.List;

public class ColumnConfig {
    private String source;
    private String destination;
    private String type;
    private boolean shouldSync;
    private boolean shouldSource;
    private String defaultValue;
    private String query;
    private List<String> queryElements;

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isShouldSync() {
        return shouldSync;
    }

    public void setShouldSync(boolean shouldSync) {
        this.shouldSync = shouldSync;
    }

    public boolean isShouldSource() {
        return shouldSource;
    }

    public void setShouldSource(boolean shouldSource) {
        this.shouldSource = shouldSource;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getQueryElements() {
        return queryElements;
    }

    public void setQueryElements(List<String> queryElements) {
        this.queryElements = queryElements;
    }
}
