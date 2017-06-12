package entities.assetManagement;

public class CustomFieldsDetails {

    private String name;
    private String dataType;
    private String regExFormat;
    private boolean isActive;
    private boolean isMandatory;
    private String value;
    private String localText;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getRegExFormat() {
        return regExFormat;
    }

    public void setRegExFormat(String regExFormat) {
        this.regExFormat = regExFormat;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setMandatory(boolean mandatory) {
        isMandatory = mandatory;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLocalText() {
        return localText;
    }

    public void setLocalText(String localText) {
        this.localText = localText;
    }
}
