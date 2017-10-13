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
    And user on Home screen types on menuSearch value Create License Category
    And user on Home screen clicks on firstMenuItem

    ### On Create Category Screen ###
    And user on TLCategoryMaster screen type on categoryName value "Category ",3 random characters
    And user on TLCategoryMaster screen type on categoryCode value "CategoryCode",3 random numbers
    And user on TLCategoryMaster screen copies the categoryName to categoryNameValue
    And user on TLCategoryMaster screen clicks on text value Create

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen verifies myTasks has visible value My Tasks
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
    And user on Home screen types on menuSearch value View Sub Category
    And user on Home screen clicks on firstMenuItem

    ### On View Sub Category Screen ###
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

    ### On Modify Sub Category Screen ###
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

    ### On View Sub Category Screen ###
    And user on TLSubCategoryMaster screen will wait until the page loads
    And user on TLSubCategoryMaster screen refresh's the webpage
    And user on TLSubCategoryMaster screen verifies text has visible value Search License Sub-Category
    And user on TLSubCategoryMaster screen selects on viewCategoryName value categoryName
    And user on TLSubCategoryMaster screen selects on viewSubCategoryName value subCategoryName
    And user on TLSubCategoryMaster screen clicks on text value Search
    And user on TLSubCategoryMaster screen verifies text has visible value subCategoryName

    ### Logout ###
    And Intent:LogoutIntentTest

    Scenario: Create Sub Category with existing sub category name

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
      And user on TLSubCategoryMaster screen selects on category value AFlammables
      And user on TLSubCategoryMaster screen types on name value Petrol Bunk
#      enter duplicate name
      And user on TLSubCategoryMaster screen copies the category to categoryName
      And user on TLSubCategoryMaster screen copies the name to subCategoryName
      And user on TLSubCategoryMaster screen types on code value "SubCategoryCode",3 random numbers
      And user on TLSubCategoryMaster screen types on validityYears value 2
      And user on TLSubCategoryMaster screen selects on feeType value LICENSE
      And user on TLSubCategoryMaster screen selects on rateType value FLAT BY RANGE
      And user on TLSubCategoryMaster screen selects on uomId value Area - Sq Ft
      And user on TLSubCategoryMaster screen clicks on text value Create
      And user on TLSubCategoryMaster screen verifies text has visible value Found duplicate Sub Category name, please provide another Sub Category Name.

    Scenario: Create Sub Category with existing sub category code

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
      And user on TLSubCategoryMaster screen types on code value PB
#    enter duplicate code
      And user on TLSubCategoryMaster screen types on validityYears value 2
      And user on TLSubCategoryMaster screen selects on feeType value MOTOR
      And user on TLSubCategoryMaster screen selects on rateType value FLAT BY RANGE
      And user on TLSubCategoryMaster screen selects on uomId value Area - Sq Ft
      And user on TLSubCategoryMaster screen clicks on text value Create
      And user on TLSubCategoryMaster screen verifies validationMsg has visible value Found duplicate Sub Category code, please provide another Sub Category Code.


  Scenario: Create Sub Category with existing sub category name and code combination
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
    And user on TLSubCategoryMaster screen types on name value Petrol Bunk
    And user on TLSubCategoryMaster screen copies the category to categoryName
    And user on TLSubCategoryMaster screen copies the name to subCategoryName
#    enter duplicate name
    And user on TLSubCategoryMaster screen types on code value PB
