Feature: Positive test cases for escalation master


  Scenario: Create escalation

    Given grievanceAdmin on Login screen types on username value narasappa
    And grievanceAdmin on Login screen types on password value demo
    And grievanceAdmin on Login screen clicks on signIn
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value Create/Update Escalation
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on createEscalation screen types on position suggestion box with value ENG_Assistant Engineer_1
    And grievanceAdmin on createEscalation screen clicks on search
    And grievanceAdmin on createEscalation screen selects on grievanceType value Unneccessary Traffic Fines
    And grievanceAdmin on createEscalation screen selects on department value ADMINISTRATION
    And grievanceAdmin on createEscalation screen selects on designation value Senior Assistant
    And grievanceAdmin on createEscalation screen selects on toPosition value ENG_Assistant Engineer_1
    And grievanceAdmin on createEscalation screen clicks on addButton
    And grievanceAdmin on createEscalation screen verifies successMSG has visible value Escalation Created Successfully
    And grievanceAdmin on createEscalation screen clicks on OK
    And grievanceAdmin on createEscalation screen clicks on search
    And grievanceAdmin on createEscalation screen types on searchResultsBox value Unneccessary Traffic Fines
    And grievanceAdmin on createEscalation screen verifies grievanceTypeSearchResults has visible value Unneccessary Traffic Fines
