Feature: PGR Full Flow


  Scenario: Register grievance with photo upload by selecting grievance category and Grievance Type from dropdown

         ### On Login Screen ###
#    Given citizen on Login screen verifies SignInText has visible value Sign In
    And citizen on Login screen types on username value 7777777777
    And citizen on Login screen types on password value eGov@123
    And citizen on Login screen clicks on signIn

    ### On Homepage Screen ###
    And citizen on CitizenPortalHome screen verifies profileName has visible value eGov
    And citizen on CitizenPortalHome screen clicks on createGrievance
    And citizen on CitizenGrievance screen verifies createGrievancePage has visible value Grievance Information

    ### On Create Complaint Grievance Screen Entering Grievance Information ###
    And citizen on CitizenGrievance screen selects on grievanceCategory value Town Planning
    And citizen on CitizenGrievance screen will see the grievanceType
    And citizen on CitizenGrievance screen selects on grievanceType value Obstruction of Trees

    ### On Create Complaint Grievance Screen Entering More Details ###
    And citizen on CitizenGrievance screen types on grievanceDetails value TestingTheGrievance
    And citizen on CitizenGrievance screen types on grievanceLocation suggestion box with value Bank Road
    And citizen on CitizenGrievance screen uploads on selectPhoto value pgrDocument.jpg
    And citizen on CitizenGrievance screen clicks on create

    ### On View Complaint Grievance Screen verifying the details ###
    And citizen on CitizenGrievance screen copies the complaintNum to applicationNumber
    And citizen on CitizenGrievance screen clicks on view
    And citizen on CitizenGrievance screen will see the commentBox
    And citizen on CitizenGrievance screen verifies fileAttachment is enabled

    ### Logout ###
    And Intent:LogoutIntentTest

  Scenario: Register grievance by selecting location from autopopulation list

     ### On Login Screen ###
