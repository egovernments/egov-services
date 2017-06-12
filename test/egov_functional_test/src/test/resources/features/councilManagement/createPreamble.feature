Feature: create preamble

  As a Registered user of the system
  i want to create Preamble, create agenda, create meeting invitation
  enter the attendance for the meeting and create MOM

  # Create Preamble #

  @Sanity @CouncilManagement
  Scenario Outline: Register user choose to create Preamble, agenda,meeting invitation,attendance and MOM

    Given councilCreator logs in
    And user will select the required screen as "Create Preamble"
    And he enters create preamble details as <details>
    And he forwards for approver commissioner1
    And he copies preamble number and closes the acknowledgement
    Then user will be notified by "CREATED"
    And current user logs out

    When commissioner logs in
    And he chooses to act upon above application number
    And he approves the preamble number
    And user will be notified by "APPROVED"
    And current user logs out

    # Create Agenda #

    Given councilClerk logs in
    And user will select the required screen as "Create Agenda"
    And he choose to create agenda for the above preamble
    And he enters create agenda details as <committee>
    And he copies agenda number and closes the acknowledgement
    And user will be notified by "APPROVED"

    # Create Meeting #

    And user will select the required screen as "Create Meeting invitation"
    And he choose to create meeting invitation for the above agenda
    And he enters meeting details as <meetingDetails>
    And he copies meeting number and closes the acknowledgement

    # Enter Attendance #

    And user will select the required screen as "Enter Attendance"
    And he enters above meeting number to enter attendance
    And he choose to edit attendance details
    And he finalize attendance details and comes to home page

    # Create Council MOM #

    And user will select the required screen as "Create Council MOM"
    And he choose to create council MOM for the meeting number
    And he enters details to create MOM as <MOMdeatils>
    And user will be notified by "APPROVED"
    And current user logs out

    Examples:
      | details | committee    | meetingDetails | MOMdeatils |
      | abc     | createAgenda | councilMeeting | councilMOM |



