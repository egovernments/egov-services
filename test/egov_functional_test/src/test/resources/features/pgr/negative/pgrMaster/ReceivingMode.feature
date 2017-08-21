Feature: Negative scenarios for receiving mode master

  Scenario: Create receiving mode with already existing name

    Given grievanceAdmin on Login screen types on username value narasappa
    And grievanceAdmin on Login screen types on password value demo
    And grievanceAdmin on Login screen clicks on signIn
    And grievanceAdmin on Home screen clicks on menu
    And grievanceAdmin on Home screen types on applicationSearchBox value Create Receiving Mode
    And grievanceAdmin on Home screen clicks on applicationLink
  #      Enter already existing name
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
#      Enter already existing code
    And grievanceAdmin on createReceivingMode screen types on receivingModeCode value RM1
    And grievanceAdmin on createReceivingMode screen types on receivingModeDescription value ReceivingMode1
    And grievanceAdmin on createReceivingMode screen selects channel with value as WEB
    And grievanceAdmin on createReceivingMode screen clicks on create
    And grievanceAdmin on craeteReceivingMode screen verifies validationMSG has visible value Receiving Mode code already exist
    And Intent:LogoutIntentTest


  Scenario: Create receiving mode with already existing name and code

    Given grievanceAdmin on Login screen types on username value narasappa
    And grievanceAdmin on Login screen types on password value demo
    And grievanceAdmin on Login screen clicks on signIn
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value Create Receiving Mode
    And grievanceAdmin on home screen clicks on applicationLink
#    Enter already existing name
    And grievanceAdmin on createReceivingMode screen types on receivingModeName value ReceivingMode2
#    Enter already existing code
    And grievanceAdmin on createReceivingMode screen types on receivingModeCode value RM1
    And grievanceAdmin on createReceivingMode screen types on receivingModeDescription value ReceivingMode1
    And grievanceAdmin on createReceivingMode screen selects channel with value as WEB
    And grievanceAdmin on createReceivingMode screen clicks on create
    And grievanceAdmin on craeteReceivingMode screen verifies validationMSG has visible value Combination of name and code already exists
    And Intent:LogoutIntentTest


  Scenario: Receiving Mode screen field validation

    Given grievanceAdmin on Login screen types on username value narasappa
    And grievanceAdmin on Login screen types on password value demo
    And grievanceAdmin on Login screen clicks on signIn
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value Create Receiving Mode
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on createReceivingMode screen types on receivingModeName value @
    And grievanceAdmin on createReceivingMode screen verifies receivingModeNameValidationMSG has visible value Please use only alphabets, space and special characters
    And grievanceAdmin on createReceivingMode screen types on receivingModeCode value @
    And grievanceAdmin on createReceivingMode screen verifies receivingModeCodeValidationMSG has visible value Please use only upper case alphabets and numbers
    And grievanceAdmin on createReceivingMode screen types on receivingModeDescription value ReceivingMode2
    And grievanceAdmin on createReceivingMode screen selects channel with value as WEB
    And grievanceAdmin on createReceivingMode screen clicks on activeCheckBox
    And Intent:LogoutIntentTest


