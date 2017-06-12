package builders.eGovEIS.employee;


import entities.requests.eGovEIS.employee.Hod;

public class HodBuilder {

    Hod hod = new Hod();

    public HodBuilder() {
    }

    public HodBuilder withDepartment(int department) {
        hod.setDepartment(department);
        return this;
    }

    public Hod build() {
        return hod;
    }
}
