Feature: As a regsitered user of the system

  As a registered user of the system
  I should able to create revision petition for a property

   # CREATE REVISION PETITION SCREEN #

  @Sanity @PropertyTax
  Scenario Outline: Register user choose to do revision petition of property

    Given juniorAssistant logs in
    And user will select the required screen as "Create new property" with condition as "ptis"
    And he enters property header details as <propertyHeaderDetails>
    And he enters owner details for the first owner as <ownerDetails>
    And he enters property address details as <propertyAddressDetails>
    And he enters assessment details as <assessmentDetails>

    And he enters amenities as <amenitiesDetails>
    And he enters construction type details as <constructionTypeDetails>
    And he enters floor details as <floorDetails>
    And he click on floors Details entered
    And he enters document type details as <documentDetails>
    And he forwards for approval to billCollector
    And he will copy the acknowledgement message with assessment number createProperty-create
    And user will be notified by "Successfully"
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
    And he approved the property with remarks "property approved"
    And he will copy the acknowledgement message with assessment number createProperty-forward
    Then user will be notified by "Successfully"

    And he chooses to act upon above assessment number
    And he does a digital signature
    When commissioner closes acknowledgement
    And current user logs out

    And juniorAssistant logs in
    And he chooses to act upon above assessment number
    And he generates a notice

    And user will select the required screen as "Create Revision Petition"
    And he search for assessment from commissioner screen
    And he choose revision petition header
    And he enters revision petition details<revisionPetitionDetails>
    And he forwards for approval to commissioner
    And current user closes acknowledgement
    And current user logs out

    When commissioner logs in
    And he chooses to act upon above assessment number
    And he choose revision petition header
    And he enters hearing details<hearingDetails>
    And current user closes acknowledgement
    And current user logs out

    And juniorAssistant logs in
    And he chooses to act upon above assessment number
    And he choose revision petition header
    And he enters approver remark
    And he forwards for approval to revenueInspector
    And current user closes acknowledgement
    And current user logs out

    When revenueInspector logs in
    And he chooses to act upon above assessment number
    And he enters reason for modification
    And he choose revision petition header
    And he enters inspection details

    And he forwards for approval to revenueOfficer
    And current user closes acknowledgement
    And current user logs out

    When revenueOfficer logs in
    And he chooses to act upon above assessment number
    And he choose revision petition header
    And he enters approver remark
    And he forwards for approval to commissioner
    And current user closes acknowledgement
    And current user logs out

    When commissioner logs in
    And he chooses to act upon above assessment number
    And he choose to approve for revision petition
    And current user closes acknowledgement

    And he chooses to act upon above assessment number
    And he prints endoresement notice

    And he chooses to act upon above assessment number
    And he does a digital signature

    When commissioner closes acknowledgement
    And current user logs out

    And juniorAssistant logs in
    And he chooses to act upon above assessment number
    And he generates a print special notice
    And current user logs out

    Examples:
      | propertyHeaderDetails | ownerDetails | propertyAddressDetails | assessmentDetails     | amenitiesDetails | constructionTypeDetails | floorDetails | revisionPetitionDetails | hearingDetails | documentDetails |
      | residentialPrivate    | bimal        | addressOne             | assessmentNewProperty | all              | defaultConstructionType | firstFloor   | revisionpetitionBlock   | hearingBlock   | documentSelect  |


    # GENERAL REVISION PETITION SCREEN #

  @Sanity @PropertyTax
  Scenario Outline: Register user choose to do general revision petition of property

    Given commissioner logs in
    And user will select the required screen as "Data entry screen" with condition as "ptis"
    And he creates a new assessment for a private residential property
    Then dataEntry Details saved successfully
    And he choose to add edit DCB
    And he choose to close the dataentry acknowledgement screen
    And current user logs out

    Given juniorAssistant logs in
    And user will select the required screen as "collect tax"
    And he searches for assessment with number
    And he chooses to pay tax
    And he pay tax using Cash

    And user will select the required screen as "General Revision Petition"
    And he searches for assessment with number

    And he choose revision petition header
    And he enters revision petition details<revisionPetitionDetails>
    And he forwards for approval to commissioner
    And current user closes acknowledgement
    And current user logs out

    When commissioner logs in
    And he chooses to act upon above assessment number
    And he choose revision petition header
    And he enters hearing details<hearingDetails>
    And current user closes acknowledgement
    And current user logs out

    And juniorAssistant logs in
    And he chooses to act upon above assessment number
    And he choose revision petition header
    And he enters approver remark
    And he forwards for approval to revenueInspector
    And current user closes acknowledgement
    And current user logs out

    When revenueInspector logs in
    And he chooses to act upon above assessment number
    And he enters reason for modification
    And he choose revision petition header
    And he enters inspection details

    And he forwards for approval to revenueOfficer
    And current user closes acknowledgement
    And current user logs out

    When revenueOfficer logs in
    And he chooses to act upon above assessment number
    And he choose revision petition header
    And he enters approver remark
    And he forwards for approval to commissioner
    And current user closes acknowledgement
    And current user logs out

    When commissioner logs in
    And he chooses to act upon above assessment number
    And he choose to approve for revision petition
    And current user closes acknowledgement

    And he chooses to act upon above assessment number
    And he prints endoresement notice

    And he chooses to act upon above assessment number
    And he does a digital signature

    When commissioner closes acknowledgement
    And current user logs out

    And juniorAssistant logs in
    And he chooses to act upon above assessment number
    And he generates a print special notice
    And current user logs out

    Examples:
      | revisionPetitionDetails | hearingDetails |
      | revisionpetitionBlock   | hearingBlock   |