#    enter duplicate code
    And user on TLSubCategoryMaster screen types on validityYears value 2
    And user on TLSubCategoryMaster screen selects on feeType value LICENSE
    And user on TLSubCategoryMaster screen selects on rateType value FLAT BY RANGE
    And user on TLSubCategoryMaster screen selects on uomId value Area - Sq Ft
    And user on TLSubCategoryMaster screen clicks on text value Create
    And user on TLSubCategoryMaster screen verifies validationMsg has visible value Found duplicate Sub Category name, please provide another Sub Category Name.


  Scenario: Create Sub Category with same Fee Type in 2 rows

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
    And user on TLSubCategoryMaster screen types on name value Petrol Bunk
    And user on TLSubCategoryMaster screen copies the category to categoryName
    And user on TLSubCategoryMaster screen copies the name to subCategoryName
    And user on TLSubCategoryMaster screen types on code value PB
    And user on TLSubCategoryMaster screen types on validityYears value 2
    And user on TLSubCategoryMaster screen selects on feeType value LICENSE
    And user on TLSubCategoryMaster screen selects on rateType value FLAT BY RANGE
    And user on TLSubCategoryMaster screen selects on uomId value Area - Sq Ft
    And user on TLSubCategoryMaster screen clicks on add
    And user on TLSubCategoryMaster screen selects on feeType value LICENSE
    And user on TLSubCategoryMaster screen selects on rateType value FLAT BY RANGE
    And user on TLSubCategoryMaster screen selects on uomId value Area - Sq Ft
    And user on TLSubCategoryMaster screen clicks on text value Create
    And user on TLSubCategoryMaster screen verifies validationMsg has visible value Found duplicate SubCategory detail, please provide another SubCategory detail.


  Scenario: Create inactive Sub Category

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
    And user on TLSubCategoryMaster screen cicks on activeCheckBox
#    to deactivate the sub category
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

    ### On View Sub Category Screen ###
    And user on TLSubCategoryMaster screen will wait until the page loads
    And user on TLSubCategoryMaster screen refresh's the webpage
    And user on TLSubCategoryMaster screen verifies text has visible value Search License Sub-Category
    And user on TLSubCategoryMaster screen selects on viewCategoryName value categoryName
    And user on TLSubCategoryMaster screen selects on viewSubCategoryName value subCategoryName
    And user on TLSubCategoryMaster screen clicks on text value Search
    And user on TLSubCategoryMaster screen verifies text has visible value subCategoryName
    And user on TLSubCategoryMaster screen verifies active has visible value NO

  Scenario: Create sub category with multiple fee types and same Rate type

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
      And user on TLSubCategoryMaster screen types on name value Petrol Bunk
      And user on TLSubCategoryMaster screen copies the category to categoryName
      And user on TLSubCategoryMaster screen copies the name to subCategoryName
      And user on TLSubCategoryMaster screen types on code value PB
      And user on TLSubCategoryMaster screen types on validityYears value 2
      And user on TLSubCategoryMaster screen selects on feeType value LICENSE
      And user on TLSubCategoryMaster screen selects on rateType value FLAT BY RANGE
      And user on TLSubCategoryMaster screen selects on uomId value Area - Sq Ft
      And user on TLSubCategoryMaster screen clicks on add
      And user on TLSubCategoryMaster screen selects on feeType value MOTOR
      And user on TLSubCategoryMaster screen selects on rateType value FLAT BY RANGE
      And user on TLSubCategoryMaster screen selects on uomId value Area - Sq Ft
      And user on TLSubCategoryMaster screen clicks on text value Create

      ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen will see the menu
      And user on Home screen clicks on menu
      And user on Home screen types on menuSearch value View Sub Category
      And user on Home screen clicks on firstMenuItem

    ### On View Sub Category Screen ###
      And user on TLSubCategoryMaster screen will wait until the page loads
      And user on TLSubCategoryMaster screen refresh's the webpage
      And user on TLSubCategoryMaster screen verifies text has visible value Search License Sub-Category
      And user on TLSubCategoryMaster screen selects on viewCategoryName value categoryName
      And user on TLSubCategoryMaster screen selects on viewSubCategoryName value subCategoryName
      And user on TLSubCategoryMaster screen clicks on text value Search
      And user on TLSubCategoryMaster screen verifies text has visible value subCategoryName


  Scenario: Create Sub Category screen field validations - pending
  Scenario: Create sub category with multiple fee type and different rate type

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
    And user on TLSubCategoryMaster screen types on name value Petrol Bunk
    And user on TLSubCategoryMaster screen copies the category to categoryName
    And user on TLSubCategoryMaster screen copies the name to subCategoryName
    And user on TLSubCategoryMaster screen types on code value PB
    And user on TLSubCategoryMaster screen types on validityYears value 2
    And user on TLSubCategoryMaster screen selects on feeType value LICENSE
    And user on TLSubCategoryMaster screen selects on rateType value FLAT BY RANGE
    And user on TLSubCategoryMaster screen selects on uomId value Area - Sq Ft
    And user on TLSubCategoryMaster screen clicks on add
    And user on TLSubCategoryMaster screen selects on feeType value MOTOR
    And user on TLSubCategoryMaster screen selects on rateType value FLAT BY UNIT
    And user on TLSubCategoryMaster screen selects on uomId value Area - Sq Ft
    And user on TLSubCategoryMaster screen clicks on text value Create

      ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value View Sub Category
    And user on Home screen clicks on firstMenuItem

    ### On View Sub Category Screen ###
    And user on TLSubCategoryMaster screen will wait until the page loads
    And user on TLSubCategoryMaster screen refresh's the webpage
    And user on TLSubCategoryMaster screen verifies text has visible value Search License Sub-Category
    And user on TLSubCategoryMaster screen selects on viewCategoryName value categoryName
    And user on TLSubCategoryMaster screen selects on viewSubCategoryName value subCategoryName
    And user on TLSubCategoryMaster screen clicks on text value Search
    And user on TLSubCategoryMaster screen verifies text has visible value subCategoryName

    Scenario: Modify Sub Category name with already existing name

      ### On Login Screen ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value elzan
      And user on Login screen types on password value 12345678
      And user on Login screen clicks on signIn

      ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen will see the menu
      And user on Home screen clicks on menu
      And user on Home screen types on menuSearch value Modify Sub Category
      And user on Home screen clicks on firstMenuItem

    ### On Modify Sub Category Screen ###
      And user on TLSubCategoryMaster screen will wait until the page loads
      And user on TLSubCategoryMaster screen refresh's the webpage
      And user on TLSubCategoryMaster screen verifies text has visible value Search License Sub-Category
      And user on TLSubCategoryMaster screen selects on viewCategoryName value categoryName
      And user on TLSubCategoryMaster screen selects on viewSubCategoryName value subCategoryName
