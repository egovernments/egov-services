Feature: In this feature we will create the no of leave days for an employee.
  Here we are going to create , view and update


  Background: Create an employee

    Given hrAdmin logs in
    And user will select the required screen as "Create Employee" with condition as "/employee"
    And user enters the employee details as employee1
    And user will enter the assignment details as assignment1
    And user will enter the jurisdiction details as JurisdictionList1
    And user will enter the service section and other details
    Then user clicks on submit button
    And current user logs out

    @WIP
  Scenario: We are going to create the leave opening balance in this scenario

    Given hrAdmin logs in
    And user will select the required screen as "Create Leave Opening Balance"
    And user will search the employee to create the leave opening balance
    And user will enter the no of days to an employee