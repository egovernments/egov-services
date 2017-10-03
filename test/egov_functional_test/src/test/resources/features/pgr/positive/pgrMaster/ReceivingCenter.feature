Feature: Create receiving center

  Scenario: Create inactive receiving Center with valid data
    Given grievanceAdmin on Login screen types on username value narasappa
    And grievanceAdmin on Login screen types on password value demo
    And grievanceAdmin on Login screen clicks on signIn
    And grievanceAdmin on Home screen clicks on menu
    And grievanceAdmin on Home screen types on menuSearch value Create Receiving Center
    And grievanceAdmin on Home screen clicks on firstMenuItem
    And grievanceAdmin on ReceivingCenter screen types on receivingCenterName value Commissioner Office
    And grievanceAdmin on ReceivingCenter screen types on receivingCenterCode value CO1
    And grievanceAdmin on ReceivingCenter screen types on recevingCenterDescription value Commissioner Office
    And grievanceAdmin on ReceivingCenter screen types on receivingCenterOrderNo value 10
    And grievanceAdmin on ReceivingCenter screen clicks on Create
    And grievanceAdmin on ReceivingCenter screen verifies successMSG has visible value Success
    And grievanceAdmin on ReceivingCenter screen clicks on CLOSE
    And grievanceAdmin on Home screen clicks on menu
    And grievanceAdmin on Home screen types on menuSearch value View Receiving Center
    And grievanceAdmin on Home screen clicks on firstMenuItem
    And grievanceAdmin on ReceivingCenterView screen types on receivingCenterSearchBox value Commissioner Office
    And grievanceAdmin on ReceivingCenterView screen verifies nameColumn has visible value Commissioner Office
    And grievanceAdmin on Home screen clicks on menu
    And grievanceAdmin on Home screen types on menuSearch value Officials Register Grievance
    And grievanceAdmin on Home screen clicks on firstMenuItem
    And grievanceAdmin on Grievance screen selects on receivingMode value Manual
    And grievanceAdmin on Grievance screen verifies receivingCenter has notvisible value Commissioner Office
    And Intent:LogoutIntentTest



  Scenario: Create Active Receiving Center with valid data

    Given grievanceAdmin on Login screen types on username value narasappa
    And grievanceAdmin on Login screen types on password value demo
    And grievanceAdmin on Login screen clicks on signIn
    And grievanceAdmin on Home screen clicks on menu
    And grievanceAdmin on Home screen types on menuSearch value Create Receiving Center
    And grievanceAdmin on Home screen clicks on firstMenuItem
    And grievanceAdmin on ReceivingCenter screen types on receivingCenterName value Assistant Commissioner Office
    And grievanceAdmin on ReceivingCenter screen types on receivingCenterCode value ACO1
    And grievanceAdmin on ReceivingCenter screen types on recevingCenterDescription value Assistant Commissioner Office
    And grievanceAdmin on ReceivingCenter screen types on receivingCenterOrderNo value 10
    And grievanceAdmin on ReceivingCenter screen clicks on ActiveButton
    And grievanceAdmin on ReceivingCenter screen clicks on Create
    And grievanceAdmin on ReceivingCenter screen verifies successMSG has visible value Success
    And grievanceAdmin on ReceivingCenter screen clicks on CLOSE
    And grievanceAdmin on Home screen clicks on menu
    And grievanceAdmin on Home screen types on menuSearch value View Receiving Center
    And grievanceAdmin on Home screen clicks on firstMenuItem
    And grievanceAdmin on ReceivingCenterView screen types on receivingCenterSearchBox value Assistant Commissioner Office
    And grievanceAdmin on ReceivingCenterView screen verifies nameColumn has visible value Assistant Commissioner Office
    And grievanceAdmin on Home screen clicks on menu
    And grievanceAdmin on Home screen types on menuSearch value Officials Register Grievance
    And grievanceAdmin on Home screen clicks on firstMenuItem
    And grievanceAdmin on Grievance screen selects receivingMode with value as Manual
    And grievanceAdmin on Grievance screen verifies receivingCenter has visible value Assistant Commissioner Office
    And Intent:LogoutIntentTest


  Scenario: Create receiving center with already existing name

    Given grievanceAdmin on Login screen types on username value narasappa
    And grievanceAdmin on Login screen types on password value demo
    And grievanceAdmin on Login screen clicks on signIn
    And grievanceAdmin on Home screen clicks on menu
    And grievanceAdmin on Home screen types on menuSearch value Create Receiving Center
    And grievanceAdmin on Home screen clicks on firstMenuItem
