Feature: As a registered user will create a building planning and checking the flow till the end

  Scenario Outline: Create A Building Planning Registration

    Given ulbOperator logs in
    And user will select the required screen as "New BPA Application"
    And user will enter the Building Application Details
    And user will enter the Applicant Details
    And user will enter the Site Details
    And user will enter the Services and Amenities Measurement Details
    And user will enter the Building Details
    And user will click on submit the application
    And user will collect the fee from the applicant
    And user will close the acknowledgement form
    And current user logs out

    ########################################################
    ###    Application is Went to Superintendent Inbox   ###
    ########################################################

    Given superintendentBPA logs in
    And he chooses to act upon above application number
    And user will enter the approval details as <approvalOfficer1> and forwards it
    And current user logs out

    #########################################################
    ###    Application is Went to JuniorAssistant Inbox   ###
    #########################################################

    Given ulbOperator logs in
    And he chooses to act upon above application number
    And user will provide the appointment for the verification of documents
    And he chooses to act upon above application number
    And user takes action on Document Scrutiny
    And user will enter the approval details as <approvalOfficer2> and forwards it
    And current user logs out

    ############################################################
    ###    Application is Went to Assistant Engineer Inbox   ###
    ############################################################

    Given assistantEngineerBPA logs in
    And he chooses to act upon above application number
    And user will enter the approval details as <approvalOfficer3> and forwards it
    And current user logs out

    #########################################################################
    ###    Application is Went to Town Planning Building Overseer Inbox   ###
    #########################################################################

    Given overseer logs in
    And he chooses to act upon above application number
    And user will provide the appointment for the verification of documents
    And he chooses to act upon above application number
    And user takes action on Capturing the Field Inspection Details
    And he chooses to act upon above application number
    And user will enter the approval details as <approvalOfficer2> and forwards it
    And current user logs out

    ############################################################
    ###    Application is Went to Assistant Engineer Inbox   ###
    ############################################################

    Given assistantEngineerBPA logs in
    And he chooses to act upon above application number
    And user will enter the approval details as <approvalOfficer4> and forwards it
    And current user logs out

    ###############################################################
    ###    Application is Went to Superintendent of NOC Inbox   ###
    ###############################################################

    Given superintendentOfNOC logs in
    And he chooses to act upon above application number
    And user will enter the approval details as <approvalOfficer2> and forwards it
    And current user logs out

    ############################################################
    ###    Application is Went to Assistant Engineer Inbox   ###
    ############################################################

    Given assistantEngineerBPA logs in
    And he chooses to act upon above application number
    And user will calculate the fees based on the building measurements
    And he chooses to act upon above application number
    And user will enter the approval details as <approvalOfficer5> and forwards it
    And current user logs out

    ######################################################################
    ###    Application is Went to Assistant Executive Engineer Inbox   ###
    ######################################################################

    Given assistantExecutiveEngineer logs in
    And he chooses to act upon above application number
    And user will enter the approval details as <approvalOfficer6> and forwards it
    And current user logs out

    ############################################################
    ###    Application is Went to Executive Engineer Inbox   ###
    ############################################################

    Given executiveEngineer logs in
    And he chooses to act upon above application number
    And user will enter the approval details as <approvalOfficer7> and forwards it
    And current user logs out

    ##############################################################
    ###    Application is Went to Corporation Engineer Inbox   ###
    ##############################################################

    Given corporationEngineer logs in
    And he chooses to act upon above application number
    And user will enter the approval details as <approvalOfficer8> and forwards it
    And current user logs out

    ########################################################
    ###      Application is Went to Secretary Inbox      ###
    ########################################################

    Given secretary logs in
    And he chooses to act upon above application number
    And secretary will approve the application with approval details as <approvalOfficer9> and forwards it
    And current user logs out

    ########################################################
    ###         Application is Went to ULB Inbox         ###
    ########################################################

    Given ulbOperator logs in
    And user will select the required screen as "Search Application" with condition as "bpa/application/search"
    And user will search the application based on application number
    And user will close the acknowledgement form
    And current user logs out

    ####################################################################
    ###    Application is Went to Superintendent of Approval Inbox   ###
    ####################################################################

    Given superintendentOfApproval logs in
    And he chooses to act upon above application number
    And user will enter the approval details as <approvalOfficer8> and forwards it
    And current user logs out

    ########################################################
    ###      Application is Went to Secretary Inbox      ###
    ########################################################

    Given secretary logs in
    And he chooses to act upon above application number
    And Secretary will apply the Digital Signature and forwards to <approvalOfficer9>
    And current user logs out

    ####################################################################
    ###    Application is Went to Superintendent of Approval Inbox   ###
    ####################################################################

    Given superintendentOfApproval logs in
    And he chooses to act upon above application number
    And user will click on Generate Permit Order
    And current user logs out

    Examples:
    | approvalOfficer1 | approvalOfficer2     | approvalOfficer3 | approvalOfficer4    | approvalOfficer5           | approvalOfficer6 | approvalOfficer7    | approvalOfficer8 | approvalOfficer9         |
    | ulbOperator      | assistantEngineerBPA | overseer         | superintendentOfNOC | assistantExecutiveEngineer | executiveEngineer| corporationEngineer | secretary        | superintendentOfApproval |
