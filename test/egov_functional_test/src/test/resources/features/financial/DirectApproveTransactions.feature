Feature: In this feature the transactions are related to create and direct approve

  @Sanity @Finance
  Scenario Outline: To create a direct journal voucher with budget check and with subledger

    Given accountsOfficer logs in
    And user will select the required screen as "Create Journal Voucher"
    And officer will enter the journal voucher details as <voucherDetails> with subledger <isPresent>
    And officer will click on the direct approve button
    And officer will get the successful create and approve page and closes it
    Then user will be notified by "Sucessfully"
    And current user logs out

    Examples:
      | voucherDetails           | isPresent |
      | budgetCheckWithSubledger | yes       |

  @Sanity @Finance
  Scenario Outline: To create a direct journal voucher with budget check and with out subledger

    Given accountsOfficer logs in
    And user will select the required screen as "Create Journal Voucher"
    And officer will enter the journal voucher details as <voucherDetails> with subledger <isPresent>
    And officer will click on the direct approve button
    And officer will get the successful create and approve page and closes it
    Then user will be notified by "Sucessfully"
    And current user logs out

    Examples:
      | voucherDetails              | isPresent |
      | budgetCheckWithOutSubledger | no        |

  @Sanity @Finance
  Scenario Outline: To create direct bank payments with cheque and rtgs modes and also applying the check assignment

    Given accountsOfficer logs in
    And user will select the required screen as "Direct Bank Payments"
    And officer will enter the direct bank payment details as <bankDetails> with mode as <paymentMode>
    And officer will click on the direct approve button
    And officer will see the successful voucher creation page and closes it
    Then user will be notified by "Successful"

    And officer search for the assignment mode as <assignment>
    And officer will filter the payment cheque assignment bill as <singleOrMultiple>
    And officer will select the <singleOrMultiple> bill and enter the details <assignment>
    And officer will close the successfull assignment page
    Then user will be notified by "successfully"
    And current user logs out

    Examples:
      | bankDetails       | paymentMode | assignment | singleOrMultiple |
      | directBankDetails | cheque      | cheque     | single           |
      | directBankDetails | RTGS        | RTGS       | single           |

  @Sanity @Finance
  Scenario Outline: To create a direct bank payments with cash mode

    Given accountsOfficer logs in
    And user will select the required screen as "Direct Bank Payments"
    And officer will enter the direct bank payment details as <bankDetails> with mode as <paymentMode>
    And officer will click on the direct approve button
    And officer will see the successful voucher creation page and closes it
    Then user will be notified by "Successful"
    And current user logs out

    Examples:
      | bankDetails       | paymentMode |
      | directBankDetails | cash        |

  @Sanity @Finance
  Scenario Outline: To create the direct financial journal voucher with type General

    Given accountsOfficer logs in
    And user will select the required screen as "Create Journal Voucher"
    And officer will enter the journal voucher details as <voucherDetails> with subledger <isPresent>
    And officer will click on the direct approve button
    And officer will get successful voucher created and closes it
    Then user will be notified by "Created"
    And current user logs out

    Examples:
      | isPresent | voucherDetails          |
      | no        | voucherWithOutSubledger |
      | yes       | voucherWithSubledger    |

  @Sanity @Finance
  Scenario Outline: To create the direct financial journal voucher with type expense

    Given accountsOfficer logs in
    And user will select the required screen as "Create Journal Voucher"
    And officer will enter the journal voucher details as <voucherDetails> with subledger <isPresent>
    And officer will click on the direct approve button
    And officer will get successful voucher created and closes it
    Then user will be notified by "Created"
    And current user logs out

    Examples:
      | isPresent | voucherDetails          |
      | no        | voucherWithOutSubledger |
      | yes       | voucher2                |

  @Sanity @Finance
  Scenario Outline: To create a direct journal voucher as well as payment with different modes

    Given accountsOfficer logs in
    And user will select the required screen as "Create Journal Voucher"
    And officer will enter the journal voucher details as <voucherDetails> with subledger <isPresent>
    And officer will click on the direct approve button
    And officer will get successful voucher created and closes it
    Then user will be notified by "Created"

    And user will select the required screen as "Bill Payment"
    And officer will modify the results depending upon the fund and date
    And officer will act upon the above voucher with payment mode as <paymentMode>

    And officer will enter the bank details
    And officer will click on the direct approve button
    And officer will closes the successfull payment page
    And current user logs out

    Examples:
      | isPresent | voucherDetails     | paymentMode |
      | yes       | voucherBillPayment | cheque      |
      | yes       | voucherBillPayment | cash        |
      | yes       | voucherBillPayment | RTGS        |

  @Sanity @Finance
  Scenario Outline: To create a direct new expense bill

    Given accountsOfficer logs in
    And user will select the required screen as "New Create Expense Bill"
    And officer will the expense bill details as <billDetails>
    And officer will click on the direct approve button
    And officer will closes the expense acknowledgement page
    Then user will be notified by "approved"

    And user will select the required screen as "Create Voucher"
    And officer will filter the bill according to the type
    And officer will click on the direct approve button
    And officer will set the new expense voucher number and closes it
    Then user will be notified by "created"
    And current user logs out

    Examples:
      | billDetails |
      | expenseBill |

  @Sanity @Finance
  Scenario Outline: To create a direct journal voucher as well as payment with different modes and also applying check assignment

    Given accountsOfficer logs in
    And user will select the required screen as "Create Journal Voucher"
    And officer will enter the journal voucher details as <voucherDetails> with subledger <isPresent>
    And officer will click on the direct approve button
    And officer will get successful voucher created and closes it
    Then user will be notified by "Created"

    And user will select the required screen as "Bill Payment"
    And officer will modify the results depending upon the fund and date
    And officer will act upon the above voucher with payment mode as <paymentMode>

    And officer will enter the bank details
    And officer will click on the direct approve button
    And officer will closes the successfull payment page
    And current user logs out

    And accountsOfficer logs in
    And officer search for the assignment mode as <assignment>
    And officer will filter the payment cheque assignment bill as <singleOrMultiple>
    And officer will select the <singleOrMultiple> bill and enter the details <assignment>
    And officer will close the successfull assignment page
    Then user will be notified by "successfully"
    And current user logs out

    Examples:
      | isPresent | voucherDetails     | paymentMode | assignment | singleOrMultiple |
      | yes       | voucherBillPayment | cheque      | cheque     | single           |
      | yes       | voucherBillPayment | RTGS        | RTGS       | single           |

  @Sanity @Finance
  Scenario Outline: To create the remittance recovery with expense type

    Given admin logs in
    And user will select the required screen as "Modify Detailed Code"
    And user will enter the account code to modify as <glCode>
    And user will map the account code to particular
    And current user logs out

    And accountsOfficer logs in
    And user will select the required screen as "Create Journal Voucher"
    And officer will enter the journal voucher details as <voucherDetails> with subledger <isPresent>
    And officer will click on the direct approve button
    And officer will get successful voucher created and closes it
    Then user will be notified by "Created"

    And user will select the required screen as "Create Remittance Recovery"
    And officer will search for <singleOrMultiple> remittance bill
    And officer will enter the remittance bank details
    And officer will click on the direct approve button
    And officer will closes the acknowledgement page
    Then user will be notified by "Succesfully"

    And user will select the required screen as "Cheque Assignment" with condition as "beforeSearchForRemittance"
    And officer will filter the payment cheque assignment bill as <singleOrMultiple>
    And officer will select the <singleOrMultiple> bill and enter the details <assignment>
    And officer will close the successfull assignment page
    Then user will be notified by "successfully"
    And current user logs out

    Examples:
      | glCode  | assignment | voucherDetails | singleOrMultiple | isPresent |
      | 3502002 | remittance | remittance     | single           | yes       |

