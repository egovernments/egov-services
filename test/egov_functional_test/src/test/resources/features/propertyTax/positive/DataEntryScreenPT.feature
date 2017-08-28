Feature: Creating a Property Tax with Data Entry Screen

  Scenario Outline: Create A DataEntryScreen in MAHA-ULB

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value mh.narasappa
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen verifies myTasks has visible value My Tasks
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create Data Entry
    And user on Home screen clicks on firstMenuItem

    ### On Create Data Entry Screen entering owner details ###
    And user on PropertyTax screen verifies dataEntryText has visible value Create DataEntry
    And user on PropertyTax screen types on oldPropertyId value <Old Property No>
    And user on PropertyTax screen types on aadhaarNumber value <Aadhaar Number>
    And user on PropertyTax screen types on phoneNumber value <Phone Number>
    And user on PropertyTax screen types on ownerName value <Owner Name>
    And user on PropertyTax screen selects on gender value <Gender>
    And user on PropertyTax screen types on email value <Email>
    And user on PropertyTax screen types on panNumber value <Pan>
    And user on PropertyTax screen selects on guardianRelation value <Guardian Relation>
    And user on PropertyTax screen types on guardian value <Guardian>
    And user on PropertyTax screen types on percentageOfOwnerShip value <Percentage Of OwnerShip>
    And user on PropertyTax screen clicks radio button or checkbox on primaryOwner
    And user on PropertyTax screen clicks on text value Add

    ### On Create Data Entry Screen entering property address details ###
    And user on PropertyTax screen types on referencePropertyNumber value <Reference Property Number>
    And user on PropertyTax screen types on doorNo value <Door No>
    And user on PropertyTax screen selects on locality value <Locality>
    And user on PropertyTax screen selects on electionWard value <Election Ward>
    And user on PropertyTax screen selects on zoneNo value <Zone No>
    And user on PropertyTax screen selects on wardNo value <Ward No>
#    And user on PropertyTax screen selects on blockNo value <Block No>
#    And user on PropertyTax screen selects on street value <Street>
#    And user on PropertyTax screen selects on revenueCircle value <Revenue Circle>
    And user on PropertyTax screen types on pincode value <Pin>
    And user on PropertyTax screen selects on noOfFloors value <Total Floors>

    ### On Create Data Entry Screen entering Assessment details ###
    And user on PropertyTax screen selects on reasonForCreation value <Reason for Creation>
    And user on PropertyTax screen selects on propertyType value <Property Type>
    And user on PropertyTax screen selects on propertyTypeSubType value <Property Sub Type>
    And user on PropertyTax screen selects on usageType value <Usage Type>
    And user on PropertyTax screen selects on assessmentUsageSubType value <Usage Sub Type>
    And user on PropertyTax screen types on totalArea value <Total Area>
    And user on PropertyTax screen types on sequenceNo value <Sequence No>
    And user on PropertyTax screen types on buildingPermissionNumber value <Building permission Number>
    And user on PropertyTax screen selectDate on buildingPermissionDate value <Building Permission Date>

    ### On Create Data Entry Screen entering Property Factors ###
    And user on PropertyTax screen selects on toiletFactor value <Toilet Factor>
    And user on PropertyTax screen selects on raodFactor value <Road Factor>
    And user on PropertyTax screen selects on liftFactor value <Lift Factor>
    And user on PropertyTax screen selects on parkingFactor value <Parking Factor>

    ### On Create Data Entry Screen entering Floor Details ###
    And user on PropertyTax screen selects on floorNo value <Floor No>
    And user on PropertyTax screen selects on unitType value <Unit Type>
    And user on PropertyTax screen types on unitNo value <Unit No>
    And user on PropertyTax screen selects on constructionClass value <Construction Class>
    And user on PropertyTax screen selects on floorUsageType value <Floor Usage Type>
    And user on PropertyTax screen selects on floorUsageSubType value <Floor Usage Sub Type>
    And user on PropertyTax screen types on firmName value <Firm Name>
    And user on PropertyTax screen selects on occupancy value <Occupancy>
    And user on PropertyTax screen types on occupantName value <Occupancy Name>
