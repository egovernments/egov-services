Feature: In this feature we are going to create new trade license

  Scenario: Create New License with valid data (without document)

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 1212
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
    And user on TradeLicense screen types on mobileNumber value 9036544535
    And user on TradeLicense screen types on tradeOwnerName value Akhila
    And user on TradeLicense screen types on fatherName value Divakara
    And user on TradeLicense screen types on email value akhila.gd@egovernments.org
    And user on TradeLicense screen types on tradeOwnerAddress value Bangalore

    ### On Create New License Screen entering trade locaiton details ###
#    And user on TradeLicense screen types on propertyAssessmentNumber value 1016000009
    And user on TradeLicense screen selects on locality value Bank Road
    And user on TradeLicense screen selects on adminWard value Election Ward No 1
    And user on TradeLicense screen selects on revenueWard value Revenue Ward No 1
    And user on TradeLicense screen selects on ownershipType value OWNED
    And user on TradeLicense screen types on tradeAddress value BTM,Bangalore

    ### On Create New License Screen entering trade details ###
    And user on TradeLicense screen types on tradeTitle value DJ Tools
    And user on TradeLicense screen selects on tradeType value PERMANENT
    And user on TradeLicense screen selects on tradeCategory value AFlammables
    And user on TradeLicense screen selects on tradeSubCategory value Camphor
#   And user on TradeLicense screen display on uom value test -->auto populated
    And user on TradeLicense screen types on tradeValueForTheUOM value 20
    And user on TradeLicense screen types on remarks value Trade Details updated successfully
    And user on TradeLicense screen types on tradeCommencementDate value 10/04/2017

    ### Submitting the new license application ###
    And user on TradeLicense screen clicks on text value Submit

    And user on TradeLicense screen clicks on text value View
    And user on TradeLicense screen copies the applicationNumber to applicationNumber

    ### Forwarding application to Sanitary Inspector ###
    And user on Home screen clicks on homeButton
    And user on Home screen types on dashBoardSearch with above applicationNumber
    And user on TradeLicense screen selects on approverDepartment value PUBLIC HEALTH AND SANITATION
    And user on TradeLicense screen selects on approverDesignation value Sanitary Inspector
    And user on TradeLicense screen selects on approver value Akhila
    And user on TradeLicense screen types on comments value Can be approved
    And user on TradeLicense screen clicks on forward

    ### Logout ###
    And Intent:LogoutIntentTest

    ### On Login to Sanitary Inspector ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 2507
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen types on dashBoardSearch with above applicationNumber
    And user on TradeLicense screen types on fieldInspectionDetails value ok
    And user on TradeLicense screen uploads on fieldInspectionFile value pgrDocument.jpg
    And user on TradeLicense screen selects on approverDepartment value PUBLIC HEALTH AND SANITATION
    And user on TradeLicense screen selects on approverDesignation value Sanitary Inspector
    And user on TradeLicense screen selects on approver value Akhila
    And user on TradeLicense screen types on comments value Can be approved
    And user on TradeLicense screen clicks on forward


    ### Logout ###
    And Intent:LogoutIntentTest

    ### On Login to Commissioner ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 2020
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen types on dashBoardSearch with above applicationNumber
    And user on TradeLicense screen types on comments value Can be approved
    And user on TradeLicense screen clicks on text value Approve

    ### Logout ###
    And Intent:LogoutIntentTest

    ### On Login to creator ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 1212
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Pay Taxes
    And user on Home screen clicks on firstMenuItem

    ### License Fee Payment ###
    And user on PayTaxes screen verifies text has visible value Pay tax
    And user on PayTaxes screen selects on billingServiceName value Trade License
    And user on PayTaxes screen types on consumerCode value applicationNumber
    And user on PayTaxes screen clicks on text value Search

    And user on PayTaxes screen verifies text has visible value applicationNumber
    And user on PayTaxes screen copies the amountToBePaid to taxAmount
    And user on PayTaxes screen types on amount value taxAmount
    And user on PayTaxes screen types on paidBy value Akhila
    And user on PayTaxes screen clicks on text value Pay

    ### Print License Certificate ###
    And user on Home screen clicks on homeButton
    And user on Home screen types on dashBoardSearch with above applicationNumber
    And user on TradeLicense screen clicks on text value Print Certificate
    And user on TradeLicense screen verifies text has visible value Download

    ### Search for above created application ###
    And user on TradeLicense screen types on applicationNumberSearch value applicationNumber
    And user on TradeLicense screen clicks on search
    And user on TradeLicense screen verifies text has visible value DJ Tools

    ### Logout ###
    And Intent:LogoutIntentTest


  Scenario: Create New License with Trade commencement date in previous financial year and validity one year