#    Given citizen on Login screen verifies SignInText has visible value Sign In
    And citizen on Login screen types on username value 7777777777
    And citizen on Login screen types on password value eGov@123
    And citizen on Login screen clicks on signIn

    ### On Homepage Screen ###
    And citizen on CitizenPortalHome screen verifies profileName has visible value eGov
    And citizen on CitizenPortalHome screen clicks on createGrievance
    And citizen on CitizenGrievance screen verifies createGrievancePage has visible value Grievance Information

    ### On Create Complaint Grievance Screen Entering Grievance Information ###
    And citizen on CitizenGrievance screen selects grievanceCategory with value as Town Planning
    And citizen on CitizenGrievance screen will see the grievanceType
    And citizen on CitizenGrievance screen selects grievanceType with value as Obstruction of Trees

    ### On Create Complaint Grievance Screen Entering More Details ###
    And citizen on CitizenGrievance screen types on grievanceDetails value TestingTheGrievance
    And citizen on CitizenGrievance screen types on grievanceLocation suggestion box with value Bank Road
    And citizen on CitizenGrievance screen clicks on create

    ### On View Complaint Grievance Screen verifying the details ###
    And citizen on CitizenGrievance screen copies the complaintNum to applicationNumber
    And citizen on CitizenGrievance screen clicks on view
    And citizen on CitizenGrievance screen will see the commentBox
    And citizen on CitizenGrievance screen verifies text has visible value Bank Road - Election Ward No 31

    ### Logout ###
    And Intent:LogoutIntentTest


   Scenario: Register grievance by selecting grievance category and grievance type from dropdown

     Given citizen on Login screen types on username value 7777777777
     And citizen on Login screen types on password value eGov@123
     And citizen on Login screen clicks on signIn

     ### On Homepage Screen ###
     And citizen on CitizenPortalHome screen verifies profileName has visible value eGov
     And citizen on CitizenPortalHome screen clicks on createGrievance

     ### On Create Complaint Grievance Screen Entering Grievance Information ###
     And citizen on CitizenGrievance screen verifies createGrievancePage has visible value Grievance Information
     And citizen on CitizenGrievance screen selects grievanceCategory with value as Town Planning
     And citizen on CitizenGrievance screen will see the grievanceType
     And citizen on CitizenGrievance screen selects grievanceType with value as Obstruction of Trees

     ### On Create Complaint Grievance Screen Entering More Details ###
     And citizen on CitizenGrievance screen types on grievanceDetails value TestingTheGrievance
     And citizen on CitizenGrievance screen types on grievanceLocation suggestion box with value Bank Road
     And citizen on CitizenGrievance screen clicks on create

     ### On View Complaint Grievance Screen verifying the details ###
     And citizen on CitizenGrievance screen copies the complaintNum to applicationNumber
     And citizen on CitizenGrievance screen clicks on view
     And citizen on CitizenGrievance screen will see the commentBox
     And citizen on CitizenGrievance screen verifies text has visible value applicationNumber

     ### Logout ###
     And Intent:LogoutIntentTest


  Scenario: Register grievance by selecting grievance type from top 5 list

    Given citizen on Login screen types on username value 7777777777
    And citizen on Login screen types on password value eGov@123
    And citizen on Login screen clicks on signIn

    ### On Homepage Screen ###
    And citizen on CitizenPortalHome screen verifies profileName has visible value eGov
    And citizen on CitizenPortalHome screen clicks on createGrievance

    ### On Create Complaint Grievance Screen Entering Grievance Information ###
    And citizen on CitizenGrievance screen verifies createGrievancePage has visible value Grievance Information
    And citizen on CitizenGrievance screen clicks on topOneComplaint

    ### On Create Complaint Grievance Screen Entering More Details ###
    And citizen on CitizenGrievance screen types on grievanceDetails value TestingTheGrievance
    And citizen on CitizenGrievance screen types on grievanceLocation suggestion box with value Bank Road
    And citizen on CitizenGrievance screen clicks on create
    And citizen on CitizenGrievance screen copies the complaintNum to applicationNumber

    ### On View Complaint Grievance Screen verifying the details ###
    And citizen on CitizenGrievance screen clicks on view
    And citizen on CitizenGrievance screen will see the commentBox
    And citizen on CitizenGrievance screen verifies text has visible value applicationNumber

    ### Logout ###
    And Intent:LogoutIntentTest

  Scenario: To verify file upload accepts the length upto 30 character

    Given citizen on Login screen types on username value 7777777777
    And citizen on Login screen types on password value eGov@123
    And citizen on Login screen clicks on signIn

    ### On Homepage Screen ###
    And citizen on CitizenPortalHome screen verifies profileName has visible value eGov
    And citizen on CitizenPortalHome screen clicks on createGrievance

    ### On Create Complaint Grievance Screen Entering Grievance Information ###
    And citizen on CitizenGrievance screen verifies createGrievancePage has visible value Grievance Information
    And citizen on CitizenGrievance screen selects grievanceCategory with value as Town Planning
    And citizen on CitizenGrievance screen will see the grievanceType
    And citizen on CitizenGrievance screen selects grievanceType with value as Obstruction of Trees

    ### On Create Complaint Grievance Screen Entering More Details ###
    And citizen on CitizenGrievance screen types on grievanceDetails value TestingTheGrievance
    And citizen on CitizenGrievance screen types on grievanceLocation suggestion box with value Bank Road

    ### Citizen uploads a file with name more than 30 characters ###
    And citizen on CitizenGrievance screen uploads on selectPhoto value 741a41d9-74bd-44b9-88c9-7250eb64498d.jpg
    And citizen on CitizenGrievance screen verifies photoValidationMSG has visible value File name length should not exceed 30 characters
    And citizen on CitizenGrievance screen clicks on text value Ok

    ### Logout ###
    And Intent:LogoutIntentTest

  Scenario: To verify file upload do not accepts above 5MB

    Given citizen on Login screen types on username value 7777777777
    And citizen on Login screen types on password value eGov@123
    And citizen on Login screen clicks on signIn

    ### On Homepage Screen ###
    And citizen on CitizenPortalHome screen verifies profileName has visible value eGov
    And citizen on CitizenPortalHome screen clicks on createGrievance

    ### On Create Complaint Grievance Screen Entering Grievance Information ###
    And citizen on CitizenGrievance screen verifies createGrievancePage has visible value Grievance Information
    And citizen on CitizenGrievance screen selects grievanceCategory with value as Town Planning
    And citizen on CitizenGrievance screen will see the grievanceType
    And citizen on CitizenGrievance screen selects grievanceType with value as Obstruction of Trees

    ### On Create Complaint Grievance Screen Entering More Details ###
    And citizen on CitizenGrievance screen types on grievanceDetails value TestingTheGrievance
    And citizen on CitizenGrievance screen types on grievanceLocation suggestion box with value Bank Road

    ### Citizen uploads a photo with size more than 5MB ###
    And citizen on CitizenGrievance screen uploads on selectPhoto value Myphone 166.JPG
    And citizen on CitizenGrievance screen verifies photoMoreThan5MBValidationMSG has visible value File size exceeds 5MB
    And citizen on CitizenGrievance screen clicks on text value Ok

    And Intent:LogoutIntentTest


  @Custom
  Scenario: Create Complaint using Citizen portal

    #Citizen Registers Complaint with his login#

    #Login as Citizen and create Grievance#
    Given citizen on Login screen verifies signInText has visible value Sign In
    Given DataIntent:LoginIntent
      |7777777777|
      |eGov@123  |

    ### On Homepage Screen ###
    And citizen on CitizenPortalHome screen verifies profileName has visible value eGov
    And citizen on CitizenPortalHome screen clicks on createGrievance

    ### On Create Complaint Grievance Screen Entering Grievance Information ###
    And citizen on CitizenGrievance screen verifies createGrievancePage has visible value Grievance Information
    And citizen on CitizenGrievance screen selects grievanceCategory with value as Town Planning
    And citizen on CitizenGrievance screen will see the grievanceType
    And citizen on CitizenGrievance screen selects grievanceType with value as Obstruction of Trees

     ### On Create Complaint Grievance Screen Entering More Details ###
    And citizen on CitizenGrievance screen types on grievanceDetails value TestingTheGrievance
    And citizen on CitizenGrievance screen types on grievanceLocation suggestion box with value Bank Road
    And citizen on CitizenGrievance screen clicks on create

    ### Citizen copy the CRN number ###
    And citizen on CitizenGrievance screen copies the complaintNum to applicationNumber
    And citizen on CitizenGrievance screen clicks on view
    And citizen on CitizenGrievance screen will see the complaintDetails

    #View Applicaion On Home Page#
    And citizen on CitizenPortalHome screen types on homePageSearch with above applicationNumber
    And citizen on CitizenPortalHome screen verifies applicationBox has visible value applicationNumber
    And Intent:LogoutIntentTest

    #Login as Employee to change status as Processing#
    Given employee on Login screen verifies signInText has visible value Sign In
    Given DataIntent:LoginIntent
      |narasappa|
      |demo     |
    And employee on Home screen verifies profileName has visible value narasappa
    And employee on Home screen opens on dashBoardSearch with above applicationNumber
    And employee on CitizenGrievance screen will see the grievanceStatus
    And employee on CitizenGrievance screen verifies grievanceStatus is enabled
    And employee on CitizenGrievance screen selects grievanceStatus with value as PROCESSING
    And employee on CitizenGrievance screen types on commentBox value Processing
    And employee on CitizenGrievance screen clicks on submitButton
    And employee on CitizenGrievance screen verifies successBox has visible value succesfully
    And employee on CitizenGrievance screen clicks on Ok
    And employee on CitizenGrievance screen will see the homeButton
    And employee on CitizenGrievance screen verifies homeButton is enabled
    And employee on CitizenGrievance screen clicks on homeButton
    And employee on Home screen verifies profileName has visible value narasappa
    And Intent:LogoutIntentTest

    #Login as Citizen to verify the status of Complaint#
    Given citizen on Login screen verifies signInText has visible value Sign In
    Given DataIntent:LoginIntent
      |7777777777|
      |eGov@123  |
    And citizen on CitizenPortalHome screen verifies profileName has visible value eGov
    And citizen on CitizenPortalHome screen types on homePageSearch with above applicationNumber
    And citizen on CitizenPortalHome screen verifies applicationBox has visible value aboveApplicationNumber
    And citizen on CitizenPortalHome screen verifies applicationBox has visible value PROCESSING
    And Intent:LogoutIntentTest

    #Login as Employee to verify the application and change status to forwarded#
    Given employee on Login screen verifies signInText has visible value Sign In
    Given DataIntent:LoginIntent
      |narasappa|
      |demo     |
    And employee on Home screen verifies profileName has visible value narasappa
    And employee on Home screen opens on dashBoardSearch with above applicationNumber
    And employee on CitizenGrievance screen verifies grievanceStatus is enabled
    And employee on CitizenGrievance screen selects grievanceStatus with value as FORWARDED
    And employee on CitizenGrievance screen verifies selectDepartment is enabled
    And employee on CitizenGrievance screen selects selectDepartment with value as ACCOUNTS
    And employee on CitizenGrievance screen verifies selectDesignation is enabled
    And employee on CitizenGrievance screen selects selectDesignation with value as Accounts Officer
    And employee on CitizenGrievance screen verifies selectPosition is enabled
    And employee on CitizenGrievance screen selects selectPosition with value as Ramana
    And employee on CitizenGrievance screen types on commentBox value Forwarded
    And employee on CitizenGrievance screen clicks on submitButton
    And employee on CitizenGrievance screen verifies successBox has visible value succesfully
    And employee on CitizenGrievance screen clicks on Ok
    And employee on CitizenGrievance screen will see the homeButton
    And employee on CitizenGrievance screen verifies homeButton is enabled
    And employee on CitizenGrievance screen will see the homeButton
    And employee on CitizenGrievance screen clicks on homeButton

    And employee on Home screen types on dashBoardSearch with above applicationNumber
    And employee on Home screen verifies dashBoardApplications has notvisible value aboveApplicationNumber
    And Intent:LogoutIntentTest

    #Login as Citizen to verify the status of Complaint#
    Given citizen on Login screen verifies signInText has visible value Sign In
    Given DataIntent:LoginIntent
      |7777777777|
      |eGov@123  |
    And citizen on CitizenPortalHome screen verifies profileName has visible value eGov
    And citizen on CitizenPortalHome screen types on homePageSearch with above applicationNumber
    And citizen on CitizenPortalHome screen verifies applicationBox has visible value aboveApplicationNumber
    And citizen on CitizenPortalHome screen verifies applicationBox has visible value FORWARDED
    And Intent:LogoutIntentTest

    #Login as Account officer to verify the application and Reject the application#
    Given accountOfficer on Login screen verifies signInText has visible value Sign In
    Given DataIntent:LoginIntent
      |ramana   |
      |demo     |
    And accountOfficer on Home screen opens on dashBoardSearch with above applicationNumber
    And accountOfficer on CitizenGrievance screen verifies grievanceStatus is enabled
    And accountOfficer on CitizenGrievance screen selects grievanceStatus with value as REJECTED
    And accountOfficer on CitizenGrievance screen types on commentBox value Rejected
    And accountOfficer on CitizenGrievance screen clicks on submitButton
    And accountOfficer on CitizenGrievance screen verifies successBox has visible value succesfully
    And accountOfficer on CitizenGrievance screen clicks on Ok
    And accountOfficer on CitizenGrievance screen will see the homeButton
    And accountOfficer on CitizenGrievance screen verifies homeButton is enabled
    And accountOfficer on CitizenGrievance screen clicks on homeButton

    And employee on Home screen types on dashBoardSearch with above applicationNumber
    And employee on Home screen verifies dashBoardApplications has notvisible value aboveApplicationNumber
    And Intent:LogoutIntentTest

    #Login As Citizen to verify the application and reopen the application#
    Given citizen on Login screen verifies signInText has visible value Sign In
    Given DataIntent:LoginIntent
      |7777777777|
      |eGov@123  |
    And citizen on CitizenPortalHome screen verifies profileName has visible value eGov
    And citizen on CitizenPortalHome screen types on homePageSearch with above applicationNumber
    And citizen on CitizenPortalHome screen verifies applicationBox has visible value aboveApplicationNumber
    And citizen on CitizenPortalHome screen verifies applicationBox has visible value REJECTED
    And citizen on CitizenPortalHome screen clicks on applicationBox value aboveApplicationNumber
    And citizen on CitizenGrievance screen selects grievanceStatus with value as REOPENED
    And citizen on CitizenGrievance screen clicks on Feedback
    And citizen on CitizenGrievance screen types on commentBox value Reopened
    And citizen on CitizenGrievance screen clicks on submitButton
    And citizen on CitizenGrievance screen verifies successBox has visible value succesfully
    And citizen on CitizenGrievance screen clicks on Ok
    And citizen on CitizenGrievance screen will see the homeButton
    And citizen on CitizenGrievance screen verifies homeButton is enabled
    And citizen on CitizenGrievance screen clicks on homeButton
    And Intent:LogoutIntentTest

    #Login as Employee to change status as Completed#
    Given accountOfficer on Login screen verifies signInText has visible value Sign In
    Given DataIntent:LoginIntent
      |ramana  |
      |demo    |
    And accountOfficer on Home screen verifies profileName has visible value ramana
    And accountOfficer on Home screen opens on dashBoardSearch with above applicationNumber
    And accountOfficer on CitizenGrievance screen verifies grievanceStatus is enabled
    And accountOfficer on CitizenGrievance screen selects grievanceStatus with value as COMPLETED
    And accountOfficer on CitizenGrievance screen types on commentBox value Completed
    And accountOfficer on CitizenGrievance screen clicks on submitButton
    And accountOfficer on CitizenGrievance screen verifies successBox has visible value succesfully
    And accountOfficer on CitizenGrievance screen clicks on Ok
    And accountOfficer on CitizenGrievance screen will see the homeButton
    And accountOfficer on CitizenGrievance screen verifies homeButton is enabled
    And accountOfficer on CitizenGrievance screen clicks on homeButton
    And accountOfficer on Home screen verifies profileName has visible value ramana
    And Intent:LogoutIntentTest



  Scenario: Citizen withdraw the complaint which is registered by himself

    ### On Login Screen ###
