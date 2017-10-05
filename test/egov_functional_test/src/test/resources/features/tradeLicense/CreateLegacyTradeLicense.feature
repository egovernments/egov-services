Feature: In this feature we are going to create Legacy Trade License

  Scenario: Create Legacy License with valid data

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 1212
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create Legacy License
    And user on Home screen clicks on firstMenuItem

    ### On Create LegacyLicense Screen entering owner details ###
    And user on TradeLicense screen verifies text has visible value Create Legacy Trade License
    And user on TradeLicense screen types on oldLicenseNumber value "OldNo",3 random number
    And user on TradeLicense screen copies the oldLicenseNumber to oldLicenseNumber
    And user on TradeLicense screen types on aadhaarNumber value 222232222221
    And user on TradeLicense screen types on mobileNumber value 2222222222
    And user on TradeLicense screen types on tradeOwnerName value Akhila
    And user on TradeLicense screen copies the tradeOwnerName to tradeOwnerName
    And user on TradeLicense screen types on fatherName value Divakara
    And user on TradeLicense screen types on email value abc@xyz.com
    And user on TradeLicense screen types on tradeOwnerAddress value Bangalore

    ### On Create LegacyLicense Screen entering trade locaiton details ###
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
    And user on TradeLicense screen types on licenseValidFromDate value 01/04/2017
    And user on TradeLicense screen types on financialYearAmount value 2000
    And user on TradeLicense screen clicks on text value Create
    And user on TradeLicense screen scroll to the oldLicenseNumber
    And user on TradeLicense screen verifies text has visible value oldLicenseNumber

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Search License
    And user on Home screen clicks on firstMenuItem

    ### Search the above created application ###

    And user on TradeLicense screen types on searchWithOldLicenseNumber value oldLicenseNumber
    And user on TradeLicense screen clicks on searchButton
    And user on TradeLicense screen verifies text has visible value tradeOwnerName

    ### Logout ###
    And Intent:LogoutIntentTest



  Scenario: Create Legacy License with Trade Commencement Date Future date  - No validation as per story

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value elzan
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create Legacy License
    And user on Home screen clicks on firstMenuItem

    ### On Create LegacyLicense Screen entering owner details ###
    And user on TradeLicense screen verifies text has visible value Create Legacy Trade License
    And user on TradeLicense screen types on oldLicenseNumber value "OldNo",3 random number
    And user on TradeLicense screen copies the oldLicenseNumber to oldLicenseNumber
    And user on TradeLicense screen types on aadhaarNumber value 222232222221
    And user on TradeLicense screen types on mobileNumber value 2222222222
    And user on TradeLicense screen types on tradeOwnerName value Akhila
    And user on TradeLicense screen types on fatherName value Divakara
    And user on TradeLicense screen types on email value abc@xyz.com
    And user on TradeLicense screen types on tradeOwnerAddress value Bangalore

    ### On Create LegacyLicense Screen entering trade locaiton details ###
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
    And user on TradeLicense screen selects on tradeSubCategory value Camphor
#   And user on TradeLicense screen display on uom value test -->auto populated
    And user on TradeLicense screen types on tradeValueForTheUOM value 200
    And user on TradeLicense screen types on remarks value Trade Details updated successfully
    And user on TradeLicense screen types on tradeCommencementDate value 01/01/2018
    And user on TradeLicense screen types on licenseValidFromDate value 01/04/2017
    And user on TradeLicense screen types on financialYearAmount value 2000
    And user on TradeLicense screen clicks on text value Create

    ### Logout ###
    And Intent:LogoutIntentTest



  Scenario: Create legacy license with Validity Years = 2 and License valid from Date 01.01.2017

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value elzan
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create Legacy License
    And user on Home screen clicks on firstMenuItem

    ### On Create LegacyLicense Screen entering owner details ###
    And user on TradeLicense screen verifies text has visible value Create Legacy Trade License
    And user on TradeLicense screen types on oldLicenseNumber value "OldNo",3 random number
    And user on TradeLicense screen copies the oldLicenseNumber to oldLicenseNumber
    And user on TradeLicense screen types on aadhaarNumber value 222232222221
    And user on TradeLicense screen types on mobileNumber value 2222222222
    And user on TradeLicense screen types on tradeOwnerName value Akhila
    And user on TradeLicense screen types on fatherName value Divakara
    And user on TradeLicense screen types on email value abc@xyz.com
    And user on TradeLicense screen types on tradeOwnerAddress value Bangalore

    ### On Create LegacyLicense Screen entering trade locaiton details ###
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
    And user on TradeLicense screen selects on tradeSubCategory value Camphor
