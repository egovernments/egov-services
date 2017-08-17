Feature: Negative scenarios for Service Request for Citizen

  Scenario: Field Level Validations for Marriage Registration
    Given user on login screen types on username value 9876543210
    And user on login screen types on password value eGov@123
    And user on login screen clicks on signIn
    And user on home screen clicks on services
    And user on home screen clicks on MarriageCertificateLink
    And user on marriageCertificate screen types on BrideAddress value Bangalore
    And user on marriageCertificate screen types on BrideAge value abc
    And user on marriageCertificate screen verifies validationMSGForBrideAge has visible value Invalid field data
    And user on marriageCertificate screen types on BrideName value Girl
    And user on marriageCertificate screen types on BrideReligion value Hindu
    And user on marriageCertificate screen types on GroomAddress value Bangalore
    And user on marriageCertificate screen types on GroomAge value 29
    And user on marriageCertificate screen types on GroomName value Boy
    And user on marriageCertificate screen types on GroomReligion value Hindu
    And user on marriageCertificate screen types on Witness1Address value Bangalore
    And user on marriageCertificate screen types on Witness2Address value Bangalore
    And user on marriageCertificate screen types on Witness1Age value 25
    And user on marriageCertificate screen types on Witness2Age value 25
    And user on marriageCertificate screen types on Witness1Name value Anonymous
    And user on marriageCertificate screen types on Witness2Name value Anonymous
    And user on marriageCertificate screen types on ProcessingFee value 200
    And user on marriageCertificate screen clicks on submit