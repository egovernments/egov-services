Feature: Creating a Grievance Category

  Scenario: Create and Search a Grievance Category

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value narasappa
    And user on Login screen types on password value demo
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create Category
    And user on Home screen clicks on firstMenuItem

    ### On Grievance Screen ###
    And user on Grievance screen verifies text has visible value Grievance Category
    And user on Grievance screen types on categoryName value --"Category ", 5 random characters
    And user on Grievance screen copies the categoryName to categoryName
    And user on Grievance screen types on categoryCode value --5 random numbers
    And user on Grievance screen clicks on createButton
    And user on Grievance screen clicks on close

    ### On Homepage Screen ###
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Search Category
    And user on Home screen clicks on firstMenuItem

    ### On Search Category Screen ###
    And user on Grievance screen types on grievanceSearch value categoryName
    And user on Grievance screen verifies text has visible value categoryName

    ### Logout ###
    And Intent:LogoutIntentTest
