Feature: Vacancy Remission Of Property

  As a registered user of the system
  I should able to create vacancy remission of property

  # VACANCY REMISSION OF PROPERTY SCREEN #

  @Sanity @PropertyTax
  Scenario: Registered user create property through data entry screen and do Vacancy Remission

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

  And user will select the required screen as "Vacancy Remission"
  And he searches for assessment with number
  And he enters the Vacancy Remission Details
  And he forwarding for approval to commissioner1
  And he will copy the acknowledgement message with application number vacancyRemissionAckForm
  And current user logs out

  When commissioner logs in
  And he chooses to act upon above assessment number
  And he forward application to the junior assistant and closes acknowledgement
  And current user logs out

  When juniorAssistant logs in
  And he chooses to act upon above assessment number
  And he forwarding for approval to bill_Collector
  And current user closes tax exemption acknowledgement
  And current user logs out

  When bill_Collector logs in
  And he chooses to act upon above application number
  And he forwarding for approval to revenue_Inspector
  And current user closes tax exemption acknowledgement
  And current user logs out

  When revenue_Inspector logs in
  And he chooses to act upon above application number
  And he approved the property with remarks "vacancy remission approved"
  And current user closes tax exemption acknowledgement
  Then user will be notified by "Successfully"
  And current user logs out
