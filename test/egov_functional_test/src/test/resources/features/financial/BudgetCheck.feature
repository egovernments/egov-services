Feature: To create a new Journal voucher according to the budget check

  @Sanity @Finance
  Scenario Outline: To create the financial journal voucher with type expense and budget check

    Given accountsOfficer logs in
    And user will select the required screen as "Create Journal Voucher"
    And officer will enter the journal voucher details as <voucherDetails> with subledger <isPresent>
    And officer will enter the approval details as <approvalOfficer1>
    And officer will get successful BAN NUMBER created and closes it
    Then user will be notified by "Sucessfully"
    And current user logs out

#    And accountsOfficer logs in
    And the next user will be logged in
    And he chooses to act upon above application number
    And officer will enter the approval details as <approvalOfficer2>
    And officer will closes the acknowledgement page
    Then user will be notified by "forwarded"
    And current user logs out

#    And accountOfficer logs in
    And the next user will be logged in
    And he chooses to act upon above application number
    And officer will enter the approval details as <approvalOfficer3>
    And officer will closes the acknowledgement page
    Then user will be notified by "forwarded"
    And current user logs out

#    And commissioner logs in
    And the next user will be logged in
    And he chooses to act upon above application number
    And officer click on approval of the voucher
    And officer will closes the acknowledgement page
    Then user will be notified by "approved"
    And current user logs out

    Examples:
      | voucherDetails           | approvalOfficer1 | approvalOfficer2 | approvalOfficer3 | isPresent |
      | budgetCheckWithSubledger | accountOfficer1  | accountOfficer2  | commissioner     | yes       |


