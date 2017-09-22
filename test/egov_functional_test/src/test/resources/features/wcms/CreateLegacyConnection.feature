Feature: In this feature We are going to create legacy connection with the following requirements

  # Create Legacy Connection With Property and Connection Type is METERED along with demand
  # Create Legacy Connection With Property and Connection Type is NON METERED along with demand
  # Create Legacy Connection Without Property and Connection Type is METERED along with demand
  # Create Legacy Connection Without Property and Connection Type is NON METERED along with demand

  Scenario Outline: Create Legacy Connection Without Property and Connection Type is NON METERED along with demand

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 1234
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen verifies myTasks has visible value My Tasks
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create Legacy Connection
    And user on Home screen clicks on firstMenuItem

    ### On Create Legacy Connection entering applicant details ###
    And user on WCMSLegacyConnection screen types on ownerName value "Owner ", 4 random characters
    And user on WCMSLegacyConnection screen types on mobileNumber value "1",9 random numbers
    And user on WCMSLegacyConnection screen types on email value random email
    And user on WCMSLegacyConnection screen types on aadhaarNumber value 12 random numbers
    And user on WCMSLegacyConnection screen selects on gender value MALE
    And user on WCMSLegacyConnection screen types on houseNumber value 12345
    And user on WCMSLegacyConnection screen types on address value Address
    And user on WCMSLegacyConnection screen types on city value "City ", 4 random characters
    And user on WCMSLegacyConnection screen types on pin value 6 random numbers
    And user on WCMSLegacyConnection screen selects on zone value Zone A
    And user on WCMSLegacyConnection screen selects on ward value Prabhag 1
    And user on WCMSLegacyConnection screen selects on locality value Andhar Ali
    And user on WCMSLegacyConnection screen types on physicalConnectionDate value <physicalConnectionDate>
    And user on WCMSLegacyConnection screen types on oldConsumerNumber value 10 random numbers
    And user on WCMSLegacyConnection screen types on manualConsumerNumber value 10 random numbers

    ### On Create Legacy Connection entering connection details ###
    And user on WCMSLegacyConnection screen selects on usageType value <usageType>
    And user on WCMSLegacyConnection screen selects on usageSubType value <usageSubType>
    And user on WCMSLegacyConnection screen selects on connectionSize value <connectionSize>
    And user on WCMSLegacyConnection screen selects on connectionType value NONMETERED
    And user on WCMSLegacyConnection screen selects on connectionStatus value <connectionStatus>
    And user on WCMSLegacyConnection screen selects on waterSourceType value <waterSourceType>
    And user on WCMSLegacyConnection screen selects on storageReservoir value <storageReservoir>
    And user on WCMSLegacyConnection screen selects on waterTreatment value <waterTreatment>
    And user on WCMSLegacyConnection screen types on sumpCapacityDataEntry value 3 random numbers
    And user on WCMSLegacyConnection screen types on sequenceNumber value 3 random numbers
    And user on WCMSLegacyConnection screen types on plumberName value "Plumber ", 4 random characters
    And user on WCMSLegacyConnection screen types on noOfTaps value 2 random numbers
    And user on WCMSLegacyConnection screen types on buildingName value "BuildingName ",2 random numbers
    And user on WCMSLegacyConnection screen types on buildingAddress value "BuildingAddress ",2 random numbers
    And user on WCMSLegacyConnection screen types on roadName value "RoadName ",2 random numbers
    And user on WCMSLegacyConnection screen types on gisNumber value 5 random numbers
    And user on WCMSLegacyConnection screen types on noOfPersonsDataEntry value 2 random numbers

    ### On Create Legacy Connection entering Security Deposit details ###
    And user on WCMSLegacyConnection screen types on securityDeposit value 3 random numbers
    And user on WCMSLegacyConnection screen types on receiptNumber value 6 random numbers
    And user on WCMSLegacyConnection screen types on receiptDate value <receiptDate>
    And user on WCMSLegacyConnection screen clicks on text value Create
    And user on WCMSLegacyConnection screen clicks on text value Add/ Edit DCB

    ### On Add/Edit Demand Screen ###
    And user on WCMSLegacyConnection screen will wait until the page loads
    And user on WCMSLegacyConnection screen verifies text has visible value Applicant Particulars
    And user on WCMSLegacyConnection screen copies the dataEntryNumber to dataEntryNumber
    And user on WCMSDemand screen will enter all demand details
    And user on WCMSLegacyConnection screen clicks on text value Update

    ### On Search Connection Screen ###
    And user on WCMSLegacyConnection screen will wait until the page loads
    And user on WCMSLegacyConnection screen refresh's the webpage
    And user on WCMSLegacyConnection screen verifies text has visible value Search New Connection
    And user on WCMSLegacyConnection screen types on consumerNumber value dataEntryNumber
    And user on WCMSLegacyConnection screen clicks on text value Search
    And user on WCMSLegacyConnection screen verifies text has visible value dataEntryNumber

    ### Logout ###
    And Intent:LogoutIntentTest

    Examples:
      | physicalConnectionDate | usageType | usageSubType | connectionSize | connectionStatus | waterSourceType | storageReservoir | waterTreatment                                            | receiptDate |
      | 23/08/2016             | Mixed     | Mixed        | 1              | PERMANENT        | RIVER           | Storage          | bhuvaneshwar jal shudhikaran-bhuvaneshwar jal shudhikaran | 23/08/2017  |


  Scenario Outline: Create Legacy Connection With Property and Connection Type is NON METERED along with demand

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 1234
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen verifies myTasks has visible value My Tasks
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create Legacy Connection
    And user on Home screen clicks on firstMenuItem

    ### On Create Legacy Connection entering applicant details ###
    And user on WCMSLegacyConnection screen force clicks on withProperty
    And user on WCMSLegacyConnection screen types on ptAssessmentNumber value roh000008262
    And user on WCMSLegacyConnection screen clicks on ptSearch
    And user on WCMSLegacyConnection screen types on oldConsumerNumber1 value 10 random numbers
    And user on WCMSLegacyConnection screen types on manualConsumerNumber1 value 10 random numbers
    And user on WCMSLegacyConnection screen types on physicalConnectionDate1 value <physicalConnectionDate>

    ### On Create Legacy Connection entering connection details ###
    And user on WCMSLegacyConnection screen selects on usageType value <usageType>
    And user on WCMSLegacyConnection screen selects on usageSubType value <usageSubType>
    And user on WCMSLegacyConnection screen selects on connectionSize value <connectionSize>
    And user on WCMSLegacyConnection screen selects on connectionType value NONMETERED
    And user on WCMSLegacyConnection screen selects on connectionStatus value <connectionStatus>
    And user on WCMSLegacyConnection screen selects on waterSourceType value <waterSourceType>
    And user on WCMSLegacyConnection screen selects on storageReservoir value <storageReservoir>
    And user on WCMSLegacyConnection screen selects on waterTreatment value <waterTreatment>
    And user on WCMSLegacyConnection screen types on sumpCapacityDataEntry value 3 random numbers
    And user on WCMSLegacyConnection screen types on sequenceNumber value 3 random numbers
    And user on WCMSLegacyConnection screen types on plumberName value "Plumber ", 4 random characters
    And user on WCMSLegacyConnection screen types on noOfTaps value 2 random numbers
    And user on WCMSLegacyConnection screen types on buildingName value "BuildingName ",2 random numbers
    And user on WCMSLegacyConnection screen types on buildingAddress value "BuildingAddress ",2 random numbers
    And user on WCMSLegacyConnection screen types on roadName value "RoadName ",2 random numbers
    And user on WCMSLegacyConnection screen types on gisNumber value 5 random numbers
    And user on WCMSLegacyConnection screen types on noOfPersonsDataEntry value 2 random numbers

    ### On Create Legacy Connection entering Security Deposit details ###
    And user on WCMSLegacyConnection screen types on securityDeposit value 3 random numbers
    And user on WCMSLegacyConnection screen types on receiptNumber value 6 random numbers
    And user on WCMSLegacyConnection screen types on receiptDate value <receiptDate>
    And user on WCMSLegacyConnection screen clicks on text value Create
    And user on WCMSLegacyConnection screen clicks on text value Add/ Edit DCB

    ### On Add/Edit Demand Screen ###
    And user on WCMSLegacyConnection screen will wait until the page loads
    And user on WCMSLegacyConnection screen verifies text has visible value Applicant Particulars
    And user on WCMSLegacyConnection screen copies the dataEntryNumber to dataEntryNumber
    And user on WCMSDemand screen will enter all demand details
    And user on WCMSLegacyConnection screen clicks on text value Update

    ### On Search Connection Screen ###
    And user on WCMSLegacyConnection screen will wait until the page loads
    And user on WCMSLegacyConnection screen refresh's the webpage
    And user on WCMSLegacyConnection screen verifies text has visible value Search New Connection
    And user on WCMSLegacyConnection screen types on consumerNumber value dataEntryNumber
    And user on WCMSLegacyConnection screen clicks on text value Search
    And user on WCMSLegacyConnection screen verifies text has visible value dataEntryNumber

    ### Logout ###
    And Intent:LogoutIntentTest

    Examples:
      | physicalConnectionDate | usageType | usageSubType | connectionSize | connectionStatus | waterSourceType | storageReservoir | waterTreatment                                            | receiptDate |
      | 23/08/2016             | Mixed     | Mixed        | 1              | PERMANENT        | RIVER           | Storage          | bhuvaneshwar jal shudhikaran-bhuvaneshwar jal shudhikaran | 23/08/2017  |


  Scenario Outline: Create Legacy Connection Without Property and Connection Type is METERED along with demand

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 1234
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen verifies myTasks has visible value My Tasks
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create Legacy Connection
    And user on Home screen clicks on firstMenuItem

    ### On Create Legacy Connection entering applicant details ###
    And user on WCMSLegacyConnection screen types on ownerName value "Owner ", 4 random characters
    And user on WCMSLegacyConnection screen types on mobileNumber value "1",9 random numbers
    And user on WCMSLegacyConnection screen types on email value random email
    And user on WCMSLegacyConnection screen types on aadhaarNumber value 12 random numbers
    And user on WCMSLegacyConnection screen selects on gender value MALE
    And user on WCMSLegacyConnection screen types on houseNumber value 12345
    And user on WCMSLegacyConnection screen types on address value Address
    And user on WCMSLegacyConnection screen types on city value "City ", 4 random characters
    And user on WCMSLegacyConnection screen types on pin value 6 random numbers
    And user on WCMSLegacyConnection screen selects on zone value Zone A
    And user on WCMSLegacyConnection screen selects on ward value Prabhag 1
    And user on WCMSLegacyConnection screen selects on locality value Andhar Ali
    And user on WCMSLegacyConnection screen types on physicalConnectionDate value <physicalConnectionDate>
    And user on WCMSLegacyConnection screen types on oldConsumerNumber value 10 random numbers
    And user on WCMSLegacyConnection screen types on manualConsumerNumber value 10 random numbers

    ### On Create Legacy Connection entering connection details ###
    And user on WCMSLegacyConnection screen selects on usageType value <usageType>
    And user on WCMSLegacyConnection screen selects on usageSubType value <usageSubType>
    And user on WCMSLegacyConnection screen selects on connectionSize value <connectionSize>
    And user on WCMSLegacyConnection screen selects on connectionType value METERED
    And user on WCMSLegacyConnection screen selects on connectionStatus value <connectionStatus>
    And user on WCMSLegacyConnection screen selects on waterSourceType value <waterSourceType>
    And user on WCMSLegacyConnection screen selects on storageReservoir value <storageReservoir>
    And user on WCMSLegacyConnection screen selects on waterTreatment value <waterTreatment>
    And user on WCMSLegacyConnection screen types on sumpCapacityDataEntry value 3 random numbers
    And user on WCMSLegacyConnection screen types on sequenceNumber value 3 random numbers
    And user on WCMSLegacyConnection screen types on plumberName value "Plumber ", 4 random characters
    And user on WCMSLegacyConnection screen types on noOfTaps value 2 random numbers
    And user on WCMSLegacyConnection screen types on buildingName value "BuildingName ",2 random numbers
    And user on WCMSLegacyConnection screen types on buildingAddress value "BuildingAddress ",2 random numbers
    And user on WCMSLegacyConnection screen types on roadName value "RoadName ",2 random numbers
    And user on WCMSLegacyConnection screen types on gisNumber value 5 random numbers
    And user on WCMSLegacyConnection screen types on noOfPersonsDataEntry value 2 random numbers

    ### On Create Legacy Connection entering Metered Details ###
    And user on WCMSLegacyConnection screen selects on meteredOwnerShip value <meteredOwnerShip>
    And user on WCMSLegacyConnection screen selects on meterModel value <meterModel>