#   And user on TradeLicense screen display on uom value test -->auto populated
    And user on TradeLicense screen types on tradeValueForTheUOM value 200
    And user on TradeLicense screen types on remarks value Trade Details updated successfully
    And user on TradeLicense screen types on tradeCommencementDate value 01/01/2018
    And user on TradeLicense screen types on licenseValidFromDate value 01/01/2017
    And user on TradeLicense screen verifies financialYear has visible value 2016-17
    And user on TradeLicense screen types on financialYearAmount value 2000
    And user on TradeLicense screen clicks on text value Create

  Scenario: Create legacy license with document
  Scenario: Create legacy license with document type format Excel
  Scenario: Create legacy license with validity 2 years and License Valid From Date 01/01/2013
#    License expiry date should be 31/03/2014 (if is paid checked only for 2012-13
  Scenario: Create legacy license with validity 2 years and License Valid From Date 01/01/2013 and check Is Paid check box for 2014-15
#    License expiry date should be 31/03/2016 (if is paid checked only for 2014-15


    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 1212
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create Legacy License
    And user on Home screen clicks on firstMenuItem

    ### On Create LegacyLicense Screen entering owner details ###
    And user on TradeLicense screen verifies text has visible value Create Legacy Trade License
    And user on TradeLicense screen types on oldLicenseNumber value "OldNo",3 random number
    And user on TradeLicense screen copies the oldLicenseNumber to oldLicenseNumber
    And user on TradeLicense screen types on aadhaarNumber value 222232222221
    And user on TradeLicense screen types on mobileNumber value 2222222222
    And user on TradeLicense screen types on tradeOwnerName value Akhila
    And user on TradeLicense screen types on fatherName value Divakara
    And user on TradeLicense screen types on email value abc@xyz.com
    And user on TradeLicense screen types on tradeOwnerAddress value Bangalore

    ### On Create LegacyLicense Screen entering trade locaiton details ###
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
    And user on TradeLicense screen types on licenseValidFromDate value 01/04/2017
    And user on TradeLicense screen uploads on text value Photo
    And user on TradeLicense screen types on financialYearAmount value 2000
    And user on TradeLicense screen clicks on text value Create
    And user on TradeLicense screen scroll to the oldLicenseNumber
    And user on TradeLicense screen verifies text has visible value oldLicenseNumber

    ### Logout ###
    And Intent:LogoutIntentTest



