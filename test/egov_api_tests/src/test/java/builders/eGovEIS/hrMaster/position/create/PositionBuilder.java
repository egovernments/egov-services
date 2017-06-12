package builders.eGovEIS.hrMaster.position.create;

import entities.requests.eGovEIS.hrMaster.position.create.Deptdesig;
import entities.requests.eGovEIS.hrMaster.position.create.Position;
import tests.BaseAPITest;

public class PositionBuilder extends BaseAPITest {

    Position position = new Position();
    Deptdesig deptdesig = new DeptdesigBuilder().build();

    public PositionBuilder() {
        position.setDeptdesig(deptdesig);
        position.setName("ACO_" + get3DigitRandomInt());
        position.setIsPostOutsourced(true);
        position.setActive(true);
        position.setTenantId("ap.kurnool");
    }

    public PositionBuilder withIsPostOutsourced(boolean isPostOutsourced) {
        position.setIsPostOutsourced(isPostOutsourced);
        return this;
    }

    public PositionBuilder withDeptdesig(Deptdesig deptdesig) {
        position.setDeptdesig(deptdesig);
        return this;
    }

    public PositionBuilder withName(String name) {
        position.setName(name);
        return this;
    }

    public PositionBuilder withTenantId(String tenantId) {
        position.setTenantId(tenantId);
        return this;
    }

    public PositionBuilder withActive(boolean active) {
        position.setActive(active);
        return this;
    }

    public Position build() {
        return position;
    }
}