#    Given citizen on Login screen verifies SignInText has visible value Sign In
    And citizen on Login screen types on username value 7777777777
    And citizen on Login screen types on password value eGov@123
    And citizen on Login screen clicks on signIn

    ### On Homepage Screen ###
    And citizen on CitizenPortalHome screen verifies profileName has visible value eGov
    And citizen on CitizenPortalHome screen clicks on createGrievance
    And citizen on CitizenGrievance screen verifies createGrievancePage has visible value Grievance Information

    ### On Create Complaint Grievance Screen Entering Grievance Information ###
    And citizen on CitizenGrievance screen selects grievanceCategory with value as Town Planning
    And citizen on CitizenGrievance screen will see the grievanceType
    And citizen on CitizenGrievance screen selects grievanceType with value as Obstruction of Trees

    ### On Create Complaint Grievance Screen Entering More Details ###
    And citizen on CitizenGrievance screen types on grievanceDetails value TestingTheGrievance
    And citizen on CitizenGrievance screen types on grievanceLocation suggestion box with value Bank Road
    And citizen on CitizenGrievance screen clicks on create

    ### On View Complaint Grievance Screen verifying the details ###
    And citizen on CitizenGrievance screen copies the complaintNum to applicationNumber
    And citizen on CitizenGrievance screen clicks on view

    ### On View Complaint Grievace Screen Withdraw the complaint ###
    And citizen on CitizenGrievance screen verifies text has visible value applicationNumber
    And citizen on CitizenGrievance screen selects grievanceStatus with value as WITHDRAWN
    And citizen on CitizenGrievance screen types on commentBox value Withdrawing the complaint
    And citizen on CitizenGrievance screen clicks on submitButton
    And citizen on CitizenGrievance screen verifies successBox has visible value succesfully
    And citizen on CitizenGrievance screen clicks on Ok
    And citizen on CitizenPortalHome screen verifies profileName has visible value eGov

    ### Citizen Verifies complaint is withdrawn ###
    And citizen on CitizenGrievance screen clicks on homeButton
    And citizen on CitizenPortalHome screen types on homePageSearch with above applicationNumber
    And citizen on CitizenPortalHome screen verifies applicationBox has visible value WITHDRAWN

    ### Logout ###
    And Intent:LogoutIntentTest


  Scenario: Citizen withdraw the complaint which is reopened by himself

