Feature: Login Intent Feature

#  @Intent
#  Scenario: LoginIntentWithOutData
#    Given user on Login screen verifies cityText has visible value Roha Municipal Corporation
#    And user on Login screen types on username value aboveUser
#    And user on Login screen types on password value abovePassword
#    And user on Login screen clicks on signIn

  @Intent
  Scenario: LoginIntent
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value <username>
    And user on Login screen types on password value <password>
    And user on Login screen clicks on signIn