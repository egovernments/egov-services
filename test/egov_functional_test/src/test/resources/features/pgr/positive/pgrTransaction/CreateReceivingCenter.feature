Feature: Creating a receiving center

  @PGR
  Scenario Outline: Create A Receiving Center and registering a complaint

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value ramana
    And user on Login screen types on password value demo
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create Receiving Center
    And user on Home screen clicks on firstMenuItem

    ### On Create Receiving Center Screen ###
    And user on Grievance screen verifies text has visible value Receiving Center
    And user on Grievance screen types on receivingCenterName value --"ReceivingCenter ", 5 random characters
    And user on Grievance screen copies the receivingCenterName to receivingCenterName
    And user on Grievance screen types on receivingCenterCode value --5 random numbers
    And user on Grievance screen types on receivingCenterOrderNo value --"1",9 random numbers
    And user on Grievance screen clicks radio button or checkbox on receivingCenterActive
    And user on Grievance screen clicks radio button or checkbox on receivingCenterCRN
    And user on Grievance screen clicks on createButton
    And user on Grievance screen clicks on close

    ### On Homepage Screen ###
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Officials Register Grievance
    And user on Home screen clicks on firstMenuItem

    ### On Create Complaint Grievance Screen Entering Contact Information ###
    And user on Grievance screen verifies contactInfo has visible value Contact Information
    And user on Grievance screen selects on receivingMode value Manual
    And user on Grievance screen selects on receivingCenter value receivingCenterName
    And user on Grievance screen types on receivingCRN value --6 digit random number
    And user on Grievance screen types on name value --"User ", 4 random characters
    And user on Grievance screen types on mobileNumber value --"1",9 Digit Number
    And user on Grievance screen types on email value --email

    ### On Create Complaint Grievance Screen Entering Grievance Information ###
    And user on Grievance screen selects on grievanceCategory value Revenue
    And user on Grievance screen selects on grievanceType value Unauthorised Advt. Boards

    ### On Create Complaint Grievance Screen Entering More Details ###
    And user on Grievance screen types on grievanceDetails value Grievance Details
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
    And DataIntent:LoginIntent
      | user |
      | demo |

    ### On HomePage Screen ###
    And user on Home screen types on dashBoardSearch with above applicationNumber
    And user on Home screen opens on dashBoardSearch with above applicationNumber

    ### On Grievance Screen ###
    And user on Grievance screen will see the complaintDetails
    And user on Grievance screen selects on changeStatus value <status>
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
    And user on SearchGrievance screen verifies crnStatus has visible value <status>

    ### Logout ###
    And Intent:LogoutIntentTest

    Examples:
      | status    |
      | COMPLETED |
      | REJECTED  |