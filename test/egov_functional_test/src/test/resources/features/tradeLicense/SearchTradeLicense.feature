Feature: Search Trade License

  As a register user of the system
  I want to be able to search for a trade license
  So that the license records are up to date.

  @Sanity @TradeLicense
  Scenario: Registered user search trade license with application number

    Given creator logs in
    And user will select the required screen as "Search Trade License"
    And he search trade license with application number
    And he checks total number of records
    And current user logs out

  @Sanity @TradeLicense
  Scenario: Registered user search trade license with license number

    Given creator logs in
    And user will select the required screen as "Search Trade License"
    And he search trade license with license number
    And he checks total number of records
    And current user logs out

  @Sanity @TradeLicense
  Scenario: Registered user search trade license with status

    Given creator logs in
    And user will select the required screen as "Search Trade License"
    And he search trade license with status "Cancelled"
    And he checks total number of records
    And current user logs out

  @Sanity @TradeLicense
  Scenario: Registered user search trade license with status

    Given creator logs in
    And user will select the required screen as "Search Trade License"
    And he search trade license with status "Rejected"
    And he checks total number of records
    And current user logs out