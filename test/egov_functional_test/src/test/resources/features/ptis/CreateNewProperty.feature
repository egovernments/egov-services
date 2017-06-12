Feature: Create New Property

  As a register user of the system
  I want to be able to create a new property
  So that the property records are up to date.

  # CREATE NEW PROPERTY SCREEN #

  @Sanity @PropertyTax
  Scenario Outline: Registered user creating a new property in the system

    Given juniorAssistant logs in
    And user will select the required screen as "Create New Property"
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
    Then user will be notified by "Successfully"
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
    Then create property details get saved successfully by generating assesssment number
    And he will copy the acknowledgement message with assessment number createProperty-forward
    Then user will be notified by "Successfully"

    And he chooses to act upon above assessment number
    And he does a digital signature
    When commissioner closes acknowledgement
    And current user logs out

    And juniorAssistant logs in
    And he chooses to act upon above assessment number
    And he generates a notice
    And current user logs out

    Examples:
      | propertyHeaderDetails | ownerDetails | propertyAddressDetails | assessmentDetails     | amenitiesDetails | constructionTypeDetails | floorDetails | documentDetails |
      | residentialPrivate    | bimal        | addressOne             | assessmentNewProperty | all              | defaultConstructionType | firstFloor   | documentSelect  |

   # DATA ENTRY SCREEN #

  @Sanity @PropertyTax
  Scenario: Registered user create property through data entry screen

    Given commissioner logs in
    And user will select the required screen as "Data entry screen" with condition as "ptis"
    And he creates a new assessment for a private residential property
    Then dataEntry Details saved successfully
    And he choose to add edit DCB
    And he choose to close the dataentry acknowledgement screen
    And current user logs out


  #Check all the field validation in create new property screen

  Scenario Outline:Checking the validations for Create new property

    Given juniorAssistant logs in
    And user will select the required screen as "Create New Property"
    And he enters property header details as <propertyHeaderDetails>
    And he enters floor details as <floorDetails>
    And he enters the floor details checkbox
    And he forwards for approval to billCollector

    And he checks the validations for all textBoxes
    And he enters construction type details as <constructionTypeDetails>
    And he forwards for approval to billCollector

    And he check the errorMessage of door number
    And he enters document type details as <documentDetails>
    And he forwards for approval to billCollector

    And he will copy the acknowledgement message with assessment number createProperty-create
    Then user will be notified by "Successfully"
    And current user logs out

    When billCollector logs in
    And he chooses to act upon above assessment number
    And he rejects the application
    And he will copy the acknowledgement of above rejected createProperty-forward
    Then user will be notified by "Rejected"
    And current user logs out

    When juniorAssistant logs in
    And he chooses to act upon above assessment number
    And he forwards for approval to billCollector
    And he will copy the acknowledgement message with assessment number createProperty-forward
    Then user will be notified by "Succesfully"
    And current user logs out

    When billCollector logs in
    And he chooses to act upon above assessment number
    And he forwards for approval to revenueInspector
    And he will copy the acknowledgement message with assessment number createProperty-forward
    Then user will be notified by "Succesfully"
    And current user logs out

    When revenueInspector logs in
    And he chooses to act upon above assessment number
    And he rejects the application
    And he will copy the acknowledgement of above rejected createProperty-forward
    Then user will be notified by "Rejected"
    And current user logs out

    When juniorAssistant logs in
    And he chooses to act upon above assessment number
    And he forwards for approval to billCollector
    And he will copy the acknowledgement message with assessment number createProperty-forward
    Then user will be notified by "Succesfully"
    And current user logs out

    When billCollector logs in
    And he chooses to act upon above assessment number
    And he forwards for approval to revenueInspector
    And he will copy the acknowledgement message with assessment number createProperty-forward
    Then user will be notified by "Succesfully"
    And current user logs out

    When revenueInspector logs in
    And he chooses to act upon above assessment number
    And he forwards for approval to revenueOfficer
    And he will copy the acknowledgement message with assessment number createProperty-forward
    Then user will be notified by "Succesfully"
    And current user logs out

    When revenueOfficer logs in
    And he chooses to act upon above assessment number
    And he rejects the application
    And he will copy the acknowledgement of above rejected createProperty-forward
    Then user will be notified by "Rejected"
    And current user logs out

    When revenueInspector logs in
    And he chooses to act upon above assessment number
    And he forwards for approval to revenueOfficer
    And he will copy the acknowledgement message with assessment number createProperty-forward
    Then user will be notified by "Succesfully"
    And current user logs out

    When revenueOfficer logs in
    And he chooses to act upon above assessment number
    And he forwards for approval to commissioner
    And current user closes acknowledgement
    And current user logs out

    When commissioner logs in
    And he chooses to act upon above assessment number
    And he rejects the application
    And he will copy the acknowledgement of above rejected createProperty-forward
    Then user will be notified by "Rejected"
    And current user logs out

    When revenueInspector logs in
    And he chooses to act upon above assessment number
    And he forwards for approval to revenueOfficer
    And he will copy the acknowledgement message with assessment number createProperty-forward
    Then user will be notified by "Succesfully"
    And current user logs out

    When revenueOfficer logs in
    And he chooses to act upon above assessment number
    And he forwards for approval to commissioner
    And current user closes acknowledgement
    And current user logs out

    When commissioner logs in
    And he chooses to act upon above assessment number
    And he approved the property with remarks "property approved"
    Then create property details get saved successfully by generating assesssment number
    And he will copy the acknowledgement message with assessment number createProperty-forward
    Then user will be notified by "Successfully"

    And he chooses to act upon above assessment number
    And he does a digital signature
    When commissioner closes acknowledgement
    And current user logs out

    And juniorAssistant logs in
    And he chooses to act upon above assessment number
    And he generates a notice
    And current user logs out

    Examples:
      | propertyHeaderDetails | floorDetails | constructionTypeDetails | documentDetails |
      | residentialPrivate    | firstFloor   | defaultConstructionType | documentSelect  |


