Feature: In this feature we are going to Create Unit Of Measurement for Trade License

  Scenario: Create Search and Update UOM Master

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value elzan
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen verifies myTasks has visible value My Tasks
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create Unit Of Measurement
    And user on Home screen clicks on firstMenuItem

    ### Create UOM Screen ###
    And user on TLUOMMaster screen type on UOMName value "Category ",3 random characters
    And user on TLUOMMaster screen type on UOMCode value "CategoryCode",3 random numbers
    And user on TLUOMMaster screen copies the UOMName to UOMNameValue
    And user on TLUOMMaster screen clicks on text value Create

   ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value View Unit Of Measurement
    And user on Home screen clicks on firstMenuItem

    ### View License UOM ###
    And user on TLUOMMaster screen will wait until the page loads
    And user on TLUOMMaster screen refresh's the webpage
    And user on TLUOMMaster screen verifies text has visible value Search Unit of Measurement
    And user on TLUOMMaster screen selects on viewUOMName value UOMNameValue
    And user on TLUOMMaster screen clicks on text value Search
    And user on TLUOMMaster screen verifies text has visible value UOMNameValue

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Modify Unit of Measurement
    And user on Home screen clicks on firstMenuItem

    ### Modify License Category ###
    And user on TLUOMMaster screen will wait until the page loads
    And user on TLUOMMaster screen refresh's the webpage
    And user on TLUOMMaster screen verifies text has visible value Search Unit of Measurement
    And user on TLUOMMaster screen selects on viewUOMName value UOMNameValue
    And user on TLUOMMaster screen clicks on text value Search
    And user on TLUOMMaster screen clicks on searchResultUOMName value UOMNameValue

    And user on TLUOMMaster screen types on updateUOMName value "UpdatedCategoryName ",3 random characters
    And user on TLUOMMaster screen copies the updateUOMName to UOMNameValue
    And user on TLUOMMaster screen clicks on text value Update

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value View Unit Of Measurement
    And user on Home screen clicks on firstMenuItem

    ### View License UOM ###
    And user on TLUOMMaster screen will wait until the page loads
    And user on TLUOMMaster screen refresh's the webpage
    And user on TLUOMMaster screen verifies text has visible value Search Unit of Measurement
    And user on TLUOMMaster screen selects on viewUOMName value UOMNameValue
    And user on TLUOMMaster screen clicks on text value Search
    And user on TLUOMMaster screen verifies text has visible value UOMNameValue

    ### Logout ###
    And Intent:LogoutIntentTest


  Scenario: Create UOM with already existing name

  Scenario: Create UOM with already existing Code

  Scenario: Create UOM with already existing Name and Code

  Scenario: Create inactive UOM

  Scenario: Modify UOM with valid data

  Scenario: Modify UOM with already existing name

  Scenario: Modify UOM to make it inactive