#      License fee should be calculated for this financial year as well and validity should be upto 31/03/2018 - 1016/TL/2017/000064

    ### On Login Screen ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 1212
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
      And user on TradeLicense screen types on tradeCommencementDate value 10/04/2016

    ### Submitting the new license application ###
      And user on TradeLicense screen clicks on text value Submit
      And user on TradeLicense screen clicks on text value View
      And user on TradeLicense screen copies the applicationNumber to applicationNumber

    ### Forwarding application to Sanitary Inspector ###
      And user on Home screen clicks on homeButton
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen selects on approverDepartment value PUBLIC HEALTH AND SANITATION
      And user on TradeLicense screen selects on approverDesignation value Sanitary Inspector
      And user on TradeLicense screen selects on approver value Akhila
      And user on TradeLicense screen types on comments value Can be approved
      And user on TradeLicense screen clicks on forward

    ### Logout ###
      And Intent:LogoutIntentTest

    ### On Login to Sanitary Inspector ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 2507
      And user on Login screen types on password value 12345678
      And user on Login screen clicks on signIn

    ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen types on fieldInspection value ok
      And user on TradeLicense screen uploads on fieldInspectionReport value pgrDocument.jpg
      And user on TradeLicense screen selects on approverDepartment value ADMINISTRATION
      And user on TradeLicense screen selects on approverDesignation value Commissioner
      And user on TradeLicense screen selects on approver value Akhila
      And user on TradeLicense screen types on comments value Can be approved
      And user on TradeLicense screen clicks on forward


    ### Logout ###
      And Intent:LogoutIntentTest

    ### On Login to Commissioner ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 2020
      And user on Login screen types on password value 12345678
      And user on Login screen clicks on signIn

    ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen types on comments value Can be approved
      And user on TradeLicense screen clicks on text value Approve

    ### Logout ###
      And Intent:LogoutIntentTest

    ### On Login to creator ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 1212
      And user on Login screen types on password value 12345678
      And user on Login screen clicks on signIn

    ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen will see the menu
      And user on Home screen clicks on menu
      And user on Home screen types on menuSearch value Pay Taxes
      And user on Home screen clicks on firstMenuItem

    ### License Fee Payment ###

      And user on PayTaxes screen verifies text has visible value Pay tax
      And user on PayTaxes screen selects on billingServiceName value Trade License
      And user on PayTaxes screen types on consumerCode value applicationNumber
      And user on PayTaxes screen clicks on text value Search

      And user on PayTaxes screen verifies text has visible value applicationNumber
      And user on PayTaxes screen copies the amountToBePaid to taxAmount
      And user on PayTaxes screen types on amount value taxAmount
      And user on PayTaxes screen types on paidBy value Akhila
      And user on PayTaxes screen clicks on text value Pay

    ### Print License Certificate ###
      And user on Home screen clicks on homeButton
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen clicks on text value Print Certificate
      And user on TradeLicense screen verifies text has visible value Download

      ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen will see the menu
      And user on Home screen clicks on menu
      And user on Home screen types on menuSearch value Search License
      And user on Home screen clicks on firstMenuItem

      ### Search for above created application ###
      And user on TradeLicense screen types on applicationNumberSearch value applicationNumber
      And user on TradeLicense screen clicks on search
      And user on TradeLicense screen clicks on text value applicationNumber
      And user on TradeLicense screen verifies licenseExpiryDate has visible value 31/03/2018

     ### Logout ###
      And Intent:LogoutIntentTest




    Scenario: Create New License with Trade commencement date in previous financial year and validity two year
#      License should not be able to renew and validity expiry date should be 31/03/2018

     ### On Login Screen ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 1212
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
      And user on TradeLicense screen selects on tradeSubCategory value Petrol Bunk
#   And user on TradeLicense screen display on uom value test -->auto populated
      And user on TradeLicense screen types on tradeValueForTheUOM value 20
      And user on TradeLicense screen types on remarks value Trade Details updated successfully
      And user on TradeLicense screen types on tradeCommencementDate value 10/04/2016

    ### Submitting the new license application ###
      And user on TradeLicense screen clicks on text value Submit
      And user on TradeLicense screen clicks on text value View
      And user on TradeLicense screen copies the applicationNumber to applicationNumber

    ### Forwarding application to Sanitary Inspector ###
      And user on Home screen clicks on homeButton
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen selects on approverDepartment value PUBLIC HEALTH AND SANITATION
      And user on TradeLicense screen selects on approverDesignation value Sanitary Inspector
      And user on TradeLicense screen selects on approver value Akhila
      And user on TradeLicense screen types on comments value Can be approved
      And user on TradeLicense screen clicks on forward

    ### Logout ###
      And Intent:LogoutIntentTest

    ### On Login to Sanitary Inspector ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 2507
      And user on Login screen types on password value 12345678
      And user on Login screen clicks on signIn

    ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen types on fieldInspection value ok
      And user on TradeLicense screen uploads on fieldInspectionReport value pgrDocument.jpg
      And user on TradeLicense screen selects on approverDepartment value PUBLIC HEALTH AND SANITATION
      And user on TradeLicense screen selects on approverDesignation value Sanitary Inspector
      And user on TradeLicense screen selects on approver value Akhila
      And user on TradeLicense screen types on comments value Can be approved
      And user on TradeLicense screen clicks on forward


    ### Logout ###
      And Intent:LogoutIntentTest

    ### On Login to Commissioner ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 2020
      And user on Login screen types on password value 12345678
      And user on Login screen clicks on signIn

    ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen types on comments value Can be approved
      And user on TradeLicense screen clicks on text value Approve

    ### Logout ###
      And Intent:LogoutIntentTest

    ### On Login to creator ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 1212
      And user on Login screen types on password value 12345678
      And user on Login screen clicks on signIn

    ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen will see the menu
      And user on Home screen clicks on menu
      And user on Home screen types on menuSearch value Pay Taxes
      And user on Home screen clicks on firstMenuItem

    ### License Fee Payment ###

      And user on PayTaxes screen verifies text has visible value Pay tax
      And user on PayTaxes screen selects on billingServiceName value Trade License
      And user on PayTaxes screen types on consumerCode value applicationNumber
      And user on PayTaxes screen clicks on text value Search

      And user on PayTaxes screen verifies text has visible value applicationNumber
      And user on PayTaxes screen copies the amountToBePaid to taxAmount
      And user on PayTaxes screen types on amount value taxAmount
      And user on PayTaxes screen types on paidBy value Akhila
      And user on PayTaxes screen clicks on text value Pay

    ### Print License Certificate ###
      And user on Home screen clicks on homeButton
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen clicks on text value Print Certificate
      And user on TradeLicense screen verifies text has visible value Download

      ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen will see the menu
      And user on Home screen clicks on menu
      And user on Home screen types on menuSearch value Search License
      And user on Home screen clicks on firstMenuItem

      ### Search for above created application ###
      And user on TradeLicense screen types on applicationNumberSearch value applicationNumber
      And user on TradeLicense screen clicks on search
      And user on TradeLicense screen clicks on text value applicationNumber
      And user on TradeLicense screen verifies licenseExpiryDate has visible value 31/03/2019

     ### Logout ###
      And Intent:LogoutIntentTest




    Scenario: Create New License with Trade commencement date within current financial year and validity one year
