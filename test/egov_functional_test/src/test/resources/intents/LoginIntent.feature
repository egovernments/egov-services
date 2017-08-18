Feature: Login Intent Feature

  @Intent
  Scenario: LoginIntentTest
    Given user on Login screen verifies cityText has visible value Roha Municipal Corporation
    And user on Login screen types on username value aboveUser
    And user on Login screen types on password value abovePassword
    And user on Login screen clicks on signIn