#    And user on PropertyTax screen types on annualRent value <Annual Rent>
    And user on PropertyTax screen types on annualRetableValue value <Annual Retable Value>
    And user on PropertyTax screen types on retableValue value <Retable Value>
    And user on PropertyTax screen selectDate on constructionStartDate value <Construction Start Date>
    And user on PropertyTax screen selectDate on constructionEndDate value <Construction end Date>
    And user on PropertyTax screen selectDate on effectiveFromDate value <Effective from Date>
    And user on PropertyTax screen selects on unStructuredLand value <Unstructured Land>
#    And user on PropertyTax screen types on length value <length>
#    And user on PropertyTax screen types on breadth value <Breadth>
    And user on PropertyTax screen types on buildUpArea value <BuildUp Area>
    And user on PropertyTax screen types on carpetArea value <Carpet Area>
    And user on PropertyTax screen types on exemptedArea value <Exempted area>
    And user on PropertyTax screen types on occupancyCertificateNumber value <Occupany Certificate Number>
    And user on PropertyTax screen types on buildingCost value <Building cost>
    And user on PropertyTax screen types on landCost value <Land Cost>
    And user on PropertyTax screen clicks on text value Add Room

    ### On Create Data Entry Screen entering Construction Details ###
    And user on PropertyTax screen selectDate on currentAssessmentDate value <Current Assessment date>
    And user on PropertyTax screen selectDate on firstAssessmentDate value <First Assessment Date>
    And user on PropertyTax screen selectDate on revisedAssessmentDate value <Revised Assessment Date>
    And user on PropertyTax screen selectDate on lastAssessmentDate value <Last assessment date>
    And user on PropertyTax screen selectDate on commencementDate value <Commencement date>
    And user on PropertyTax screen types on certificateNumber value <Cerficate number>
    And user on PropertyTax screen selectDate on certificateCompletionDate value <Cerficate Complition Date>
    And user on PropertyTax screen selectDate on certificateReceivedDate value <Cerficate Received Date>
    And user on PropertyTax screen types on agencyName value <Agency name>
    And user on PropertyTax screen types on licenseType value <License Type>
    And user on PropertyTax screen types on licenseNumber value <License Number>


    Examples:
      | Old Property No | Aadhaar Number | Phone Number | Owner Name | Gender | Email           | Pan        | Guardian Relation | Guardian | Percentage Of OwnerShip | Reference Property Number | Apartment Name | Door No | Locality  | Election Ward | Zone No | Ward No   | Block No | Street | Revenue Circle | Pin    | Total Floors | Reason for Creation | Property Type | Property Sub Type | Usage Type  | Usage Sub Type | Total Area | Sequence No | Building permission Number | Building Permission Date | Toilet Factor | Road Factor | Lift Factor | Parking Factor | Floor No     | Unit Type | Unit No | Construction Class | Floor Usage Type | Floor Usage Sub Type | Firm Name | Occupancy | Occupancy Name | Annual Rent | Annual Retable Value | Retable Value | Construction Start Date | Construction end Date | Effective from Date | Unstructured Land | length | Breadth | BuildUp Area | Carpet Area | Exempted area | Occupany Certificate Number | Building cost | Land Cost | Current Assessment date | First Assessment Date | Revised Assessment Date | Last assessment date | Commencement date | Cerficate number | Cerficate Complition Date | Cerficate Received Date | Agency name | License Type | License Number |
      | 1012000001      | 202422314421   | 9967231546   | Sunil      | Male   | sunil@gmail.com | BGDHT4703E | Father            | P Sunil  | 100                     | 101211111111112           | NA             | 10001   | AndharAli | Prabhag 1     | Zone A  | Prabhag 2 | NA       | NA     | NA             | 560037 | 1 Floor      | New Property        | Building      | Options           | Residential | Residential    | 5000       | 1           | RO1                        | 01/08/2017               | 1             | 2           | 3           | 4              | Ground Floor | Room      | 505     | RCC Load bearing   | NON RESIDETIAL   | NON RESIDETIAL       | Firm      | Owner     | Sunil          | 2000        | 2400                 | 200           | 02/08/2017              | 03/08/2017            | 04/08/2017          | Yes               |        |         | 3000         | 500         | 200           | 10                          | 200000        | 50000     | 05/08/2017              | 06/08/2017            | 07/08/2017              | 08/08/2017           | 09/08/2017        | 111              | 10/08/2017                | 11/08/2017              | Raghav      | A1           | 3332           |



    Scenario Outline: Create A DataEntryScreen in Micro-QA with tenantId mh.roha

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value avinay
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
      And user on Home screen will wait until the page loads
    And user on Home screen verifies myTasks has visible value My Tasks
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create Data Entry
    And user on Home screen clicks on firstMenuItem

    ### On Create Data Entry Screen entering owner details ###
    And user on PropertyTax screen verifies dataEntryText has visible value Create DataEntry
    And user on PropertyTax screen types on oldPropertyId value <Old Property No>
    And user on PropertyTax screen types on aadhaarNumber value <Aadhaar Number>
    And user on PropertyTax screen types on phoneNumber value <Phone Number>
    And user on PropertyTax screen types on ownerName value <Owner Name>
    And user on PropertyTax screen selects on gender value <Gender>
    And user on PropertyTax screen types on email value <Email>
    And user on PropertyTax screen types on panNumber value <Pan>
    And user on PropertyTax screen selects on guardianRelation value <Guardian Relation>
    And user on PropertyTax screen types on guardian value <Guardian>
    And user on PropertyTax screen types on percentageOfOwnerShip value <Percentage Of OwnerShip>
    And user on PropertyTax screen clicks radio button or checkbox on primaryOwner
    And user on PropertyTax screen clicks on text value Add

    ### On Create Data Entry Screen entering property address details ###
    And user on PropertyTax screen types on referencePropertyNumber value <Reference Property Number>
    And user on PropertyTax screen types on doorNo value <Door No>
    And user on PropertyTax screen selects on locality value <Locality>
    And user on PropertyTax screen selects on electionWard value <Election Ward>
    And user on PropertyTax screen selects on zoneNo value <Zone No>
    And user on PropertyTax screen selects on wardNo value <Ward No>
