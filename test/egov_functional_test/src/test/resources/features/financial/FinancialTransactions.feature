Feature: To create a Financial Transactions

  @Sanity @Finance
  Scenario Outline: To create the financial journal voucher with type General

    Given accountsOfficer logs in
    And user will select the required screen as "Create Journal Voucher"
    And officer will enter the journal voucher details as <voucherDetails> with subledger <isPresent>
    And officer will enter the approval details as <approvalOfficer1>
    And officer will get successful voucher created and closes it
    Then user will be notified by "Created"
    And current user logs out

    And the next user will be logged in
    And he chooses to act upon above application number
    And officer will enter the approval details as <approvalOfficer2>
    And officer will closes the acknowledgement page
    Then user will be notified by "forwarded"
    And current user logs out

    And the next user will be logged in
    And he chooses to act upon above application number
    And officer will enter the approval details as <approvalOfficer3>
    And officer will closes the acknowledgement page
    Then user will be notified by "forwarded"
    And current user logs out

    And the next user will be logged in
    And he chooses to act upon above application number
    And officer click on approval of the voucher
    And officer will closes the acknowledgement page
    Then user will be notified by "approved"
    And current user logs out

    Examples:
      | voucherDetails          | approvalOfficer1 | approvalOfficer2 | approvalOfficer3 | isPresent |
      | voucher1                | accountOfficer1a | accountOfficer2  | commissioner     | yes       |
      | voucherWithOutSubledger | accountOfficer1a | accountOfficer2  | commissioner     | no        |


  @Sanity @Finance
  Scenario Outline: To create the financial journal voucher with type expense

    Given accountsOfficer logs in
    And user will select the required screen as "Create Journal Voucher"
    And officer will enter the journal voucher details as <voucherDetails> with subledger <isPresent>
    And officer will enter the approval details as <approvalOfficer1>
    And officer will get successful voucher created and closes it
    Then user will be notified by "Created"
    And current user logs out

#    And assistantExaminer logs in
    And the next user will be logged in
    And he chooses to act upon above application number
    And officer will enter the approval details as <approvalOfficer2>
    And officer will closes the acknowledgement page
    Then user will be notified by "forwarded"
    And current user logs out

#    And examiner logs in
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
      | voucherDetails          | approvalOfficer1 | approvalOfficer2 | approvalOfficer3 | isPresent |
      | voucher2                | accountOfficer1a | accountOfficer2  | commissioner     | yes       |
      | voucherWithOutSubledger | accountOfficer1a | accountOfficer2  | commissioner     | no        |

  @Sanity @Finance
  Scenario Outline: To create a journal voucher as well as payment with different modes

    Given accountsOfficer logs in
    And user will select the required screen as "Create Journal Voucher"
    And officer will enter the journal voucher details as <voucherDetails> with subledger <isPresent>
    And officer will enter the approval details as <approvalOfficer1>
    And officer will get successful voucher created and closes it
    Then user will be notified by "Created"
    And current user logs out

    And the next user will be logged in
    And he chooses to act upon above application number
    And officer will enter the approval details as <approvalOfficer2>
    And officer will closes the acknowledgement page
    Then user will be notified by "forwarded"
    And current user logs out

    And the next user will be logged in
    And he chooses to act upon above application number
    And officer will enter the approval details as <approvalOfficer3>
    And officer will closes the acknowledgement page
    Then user will be notified by "forwarded"
    And current user logs out

    And the next user will be logged in
    And he chooses to act upon above application number
    And officer click on approval of the voucher
    And officer will closes the acknowledgement page
    Then user will be notified by "approved"
    And current user logs out

    And accountsOfficer logs in
    And user will select the required screen as "Bill Payment"
    And officer will modify the results depending upon the fund and date
    And officer will act upon the above voucher with payment mode as <paymentMode>

    And officer will enter the bank details
    And officer will enter the approval details as <approvalOfficer2>
    And officer will closes the successfull payment page
    And current user logs out

    And the next user will be logged in
    And he chooses to act upon above application number
    And officer will enter the approval details as <approvalOfficer3>
    And officer will closes the acknowledgement page
    Then user will be notified by "forwarded"
    And current user logs out

    And the next user will be logged in
    And he chooses to act upon above application number
    And officer click on approval of the voucher
    And officer will closes the acknowledgement page
    Then user will be notified by "approved"
    And current user logs out

    Examples:
      | voucherDetails     | approvalOfficer1 | approvalOfficer2 | approvalOfficer3 | paymentMode | isPresent |
      | voucherBillPayment | accountOfficer1  | accountOfficer2  | commissioner     | cheque      | yes       |
      | voucherBillPayment | accountOfficer1  | accountOfficer2  | commissioner     | cash        | yes       |
      | voucherBillPayment | accountOfficer1  | accountOfficer2  | commissioner     | RTGS        | yes       |


  @Sanity @Finance
  Scenario Outline: To create a new expense bill

    Given accountsOfficer logs in
    And user will select the required screen as "New Create Expense Bill"
    And officer will the expense bill details as <billDetails>
    And officer will enter the expense approval details as <approvalOfficer1>
    And officer will closes the expense acknowledgement page
    Then user will be notified by "created"
    And current user logs out

