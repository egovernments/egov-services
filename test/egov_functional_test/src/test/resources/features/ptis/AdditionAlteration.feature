Feature: Addition Alteration of a property

  As a regsistered user of the system
  I should able to create addition alteration of a property


   # ADDITION ALTERATION SCREEN #

  @Sanity @PropertyTax
  Scenario Outline: Registered user Update existing property

    Given commissioner logs in
    And user will select the required screen as "Data entry screen" with condition as "ptis"
    And he creates a new assessment for a private residential property
    Then dataEntry Details saved successfully
    And he choose to add edit DCB
    And he choose to close the dataentry acknowledgement screen
    And current user logs out

    Given juniorAssistant logs in
    And user will select the required screen as "Addition/Alteration of Assessment"
    And he searches for assessment with number
    And he updates assessment details as <editAssessmentDetails>
    And he enters amenities as <amenitiesDetails>
    And he enters Floor Details as <editFloorDetails>
    And he forwards for approval to billCollector
    And he will copy the acknowledgement message with assessment number modifyProperty-forward
    Then user will be notified by "successfully"
    And current user logs out

    When billCollector logs in
    And he chooses to act upon above assessment number
    And he forwards for approval to revenueInspector
    And current user closes acknowledgement
    And current user logs out

    When revenueInspector logs in
    And he chooses to act upon above assessment number
    And he forwards for approval to revenueOfficer
    And current user closes acknowledgement
    And current user logs out

    When revenueOfficer logs in
    And he chooses to act upon above assessment number
    And he forwards for approval to commissioner
    And current user closes acknowledgement
    And current user logs out

    When commissioner logs in
    And he chooses to act upon above assessment number
    And he approved the property with remarks addition "property approved"
    And current user closes acknowledgement

    And he chooses to act upon above assessment number
    And he does a digital signature

    When commissioner closes acknowledgement
    And current user logs out

    And juniorAssistant logs in
    And he chooses to act upon above assessment number
    And he generates a notice
    And current user logs out

    Examples:
      | editAssessmentDetails      | amenitiesDetails | editFloorDetails             |
      | assessmentAdditionProperty | all              | firstFloorAdditionaltaration |

