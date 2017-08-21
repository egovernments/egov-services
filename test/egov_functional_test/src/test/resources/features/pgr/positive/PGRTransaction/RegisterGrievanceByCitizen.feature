Feature: PGR Full Flow


  Scenario: Register grievance with photo upload by selecting grievance category and Grievance Type from dropdown

#    Given citizen on Login screen verifies SignInText has visible value Sign In
    Given citizen on Login screen types on username value 7777777777
    And citizen on Login screen types on password value eGov@123
    And citizen on Login screen clicks on signIn
    And citizen on CitizenPortalHome screen verifies profileName has visible value eGov
    And citizen on CitizenPortalHome screen clicks on createGrievance
    And citizen on CitizenGrievance screen verifies createGrievancePage has visible value Grievance Information
    And citizen on CitizenGrievance screen selects grievanceCategory with value as Town Planning
    And citizen on CitizenGrievance screen will see the grievanceType
    And citizen on CitizenGrievance screen selects grievanceType with value as Obstruction of Trees
    And citizen on CitizenGrievance screen types on grievanceDetails value TestingTheGrievance
    And citizen on CitizenGrievance screen types on grievanceLocation suggestion box with value Bank Road
    And citizen on CitizenGrievance screen uploads on selectPhoto value pgrDocument.jpg
    And citizen on CitizenGrievance screen clicks on create
    And citizen on CitizenGrievance screen copies the complaintNum to applicationNumber
    And citizen on CitizenGrievance screen clicks on view
    And citizen on CitizenGrievance screen will see the commentBox
    And citizen on CitizenGrievance screen verifies fileAttachment is visible
    And Intent:LogoutIntentTest

  Scenario: Register grievance by selecting location from autopopulation list

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value ramana
    And user on Login screen types on password value demo
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Officials Register Grievance
    And user on Home screen clicks on firstMenuItem

    ### On Create Complaint Grievance Screen Entering Contact Information ###
#    And user on Grievance screen verifies contactInfo has visible value Contact Information
    And user on Grievance screen selects receivingMode with value as Call
    And user on Grievance screen types on name value --"User ", 4 random characters
    And user on Grievance screen types on mobileNumber value --"1",9 Digit Number
    And user on Grievance screen types on email value --email

    ### On Create Complaint Grievance Screen Entering Grievance Information ###
    And user on Grievance screen selects grievanceCategory with value as Revenue
    And user on Grievance screen selects grievanceType with value as Unauthorised Advt. Boards

    ### On Create Complaint Grievance Screen Entering More Details ###
    And user on Grievance screen types on grievanceDetails value Grievance Details
    And user on Grievance screen types on grievanceLocation suggestion box with value Bank Road
    And user on Grievance screen clicks on create

    ### On Create Complaint Grievance Screen verifying the details ###
    And user on Grievance screen copies the complaintNum to applicationNumber
    And user on Grievance screen clicks on view
    And user on Grievance screen will see the complaintDetails
    And user on Grievance screen copies the userName to user

    ### Logout ###
    And Intent:LogoutIntentTest


   Scenario: Register grievance by selecting grievance category and grievance type from dropdown

     Given citizen on Login screen types on username value 7777777777
     And citizen on Login screen types on password value eGov@123
     And citizen on Login screen clicks on signIn
     And citizen on CitizenPortalHome screen verifies profileName has visible value eGov
     And citizen on CitizenPortalHome screen clicks on createGrievance
     And citizen on CitizenGrievance screen verifies createGrievancePage has visible value Grievance Information
     And citizen on CitizenGrievance screen selects grievanceCategory with value as Town Planning
     And citizen on CitizenGrievance screen will see the grievanceType
     And citizen on CitizenGrievance screen selects grievanceType with value as Obstruction of Trees
     And citizen on CitizenGrievance screen types on grievanceDetails value TestingTheGrievance
     And citizen on CitizenGrievance screen types on grievanceLocation suggestion box with value Bank Road
     And citizen on CitizenGrievance screen clicks on create
     And citizen on CitizenGrievance screen copies the complaintNum to applicationNumber
     And citizen on CitizenGrievance screen clicks on view
     And citizen on CitizenGrievance screen will see the commentBox
     And citizen on CitizenGrievance screen verifies CRN is visible
     And Intent:LogoutIntentTest


  Scenario: Register grievance by selecting grievance type from top 5 list

    Given citizen on Login screen types on username value 7777777777
    And citizen on Login screen types on password value eGov@123
    And citizen on Login screen clicks on signIn
    And citizen on CitizenPortalHome screen verifies profileName has visible value eGov
    And citizen on CitizenPortalHome screen clicks on createGrievance
    And citizen on CitizenGrievance screen verifies createGrievancePage has visible value Grievance Information
    And citizen on CitizenGrievance screen clicks on topOne
    And citizen on CitizenGrievance screen types on grievanceDetails value TestingTheGrievance
    And citizen on CitizenGrievance screen types on grievanceLocation suggestion box with value Bank Road
    And citizen on CitizenGrievance screen clicks on create
    And citizen on CitizenGrievance screen copies the complaintNum to applicationNumber
    And citizen on CitizenGrievance screen clicks on view
    And citizen on CitizenGrievance screen will see the commentBox
    And citizen on CitizenGrievance screen verifies CRN is visible
    And Intent:LogoutIntentTest

  Scenario: To verify file upload accepts the length upto 30 character

    Given citizen on Login screen types on username value 7777777777
    And citizen on Login screen types on password value eGov@123
    And citizen on Login screen clicks on signIn
    And citizen on CitizenPortalHome screen verifies profileName has visible value eGov
    And citizen on CitizenPortalHome screen clicks on createGrievance
    And citizen on CitizenGrievance screen verifies createGrievancePage has visible value Grievance Information
    And citizen on CitizenGrievance screen selects grievanceCategory with value as Town Planning
    And citizen on CitizenGrievance screen will see the grievanceType
    And citizen on CitizenGrievance screen selects grievanceType with value as Obstruction of Trees
    And citizen on CitizenGrievance screen types on grievanceDetails value TestingTheGrievance
    And citizen on CitizenGrievance screen types on grievanceLocation suggestion box with value Bank Road
    And citizen on CitizenGrievance screen uploads on selectPhoto value 741a41d9-74bd-44b9-88c9-7250eb64498d.jpg
    And citizen on CitizenGrievance screen verifies photoValidationMSG has visible value File name length should not exceed 30 characters
    And Intent:LogoutIntentTest

  Scenario: To verify file upload donot accepts above 5MB

    Given citizen on Login screen types on username value 7777777777
    And citizen on Login screen types on password value eGov@123
    And citizen on Login screen clicks on signIn
    And citizen on CitizenPortalHome screen verifies profileName has visible value eGov
    And citizen on CitizenPortalHome screen clicks on createGrievance
    And citizen on CitizenGrievance screen verifies createGrievancePage has visible value Grievance Information
    And citizen on CitizenGrievance screen selects grievanceCategory with value as Town Planning
    And citizen on CitizenGrievance screen will see the grievanceType
    And citizen on CitizenGrievance screen selects grievanceType with value as Obstruction of Trees
    And citizen on CitizenGrievance screen types on grievanceDetails value TestingTheGrievance
    And citizen on CitizenGrievance screen types on grievanceLocation suggestion box with value Bank Road
    And citizen on CitizenGrievance screen uploads on selectPhoto value Myphone 166.JPG
    And citizen on CitizenGrievance screen verifies photoMoreThan5MBValidationMSG has visible value File name length should not exceed 30 characters
    And Intent:LogoutIntentTest