#    Then examiner logs in
    And the next user will be logged in
    And he chooses to act upon above application number
    And officer will enter the expense approval details as <approvalOfficer2>
    And officer will closes the expense acknowledgement page
    Then user will be notified by "created"
    And current user logs out

#    And commissioner logs in
    And the next user will be logged in
    And he chooses to act upon above application number
    And officer click on approval of the voucher
    And officer will closes the expense acknowledgement page
    Then user will be notified by "approved"
    And current user logs out

    And accountsOfficer logs in
    And user will select the required screen as "Create Voucher"
    And officer will filter the bill according to the type
    And officer will enter the approval details as <approvalOfficer3>
    And officer will set the new expense voucher number and closes it
    Then user will be notified by "forwarded"
    And current user logs out

#    And assistantExaminer logs in
    And the next user will be logged in
    And he chooses to act upon above application number
    And officer will enter the approval details as <approvalOfficer4>
    And officer will closes the acknowledgement page
    Then user will be notified by "forwarded"
    And current user logs out

#    And examiner logs in
    And the next user will be logged in
    And he chooses to act upon above application number
    And officer will enter the approval details as <approvalOfficer5>
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
      | billDetails | approvalOfficer1 | approvalOfficer2 | approvalOfficer3 | approvalOfficer4 | approvalOfficer5 |
      | expenseBill | accountOfficer3  | commissioner1    | accountOfficer1  | accountOfficer2  | commissioner     |


  @Sanity @Finance
  Scenario Outline: To create a journal voucher as well as payment with different modes along with check assignment

    Given accountsOfficer logs in
    And user will select the required screen as "Create Journal Voucher"
    And officer will enter the journal voucher details as <voucherDetails> with subledger <isPresent>
    And officer will enter the approval details as <approvalOfficer1>
    And officer will get successful voucher created and closes it
    Then user will be notified by "Created"
    And current user logs out

    And the next user will be logged in
    And he chooses to act upon above application number
    And officer will enter the approval details as <approvalOfficer2>
    And officer will closes the acknowledgement page
    Then user will be notified by "forwarded"
    And current user logs out

    And the next user will be logged in
    And he chooses to act upon above application number
    And officer will enter the approval details as <approvalOfficer3>
    And officer will closes the acknowledgement page
    Then user will be notified by "forwarded"
    And current user logs out

    And the next user will be logged in
    And he chooses to act upon above application number
    And officer click on approval of the voucher
    And officer will closes the acknowledgement page
    Then user will be notified by "approved"
    And current user logs out

    And accountsOfficer logs in
    And user will select the required screen as "Bill Payment"
    And officer will modify the results depending upon the fund and date
    And officer will act upon the above voucher with payment mode as <paymentMode>

    And officer will enter the bank details
    And officer will enter the approval details as <approvalOfficer2>
    And officer will closes the successfull payment page
    And current user logs out

#    And examiner logs in
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

    And accountsOfficer logs in
    And officer search for the assignment mode as <assignment>
    And officer will filter the payment cheque assignment bill as <singleOrMultiple>
    And officer will select the <singleOrMultiple> bill and enter the details <assignment>
    And officer will close the successfull assignment page
    Then user will be notified by "successfully"
    And current user logs out

    Examples:
      | voucherDetails     | approvalOfficer1 | approvalOfficer2 | approvalOfficer3 | paymentMode | assignment | isPresent | singleOrMultiple |
      | voucherBillPayment | accountOfficer1  | accountOfficer2  | commissioner     | cheque      | cheque     | yes       | single           |
      | voucherBillPayment | accountOfficer1  | accountOfficer2  | commissioner     | RTGS        | RTGS       | yes       | single           |


