Feature: Sample Test From PGR

#  @Custom
  Scenario: Create Complaint
    Given Intent:LoginIntentTest
    And user on home screen clicks on menu
    And user on home screen types on applicationSearchBox value official
    And user on home screen clicks on applicationLink
    And user on grievance screen select on receivingMode value Letters
    And Intent:LogoutIntentTest

  @Custom
  Scenario: Create Complaint using Citizen portal

    #Citizen Registers Complaint with his login#

    #Login#
    Given citizen on Login screen verifies SignInText has visible value Sign In
    And citizen on Login screen types on username value 7777777777
    And citizen on Login screen types on password value eGov@123
    And citizen on Login screen clicks on signIn
    And citizen on CitizenPortalHome screen verifies profileName has visible value Akhila


    #Create Grievance#
    And citizen on CitizenPortalHome screen clicks on createGrievance
    And citizen on CitizenGrievance screen verifies createGrievancePage has visible value Grievance Information
    And citizen on CitizenGrievance screen selects grievanceCategory with value as Public Health and Sanitation
    And citizen on CitizenGrievance screen selects grievanceType with value as Slaughter House
    And citizen on CitizenGrievance screen types on grievanceDetails value TestingTheGrievance
    And citizen on CitizenGrievance screen types on grievanceLocation suggestion box with value BankRoad
    And citizen on CitizenGrievance screen clicks on create
    And citizen on CitizenGrievance screen copies the complaintNum to applicationNumber
    And citizen on CitizenGrievance screen clicks on view
    And citizen on CitizenGrievance screen verifies commentBox is enabled
    And citizen on CitizenGrievance screen clicks on homeButton
    And citizen on CitizenPortalHome screen verifies profileName has visible value Akhila

    #View Applicaion On Home Page#
    And citizen on CitizenPortalHome screen types on homePageSearch with above applicationNumber
    And citizen on CitizenPortalHome screen verifies applicationBox has visible value aboveApplicationNumber
    And Intent:LogoutIntentTest

#    Given employee on Login screen verifies SignInText has visible value Sign In
#    And employee on Login screen types on username value Soumya
#    And employee on Login screen types on password value 12345678
#    And employee on Login screen clicks on signIn
#    And employee on CitizenPortalHome screen verifies profileName has visible value Soumya
#    And employee on Home screen opens on dashBoardSearch with above applicationNumber
#    And Intent:LogoutIntentTest
