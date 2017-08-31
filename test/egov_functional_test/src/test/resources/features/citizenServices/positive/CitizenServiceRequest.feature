Feature: Create Citizen Service Request

  ##Positive Scenario for Citizen Registration##
  Scenario: Citizen raising Marriage Certificate service request with valid data from citizen login
    Given user on login screen types on username value 9876543210
    And user on login screen types on password value eGov@123
    And user on login screen clicks on signIn
    And user on dashboard screen clicks on services
    And user on dashboard screen clicks on MarriageCertificateLink
    And user on marriageCertificate screen types on BrideAddress value Bangalore
    And user on marriageCertificate screen types on BrideAge value 25
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

  ##Service Request from the ULB/CSC/CFC operator##
  Scenario: Citizen raising service request with valid data by submitting document to ULB/CSC/CFC
    Given user on login screen types on username value narasappa
    And user on login screen types on password value demo
    And user on login screen clicks on signIn
    And user on home screen clicks on menu
    And user on home screen types on applicationSearchBox value Service request
    And user on serviceRequest screen types on UDIDNo value 222222222222
    And user on serviceRequest screen types on Name value Girl
    And user on serviceRequest screen types on Address value Bangalore
    And user on serviceRequest screen types on emailID value abc@xyz.com
    And user on serviceRequest screen clicks on locality
    And user on serviceRequest screen clicks on locality value abbas nagar
    And user on serviceRequest screen types on mobileNumber value 5555555555
    And user on serviceRequest screen clicks on service
    And user on serviceRequest screen clicks on service value Marriage Registration
    And user on marriageCertificate screen types on BrideAddress value Bangalore
    And user on marriageCertificate screen types on BrideAge value 25
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

  Scenario: Citizen raising service request with valid data by submitting document to ULB/CSC/CFC
    Given DataIntent:LoginIntentTest
    |username:9483619659|password:eGov@123|
    And user on login screen clicks on signIn
    And user on home screen clicks on menu
    And user on home screen types on applicationSearchBox value Service request
    And user on serviceRequest screen types on UDIDNo value 222222222222
    And user on serviceRequest screen types on Name value Girl
    And user on serviceRequest screen types on Address value Bangalore
    And user on serviceRequest screen types on emailID value abc@xyz.com
    And user on serviceRequest screen clicks on locality
    And user on serviceRequest screen clicks on locality value abbas nagar
    And user on serviceRequest screen types on mobileNumber value 5555555555
    And user on serviceRequest screen clicks on service
    And user on serviceRequest screen clicks on service value Marriage Registration
    And user on marriageCertificate screen types on BrideAddress value Bangalore
    And user on marriageCertificate screen types on BrideAge value 25
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