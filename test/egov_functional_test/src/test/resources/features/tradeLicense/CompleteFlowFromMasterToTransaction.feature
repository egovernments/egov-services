Feature: Completing the workflow for new license application from creating master till end of the workflow


  Scenario: Complete workflow for new license

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 1212
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen verifies myTasks has visible value My Tasks
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create License Category
    And user on Home screen clicks on firstMenuItem

    ### On Create Category Screen ###
    And user on TLCategoryMaster screen type on categoryName value "Category ",4 random characters
    And user on TLCategoryMaster screen type on categoryCode value "CategoryCode",4 random numbers
    And user on TLCategoryMaster screen copies the categoryName to categoryNameValue
    And user on TLCategoryMaster screen clicks on text value Create

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create Sub Category
    And user on Home screen clicks on firstMenuItem

    ### CreateSubCategory ###
    And user on TLSubCategoryMaster screen selects on category value categoryNameValue
    And user on TLSubCategoryMaster screen types on name value "SubCategory ",4 random characters
    And user on TLSubCategoryMaster screen copies the category to categoryName
    And user on TLSubCategoryMaster screen copies the name to subCategoryName
    And user on TLSubCategoryMaster screen types on code value "SubCategoryCode",4 random numbers
    And user on TLSubCategoryMaster screen types on validityYears value 2
    And user on TLSubCategoryMaster screen selects on feeType value LICENSE
    And user on TLSubCategoryMaster screen selects on rateType value FLAT BY RANGE
    And user on TLSubCategoryMaster screen selects on uomId value Area - Sq Ft
    And user on TLSubCategoryMaster screen clicks on text value Create


    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create Fee Matrix
    And user on Home screen clicks on firstMenuItem

    ### On Create Fee Matrix Screen ###
    And user on TLFeeMatrixMaster screen selects on category value categoryNameValue
    And user on TLFeeMatrixMaster screen selects on subCategory value subCategoryName
    And user on TLFeeMatrixMaster screen selects on feeType value LICENSE
    And user on TLFeeMatrixMaster screen selects on effectiveFinancialYear value 2017-18
    And user on TLFeeMatrixMaster screen types on UOMToRange value 2000
    And user on TLFeeMatrixMaster screen types on AmountValue value 100
    And user on TLFeeMatrixMaster screen clicks on text value Create


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
    And user on TradeLicense screen selects on locality value Adarashanagar
    And user on TradeLicense screen selects on adminWard value Election Ward No 1
    And user on TradeLicense screen selects on revenueWard value Revenue Ward No 1
    And user on TradeLicense screen selects on ownershipType value OWNED
    And user on TradeLicense screen types on tradeAddress value BTM,Bangalore

    ### On Create New License Screen entering trade details ###
    And user on TradeLicense screen types on tradeTitle value DJ Tools
    And user on TradeLicense screen selects on tradeType value PERMANENT
    And user on TradeLicense screen selects on tradeCategory value categoryNameValue
    And user on TradeLicense screen selects on tradeSubCategory value subCategoryName
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
