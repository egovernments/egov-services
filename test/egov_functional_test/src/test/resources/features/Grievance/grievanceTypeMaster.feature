
Feature: Grievance Type Creation

  Scenario: Create Grievance Type with valid details

    Given user on Login screen types on username value 7777777777
    And user on Login screen types on password value eGov@123
    And user on Login screen clicks on signIn
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value Create Grievance Type
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on createGrievanceType screen types on grievanceTypeName value Unneccessary Traffic Fines
    And grievanceAdmin on createGrievanceType screen types on grievanceTypeCode value UTF
    And grievanceAdmin on createGrievanceType screen types on grievanceTypeDescription value Unneccessary Traffic Fines
    And grievanceAdmin on createGrievanceType screen types on grievanceTypeSLAHours value 1
    And user on createGrievanceType screen selects grievanceCategory with value as Bangalore City Problems