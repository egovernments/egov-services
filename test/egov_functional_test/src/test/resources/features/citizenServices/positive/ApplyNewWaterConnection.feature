Feature: Applying New Water Connection from Citizen Login

  Scenario: Apply new water connection

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 9483619659
    And user on Login screen types on password value eGov@123
    And user on Login screen clicks on signIn

    ### On Dashboard Screen ###
    And user on Dashboard screen will wait until the page loads
    And user on Dashboard screen verifies myServiceRequests has visible value My Service Requests
    And user on Dashboard screen clicks on text value Water
    And user on Dashboard screen clicks on text value Apply for New Connection

    ### On Create Connection Screen ###
    And user on WaterConnection screen types on ownerName value --"Owner ", 4 random characters
    And user on WaterConnection screen types on mobileNumber value --"1",9 random numbers
    And user on WaterConnection screen types on email value --email
    And user on WaterConnection screen types on aadhaarNumber value --12 digit number
    And user on WaterConnection screen selects on gender value MALE
    And user on WaterConnection screen types on addressNumber value 12345
    And user on WaterConnection screen types on address value Address
    And user on WaterConnection screen types on city value --"City ", 4 random characters
    And user on WaterConnection screen types on pin value --6 random numbers
    And user on WaterConnection screen selects on locality value Andhar Ali
    And user on WaterConnection screen selects on zone value Zone A
    And user on WaterConnection screen selects on ward value Prabhag 1

    ### On Create Legacy Connection entering connection details ###
    And user on WaterConnection screen selects on usageType value Residential
    And user on WaterConnection screen selects on usageSubType value Residential
    And user on WaterConnection screen selects on connectionSize value 1
    And user on WaterConnection screen selects on connectionType value METERED
    And user on WaterConnection screen selects on connectionStatus value PERMANENT
    And user on WaterConnection screen selects on waterSourceType value RIVER
    And user on WaterConnection screen selects on waterTreatment value bhuvaneshwar jal shudhikaran
    And user on WaterConnection screen types on sumpCapacityDataEntry value --3 random numbers
    And user on WaterConnection screen types on sequenceNumber value --6 random numbers
    And user on WaterConnection screen types on plumberName value --"Plumber ", 4 random characters
    And user on WaterConnection screen types on noOfTaps value --2 random numbers
    And user on WaterConnection screen types on noOfPersonsDataEntry value --2 random numbers
    And user on WaterConnection screen clicks on createButton
    And user on WaterConnection screen clicks on text value Pay & Proceed

    ### On View Receipt Screen ###
    And user on WaterConnection screen will wait until the page loads
    And user on WaterConnection screen copies the reqNumber to SRNReqNumber
    And user on WaterConnection screen clicks on homeButton
    And user on WaterConnection screen will wait until the page loads

    ### On Dashboard Screen ###
    And user on Home screen types on dashBoardSearch value SRNReqNumber
    And user on Home screen verifies text has visible value SRNReqNumber
    And user on Home screen clicks on text value SRNReqNumber

    ### Logout ###
    And Intent:LogoutIntentTest

