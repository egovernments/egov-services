Feature: Create/renewal/closure of sewerage connection
  As a registered user of the system
  User should be able to create/change/closure of sewerage connection

  Background:It will run the data entry screen of property tax

    Given commissioner logs in
    And user will select the required screen as "Data entry screen" with condition as "ptis"
    And he creates a new assessment for a private residential property
    Then dataEntry Details saved successfully
    And he choose to add edit DCB
    And he choose to close the dataentry acknowledgement screen
    And current user logs out

  @SewerageTax @Sanity
  Scenario Outline: create change closure of sewerage connection

    Given creator logs in
    And user will select the required screen as "Property Tax"
    And he chooses to collect tax for above assessment number
    And he chooses to pay tax
    And he collect tax using <paymentMode>
    And user closes the acknowledgement

    And user will select the required screen as "Apply for new connection" with condition as "stms"
    And he create new sewerage connection for above assessment number <creationDocuments>
    And he forward to assistant engineer and closes the acknowledgement
    Then user will be notified by "forwarded"

    And user will select the required screen as "Collect Sewerage Charges"
    And he search for above application number to collect
    And he collect the charges and closes the acknowledgement
    And current user logs out

    When assistantEngineer logs in
    And he chooses to act upon above application number
    And he forward to DEE and close the acknowledgement
    Then user will be notified by "forwarded"
    And current user logs out

    When deputyExecutiveEngineer logs in
    And he chooses to act upon above application number
    And he approve the above sewerage application
    And he closes the sewerage acknowledgement
    Then user will be notified by "successfully"
    And current user logs out

    When creator logs in
    And he chooses to act upon above application number
    And he generates estimation notice for above sewerage application
    And user will select the required screen as "Collect Sewerage Charges"
    And he search for above application number to collect
    And he collect the charges and closes the acknowledgement

    And he chooses to act upon above application number
    And he forward to executive engineer and closes the acknowledgement
    Then user will be notified by "forwarded"
    And current user logs out

    When Executive_engineer logs in
    And he chooses to act upon above application number
    And he approve the above sewerage application
    And he closes the sewerage acknowledgement
    Then user will be notified by "successfully"
    And current user logs out

    When creator logs in
    And he chooses to act upon above application number
    And he generate workOrder for above sewerage connection
    And he forward to assistant engineer and closes the acknowledgement
    Then user will be notified by "forwarded"
    And current user logs out

    When assistantEngineer logs in
    And he chooses to act upon above application number
    And he execute connection and closes the acknowledgement
    Then user will be notified by "completed"
    And current user logs out

     ########################################################
     #############  change of sewerage connection ###########
     #########################################################

    Given creator logs in
    And user will select the required screen as "search connection" with condition as "stms"
    And he search for above sewerage connection
    And he increses the number of closets <changeDocuments>
    And he forward to assistant engineer for change in closets and closes the acknowledgement
    Then user will be notified by "forwarded"

    And user will select the required screen as "Collect Sewerage Charges"
    And he search for above application number to collect
    And he collect the charges and closes the acknowledgement
    And current user logs out

    When assistantEngineer logs in
    And he chooses to act upon above application number
    And he forward to DEE for change and close the acknowledgement
    Then user will be notified by "forwarded"
    And current user logs out

    When deputyExecutiveEngineer logs in
    And he chooses to act upon above application number
    And he approve the above sewerage application
    And he closes the sewerageChange acknowledgement
    Then user will be notified by "successfully"
    And current user logs out

    When creator logs in
    And he chooses to act upon above application number
    And he generates estimation notice for above sewerage application

    And user will select the required screen as "Collect Sewerage Charges"
    And he search for above application number to collect
    And he collect the charges and closes the acknowledgement

    And he chooses to act upon above application number
    And he forward to executive engineer for change in closets and closes the acknowledgement
    Then user will be notified by "forwarded"
    And current user logs out

    When Executive_engineer logs in
    And he chooses to act upon above application number
    And he approve the above sewerage application
    And he closes the sewerageChange acknowledgement
    Then user will be notified by "successfully"
    And current user logs out

    When creator logs in
    And he chooses to act upon above application number
    And he generate workOrder for above sewerage connection
    And he forward to assistant engineer for change in closets and closes the acknowledgement
    Then user will be notified by "forwarded"
    And current user logs out

    When assistantEngineer logs in
    And he chooses to act upon above application number
    And he execute connection for change and closes the acknowledgement
    Then user will be notified by "completed"
    And current user logs out

     #########################################################
     #############  closure of sewerage connection ###########
     #########################################################

    Given creator logs in
    And user will select the required screen as "search connection" with condition as "stms"
    And he search for above sewerage application for closure
    And he put remarks and forward the application
    And he forwards for closure and closes the acknowledgement
    Then user will be notified by "forwarded"
    And current user logs out

    When assistantEngineer logs in
    And he chooses to act upon above application number
    And he forwards to DEE for closure and close the acknowledgement
    Then user will be notified by "forwarded"
    And current user logs out

    When deputyExecutiveEngineer logs in
    And he chooses to act upon above application number
    And he forwards to executive engineer for closure and close the acknowledgement
    Then user will be notified by "forwarded"
    And current user logs out

    When Executive_engineer logs in
    And he chooses to act upon above application number
    And he approve the above sewerage application
    And he closes the seweargeClosure acknowledgement
    Then user will be notified by "successfully"
    And current user logs out

    When creator logs in
    And he chooses to act upon above application number
    And he generates closure notice
    And current user logs out

    Examples:
      | paymentMode | creationDocuments | changeDocuments |
      | cash        | creation1         | change1         |
      | cash        | creation2         | change2         |
      | cash        | creation3         | change3         |

  @SewerageTax @Sanity
  Scenario: Generate demand bill for legacy sewerage connection

    Given commissioner logs in
    And user will select the required screen as "Data Entry Screen" with condition as "stms"
    And he enter details for legacy sewerage connection
    And he submit the application of legacy sewerage connection and closes the acknowledgement
    Then user will be notified by "successfully."
    And current user logs out

    When creator logs in
    And user will select the required screen as "search connection" with condition as "stms"
    And he search application and generate demand bill
    And current user logs out


