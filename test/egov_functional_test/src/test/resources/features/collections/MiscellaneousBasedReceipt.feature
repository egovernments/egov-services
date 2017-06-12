Feature: Create/Collect/Remit/Cancel Miscellaneous Receipt
  As a registered user of the system
  I am able to Create/Collect/Remit/Cancel Miscellaneous Receipt

  @Collections @Sanity @Smoke
  Scenario Outline: System should be able to create Miscellaneous receipt

    Given creator logs in
    And user will select the required screen as "miscellaneous receipt"
    And he enters Miscellaneous header

    And he pays using <paymentMethod>
    And user will notified by payment receipt as url receipts
    And current user closes acknowledgement
    And current user logs out

    Examples:
      | paymentMethod |
      | cash          |
      | cheque        |
      | dd            |
      | directBank    |


  @Collections @Sanity @Smoke
  Scenario: System should be able to cancel receipt

    Given creator logs in
    And he chooses to act upon the above receipt in drafts
    And he submit all collections
    Then user will be notified by "Submitted"
    And user closes the acknowledgement
    And current user logs out

    And adm_manager logs in
    And he chooses to act upon the above receipt in inbox
    And he approves all collections
    Then user will be notified by "Approved"
    And user closes the acknowledgement

    And user will select the required screen as "search receipts"
    And he search for required receipt
    And he selects the required receipt
    And he cancel the receipt
    Then user will be notified by "Cancelled"
    And user closes the acknowledgement
    And current user logs out


  @Collections @Sanity @Smoke
  Scenario: Remittance of receipt

    Given adm_manager logs in
    And user will select the required screen as "bank remittance"
    And he select the required file with bank details
    Then user will be notified by "successfully"
    And user closes the acknowledgement
    And current user logs out



