Feature: In this feature we are going to Create Category for Trade License

  Scenario: Create, Search and Update Category with valid data

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
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value View License Category
    And user on Home screen clicks on firstMenuItem

    ### On View License Category ###
    And user on TLCategoryMaster screen will wait until the page loads
    And user on TLCategoryMaster screen refresh's the webpage
    And user on TLCategoryMaster screen verifies text has visible value Search License Category
    And user on TLCategoryMaster screen selects on viewCategoryName value categoryNameValue
    And user on TLCategoryMaster screen clicks on text value Search
    And user on TLCategoryMaster screen verifies text has visible value categoryNameValue

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Modify License Category
    And user on Home screen clicks on firstMenuItem

    ### On Modify License Category ###
    And user on TLCategoryMaster screen will wait until the page loads
    And user on TLCategoryMaster screen refresh's the webpage
    And user on TLCategoryMaster screen verifies text has visible value Search License Category
    And user on TLCategoryMaster screen selects on viewCategoryName value categoryNameValue
    And user on TLCategoryMaster screen clicks on text value Search
    And user on TLCategoryMaster screen clicks on searchResultCategoryName value categoryNameValue

    And user on TLCategoryMaster screen types on updateCategoryName value "UpdatedCategoryName ",3 random characters
    And user on TLCategoryMaster screen copies the updateCategoryName to categoryNameValue
    And user on TLCategoryMaster screen clicks on text value Update

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value View License Category
    And user on Home screen clicks on firstMenuItem

    ### View License Category ###
    And user on TLCategoryMaster screen will wait until the page loads
    And user on TLCategoryMaster screen refresh's the webpage
    And user on TLCategoryMaster screen verifies text has visible value Search License Category
    And user on TLCategoryMaster screen selects on viewCategoryName value categoryNameValue
    And user on TLCategoryMaster screen clicks on text value Search
    And user on TLCategoryMaster screen verifies text has visible value categoryNameValue

    ### Logout ###
    And Intent:LogoutIntentTest

  Scenario: Create Category with already existing name

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
    And user on TLCategoryMaster screen type on categoryName value Flammables
#    Enter duplicate Name
    And user on TLCategoryMaster screen type on categoryCode value "CategoryCode",3 random numbers
    And user on TLCategoryMaster screen copies the categoryName to categoryNameValue
    And user on TLCategoryMaster screen clicks on text value Create
    And user on TLCategoryMaster screen verifies validationMSG has visible value Found duplicate Category name, please provide another Category Name.


  Scenario: Create category with already existing code
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
    And user on TLCategoryMaster screen type on categoryCode value Flammables
    #    Enter duplicate code
    And user on TLCategoryMaster screen copies the categoryName to categoryNameValue
    And user on TLCategoryMaster screen clicks on text value Create
    And user on TLCategoryMaster screen verifies validationMSG has visible value Found duplicate Category code, please provide another Category Code.

  Scenario: Create category with already existing Name and code

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
    And user on TLCategoryMaster screen type on categoryName value Flammables
    #    Enter duplicate name
    And user on TLCategoryMaster screen type on categoryCode value Flammables
    #    Enter duplicate code
    And user on TLCategoryMaster screen copies the categoryName to categoryNameValue
    And user on TLCategoryMaster screen clicks on text value Create
    And user on TLCategoryMaster screen verifies validationMSG has visible value Found duplicate Category code, please provide another Category Code.

  Scenario: Create category with inactive category

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
    And user on TLCategoryMaster screen clicks on activeCheckBox
#    to deactivate the category
    And user on TLCategoryMaster screen clicks on text value Create


     ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value View License Category
    And user on Home screen clicks on firstMenuItem

    ### On View License Category ###
    And user on TLCategoryMaster screen will wait until the page loads
    And user on TLCategoryMaster screen refresh's the webpage
    And user on TLCategoryMaster screen verifies text has visible value Search License Category
    And user on TLCategoryMaster screen selects on viewCategoryName value categoryNameValue
    And user on TLCategoryMaster screen clicks on text value Search
    And user on TLCategoryMaster screen verifies text has visible value categoryNameValue
    And user on TLCategoryMaster screen verifies active has visible value NO

  Scenario: Modify Category with valid data

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value elzan
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Modify License Category
    And user on Home screen clicks on firstMenuItem

    ### On Modify License Category ###
    And user on TLCategoryMaster screen will wait until the page loads
    And user on TLCategoryMaster screen refresh's the webpage
    And user on TLCategoryMaster screen verifies text has visible value Search License Category
    And user on TLCategoryMaster screen selects on viewCategoryName value categoryNameValue
    And user on TLCategoryMaster screen clicks on text value Search
    And user on TLCategoryMaster screen clicks on searchResultCategoryName value categoryNameValue

    And user on TLCategoryMaster screen types on updateCategoryName value "UpdatedCategoryName ",3 random characters
    And user on TLCategoryMaster screen copies the updateCategoryName to categoryNameValue
    And user on TLCategoryMaster screen clicks on text value Update

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value View License Category
    And user on Home screen clicks on firstMenuItem

    ### View License Category ###
    And user on TLCategoryMaster screen will wait until the page loads
    And user on TLCategoryMaster screen refresh's the webpage
    And user on TLCategoryMaster screen verifies text has visible value Search License Category
    And user on TLCategoryMaster screen selects on viewCategoryName value categoryNameValue
    And user on TLCategoryMaster screen clicks on text value Search
    And user on TLCategoryMaster screen verifies text has visible value categoryNameValue


  Scenario: Modify Category with already existing name


    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value elzan
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Modify License Category
    And user on Home screen clicks on firstMenuItem

    ### On Modify License Category ###
    And user on TLCategoryMaster screen will wait until the page loads
    And user on TLCategoryMaster screen refresh's the webpage
    And user on TLCategoryMaster screen verifies text has visible value Search License Category
    And user on TLCategoryMaster screen selects on viewCategoryName value categoryNameValue
    And user on TLCategoryMaster screen clicks on text value Search
    And user on TLCategoryMaster screen clicks on searchResultCategoryName value categoryNameValue

    And user on TLCategoryMaster screen types on updateCategoryName value Flammables
#    enter duplicate name
    And user on TLCategoryMaster screen clicks on text value Update
    And user on TLCategoryMaster screen verifies validationMSG has visible value Found duplicate Category name, please provide another Category Name.


  Scenario: Modify Category to inactive with Sub Category Active

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value elzan
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Modify License Category
    And user on Home screen clicks on firstMenuItem

    ### On Modify License Category ###
    And user on TLCategoryMaster screen will wait until the page loads
    And user on TLCategoryMaster screen refresh's the webpage
    And user on TLCategoryMaster screen verifies text has visible value Search License Category
    And user on TLCategoryMaster screen selects on viewCategoryName value categoryNameValue
    And user on TLCategoryMaster screen clicks on text value Search
    And user on TLCategoryMaster screen clicks on searchResultCategoryName value categoryNameValue

    And user on TLCategoryMaster screen clicks on activeCheckbox
#    to deactivate the category
    And user on TLCategoryMaster screen clicks on text value Update
    And user on TLCategoryMaster screen verifies validationMSG has visible value cannot mark this category as inactive, As there are active subcategories under this category :

  Scenario: Field validations for create category screen as per story EGSVC-660 (At UI level) - pending

  Scenario: Field validations for Modify Category screen as per story EGSVC-664 (At UI level) - pending