#      enter duplicate sub category name
      And user on TLSubCategoryMaster screen clicks on text value Search
      And user on TLSubCategoryMaster screen clicks on searchResultSubCategoryName value subCategoryName
      And user on TLSubCategoryMaster screen types on updateSubCategoryName value "UpdatedSubCategoryName ",3 random characters
      And user on TLSubCategoryMaster screen copies the updateSubCategoryName to subCategoryName
      And user on TLSubCategoryMaster screen clicks on text value Update
      And user on TLSubCategoryMaster screen verifies validationMsg has visible value Found duplicate Sub Category name, please provide another Sub Category Name.

    Scenario: Modify Sub Category Details with same fee type in 2 rows
      ### On Login Screen ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value elzan
      And user on Login screen types on password value 12345678
      And user on Login screen clicks on signIn

      ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen will see the menu
      And user on Home screen clicks on menu
      And user on Home screen types on menuSearch value Modify Sub Category
      And user on Home screen clicks on firstMenuItem

    ### On Modify Sub Category Screen ###
      And user on TLSubCategoryMaster screen will wait until the page loads
      And user on TLSubCategoryMaster screen refresh's the webpage
      And user on TLSubCategoryMaster screen verifies text has visible value Search License Sub-Category
      And user on TLSubCategoryMaster screen selects on viewCategoryName value categoryName
      And user on TLSubCategoryMaster screen selects on viewSubCategoryName value subCategoryName
      And user on TLSubCategoryMaster screen clicks on text value Search
      And user on TLSubCategoryMaster screen clicks on searchResultSubCategoryName value subCategoryName
      And user on TLSubCategoryMaster screen types on updateSubCategoryName value "UpdatedSubCategoryName ",3 random characters
      And user on TLSubCategoryMaster screen copies the updateSubCategoryName to subCategoryName
      And user on TLSubCategoryMaster screen selects on feeType value LICENSE
      And user on TLSubCategoryMaster screen selects on rateType value FLAT BY RANGE
      And user on TLSubCategoryMaster screen selects on uomId value Area - Sq Ft
      And user on TLSubCategoryMaster screen clicks on add
      And user on TLSubCategoryMaster screen selects on feeType value LICENSE
      And user on TLSubCategoryMaster screen selects on rateType value FLAT BY UNIT
      And user on TLSubCategoryMaster screen selects on uomId value Area - Sq Ft
      And user on TLSubCategoryMaster screen clicks on text value Update
      And user on TLSubCategoryMaster screen verifies validationMsg has visible value Found duplicate SubCategory detail, please provide another SubCategory detail.


      ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen will see the menu
      And user on Home screen clicks on menu
      And user on Home screen types on menuSearch value View Sub Category
      And user on Home screen clicks on firstMenuItem

    ### On View Sub Category Screen ###
      And user on TLSubCategoryMaster screen will wait until the page loads
      And user on TLSubCategoryMaster screen refresh's the webpage
      And user on TLSubCategoryMaster screen verifies text has visible value Search License Sub-Category
      And user on TLSubCategoryMaster screen selects on viewCategoryName value categoryName
      And user on TLSubCategoryMaster screen selects on viewSubCategoryName value subCategoryName
      And user on TLSubCategoryMaster screen clicks on text value Search
      And user on TLSubCategoryMaster screen verifies text has visible value subCategoryName

    Scenario: Modify sub category with multiple fee types and same Rate type

      ### On Login Screen ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value elzan
      And user on Login screen types on password value 12345678
      And user on Login screen clicks on signIn

      ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen will see the menu
      And user on Home screen clicks on menu
      And user on Home screen types on menuSearch value Modify Sub Category
      And user on Home screen clicks on firstMenuItem

    ### On Modify Sub Category Screen ###
      And user on TLSubCategoryMaster screen will wait until the page loads
      And user on TLSubCategoryMaster screen refresh's the webpage
      And user on TLSubCategoryMaster screen verifies text has visible value Search License Sub-Category
      And user on TLSubCategoryMaster screen selects on viewCategoryName value categoryName
      And user on TLSubCategoryMaster screen selects on viewSubCategoryName value subCategoryName
      And user on TLSubCategoryMaster screen clicks on text value Search
      And user on TLSubCategoryMaster screen clicks on searchResultSubCategoryName value subCategoryName
      And user on TLSubCategoryMaster screen types on updateSubCategoryName value "UpdatedSubCategoryName ",3 random characters
      And user on TLSubCategoryMaster screen copies the updateSubCategoryName to subCategoryName
      And user on TLSubCategoryMaster screen selects on feeType value LICENSE
      And user on TLSubCategoryMaster screen selects on rateType value FLAT BY RANGE
      And user on TLSubCategoryMaster screen selects on uomId value Area - Sq Ft
      And user on TLSubCategoryMaster screen clicks on add
      And user on TLSubCategoryMaster screen selects on feeType value MOTOR
      And user on TLSubCategoryMaster screen selects on rateType value FLAT BY RANGE
      And user on TLSubCategoryMaster screen selects on uomId value Area - Sq Ft
      And user on TLSubCategoryMaster screen clicks on text value Update

      ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen will see the menu
      And user on Home screen clicks on menu
      And user on Home screen types on menuSearch value View Sub Category
      And user on Home screen clicks on firstMenuItem

    ### On View Sub Category Screen ###
      And user on TLSubCategoryMaster screen will wait until the page loads
      And user on TLSubCategoryMaster screen refresh's the webpage
      And user on TLSubCategoryMaster screen verifies text has visible value Search License Sub-Category
      And user on TLSubCategoryMaster screen selects on viewCategoryName value categoryName
      And user on TLSubCategoryMaster screen selects on viewSubCategoryName value subCategoryName
      And user on TLSubCategoryMaster screen clicks on text value Search
      And user on TLSubCategoryMaster screen verifies text has visible value subCategoryName

    Scenario: Modify sub category with new fee type

      ### On Login Screen ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value elzan
      And user on Login screen types on password value 12345678
      And user on Login screen clicks on signIn

      ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen will see the menu
      And user on Home screen clicks on menu
      And user on Home screen types on menuSearch value Modify Sub Category
      And user on Home screen clicks on firstMenuItem

    ### On Modify Sub Category Screen ###
      And user on TLSubCategoryMaster screen will wait until the page loads
      And user on TLSubCategoryMaster screen refresh's the webpage
      And user on TLSubCategoryMaster screen verifies text has visible value Search License Sub-Category
      And user on TLSubCategoryMaster screen selects on viewCategoryName value categoryName
      And user on TLSubCategoryMaster screen selects on viewSubCategoryName value subCategoryName
      And user on TLSubCategoryMaster screen clicks on text value Search
      And user on TLSubCategoryMaster screen clicks on searchResultSubCategoryName value subCategoryName
      And user on TLSubCategoryMaster screen types on updateSubCategoryName value "UpdatedSubCategoryName ",3 random characters
      And user on TLSubCategoryMaster screen copies the updateSubCategoryName to subCategoryName
      And user on TLSubCategoryMaster screen selects on feeType value MOTOR
      And user on TLSubCategoryMaster screen selects on rateType value FLAT BY RANGE
      And user on TLSubCategoryMaster screen selects on uomId value Area - Sq Ft
      And user on TLSubCategoryMaster screen clicks on text value Update

      ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen will see the menu
      And user on Home screen clicks on menu
      And user on Home screen types on menuSearch value View Sub Category
      And user on Home screen clicks on firstMenuItem

    ### On View Sub Category Screen ###
      And user on TLSubCategoryMaster screen will wait until the page loads
      And user on TLSubCategoryMaster screen refresh's the webpage
      And user on TLSubCategoryMaster screen verifies text has visible value Search License Sub-Category
      And user on TLSubCategoryMaster screen selects on viewCategoryName value categoryName
      And user on TLSubCategoryMaster screen selects on viewSubCategoryName value subCategoryName
      And user on TLSubCategoryMaster screen clicks on text value Search
      And user on TLSubCategoryMaster screen verifies text has visible value subCategoryName

    Scenario: Modify sub category with new rate type for same fee type

      ## On Login Screen ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value elzan
      And user on Login screen types on password value 12345678
      And user on Login screen clicks on signIn

      ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen will see the menu
      And user on Home screen clicks on menu
      And user on Home screen types on menuSearch value Modify Sub Category
      And user on Home screen clicks on firstMenuItem

    ### On Modify Sub Category Screen ###
      And user on TLSubCategoryMaster screen will wait until the page loads
      And user on TLSubCategoryMaster screen refresh's the webpage
      And user on TLSubCategoryMaster screen verifies text has visible value Search License Sub-Category
      And user on TLSubCategoryMaster screen selects on viewCategoryName value categoryName
      And user on TLSubCategoryMaster screen selects on viewSubCategoryName value subCategoryName
      And user on TLSubCategoryMaster screen clicks on text value Search
      And user on TLSubCategoryMaster screen clicks on searchResultSubCategoryName value subCategoryName
      And user on TLSubCategoryMaster screen types on updateSubCategoryName value "UpdatedSubCategoryName ",3 random characters
      And user on TLSubCategoryMaster screen copies the updateSubCategoryName to subCategoryName
      And user on TLSubCategoryMaster screen selects on rateType value FLAT BY UNIT
