Feature: In this feature we are going to select the multiple files for

  1) Bill Payments
  2) Remittance Bill
  3) Assignments

  @OnceInMonth @Finance1
  Scenario Outline: To select the multiple bills from bill payment screen and approving the payment

    Given accountsOfficer logs in
    And user will select the required screen as "Bill Payment"
    And officer will search the bill based on department and fund with type as <vouchersType> with payment mode as <paymentMode>

    And officer will enter the bank details
    And officer will click on the direct approve button
    And officer will closes the successfull payment page
    And current user logs out

    Examples:
      | paymentMode | vouchersType |
      | cheque      | expense      |

  @OnceInMonth @Finance1
  Scenario Outline: To select the multiple remittance recovery application and approving the payment

    And accountsOfficer logs in
    And user will select the required screen as "Create Remittance Recovery"
    And officer will search for <singleOrMultiple> remittance bill
    And officer will enter the remittance bank details
    And officer will click on the direct approve button
    And officer will closes the acknowledgement page
    And current user logs out

    Examples:
      | singleOrMultiple |
      | multiple         |

  @OnceInMonth @Finance1
  Scenario Outline: To select the multiple remittance recovery cheque assignment applications

    And accountsOfficer logs in
    And user will select the required screen as "Cheque Assignment" with condition as "beforeSearchForRemittance"
    And officer will filter the payment cheque assignment bill as <singleOrMultiple>
    And officer will select the <singleOrMultiple> bill and enter the details <assignment>
    And officer will close the successfull assignment page
    Then user will be notified by "successfully"
    And current user logs out

    Examples:
      | singleOrMultiple | assignment |
      | multiple         | remittance |

  @OnceInMonth @Finance1
  Scenario Outline: To select the multiple cheque and rtgs assignment applications

    And accountsOfficer logs in
    And officer search for the assignment mode as <assignment>
    And officer will filter the payment cheque assignment bill as <singleOrMultiple>
    And officer will select the <singleOrMultiple> bill and enter the details <assignment>
    And officer will close the successfull assignment page
    Then user will be notified by "successfully"
    And current user logs out

    Examples:
      | singleOrMultiple | assignment |
      | multiple         | cheque     |
      | multiple         | RTGS       |



