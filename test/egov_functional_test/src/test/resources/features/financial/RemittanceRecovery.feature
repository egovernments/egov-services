Feature: To create a new remittance recovery

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

    And accountsOfficer logs in
    And user will select the required screen as "Create Remittance Recovery"
    And officer will search for <singleOrMultiple> remittance bill
    And officer will enter the remittance bank details
    And officer will enter the approval details as <approvalOfficer2>
    And officer will closes the acknowledgement page
    Then user will be notified by "forwarded"
    And current user logs out

    And examiner logs in
    And he chooses to act upon above application number
    And officer will enter the approval details as <approvalOfficer3>
    And officer will closes the acknowledgement page
    Then user will be notified by "forwarded"
    And current user logs out

    And commissioner logs in
    And he chooses to act upon above application number
    And officer click on approval of the voucher
    And officer will closes the acknowledgement page
    Then user will be notified by "approved"
    And current user logs out

    ###################################################################
          # Creating check assignment for Remittance Recovery #
    ###################################################################

    And accountsOfficer logs in
    And user will select the required screen as "Cheque Assignment" with condition as "beforeSearchForRemittance"
    And officer will filter the payment cheque assignment bill as <singleOrMultiple>
    And officer will select the <singleOrMultiple> bill and enter the details <assignment>
    And officer will close the successfull assignment page
    Then user will be notified by "successfully"
    And current user logs out

    Examples:
      | voucherDetails | isPresent | approvalOfficer1 | approvalOfficer2 | approvalOfficer3 | glCode  | assignment | singleOrMultiple |
      | remittance     | yes       | accountOfficer1  | accountOfficer2  | commissioner     | 3502002 | remittance | single           |


