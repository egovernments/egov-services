Feature: Amalgamation Of Property

  As a regsistered user of system
  I should be able to create a amalgamation of property

  # AMALGAMATION OF PROPERTY #

  @Sanity @PropertyTax
  Scenario: Registered user choose to do amalgamation of property

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
  And user will select the required screen as "Amalgamation of Property"
  And he searches for assessment with number
  And he search for the Amalgamated Properties
  And he forwards for approval to billCollector
  And current user closes tax exemption acknowledgement
  And current user logs out

  When billCollector logs in
  And he chooses to act upon above assessment number
  And he forwards for approval to revenueInspector
  And current user closes tax exemption acknowledgement
  And current user logs out

  When revenueInspector logs in
  And he chooses to act upon above assessment number
  And he forwards for approval to revenueOfficer
  And current user closes tax exemption acknowledgement
  And current user logs out

  When revenueOfficer logs in
  And he chooses to act upon above assessment number
  And he forwards for approval to commissioner
  And current user closes tax exemption acknowledgement
  And current user logs out

  When commissioner logs in
  And he chooses to act upon above assessment number
  And he approved the property with remarks "amalgamation-approve"
  And current user closes tax exemption acknowledgement
  And he chooses to act upon above assessment number
  And he does a digital signature
  When commissioner closes acknowledgement
  And current user logs out

  And juniorAssistant logs in
  And he chooses to act upon above assessment number
  Then he generates a notice
  And current user logs out

