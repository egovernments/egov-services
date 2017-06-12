package entities.requests.eGovEIS.hrMaster.position.create;

public class Deptdesig {
    private Designation designation;
    private int department;

    public Designation getDesignation() {
        return this.designation;
    }

    public void setDesignation(Designation designation) {
        this.designation = designation;
    }

    public int getDepartment() {
        return this.department;
    }

    public void setDepartment(int department) {
        this.department = department;
    }
}
