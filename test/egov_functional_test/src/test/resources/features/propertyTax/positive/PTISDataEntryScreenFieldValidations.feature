Feature: Checking Field Validations for Data Entry Screen In PTIS Module

  Scenario: Create A DataEntryScreen along with field validations

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value avinay
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create Data Entry
    And user on Home screen clicks on firstMenuItem

    ### On Create Data Entry Screen entering owner details ###
    And user on PTISDataEntry screen verifies dataEntryText has visible value Create DataEntry
    And user on PTISDataEntry screen validates
      | types   | oldPropertyId         | asdfgh$@jkl   | Invalid field data | 8688000062    |
      | types   | aadhaarNumber         | as#dfghj@;kl  | Invalid field data | 199022311994  |
      | types   | phoneNumber           | as#dfghj@;kl  | Invalid field data | 1994915765    |
      | types   | ownerName             | 1234;*        | Invalid field data | Rahim         |
      | selects | gender                | NA            | NA                 | Male          |
      | types   | email                 | 1234567sdfgh] | Invalid field data | rahim@xyz.com |
      | types   | panNumber             | abcd          | Invalid field data | KLMHT1238G    |
      | selects | guardianRelation      | NA            | NA                 | Father        |
      | types   | guardian              | 12345;^       | Invalid field data | Imran         |
      | types   | percentageOfOwnerShip | ab            | Invalid field data | 35            |

#    ### On Create Data Entry Screen entering property address details ###
#    And user on PTISDataEntry screen types on referencePropertyNumber value <Reference Property Number>
#    And user on PTISDataEntry screen types on doorNo value <Door No>
#    And user on PTISDataEntry screen selects on locality value <Locality>
#    And user on PTISDataEntry screen selects on electionWard value <Election Ward>
#    And user on PTISDataEntry screen selects on zoneNo value <Zone No>
#    And user on PTISDataEntry screen selects on wardNo value <Ward No>
##    And user on PTISDataEntry screen selects on blockNo value <Block No>
##    And user on PTISDataEntry screen selects on street value <Street>
##    And user on PTISDataEntry screen selects on revenueCircle value <Revenue Circle>
#    And user on PTISDataEntry screen types on pincode value <Pin>
#    And user on PTISDataEntry screen types on noOfFloors value <Total Floors>
#
#    ### On Create Data Entry Screen entering Assessment details ###
#    And user on PTISDataEntry screen selects on reasonForCreation value <Reason for Creation>
#    And user on PTISDataEntry screen selects on propertyType value <Property Type>
##    And user on PTISDataEntry screen selects on propertyTypeSubType value <Property Sub Type>
#    And user on PTISDataEntry screen selects on usageType value <Usage Type>
#    And user on PTISDataEntry screen selects on assessmentUsageSubType value <Usage Sub Type>
#    And user on PTISDataEntry screen types on totalArea value <Total Area>
#    And user on PTISDataEntry screen types on sequenceNo value <Sequence No>
#    And user on PTISDataEntry screen types on buildingPermissionNumber value <Building permission Number>
#    And user on PTISDataEntry screen types on buildingPermissionDateQA value <Building Permission Date>
#
#    ### On Create Data Entry Screen entering Property Factors ###
#    And user on PTISDataEntry screen selects on toiletFactor value <Toilet Factor>
#    And user on PTISDataEntry screen selects on raodFactor value <Road Factor>
#    And user on PTISDataEntry screen selects on liftFactor value <Lift Factor>
#    And user on PTISDataEntry screen selects on parkingFactor value <Parking Factor>
#
#    ### On Create Data Entry Screen entering Floor Details ###
#    And user on PTISDataEntry screen selects on floorNo value <Floor No>
#    And user on PTISDataEntry screen selects on unitType value <Unit Type>
#    And user on PTISDataEntry screen types on unitNo value <Unit No>
#    And user on PTISDataEntry screen selects on constructionClass value <Construction Class>
#    And user on PTISDataEntry screen selects on floorUsageType value <Floor Usage Type>
#    And user on PTISDataEntry screen selects on floorUsageSubType value <Floor Usage Sub Type>
#    And user on PTISDataEntry screen types on firmName value <Firm Name>
#    And user on PTISDataEntry screen selects on occupancy value <Occupancy>
#    And user on PTISDataEntry screen types on occupantName value <Occupancy Name>
##    And user on PTISDataEntry screen types on annualRent value <Annual Rent>
#    And user on PTISDataEntry screen types on annualRetableValue value <Annual Retable Value>
#    And user on PTISDataEntry screen types on retableValue value <Retable Value>
#    And user on PTISDataEntry screen types on constructionStartDateQA value <Construction Start Date>
#    And user on PTISDataEntry screen types on constructionEndDate value <Construction end Date>
#    And user on PTISDataEntry screen types on effectiveFromDate value <Effective from Date>
#    And user on PTISDataEntry screen selects on unStructuredLand value <Unstructured Land>
#    And user on PTISDataEntry screen types on length value <length>
#    And user on PTISDataEntry screen types on breadth value <Breadth>
#    And user on PTISDataEntry screen types on buildUpArea value <BuildUp Area>
#    And user on PTISDataEntry screen types on carpetArea value <Carpet Area>
#    And user on PTISDataEntry screen types on exemptedArea value <Exempted area>
#    And user on PTISDataEntry screen types on occupancyCertificateNumber value <Occupany Certificate Number>
#    And user on PTISDataEntry screen types on buildingCost value <Building cost>
#    And user on PTISDataEntry screen types on landCost value <Land Cost>
#    And user on PTISDataEntry screen clicks on text value Add Room
#
#    ### On Create Data Entry Screen entering Construction Details ###
#    And user on PTISDataEntry screen types on currentAssessmentDate value <Current Assessment date>
#    And user on PTISDataEntry screen types on firstAssessmentDate value <First Assessment Date>
#    And user on PTISDataEntry screen types on revisedAssessmentDate value <Revised Assessment Date>
#    And user on PTISDataEntry screen types on lastAssessmentDate value <Last assessment date>
#    And user on PTISDataEntry screen types on commencementDate value <Commencement date>
#    And user on PTISDataEntry screen types on certificateNumber value <Cerficate number>
#    And user on PTISDataEntry screen types on certificateCompletionDate value <Cerficate Complition Date>
#    And user on PTISDataEntry screen types on certificateReceivedDate value <Cerficate Received Date>
#    And user on PTISDataEntry screen types on agencyName value <Agency name>
#    And user on PTISDataEntry screen types on licenseType value <License Type>
#    And user on PTISDataEntry screen types on licenseNumber value <License Number>
#    And user on PTISDataEntry screen clicks on text value Create
#    And user on PTISDataEntry screen will wait until the page loads
#    And user on PTISDataEntry screen copies the propertyTaxNumber to ptNumber
#
#    ### On Homepage Screen ###
#    And user on Home screen will wait until the page loads
#    And user on Home screen will see the menu
#    And user on Home screen clicks on menu
#    And user on Home screen types on menuSearch value Search Property
#    And user on Home screen clicks on firstMenuItem
#
#    ### On PropertyTax Search Screen ###
#    And user on PTISSearchProperty screen types on assessmentNumber value ptNumber
#    And user on PTISSearchProperty screen clicks on text value Search
#    And user on PTISSearchProperty screen verifies assessmentNumberInSearchTable has visible value ptNumber
#    And user on PTISSearchProperty screen clicks on action
#    And user on PTISSearchProperty screen force clicks on text value Add/Edit DCB

