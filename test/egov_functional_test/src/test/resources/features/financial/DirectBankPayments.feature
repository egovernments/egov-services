Feature: To create a voucher through direct bank payments

  @Sanity @Finance
  Scenario Outline: To create a voucher through direct bank payments with cheque and rtgs modes and also applying the check assignment

    Given accountsOfficer logs in
    And user will select the required screen as "Direct Bank Payments"
    And officer will enter the direct bank payment details as <bankDetails> with mode as <paymentMode>
    And officer will enter the approval details as <approvalOfficer1>
    And officer will see the successful voucher creation page and closes it
    Then user will be notified by "Successful"
    And current user logs out

#    And examiner logs in
    And the next user will be logged in
    And he chooses to act upon above application number
    And officer will enter the approval details as <approvalOfficer2>
    And officer will see the successful voucher creation page and closes it
    Then user will be notified by "forwarded"
    And current user logs out

#    And commissioner logs in
    And the next user will be logged in
    And he chooses to act upon above application number
    And officer click on approval of the voucher
    And officer will see the successful voucher creation page and closes it
    Then user will be notified by "approved"
    And current user logs out

    And accountsOfficer logs in
    And officer search for the assignment mode as <assignment>
    And officer will filter the payment cheque assignment bill as <singleOrMultiple>
    And officer will select the <singleOrMultiple> bill and enter the details <assignment>
    And officer will close the successfull assignment page
    Then user will be notified by "successfully"
    And current user logs out

    Examples:
    | bankDetails       |paymentMode | approvalOfficer1  | approvalOfficer2  | assignment | singleOrMultiple |
    | directBankDetails |cheque      | accountOfficer2   | commissioner      | cheque     | single           |
    | directBankDetails |RTGS        | accountOfficer2   | commissioner      | RTGS       | single           |


    @Sanity @Finance
    Scenario Outline: To create a voucher through direct bank payments with cash mode

    Given accountsOfficer logs in
    And user will select the required screen as "Direct Bank Payments"
    And officer will enter the direct bank payment details as <bankDetails> with mode as <paymentMode>
    And officer will enter the approval details as <approvalOfficer1>
    And officer will see the successful voucher creation page and closes it
    Then user will be notified by "Successful"
    And current user logs out

#    And examiner logs in
    And the next user will be logged in
    And he chooses to act upon above application number
    And officer will enter the approval details as <approvalOfficer2>
    And officer will see the successful voucher creation page and closes it
    Then user will be notified by "forwarded"
    And current user logs out

#    And commissioner logs in
    And the next user will be logged in
    And he chooses to act upon above application number
    And officer click on approval of the voucher
    And officer will see the successful voucher creation page and closes it
    Then user will be notified by "approved"
    And current user logs out

    Examples:
    | bankDetails       |paymentMode | approvalOfficer1  | approvalOfficer2  |
    | directBankDetails |cash        | accountOfficer2   | commissioner      |