Feature: In this feature we are going to test Create New Property

  Scenario Outline: : Create New Property

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 40004
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen verifies myTasks has visible value My Tasks
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create New Property
    And user on Home screen clicks on firstMenuItem

    ### On Create Data Entry Screen entering owner details ###
    And user on CreateNewProperty screen will see the createNewPropertyText
    And user on CreateNewProperty screen types on aadhaarNumber value <Aadhaar Number>
    And user on CreateNewProperty screen types on phoneNumber value <Phone Number>
    And user on CreateNewProperty screen types on ownerName value <Owner Name>
    And user on CreateNewProperty screen selects on gender value <Gender>
    And user on CreateNewProperty screen types on email value <Email>
    And user on CreateNewProperty screen types on panNumber value <Pan>
    And user on CreateNewProperty screen selects on guardianRelation value <Guardian Relation>
    And user on CreateNewProperty screen types on guardian value <Guardian>
    And user on CreateNewProperty screen types on percentageOfOwnerShip value <Percentage Of OwnerShip>
    And user on CreateNewProperty screen clicks radio button or checkbox on primaryOwner
    And user on CreateNewProperty screen clicks on text value Add

    ### On Create Data Entry Screen entering property address details ###
    And user on CreateNewProperty screen types on referencePropertyNumber value <Reference Property Number>
    And user on CreateNewProperty screen selects on apartmentComplexName value <Apartment Name>
    And user on CreateNewProperty screen types on doorNo value <Door No>
    And user on CreateNewProperty screen selects on locality value <Locality>
    And user on CreateNewProperty screen selects on electionWard value <Election Ward>
    And user on CreateNewProperty screen selects on zoneNo value <Zone No>
    And user on CreateNewProperty screen selects on wardNo value <Ward No>
    And user on CreateNewProperty screen selects on blockNo value <Block No>
    And user on CreateNewProperty screen selects on street value <Street>
    And user on CreateNewProperty screen selects on revenueCircle value <Revenue Circle>
    And user on CreateNewProperty screen types on pincode value <Pin>
    And user on CreateNewProperty screen types on noOfFloors value <Total Floors>
    And user on CreateNewProperty screen types on plotNumber value 3 digit random number
    And user on CreateNewProperty screen types on ctsSurveyNumber value 4 digit random number
    And user on CreateNewProperty screen types on landmark value "LandMark ", 4 random characters

    ### On Create Data Entry Screen entering Assessment details ###
    And user on CreateNewProperty screen selects on reasonForCreation value <Reason for Creation>
    And user on CreateNewProperty screen selects on propertyType value <Property Type>
    And user on CreateNewProperty screen selects on propertyTypeSubType value <Property Sub Type>
    And user on CreateNewProperty screen selects on usageType value <Usage Type>
    And user on CreateNewProperty screen selects on assessmentUsageSubType value <Usage Sub Type>
    And user on CreateNewProperty screen types on totalArea value <Total Area>
    And user on CreateNewProperty screen types on sequenceNo value <Sequence No>
    And user on CreateNewProperty screen types on buildingPermissionNumber value <Building permission Number>
    And user on CreateNewProperty screen types on buildingPermissionDate value <Building Permission Date>

    ### On Create Data Entry Screen entering Property Factors ###
    And user on CreateNewProperty screen selects on toiletFactor value <Toilet Factor>
    And user on CreateNewProperty screen selects on raodFactor value <Road Factor>
    And user on CreateNewProperty screen selects on liftFactor value <Lift Factor>
    And user on CreateNewProperty screen selects on parkingFactor value <Parking Factor>

    ### On Create Data Entry Screen entering Floor Details ###
    And user on CreateNewProperty screen selects on floorNo value <Floor No>
    And user on CreateNewProperty screen selects on unitType value <Unit Type>
    And user on CreateNewProperty screen types on unitNo value <Unit No>
    And user on CreateNewProperty screen selects on constructionClass value <Construction Class>
    And user on CreateNewProperty screen selects on floorUsageType value <Floor Usage Type>
    And user on CreateNewProperty screen selects on floorUsageSubType value <Floor Usage Sub Type>
    And user on CreateNewProperty screen types on firmName value <Firm Name>
    And user on CreateNewProperty screen selects on occupancy value <Occupancy>
    And user on CreateNewProperty screen types on occupantName value <Occupancy Name>