#    Enter already existing name
    And grievanceAdmin on ReceivingCenter screen types on receivingCenterName value Assistant Commissioner Office
    And grievanceAdmin on ReceivingCenter screen types on receivingCenterCode value ACO1
    And grievanceAdmin on ReceivingCenter screen types on recevingCenterDescription value Assistant Commissioner Office
    And grievanceAdmin on ReceivingCenter screen types on receivingCenterOrderNo value 10
    And grievanceAdmin on ReceivingCenter screen clicks on ActiveButton
    And grievanceAdmin on ReceivingCenter screen clicks on Create
    And grievanceAdmin on ReceivingCenter screen verifies validationMSG has visible value Receiving Center name already exist

  Scenario: Create receiving center with already existing code

    Given grievanceAdmin on Login screen types on username value narasappa
    And grievanceAdmin on Login screen types on password value demo
    And grievanceAdmin on Login screen clicks on signIn
    And grievanceAdmin on Home screen clicks on menu
    And grievanceAdmin on Home screen types on menuSearch value Create Receiving Center
    And grievanceAdmin on Home screen clicks on firstMenuItem
    And grievanceAdmin on ReceivingCenter screen types on receivingCenterName value Assistant Commissioner Office
#    Enter already existing code
    And grievanceAdmin on ReceivingCenter screen types on receivingCenterCode value ACO1
    And grievanceAdmin on ReceivingCenter screen types on recevingCenterDescription value Assistant Commissioner Office
    And grievanceAdmin on ReceivingCenter screen types on receivingCenterOrderNo value 10
    And grievanceAdmin on ReceivingCenter screen clicks on ActiveButton
    And grievanceAdmin on ReceivingCenter screen clicks on Create
    And grievanceAdmin on ReceivingCenter screen verifies validationMSG has visible value Receiving Center code already exists

  Scenario: Create receiving center with already existing name and code

    Given grievanceAdmin on Login screen types on username value narasappa
    And grievanceAdmin on Login screen types on password value demo
    And grievanceAdmin on Login screen clicks on signIn
    And grievanceAdmin on Home screen clicks on menu
    And grievanceAdmin on Home screen types on menuSearch value Create Receiving Center
    And grievanceAdmin on Home screen clicks on firstMenuItem
#    Enter already existing name
    And grievanceAdmin on ReceivingCenter screen types on receivingCenterName value Assistant Commissioner Office
#    Enter already existing code
    And grievanceAdmin on ReceivingCenter screen types on receivingCenterCode value ACO1
    And grievanceAdmin on ReceivingCenter screen types on recevingCenterDescription value Assistant Commissioner Office
    And grievanceAdmin on ReceivingCenter screen types on receivingCenterOrderNo value 10
    And grievanceAdmin on ReceivingCenter screen clicks on ActiveButton
    And grievanceAdmin on ReceivingCenter screen clicks on Create
    And grievanceAdmin on ReceivingCenter screen verifies validationMSG has visible value Combination of code and name already exists

  Scenario: Create receiving center with active CRN field

    Given grievanceAdmin on Login screen types on username value narasappa
    And grievanceAdmin on Login screen types on password value demo
    And grievanceAdmin on Login screen clicks on signIn
    And grievanceAdmin on Home screen clicks on menu
    And grievanceAdmin on Home screen types on menuSearch value Create Receiving Center
    And grievanceAdmin on Home screen clicks on firstMenuItem
    And grievanceAdmin on ReceivingCenter screen types on receivingCenterName value Assistant Commissioner Office
    And grievanceAdmin on ReceivingCenter screen types on receivingCenterCode value ACO1
    And grievanceAdmin on ReceivingCenter screen types on recevingCenterDescription value Assistant Commissioner Office
    And grievanceAdmin on ReceivingCenter screen types on receivingCenterOrderNo value 10
    And grievanceAdmin on ReceivingCenter screen clicks on ActiveButton
    And grievanceAdmin on ReceivingCenter screen clicks on Create
    And grievanceAdmin on ReceivingCenter screen verifies successMSG has visible value Success
    And grievanceAdmin on ReceivingCenter screen clicks on CLOSE
    And grievanceAdmin on Home screen clicks on menu
    And grievanceAdmin on Home screen types on menuSearch value View Receiving Center
    And grievanceAdmin on Home screen clicks on firstMenuItem
    And grievanceAdmin on ReceivingCenterView screen types on receivingCenterSearchBox value Assistant Commissioner Office
    And grievanceAdmin on ReceivingCenterView screen verifies nameColumn has visible value Assistant Commissioner Office
    And grievanceAdmin on ReceivingCenterView screen verifies CRNActive has visible value Yes
    And grievanceAdmin on Home screen clicks on menu
    And grievanceAdmin on Home screen types on menuSearch value Officials Register Grievance
    And grievanceAdmin on Home screen clicks on firstMenuItem
    And grievanceAdmin on Grievance screen selects receivingMode with value as Manual
    And grievanceAdmin on Grievance screen selects receivingCenter with value as Assistant Commissioner Office
