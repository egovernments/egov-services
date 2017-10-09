Feature: In this feature we are testing the negative scenarios for new trade license

  Scenario: Create new license application with trade commencement date as next to next financial year

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

    ### On Create New License Screen entering trade details ###
    And user on TradeLicense screen types on tradeTitle value DJ Tools
    And user on TradeLicense screen selects on tradeType value PERMANENT
    And user on TradeLicense screen selects on tradeCategory value Flammables
    And user on TradeLicense screen selects on tradeSubCategory value Camphor
#   And user on TradeLicense screen display on uom value test -->auto populated
    And user on TradeLicense screen types on tradeValueForTheUOM value 20
    And user on TradeLicense screen types on remarks value Trade Details updated successfully
    And user on TradeLicense screen types on tradeCommencementDate value 10/04/2019

    ### Submitting the new license application ###
    And user on TradeLicense screen clicks on text value Submit
    And user on TradeLicense screen verifies test has visible value Trade Commencement Date is not valid, Please enter valid date as epoch :