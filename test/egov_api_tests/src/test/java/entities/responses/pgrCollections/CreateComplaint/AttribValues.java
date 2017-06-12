package entities.responses.pgrCollections.CreateComplaint;

import org.codehaus.jackson.annotate.JsonProperty;

public class AttribValues {

    @JsonProperty("name")
    private String name;

    @JsonProperty("key")
    private String key;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
