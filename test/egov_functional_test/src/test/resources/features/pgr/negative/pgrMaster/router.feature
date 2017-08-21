Feature: Negative test cases for router

Scenario: Create router field validation
Given grievanceAdmin on Login screen types on username value narasappa
And grievanceAdmin on Login screen types on password value demo
And grievanceAdmin on Login screen clicks on signIn
And grievanceAdmin on home screen clicks on menu
And grievanceAdmin on home screen types on applicationSearchBox value Create Router
And grievanceAdmin on home screen clicks on applicationLink
And grievanceAdmin on createRouter screen types on grievancetype suggestion box with value abc
And grievanceAdmin on createRouter screen verifies grievanceTypeValidationMSG has visible value This field is required
And grievanceAdmin on createRouter screen selects boundaryType with value as Ward
And grievanceAdmin on createRouter screen types on boundary suggestion box with value election ward no 1
And grievanceAdmin on createRouter screen verifies boundaryValidationMSG has visible value This field is required
And grievanceAdmin on createRouter screen types on position suggestion box with value Acc_Senior Account_1
And grievanceAdmin on createRouter screen verifies positionValidationMSG has visible value This field is required


Scenario: Update Router