#  Scenario: Create Legacy License with License Valid From Date Future date
#
#    ### On Login Screen ###
#    Given user on Login screen verifies signInText has visible value Sign In
#    And user on Login screen types on username value elzan
#    And user on Login screen types on password value 12345678
#    And user on Login screen clicks on signIn
#
#    ### On Homepage Screen ###
#    And user on Home screen will wait until the page loads
#    And user on Home screen will see the menu
#    And user on Home screen clicks on menu
#    And user on Home screen types on menuSearch value Create Legacy License
#    And user on Home screen clicks on firstMenuItem
#
#    ### On Create LegacyLicense Screen entering owner details ###
#    And user on TradeLicense screen verifies text has visible value Create Legacy Trade License
#    And user on TradeLicense screen types on oldLicenseNumber value "OldNo",3 random number
#    And user on TradeLicense screen copies the oldLicenseNumber to oldLicenseNumber
#    And user on TradeLicense screen types on aadhaarNumber value 222232222221
#    And user on TradeLicense screen types on mobileNumber value 2222222222
#    And user on TradeLicense screen types on tradeOwnerName value Akhila
#    And user on TradeLicense screen types on fatherName value Divakara
#    And user on TradeLicense screen types on email value abc@xyz.com
#    And user on TradeLicense screen types on tradeOwnerAddress value Bangalore
#
#    ### On Create LegacyLicense Screen entering trade locaiton details ###
##    And user on TradeLicense screen types on propertyAssessmentNumber value 1016000009
#    And user on TradeLicense screen selects on locality value Bank Road
#    And user on TradeLicense screen selects on adminWard value Election Ward No 1
#    And user on TradeLicense screen selects on revenueWard value Revenue Ward No 1
#    And user on TradeLicense screen selects on ownershipType value RENTED
#    And user on TradeLicense screen types on tradeAddress value BTM,Bangalore
#
#    ### On Create LegacyLicense Screen entering trade details ###
#    And user on TradeLicense screen types on tradeTitle value DJ Tools
#    And user on TradeLicense screen selects on tradeType value PERMANENT
#    And user on TradeLicense screen selects on tradeCategory value Flammables
#    And user on TradeLicense screen selects on tradeSubCategory value Acetylene Gas
##   And user on TradeLicense screen display on uom value test -->auto populated
#    And user on TradeLicense screen types on tradeValueForTheUOM value 200
#    And user on TradeLicense screen types on remarks value Trade Details updated successfully
#    And user on TradeLicense screen types on tradeCommencementDate value 01/01/2017
#    And user on TradeLicense screen types on licenseValidFromDate value 01/01/2018
#    And user on TradeLicense screen types on financialYearAmount value 2000
#    And user on TradeLicense screen clicks on text value Create
#
#    ### Logout ###
#    And Intent:LogoutIntentTest
#
#
#  Scenario: Create legacy license with duplicate old license number
#
#    ### On Login Screen ###
#    Given user on Login screen verifies signInText has visible value Sign In
#    And user on Login screen types on username value elzan
#    And user on Login screen types on password value 12345678
#    And user on Login screen clicks on signIn
#
#    ### On Homepage Screen ###
#    And user on Home screen will wait until the page loads
#    And user on Home screen will see the menu
#    And user on Home screen clicks on menu
#    And user on Home screen types on menuSearch value Create Legacy License
#    And user on Home screen clicks on firstMenuItem
#
#    ### On Create LegacyLicense Screen entering owner details ###
#    And user on TradeLicense screen verifies text has visible value Create Legacy Trade License
#    And user on TradeLicense screen types on oldLicenseNumber value 1234
##    enter duplicate old license number
#    And user on TradeLicense screen copies the oldLicenseNumber to oldLicenseNumber
#    And user on TradeLicense screen types on aadhaarNumber value 222232222221
#    And user on TradeLicense screen types on mobileNumber value 2222222222
#    And user on TradeLicense screen types on tradeOwnerName value Akhila
#    And user on TradeLicense screen types on fatherName value Divakara
#    And user on TradeLicense screen types on email value abc@xyz.com
#    And user on TradeLicense screen types on tradeOwnerAddress value Bangalore
#
#    ### On Create LegacyLicense Screen entering trade locaiton details ###
##    And user on TradeLicense screen types on propertyAssessmentNumber value 1016000009
#    And user on TradeLicense screen selects on locality value Bank Road
#    And user on TradeLicense screen selects on adminWard value Election Ward No 1
#    And user on TradeLicense screen selects on revenueWard value Revenue Ward No 1
#    And user on TradeLicense screen selects on ownershipType value RENTED
#    And user on TradeLicense screen types on tradeAddress value BTM,Bangalore
#
#    ### On Create LegacyLicense Screen entering trade details ###
#    And user on TradeLicense screen types on tradeTitle value DJ Tools
#    And user on TradeLicense screen selects on tradeType value PERMANENT
#    And user on TradeLicense screen selects on tradeCategory value Flammables
#    And user on TradeLicense screen selects on tradeSubCategory value Acetylene Gas
##   And user on TradeLicense screen display on uom value test -->auto populated
#    And user on TradeLicense screen types on tradeValueForTheUOM value 200
#    And user on TradeLicense screen types on remarks value Trade Details updated successfully
#    And user on TradeLicense screen types on tradeCommencementDate value 01/01/2017
#    And user on TradeLicense screen types on licenseValidFromDate value 01/01/2018
#    And user on TradeLicense screen types on financialYearAmount value 2000
#    And user on TradeLicense screen clicks on text value Create
#
#    ### Logout ###
#    And Intent:LogoutIntentTest
























