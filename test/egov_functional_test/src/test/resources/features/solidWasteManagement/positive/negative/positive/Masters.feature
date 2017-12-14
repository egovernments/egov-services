Feature: To create master data for Solid Waste Management

  @SWM
  Scenario: Collection point master

    ####### Create collection point #######
    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 2506
    And user on Login screen types on password value demo
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create Collection Point
    And user on Home screen clicks on firstMenuItem

    ##### Create collection point #####
    ### Enter Location Details ###
    And user on SWMMaster screen selects on ward value Revenue Ward No 1
    And user on SWMMaster screen selects on zone value Bheemilipatnam
    And user on SWMMaster screen selects on street value Bank Road
    And user on SWMMaster screen selects on colony value Bank Colony

    ### Enter Stop details ###
    And user on SWMMaster screen types on stopName value "Collection point ", 4 random characters
    And user on SWMMaster screen selects on collectionType value Fake Collection Type Name 1
    And user on SWMMaster screen types on garbageEstimate value 1000
    And user on SWMMaster screen copies the stopName to stopNameValue
    And user on SWMMaster screen types on binID value "Bin ID ", 4 random characters
#    And user on SWMMaster screen clicks on RFIDAssigned
#    And user on SWMMaster screen types on RFID value RFID1
#    And user on SWMMaster screen types on lattitude value 0.0
#    And user on SWMMaster screen types on longitude value 0.0
    And user on SWMMaster screen clicks on Create
#
#
#    ### Search for above created collection point ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen types on menuSearch value View Collection Point
    And user on Home screen clicks on firstMenuItem

    And user on SWMMaster screen types on searchName suggestion box with value stopNameValue
    And user on SWMMaster screen clicks on search
    And user on SWMMaster screen verifies searchResultStopName has visible value stopNameValue


    ### Update Collection Point ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen types on menuSearch value Update Collection Point
    And user on Home screen clicks on firstMenuItem

    And user on SWMMaster screen refresh's the webpage
    And user on SWMMaster screen types on searchName suggestion box with value stopNameValue
    And user on SWMMaster screen clicks on search
    And user on SWMMaster screen clicks on searchResultStopName

    And user on SWMMaster screen refresh's the webpage
    And user on SWMMaster screen selects on ward value Revenue Ward No 1
#    And user on SWMMaster screen forceClicks on zone
    And user on SWMMaster screen selects on zone value Bheemilipatnam
    And user on SWMMaster screen selects on street value Bank Road
    And user on SWMMaster screen selects on colony value Bank Colony

    And user on SWMMaster screen selects on collectionType value Fake Collection Type Name 2
    And user on SWMMaster screen copies the collectionType to collectionTypeValue
    And user on SWMMaster screen clicks on Update
    And user on SWMMaster screen will wait until the page loads
    And user on SWMMaster screen verifies collectionType has visible value collectionTypeValue

#
   ### Logout ###
    And Intent:LogoutIntentTest

    ##### Vehicle Master #####
  # Pre-requirements:
  # Vehicle Type Master should be defined

  Scenario: Create, Search and Update Vehicle Master

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value narasappa
    And user on Login screen types on password value demo
    And user on Login screen clicks on signIn

