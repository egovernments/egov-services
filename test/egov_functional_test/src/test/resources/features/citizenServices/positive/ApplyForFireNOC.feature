Feature: Citizen can apply for Fire NOC from Building Planning

  Scenario: Apply for Fire NOC from Citizen login

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 7975179334
    And user on Login screen types on password value demo
    And user on Login screen clicks on signIn

    ### On Dashboard Screen ###
    And user on Dashboard screen will wait until the page loads
    And user on Dashboard screen verifies myServiceRequests has visible value My Service Requests
    And user on Dashboard screen clicks on text value NOC
    And user on Dashboard screen clicks on text value Apply for Fire NOC

    ### On Apply Fire NOC Screen: Application Details ###
    And user on ApplyFireNOC screen types on applicantName value --"Shivaji ", 2 random characters
    And user on ApplyFireNOC screen types on mobileNumber value --"9784",6 random numbers
    And user on ApplyFireNOC screen types on email value --email
    And user on ApplyFireNOC screen types on aadhaarNumber value --12 digit number
    And user on ApplyFireNOC screen types on address value Roha, MH
    And user on ApplyFireNOC screen selects on zone value Zone A
    And user on ApplyFireNOC screen selects on ward value Prabhag 1

#    ### On Apply Fire NOC Screen: Service Details ###
#    And user on ApplyFireNOC screen types on provisionalFireNOCNumber value --"PNOC", 3 random numbers
#    And user on ApplyFireNOC screen types on plotNumber value --"P", 3 random numbers
#    And user on ApplyFireNOC screen types on nocSubCategory value Residential
#    And user on ApplyFireNOC screen types on vikasPrastavNumber value --"VP", 3 random numbers
#    And user on ApplyFireNOC screen types on fileNumber value --3 random numbers
#    And user on ApplyFireNOC screen types on landOwnerName value --"Shivaji ", 2 random characters
#    And user on ApplyFireNOC screen types on developerName value Mantri
#    And user on ApplyFireNOC screen types on plotSurveyNumber value --"PS", 3 random numbers
#    And user on ApplyFireNOC screen types on plotTalukaName value "Roha"
#    And user on ApplyFireNOC screen types on architectName value Total Envi
#    And user on ApplyFireNOC screen types on architectLicenseNumber value --"A", 3 random numbers
#    And user on ApplyFireNOC screen selects on locality value Prabhag 1
#    And user on ApplyFireNOC screen types on buildingName value --"Building Name:", 3 random charatcters
#    And user on ApplyFireNOC screen uploads on ApplicationOnBuilderLetterHead value legalCaseTestData.xlsx