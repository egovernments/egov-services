package builders.wcms;

import entities.wcms.ConnectionInfo;

public class ConnectionInfoBuilder {

    ConnectionInfo connectionInfo = new ConnectionInfo();

    public ConnectionInfoBuilder withWaterSourceType(String waterSourceType) {
        connectionInfo.setWaterSourceType(waterSourceType);
        return this;
    }

    public ConnectionInfoBuilder withConnectionType(String connectionType) {
        connectionInfo.setConnectionType(connectionType);
        return this;
    }

    public ConnectionInfoBuilder withPropertyType(String propertyType) {
        connectionInfo.setPropertyType(propertyType);
        return this;
    }

    public ConnectionInfoBuilder withCategory(String category) {
        connectionInfo.setCategory(category);
        return this;
    }

    public ConnectionInfoBuilder withUsageType(String usageType) {
        connectionInfo.setUsageType(usageType);
        return this;
    }

    public ConnectionInfoBuilder withHSCPipeSize(String hscPipeSize) {
        connectionInfo.setHscPipeSize(hscPipeSize);
        return this;
    }

    public ConnectionInfoBuilder withSumpCapacity(String sumpCapacity) {
        connectionInfo.setSumpCapacity(sumpCapacity);
        return this;
    }

    public ConnectionInfoBuilder withNoOfPersons(String noOfPersons) {
        connectionInfo.setNoOfPersons(noOfPersons);
        return this;
    }

    public ConnectionInfoBuilder withWaterSupplyType(String waterSupplyType) {
        connectionInfo.setWaterSupplyType(waterSupplyType);
        return this;
    }

    public ConnectionInfoBuilder withApartmentName(String apartmentName) {
        connectionInfo.setApartmentName(apartmentName);
        return this;
    }

    public ConnectionInfoBuilder withReasonForAdditionalConnection(String reason) {
        connectionInfo.setReasonForAdditionalConnection(reason);
        return this;
    }

    public ConnectionInfo build() {
        return connectionInfo;
    }
}