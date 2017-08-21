Feature: Sample Test From PGR

#  @Custom
  Scenario: Create Complaint
    Given DataIntent:LoginIntent
    |narasappa|
    |     demo|
    And user on home screen clicks on menu
    And user on home screen types on applicationSearchBox value official
    And user on home screen clicks on applicationLink
    And user on grievance screen select on receivingMode value Letters
    And Intent:LogoutIntentTest

  @Custom
  Scenario: Create Complaint using Citizen portal

    #Citizen Registers Complaint with his login#

    #Login as Citizen and create Grievance#
    Given citizen on Login screen verifies signInText has visible value Sign In
    Given DataIntent:LoginIntent
    |7777777777|
    |eGov@123  |
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
    And citizen on CitizenGrievance screen verifies commentBox is enabled
    And citizen on CitizenGrievance screen clicks on homeButton
    And citizen on CitizenPortalHome screen verifies profileName has visible value eGov

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
    And accountOfficer on Home screen verifies profileName has visible value narasappa
    And Intent:LogoutIntentTest



