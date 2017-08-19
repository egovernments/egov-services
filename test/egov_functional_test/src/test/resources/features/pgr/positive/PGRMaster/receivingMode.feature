Feature: Create Receiving Mode

  Scenario: Create receiving mode with valid data

    Given grievanceAdmin on Login screen types on username value narasappa
    And grievanceAdmin on Login screen types on password value demo
    And grievanceAdmin on Login screen clicks on signIn
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value Create Receiving Mode
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on createReceivingMode screen types on receivingModeName value ReceivingMode1
    And grievanceAdmin on createReceivingMode screen types on receivingModeCode value RM1
    And grievanceAdmin on createReceivingMode screen types on receivingModeDescription value ReceivingMode1
    And grievanceAdmin on createReceivingMode screen selects channel with value as WEB
    And grievanceAdmin on createReceivingMode screen clicks on create
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value View Receiving Mode
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on viewReceivingMode screen types on searchReceivingMode value ReceivingMode1
    And grievanceAdmin on viewReceivingMode screen verifies nameColumn has visible value ReceivingMode1
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value Officials Register Grievance
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on Grievance screen verifies receivingMode has visible value ReceivingMode1
    And Intent:LogoutIntentTest


    Scenario: Create receiving mode with already existing name

      Given grievanceAdmin on Login screen types on username value narasappa
      And grievanceAdmin on Login screen types on password value demo
      And grievanceAdmin on Login screen clicks on signIn
      And grievanceAdmin on home screen clicks on menu
      And grievanceAdmin on home screen types on applicationSearchBox value Create Receiving Mode
      And grievanceAdmin on home screen clicks on applicationLink
      And grievanceAdmin on createReceivingMode screen types on receivingModeName value ReceivingMode1
      And grievanceAdmin on createReceivingMode screen types on receivingModeCode value RM12
      And grievanceAdmin on createReceivingMode screen types on receivingModeDescription value ReceivingMode1
      And grievanceAdmin on createReceivingMode screen selects channel with value as WEB
      And grievanceAdmin on createReceivingMode screen clicks on create
      And grievanceAdmin on craeteReceivingMode screen verifies validationMSG has visible value Receiving Mode name already exist
      And Intent:LogoutIntentTest

    Scenario: Create receiving mode with already existig code

      Given grievanceAdmin on Login screen types on username value narasappa
      And grievanceAdmin on Login screen types on password value demo
      And grievanceAdmin on Login screen clicks on signIn
      And grievanceAdmin on home screen clicks on menu
      And grievanceAdmin on home screen types on applicationSearchBox value Create Receiving Mode
      And grievanceAdmin on home screen clicks on applicationLink
      And grievanceAdmin on createReceivingMode screen types on receivingModeName value ReceivingMode2
      And grievanceAdmin on createReceivingMode screen types on receivingModeCode value RM1
      And grievanceAdmin on createReceivingMode screen types on receivingModeDescription value ReceivingMode1
      And grievanceAdmin on createReceivingMode screen selects channel with value as WEB
      And grievanceAdmin on createReceivingMode screen clicks on create
      And grievanceAdmin on craeteReceivingMode screen verifies validationMSG has visible value Receiving Mode code already exist
      And Intent:LogoutIntentTest


    Scenario: create receiving mode inactive

      Given grievanceAdmin on Login screen types on username value narasappa
      And grievanceAdmin on Login screen types on password value demo
      And grievanceAdmin on Login screen clicks on signIn
      And grievanceAdmin on home screen clicks on menu
      And grievanceAdmin on home screen types on applicationSearchBox value Create Receiving Mode
      And grievanceAdmin on home screen clicks on applicationLink
      And grievanceAdmin on home screen clicks on applicationLink
      And grievanceAdmin on createReceivingMode screen types on receivingModeName value ReceivingMode2
      And grievanceAdmin on createReceivingMode screen types on receivingModeCode value RM2
      And grievanceAdmin on createReceivingMode screen types on receivingModeDescription value ReceivingMode2
      And grievanceAdmin on createReceivingMode screen selects channel with value as WEB
      And grievanceAdmin on createReceivingMode screen clicks on activeCheckBox
      And grievanceAdmin on createReceivingMode screen clicks on create
      And grievanceAdmin on home screen clicks on menu
      And grievanceAdmin on home screen types on applicationSearchBox value View Receiving Mode
      And grievanceAdmin on home screen clicks on applicationLink
      And grievanceAdmin on viewReceivingMode screen types on searchReceivingMode value ReceivingMode2
      And grievanceAdmin on viewReceivingMode screen verifies nameColumn has visible value ReceivingMode2
      And grievanceAdmin on home screen clicks on menu
      And grievanceAdmin on home screen types on applicationSearchBox value Officials Register Grievance
      And grievanceAdmin on home screen clicks on applicationLink
      And grievanceAdmin on Grievance screen verifies receivingMode has notvisible value ReceivingMode2
      And Intent:LogoutIntentTest