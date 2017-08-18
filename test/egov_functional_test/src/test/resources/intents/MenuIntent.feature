Feature: All intents perform action on menu bar

  @Intent
  Scenario: SearchATransactionAndClickOnFirstMenuItem
    And user on Home screen will see the myTasks
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Officials Register Grievance
    And user on Home screen clicks on firstMenuItem

  @Intent
  Scenario: SearchATransactionAndClickOnFirstMenuItem1
    And user on Home screen will see the myTasks
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Search Grievance
    And user on Home screen clicks on firstMenuItem
