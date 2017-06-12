Feature: In this feature the following are created as well as edited

  1. Create/Edit Legal Case.
  2. Create/Edit Hearings
  3. Create/Edit Interim Order
  4. Create/Edit Judgment
  5. Create/Edit Judgment Implementation
  6. Case Closure

  @Sanity @LegalCaseManagement
  Scenario Outline: It includes the creation and edit of Legal Case , Hearings , Interim Order , Judgement,
  Judgment Implementation and Case Closure

    ##########################################################
                  # Creating a Legal Case #
    ##########################################################

    Given admin logs in
    And user will select the required screen as "Create Legal Case"
    And user will enter the legal case details as <legalCaseData>
    And user closes the successful acknowledgement form
    And user will be notified by "successfully."

    ##########################################################
                  # Creating a Legal Case #
    ##########################################################

    And user will select the required screen as "Search Legal Case"
    And user will enter the case file number to search the file
    And user will take the corresponding action on above as <action1>
    And user closes the successful acknowledgement form
    And user will be notified by "updated"

    #########################################################
                    # Create Hearings #
    #########################################################

    And user will select the required screen as "Search Legal Case"
    And user will enter the case file number to search the file
    And user will take the corresponding action on above as <action2>
    And user will closes the successful created or updated page
    And user will be notified by "created"

    #########################################################
                    # Editing Hearings #
    #########################################################

    And user will select the required screen as "Search Legal Case"
    And user will enter the case file number to search the file
    And user will take the corresponding action on above as <action3>
    And user will closes the successful created or updated page
    And user will be notified by "updated"

    ########################################################
                  #  Create Interim Order #
    ########################################################

    And user will select the required screen as "Search Legal Case"
    And user will enter the case file number to search the file
    And user will take the corresponding action on above as <action4>
    And user will closes the successful created or updated page
    And user will be notified by "Created"

    ########################################################
                   # Editing Interim Order #
    ########################################################

    And user will select the required screen as "Search Legal Case"
    And user will enter the case file number to search the file
    And user will take the corresponding action on above as <action5>
    And user will closes the successful created or updated page
    And user will be notified by "updated"

    ########################################################
                   # Add/Edit StandingCouncil #
    ########################################################

    And user will select the required screen as "Search Legal Case"
    And user will enter the case file number to search the file
    And user will take the corresponding action on above as <action11>
    And user will closes the successful created or updated page
    And user will be notified by "successfully."

    ########################################################
                 # Add/edit Counter Affidavit #
    ########################################################

    And user will select the required screen as "Search Legal Case"
    And user will enter the case file number to search the file
    And user will take the corresponding action on above as <action12>
    And user will closes the successful created or updated page
    And user will be notified by "Successfully."

    #########################################################
                    # Create Judgment #
    #########################################################

    And user will select the required screen as "Search Legal Case"
    And user will enter the case file number to search the file
    And user will take the corresponding action on above as <action6>
    And user will closes the successful created or updated page
    And user will be notified by "Created"

    #########################################################
                        # Edit Judgment #
    #########################################################

    And user will select the required screen as "Search Legal Case"
    And user will enter the case file number to search the file
    And user will take the corresponding action on above as <action7>
    And user will closes the successful created or updated page
    And user will be notified by "updated"

    ########################################################
              # Create Judgment Implementation #
    ########################################################

    And user will select the required screen as "Search Legal Case"
    And user will enter the case file number to search the file
    And user will take the corresponding action on above as <action8>
    And user will enter the details of judgment implementation details based on <implementationMode>
    And user will closes the successful created or updated page
    And user will be notified by "successfully."

    #########################################################
              # Edit Judgment Implementation #
    #########################################################

    And user will select the required screen as "Search Legal Case"
    And user will enter the case file number to search the file
    And user will take the corresponding action on above as <action9>
    And user will enter the details of judgment implementation details based on <editImplementationMode>
    And user will closes the successful created or updated page
    And user will be notified by "successfully."

     #########################################################
                        # Close Case #
     #########################################################

    And user will select the required screen as "Search Legal Case"
    And user will enter the case file number to search the file
    And user will take the corresponding action on above as <action10>
    And user will closes the successful created or updated page
    And user will be notified by "closed"
    And current user logs out

    Examples:
      | legalCaseData | action1       | action2  | action3      | action4      | action5     | action6  | action7      | action8                | implementationMode | action9                    | editImplementationMode | action10  | action11                 | action12                  |
      | testData1     | editLegalCase | hearings | editHearings | interimOrder | editInterim | judgment | editJudgment | judgmentImplementation | Yes                | editJudgmentImplementation | edit_Yes               | closeCase | addOrEditStandingCouncil | addOrEditCounterAffidavit |
      | testData1     | editLegalCase | hearings | editHearings | interimOrder | editInterim | judgment | editJudgment | judgmentImplementation | No_Appeal          | editJudgmentImplementation | edit_No_Appeal         | closeCase | addOrEditStandingCouncil | addOrEditCounterAffidavit |
      | testData1     | editLegalCase | hearings | editHearings | interimOrder | editInterim | judgment | editJudgment | judgmentImplementation | No_Contempt        | editJudgmentImplementation | edit_No_Contempt       | closeCase | addOrEditStandingCouncil | addOrEditCounterAffidavit |
      | testData1     | editLegalCase | hearings | editHearings | interimOrder | editInterim | judgment | editJudgment | judgmentImplementation | InProgress         | editJudgmentImplementation | edit_InProgress        | closeCase | addOrEditStandingCouncil | addOrEditCounterAffidavit |




