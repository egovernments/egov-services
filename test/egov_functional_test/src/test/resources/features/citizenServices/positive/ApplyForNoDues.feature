Feature: As a registered user will apply for the No due Certificate

  Scenario: Apply for No Due Certificate

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 9160977087
    And user on Login screen types on password value eGov@123
    And user on Login screen clicks on signIn

    ### On Dashboard Screen ###
    And user on Dashboard screen will wait until the page loads
    And user on Dashboard screen verifies myServiceRequests has visible value My Service Requests
    And user on Dashboard screen clicks on text value Property
    And user on Dashboard screen clicks on applyNoDuesForProperty

    ### On PropertyTax Request Screen ###
    And user on PropertyServiceRequest screen types on assessmentNumber value roh000008280
    And user on PropertyServiceRequest screen clicks on search

    ### On No Dues Certificate for Property Tax ###
    And user on PropertyServiceRequest screen will wait until the page loads
    And user on PropertyServiceRequest screen clicks on payButton

    ### On Payment Screen ###
    And user on PropertyServiceRequest screen clicks on text value Wallet
    And user on PropertyServiceRequest screen clicks on selectPayment
    And user on PropertyServiceRequest screen clicks on proceedForPayment

    ### On Paytm Screen ###
    And user on Paytm screen types on cardNumber value 5457210001000019
    And user on Paytm screen dropdown on expMonth value 12
    And user on Paytm screen dropdown on expYear value 2025
    And user on Paytm screen types on cvv value 123
    And user on Paytm screen clicks on payNow
