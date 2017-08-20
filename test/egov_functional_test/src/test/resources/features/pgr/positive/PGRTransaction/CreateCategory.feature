Feature: Creating a Grievance Category

  Scenario: Create a Grievance Category
    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value ramana
    And user on Login screen types on password value demo
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create Category
    And user on Home screen clicks on firstMenuItem

    ### On Grievance Screen ###
#    And user on Grievance screen verifies text has visible value Create Grievance Category
    And user on Grievance screen types on categoryName value --"Category ", 5 random characters
    And user on Grievance screen copies the complaintNum to applicationNumber
    And user on Grievance screen types on categoryCode value --5 random characters
    And user on Grievance screen clicks on createCategoryButton
