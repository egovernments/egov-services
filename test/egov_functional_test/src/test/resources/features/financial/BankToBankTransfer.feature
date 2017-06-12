Feature: To transfer payments from one bank to other bank

  @Sanity @Finance
  Scenario Outline: To make payment transfers from one bank to other bank

    Given accountsOfficer logs in
    And user will select the required screen as "Bank to Bank Transfer"
    And officer will enter the bank to bank transfer details as <bankDetails>
    And officer will close the successful creation page
    Then user will be notified by "Successful"
    And current user logs out

    Examples:
      | bankDetails  |
      | bankDetails1 |

