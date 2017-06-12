Feature: Demolition of property

  As a registered user of the system
  I should able to create demolition of a property

   # DEMOLITION OF PROPERTY SCREEN #

   @Sanity @PropertyTax
   Scenario Outline: Register user choose to do demolition of property

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

   And user will select the required screen as "Demolition"
   And he searches for assessment with number
   And he enters demolition details as <demolition Details>

   And he forwarding for approval to bill_Collector
   And he will copy the acknowledgement message with application number propertyAckForm
   Then user will be notified by "successfully"
   And current user logs out

   When bill_Collector logs in
   And he chooses to act upon above application number
   And he forwarding for approval to revenue_Inspector
   And current user closes tax exemption acknowledgement
   And current user logs out

   When revenue_Inspector logs in
   And he chooses to act upon above application number
   And he forwarding for approval to revenue_Officer
   And current user closes tax exemption acknowledgement
   And current user logs out

   When revenue_Officer logs in
   And he chooses to act upon above application number
   And he forwarding for approval to commissioner1
   And current user closes tax exemption acknowledgement
   And current user logs out

   When commissioner logs in
   And he chooses to act upon above assessment number
   And he approved the property with remarks "property approved"
   And current user closes tax exemption acknowledgement
   #    Then user will be notified by "Successfully"
   And he chooses to act upon above assessment number
   And he does a digital signature
   When commissioner closes acknowledgement
   And current user logs out

   And juniorAssistant logs in
   And he chooses to act upon above assessment number
   And he generates a notice
   And current user logs out

   Examples:
   | demolition Details |
   | demolitionBlock    |