#    And user on CreateNewProperty screen types on annualRent value <Annual Rent>
    And user on CreateNewProperty screen types on annualRetableValue value <Annual Retable Value>
    And user on CreateNewProperty screen types on retableValue value <Retable Value>
    And user on CreateNewProperty screen types on constructionStartDate value <Construction Start Date>
    And user on CreateNewProperty screen types on constructionEndDate value <Construction end Date>
    And user on CreateNewProperty screen types on effectiveFromDate value <Effective from Date>
    And user on CreateNewProperty screen selects on unStructuredLand value <Unstructured Land>
    And user on CreateNewProperty screen types on length value <length>
    And user on CreateNewProperty screen types on breadth value <Breadth>
    And user on CreateNewProperty screen types on buildUpArea value <BuildUp Area>
    And user on CreateNewProperty screen types on carpetArea value <Carpet Area>
    And user on CreateNewProperty screen types on exemptedArea value <Exempted area>
    And user on CreateNewProperty screen types on occupancyCertificateNumber value <Occupany Certificate Number>
    And user on CreateNewProperty screen types on buildingCost value <Building cost>
    And user on CreateNewProperty screen types on landCost value <Land Cost>
    And user on CreateNewProperty screen clicks on text value Add Room

    ### On Create Data Entry Screen entering Workflow Details ###
    And user on CreateNewProperty screen selects on departmentName value <Department Name>
    And user on CreateNewProperty screen selects on designation value <Designation>
    And user on CreateNewProperty screen selects on approverName value <Approver>
    And user on CreateNewProperty screen types on comments value Comments
    And user on CreateNewProperty screen clicks on text value Forward
    And user on WCMSNewConnection screen copies the ptNumber to ptNumber

#    ### Logout ###
#    And Intent:LogoutIntentTest

    Examples:
      | Aadhaar Number | Phone Number | Owner Name | Gender | Email           | Pan        | Guardian Relation | Guardian | Percentage Of OwnerShip | Reference Property Number | Apartment Name | Door No | Locality  | Election Ward | Zone No | Ward No   | Block No | Street | Revenue Circle | Pin    | Total Floors | Reason for Creation | Property Type | Property Sub Type | Usage Type  | Usage Sub Type | Total Area | Sequence No | Building permission Number | Building Permission Date | Toilet Factor | Road Factor | Lift Factor | Parking Factor | Floor No     | Unit Type | Unit No | Construction Class | Floor Usage Type | Floor Usage Sub Type | Firm Name | Occupancy | Occupancy Name | Annual Rent | Annual Retable Value | Retable Value | Construction Start Date | Construction end Date | Effective from Date | Unstructured Land | length | Breadth | BuildUp Area | Carpet Area | Exempted area | Occupany Certificate Number | Building cost | Land Cost | Department Name       | Designation   | Approver    |
      | 202422314421   | 9967231546   | Sunil      | Male   | sunil@gmail.com | BGDHT4703E | Father            | P Sunil  | 100                     | 101211111111112           | NA             | 10001   | AndharAli | Prabhag 1     | Zone A  | Prabhag 2 | NA       | NA     | NA             | 560037 | 2            | New Property        | Building      | Shop              | Residential | Slum           | 5000       | 1           | 1234                       | 01/08/2017               | 1             | 2           | 3           | 4              | Ground Floor | Room      | 505     | RCC Load bearing   | Non Residential  | Shop                 | Firm      | Owner     | Sunil          | 2000        | NA                   | 200           | 02/05/2012              | 03/05/2012            | 04/05/2012          | Yes               | NA     | NA      | 3000         | 500         | 200           | 10                          | 200000        | 50000     | Assessment Department | Tax Inspector | Ramakrishna |
