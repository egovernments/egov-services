package builders.eGovEIS.employeeMaster.leaveType.create;

import entities.requests.eGovEIS.employeeMaster.leaveType.create.LeaveType;

public final class LeaveTypeBuilder {

    LeaveType leaveType = new LeaveType();

    public LeaveTypeBuilder withEncashable(boolean encashable) {
        leaveType.setEncashable(encashable);
        return this;
    }

    public LeaveTypeBuilder withCreatedDate(String createdDate) {
        leaveType.setCreatedDate(createdDate);
        return this;
    }

    public LeaveTypeBuilder withAccumulative(boolean accumulative) {
        leaveType.setAccumulative(accumulative);
        return this;
    }

    public LeaveTypeBuilder withCreatedBy(String createdBy) {
        leaveType.setCreatedBy(createdBy);
        return this;
    }

    public LeaveTypeBuilder withLastModifiedDate(String lastModifiedDate) {
        leaveType.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public LeaveTypeBuilder withLastModifiedBy(String lastModifiedBy) {
        leaveType.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public LeaveTypeBuilder withName(String name) {
        leaveType.setName(name);
        return this;
    }

    public LeaveTypeBuilder withTenantId(String tenantId) {
        leaveType.setTenantId(tenantId);
        return this;
    }

    public LeaveTypeBuilder withDescription(String description) {
        leaveType.setDescription(description);
        return this;
    }

    public LeaveTypeBuilder withPayEligible(boolean payEligible) {
        leaveType.setPayEligible(payEligible);
        return this;
    }

    public LeaveTypeBuilder withActive(boolean active) {
        leaveType.setActive(active);
        return this;
    }

    public LeaveTypeBuilder withHalfdayAllowed(boolean halfdayAllowed) {
        leaveType.setHalfdayAllowed(halfdayAllowed);
        return this;
    }

    public LeaveType build() {
        return leaveType;
    }
}
