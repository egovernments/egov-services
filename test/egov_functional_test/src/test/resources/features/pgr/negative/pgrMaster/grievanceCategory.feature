Feature: Category Master

Scenario: Create Grievance Category with already existing Name

Given grievanceAdmin on Login screen types on username value narasappa
And grievanceAdmin on Login screen types on password value demo
And grievanceAdmin on Login screen clicks on signIn
And grievanceAdmin on home screen clicks on menu
And grievanceAdmin on home screen types on applicationSearchBox value Create Category
And grievanceAdmin on home screen clicks on applicationLink

And grievanceAdmin on Grievance screen types on categoryName value Bangalore City Problems
And grievanceAdmin on Grievance screen types on categoryCode value BCPS
And grievanceAdmin on Grievance screen types on categoryDescription value Bangalore City Problems
And grievanceAdmin on Grievance screen clicks on create
And grievanceAdmin on Grievance screen verifies validationMSG has visible value Grievance Category name already exists
And Intent:LogoutIntentTest


Scenario:  Create Grievance Category with already existing Code
Given grievanceAdmin on Login screen types on username value narasappa
And grievanceAdmin on Login screen types on password value demo
And grievanceAdmin on Login screen clicks on signIn
And grievanceAdmin on home screen clicks on menu
And grievanceAdmin on home screen types on applicationSearchBox value Create Category
And grievanceAdmin on home screen clicks on applicationLink
And grievanceAdmin on createGrievanceCategory screen types on categoryName value Bangalore City Complaints
And grievanceAdmin on createGrievanceCategory screen types on categoryCode value BCP
And grievanceAdmin on createGrievanceCategory screen types on categoryDescription value Bangalore City Problems
And grievanceAdmin on createGrievanceCategory screen clicks on create
And grievanceAdmin on createGrievanceCategory screen verifies validationMSG has visible value Grievance Category code already exists
And Intent:LogoutIntentTest


Scenario:  Create Grievance Category with already existing Name and Code

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
And grievanceAdmin on createGrievanceCategory screen verifies validationMSG has visible value Combination of name and code already exists
And Intent:LogoutIntentTest


Scenario:  Create Grievance Category screen field validations

Given grievanceAdmin on Login screen types on username value narasappa
And grievanceAdmin on Login screen types on password value demo
And grievanceAdmin on Login screen clicks on signIn
And grievanceAdmin on home screen clicks on menu
And grievanceAdmin on home screen types on applicationSearchBox value Create Category
And grievanceAdmin on home screen clicks on applicationLink
And grievanceAdmin on createGrievanceCategory screen types on categoryName value @
And grievanceAdmin on createGrievanceCategory screen verifies nameValidationMSG has visible value Please use only alphabets, space and special characters
And grievanceAdmin on createGrievanceCategory screen types on categoryCode value bc
And grievanceAdmin on createGrievanceCategory screen verifies codeValidationMSG has visible value Please use only upper case alphabets and numbers
And Intent:LogoutIntentTest



Scenario: Modify Grievance Category with already existing name
Given grievanceAdmin on Login screen types on username value narasappa
And grievanceAdmin on Login screen types on password value demo
And grievanceAdmin on Login screen clicks on signIn
And grievanceAdmin on home screen clicks on menu
And grievanceAdmin on home screen types on applicationSearchBox value Update Category
And grievanceAdmin on home screen clicks on applicationLink
And grievanceAdmin on searchCategory screen types on searchCategoryBox value Bangalore City Problems
And grievanceAdmin on searchCategory screen clicks on categoryList
And grievanceAdmin on UpdateGrievanceCategory screen types on categoryName value Street Lighting
And grievanceAdmin on UpdateGrievanceCategory screen verifies code is in display mode
And grievanceAdmin on UpdateGrievanceCategory screen types on categoryDescription value Bangalore City Problems
And grievanceAdmin on UpdateGrievanceCategory screen clicks on Update
And grievanceAdmin on UpdateGrievanceCategory screen verifies validationMSG has visible value Grievance Category name already exists
And Intent:LogoutIntentTest


  Scenario: Modify Grievance Category field validation
Given grievanceAdmin on Login screen types on username value narasappa
And grievanceAdmin on Login screen types on password value demo
And grievanceAdmin on Login screen clicks on signIn
And grievanceAdmin on home screen clicks on menu
And grievanceAdmin on home screen types on applicationSearchBox value Update Category
And grievanceAdmin on home screen clicks on applicationLink
And grievanceAdmin on searchCategory screen types on searchCategoryBox value Bangalore City Problems
And grievanceAdmin on searchCategory screen clicks on categoryList
And grievanceAdmin on UpdateGrievanceCategory screen types on categoryName value Bangalore City Problems @
And grievanceAdmin on UpdateGrievanceCategory screen verifies nameValidationMSG has visible value Please use only alphabets, space and special characters
And Intent:LogoutIntentTest