#      new rate type
      And user on TLSubCategoryMaster screen selects on uomId value Area - Sq Ft
      And user on TLSubCategoryMaster screen clicks on text value Update

      ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen will see the menu
      And user on Home screen clicks on menu
      And user on Home screen types on menuSearch value View Sub Category
      And user on Home screen clicks on firstMenuItem

    ### On View Sub Category Screen ###
      And user on TLSubCategoryMaster screen will wait until the page loads
      And user on TLSubCategoryMaster screen refresh's the webpage
      And user on TLSubCategoryMaster screen verifies text has visible value Search License Sub-Category
      And user on TLSubCategoryMaster screen selects on viewCategoryName value categoryName
      And user on TLSubCategoryMaster screen selects on viewSubCategoryName value subCategoryName
      And user on TLSubCategoryMaster screen clicks on text value Search
      And user on TLSubCategoryMaster screen verifies text has visible value subCategoryName

    Scenario: Modify sub category with new UOM for same fee type

      ## On Login Screen ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value elzan
      And user on Login screen types on password value 12345678
      And user on Login screen clicks on signIn

      ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen will see the menu
      And user on Home screen clicks on menu
      And user on Home screen types on menuSearch value Modify Sub Category
      And user on Home screen clicks on firstMenuItem

    ### On Modify Sub Category Screen ###
      And user on TLSubCategoryMaster screen will wait until the page loads
      And user on TLSubCategoryMaster screen refresh's the webpage
      And user on TLSubCategoryMaster screen verifies text has visible value Search License Sub-Category
      And user on TLSubCategoryMaster screen selects on viewCategoryName value categoryName
      And user on TLSubCategoryMaster screen selects on viewSubCategoryName value subCategoryName
      And user on TLSubCategoryMaster screen clicks on text value Search
      And user on TLSubCategoryMaster screen clicks on searchResultSubCategoryName value subCategoryName
      And user on TLSubCategoryMaster screen types on updateSubCategoryName value "UpdatedSubCategoryName ",3 random characters
      And user on TLSubCategoryMaster screen copies the updateSubCategoryName to subCategoryName
      And user on TLSubCategoryMaster screen selects on uomId value Joule
