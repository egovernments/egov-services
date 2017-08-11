Feature: Sample Test From PGR

  @Custom
  Scenario: Create Complaint
    Given Intent:LoginIntentTest
    And user on home screen clicks on menu
    And user on home screen types on applicationSearchBox value official
    And user on home screen clicks on applicationLink
    And user on grievance screen select on receivingMode value Letters
    And Intent:LogoutIntentTest

  @Custom
  Scenario: Create Complaint using Citizen portal
    Given Intent:LoginIntentTest
    And user on home screen clicks on NewGrievance
    And user on Grievance screen clicks on grievanceCategory
    And user on Grievance screen types on grievanceDetails value kurnoolCity
    And user on Grievance screen clicks on create
    And user on Grievance screen copies the complaintNum to applicationNumber
    And user on Grievance screen clicks on view
    And user on Grievance screen types on commentBox value Created
    And user on Grievance screen clicks on submitButton
    And user on Grievance screen verifies successBox has visible value Grievance updated succesfully
    And user on Grievance screen clicks on Ok
    And user on Grievance screen clicks on homeButton
    And user on home screen verifies egov has visible value Kurnool
    And Intent:LogoutIntentTest
    And Intent:LoginIntentTest
    And user on home screen types on applicationSearch with above applicationNumber