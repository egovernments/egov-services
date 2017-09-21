Feature: In this feature we are going to Create Sub-Category for Trade License

  Scenario: Create Search And Update Sub Category with Valid Data

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value elzan
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen verifies myTasks has visible value My Tasks
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create Sub Category
    And user on Home screen clicks on firstMenuItem

    ### CreateSubCategory ###
    And user on TLSubCategoryMaster screen selects on category value Flammables
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
    And user on Home screen types on menuSearch value View Sub Category
    And user on Home screen clicks on firstMenuItem

    #ViewSubCategory
    And user on TLSubCategoryMaster screen will wait until the page loads
    And user on TLSubCategoryMaster screen refresh's the webpage
    And user on TLSubCategoryMaster screen verifies text has visible value Search License Sub-Category
    And user on TLSubCategoryMaster screen selects on viewCategoryName value categoryName
    And user on TLSubCategoryMaster screen selects on viewSubCategoryName value subCategoryName
    And user on TLSubCategoryMaster screen clicks on text value Search
    And user on TLSubCategoryMaster screen verifies text has visible value subCategoryName

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Modify Sub Category
    And user on Home screen clicks on firstMenuItem

    #ModifySubCateogry
    And user on TLSubCategoryMaster screen will wait until the page loads
    And user on TLSubCategoryMaster screen refresh's the webpage
    And user on TLSubCategoryMaster screen verifies text has visible value Search License Sub-Category
    And user on TLSubCategoryMaster screen selects on viewCategoryName value categoryName
    And user on TLSubCategoryMaster screen selects on viewSubCategoryName value subCategoryName
    And user on TLSubCategoryMaster screen clicks on text value Search
    And user on TLSubCategoryMaster screen clicks on searchResultSubCategoryName value subCategoryName
    And user on TLSubCategoryMaster screen types on updateSubCategoryName value "UpdatedSubCategoryName ",3 random characters
    And user on TLSubCategoryMaster screen copies the updateSubCategoryName to subCategoryName
    And user on TLSubCategoryMaster screen clicks on text value Update

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value View Sub Category
    And user on Home screen clicks on firstMenuItem

    #ViewSubCategory
    And user on TLSubCategoryMaster screen will wait until the page loads
    And user on TLSubCategoryMaster screen refresh's the webpage
    And user on TLSubCategoryMaster screen verifies text has visible value Search License Sub-Category
    And user on TLSubCategoryMaster screen selects on viewCategoryName value categoryName
    And user on TLSubCategoryMaster screen selects on viewSubCategoryName value subCategoryName
    And user on TLSubCategoryMaster screen clicks on text value Search
    And user on TLSubCategoryMaster screen verifies text has visible value subCategoryName

    ### Logout ###
    And Intent:LogoutIntentTest



