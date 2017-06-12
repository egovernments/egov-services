Feature: Create Contractors in the Masters
  As a registered user of the system
  I want able to create Contractors

#  Create Contractor

  @Works

  Scenario: Create Contractors

    When assistantEngineer logs in
    And user will select the required screen as "Create Contractor"
    And he enters the contractor master data
    Then user will be notified by "successfully"
    And he close the acknowledgement

    #Modify Contractor

    And user will select the required screen as "View/Modify Contractor"
    And he search for contractor
    And he select the required contractor
    And modifies the required contractor
    Then user will be notified by "successfully"
    And he close the acknowledgement
    And current user logs out