#    And grievanceAdmin on Grievance screen verifies CRNField visible
    And Intent:LogoutIntentTest


  Scenario: Create receiving center with inactive CRN field

    Given grievanceAdmin on Login screen types on username value narasappa
    And grievanceAdmin on Login screen types on password value demo
    And grievanceAdmin on Login screen clicks on signIn
    And grievanceAdmin on Home screen clicks on menu
    And grievanceAdmin on Home screen types on menuSearch value Create Receiving Center
    And grievanceAdmin on Home screen clicks on firstMenuItem
    And grievanceAdmin on ReceivingCenter screen types on receivingCenterName value Assistant Commissioner Office
    And grievanceAdmin on ReceivingCenter screen types on receivingCenterCode value ACO1
    And grievanceAdmin on ReceivingCenter screen types on recevingCenterDescription value Assistant Commissioner Office
    And grievanceAdmin on ReceivingCenter screen types on receivingCenterOrderNo value 10
    And grievanceAdmin on ReceivingCenter screen clicks on ActiveButton
    And grievanceAdmin on ReceivingCenter screen clicks on Create
    And grievanceAdmin on ReceivingCenter screen verifies successMSG has visible value Success
    And grievanceAdmin on ReceivingCenter screen clicks on CLOSE
    And grievanceAdmin on Home screen clicks on menu
    And grievanceAdmin on Home screen types on menuSearch value View Receiving Center
    And grievanceAdmin on Home screen clicks on firstMenuItem
    And grievanceAdmin on ReceivingCenterView screen types on receivingCenterSearchBox value Assistant Commissioner Office
    And grievanceAdmin on ReceivingCenterView screen verifies nameColumn has visible value Assistant Commissioner Office
    And grievanceAdmin on ReceivingCenterView screen verifies CRNActive has visible value No
    And grievanceAdmin on Home screen clicks on menu
    And grievanceAdmin on Home screen types on menuSearch value Officials Register Grievance
    And grievanceAdmin on Home screen clicks on firstMenuItem
    And grievanceAdmin on Grievance screen selects receivingMode with value as Manual
    And grievanceAdmin on Grievance screen selects receivingCenter with value as Assistant Commissioner Office
#    And grievanceAdmin on Grievance screen verifies CRNField notvisible
    And Intent:LogoutIntentTest


  Scenario: Create receiving center screen field validation
    Given grievanceAdmin on Login screen types on username value narasappa
    And grievanceAdmin on Login screen types on password value demo
    And grievanceAdmin on Login screen clicks on signIn
    And grievanceAdmin on Home screen clicks on menu
    And grievanceAdmin on Home screen types on menuSearch value Create Receiving Center
    And grievanceAdmin on Home screen clicks on firstMenuItem
    And grievanceAdmin on ReceivingCenter screen types on receivingCenterName value @
    And grievanceAdmin on ReceivingCenter screen verifies nameValidationMSG has visible value Please use only alphabets, space and special characters
    And grievanceAdmin on ReceivingCenter screen types on receivingCenterCode value a
    And grievanceAdmin on ReceivingCenter screen verifies codeValidationMSG has visible value Please use only upper case alphabets and number
    And grievanceAdmin on ReceivingCenter screen types on receivingCenterOrderNo value 10
    And grievanceAdmin on ReceivingCenter screen verifies OrderNoValidationMSG has visible value Please use only numbers
#    And grievanceAdmin on ReceivingCenter screen verifies Create notvisible


  Scenario: Modify receiving center to make it active

    Given grievanceAdmin on Login screen types on username value narasappa
    And grievanceAdmin on Login screen types on password value demo
    And grievanceAdmin on Login screen clicks on signIn
    And grievanceAdmin on Home screen clicks on menu
    And grievanceAdmin on Home screen types on menuSearch value Update Receiving Center
    And grievanceAdmin on Home screen clicks on firstMenuItem
    And grievanceAdmin on UpdateReceivingCenter screen types on UpdateSearchBox suggestion box with value Assistant Commissioner Office
    And grievanceAdmin on UpdateReceivingCenter screen clicks on ReceivingCenterName
    And grievanceAdmin on ReceivingCenter screen clicks on ActiveButton
    And grievanceAdmin on ReceivingCenter screen clicks on Update
    And grievanceAdmin on ReceivingCenter screen verifies successMSG has visible value Success
    And grievanceAdmin on ReceivingCenter screen clicks on CLOSE
    And grievanceAdmin on Home screen clicks on menu
    And grievanceAdmin on Home screen types on menuSearch value View Receiving Center
    And grievanceAdmin on Home screen clicks on firstMenuItem
    And grievanceAdmin on ReceivingCenterView screen types on receivingCenterSearchBox value Assistant Commissioner Office
    And grievanceAdmin on ReceivingCenterView screen verifies nameColumn has visible value Assistant Commissioner Office
    And grievanceAdmin on ReceivingCenterView screen verifies CRNActive has visible value Yes
    And grievanceAdmin on Home screen clicks on menu
    And grievanceAdmin on Home screen types on menuSearch value Officials Register Grievance
    And grievanceAdmin on Home screen clicks on firstMenuItem
    And grievanceAdmin on Grievance screen selects receivingMode with value as Manual
    And grievanceAdmin on Grievance screen selects receivingCenter with value as Assistant Commissioner Office
