Feature: Creating Legacy Connection

  Scenario: Create Legacy Connection in Micro-QA with tenantId mh.roha

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value nadirroha
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen verifies myTasks has visible value My Tasks
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create Legacy Connection
    And user on Home screen clicks on firstMenuItem

    ### On Create Legacy Connection entering applicant details ###
    And user on WaterConnection screen types on ownerName value --"Owner ", 4 random characters
    And user on WaterConnection screen types on mobileNumber value --"1",9 random numbers
    And user on WaterConnection screen types on email value --email
    And user on WaterConnection screen types on aadhaarNumber value --12 digit number
    And user on WaterConnection screen selects on gender value MALE
    And user on WaterConnection screen types on address value Address
    And user on WaterConnection screen types on city value --"City ", 4 random characters
    And user on WaterConnection screen types on pin value --6 random numbers
    And user on WaterConnection screen selects on locality value Andhar Ali
    And user on WaterConnection screen selects on ward value Prabhag 1
    And user on WaterConnection screen selects on zone value Zone A
    And user on WaterConnection screen types on oldConsumerNumber value --10 random numbers
    And user on WaterConnection screen types on physicalConnectionDate value 23/08/2017

    ### On Create Legacy Connection entering connection details ###
    And user on WaterConnection screen selects on propertyTypeDataEntry value Others
    And user on WaterConnection screen selects on categoryType value GENERAL
    And user on WaterConnection screen selects on usageType value Industrial
    And user on WaterConnection screen selects on usageSubType value Industrial
    And user on WaterConnection screen selects on connectionSize value 1
    And user on WaterConnection screen selects on connectionType value NONMETERED
    And user on WaterConnection screen selects on connectionStatus value PERMANENT
    And user on WaterConnection screen selects on waterSourceType value RIVER
    And user on WaterConnection screen selects on supplyType value Semi Bulk Type
    And user on WaterConnection screen selects on waterTreatment value bhuvaneshwar jal shudhikaran-bhuvaneshwar jal shudhikaran
    And user on WaterConnection screen types on sumpCapacityDataEntry value --3 random numbers
    And user on WaterConnection screen types on sequenceNumber value --6 random numbers
    And user on WaterConnection screen types on plumberName value --"Plumber ", 4 random characters
    And user on WaterConnection screen types on noOfTaps value --2 random numbers
    And user on WaterConnection screen types on noOfPersonsDataEntry value --2 random numbers

    ### On Create Legacy Connection entering connection details ###
    And user on WaterConnection screen types on securityDeposit value --3 random numbers
    And user on WaterConnection screen types on receiptNumber value --6 random numbers
    And user on WaterConnection screen types on receiptDate value 23/08/2017
