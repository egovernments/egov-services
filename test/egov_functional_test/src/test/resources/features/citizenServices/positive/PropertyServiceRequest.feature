Feature: Citizen request service for Property No Due Certificate and Extract Certificate

  Scenario: Service Request for Property No Due
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 9483619659
    And user on Login screen types on password value eGov@123
    And user on Login screen clicks on signIn

    And user on Dashboard screen verifies myServiceRequests has visible value My Service Requests
    And user on Dashboard screen force clicks on property
    And user on Dashboard screen force clicks on payMyDues
    And user on PropertyServiceRequest screen verifies payMyDuesforPropertyTax has visible value Pay My Dues for Property Tax
    And user on PropertyServiceRequest screen types on assessmentNumber value roh000008235
#    And user on PropertyServiceRequest screen clicks on search