#      new UOM
      And user on TLSubCategoryMaster screen clicks on text value Update

      ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen will see the menu
      And user on Home screen clicks on menu
      And user on Home screen types on menuSearch value View Sub Category
      And user on Home screen clicks on firstMenuItem

    ### On View Sub Category Screen ###
      And user on TLSubCategoryMaster screen will wait until the page loads
      And user on TLSubCategoryMaster screen refresh's the webpage
      And user on TLSubCategoryMaster screen verifies text has visible value Search License Sub-Category
      And user on TLSubCategoryMaster screen selects on viewCategoryName value categoryName
      And user on TLSubCategoryMaster screen selects on viewSubCategoryName value subCategoryName
      And user on TLSubCategoryMaster screen clicks on text value Search
      And user on TLSubCategoryMaster screen verifies text has visible value subCategoryName

    Scenario: Modify sub category to make it inactive

      ## On Login Screen ###
      Given user on Login screen verifies signInText has visible value Sign In
      And user on Login screen types on username value elzan
      And user on Login screen types on password value 12345678
      And user on Login screen clicks on signIn

      ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen will see the menu
      And user on Home screen clicks on menu
      And user on Home screen types on menuSearch value Modify Sub Category
      And user on Home screen clicks on firstMenuItem

    ### On Modify Sub Category Screen ###
      And user on TLSubCategoryMaster screen will wait until the page loads
      And user on TLSubCategoryMaster screen refresh's the webpage
      And user on TLSubCategoryMaster screen verifies text has visible value Search License Sub-Category
      And user on TLSubCategoryMaster screen selects on viewCategoryName value categoryName
      And user on TLSubCategoryMaster screen selects on viewSubCategoryName value subCategoryName
      And user on TLSubCategoryMaster screen clicks on text value Search
      And user on TLSubCategoryMaster screen clicks on searchResultSubCategoryName value subCategoryName
      And user on TLSubCategoryMaster screen clicks on active
#      to deactivate
      And user on TLSubCategoryMaster screen clicks on text value Update

      ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
      And user on Home screen will see the menu
      And user on Home screen clicks on menu
      And user on Home screen types on menuSearch value View Sub Category
      And user on Home screen clicks on firstMenuItem

    ### On View Sub Category Screen ###
      And user on TLSubCategoryMaster screen will wait until the page loads
      And user on TLSubCategoryMaster screen refresh's the webpage
      And user on TLSubCategoryMaster screen verifies text has visible value Search License Sub-Category
      And user on TLSubCategoryMaster screen selects on viewCategoryName value categoryName
      And user on TLSubCategoryMaster screen selects on viewSubCategoryName value subCategoryName
      And user on TLSubCategoryMaster screen clicks on text value Search
      And user on TLSubCategoryMaster screen verifies text has visible value subCategoryName
      And user on TLSubCategoryMaster screen verifies active has visible value NO





