package org.egov.hr.querybuilder;

public class AttendanceQueryBuilder {

    public static String insertAttendanceQuery() {
        final String query = "INSERT INTO egeis_attendance values "
                + "(nextval('seq_egeis_attendance'),?,?,?,?,?,?,?,?,?,?,?)";
        return query;
    }
}
