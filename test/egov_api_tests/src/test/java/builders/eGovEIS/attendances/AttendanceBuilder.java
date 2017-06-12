package builders.eGovEIS.attendances;

import entities.requests.eGovEIS.attendances.Attendance;
import entities.requests.eGovEIS.attendances.Type;

public class AttendanceBuilder {

    Attendance attendance = new Attendance();
    Type type = new TypeBuilder().build();

    public AttendanceBuilder() {
        attendance.setTenantId("1");
        attendance.setAttendanceDate("01/03/2017");
        attendance.setMonth("3");
        attendance.setRemarks("Present");
        attendance.setYear("2017");
        attendance.setType(type);
        attendance.setEmployee("4");
    }

    public AttendanceBuilder withTenantId(String tenantId) {
        attendance.setTenantId(tenantId);
        return this;
    }

    public AttendanceBuilder withAttendanceDate(String attendanceDate) {
        attendance.setAttendanceDate(attendanceDate);
        return this;
    }

    public AttendanceBuilder withMonth(String month) {
        attendance.setMonth(month);
        return this;
    }

    public AttendanceBuilder withRemarks(String remarks) {
        attendance.setRemarks(remarks);
        return this;
    }

    public AttendanceBuilder withYear(String year) {
        attendance.setYear(year);
        return this;
    }

    public AttendanceBuilder withType(Type type) {
        attendance.setType(type);
        return this;
    }

    public AttendanceBuilder withEmployee(String employee) {
        attendance.setEmployee(employee);
        return this;
    }

    public Attendance build() {
        return attendance;
    }
}