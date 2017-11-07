Feature: Abstract Estimate is prepared by the clerk to arrive at the approximate project cost.

  Narrative Description: As a Clerk, I want to prepare an Abstract Estimate, so that I:
  i. Create Abstract Estimate
  ii.Create Spill over Estimate (Data Entry for existing entities)
  Pre-Conditions:
  System should have list of departments
  System should have list of election ward
  System should have list of location
  System should have list of Work Category
  System should have list of Beneficiary
  System should have list of Nature of Work
  System should have list of type of Work
  System should have list of sub type of Work
  System should have list of mode of entrusted
  System should have list of Fund
  System should have list of Function
  System should have list of Budget head
  System should have list of Scheme
  System should have list of Sub Scheme

  Background:
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value clerk
    And user on Login screen types on password value eGov@123
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen verifies myTasks has visible value My Tasks
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Masters
    And user on Home screen clicks on firstMenuItem

  Scenario Outline: Create Abstract Estimate

    ###### Login details #########
    Given user on login screen verifies signText has visible value Sign In
    And user on login screen types on username value clerk
    And user on login screen types on password value eGov@123
    And user on login screen clicks on SignIn

    ###### Home Page details #####
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create Abstract Estimate
    And user on Home screen clicks on firstMenuItem

    ###### Header details ########
    ##############################
    And user on CreateAbstractEstimate screen verifies text has visible value Create Abstract Estimate
    And user on CreateAbstractEstimate screen types on dateOfProposal value "current date"
    And user on CreateAbstractEstimate screen selects on Department value Engineering
    And user on CreateAbstractEstimate screen types on Subject value <subject>
    And user on CreateAbstractEstimate screen selects on referenceType value <referenceType>
    And user on CreateAbstractEstimate screen types on letterReferenceNumber value --Random numbers
    And user on CreateAbstractEstimate screen types on Description value Description
    And user on CreateAbstractEstimate screen selects on natureOfWork value <natureOfWork>
    And user on CreateAbstractEstimate screen selects on typeOfWork value <typeOfWork>
    And user on CreateAbstractEstimate screen selects on workCategory value <workCategory>
    And user on CreateAbstractEstimate screen selects on subTypeOfWork value <subTypeOfWork>
    And user on CreateAbstractEstimate screen selects on subTypeOfWork value <recommendedModeOfEntrusted>
    And user on CreateAbstractEstimate screen selects on Beneficiary value <beneficiary>
    And user on CreateAbstractEstimate screen types on electionWard value <electionWard>
    And user on CreateAbstractEstimate screen types on NameOfExecutingDepartmentLocation

    And user on CreateAbstractEstimate screen types on AssestNumber
    And user on CreateAbstractEstimate screen verifies text has visible value AssetDetails

    #Financial details#
    And user on CreateAbstractEstimate screen selects on fund
    And user on CreateAbstractEstimate screen selects on function
    And user on CreateAbstractEstimate screen selects on function
    And user on CreateAbstractEstimate screen selects on budgetHead
    And user on CreateAbstractEstimate screen selects on scheme
    And user on CreateAbstractEstimate screen selects on subScheme

    #Work Details#
    And user on CreateAbstractEstimate screen selects on Function
    And user on CreateAbstractEstimate screen selects on Scheme
    And user on CreateAbstractEstimate screen selects on SubScheme
    And user on CreateAbstractEstimate screen types on NameOfWork
    And user on CreateAbstractEstimate screen types on EstimatedAmount
    And user on CreateAbstractEstimate screen uploads on estimateDocuments

    #Approval Details#
    And user on CreateAbstractEstimate screen forward application to next level
    Then Estimate will be created in the system.
    And user on CreateAbstractEstimate copies the Work Identification number Generated
    And user on CreateAbstractEstimate verifies Proceeding letter will be generate from system
    And current user log out

    When HOD/ Jr. Engineer logs in
    And he will open the estimate from the user inbox based on Work Identification Number
    And he verifies details entered by clerk
    And forward to Chief officer
    And user logs out

    When Chief Officer logs in
    And he will open the estimate from the user inbox based on Work Identification Number
    And user on CreateAbstractEstimate screen types on AdministrativeSanctionNum
    And user on CreateAbstractEstimate screen types on AdministrativeSanctionDate
    And user on CreateAbstractEstimate screen types on CouncilSanctionNum
    And user on CreateAbstractEstimate screen types on CouncilSanctionDate
    And user on CreateAbstractEstimate screen clicks on approve
    And user logs out

    Examples:
      | subject       | referenceType  | natureOfWork  | typeOfWork   | workCategory | subTypeOfWork    | recommendedModeOfEntrusted | beneficiary | electionWard  |
      | Line Estimate | Reference Type | Nature of Wor | Type of Work | Work Cat     | Sub Type of Work | Nomination                 | Beneficiary | Election Ward |

  Scenario: Spill over Estimate (Data Entry for existing works)
    ##############################
    ###### Login details #########
    ##############################
    Given user on login screen verifies signText has visible value Sign In
    And user on login screen types on username value juniorEngineer
    And user on login screen types on password value eGov@123
    And user on login screen clicks on SignIn
    ##############################
    ###### Home Page details #####
    ##############################
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create Spill Over Abstract Estimate
    And user on Home screen clicks on firstMenuItem
    #######################################
    ###### Spillover Estimate details #####
    #######################################
    And user on CreateSpilloverEstimate screen verifies text has visible value Create Spill Over Estimate
    And user on CreateSpilloverEstimate screen types on DateOfProposal
    And user on CreateSpilloverEstimate screen selects on Department
    And user on CreateSpilloverEstimate screen types on Subject
    And user on CreateSpilloverEstimate screen types on RequirementNumber
    And user on CreateSpilloverEstimate screen types on Description
    And user on CreateSpilloverEstimate screen types on ElectionWard
    And user on CreateSpilloverEstimate screen types on Location
    And user on CreateSpilloverEstimate screen selects on WorkCategory
    And user on CreateSpilloverEstimate screen selects on Beneficiary
    And user on CreateSpilloverEstimate screen selects on NatureOfWork
    And user on CreateSpilloverEstimate screen selects on TypeOfWork
    And user on CreateSpilloverEstimate screen selects on SubTypeOfWork
    And user on CreateSpilloverEstimate screen selects on RecommendedModeOfEntrusted
    And user on CreateSpilloverEstimate screen selects on Fund
    And user on CreateSpilloverEstimate screen selects on Function
    And user on CreateSpilloverEstimate screen selects on Scheme
    And user on CreateSpilloverEstimate screen selects on SubScheme
    And user on CreateSpilloverEstimate screen types on NameOfWork
    And user on CreateSpilloverEstimate screen types on EstimatedAmount
    And user on CreateSpilloverEstimate screen types on AdministrativeSanctionNum
    And user on CreateSpilloverEstimate screen types on AdministrativeSanctionDate
    And user on CreateSpilloverEstimate screen types on CouncilSanctionNum
    And user on CreateSpilloverEstimate screen types on CouncilSanctionDate
    And user on CreateSpilloverEstimate screen types on CurrentStatus
    And user on CreateSpilloverEstimate screen types on AbstractEstimateNumber
    And user on CreateSpilloverEstimate screen types on EstimatedAmount
    And user on CreateSpilloverEstimate screen types on WorkIdentificationNumber
    And user on CreateSpilloverEstimate screen types on GrossAmountBilledSoFar
    And user on CreateSpilloverEstimate screen uploads on UploadFile
    And user on CreateSpilloverEstimate screen clicks on Submit
    And user logs out.