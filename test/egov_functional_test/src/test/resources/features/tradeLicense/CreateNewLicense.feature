Feature: In this feature we are going to create new trade license

  Scenario: Create New License with valid data (without document)

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value elzan
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create New License
    And user on Home screen clicks on firstMenuItem

    ### On Create New License Screen entering trade owner details ###
    And user on TradeLicense screen verifies text has visible value Apply New Trade License
    And user on TradeLicense screen types on aadhaarNumber value 222232222221
    And user on TradeLicense screen types on mobileNumber value 2222222222
    And user on TradeLicense screen types on tradeOwnerName value Akhila
    And user on TradeLicense screen types on fatherName value Divakara
    And user on TradeLicense screen types on email value abc@xyz.com
    And user on TradeLicense screen types on tradeOwnerAddress value Bangalore

    ### On Create New License Screen entering trade locaiton details ###
#    And user on TradeLicense screen types on propertyAssessmentNumber value 1016000009
    And user on TradeLicense screen selects on locality value Bank Road
    And user on TradeLicense screen selects on adminWard value Election Ward No 1
    And user on TradeLicense screen selects on revenueWard value Revenue Ward No 1
    And user on TradeLicense screen selects on ownershipType value RENTED
    And user on TradeLicense screen types on tradeAddress value BTM,Bangalore

    ### On Create LegacyLicense Screen entering trade details ###
    And user on TradeLicense screen types on tradeTitle value DJ Tools
    And user on TradeLicense screen selects on tradeType value PERMANENT
    And user on TradeLicense screen selects on tradeCategory value Flammables
    And user on TradeLicense screen selects on tradeSubCategory value Acetylene Gas
#   And user on TradeLicense screen display on uom value test -->auto populated
    And user on TradeLicense screen types on tradeValueForTheUOM value 200
    And user on TradeLicense screen types on remarks value Trade Details updated successfully
    And user on TradeLicense screen types on tradeCommencementDate value 10/04/2017


    Scenario: Create New License with Trade commencement date in previous financial year and validity one year
#      License fee should be calculated for this financial year as well and validity should be upto 31/03/2018 - 1016/TL/2017/000064
    Scenario: Create New License with Trade commencement date in previous financial year and validity two year
#      License should not be able to renew and validity expiry date should be 31/03/2018
    Scenario: Create New License with Trade commencement date within current financial year and validity one year
#      License should not be able to renew and validity expiry date should be 31/03/2018 - 1016/TL/2017/000065
    Scenario: Create New License with Trade commencement date within current financial year and validity two year
#      License should not be able to renew and validity expiry date should be 31/03/2019
    Scenario: Create New License with Trade Commencement date within next financial year and validity one year.
#      License should not be able to renew and validity expiry date should be 31/03/2019 - 1016/TL/2017/000066

    Scenario: Create New License with Property Assessment Number
    Scenario: Create New License without changing Trade value for the UOM
    Scenario: Create New License with change in Trade value for the UOM
    Scenario: Create New License, when application is in commissioner inbox change fee matrix
    Scenario: Create New License without defining fee matrix
    Scenario: Create New License without defining range in the fee matrix


    Scenario: Rejection of new license by Junior Assistant after application submitted.
    Scenario: Rejection of new license by SI
    Scenario: Rejection of new license by Commmissioner
    Scenario: Rejection of new license after commissioner approval and before fee payment
#    1016/TL/2017/000107 - License fee to be paid not showing - before collection only its showing Print Cerftificate option
#  User cannot reject the application after commissioner approval