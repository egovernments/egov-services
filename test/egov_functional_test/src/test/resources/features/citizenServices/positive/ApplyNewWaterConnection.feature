Feature: Applying New Water Connection from Citizen Login

  Scenario: Apply new water connection

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 7975179334
    And user on Login screen types on password value demo
    And user on Login screen clicks on signIn

    ### On Dashboard Screen ###
    And user on Dashboard screen will wait until the page loads
    And user on Dashboard screen verifies myServiceRequests has visible value My Service Requests
    And user on Dashboard screen clicks on text value Water
    And user on Dashboard screen clicks on text value Apply for New Connection

    ### On Create Connection Screen ###
    And user on CitizenNewWaterConnection screen types on ownerName value "Shivaji ", 4 random characters
    And user on CitizenNewWaterConnection screen types on mobileNumber value "9487",6 random numbers
    And user on CitizenNewWaterConnection screen types on email value "abcd@mail.com"
    And user on CitizenNewWaterConnection screen types on aadhaarNumber value 12 random numbers
    And user on CitizenNewWaterConnection screen selects on gender value MALE
    And user on CitizenNewWaterConnection screen types on houseNumber value "123/", 2 random numbers
    And user on CitizenNewWaterConnection screen types on address value Roha, MH
    And user on CitizenNewWaterConnection screen types on city value Roha
    And user on CitizenNewWaterConnection screen types on pin value 555444
    And user on CitizenNewWaterConnection screen selects on locality value Andhar Ali
    And user on CitizenNewWaterConnection screen selects on zone value Zone A
    And user on CitizenNewWaterConnection screen selects on ward value Prabhag 1

    ### On Create Legacy Connection entering connection details ###
    And user on CitizenNewWaterConnection screen selects on usageType value Residential
    And user on CitizenNewWaterConnection screen selects on usageSubType value Residential
    And user on CitizenNewWaterConnection screen selects on connectionSize value 1
    And user on CitizenNewWaterConnection screen selects on connectionType value METERED
    And user on CitizenNewWaterConnection screen selects on connectionStatus value PERMANENT
    And user on CitizenNewWaterConnection screen selects on waterSourceType value RIVER
    And user on CitizenNewWaterConnection screen selects on waterTreatment value bhuvaneshwar jal shudhikaran
    And user on CitizenNewWaterConnection screen types on sumpCapacityDataEntry value --3 random numbers
    And user on CitizenNewWaterConnection screen types on sequenceNumber value --6 random numbers
    And user on CitizenNewWaterConnection screen types on plumberName value "Plumber ", 4 random characters
    And user on CitizenNewWaterConnection screen types on noOfTaps value 2 random numbers
    And user on CitizenNewWaterConnection screen types on noOfPersonsDataEntry value 2 random numbers
    And user on CitizenNewWaterConnection screen scroll to the createButton
#    And user on CitizenNewWaterConnection screen uploads on uploadDocument value egov-services\test\egov_functional_test\src\test\resources\dataFiles\CollectionsTestData.xlsx
    And user on CitizenNewWaterConnection screen clicks on createButton
    And user on PaymentGateways screen clicks on text value Net Banking
    And user on PaymentGateways screen clicks on yesBankNetBanking
    And user on PaymentGateways screen clicks on proceedToPay
    And user on PaymentGateways screen clicks on netBanking
    And user on PaymentGateways screen dropdown on selectBank value Atom Bank
    And user on PaymentGateways screen clicks on payNow
    And user on PaymentGateways screen clicks on clickToTransferFunds
    And user on PaymentGateways screen accepts the popup

    ### On View Receipt Screen ###
    And user on CitizenNewWaterConnection screen will wait until the page loads
    And user on CitizenNewWaterConnection screen copies the reqNumber to SRNReqNumber
    And user on CitizenNewWaterConnection screen clicks on homeButton
    And user on CitizenNewWaterConnection screen will wait until the page loads

    ### On Dashboard Screen ###
    And user on Home screen types on dashBoardSearch value SRNReqNumber
    And user on Home screen verifies text has visible value SRNReqNumber
    And user on Home screen clicks on text value SRNReqNumber

    ### Logout ###
    And Intent:LogoutIntentTest