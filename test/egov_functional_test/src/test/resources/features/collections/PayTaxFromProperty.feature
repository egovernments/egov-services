Feature: In this feature we are going to test the following

  # Collecting Tax By Creating Legacy Data Entry Screen along with Demand

  Scenario Outline: Collecting Tax By Creating Legacy Data Entry Screen along with Demand

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 40004
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen verifies myTasks has visible value My Tasks
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create Data Entry
    And user on Home screen clicks on firstMenuItem

    ### On Create Data Entry Screen entering owner details ###
    And user on PTISDataEntry screen will see the dataEntryText
    And user on PTISDataEntry screen types on oldPropertyId value <Old Property No>
    And user on PTISDataEntry screen types on aadhaarNumber value <Aadhaar Number>
    And user on PTISDataEntry screen types on phoneNumber value <Phone Number>
    And user on PTISDataEntry screen types on ownerName value <Owner Name>
    And user on PTISDataEntry screen selects on gender value <Gender>
    And user on PTISDataEntry screen types on email value <Email>
    And user on PTISDataEntry screen types on panNumber value <Pan>
    And user on PTISDataEntry screen selects on guardianRelation value <Guardian Relation>
    And user on PTISDataEntry screen types on guardian value <Guardian>
    And user on PTISDataEntry screen types on percentageOfOwnerShip value <Percentage Of OwnerShip>
#    And user on PTISDataEntry screen clicks on primaryOwner
    And user on PTISDataEntry screen clicks on text value Add

    ### On Create Data Entry Screen entering property address details ###
    And user on PTISDataEntry screen types on referencePropertyNumber value <Reference Property Number>
    And user on PTISDataEntry screen types on doorNo value <Door No>
    And user on PTISDataEntry screen selects on locality value <Locality>
    And user on PTISDataEntry screen selects on electionWard value <Election Ward>
    And user on PTISDataEntry screen selects on zoneNo value <Zone No>
    And user on PTISDataEntry screen selects on wardNo value <Ward No>
    And user on PTISDataEntry screen selects on blockNo value <Block No>
    And user on PTISDataEntry screen selects on street value <Street>
    And user on PTISDataEntry screen selects on revenueCircle value <Revenue Circle>
    And user on PTISDataEntry screen types on pincode value <Pin>
    And user on PTISDataEntry screen types on noOfFloors value <Total Floors>
    And user on PTISDataEntry screen types on plotNumber value 3 digit random number
    And user on PTISDataEntry screen types on ctsSurveyNumber value 4 digit random number
    And user on PTISDataEntry screen types on landmark value "LandMark ", 4 random characters

    ### On Create Data Entry Screen entering Assessment details ###
    And user on PTISDataEntry screen selects on reasonForCreation value <Reason for Creation>
    And user on PTISDataEntry screen selects on propertyType value <Property Type>
    And user on PTISDataEntry screen selects on propertyTypeSubType value <Property Sub Type>
    And user on PTISDataEntry screen selects on usageType value <Usage Type>
    And user on PTISDataEntry screen selects on assessmentUsageSubType value <Usage Sub Type>
    And user on PTISDataEntry screen types on totalArea value <Total Area>
    And user on PTISDataEntry screen types on sequenceNo value <Sequence No>
    And user on PTISDataEntry screen types on buildingPermissionNumber value <Building permission Number>
    And user on PTISDataEntry screen types on buildingPermissionDate value <Building Permission Date>

    ### On Create Data Entry Screen entering Property Factors ###
    And user on PTISDataEntry screen selects on toiletFactor value <Toilet Factor>
    And user on PTISDataEntry screen selects on roadFactor value <Road Factor>
    And user on PTISDataEntry screen selects on liftFactor value <Lift Factor>
    And user on PTISDataEntry screen selects on parkingFactor value <Parking Factor>

    ### On Create Data Entry Screen entering Floor Details ###
    And user on PTISDataEntry screen selects on floorNo value <Floor No>
    And user on PTISDataEntry screen selects on unitType value <Unit Type>
    And user on PTISDataEntry screen types on unitNo value <Unit No>
    And user on PTISDataEntry screen selects on constructionClass value <Construction Class>
    And user on PTISDataEntry screen selects on floorUsageType value <Floor Usage Type>
    And user on PTISDataEntry screen selects on floorUsageSubType value <Floor Usage Sub Type>
    And user on PTISDataEntry screen types on firmName value <Firm Name>
    And user on PTISDataEntry screen selects on occupancy value <Occupancy>
    And user on PTISDataEntry screen types on occupantName value <Occupancy Name>
    And user on PTISDataEntry screen types on annualRent value <Annual Rent>
    And user on PTISDataEntry screen types on annualRetableValue value <Annual Retable Value>
    And user on PTISDataEntry screen types on retableValue value <Retable Value>
    And user on PTISDataEntry screen types on constructionStartDate value <Construction Start Date>
    And user on PTISDataEntry screen types on constructionEndDate value <Construction end Date>
    And user on PTISDataEntry screen types on effectiveFromDate value <Effective from Date>
    And user on PTISDataEntry screen selects on unStructuredLand value <Unstructured Land>
    And user on PTISDataEntry screen types on length value <length>
    And user on PTISDataEntry screen types on breadth value <Breadth>
    And user on PTISDataEntry screen types on buildUpArea value <BuildUp Area>
    And user on PTISDataEntry screen types on carpetArea value <Carpet Area>
    And user on PTISDataEntry screen types on exemptedArea value <Exempted area>
    And user on PTISDataEntry screen types on occupancyCertificateNumber value <Occupany Certificate Number>
    And user on PTISDataEntry screen types on buildingCost value <Building cost>
    And user on PTISDataEntry screen types on landCost value <Land Cost>
    And user on PTISDataEntry screen clicks on text value Add Room

    ### On Create Data Entry Screen entering Construction Details ###
    And user on PTISDataEntry screen types on currentAssessmentDate value <Current Assessment date>
    And user on PTISDataEntry screen types on firstAssessmentDate value <First Assessment Date>
    And user on PTISDataEntry screen types on revisedAssessmentDate value <Revised Assessment Date>
    And user on PTISDataEntry screen types on lastAssessmentDate value <Last assessment date>
    And user on PTISDataEntry screen types on commencementDate value <Commencement date>
    And user on PTISDataEntry screen types on certificateNumber value <Cerficate number>
    And user on PTISDataEntry screen types on certificateCompletionDate value <Cerficate Complition Date>
    And user on PTISDataEntry screen types on certificateReceivedDate value <Cerficate Received Date>
    And user on PTISDataEntry screen types on agencyName value <Agency name>
    And user on PTISDataEntry screen types on licenseType value <License Type>
    And user on PTISDataEntry screen types on licenseNumber value <License Number>
    And user on PTISDataEntry screen clicks on text value Create
    And user on PTISDataEntry screen will wait until the page loads
    And user on PTISDataEntry screen verifies text has visible value Acknowledgement
    And user on PTISDataEntry screen copies the propertyTaxNumber to ptNumber

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Search Property
    And user on Home screen clicks on firstMenuItem

    ### On PropertyTax Search Screen ###
    And user on PTISSearchProperty screen types on assessmentNumber value ptNumber
    And user on PTISSearchProperty screen clicks on text value Search
    And user on PTISSearchProperty screen scroll to the action
    And user on PTISSearchProperty screen verifies assessmentNumberInSearchTable has visible value ptNumber
    And user on PTISSearchProperty screen clicks on action
    And user on PTISSearchProperty screen forceClicks on text value Add/Edit DCB

    ### On PropertyTax Demand Screen ###
    And user on Home screen will wait until the page loads
    And user on PTISDemand screen enter all demands
    And user on PTISDemand screen clicks on text value Update
    And user on Home screen will wait until the page loads

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Pay Taxes
    And user on Home screen clicks on firstMenuItem

    ### On PayTaxes Screen ###
    And user on PayTaxes screen selects on billingServiceName value Property Tax
    And user on PayTaxes screen types on consumerCode value ptNumber
    And user on PayTaxes screen clicks on text value Search
    And user on PayTaxes screen verifies text has visible value ptNumber
    And user on PayTaxes screen copies the amountToBePaid to taxAmount
    And user on PayTaxes screen types on amount value 10
    And user on PayTaxes screen clicks on text value Pay

    ### On Receipt View Screen ###
    And user on PayTaxes screen will wait until the page loads
    And user on PayTaxes screen verifies text has visible value Receipt Number
    And user on PayTaxes screen verifies text has visible value ptNumber
    And user on PayTaxes screen copies the receiptNumber to receiptNumber
    And user on PayTaxes screen verifies amountPaid has visible value 10