### On Login Screen ###
#    Given citizen on Login screen verifies SignInText has visible value Sign In
    And citizen on Login screen types on username value 7777777777
    And citizen on Login screen types on password value eGov@123
    And citizen on Login screen clicks on signIn

    ### On Homepage Screen ###
    And citizen on CitizenPortalHome screen verifies profileName has visible value eGov
    And citizen on CitizenPortalHome screen clicks on createGrievance
    And citizen on CitizenGrievance screen verifies createGrievancePage has visible value Grievance Information

    ### On Create Complaint Grievance Screen Entering Grievance Information ###
    And citizen on CitizenGrievance screen selects grievanceCategory with value as Town Planning
    And citizen on CitizenGrievance screen will see the grievanceType
    And citizen on CitizenGrievance screen selects grievanceType with value as Obstruction of Trees

    ### On Create Complaint Grievance Screen Entering More Details ###
    And citizen on CitizenGrievance screen types on grievanceDetails value TestingTheGrievance
    And citizen on CitizenGrievance screen types on grievanceLocation suggestion box with value Bank Road
    And citizen on CitizenGrievance screen clicks on create

    ### On View Complaint Grievance Screen verifying the details ###
    And citizen on CitizenGrievance screen copies the complaintNum to applicationNumber
    And citizen on CitizenGrievance screen clicks on view
    And citizen on Grievance screen will see the complaintDetails
    And citizen on CitizenGrievance screen clicks on homeButton
    And citizen on CitizenPortalHome screen types on homePageSearch with above applicationNumber
    And citizen on CitizenPortalHome screen verifies applicationBox has visible value applicationNumber

    ### Logout ###
    And Intent:LogoutIntentTest

    #######Login as Account officer to verify the application and Reject the application#######

    ### Employee logsin ###
    Given accountOfficer on Login screen verifies signInText has visible value Sign In
    Given DataIntent:LoginIntent
      |narasappa   |
      |demo        |

    ### Employee opens the application from his inbox and reject it###
    And accountOfficer on Home screen opens on dashBoardSearch with above applicationNumber
    And citizen on Grievance screen will see the complaintDetails
    And accountOfficer on CitizenGrievance screen selects grievanceStatus with value as REJECTED
    And accountOfficer on CitizenGrievance screen types on commentBox value Rejected
    And accountOfficer on CitizenGrievance screen clicks on submitButton
    And accountOfficer on CitizenGrievance screen verifies successBox has visible value succesfully
    And accountOfficer on CitizenGrievance screen clicks on Ok
    And citizen on Grievance screen will see the complaintDetails
    And accountOfficer on CitizenGrievance screen clicks on homeButton


    ### Employee verifies complaint not there in his inbox ###
    And employee on Home screen types on dashBoardSearch with above applicationNumber
    And employee on Home screen verifies dashBoardApplications has notvisible value aboveApplicationNumber
    And Intent:LogoutIntentTest

    #######Login As Citizen to verify the application and reopen the complaint#######

    ### Citizen login ###
    Given citizen on Login screen verifies signInText has visible value Sign In
    Given DataIntent:LoginIntent
      |7777777777|
      |eGov@123  |
    And citizen on CitizenPortalHome screen verifies profileName has visible value eGov

    ### Citizen checks the status of the complaint ###
    And citizen on CitizenPortalHome screen types on homePageSearch with above applicationNumber
    And citizen on CitizenPortalHome screen verifies applicationBox has visible value REJECTED

    ### Citizen opens the complaint and reopen the same ###
    And citizen on CitizenPortalHome screen clicks on applicationBox value applicationNumber
    And citizen on CitizenGrievance screen verifies text has visible value applicationNumber
    And citizen on CitizenGrievance screen selects grievanceStatus with value as REOPENED
    And citizen on CitizenGrievance screen clicks on Feedback
    And citizen on CitizenGrievance screen types on commentBox value Reopened
    And citizen on CitizenGrievance screen clicks on submitButton
    And citizen on CitizenGrievance screen verifies successBox has visible value succesfully
    And citizen on CitizenGrievance screen clicks on Ok

    ### On View Complaint Grievace Screen Withdraw the complaint ###
    And citizen on CitizenGrievance screen verifies text has visible value applicationNumber
    And citizen on CitizenGrievance screen selects grievanceStatus with value as WITHDRAWN
    And citizen on CitizenGrievance screen types on commentBox value Withdrawing the complaint
    And citizen on CitizenGrievance screen clicks on submitButton
    And citizen on CitizenGrievance screen verifies successBox has visible value succesfully
    And citizen on CitizenGrievance screen clicks on Ok
    And citizen on Grievance screen will see the complaintDetails

    ### Citizen Verifies complaint is withdrawn ###
    And citizen on CitizenGrievance screen clicks on homeButton
    And citizen on CitizenPortalHome screen types on homePageSearch with above applicationNumber
    And citizen on CitizenPortalHome screen verifies applicationBox has visible value WITHDRAWN

    ### Logout ###
    And Intent:LogoutIntentTest


  Scenario: Citizen register the complaint, employee updates complaint to Processing and then resolves it, Citizen rate the complaint

    ### On Login Screen ###
