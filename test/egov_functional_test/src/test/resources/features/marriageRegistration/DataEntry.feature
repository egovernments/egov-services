Feature: Create an Data Entry for the Marriage registration
  An commissioner can make data entry
  the Marriage registration in the system

  @Marriage @Sanity
  Scenario Outline: Data Entry for the Marriage registration
    When commissioner logs in
    And user will select the required screen as "Data Entry Screen" with condition as "mrs"
    And he enters the applicants details as <generalInformation>
    And he enters the bridegroom information as <bridegroomInformation> <brideInformation>
    And he enters the serial and page number
    And he enters the Witnesses Information
    And he enters the checklist
    And he submit the data entry
    Then user will be notified by "registered"
    And he closes the acknowledgement
    And current user logs out

    #Modification of Marriage Registration

    When commissioner logs in
    And user will select the required screen as "Modify Marriage Registration"
    And he search the marrige application
    And he modify application and update it
    Then user will be notified by "updated"
    And he closes the acknowledgements
    And current user logs out

        #Re-Issue of Certificate#
    When juniorAssistant logs in
    And user will select the required screen as "Re Issue Marriage Certifiate"
    And he search applications for re issue certificate
    And he selects the application for re issue certificate
    And he enters the memorandum of marriage
    And he forward to commissioner
    And he get application number and closes acknowledgement

    And user will select the required screen as "Collect Fee"
    And he search for above application number to collect marriage Registration fee for reissue
    And he collect the charges and closes the acknowledgement
    And current user logs out

    When commissioner logs in
    And he chooses to act upon above application number
    And he re issue the marriage application  and close the acknowledgement
    And current user logs out

    Examples:
      | generalInformation | bridegroomInformation | brideInformation |
      | generalInfo        | bridegroomInfo        | brideInfo        |