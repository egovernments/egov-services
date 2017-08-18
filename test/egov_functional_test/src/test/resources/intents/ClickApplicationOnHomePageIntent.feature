Feature: Here all intents belongs to open a application on homepage screen

  @Intent
  Scenario: OpenApplicationOnHomePageScreen
    ### On HomePage Screen ###
    And user on Home screen types on dashBoardSearch with above applicationNumber
    And user on Home screen verifies applicationBox has visible value aboveApplicationNumber
    And user on Home screen clicks on applicationBox value aboveApplicationNumber