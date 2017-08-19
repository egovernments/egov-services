Feature: PGR Full Flow


  Scenario: PGR full flow from master

    Given grievanceAdmin on Login screen types on username value narasappa
    And grievanceAdmin on Login screen types on password value demo
    And grievanceAdmin on Login screen clicks on signIn
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value Create Category
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on Grievance screen types on categoryName value New Grievance Category1
    And grievanceAdmin on Grievance screen types on categoryCode value NGC1
    And grievanceAdmin on Grievance screen types on categoryDescription value New Grievance Category1
    And grievanceAdmin on Grievance screen clicks on createCategory
#    And grievanceAdmin on Grievance screen verifies successMSG has visible value Success
#    And grievanceAdmin on Grievance screen clicks on closeButton
#    And grievanceAdmin on home screen clicks on menu
#    And grievanceAdmin on home screen types on applicationSearchBox value Create Grievance Type
#    And grievanceAdmin on home screen clicks on applicationLink
#    And grievanceAdmin on Grievance screen types on grievanceTypeName value New Grievance Type1
#    And grievanceAdmin on Grievance screen types on grievanceTypeCode value NGT1
#    And grievanceAdmin on Grievance screen types on grievanceTypeDescription value New Grievance Type1
#    And grievanceAdmin on Grievance screen types on grievanceTypeSLAHours value 1
#    And grievanceAdmin on Grievance screen selects grievanceCategory with value as New Grievance Category1
#    And grievanceAdmin on Grievance screen clicks on Create
#    And grievanceAdmin on Grievance screen verifies successMSG has visible value Success
#    And grievanceAdmin on Grievance screen clicks on closeButton