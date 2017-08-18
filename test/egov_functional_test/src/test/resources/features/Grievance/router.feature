Feature: Routing of complaint to particular employee

  Scenario: Create router

    Given grievanceAdmin on Login screen types on username value narasappa
    And grievanceAdmin on Login screen types on password value demo
    And grievanceAdmin on Login screen clicks on signIn
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value Create Router
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on createRouter clicks on grievanceType
    