#    And user on PropertyTax screen selects on blockNo value <Block No>
#    And user on PropertyTax screen selects on street value <Street>
#    And user on PropertyTax screen selects on revenueCircle value <Revenue Circle>
    And user on PropertyTax screen types on pincode value <Pin>
    And user on PropertyTax screen selects on noOfFloors value <Total Floors>

    ### On Create Data Entry Screen entering Assessment details ###
    And user on PropertyTax screen selects on reasonForCreation value <Reason for Creation>
    And user on PropertyTax screen selects on propertyType value <Property Type>
#    And user on PropertyTax screen selects on propertyTypeSubType value <Property Sub Type>
    And user on PropertyTax screen selects on usageType value <Usage Type>
    And user on PropertyTax screen selects on assessmentUsageSubType value <Usage Sub Type>
    And user on PropertyTax screen types on totalArea value <Total Area>
    And user on PropertyTax screen types on sequenceNo value <Sequence No>
    And user on PropertyTax screen types on buildingPermissionNumber value <Building permission Number>
    And user on PropertyTax screen types on buildingPermissionDateQA value <Building Permission Date>

    ### On Create Data Entry Screen entering Property Factors ###
    And user on PropertyTax screen selects on toiletFactor value <Toilet Factor>
    And user on PropertyTax screen selects on raodFactor value <Road Factor>
    And user on PropertyTax screen selects on liftFactor value <Lift Factor>
    And user on PropertyTax screen selects on parkingFactor value <Parking Factor>

    ### On Create Data Entry Screen entering Floor Details ###
    And user on PropertyTax screen selects on floorNo value <Floor No>
    And user on PropertyTax screen selects on unitType value <Unit Type>
    And user on PropertyTax screen types on unitNo value <Unit No>
    And user on PropertyTax screen selects on constructionClass value <Construction Class>
    And user on PropertyTax screen selects on floorUsageType value <Floor Usage Type>
    And user on PropertyTax screen selects on floorUsageSubType value <Floor Usage Sub Type>
    And user on PropertyTax screen types on firmName value <Firm Name>
    And user on PropertyTax screen selects on occupancy value <Occupancy>
    And user on PropertyTax screen types on occupantName value <Occupancy Name>