#    And user on WCMSLegacyConnection screen types on meterMake value Meter Make
    And user on WCMSLegacyConnection screen types on meterNumber value 5 random number
    And user on WCMSLegacyConnection screen types on meterCost value 3 random number
    And user on WCMSLegacyConnection screen types on initialMeterReading value 2 random number
    And user on WCMSLegacyConnection screen types on maxMeterReading value 5 random number

    And user on WCMSLegacyConnection screen types on reading1 value 2 random number
    And user on WCMSLegacyConnection screen types on reading2 value 3 random number
    And user on WCMSLegacyConnection screen types on reading3 value 4 random number

    And user on WCMSLegacyConnection screen types on reading1Date value 23/08/2016
    And user on WCMSLegacyConnection screen types on reading2Date value 23/09/2016
    And user on WCMSLegacyConnection screen types on reading3Date value 23/10/2016

    And user on WCMSLegacyConnection screen selects on gapCode1 value <gapCode1>
    And user on WCMSLegacyConnection screen selects on gapCode2 value <gapCode2>
    And user on WCMSLegacyConnection screen selects on gapCode3 value <gapCode3>

    And user on WCMSLegacyConnection screen types on firstConsumptionAdjustment value 10
    And user on WCMSLegacyConnection screen types on secondConsumptionAdjustment value 20
    And user on WCMSLegacyConnection screen types on thirdConsumptionAdjustment value 30

    And user on WCMSLegacyConnection screen force clicks on firstMonthResetReading
    And user on WCMSLegacyConnection screen force clicks on secondMonthResetReading
    And user on WCMSLegacyConnection screen force clicks on thirdMonthResetReading

    ### On Create Legacy Connection entering Security Deposit details ###
    And user on WCMSLegacyConnection screen types on securityDeposit value 3 random numbers
    And user on WCMSLegacyConnection screen types on receiptNumber value 6 random numbers
    And user on WCMSLegacyConnection screen types on receiptDate value <receiptDate>
    And user on WCMSLegacyConnection screen clicks on text value Create
    And user on WCMSLegacyConnection screen will wait until the page loads
    And user on WCMSLegacyConnection screen scroll to top of the page
    And user on WCMSLegacyConnection screen verifies text has visible value Application Particular
    And user on WCMSLegacyConnection screen copies the dataEntryNumberMetered to dataEntryNumber

    ### On Homepage Screen ###
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Search Connection
    And user on Home screen clicks on firstMenuItem
    And user on Home screen refresh's the webpage

    ### On Search Connection Screen ###
    And user on WCMSLegacyConnection screen verifies text has visible value Search New Connection
    And user on WCMSLegacyConnection screen types on consumerNumber value dataEntryNumber
    And user on WCMSLegacyConnection screen clicks on text value Search
    And user on WCMSLegacyConnection screen verifies text has visible value dataEntryNumber

    ### Logout ###
    And Intent:LogoutIntentTest

    Examples:
      | physicalConnectionDate | usageType | usageSubType | connectionSize | connectionStatus | waterSourceType | storageReservoir | waterTreatment                                            | receiptDate | meteredOwnerShip | meterModel | gapCode1             | gapCode2              | gapCode3                         |
      | 23/08/2016             | Mixed     | Mixed        | 1              | PERMANENT        | RIVER           | Storage          | bhuvaneshwar jal shudhikaran-bhuvaneshwar jal shudhikaran | 23/08/2017  | Citizen          | General    | Last 2 months Lowest | Last 3 months Average | Meter is OK, readings acceptable |


    Scenario Outline: Create Legacy Connection With Property and Connection Type is METERED along with demand

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 1234
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen verifies myTasks has visible value My Tasks
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create Legacy Connection
    And user on Home screen clicks on firstMenuItem

    ### On Create Legacy Connection entering applicant details ###
    And user on WCMSLegacyConnection screen force clicks on withProperty
    And user on WCMSLegacyConnection screen types on ptAssessmentNumber value roh000008262
    And user on WCMSLegacyConnection screen clicks on ptSearch
    And user on WCMSLegacyConnection screen types on oldConsumerNumber1 value 10 random numbers
    And user on WCMSLegacyConnection screen types on manualConsumerNumber1 value 10 random numbers
    And user on WCMSLegacyConnection screen types on physicalConnectionDate1 value <physicalConnectionDate>

    ### On Create Legacy Connection entering connection details ###
    And user on WCMSLegacyConnection screen selects on usageType value <usageType>
    And user on WCMSLegacyConnection screen selects on usageSubType value <usageSubType>
    And user on WCMSLegacyConnection screen selects on connectionSize value <connectionSize>
    And user on WCMSLegacyConnection screen selects on connectionType value METERED
    And user on WCMSLegacyConnection screen selects on connectionStatus value <connectionStatus>
    And user on WCMSLegacyConnection screen selects on waterSourceType value <waterSourceType>
    And user on WCMSLegacyConnection screen selects on storageReservoir value <storageReservoir>
    And user on WCMSLegacyConnection screen selects on waterTreatment value <waterTreatment>
    And user on WCMSLegacyConnection screen types on sumpCapacityDataEntry value 3 random numbers
    And user on WCMSLegacyConnection screen types on sequenceNumber value 3 random numbers
    And user on WCMSLegacyConnection screen types on plumberName value "Plumber ", 4 random characters
    And user on WCMSLegacyConnection screen types on noOfTaps value 2 random numbers
    And user on WCMSLegacyConnection screen types on buildingName value "BuildingName ",2 random numbers
    And user on WCMSLegacyConnection screen types on buildingAddress value "BuildingAddress ",2 random numbers
    And user on WCMSLegacyConnection screen types on roadName value "RoadName ",2 random numbers
    And user on WCMSLegacyConnection screen types on gisNumber value 5 random numbers
    And user on WCMSLegacyConnection screen types on noOfPersonsDataEntry value 2 random numbers

    ### On Create Legacy Connection entering Metered Details ###
    And user on WCMSLegacyConnection screen selects on meteredOwnerShip value <meteredOwnerShip>
    And user on WCMSLegacyConnection screen selects on meterModel value <meterModel>
