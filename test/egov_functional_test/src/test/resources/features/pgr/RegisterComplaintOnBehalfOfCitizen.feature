Feature: Registering a Complaint

  @PGR
  Scenario: Creating a complaint on behalf of Citizen

    ### On Login Screen ###
    And user on Login screen verifies cityText has visible value Roha Municipal Corporation
    Given user on Login screen types on username value ramana
    And user on Login screen types on password value demo
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen clicks on menu
    And user on Home screen types on search value Officials Register Grievance
    And user on Home screen clicks on firstMenuItem

    ### On Create Complaint Grievance Screen ###
    And user on Grievance screen verifies contactInfo has visible value Contact Information
    And user on Grievance screen selects receivingMode with value as Call
    And user on Grievance screen types on name value User
    And user on Grievance screen types on mobileNumber value --"9",9 Digit Number
    And user on Grievance screen types on email value --email

    And user on Grievance screen selects grievanceCategory with value as Revenue
    And user on Grievance screen selects grievanceType with value as Unauthorised Advt. Boards
    And user on Grievance screen types on grievanceDetails value Grievance Details
    And user on Grievance screen types on grievanceLocation suggestion box with value Bank Road
    And user on Grievance screen uploads on selectPhoto value /home/vinay/pgrDocument.jpg
    And user on Grievance screen clicks on create