#      License should not be able to renew and validity expiry date should be 31/03/2018 - 1016/TL/2017/000065

  ### On Login Screen ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 1212
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
      And user on TradeLicense screen types on tradeCommencementDate value 10/04/2017

    ### Submitting the new license application ###
      And user on TradeLicense screen clicks on text value Submit
      And user on TradeLicense screen clicks on text value View
      And user on TradeLicense screen copies the applicationNumber to applicationNumber

    ### Forwarding application to Sanitary Inspector ###
      And user on Home screen clicks on homeButton
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen selects on approverDepartment value PUBLIC HEALTH AND SANITATION
      And user on TradeLicense screen selects on approverDesignation value Sanitary Inspector
      And user on TradeLicense screen selects on approver value Akhila
      And user on TradeLicense screen types on comments value Can be approved
      And user on TradeLicense screen clicks on forward

    ### Logout ###
      And Intent:LogoutIntentTest

    ### On Login to Sanitary Inspector ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 2507
      And user on Login screen types on password value 12345678
      And user on Login screen clicks on signIn

    ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen types on fieldInspectionDetails value ok
      And user on TradeLicense screen uploads on fieldInspectionFile value pgrDocument.jpg
      And user on TradeLicense screen selects on approverDepartment value PUBLIC HEALTH AND SANITATION
      And user on TradeLicense screen selects on approverDesignation value Sanitary Inspector
      And user on TradeLicense screen selects on approver value Akhila
      And user on TradeLicense screen types on comments value Can be approved
      And user on TradeLicense screen clicks on forward


    ### Logout ###
      And Intent:LogoutIntentTest

    ### On Login to Commissioner ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 2020
      And user on Login screen types on password value 12345678
      And user on Login screen clicks on signIn

    ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen types on comments value Can be approved
      And user on TradeLicense screen clicks on text value Approve

    ### Logout ###
      And Intent:LogoutIntentTest

    ### On Login to creator ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 1212
      And user on Login screen types on password value 12345678
      And user on Login screen clicks on signIn

    ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen will see the menu
      And user on Home screen clicks on menu
      And user on Home screen types on menuSearch value Pay Taxes
      And user on Home screen clicks on firstMenuItem

    ### License Fee Payment ###
      And user on PayTaxes screen verifies text has visible value Pay tax
      And user on PayTaxes screen selects on billingServiceName value Trade License
      And user on PayTaxes screen types on consumerCode value applicationNumber
      And user on PayTaxes screen clicks on text value Search

      And user on PayTaxes screen verifies text has visible value applicationNumber
      And user on PayTaxes screen copies the amountToBePaid to taxAmount
      And user on PayTaxes screen types on amount value taxAmount
      And user on PayTaxes screen types on paidBy value Akhila
      And user on PayTaxes screen clicks on text value Pay

    ### Print License Certificate ###
      And user on Home screen clicks on homeButton
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen clicks on text value Print Certificate
      And user on TradeLicense screen verifies text has visible value Download

    ### Search for above created application ###
      And user on TradeLicense screen types on applicationNumberSearch value applicationNumber
      And user on TradeLicense screen clicks on search
      And user on TradeLicense screen clicks on text value applicationNumber
      And user on TradeLicense screen verifies licenseExpiryDate has visible value 31/03/2018

    ### Logout ###
      And Intent:LogoutIntentTest


    Scenario: Create New License with Trade commencement date within current financial year and validity two year
#      License should not be able to renew and validity expiry date should be 31/03/2019

  ### On Login Screen ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 1212
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
      And user on TradeLicense screen selects on tradeSubCategory value Petrol Bunk