#    And user on PropertyTax screen types on annualRent value <Annual Rent>
    And user on PropertyTax screen types on annualRetableValue value <Annual Retable Value>
    And user on PropertyTax screen types on retableValue value <Retable Value>
    And user on PropertyTax screen types on constructionStartDateQA value <Construction Start Date>
    And user on PropertyTax screen types on constructionEndDate value <Construction end Date>
    And user on PropertyTax screen types on effectiveFromDate value <Effective from Date>
    And user on PropertyTax screen selects on unStructuredLand value <Unstructured Land>
    And user on PropertyTax screen types on length value <length>
    And user on PropertyTax screen types on breadth value <Breadth>
    And user on PropertyTax screen types on buildUpArea value <BuildUp Area>
    And user on PropertyTax screen types on carpetArea value <Carpet Area>
    And user on PropertyTax screen types on exemptedArea value <Exempted area>
    And user on PropertyTax screen types on occupancyCertificateNumber value <Occupany Certificate Number>
    And user on PropertyTax screen types on buildingCost value <Building cost>
    And user on PropertyTax screen types on landCost value <Land Cost>
    And user on PropertyTax screen clicks on text value Add Room

    ### On Create Data Entry Screen entering Construction Details ###
    And user on PropertyTax screen types on currentAssessmentDate value <Current Assessment date>
    And user on PropertyTax screen types on firstAssessmentDate value <First Assessment Date>
    And user on PropertyTax screen types on revisedAssessmentDate value <Revised Assessment Date>
    And user on PropertyTax screen types on lastAssessmentDate value <Last assessment date>
    And user on PropertyTax screen types on commencementDate value <Commencement date>
    And user on PropertyTax screen types on certificateNumber value <Cerficate number>
    And user on PropertyTax screen types on certificateCompletionDate value <Cerficate Complition Date>
    And user on PropertyTax screen types on certificateReceivedDate value <Cerficate Received Date>
    And user on PropertyTax screen types on agencyName value <Agency name>
    And user on PropertyTax screen types on licenseType value <License Type>
    And user on PropertyTax screen types on licenseNumber value <License Number>

    Examples:
      | Old Property No | Aadhaar Number | Phone Number | Owner Name | Gender | Email             | Pan        | Guardian Relation | Guardian  | Percentage Of OwnerShip | Reference Property Number | Apartment Name | Door No | Locality          | Election Ward | Zone No | Ward No   | Block No | Street | Revenue Circle | Pin    | Total Floors | Reason for Creation | Property Type                | Property Sub Type | Usage Type  | Usage Sub Type | Total Area | Sequence No | Building permission Number | Building Permission Date | Toilet Factor | Road Factor | Lift Factor | Parking Factor | Floor No     | Unit Type | Unit No | Construction Class | Floor Usage Type | Floor Usage Sub Type | Firm Name | Occupancy | Occupancy Name | Annual Rent | Annual Retable Value | Retable Value | Construction Start Date | Construction end Date | Effective from Date | Unstructured Land | length | Breadth | BuildUp Area | Carpet Area | Exempted area | Occupany Certificate Number | Building cost | Land Cost | Current Assessment date | First Assessment Date | Revised Assessment Date | Last assessment date | Commencement date | Cerficate number | Cerficate Complition Date | Cerficate Received Date | Agency name | License Type | License Number |
      | 1012000001      | 202422314421   | 9967231546   | Sunil      | Male   | sunil@gmail.com   | BGDHT4703E | Father            | P Sunil   | 100                     | 101211111111112           | NA             | 10001   | AndharAli         | Prabhag 1     | Zone A  | Prabhag 2 | NA       | NA     | NA             | 560037 | 1 Floor      | New Property        | Private property            | Options           | Residential | Residential    | 5000       | 1           | 1234                       | 01/08/2017               | 1             | 2           | 3           | 4              | Ground Floor | Room      | 505     | RCC Load bearing   | NON RESIDETIAL   | Hall                 | Firm      | Owner     | Sunil          | 2000        | 2400                 | 200           | 02/05/2012              | 03/05/2012            | 04/05/2012          | Yes               | null   | null    | 3000         | 500         | 200           | 10                          | 200000        | 50000     | 05/08/2017              | 06/08/2017            | 07/08/2017              | 08/08/2017           | 09/08/2017        | 111              | 10/08/2017                | 11/08/2017              | Raghav      | A1           | 3332           |