#    Given citizen on Login screen verifies SignInText has visible value Sign In
    And citizen on Login screen types on username value 7777777777
    And citizen on Login screen types on password value eGov@123
    And citizen on Login screen clicks on signIn

    ### On Homepage Screen ###
    And citizen on CitizenPortalHome screen verifies profileName has visible value eGov
    And citizen on CitizenPortalHome screen clicks on createGrievance
    And citizen on CitizenGrievance screen verifies createGrievancePage has visible value Grievance Information

    ### On Create Complaint Grievance Screen Entering Grievance Information ###
    And citizen on CitizenGrievance screen selects grievanceCategory with value as Town Planning
    And citizen on CitizenGrievance screen will see the grievanceType
    And citizen on CitizenGrievance screen selects grievanceType with value as Obstruction of Trees

    ### On Create Complaint Grievance Screen Entering More Details ###
    And citizen on CitizenGrievance screen types on grievanceDetails value TestingTheGrievance
    And citizen on CitizenGrievance screen types on grievanceLocation suggestion box with value Bank Road
    And citizen on CitizenGrievance screen clicks on create

    ### On View Complaint Grievance Screen verifying the details ###
    And citizen on CitizenGrievance screen copies the complaintNum to applicationNumber
    And citizen on CitizenGrievance screen clicks on view
    And citizen on CitizenGrievance screen will see the complaintDetails
    And citizen on CitizenGrievance screen clicks on homeButton
    And citizen on CitizenPortalHome screen types on homePageSearch with above applicationNumber
    And citizen on CitizenPortalHome screen verifies applicationBox has visible value applicationNumber

    ### Citizen Logout ###
    And Intent:LogoutIntentTest


    ####### Login as employee to verify the complaint and resolve it #######

    ### Login as employee ###
    Given employee on Login screen verifies signInText has visible value Sign In
    Given DataIntent:LoginIntent
      |narasappa|
      |demo     |
    And employee on Home screen refresh's the webpage
    And employee on Home screen verifies profileName has visible value narasappa
    And employee on Home screen opens on dashBoardSearch with above applicationNumber

    ### And emplyee updates the status to Processing ###
    And employee on CitizenGrievance screen will see the grievanceStatus
    And employee on CitizenGrievance screen selects grievanceStatus with value as PROCESSING
    And employee on CitizenGrievance screen types on commentBox value Processing
    And employee on CitizenGrievance screen clicks on submitButton
    And employee on CitizenGrievance screen verifies successBox has visible value succesfully
    And employee on CitizenGrievance screen clicks on Ok
    And employee on CitizenGrievance screen will see the complaintDetails
    And employee on CitizenGrievance screen clicks on homeButton

    ### Employee opens the complaint from his inbox and update the complaint to Completed ###

    And accountOfficer on Home screen opens on dashBoardSearch with above applicationNumber
    And employee on CitizenGrievance screen will see the grievanceStatus
    And accountOfficer on CitizenGrievance screen selects grievanceStatus with value as COMPLETED
    And accountOfficer on CitizenGrievance screen types on commentBox value Completed
    And accountOfficer on CitizenGrievance screen clicks on submitButton
    And accountOfficer on CitizenGrievance screen verifies successBox has visible value succesfully
    And accountOfficer on CitizenGrievance screen clicks on Ok
    And employee on CitizenGrievance screen will see the complaintDetails
    And accountOfficer on CitizenGrievance screen clicks on homeButton
    And accountOfficer on Home screen verifies profileName has visible value narasappa

    ### Employee logout ###
    And Intent:LogoutIntentTest

    ####### Citizen rate the complaint #######

    ### Citizen login ###
    Given citizen on Login screen verifies signInText has visible value Sign In
    Given DataIntent:LoginIntent
      |7777777777|
      |eGov@123  |
    And citizen on CitizenPortalHome screen verifies profileName has visible value eGov

    ### Citizen checks the status of the complaint ###
    And citizen on CitizenPortalHome screen types on homePageSearch with above applicationNumber
    And citizen on CitizenPortalHome screen verifies applicationBox has visible value COMPLETED

    ### Citizen opens the complaint and reopen the same ###
    And citizen on CitizenPortalHome screen clicks on applicationBox value applicationNumber
    And citizen on CitizenGrievance screen will see the complaintDetails
    And citizen on CitizenGrievance screen clicks on Feedback
    And citizen on CitizenGrievance screen types on commentBox value Thanks for immediate response
    And citizen on CitizenGrievance screen clicks on submitButton
    And citizen on CitizenGrievance screen verifies successBox has visible value succesfully
    And citizen on CitizenGrievance screen clicks on Ok
    And citizen on CitizenPortalHome screen verifies profileName has visible value eGov

    ### Citizen logout ###
    And Intent:LogoutIntentTest


  Scenario:  Citizen register the complaint and employee updates complaint to Rejected, Citizen reopens

     ### On Login Screen ###
