Feature: All intents belongs to Complaint on behalf of citizen

  @Intent
  Scenario: RegisterComplaintOnBehalfOfCitizen
    ### On Create Complaint Grievance Screen Entering Contact Information ###
    And user on Grievance screen verifies contactInfo has visible value Contact Information
    And user on Grievance screen selects on receivingMode value Call
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

  @Intent
  Scenario: VerificationOfComplaint
    ### On Grievance Screen ###
    And user on Grievance screen will see the complaintDetails
    And user on Grievance screen selects on changeStatus value COMPLETED
    And user on Grievance screen types on comments value Comments
    And user on Grievance screen clicks on submitButton
    And user on Grievance screen clicks on okButton
    And user on Grievance screen will see the complaintDetails
    And user on Grievance screen clicks on homeButton

  @Intent
  Scenario: SearchingComplaintOnSearchGrievanceScreen
  ### On SearchGrievance Screen ###
    And user on SearchGrievance screen types on crnComplaintNumber with above applicationNumber
    And user on SearchGrievance screen clicks on crnSearchButton
    And user on SearchGrievance screen verifies crnStatus has visible value COMPLETED