#    ### Logout ###
#    And Intent:LogoutIntentTest

    Examples:
      | Old Property No   | Aadhaar Number         | Phone Number      | Owner Name                   | Gender | Email        | Pan        | Guardian Relation | Guardian | Percentage Of OwnerShip | Reference Property Number | Apartment Name | Door No | Locality  | Election Ward | Zone No | Ward No   | Block No | Street | Revenue Circle | Pin    | Total Floors | Reason for Creation | Property Type | Property Sub Type | Usage Type  | Usage Sub Type | Total Area | Sequence No | Building permission Number | Building Permission Date | Toilet Factor | Road Factor | Lift Factor | Parking Factor | Floor No     | Unit Type | Unit No | Construction Class | Floor Usage Type | Floor Usage Sub Type | Firm Name | Occupancy | Occupancy Name | Annual Rent | Annual Retable Value | Retable Value | Construction Start Date | Construction end Date | Effective from Date | Unstructured Land | length | Breadth | BuildUp Area | Carpet Area | Exempted area | Occupany Certificate Number | Building cost | Land Cost | Current Assessment date | First Assessment Date | Revised Assessment Date | Last assessment date | Commencement date | Cerficate number | Cerficate Complition Date | Cerficate Received Date | Agency name | License Type | License Number |
      | 10 random numbers | "20",10 random numbers | 10 random numbers | "Owner ",4 random characters | Male   | random email | BGDHT4703E | Father            | P Sunil  | 100                     | 101211111111112           | NA             | 10001   | AndharAli | Prabhag 1     | Zone A  | Prabhag 2 | NA       | NA     | NA             | 560037 | 2            | New Property        | Building      | Shop              | Residential | Slum           | 5000       | 1           | 1234                       | 01/08/2017               | 1             | 2           | 3           | 4              | Ground Floor | Room      | 505     | RCC Load bearing   | Non Residential  | Shop                 | Firm      | Owner     | Sunil          | NA          | 2000                 | 200           | 02/05/2012              | 03/05/2012            | 04/05/2012          | Yes               | NA     | NA      | 3000         | 500         | 200           | 10                          | 200000        | 50000     | 05/08/2017              | 06/08/2017            | 07/08/2017              | 08/08/2017           | 09/08/2017        | 111              | 10/08/2017                | 11/08/2017              | Imran       | A7           | 4634           |