Feature: Transaction for solid waste management

  Scenario: Create, search and update Vehicle Fuelling Details

    ####### Create collection point #######
    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value narasappa
    And user on Login screen types on password value demo
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Enter Vehicle Fuelling Details
    And user on Home screen clicks on firstMenuItem

    ### Create Vehicle fuelling transaction ###
    And user on SWMTransaction screen types on transactionDate value current date
    And user on SWMTransaction screen selects on vehicleType value Fake Vehicle Type 1
    And user on SWMTransaction screen selects on vehicleRegNo value Fake registration Number 1
    And user on SWMTransaction screen copies the vehicleRegNo to vehicleRegNo
    And user on SWMTransaction screen types on vehicleReadingDuringFueling value 2000
    And user on SWMTransaction screen selects on refillingPumpStation value Fake name 1
    And user on SWMTransaction screen types on fuelType value Petrol
    And user on SWMTransaction screen types on fuelFilled value 20
    And user on SWMTransaction screen types on totalCost value 1500
    And user on SWMTransaction screen types on receiptNo value "RNo. ", 4 digit random numbers
    And user on SWMTransaction screen copies the receiptNo to receiptNo
    And user on SWMTransaction screen types on receiptDate value 01/10/2017
    And user on SWMTransaction screen clicks on text value Create

    ##### Search above created transaction #####
    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen types on menuSearch value View Vehicle Fuelling Details
    And user on Home screen clicks on firstMenuItem


    ### View the above transaction ###
    And user on SWMTransaction screen will wait until the page loads
    And user on SWMTransaction screen types on viewReceiptNo value receiptNo
    And user on SWMTransaction screen clicks on search
    And user on SWMTransaction screen verifies text has visible value vehicleRegNo


    ##### Update above transaction #####
    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen types on menuSearch value Update Vehicle Fuelling Details
    And user on Home screen clicks on firstMenuItem


    ### Search the above transaction ###
    And user on SWMTransaction screen will wait until the page loads
    And user on SWMTransaction screen types on viewReceiptNo value receiptNo
    And user on SWMTransaction screen clicks on search
    And user on SWMTransaction screen clicks on text value vehicleRegNo

    ### update the transaction ###
    And user on SWMTransaction screen will wait until the page loads
    And user on SWMTransaction screen types on fuelFilled value 40
    And user on SWMTransaction screen copies the fuelFilled to fuelFilled
    And user on SWMTransaction screen clicks on update

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen types on menuSearch value View Vehicle Fuelling Details
    And user on Home screen clicks on firstMenuItem

    ### View the above transaction ###
    And user on SWMTransaction screen will wait until the page loads
    And user on SWMTransaction screen types on viewReceiptNo value receiptNo
    And user on SWMTransaction screen clicks on search
    And user on SWMTransaction screen verifies text has visible value fuelFilled

    ### Logout ###
    And Intent:LogoutIntentTest



    