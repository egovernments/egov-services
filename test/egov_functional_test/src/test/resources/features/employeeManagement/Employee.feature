Feature: Create/View/Update

  As a registered user of a system
  i should able to create/view/update employee

  Scenario Outline: Create an employee

    Given hrAdmin logs in
    And user will select the required screen as "Create Employee" with condition as "/employee"
    And user enters the employee details as <employeeDetails>
    And user will enter the assignment details as <assignmentDetails>
    And user will enter the jurisdiction details as <jurisdictionDetails>
    And user will enter the service section and other details
    Then user clicks on submit button

    And user will select the required screen as "View Employee"
    And user will enter the employee search details

    And user will select the required screen as "Update Employee"
    And user will enter the employee search details for updating the employee information
    And user will update the employee details
    Then user clicks on submit button
    And current user logs out

#    Then user close the employee search
#    And user will select the required screen as "Create LeaveBalance"

    Examples:
      | employeeDetails | assignmentDetails | jurisdictionDetails |
      | employee1       | assignment1       | JurisdictionList1   |
      | employee2       | assignment2       | JurisdictionList2   |
      | employee3       | assignment3       | JurisdictionList3   |
      | employee4       | assignment4       | JurisdictionList4   |
#      | employee5       | assignment5       | JurisdictionList5   |


  Scenario Outline: Create employee fields validation test

    Given hrAdmin logs in
    And user will select the required screen as "Create Employee" with condition as "/employee"
    And user test the fields validation for employee details
    And user will enter the assignment details as <assignmentDetails>
    And user test the fields validation for assisgnment details
    And user will enter the jurisdiction details as <jurisdictionDetails>
    And user test the fields validation for service and other details

    Examples:
      | assignmentDetails | jurisdictionDetails |
      | assignment3       | JurisdictionList3   |