#    ### On Homepage Screen ###
#    And user on Home screen will wait until the page loads
#    And user on Home screen will see the menu
#    And user on Home screen clicks on menu
#    And user on Home screen types on menuSearch value Create Vendor
#    And user on Home screen clicks on thirdMenuItem
#
#   ### Create Vendor details ###

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create Vehicle
    And user on Home screen clicks on forthMenuItem

    ### On Create Vehicle Master ###
    And user on SWMMaster screen selects on vehicleType value Fake Vehicle Type Code 1
    And user on SWMMaster screen types on vehicleRegistrationNo value "KA05 HK", 4 random digit number
    And user on SWMMaster screen copies the vehicleRegistrationNo to vehicleRegistrationNo
    And user on SWMMaster screen types on vehicleCapacity value 20
    And user on SWMMaster screen types on engineSrNo value "AB", 4 random digit number
    And user on SWMMaster screen copies the engineSrNo to engineSrNo
    And user on SWMMaster screen types on NoOfPersonsReq value 10
    And user on SWMMaster screen types on chassisSrNumber value "AB", 4 random digit number
    And user on SWMMaster screen types on model value Honda
    And user on SWMMaster screen selects on fuelType value Fake Fuel Type Code 1
    And user on SWMMaster screen selects on vendorName value Vendor Name 1353
    And user on SWMMaster screen types on insuranceNumber value 1234
    And user on SWMMaster screen types on insuranceValidityDate value 01/10/2017
    And user on SWMMaster screen types on insuranceDocument value Photo
    And user on SWMMaster screen types on purchaseDate value 01/10/2017
    And user on SWMMaster screen types on price value 200000
    And user on SWMMaster screen clicks on create

    ### Search Vehicle ###
    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen types on menuSearch value View Vehicle
    And user on Home screen clicks on firstMenuItem

    ### View above created vehicle details ###
    And user on SWMMaster screen types on searchengineSrNumber value engineSrNo
    And user on SWMMaster screen clicks on search
    And user on SWMMaster screen verifies text has visible value vehicleRegistrationNo

    ### Update Vihicle ###
    ### Search Vehicle ###
    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen types on menuSearch value Update Vehicle
    And user on Home screen clicks on firstMenuItem

    ### View above created vehicle details ###
    And user on SWMMaster screen types on searchengineSrNumber value engineSrNo
    And user on SWMMaster screen clicks on search
    And user on SWMMaster screen clicks on text value vehicleRegistrationNo

    ### Update vehicle details ###
    And user on SWMMaster screen will wait until the page loads
    And user on SWMMaster screen selects on vehicleType value Fake Vehicle Type Code 2
    And user on SWMMaster screen copies the vehicleType to vehicleType
    And user on SWMMaster screen types on vehicleRegistrationNo value "KA05 HK", 4 random digit number
    And user on SWMMaster screen copies the vehicleRegistrationNo to vehicleRegistrationNo
    And user on SWMMaster screen types on vehicleCapacity value 40
    And user on SWMMaster screen copies the vehicleCapacity to vehicleCapacity
    And user on SWMMaster screen types on engineSrNo value "AB", 4 random digit number
    And user on SWMMaster screen copies the engineSrNo to engineSrNo
    And user on SWMMaster screen types on NoOfPersonsReq value 10
    And user on SWMMaster screen types on chassisSrNumber value "AB", 4 random digit number
    And user on SWMMaster screen types on model value Honda
    And user on SWMMaster screen selects on fuelType value Fake Fuel Type Code 1
    And user on SWMMaster screen selects on vendorName value Fake Vendor name 1
    And user on SWMMaster screen types on insuranceNumber value 1234
    And user on SWMMaster screen types on insuranceValidityDate value 01//10//2017
    And user on SWMMaster screen types on insuranceDocument value Photo
    And user on SWMMaster screen types on purchaseDate value 01//10//2017
    And user on SWMMaster screen types on price value 200000
    And user on SWMMaster screen clicks on update

    ### Search Vehicle ###
    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen types on menuSearch value View Vehicle
    And user on Home screen clicks on firstMenuItem

    ### View above created vehicle details ###
    And user on SWMMaster screen types on searchengineSrNumber value engineSrNo
    And user on SWMMaster screen clicks on search
    And user on SWMMaster screen verifies text has visible value vehicleRegistrationNo

    ### Logout ###
    And Intent:LogoutIntentTest


    Scenario: Create, search and update vehicle maintenance master

      ### On Login Screen ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 2506
      And user on Login screen types on password value demo
      And user on Login screen clicks on signIn

      ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen will see the menu
      And user on Home screen clicks on menu
      And user on Home screen types on menuSearch value Create Vehicle
      And user on Home screen clicks on forthMenuItem

    ### On Create Vehicle Master ###
      And user on SWMMaster screen selects on vehicleType value Fake Vehicle Type Name 1
      And user on SWMMaster screen types on vehicleRegistrationNo value "KA05HK", 4 random characters
      And user on SWMMaster screen copies the vehicleRegistrationNo to vehicleRegistrationNo
      And user on SWMMaster screen types on vehicleCapacity value 20
      And user on SWMMaster screen types on engineSrNo value "KA05HK", 4 random characters
      And user on SWMMaster screen copies the engineSrNo to engineSrNo
      And user on SWMMaster screen types on NoOfPersonsReq value 10
      And user on SWMMaster screen types on chassisSrNumber value "KA05HK", 4 random characters
      And user on SWMMaster screen types on model value Honda
      And user on SWMMaster screen selects on fuelType value Fake Fuel Type Name 1
      And user on SWMMaster screen selects on vendorName value Vendor Name 1353
      And user on SWMMaster screen types on insuranceNumber value "KA05HK", 4 random characters
      And user on SWMMaster screen types on insuranceValidityDate value 01/10/2018
      And user on SWMMaster screen uploads on insuranceDocument value pgrDocument.jpg
      And user on SWMMaster screen types on purchaseDate value past 5 dates
      And user on SWMMaster screen types on price value 200000
      And user on SWMMaster screen types on sourceOfPurchase value Loan
      And user on SWMMaster screen clicks on create

      ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen will see the menu
      And user on Home screen types on menuSearch value Create Vehicle Maintenance
      And user on Home screen clicks on secondMenuItem

      ### Create vehicle maintenance ###

      And user on SWMMaster screen types on regNumber suggestion box with value vehicleRegistrationNo
      And user on SWMMaster screen types on maintenanceAfter value 20
      And user on SWMMaster screen selects on maintenanceUom value Days
      And user on SWMMaster screen types on downtimeforMaintenance value 2
      And user on SWMMaster screen selects on downtimeforMaintenanceUom value Days
      And user on SWMMaster screen clicks on create

      ### View Vehicle Maintenance Details ###
      And user on Home screen will wait until the page loads
      And user on Home screen will see the menu
      And user on Home screen types on menuSearch value View Vehicle Maintenance
      And user on Home screen clicks on secondMenuItem

      And user on SWMMaster screen types on searchRegNumber suggestion box with value vehicleRegistrationNo
      And user on SWMMaster screen clicks on search
      And user on SWMMaster screen verifies searchResulRegNumber has visible value vehicleRegistrationNo

      ### Update Vehicle Maintenance Details ###
      And user on Home screen will wait until the page loads
      And user on Home screen will see the menu
      And user on Home screen types on menuSearch value Update Vehicle Maintenance
      And user on Home screen clicks on secondMenuItem

      And user on SWMMaster screen refresh's the webpage
      And user on SWMMaster screen types on searchRegNumber suggestion box with value vehicleRegistrationNo
      And user on SWMMaster screen clicks on search
      And user on SWMMaster screen verifies searchResulRegNumber has visible value vehicleRegistrationNo

      And user on SWMMaster

      ### Logout ###
      And Intent:LogoutIntentTest
