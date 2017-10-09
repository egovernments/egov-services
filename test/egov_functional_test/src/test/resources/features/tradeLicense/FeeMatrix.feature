Feature: In this feature we are going to Create Fee Matrix for Trade License
  Tested on 03.10.2017
  Scenario: Create fee matrix for  application type: new and renew, Nature of Business: PERMANENT and TEMPORARY

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
    And user on TLCategoryMaster screen type on categoryName value "Category ",3 random characters
    And user on TLCategoryMaster screen type on categoryCode value "CategoryCode",3 random numbers
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
    And user on TLSubCategoryMaster screen types on name value "SubCategory ",3 random characters
    And user on TLSubCategoryMaster screen copies the category to categoryName
    And user on TLSubCategoryMaster screen copies the name to subCategoryName
    And user on TLSubCategoryMaster screen types on code value "SubCategoryCode",3 random numbers
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
    And user on Home screen types on menuSearch value View Fee Matrix
    And user on Home screen clicks on firstMenuItem

    ### On Search Fee Matrix screen ###
    And user on TLFeeMatrixMaster screen refresh's the webpage
    And user on TLFeeMatrixMaster screen selects on searchCategory value categoryNameValue
    And user on TLFeeMatrixMaster screen selects on searchSubCategory value subCategoryName
    And user on TLFeeMatrixMaster screen clicks on text value Search
    And user on TLFeeMatrixMaster screen verifies text has visible value LICENSE




     ### Logout ###
    And Intent:LogoutIntentTest

#  Scenario: Create fee matrix for application type: new and Nature of Business: PERMANENT and TEMPORARY - pending as renewal story not yet implemented
  Scenario: Create fee matrix for application type: new and renew, Nature of Business: PERMANENT
  Scenario: Create fee matrix for application type: new and renew, Nature of Business: TEMPORARY

  Scenario: Create duplicate fee matrix for given combination
#    EGSVC-1634 - raised issue on 03.10.2017
  Scenario: Define fee matrix for 2013-14 and 2017-18 and create a new license application for 01.01.2017
#    It should calculate the license fee defined for 2013-14 could not create fee matrix for 2017-18 hence pending

