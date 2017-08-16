Feature: Sample Test From PGR

  @Custom
  Scenario: Create Complaint
    Given Intent:LoginIntentTest
    And user on home screen clicks on menu
    And user on home screen types on applicationSearchBox value official
    And user on home screen clicks on applicationLink
    And user on grievance screen select on receivingMode value Letters
    And Intent:LogoutIntentTest

  @Custom
  Scenario: Create Complaint using Citizen portal

    Given citizen on Login screen verifies SignInText has visible value Sign In
    And citizen on Login screen types on username value 9999999999
    And citizen on Login screen types on password value demo
    And citizen on Login screen clicks on signIn
    And citizen on CitizenPortalHome screen verifies profileName has visible value 999999999
    And citizen on CitizenPortalHome screen clicks on createGrievance
    And citizen on CitizenGrievance screen verifies createGrievancePage has visible value Grievance Information
    And citizen on CitizenGrievance screen clicks on grievanceCategory
    And citizen on CitizenGrievance screen clicks on grievanceCategoryValue value Public Health and Sanitation
    And citizen on CitizenGrievance screen clicks on grievanceType
    And citizen on CitizenGrievance screen clicks on grievanceTypeValue value Slaughter House
    And citizen on CitizenGrievance screen types on grievanceDetails value TestingTheGrievance
    And citizen on CitizenGrievance screen types on grievanceLocation value Bank Road - Election Ward No 31 - Srikakulam  Municipality
    And citizen on CitizenGrievance screen clicks on create
    And citizen on CitizenGrievance screen copies the complaintNum to applicationNumber
    And citizen on CitizenGrievance screen clicks on view
    And citizen on CitizenGrievance screen types on commentBox value Created
    And citizen on CitizenGrievance screen clicks on submitButton
    And citizen on CitizenGrievance screen verifies successBox has visible value Grievance updated succesfully
    And citizen on CitizenGrievance screen clicks on Ok
    And citizen on CitizenGrievance screen clicks on homeButton
    And citizen on CitizenPortalHome screen verifies profileName has visible value 999999999
    And Intent:LogoutIntentTest
#    And Intent:LoginIntentTest
#    And user on home screen types on applicationSearch with above applicationNumber