Feature: Create Grievance Category

  Scenario: Create Grievance Category with valid details

    Given grievanceAdmin on Login screen types on username value narasappa
    And grievanceAdmin on Login screen types on password value demo
    And grievanceAdmin on Login screen clicks on signIn
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


  Scenario: Modify Grievance Category with valid data
    Given grievanceAdmin on Login screen types on username value narasappa
    And grievanceAdmin on Login screen types on password value demo
    And grievanceAdmin on Login screen clicks on signIn
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value Update Category
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on searchCategory screen types on searchCategoryBox value Bangalore City Problems
    And grievanceAdmin on searchCategory screen clicks on categoryList
    And grievanceAdmin on UpdateGrievanceCategory screen types on categoryName value Bangalore City Problems and solution
    And grievanceAdmin on UpdateGrievanceCategory screen verifies code is in display mode
    And grievanceAdmin on UpdateGrievanceCategory screen types on categoryDescription value Bangalore City Problems
    And grievanceAdmin on UpdateGrievanceCategory screen clicks on Update
    And grievanceAdmin on UpdateGrievanceCategory screen verifies successMSG has visible value Success
    And grievanceAdmin on UpdateGrievanceCategory screen clicks on closeButton
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value Search Category
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on searchCategory screen types on searchCategoryBox value Bangalore City Problems and solution
    And grievanceAdmin on searchcategory screen clicks on categoryList
    And grievanceAdmin on viewCategory screen verifies name has visible value Bangalore City Problems and solution
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value Officials Register Grievance
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on Grievance screen clicks on grievanceCategory
    And grievanceAdmin on Grievance screen verifies grievanceCategory has visible value Bangalore City Problems and solution
    And Intent:LogoutIntentTest