#    Given citizen on Login screen verifies SignInText has visible value Sign In
    And citizen on Login screen types on username value 7777777777
    And citizen on Login screen types on password value eGov@123
    And citizen on Login screen clicks on signIn

    ### On Homepage Screen ###
    And citizen on CitizenPortalHome screen verifies profileName has visible value eGov
    And citizen on CitizenPortalHome screen clicks on createGrievance
    And citizen on CitizenGrievance screen verifies createGrievancePage has visible value Grievance Information

    ### On Create Complaint Grievance Screen Entering Grievance Information ###
    And citizen on CitizenGrievance screen selects grievanceCategory with value as Town Planning
    And citizen on CitizenGrievance screen will see the grievanceType
    And citizen on CitizenGrievance screen selects grievanceType with value as Obstruction of Trees

    ### On Create Complaint Grievance Screen Entering More Details ###
    And citizen on CitizenGrievance screen types on grievanceDetails value TestingTheGrievance
    And citizen on CitizenGrievance screen types on grievanceLocation suggestion box with value Bank Road
    And citizen on CitizenGrievance screen clicks on create

    ### On View Complaint Grievance Screen verifying the details ###
    And citizen on CitizenGrievance screen copies the complaintNum to applicationNumber
    And citizen on CitizenGrievance screen clicks on view
    And citizen on CitizenGrievance screen will see the complaintDetails
    And citizen on CitizenGrievance screen clicks on homeButton
    And citizen on CitizenPortalHome screen types on homePageSearch with above applicationNumber
    And citizen on CitizenPortalHome screen verifies applicationBox has visible value applicationNumber

    ### Citizen Logout ###
    And Intent:LogoutIntentTest


    ####### Login as employee to verify the complaint and resolve it #######

    ### Login as employee ###
    Given employee on Login screen verifies signInText has visible value Sign In
    Given DataIntent:LoginIntent
      |narasappa|
      |demo     |
    And employee on Home screen refresh's the webpage
    And employee on Home screen verifies profileName has visible value narasappa
    And employee on Home screen opens on dashBoardSearch with above applicationNumber

    ### Employee rejects the complaint ###
    And employee on CitizenGrievance screen will see the complaintDetails
    And employee on CitizenGrievance screen selects grievanceStatus with value as REJECTED
    And employee on CitizenGrievance screen types on commentBox value Rejected
    And employee on CitizenGrievance screen clicks on submitButton
    And employee on CitizenGrievance screen verifies successBox has visible value succesfully
    And employee on CitizenGrievance screen clicks on Ok
    And employee on CitizenGrievance screen will see the complaintDetails
    And employee on CitizenGrievance screen clicks on homeButton


    ### Employee on Home screen verifies complaint not in his inbox ###
    And employee on Home screen types on dashBoardSearch with above applicationNumber
    And employee on Home screen verifies dashBoardApplications has notvisible value applicationNumber

    ### Employee Logout ###
    And Intent:LogoutIntentTest


     ### Citizen login ###
    Given citizen on Login screen verifies signInText has visible value Sign In
    Given DataIntent:LoginIntent
      |7777777777|
      |eGov@123  |
    And citizen on CitizenPortalHome screen verifies profileName has visible value eGov

    ### Citizen checks the status of the complaint ###
    And citizen on CitizenPortalHome screen types on homePageSearch with above applicationNumber
    And citizen on CitizenPortalHome screen verifies applicationBox has visible value REJECTED

    ### Citizen opens the complaint and reopen the same ###
    And citizen on CitizenPortalHome screen clicks on applicationBox value applicationNumber
    And citizen on CitizenGrievance screen verifies text has visible value applicationNumber
    And citizen on CitizenGrievance screen selects grievanceStatus with value as REOPENED
    And citizen on CitizenGrievance screen clicks on Feedback
    And citizen on CitizenGrievance screen types on commentBox value Reopened
    And citizen on CitizenGrievance screen clicks on submitButton
    And citizen on CitizenGrievance screen verifies successBox has visible value succesfully
    And citizen on CitizenGrievance screen clicks on Ok
    And employee on CitizenGrievance screen will see the complaintDetails
    And employee on CitizenGrievance screen clicks on homeButton
    And citizen on CitizenPortalHome screen types on homePageSearch with above applicationNumber
    And citizen on CitizenPortalHome screen verifies applicationBox has visible value REOPENED

    ### Citizen Logout ###
    And Intent:LogoutIntentTest


  Scenario:  Citizen register the complaint, employee forward the same to another and that employee resolves it

    ### On Login Screen ###
