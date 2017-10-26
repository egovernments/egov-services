Feature: To create master data for Solid Waste Management

  @SWM
  Scenario: Collection point master

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
    And user on Home screen types on menuSearch value Stop Entry Master
    And user on Home screen clicks on firstMenuItem

    ##### Create collection point #####
    ### Enter Location Details ###
    And user on SWMMaster screen selects on ward value Election Ward 1
    And user on SWMMaster screen selects on zone value Revenue Ward 1
    And user on SWMMaster screen selects on street value Anjaneya Street
    And user on SWMMaster screen selects on colony value NR Colony

    ### Enter Stop details ###
    And user on SWMMaster screen types on stopName value collection point 1
    And user on SWMMaster screen copies the stopName to stopNameValue
    And user on SWMMaster screen types on binID value bin1
    And user on SWMMaster screen clicks on RFIDAssigned
    And user on SWMMaster screen types on RFID value RFID1
    And user on SWMMaster screen types on lattitude value 0.0
    And user on SWMMaster screen types on longitude value 0.0
    And user on SWMMaster screen clicks on text value Add


    ### Search for above created collection point ###
    And user on SWMMaster screen clicks on text value Search
    And user on SWMMaster screen verifies searchResultStopName has visible value stopNameValue



    ##### Vehicle Master #####
  # Pre-requirements:
  # Vehicle Type Master should be defined

  Scenario: Create, Search and Update Vehicle Master

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value narasappa
    And user on Login screen types on password value demo
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create Vehicle Master
    And user on Home screen clicks on firstMenuItem

    ### On Create Vehicle Master ###
    And user on SWMMaster screen selects on vehicleType value Dumper
    And user on SWMMaster screen types on vehicleRegistrationNo value KA05 HK4597
    And user on SWMMaster screen types on vehicleCapacity value 20