#   And user on TradeLicense screen display on uom value test -->auto populated
      And user on TradeLicense screen types on tradeValueForTheUOM value 20
      And user on TradeLicense screen types on remarks value Trade Details updated successfully
      And user on TradeLicense screen types on tradeCommencementDate value 10/04/2017

    ### Submitting the new license application ###
      And user on TradeLicense screen clicks on text value Submit
      And user on TradeLicense screen clicks on text value View
      And user on TradeLicense screen copies the applicationNumber to applicationNumber

    ### Forwarding application to Sanitary Inspector ###
      And user on Home screen clicks on homeButton
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen selects on approverDepartment value PUBLIC HEALTH AND SANITATION
      And user on TradeLicense screen selects on approverDesignation value Sanitary Inspector
      And user on TradeLicense screen selects on approver value Akhila
      And user on TradeLicense screen types on comments value Can be approved
      And user on TradeLicense screen clicks on forward

    ### Logout ###
      And Intent:LogoutIntentTest

    ### On Login to Sanitary Inspector ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 2507
      And user on Login screen types on password value 12345678
      And user on Login screen clicks on signIn

    ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen types on fieldInspectionDetails value ok
      And user on TradeLicense screen uploads on fieldInspectionFile value pgrDocument.jpg
      And user on TradeLicense screen selects on approverDepartment value PUBLIC HEALTH AND SANITATION
      And user on TradeLicense screen selects on approverDesignation value Sanitary Inspector
      And user on TradeLicense screen selects on approver value Akhila
      And user on TradeLicense screen types on comments value Can be approved
      And user on TradeLicense screen clicks on forward


    ### Logout ###
      And Intent:LogoutIntentTest

    ### On Login to Commissioner ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 2020
      And user on Login screen types on password value 12345678
      And user on Login screen clicks on signIn

    ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen types on comments value Can be approved
      And user on TradeLicense screen clicks on text value Approve

    ### Logout ###
      And Intent:LogoutIntentTest

    ### On Login to creator ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 1212
      And user on Login screen types on password value 12345678
      And user on Login screen clicks on signIn

    ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen will see the menu
      And user on Home screen clicks on menu
      And user on Home screen types on menuSearch value Pay Taxes
      And user on Home screen clicks on firstMenuItem

    ### License Fee Payment ###
      And user on PayTaxes screen verifies text has visible value Pay tax
      And user on PayTaxes screen selects on billingServiceName value Trade License
      And user on PayTaxes screen types on consumerCode value applicationNumber
      And user on PayTaxes screen clicks on text value Search

      And user on PayTaxes screen verifies text has visible value applicationNumber
      And user on PayTaxes screen copies the amountToBePaid to taxAmount
      And user on PayTaxes screen types on amount value taxAmount
      And user on PayTaxes screen types on paidBy value Akhila
      And user on PayTaxes screen clicks on text value Pay

    ### Print License Certificate ###
      And user on Home screen clicks on homeButton
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen clicks on text value Print Certificate
      And user on TradeLicense screen verifies text has visible value Download

    ### Search for above created application ###
      And user on TradeLicense screen types on applicationNumberSearch value applicationNumber
      And user on TradeLicense screen clicks on search
      And user on TradeLicense screen clicks on text value applicationNumber
      And user on TradeLicense screen verifies licenseExpiryDate has visible value 31/03/2019

    ### Logout ###
      And Intent:LogoutIntentTest


    Scenario: Create New License with Trade Commencement date within next financial year and validity one year.
