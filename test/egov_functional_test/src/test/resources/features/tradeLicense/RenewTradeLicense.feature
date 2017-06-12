Feature: Renewal of trade license


#  @TradeLicense
  Scenario Outline: Renewal of license with demand generation

    Given creator logs in
    And user will select the required screen as "Create New License"
    And he enters trade owner details of new license <tradeDetailsData>
    And he enters trade location details of new license <tradeLocationData>
    And he enters trade details of new license <tradeDetailsData1>
    And he saves the application
    And he copy trade application number

    And user will select the required screen as "Search Trade License"
    And he search existing application number
    And he choose action "Collect Fees"
    And he choose to payTax of applicationNumber
    And he chooses to act upon above application number

    And he forwards for approver sanitaryInspector
    And he confirms to proceed
    And he closes acknowledgement page
    And current user logs out

    When sanitaryInspector logs in
    And he chooses to act upon above application number
    And he forwards for approver commissioner
    And he confirms to proceed
    And he closes acknowledgement page
    And current user logs out

    When commissioner logs in
    And he chooses to act upon above application number
    And he approves application
    And he confirms to proceed
    And he closes acknowledgement page
    And current user logs out

    When creator logs in
    And he chooses to act upon above application number
    And he generates the license certificate
    And user will be notified by "License"

    And user will select the required screen as "Search Trade License"
    And he search existing application number
    And he choose action "Generate Demand"
    And he generates demand
    And user will be notified by "successfully"
    And he choose action "Renew License"
    And he copy trade license number
    And he choose to renew trade license
    And he choose to search with license number
    And he choose action "Collect Fees"
    And he choose to payTax of applicationNumber
    And he chooses to act upon above application number
    And he forwards for approver sanitaryInspector
    And he confirms to proceed
    And he closes acknowledgement page
    And current user logs out

    When sanitaryInspector logs in
    And he chooses to act upon above application number
    And he forwards for approver commissioner
    And he confirms to proceed
    And he closes acknowledgement page
    And current user logs out

    When commissioner logs in
    And he chooses to act upon above application number
    And he approves application
    And he confirms to proceed
    And he closes acknowledgement page
    And current user logs out

    When creator logs in
    And he chooses to act upon above application number
    And he generates the license certificate
    And current user logs out

    Examples:
      | tradeDetailsData         | tradeLocationData           | tradeDetailsData1        |
      | ownerDetailsTradeLicense | locationDetailsTradeLicense | tradeDetailsTradeLicense |


    # Trade License Renewal #
  @Sanity @TradeLicense
  Scenario Outline: Renewal of Trade License with legacy license

    Given creator logs in
    And user will select the required screen as "Create Legacy License"
    And he enters old license number
    And he enters trade owner details of new license <tradeDetailsData>
    And he enters trade location details of new license <tradeLocationData>
    And he enters trade details of new license <tradeDetailsData1>
    And he enters fee details of legency trade license
    And he saves the application
    And he copies the license number and closes the acknowledgement

    And user will select the required screen as "Search Trade License"
    And he choose to search with license number
    And he choose action "Renew License"
    And he choose to renew trade license
    And he choose action "Collect Fees"
    And he choose to payTax of applicationNumber
    And he chooses to act upon above application number
    And he forwards for approver sanitaryInspector
    And he confirms to proceed
    And he closes acknowledgement page
    And current user logs out

    When sanitaryInspector logs in
    And he chooses to act upon above application number
    And he forwards for approver commissioner
    And he confirms to proceed
    And he closes acknowledgement page
    And current user logs out

    When commissioner logs in
    And he chooses to act upon above application number
    And he approves application
    And he confirms to proceed
    And he closes acknowledgement page
    And current user logs out

    When creator logs in
    And he chooses to act upon above application number
    And he generates the license certificate
    And current user logs out

    Examples:
      | tradeDetailsData         | tradeLocationData           | tradeDetailsData1        |
      | ownerDetailsTradeLicense | locationDetailsTradeLicense | tradeDetailsTradeLicense |

