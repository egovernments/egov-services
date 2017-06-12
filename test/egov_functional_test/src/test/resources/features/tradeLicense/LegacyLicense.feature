Feature: Legacy license


   # CREATE LEGENCY TRADE LICENSE #
  @Sanity @TradeLicense
  Scenario Outline: Register user create legacy trade license
    Given creator logs in
    And user will select the required screen as "Create Legacy License"
    And he enters old license number
    And he enters trade owner details of new license <tradeDetailsData>
    And he enters trade location details of new license <tradeLocationData>
    And he enters trade details of new license <tradeDetailsData1>
    And he enters fee details of legency trade license
    And he saves the application
    And he copies the license number and closes the acknowledgement
    And current user logs out

    Examples:
      | tradeDetailsData         | tradeLocationData           | tradeDetailsData1        | legencyDetailsData |
      | ownerDetailsTradeLicense | locationDetailsTradeLicense | tradeDetailsTradeLicense | legencyTrade       |
