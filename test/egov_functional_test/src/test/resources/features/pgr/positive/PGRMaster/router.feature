Feature: Routing of complaint to particular employee

  Scenario: Create router

    Given grievanceAdmin on Login screen types on username value narasappa
    And grievanceAdmin on Login screen types on password value demo
    And grievanceAdmin on Login screen clicks on signIn
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value Create Router
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on createRouter screen types on grievancetype suggestion box with value Unneccessary Traffic Fines
    And grievanceAdmin on createRouter screen selects boundaryType with value as Ward
    And grievanceAdmin on createRouter screen types on boundary suggestion box with value election ward no 1
    And grievanceAdmin on createRouter screen types on position suggestion box with value Acc_Senior Account_1
    And grievanceAdmin on createRouter screen clicks on create
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value View Router
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on viewRouter screen types on grievanceType suggestion box with value Unneccessary Traffic Fines
    And grievanceAdmin on viewRouter screen clicks on search
    And grievanceAdmin on viewRouter screen verifies grievanceTypeList has visible value Unneccessary Traffic Fines
    And Intent:LogoutIntentTest