#  @TradeLicense
  Scenario Outline: Renewal of TL -> collect fee -> forward to SI -> forward to Commissioner -> reject

    Given creator logs in
    And user will select the required screen as "Create New License"
    And he enters trade owner details of new license <tradeDetailsData>
    And he enters trade location details of new license <tradeLocationData>
    And he enters trade details of new license <tradeDetailsData1>
    And he saves the application
    And he copy trade application number

    And user will select the required screen as "Search Trade License"
    And he search existing application number
    And he choose action "Collect Fees"
    And he choose to payTax of applicationNumber
    And he chooses to act upon above application number

    And he forwards for approver sanitaryInspector
    And he confirms to proceed
    And he closes acknowledgement page
    And current user logs out

    When sanitaryInspector logs in
    And he chooses to act upon above application number
    And he forwards for approver commissioner
    And he confirms to proceed
    And he closes acknowledgement page
    And current user logs out

    When commissioner logs in
    And he chooses to act upon above application number
    And he approves application
    And he confirms to proceed
    And he closes acknowledgement page
    And current user logs out

    When creator logs in
    And he chooses to act upon above application number
    And he generates the license certificate
    And user will be notified by "License"

    And user will select the required screen as "Search Trade License"
    And he search existing application number
    And he choose action "Generate Demand"
    And he generates demand
    And user will be notified by "successfully"
    And he choose action "Renew License"
    And he copy trade license number
    And he choose to renew trade license
    And he choose to search with license number
    And he choose action "Collect Fees"
    And he choose to payTax of applicationNumber
    And he chooses to act upon above application number
    And he forwards for approver sanitaryInspector
    And he confirms to proceed
    And he closes acknowledgement page
    And current user logs out

    When sanitaryInspector logs in
    And he chooses to act upon above application number
    And he forwards for approver commissioner
    And he confirms to proceed
    And he closes acknowledgement page
    And current user logs out

    When commissioner logs in
    And he chooses to act upon above application number
    And he rejects the application
    And he confirms to proceed
    And he closes acknowledgement page
    And current user logs out

    When creator logs in
    And he chooses to act upon above application number
    And he rejects the application
    And he confirms to proceed
    And he closes acknowledgement page
    And user will select the required screen as "Search Trade License"
    And he choose to search with license number
    And he verifies the application status
    And user will be notified by "Cancelled"
    And he verifies the License active
    And user will be notified by "YES"
    And he closes search screen
    And current user logs out

    Examples:
      | tradeDetailsData         | tradeLocationData           | tradeDetailsData1        |
      | ownerDetailsTradeLicense | locationDetailsTradeLicense | tradeDetailsTradeLicense |

#  @TradeLicense
  Scenario Outline: Renewal of TL -> collect fee -> forward to SI
  -> change trade area and forward to Commissioner -> reject

    Given creator logs in
    And user will select the required screen as "Create New License"
    And he enters trade owner details of new license <tradeDetailsData>
    And he enters trade location details of new license <tradeLocationData>
    And he enters trade details of new license <tradeDetailsData1>
    And he saves the application
    And he copy trade application number

    And user will select the required screen as "Search Trade License"
    And he search existing application number
    And he choose action "Collect Fees"
    And he choose to payTax of applicationNumber
    And he chooses to act upon above application number

    And he forwards for approver sanitaryInspector
    And he confirms to proceed
    And he closes acknowledgement page
    And current user logs out

    When sanitaryInspector logs in
    And he chooses to act upon above application number
    And he changes trade area as "2000"
    And he forwards for approver commissioner
    And he confirms to proceed
    And he closes acknowledgement page
    And current user logs out

    When commissioner logs in
    And he chooses to act upon above application number
    And he rejects the application
    And he confirms to proceed
    And he closes acknowledgement page
    And current user logs out

    When creator logs in
    And he chooses to act upon above application number
    And he rejects the application
    And he confirms to proceed
    And he closes acknowledgement page
    And user will select the required screen as "Search Trade License"
    And he search existing application number
    And he verifies the application status
    And user will be notified by "Cancelled"
    And he verifies the License active
    And user will be notified by "NO"
    And he closes search screen
    And current user logs out


    Examples:
      | tradeDetailsData         | tradeLocationData           | tradeDetailsData1        |
      | ownerDetailsTradeLicense | locationDetailsTradeLicense | tradeDetailsTradeLicense |
