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
    And grievanceAdmin on createReceivingMode screen clicks on activeCheckBox
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


  Scenario: Create receiving mode inactive

      Given grievanceAdmin on Login screen types on username value narasappa
      And grievanceAdmin on Login screen types on password value demo
      And grievanceAdmin on Login screen clicks on signIn
      And grievanceAdmin on home screen clicks on menu
      And grievanceAdmin on home screen types on applicationSearchBox value Create Receiving Mode
      And grievanceAdmin on home screen clicks on applicationLink
      And grievanceAdmin on createReceivingMode screen types on receivingModeName value ReceivingMode
      And grievanceAdmin on createReceivingMode screen types on receivingModeCode value RM2
      And grievanceAdmin on createReceivingMode screen types on receivingModeDescription value ReceivingMode2
      And grievanceAdmin on createReceivingMode screen selects channel with value as WEB
      And grievanceAdmin on createReceivingMode screen clicks on create
      And grievanceAdmin on home screen clicks on menu
      And grievanceAdmin on home screen types on applicationSearchBox value View Receiving Mode
      And grievanceAdmin on home screen clicks on applicationLink
      And grievanceAdmin on viewReceivingMode screen types on searchReceivingMode value ReceivingMode
      And grievanceAdmin on viewReceivingMode screen verifies nameColumn has visible value ReceivingModes
      And grievanceAdmin on home screen clicks on menu
      And grievanceAdmin on home screen types on applicationSearchBox value Officials Register Grievance
      And grievanceAdmin on home screen clicks on applicationLink
      And grievanceAdmin on Grievance screen verifies receivingMode has notvisible value ReceivingMode2
      And Intent:LogoutIntentTest

  Scenario: Modify receving mode with valid data

    Given grievanceAdmin on Login screen types on username value narasappa
    And grievanceAdmin on Login screen types on password value demo
    And grievanceAdmin on Login screen clicks on signIn
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value Modify Receiving Mode
    And grievanceAdmin on home screen clicks on applicationLink
#    Enter new name
    And grievanceAdmin on ModifyReceivingMode screen types on receivingModeName value ReceivingModes
    And grievanceAdmin on ModifyReceivingMode screen types on receivingModeDescription value ReceivingMode
    And grievanceAdmin on ModifyReceivingMode screen clicks on Update
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value View Receiving Mode
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on viewReceivingMode screen types on searchReceivingMode value ReceivingModes
    And grievanceAdmin on viewReceivingMode screen verifies nameColumn has visible value ReceivingModes
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value Officials Register Grievance
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on Grievance screen verifies receivingMode has visible value ReceivingModes
    And Intent:LogoutIntentTest


  Scenario: Modify receiving mode with already existing name

    Given grievanceAdmin on Login screen types on username value narasappa
    And grievanceAdmin on Login screen types on password value demo
    And grievanceAdmin on Login screen clicks on signIn
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value Modify Receiving Mode
    And grievanceAdmin on home screen clicks on applicationLink
#    Enter already existing receving mode name
    And grievanceAdmin on ModifyReceivingMode screen types on receivingModeName value ReceivingModes
    And grievanceAdmin on ModifyReceivingMode screen types on receivingModeDescription value ReceivingMode
    And grievanceAdmin on ModifyReceivingMode screen selects channel with value as WEB
    And grievanceAdmin on ModifyReceivingMode screen clicks on Update
    And grievaanceAdmin on ModifyReceivingMode screen verifies validationMSG has visible value Receiving Mode name already exist
    And Intent:LogoutIntentTest


   Scenario: Modify receving mode to make it inactive

     Given grievanceAdmin on Login screen types on username value narasappa
     And grievanceAdmin on Login screen types on password value demo
     And grievanceAdmin on Login screen clicks on signIn
     And grievanceAdmin on home screen clicks on menu
     And grievanceAdmin on home screen types on applicationSearchBox value Modify Receiving Mode
     And grievanceAdmin on home screen clicks on applicationLink
#     Uncheck the active checkbox
     And grievanceAdmin on ModifyReceivingMode screen clicks on active
     And grievanceAdmin on ModifyReceivingMode screen clicks on Update
     And grievanceAdmin on home screen clicks on menu
     And grievanceAdmin on home screen types on applicationSearchBox value View Receiving Mode
     And grievanceAdmin on home screen clicks on applicationLink
     And grievanceAdmin on viewReceivingMode screen types on searchReceivingMode value ReceivingModes
     And grievanceAdmin on viewReceivingMode screen verifies activeColumn has visible value No
     And grievanceAdmin on home screen clicks on menu
     And grievanceAdmin on home screen types on applicationSearchBox value Officials Register Grievance
     And grievanceAdmin on home screen clicks on applicationLink
     And grievanceAdmin on Grievance screen verifies receivingMode has notvisible value ReceivingMode2
     And Intent:LogoutIntentTest


   Scenario:  Modify receving mode to make it active
     Given grievanceAdmin on Login screen types on username value narasappa
     And grievanceAdmin on Login screen types on password value demo
     And grievanceAdmin on Login screen clicks on signIn
     And grievanceAdmin on home screen clicks on menu
     And grievanceAdmin on home screen types on applicationSearchBox value Modify Receiving Mode
     And grievanceAdmin on home screen clicks on applicationLink
#     Check the active checkbox
     And grievanceAdmin on ModifyReceivingMode screen clicks on active
     And grievanceAdmin on ModifyReceivingMode screen clicks on Update
     And grievanceAdmin on ModifyReceivingMode screen clicks on active
     And grievanceAdmin on ModifyReceivingMode screen clicks on Update
     And grievanceAdmin on home screen clicks on menu
     And grievanceAdmin on home screen types on applicationSearchBox value View Receiving Mode
     And grievanceAdmin on home screen clicks on applicationLink
     And grievanceAdmin on viewReceivingMode screen types on searchReceivingMode value ReceivingModes
     And grievanceAdmin on viewReceivingMode screen verifies activeColumn has visible value Yes
     And grievanceAdmin on home screen clicks on menu
     And grievanceAdmin on home screen types on applicationSearchBox value Officials Register Grievance
     And grievanceAdmin on home screen clicks on applicationLink
     And grievanceAdmin on Grievance screen verifies receivingMode has visible value ReceivingMode2
     And Intent:LogoutIntentTest