#      License should not be able to renew and validity expiry date should be 31/03/2019 - 1016/TL/2017/000066

  ### On Login Screen ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 1212
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
      And user on TradeLicense screen types on tradeCommencementDate value 10/04/2018

    ### Submitting the new license application ###
      And user on TradeLicense screen clicks on text value Submit
      And user on TradeLicense screen clicks on text value View
      And user on TradeLicense screen copies the applicationNumber to applicationNumber

    ### Forwarding application to Sanitary Inspector ###
      And user on Home screen clicks on homeButton
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen selects on approverDepartment value PUBLIC HEALTH AND SANITATION
      And user on TradeLicense screen selects on approverDesignation value Sanitary Inspector
      And user on TradeLicense screen selects on approver value Akhila
      And user on TradeLicense screen types on comments value Can be approved
      And user on TradeLicense screen clicks on forward

    ### Logout ###
      And Intent:LogoutIntentTest

    ### On Login to Sanitary Inspector ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 2507
      And user on Login screen types on password value 12345678
      And user on Login screen clicks on signIn

    ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen types on fieldInspectionDetails value ok
      And user on TradeLicense screen uploads on fieldInspectionFile value pgrDocument.jpg
      And user on TradeLicense screen selects on approverDepartment value PUBLIC HEALTH AND SANITATION
      And user on TradeLicense screen selects on approverDesignation value Sanitary Inspector
      And user on TradeLicense screen selects on approver value Akhila
      And user on TradeLicense screen types on comments value Can be approved
      And user on TradeLicense screen clicks on forward


    ### Logout ###
      And Intent:LogoutIntentTest

    ### On Login to Commissioner ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 2020
      And user on Login screen types on password value 12345678
      And user on Login screen clicks on signIn

    ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen types on comments value Can be approved
      And user on TradeLicense screen clicks on text value Approve

    ### Logout ###
      And Intent:LogoutIntentTest

    ### On Login to creator ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 1212
      And user on Login screen types on password value 12345678
      And user on Login screen clicks on signIn

    ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen will see the menu
      And user on Home screen clicks on menu
      And user on Home screen types on menuSearch value Pay Taxes
      And user on Home screen clicks on firstMenuItem

    ### License Fee Payment ###
      And user on PayTaxes screen verifies text has visible value Pay tax
      And user on PayTaxes screen selects on billingServiceName value Trade License
      And user on PayTaxes screen types on consumerCode value applicationNumber
      And user on PayTaxes screen clicks on text value Search

      And user on PayTaxes screen verifies text has visible value applicationNumber
      And user on PayTaxes screen copies the amountToBePaid to taxAmount
      And user on PayTaxes screen types on amount value taxAmount
      And user on PayTaxes screen types on paidBy value Akhila
      And user on PayTaxes screen clicks on text value Pay

    ### Print License Certificate ###
      And user on Home screen clicks on homeButton
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen clicks on text value Print Certificate
      And user on TradeLicense screen verifies text has visible value Download

    ### Search for above created application ###
      And user on TradeLicense screen types on applicationNumberSearch value applicationNumber
      And user on TradeLicense screen clicks on search
      And user on TradeLicense screen clicks on text value applicationNumber
      And user on TradeLicense screen verifies licenseExpiryDate has visible value 31/03/2019

    ### Logout ###
      And Intent:LogoutIntentTest


    Scenario: Create New License without changing Trade value for the UOM

      ### On Login Screen ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 1212
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
      And user on TradeLicense screen types on tradeCommencementDate value 10/04/2017

    ### Submitting the new license application ###
      And user on TradeLicense screen clicks on text value Submit
      And user on TradeLicense screen clicks on text value View
      And user on TradeLicense screen copies the applicationNumber to applicationNumber

    ### Forwarding application to Sanitary Inspector ###
      And user on Home screen clicks on homeButton
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen selects on approverDepartment value PUBLIC HEALTH AND SANITATION
      And user on TradeLicense screen selects on approverDesignation value Sanitary Inspector
      And user on TradeLicense screen selects on approver value Akhila
      And user on TradeLicense screen types on comments value Can be approved
      And user on TradeLicense screen clicks on forward

    ### Logout ###
      And Intent:LogoutIntentTest

    ### On Login to Sanitary Inspector ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 2507
      And user on Login screen types on password value 12345678
      And user on Login screen clicks on signIn

    ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen types on fieldInspectionDetails value ok
      And user on TradeLicense screen uploads on fieldInspectionFile value pgrDocument.jpg
      And user on TradeLicense screen selects on approverDepartment value PUBLIC HEALTH AND SANITATION
      And user on TradeLicense screen selects on approverDesignation value Sanitary Inspector
      And user on TradeLicense screen selects on approver value Akhila
      And user on TradeLicense screen types on comments value Can be approved
      And user on TradeLicense screen clicks on forward


    ### Logout ###
      And Intent:LogoutIntentTest

    ### On Login to Commissioner ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 2020
      And user on Login screen types on password value 12345678
      And user on Login screen clicks on signIn

    ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen types on comments value Can be approved
      And user on TradeLicense screen clicks on text value Approve

    ### Logout ###
      And Intent:LogoutIntentTest

    ### On Login to creator ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 1212
      And user on Login screen types on password value 12345678
      And user on Login screen clicks on signIn

    ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen will see the menu
      And user on Home screen clicks on menu
      And user on Home screen types on menuSearch value Pay Taxes
      And user on Home screen clicks on firstMenuItem

    ### License Fee Payment ###
      And user on PayTaxes screen verifies text has visible value Pay tax
      And user on PayTaxes screen selects on billingServiceName value Trade License
      And user on PayTaxes screen types on consumerCode value applicationNumber
      And user on PayTaxes screen clicks on text value Search

      And user on PayTaxes screen verifies text has visible value applicationNumber
      And user on PayTaxes screen copies the amountToBePaid to taxAmount
      And user on PayTaxes screen types on amount value taxAmount
      And user on PayTaxes screen types on paidBy value Akhila
      And user on PayTaxes screen clicks on text value Pay

    ### Print License Certificate ###
      And user on Home screen clicks on homeButton
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen clicks on text value Print Certificate
      And user on TradeLicense screen verifies text has visible value Download

    ### Search for above created application ###
      And user on TradeLicense screen types on applicationNumberSearch value applicationNumber
      And user on TradeLicense screen clicks on search
      And user on TradeLicense screen verifies text has visible value DJ Tools

    ### Logout ###
      And Intent:LogoutIntentTest


  Scenario: Create New License with change in Trade value for the UOM
    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 1212
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
    And user on TradeLicense screen types on tradeCommencementDate value 10/04/2017

    ### Submitting the new license application ###
    And user on TradeLicense screen clicks on text value Submit
    And user on TradeLicense screen clicks on text value View
    And user on TradeLicense screen copies the applicationNumber to applicationNumber

    ### Forwarding application to Sanitary Inspector ###
    And user on Home screen clicks on homeButton
    And user on Home screen types on dashBoardSearch with above applicationNumber
    And user on TradeLicense screen selects on approverDepartment value PUBLIC HEALTH AND SANITATION
    And user on TradeLicense screen selects on approverDesignation value Sanitary Inspector
    And user on TradeLicense screen selects on approver value Akhila
    And user on TradeLicense screen types on comments value Can be approved
    And user on TradeLicense screen clicks on forward

    ### Logout ###
    And Intent:LogoutIntentTest

    ### On Login to Sanitary Inspector ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 2507
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen types on dashBoardSearch with above applicationNumber
    And user on TradeLicense screen types on tradeValueForUOM value 1200
    And user on TradeLicense screen types on fieldInspectionDetails value ok
    And user on TradeLicense screen uploads on fieldInspectionFile value pgrDocument.jpg
    And user on TradeLicense screen selects on approverDepartment value PUBLIC HEALTH AND SANITATION
    And user on TradeLicense screen selects on approverDesignation value Sanitary Inspector
    And user on TradeLicense screen selects on approver value Akhila
    And user on TradeLicense screen types on comments value Can be approved
    And user on TradeLicense screen clicks on forward


    ### Logout ###
    And Intent:LogoutIntentTest

    ### On Login to Commissioner ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 2020
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen types on dashBoardSearch with above applicationNumber
    And user on TradeLicense screen types on comments value Can be approved
    And user on TradeLicense screen clicks on text value Approve

    ### Logout ###
    And Intent:LogoutIntentTest

    ### On Login to creator ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 1212
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Pay Taxes
    And user on Home screen clicks on firstMenuItem

    ### License Fee Payment ###
    And user on PayTaxes screen verifies text has visible value Pay tax
    And user on PayTaxes screen selects on billingServiceName value Trade License
    And user on PayTaxes screen types on consumerCode value applicationNumber
    And user on PayTaxes screen clicks on text value Search

    And user on PayTaxes screen verifies text has visible value applicationNumber
    And user on PayTaxes screen copies the amountToBePaid to taxAmount
    And user on PayTaxes screen types on amount value taxAmount
    And user on PayTaxes screen types on paidBy value Akhila
    And user on PayTaxes screen clicks on text value Pay

    ### Print License Certificate ###
    And user on Home screen clicks on homeButton
    And user on Home screen types on dashBoardSearch with above applicationNumber
    And user on TradeLicense screen clicks on text value Print Certificate
    And user on TradeLicense screen verifies text has visible value Download

    ### Search for above created application ###
    And user on TradeLicense screen types on applicationNumberSearch value applicationNumber
    And user on TradeLicense screen clicks on search
    And user on TradeLicense screen verifies text has visible value DJ Tools

    ### Logout ###
    And Intent:LogoutIntentTest

  Scenario: Create New License, when application is in commissioner inbox change fee matrix - pending


    Scenario: Create New License without defining fee matrix
      ### On Login Screen ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 1212
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
      And user on TradeLicense screen selects on tradeSubCategory value Gas Agency