#    Examples:
#      | Old Property No | Aadhaar Number | Phone Number | Owner Name | Gender | Email         | Pan        | Guardian Relation | Guardian | Percentage Of OwnerShip | Reference Property Number | Apartment Name | Door No | Locality  | Election Ward | Zone No | Ward No   | Block No | Street | Revenue Circle | Pin    | Total Floors | Reason for Creation | Property Type | Property Sub Type | Usage Type | Usage Sub Type | Total Area | Sequence No | Building permission Number | Building Permission Date | Toilet Factor | Road Factor | Lift Factor | Parking Factor | Floor No  | Unit Type | Unit No | Construction Class | Floor Usage Type | Floor Usage Sub Type | Firm Name | Occupancy | Occupancy Name | Annual Rent | Annual Retable Value | Retable Value | Construction Start Date | Construction end Date | Effective from Date | Unstructured Land | length | Breadth | BuildUp Area | Carpet Area | Exempted area | Occupany Certificate Number | Building cost | Land Cost | Current Assessment date | First Assessment Date | Revised Assessment Date | Last assessment date | Commencement date | Cerficate number | Cerficate Complition Date | Cerficate Received Date | Agency name | License Type | License Number |
#      | 8688000062      | 199022311994   | 1994915765   | Rahim      | Male   | rahim@xyz.com | KLMHT1238G | Father            | Imran    | 35                      | 994961100111345           | NA             | 1995    | Kasar Ali | Prabhag 2     | Zone A  | Prabhag 5 | NA       | NA     | NA             | 234567 | 4            | New Property        | Shop          | Options           | Industrial | null           | 4500       | 4           | 4578                       | 01/08/2017               | 9             | 3           | 5           | 2              | 6th Floor | Room      | 678     | RCC Load bearing   | Mixed            | Mixed                | Firm New  | Owner     | Imran          | 2000        | 1900                 | 250           | 02/08/2017              | 03/08/2017            | 04/08/2017          | Yes               | null   | null    | 1800         | 360         | 150           | 14                          | 134000        | 9000      | 05/08/2017              | 06/08/2017            | 07/08/2017              | 08/08/2017           | 09/08/2017        | 111              | 10/08/2017                | 11/08/2017              | Imran       | A7           | 4634           |