#    And user on WCMSLegacyConnection screen types on meterMake value Meter Make
    And user on WCMSLegacyConnection screen types on meterNumber value 5 random number
    And user on WCMSLegacyConnection screen types on meterCost value 3 random number
    And user on WCMSLegacyConnection screen types on initialMeterReading value 2 random number
    And user on WCMSLegacyConnection screen types on maxMeterReading value 5 random number

    And user on WCMSLegacyConnection screen types on reading1 value 2 random number
    And user on WCMSLegacyConnection screen types on reading2 value 3 random number
    And user on WCMSLegacyConnection screen types on reading3 value 4 random number

    And user on WCMSLegacyConnection screen types on reading1Date value 23/08/2016
    And user on WCMSLegacyConnection screen types on reading2Date value 23/09/2016
    And user on WCMSLegacyConnection screen types on reading3Date value 23/10/2016

    And user on WCMSLegacyConnection screen selects on gapCode1 value <gapCode1>
    And user on WCMSLegacyConnection screen selects on gapCode2 value <gapCode2>
    And user on WCMSLegacyConnection screen selects on gapCode3 value <gapCode3>

    And user on WCMSLegacyConnection screen types on firstConsumptionAdjustment value 10
    And user on WCMSLegacyConnection screen types on secondConsumptionAdjustment value 20
    And user on WCMSLegacyConnection screen types on thirdConsumptionAdjustment value 30

    And user on WCMSLegacyConnection screen force clicks on firstMonthResetReading
    And user on WCMSLegacyConnection screen force clicks on secondMonthResetReading
    And user on WCMSLegacyConnection screen force clicks on thirdMonthResetReading

    ### On Create Legacy Connection entering Security Deposit details ###
    And user on WCMSLegacyConnection screen types on securityDeposit value 3 random numbers
    And user on WCMSLegacyConnection screen types on receiptNumber value 6 random numbers
    And user on WCMSLegacyConnection screen types on receiptDate value <receiptDate>
    And user on WCMSLegacyConnection screen clicks on text value Create
    And user on WCMSLegacyConnection screen will wait until the page loads
    And user on WCMSLegacyConnection screen scroll to top of the page
    And user on WCMSLegacyConnection screen verifies text has visible value Application Particular
    And user on WCMSLegacyConnection screen copies the dataEntryNumberMetered1 to dataEntryNumber

    ### On Homepage Screen ###
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Search Connection
    And user on Home screen clicks on firstMenuItem
    And user on Home screen refresh's the webpage

    ### On Search Connection Screen ###
    And user on WCMSLegacyConnection screen verifies text has visible value Search New Connection
    And user on WCMSLegacyConnection screen types on consumerNumber value dataEntryNumber
    And user on WCMSLegacyConnection screen clicks on text value Search
    And user on WCMSLegacyConnection screen verifies text has visible value dataEntryNumber

    ### Logout ###
    And Intent:LogoutIntentTest

    Examples:
      | physicalConnectionDate | usageType | usageSubType | connectionSize | connectionStatus | waterSourceType | storageReservoir | waterTreatment                                            | receiptDate | meteredOwnerShip | meterModel | gapCode1             | gapCode2              | gapCode3                         |
      | 23/08/2016             | Mixed     | Mixed        | 1              | PERMANENT        | RIVER           | Storage          | bhuvaneshwar jal shudhikaran-bhuvaneshwar jal shudhikaran | 23/08/2017  | Citizen          | General    | Last 2 months Lowest | Last 3 months Average | Meter is OK, readings acceptable |