#    Given citizen on Login screen verifies SignInText has visible value Sign In
    And citizen on Login screen types on username value 7777777777
    And citizen on Login screen types on password value eGov@123
    And citizen on Login screen clicks on signIn

    ### On Homepage Screen ###
    And citizen on CitizenPortalHome screen verifies profileName has visible value eGov
    And citizen on CitizenPortalHome screen clicks on createGrievance
    And citizen on CitizenGrievance screen verifies createGrievancePage has visible value Grievance Information

    ### On Create Complaint Grievance Screen Entering Grievance Information ###
    And citizen on CitizenGrievance screen selects grievanceCategory with value as Town Planning
    And citizen on CitizenGrievance screen will see the grievanceType
    And citizen on CitizenGrievance screen selects grievanceType with value as Obstruction of Trees

    ### On Create Complaint Grievance Screen Entering More Details ###
    And citizen on CitizenGrievance screen types on grievanceDetails value TestingTheGrievance
    And citizen on CitizenGrievance screen types on grievanceLocation suggestion box with value Bank Road
    And citizen on CitizenGrievance screen clicks on create

    ### On View Complaint Grievance Screen verifying the details ###
    And citizen on CitizenGrievance screen copies the complaintNum to applicationNumber
    And citizen on CitizenGrievance screen clicks on view
    And citizen on CitizenGrievance screen will see the complaintDetails
    And citizen on CitizenGrievance screen clicks on homeButton
    And citizen on CitizenPortalHome screen types on homePageSearch with above applicationNumber
    And citizen on CitizenPortalHome screen verifies applicationBox has visible value applicationNumber

    ### Citizen Logout ###
    And Intent:LogoutIntentTest

    ####### Employee forward to another #######

    ### Employee Login ###
    Given employee on Login screen verifies signInText has visible value Sign In
    Given DataIntent:LoginIntent
      |narasappa|
      |demo     |
    And employee on Home screen refresh's the webpage
    And employee on Home screen verifies profileName has visible value narasappa

    ### Employee opens the application from his inbox ###
    And employee on Home screen opens on dashBoardSearch with above applicationNumber
    And citizen on CitizenGrievance screen will see the complaintDetails

    ### Forward the complaint to another ###
    And employee on CitizenGrievance screen selects grievanceStatus with value as FORWARDED
    And employee on CitizenGrievance screen verifies selectDepartment is enabled
    And employee on CitizenGrievance screen selects selectDepartment with value as ACCOUNTS
    And employee on CitizenGrievance screen selects selectDesignation with value as Accounts Officer
    And employee on CitizenGrievance screen selects selectPosition with value as Ramana
    And employee on CitizenGrievance screen types on commentBox value Forwarded
    And employee on CitizenGrievance screen clicks on submitButton
    And employee on CitizenGrievance screen verifies successBox has visible value succesfully
    And employee on CitizenGrievance screen clicks on Ok
    And citizen on CitizenGrievance screen will see the complaintDetails
    And employee on CitizenGrievance screen clicks on homeButton

    ### Employee verifies complaint not in his inbox ###
    And employee on Home screen types on dashBoardSearch with above applicationNumber
    And employee on Home screen verifies dashBoardApplications has notvisible value aboveApplicationNumber

    ### Employee logout ###
    And Intent:LogoutIntentTest

    ####### Login as employee to verify the complaint and resolve it #######

    ### Login as employee ###
    Given employee on Login screen verifies signInText has visible value Sign In
    Given DataIntent:LoginIntent
      |ramana|
      |demo     |
    And employee on Home screen refresh's the webpage
    And employee on Home screen verifies profileName has visible value Ramana
    And employee on Home screen opens on dashBoardSearch with above applicationNumber

    ### Employee rejects the complaint ###
    And employee on CitizenGrievance screen will see the complaintDetails
    And employee on CitizenGrievance screen selects grievanceStatus with value as COMPLETED
    And employee on CitizenGrievance screen types on commentBox value Resolved
    And employee on CitizenGrievance screen clicks on submitButton
    And employee on CitizenGrievance screen verifies successBox has visible value succesfully
    And employee on CitizenGrievance screen clicks on Ok
    And employee on CitizenGrievance screen will see the complaintDetails
    And employee on CitizenGrievance screen clicks on homeButton


    ### Employee on Home screen verifies complaint not in his inbox ###
    And employee on Home screen types on dashBoardSearch with above applicationNumber
    And employee on Home screen verifies dashBoardApplications has notvisible value applicationNumber

    ### Employee Logout ###
    And Intent:LogoutIntentTest