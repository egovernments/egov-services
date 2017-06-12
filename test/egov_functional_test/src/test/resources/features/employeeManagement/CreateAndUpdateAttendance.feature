Feature: In this Feature We are going to create and update the attendance for one employee or group of employees.

  Scenario Outline: Create Attendance for an Employee

    Given hrPilot logs in
    And user will select the required screen as "Create/Update"
    And user will enter the details of employee code as <employeeCode>
    And user will enter the attendance details for an employee with status as <status>

    Examples:
    | employeeCode | status  |
    | employee1    | present |