#   And user on TradeLicense screen display on uom value test -->auto populated
      And user on TradeLicense screen types on tradeValueForTheUOM value 20
      And user on TradeLicense screen types on remarks value Trade Details updated successfully
      And user on TradeLicense screen types on tradeCommencementDate value 10/04/2017

    ### Submitting the new license application ###
      And user on TradeLicense screen clicks on text value Submit
      And user on TradeLicense screen clicks on text value View
      And user on TradeLicense screen copies the applicationNumber to applicationNumber

    ### Forwarding application to Sanitary Inspector ###
      And user on Home screen clicks on homeButton
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen selects on approverDepartment value PUBLIC HEALTH AND SANITATION
      And user on TradeLicense screen selects on approverDesignation value Sanitary Inspector
      And user on TradeLicense screen selects on approver value Akhila
      And user on TradeLicense screen types on comments value Can be approved
      And user on TradeLicense screen clicks on forward

    ### Logout ###
      And Intent:LogoutIntentTest

    ### On Login to Sanitary Inspector ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 2507
      And user on Login screen types on password value 12345678
      And user on Login screen clicks on signIn

    ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen types on tradeValueForUOM value 1200
      And user on TradeLicense screen types on fieldInspectionDetails value ok
      And user on TradeLicense screen uploads on fieldInspectionFile value pgrDocument.jpg
      And user on TradeLicense screen selects on approverDepartment value PUBLIC HEALTH AND SANITATION
      And user on TradeLicense screen selects on approverDesignation value Sanitary Inspector
      And user on TradeLicense screen selects on approver value Akhila
      And user on TradeLicense screen types on comments value Can be approved
      And user on TradeLicense screen clicks on forward
      And user on TradeLicense screen verifies validationMsg has visible value Fee Matrix not defined

    Scenario: Create New License without defining range in the fee matrix
