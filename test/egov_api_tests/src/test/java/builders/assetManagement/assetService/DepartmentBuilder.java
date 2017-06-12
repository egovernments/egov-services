package builders.assetManagement.assetService;

import entities.responses.assetManagement.assetService.Department;

public final class DepartmentBuilder {

    Department department = new Department();

    public DepartmentBuilder() {
        department.setId(2);
        department.setName(null);
        department.setCode(null);
    }

    public DepartmentBuilder withCode(String code) {
        department.setCode(code);
        return this;
    }

    public DepartmentBuilder withName(String name) {
        department.setName(name);
        return this;
    }

    public DepartmentBuilder withId(int id) {
        department.setId(id);
        return this;
    }

    public Department build() {
        return department;
    }
}