#      | 1012000002      | 202422314424   | 9967231547   | Ramesh     | Male   | ramesh@gmail.com  | BGDHT4703D | Mother            | J Raju    | 50                      | 101211111111114           | NA             | 1003    | Ashtami Bajarpeth | Prabhag 2     | Zone B  | Prabhag 4 | NA       | NA     | NA             | 543266 | 2 Floors     | New Property        | Central Government Property  | Options           | Residential | Residential    | 4500       | 4           | 4321                       | 01/08/2017               | 9             | 9           | 9           | 9              | 6th Floor    | Room      | 707     | RCC Load bearing   | Residential      | Residential          | New Firm  | Owner     | Pratap         | 3000        | 3600                 | 150           | 02/08/2017              | 03/08/2017            | 04/08/2017          | Yes               | null   | null    | 250          | 180         | 120           | 901                         | 200000        | 75000     | 05/08/2017              | 06/08/2017            | 07/08/2017              | 08/08/2017           | 09/08/2017        | 333              | 10/08/2017                | 11/08/2017              | pratap      | A3           | 4455           |
#      | 1012000003      | 202422314425   | 9954321456   | Vinay      | Male   | vinay@gmail.com   | BGDHT4705T | Father            | N Vinay   | 20                      | 101211111111665           | NA             | 1005    | Aambedkar Nagar   | Prabhag 3     | Zone C  | Prabhag 3 | NA       | NA     | NA             | 456781 | 3 Floors     | New Property        | Building                    | Options           | Residential | Residential    | 6000       | 3           | 1243                       | 01/08/2017               | 4             | 8           | 6           | 8              | 2nd Floor    | Room      | 443     | RCC Load bearing   | Residential      | Other                | Firm1     | Owner     | Dillip         | 2700        | 1900                 | 400           | 02/08/2017              | 03/08/2017            | 04/08/2017          | No                | 15     | 11      | null         | 250         | 220           | 120                         | 300000        | 55000     | 04/08/2017              | 05/08/2017            | 06/08/2017              | 07/08/2017           | 08/08/2017        | 444              | 09/08/2017                | 10/08/2017              | Dinesh      | A4           | 6666           |
#      | 1012000004      | 202422314426   | 9967231547   | Karthik    | Male   | karthik@gmail.com | TGDHT4703D | Mother            | D Karthik | 30                      | 101211111111555           | NA             | 1006    | Court Road        | Prabhag 4     | Zone A  | Prabhag 4 | NA       | NA     | NA             | 112255 | 5 Floors     | New Property        | Building                    | Options           | Residential | Residential    | 5500       | 8           | 2143                       | 01/08/2017               | 5             | 9           | 7           | 7              | 3rd Floor    | Room      | 332     | RCC Load bearing   | Residential      | Home                 | Firm2     | Owner     | Kumar          | 2100        | 1300                 | 300           | 02/08/2017              | 03/08/2017            | 04/08/2017          | Yes               | null   | null    | 130          | 180         | 130           | 111                         | 350000        | 120000    | 04/08/2017              | 05/08/2017            | 06/08/2017              | 07/08/2017           | 08/08/2017        | 555              | 09/08/2017                | 10/08/2017              | Sudheer     | A5           | 8799           |
#      | 1012000008      | 202422314421   | 9967231546   | Sumanth K  | Male   | sumanth@gmail.com | HYDHT4703J | Father            | Pratap    | 100                     | 101211111117896           | NA             | 9009    | AndharAli         | Prabhag 1     | Zone A  | Prabhag 2 | NA       | NA     | NA             | 112233 | 4 Floors     | New Property        | Building                    | Options           | Residential | Residential    | 3300       | 7           | 2134                       | 01/08/2017               | 8             | 2           | 3           | 4              | Ground Floor | Room      | 999     | RCC Load bearing   | Residential      | Other                | Firm      | Owner     | Jay            | 2100        | 2700                 | 200           | 02/08/2017              | 03/08/2017            | 04/08/2017          | Yes               | null   | null    | 3000         | 500         | 200           | 10                          | 200000        | 50000     | 05/08/2017              | 06/08/2017            | 07/08/2017              | 08/08/2017           | 09/08/2017        | 111              | 10/08/2017                | 11/08/2017              | Ravindra    | A8           | 5555           |