### On Login Screen ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 1212
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
      And user on TradeLicense screen types on tradeValueForTheUOM value 999999
      And user on TradeLicense screen types on remarks value Trade Details updated successfully
      And user on TradeLicense screen types on tradeCommencementDate value 10/04/2017

    ### Submitting the new license application ###
      And user on TradeLicense screen clicks on text value Submit
      And user on TradeLicense screen clicks on text value View
      And user on TradeLicense screen copies the applicationNumber to applicationNumber

    ### Forwarding application to Sanitary Inspector ###
      And user on Home screen clicks on homeButton
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen selects on approverDepartment value PUBLIC HEALTH AND SANITATION
      And user on TradeLicense screen selects on approverDesignation value Sanitary Inspector
      And user on TradeLicense screen selects on approver value Akhila
      And user on TradeLicense screen types on comments value Can be approved
      And user on TradeLicense screen clicks on forward

    ### Logout ###
      And Intent:LogoutIntentTest

    ### On Login to Sanitary Inspector ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 2507
      And user on Login screen types on password value 12345678
      And user on Login screen clicks on signIn

    ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen types on tradeValueForUOM value 1200
      And user on TradeLicense screen types on fieldInspectionDetails value ok
      And user on TradeLicense screen uploads on fieldInspectionFile value pgrDocument.jpg
      And user on TradeLicense screen selects on approverDepartment value PUBLIC HEALTH AND SANITATION
      And user on TradeLicense screen selects on approverDesignation value Sanitary Inspector
      And user on TradeLicense screen selects on approver value Akhila
      And user on TradeLicense screen types on comments value Can be approved
      And user on TradeLicense screen clicks on forward
      And user on TradeLicense screen verifies validationMsg has visible value UOM Range not defined




    Scenario: Rejection of new license by Junior Assistant after application submitted.
      ### On Login Screen ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 1212
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
      And user on TradeLicense screen types on tradeValueForTheUOM value 999999
      And user on TradeLicense screen types on remarks value Trade Details updated successfully
      And user on TradeLicense screen types on tradeCommencementDate value 10/04/2017

    ### Submitting the new license application ###
      And user on TradeLicense screen clicks on text value Submit
      And user on TradeLicense screen clicks on text value View
      And user on TradeLicense screen copies the applicationNumber to applicationNumber

    ### Forwarding application to Sanitary Inspector ###
      And user on Home screen clicks on homeButton
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen types on comments value Address Proof required
      And user on TradeLicense screen clicks on text value Reject

    Scenario: Rejection of new license by SI
      ### On Login Screen ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 1212
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
      And user on TradeLicense screen types on tradeCommencementDate value 10/04/2017

    ### Submitting the new license application ###
      And user on TradeLicense screen clicks on text value Submit
      And user on TradeLicense screen clicks on text value View
      And user on TradeLicense screen copies the applicationNumber to applicationNumber

    ### Forwarding application to Sanitary Inspector ###
      And user on Home screen clicks on homeButton
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen selects on approverDepartment value PUBLIC HEALTH AND SANITATION
      And user on TradeLicense screen selects on approverDesignation value Sanitary Inspector
      And user on TradeLicense screen selects on approver value Akhila
      And user on TradeLicense screen types on comments value Can be approved
      And user on TradeLicense screen clicks on forward

    ### Logout ###
      And Intent:LogoutIntentTest

    ### On Login to Sanitary Inspector ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 2507
      And user on Login screen types on password value 12345678
      And user on Login screen clicks on signIn

    ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen types on comments value Rejected
      And user on TradeLicense screen clicks on text value Rejected

      ### Logout ###
      And Intent:LogoutIntentTest

      ### On Login Screen ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 1212
      And user on Login screen types on password value 12345678
      And user on Login screen clicks on signIn

      ### Creator cancel the application ###
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen types on comments value Resubmit
      And user on TradeLicense screen clicks on text value Cancel
      And user on TradeLicense screen clicks on confirmToCancel
      And user on TradeLicense screen verifies text has visible value Rejection Letter

      ### Logout ###
      And Intent:LogoutIntentTest


    Scenario: Rejection of new license by Commmissioner

      ### On Login Screen ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 1212
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
      And user on TradeLicense screen types on tradeCommencementDate value 10/04/2017

    ### Submitting the new license application ###
      And user on TradeLicense screen clicks on text value Submit
      And user on TradeLicense screen clicks on text value View
      And user on TradeLicense screen copies the applicationNumber to applicationNumber

    ### Forwarding application to Sanitary Inspector ###
      And user on Home screen clicks on homeButton
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen selects on approverDepartment value PUBLIC HEALTH AND SANITATION
      And user on TradeLicense screen selects on approverDesignation value Sanitary Inspector
      And user on TradeLicense screen selects on approver value Akhila
      And user on TradeLicense screen types on comments value Can be approved
      And user on TradeLicense screen clicks on forward

    ### Logout ###
      And Intent:LogoutIntentTest

    ### On Login to Sanitary Inspector ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 2507
      And user on Login screen types on password value 12345678
      And user on Login screen clicks on signIn

    ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen types on fieldInspectionDetails value ok
      And user on TradeLicense screen uploads on fieldInspectionFile value pgrDocument.jpg
      And user on TradeLicense screen selects on approverDepartment value PUBLIC HEALTH AND SANITATION
      And user on TradeLicense screen selects on approverDesignation value Sanitary Inspector
      And user on TradeLicense screen selects on approver value Akhila
      And user on TradeLicense screen types on comments value Can be approved
      And user on TradeLicense screen clicks on forward


    ### Logout ###
      And Intent:LogoutIntentTest

    ### On Login to Commissioner ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 2020
      And user on Login screen types on password value 12345678
      And user on Login screen clicks on signIn

      ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen types on comments value Rejected
      And user on TradeLicense screen clicks on text value Rejected

      ### Logout ###
      And Intent:LogoutIntentTest

      ### On Login Screen ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value 1212
      And user on Login screen types on password value 12345678
      And user on Login screen clicks on signIn

      ### Creator cancel the application ###
      And user on Home screen types on dashBoardSearch with above applicationNumber
      And user on TradeLicense screen types on comments value Resubmit
      And user on TradeLicense screen clicks on text value Cancel
      And user on TradeLicense screen clicks on confirmToCancel
      And user on TradeLicense screen verifies text has visible value Rejection Letter

      ### Logout ###
      And Intent:LogoutIntentTest


  Scenario: Modify fields when application is rejected by SI

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 1212
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
    And user on TradeLicense screen types on tradeCommencementDate value 10/04/2017

    ### Submitting the new license application ###
    And user on TradeLicense screen clicks on text value Submit
    And user on TradeLicense screen clicks on text value View
    And user on TradeLicense screen copies the applicationNumber to applicationNumber

    ### Forwarding application to Sanitary Inspector ###
    And user on Home screen clicks on homeButton
    And user on Home screen types on dashBoardSearch with above applicationNumber
    And user on TradeLicense screen selects on approverDepartment value PUBLIC HEALTH AND SANITATION
    And user on TradeLicense screen selects on approverDesignation value Sanitary Inspector
    And user on TradeLicense screen selects on approver value Akhila
    And user on TradeLicense screen types on comments value Can be approved
    And user on TradeLicense screen clicks on forward

    ### Logout ###
    And Intent:LogoutIntentTest

    ### On Login to Sanitary Inspector ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 2507
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen types on dashBoardSearch with above applicationNumber
    And user on TradeLicense screen types on comments value Rejected
    And user on TradeLicense screen clicks on text value Rejected

      ### Logout ###
    And Intent:LogoutIntentTest

      ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 1212
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

      ### Creator cancel the application ###
    And user on Home screen types on dashBoardSearch with above applicationNumber
    And user on TradeLicense screen types on aadhaarNumber value 111111111111
    And user on TradeLicense screen types on mobileNumber value 3333333333
    And user on TradeLicense screen types on tradeOwnerName value Akhila GD
    And user on TradeLicense screen types on fatherName value Divakara GK
    And user on TradeLicense screen types on email value abc@xyz.com
    And user on TradeLicense screen types on tradeOwnerAddress value Bangalore, 560102

    ### On Create New License Screen entering trade locaiton details ###