#    And grievanceAdmin on Grievance screen verifies CRNField visible
    And Intent:LogoutIntentTest


  Scenario: Modify receiving center to make it inactive
    Given grievanceAdmin on Login screen types on username value narasappa
    And grievanceAdmin on Login screen types on password value demo
    And grievanceAdmin on Login screen clicks on signIn
    And grievanceAdmin on Home screen clicks on menu
    And grievanceAdmin on Home screen types on menuSearch value Update Receiving Center
    And grievanceAdmin on Home screen clicks on firstMenuItem
    And grievanceAdmin on UpdateReceivingCenter screen types on UpdateSearchBox suggestion box with value Assistant Commissioner Office
    And grievanceAdmin on UpdateReceivingCenter screen clicks on ReceivingCenterName
    And grievanceAdmin on ReceivingCenter screen clicks on ActiveButton
    And grievanceAdmin on ReceivingCenter screen clicks on Update
    And grievanceAdmin on ReceivingCenter screen verifies successMSG has visible value Success
    And grievanceAdmin on ReceivingCenter screen clicks on CLOSE
    And grievanceAdmin on Home screen clicks on menu
    And grievanceAdmin on Home screen types on menuSearch value View Receiving Center
    And grievanceAdmin on Home screen clicks on firstMenuItem
    And grievanceAdmin on ReceivingCenterView screen types on receivingCenterSearchBox value Assistant Commissioner Office
    And grievanceAdmin on ReceivingCenterView screen verifies nameColumn has visible value Assistant Commissioner Office
    And grievanceAdmin on ReceivingCenterView screen verifies CRNActive has visible value No
    And grievanceAdmin on Home screen clicks on menu
    And grievanceAdmin on Home screen types on menuSearch value Officials Register Grievance
    And grievanceAdmin on Home screen clicks on firstMenuItem
    And grievanceAdmin on Grievance screen selects receivingMode with value as Manual
    And grievanceAdmin on Grievance screen selects receivingCenter with value as Assistant Commissioner Office
#    And grievanceAdmin on Grievance screen verifies CRNField notvisible
    And Intent:LogoutIntentTest


    Scenario: Modify receiving center name

      Given grievanceAdmin on Login screen types on username value narasappa
      And grievanceAdmin on Login screen types on password value demo
      And grievanceAdmin on Login screen clicks on signIn
      And grievanceAdmin on Home screen clicks on menu
      And grievanceAdmin on Home screen types on menuSearch value Update Receiving Center
      And grievanceAdmin on Home screen clicks on firstMenuItem
      And grievanceAdmin on UpdateReceivingCenter screen types on UpdateSearchBox suggestion box with value Assistant Commissioner Office
      And grievanceAdmin on UpdateReceivingCenter screen clicks on ReceivingCenterName
      And grievanceAdmin on UpdateReceivingCenter screen types on modifyReceivingCenterName value Assistant Commissioner Office_Zone
      And grievanceAdmin on ReceivingCenter screen clicks on ActiveButton
      And grievanceAdmin on ReceivingCenter screen clicks on Update
      And grievanceAdmin on ReceivingCenter screen verifies successMSG has visible value Success
      And grievanceAdmin on ReceivingCenter screen clicks on CLOSE
      And grievanceAdmin on Home screen clicks on menu
      And grievanceAdmin on Home screen types on menuSearch value View Receiving Center
      And grievanceAdmin on Home screen clicks on firstMenuItem
      And grievanceAdmin on ReceivingCenterView screen types on receivingCenterSearchBox value Assistant Commissioner Office_Zone
      And grievanceAdmin on ReceivingCenterView screen verifies nameColumn has visible value Assistant Commissioner Office_Zone
      And Intent:LogoutIntentTest