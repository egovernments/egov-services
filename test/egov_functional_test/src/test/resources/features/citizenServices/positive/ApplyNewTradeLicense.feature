Feature: Applying New Trade License from Citizen Login

  Scenario: Apply New Trade License

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 9483619659
    And user on Login screen types on password value demo
    And user on Login screen clicks on signIn

    ### On Dashboard Screen ###
    And user on Dashboard screen will wait until the page loads
    And user on Dashboard screen verifies myServiceRequests has visible value My Service Requests
    And user on Dashboard screen clicks on text value Trade License
    And user on Dashboard screen clicks on text value Apply for New License

    ###On Apply New Trade License Screen
    And user on ApplyingforNewTradeLicense screen types on aadharNumber value 12 random numbers
    And user on ApplyingforNewTradeLicense screen types on fatherOrspouseName value "Father Name", 4 random characters
    And user on ApplyingforNewTradeLicense screen types on tradeOwnerAddress value "Roha, Maharastra"
    And user on ApplyingforNewTradeLicense screen selects on locality value Roha Bajar Peth
    And user on ApplyingforNewTradeLicense screen selects on adminWard value Prabhag 1
    And user on ApplyingforNewTradeLicense screen selects on revenueWard value Prabhag 1
    And user on ApplyingforNewTradeLicense screen selects on ownershipType value STATE GOVERNMENT
    And user on ApplyingforNewTradeLicense screen types on tradeAddress value "Roha"
    And user on ApplyingforNewTradeLicense screen types on tradeTitle value "Trade Title"
    And user on ApplyingforNewTradeLicense screen selects on tradeType value PERMANENT
    And user on ApplyingforNewTradeLicense screen selects on tradeCategory value Non - Eating / Non-medical Establishments
    And user on ApplyingforNewTradeLicense screen selects on tradeSubCategory value Video Games
    And user on ApplyingforNewTradeLicense screen types on tradeCommencementDate value "12/12/2017"
    And user on ApplyingforNewTradeLicense screen scroll to the payButton
    And user on ApplyingforNewTradeLicense screen clicks on payButton

    And user on PaymentGateways screen clicks on text value Net Banking
    And user on PaymentGateways screen clicks on yesBankNetBanking
    And user on PaymentGateways screen clicks on proceedForPayment
    And user on PaymentGateways screen clicks on netBanking
    And user on PaymentGateways screen dropdown on selectBank value Atom Bank
    And user on PaymentGateways screen clicks on payNow
    And user on PaymentGateways screen clicks on clickToTransferFunds
    And user on PaymentGateways screen accepts the popup

    ### On View Receipt Screen ###
    And user on ApplyingforNewTradeLicense screen will wait until the page loads
    And user on ApplyingforNewTradeLicense screen copies the reqNumber to SRNReqNumber
    And user on ApplyingforNewTradeLicense screen clicks on homeButton
    And user on ApplyingforNewTradeLicense screen will wait until the page loads

    ### On Dashboard Screen ###
    And user on Home screen types on dashBoardSearch value SRNReqNumber
    And user on Home screen verifies text has visible value SRNReqNumber
    And user on Home screen clicks on text value SRNReqNumber

    ### Logout ###
    And Intent:LogoutIntentTest