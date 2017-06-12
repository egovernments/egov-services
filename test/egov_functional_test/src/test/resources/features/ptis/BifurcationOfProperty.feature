Feature: Bifurcation of Property

  As a registered user of the system
  I should able to make bifurcation of property


  Background:It will run the data entry screen of property tax

    Given commissioner logs in
    And user will select the required screen as "Data entry screen" with condition as "ptis"
    And he creates a new assessment for a private residential property
    Then dataEntry Details saved successfully
    And he choose to add edit DCB
    And he choose to close the dataentry acknowledgement screen
    And current user logs out


  @Sanity @PropertyTax
  Scenario Outline: Register user choose to do bifurcation of property

    Given creator logs in
    And user will select the required screen as "Property Tax"
    And he chooses to collect tax for above assessment number
    And he chooses to pay tax
    And he collect tax using <paymentMode>
    And user closes the acknowledgement

    And user will select the required screen as "Create new property" with condition as "ptis"
    And he enters property header details as <propertyHeaderDetails>
    And he enters owner details for the first owner as <ownerDetails>
    And he enters property address details as <propertyAddressDetails>
    And he enters bifurcation assessment details as <bifurcationDetails>

    And he enters amenities as <amenitiesDetails>
    And he enters construction type details as <constructionTypeDetails>
    And he enters floor details as <floorDetails>
    And he click on floors Details entered
    And he enters document type details as <documentDetails>
    And he forwards for approval to billCollector
    And he will copy the acknowledgement message with assessment number createProperty-create
    And user will be notified by "Successfully"

    And user will select the required screen as "Bifurcation of Assessment"
    And he enters parent bifurcated assessment number
    And he forwards for approval to billCollector
    And he will copy the acknowledgement message with assessment number modifyProperty-forward
    Then user will be notified by "forwarded"
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
    | paymentMode| propertyHeaderDetails | ownerDetails | propertyAddressDetails | bifurcationDetails  | amenitiesDetails | constructionTypeDetails | floorDetails | documentDetails |
    |cash        | residentialPrivate    | bimal        | addressOne             | bifurcationProperty | all              | defaultConstructionType | firstFloor   | documentSelect  |

