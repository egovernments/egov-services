Feature: Trade License

  Scenario: Create Legacy License with valid data

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value narasappa
    And user on Login screen types on password value demo
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create Legacy License
    And user on Home screen clicks on firstMenuItem

    ### On Create LegacyLicense Screen entering owner details ###
    And user on TradeLicense screen verifies text has visible value Create Legacy Trade License
    And user on TradeLicense screen types on oldLicenseNumber value TL/100001/2017
    And user on TradeLicense screen types on aadhaarNumber value 222232222221
    And user on TradeLicense screen types on mobileNumber value 9861234567
    And user on TradeLicense screen types on tradeOwnerName value Rakesh
    And user on TradeLicense screen types on fatherName value Sujit
    And user on TradeLicense screen types on email value rakesh@gmail.com
    And user on TradeLicense screen types on tradeOwnerAddress value Bangalore

    ### On Create LegacyLicense Screen entering trade locaiton details ###
    And user on TradeLicense screen types on propertyAssessmentNumber value 1016000009
    And user on TradeLicense screen selects on locality value Balaji Nagar
    And user on TradeLicense screen selects on adminWard value Election Ward No 1
    And user on TradeLicense screen selects on revenueWard value Revenue Ward No 1
    And user on TradeLicense screen selects on ownershipType value RENTED
    And user on TradeLicense screen types on tradeAddress value BTM,Bangalore

    ### On Create LegacyLicense Screen entering trade details ###
    And user on TradeLicense screen types on tradeTitle value Shopping Mall
    And user on TradeLicense screen selects on tradeType value PERMANENT
    And user on TradeLicense screen selects on tradeCategory value Flammables updated
    And user on TradeLicense screen selects on tradeSubCategory value teste
#   And user on TradeLicense screen display on uom value test -->auto populated
    And user on TradeLicense screen types on tradeValueForTheUOM value 8456
    And user on TradeLicense screen types on remarks value Trade Details updated successfully
    And user on TradeLicense screen types on tradeCommencementDate value 10/04/2016
    And user on TradeLicense screen types on licenseValidFromDate value 15/04/2016























