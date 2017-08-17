Feature: Create Grievance Category

  Scenario: Create Grievance Category with valid details

    Given user on Login screen types on username value 7777777777
    And user on Login screen types on password value eGov@123
    And user on Login screen clicks on signIn
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value Create Category
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on createGrievanceCategory screen types on categoryName value Bangalore City Problems
    And grievanceAdmin on createGrievanceCategory screen types on categoryCode value BCP
    And grievanceAdmin on createGrievanceCategory screen types on categoryDescription value Bangalore City Problems
    And grievanceAdmin on createGrievanceCategory screen clicks on create
    And grievanceAdmin on createGrievanceCategory screen verifies successMSG has visible value Success
    And grievanceAdmin on createGrievanceCategory screen clicks on closeButton
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value Search Category
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on searchCategory screen types on searchCategoryBox value Bangalore City Problems
    And grievanceAdmin on searchcategory screen clicks on categoryList
    And grievanceAdmin on viewCategory screen verifies name has visible value Bangalore City Problems
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value Officials Register Grievance
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on Grievance screen clicks on grievanceCategory
    And grievanceAdmin on Grievance screen verifies grievanceCategory has visible value Bangalore City Problems
    And Intent:LogoutIntentTest
