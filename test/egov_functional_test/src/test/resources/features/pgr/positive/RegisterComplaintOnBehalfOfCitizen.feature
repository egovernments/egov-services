Feature: Registering a Complaint

  @PGR
  Scenario: Creating a complaint on behalf of Citizen

    ### On Login Screen ###
    Given user on Login screen verifies cityText has visible value Roha Municipal Corporation
    And user on Login screen types on username value ramana
    And user on Login screen types on password value demo
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will see the myTasks
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Officials Register Grievance
    And user on Home screen clicks on firstMenuItem

    ### On Create Complaint Grievance Screen Entering Contact Information ###
    And user on Grievance screen verifies contactInfo has visible value Contact Information
    And user on Grievance screen selects receivingMode with value as Call
    And user on Grievance screen types on name value --"User ", 3 random characters
    And user on Grievance screen types on mobileNumber value --"9",9 Digit Number
    And user on Grievance screen types on email value --email

    ### On Create Complaint Grievance Screen Entering Grievance Information ###
    And user on Grievance screen clicks on text value Spilling of Garbage from lorry
    And user on Grievance screen types on grievanceDetails value Grievance Details

    ### On Create Complaint Grievance Screen Entering More Details ###
    And user on Grievance screen types on grievanceLocation suggestion box with value Bank Road
    And user on Grievance screen uploads on selectPhoto value pgrDocument.jpg
    And user on Grievance screen clicks on create

    ### On Create Complaint Grievance Screen verifying the details ###
    And user on Grievance screen copies the complaintNum to applicationNumber
    And user on Grievance screen clicks on view
    And user on Grievance screen will see the complaintDetails
    And user on Grievance screen copies the userName to user

    ### Logout ###
    And Intent:LogoutIntentTest

    ### Login ###
    Given user on Login screen types on username value aboveUser
    And user on Login screen types on password value demo
    And user on Login screen clicks on signIn

    ### On HomePage Screen ###
    And user on Home screen types on dashBoardSearch with above applicationNumber
    And user on Home screen verifies applicationBox has visible value aboveApplicationNumber
    And user on Home screen clicks on applicationBox value aboveApplicationNumber

    ### On Grievance Screen ###
    And user on Grievance screen will see the complaintDetails
    And user on Grievance screen selects changeStatus with value as COMPLETED
    And user on Grievance screen types on comments value Comments
    And user on Grievance screen clicks on submitButton
    And user on Grievance screen clicks on okButton
    And user on Grievance screen will see the complaintDetails
    And user on Grievance screen clicks on homeButton

    ### On HomePage Screen ###
    And user on Home screen will see the myTasks
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Search Grievance
    And user on Home screen clicks on firstMenuItem

    ### On SearchGrievance Screen ###
    And user on SearchGrievance screen types on crnComplaintNumber with above applicationNumber
    And user on SearchGrievance screen clicks on crnSearchButton
    And user on SearchGrievance screen verifies crnStatus has visible value COMPLETED

    ### Logout ###
    And Intent:LogoutIntentTest

  @TestUsingIntent
  Scenario: Sample Scenario Using Intents
    Given DataIntent:LoginIntent
      | narasappa |
      | demo      |
    And DataIntent:SearchATransactionAndClickOnFirstMenuItem
      | Officials Register Grievance |
    And Intent:RegisterComplaintOnBehalfOfCitizen
    And Intent:LogoutIntentTest

    And DataIntent:LoginIntent
      | aboveUser |
      | demo      |
    And Intent:OpenApplicationOnHomePageScreen
    And Intent:VerificationOfComplaint
    And DataIntent:SearchATransactionAndClickOnFirstMenuItem
      | Search Grievance |
    And Intent:SearchingComplaintOnSearchGrievanceScreen
    And Intent:LogoutIntentTest