#    And user on TradeLicense screen types on propertyAssessmentNumber value 1016000009
    And user on TradeLicense screen selects on locality value Bank Road
    And user on TradeLicense screen selects on adminWard value Election Ward No 1
    And user on TradeLicense screen selects on revenueWard value Revenue Ward No 1
    And user on TradeLicense screen selects on ownershipType value RENTED
    And user on TradeLicense screen types on tradeAddress value BTM,Bangalore 5600102

    ### On Create New License Screen entering trade details ###
    And user on TradeLicense screen types on tradeTitle value DJ Tools and machine
    And user on TradeLicense screen selects on tradeType value PERMANENT
    And user on TradeLicense screen selects on tradeCategory value Flammables
    And user on TradeLicense screen selects on tradeSubCategory value Camphor
#   And user on TradeLicense screen display on uom value test -->auto populated
    And user on TradeLicense screen types on tradeValueForTheUOM value 20
    And user on TradeLicense screen types on remarks value Trade Details updated successfully
    And user on TradeLicense screen types on tradeCommencementDate value 10/04/2018

    ### Enter approvaer details ###
    And user on TradeLicense screen selects on approverDepartment value PUBLIC HEALTH AND SANITATION
    And user on TradeLicense screen selects on approverDesignation value Sanitary Inspector
    And user on TradeLicense screen selects on approver value Akhila
    And user on TradeLicense screen types on comments value Can be approved
    And user on TradeLicense screen clicks on text value Forward

    ###  Verifying the changes ###
    And user on TradeLicense screen verifies text has visible value Akhila GD


  Scenario: Modify license application rejected by commissioner

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 1212
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
    And user on TradeLicense screen types on tradeCommencementDate value 10/04/2017

    ### Submitting the new license application ###
    And user on TradeLicense screen clicks on text value Submit
    And user on TradeLicense screen clicks on text value View
    And user on TradeLicense screen copies the applicationNumber to applicationNumber

    ### Forwarding application to Sanitary Inspector ###
    And user on Home screen clicks on homeButton
    And user on Home screen types on dashBoardSearch with above applicationNumber
    And user on TradeLicense screen selects on approverDepartment value PUBLIC HEALTH AND SANITATION
    And user on TradeLicense screen selects on approverDesignation value Sanitary Inspector
    And user on TradeLicense screen selects on approver value Akhila
    And user on TradeLicense screen types on comments value Can be approved
    And user on TradeLicense screen clicks on forward

    ### Logout ###
    And Intent:LogoutIntentTest

    ### On Login to Sanitary Inspector ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 2507
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen types on dashBoardSearch with above applicationNumber
    And user on TradeLicense screen types on fieldInspectionDetails value ok
    And user on TradeLicense screen uploads on fieldInspectionFile value pgrDocument.jpg
    And user on TradeLicense screen selects on approverDepartment value PUBLIC HEALTH AND SANITATION
    And user on TradeLicense screen selects on approverDesignation value Sanitary Inspector
    And user on TradeLicense screen selects on approver value Akhila
    And user on TradeLicense screen types on comments value Can be approved
    And user on TradeLicense screen clicks on forward


    ### Logout ###
    And Intent:LogoutIntentTest

    ### On Login to Commissioner ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 2020
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

      ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen types on dashBoardSearch with above applicationNumber
    And user on TradeLicense screen types on comments value Rejected
    And user on TradeLicense screen clicks on text value Rejected

      ### Logout ###
    And Intent:LogoutIntentTest

      ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 1212
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

      ### Creator cancel the application ###
    And user on Home screen types on dashBoardSearch with above applicationNumber
    And user on TradeLicense screen types on aadhaarNumber value 111111111111
    And user on TradeLicense screen types on mobileNumber value 3333333333
    And user on TradeLicense screen types on tradeOwnerName value Akhila GD
    And user on TradeLicense screen types on fatherName value Divakara GK
    And user on TradeLicense screen types on email value abc@xyz.com
    And user on TradeLicense screen types on tradeOwnerAddress value Bangalore, 560102

    ### On Create New License Screen entering trade locaiton details ###
#    And user on TradeLicense screen types on propertyAssessmentNumber value 1016000009
    And user on TradeLicense screen selects on locality value Bank Road
    And user on TradeLicense screen selects on adminWard value Election Ward No 1
    And user on TradeLicense screen selects on revenueWard value Revenue Ward No 1
    And user on TradeLicense screen selects on ownershipType value RENTED
    And user on TradeLicense screen types on tradeAddress value BTM,Bangalore 5600102

    ### On Create New License Screen entering trade details ###
    And user on TradeLicense screen types on tradeTitle value DJ Tools and machine
    And user on TradeLicense screen selects on tradeType value PERMANENT
    And user on TradeLicense screen selects on tradeCategory value Flammables
    And user on TradeLicense screen selects on tradeSubCategory value Camphor
#   And user on TradeLicense screen display on uom value test -->auto populated
    And user on TradeLicense screen types on tradeValueForTheUOM value 20
    And user on TradeLicense screen types on remarks value Trade Details updated successfully
    And user on TradeLicense screen types on tradeCommencementDate value 10/04/2018

    ### Enter approvaer details ###
    And user on TradeLicense screen selects on approverDepartment value PUBLIC HEALTH AND SANITATION
    And user on TradeLicense screen selects on approverDesignation value Sanitary Inspector
    And user on TradeLicense screen selects on approver value Akhila
    And user on TradeLicense screen types on comments value Can be approved
    And user on TradeLicense screen clicks on text value Forward

    ###  Verifying the changes ###
    And user on TradeLicense screen verifies text has visible value Akhila GD


    Scenario: Rejection of new license after commissioner approval and before fee payment
#    1016/TL/2017/000107 - License fee to be paid not showing - before collection only its showing Print Cerftificate option
#  User cannot reject the application after commissioner approval

  Scenario: Create New License with Property Assessment Number - pending

