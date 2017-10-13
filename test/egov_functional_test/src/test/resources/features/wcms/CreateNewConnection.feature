Feature: In this feature We are going to create legacy connection with the following requirements

  # Create New Connection Without Property
  # Create New Connection With Property

  Scenario Outline: Create Legacy Connection Without Property

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 1234
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen verifies myTasks has visible value My Tasks
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create New Connection
    And user on Home screen clicks on firstMenuItem

    ### On Create New Connection entering applicant details ###
    And user on WCMSNewConnection screen types on ownerName value "Owner ", 4 random characters
    And user on WCMSNewConnection screen types on mobileNumber value "1",9 random numbers
    And user on WCMSNewConnection screen types on email value random email
    And user on WCMSNewConnection screen types on aadhaarNumber value 12 random numbers
    And user on WCMSNewConnection screen selects on gender value MALE
    And user on WCMSNewConnection screen types on houseNumber value 12345
    And user on WCMSNewConnection screen types on address value Address
    And user on WCMSNewConnection screen types on city value "City ", 4 random characters
    And user on WCMSNewConnection screen types on pin value 6 random numbers
    And user on WCMSNewConnection screen selects on zone value Zone A
    And user on WCMSNewConnection screen selects on ward value Prabhag 1
    And user on WCMSNewConnection screen selects on locality value Andhar Ali

    ### On Create New Connection entering connection details ###
    And user on WCMSNewConnection screen selects on usageType value <usageType>
    And user on WCMSNewConnection screen selects on usageSubType value <usageSubType>
    And user on WCMSNewConnection screen selects on connectionSize value <connectionSize>
    And user on WCMSNewConnection screen selects on connectionType value <connectionType>
    And user on WCMSNewConnection screen selects on connectionStatus value <connectionStatus>
    And user on WCMSNewConnection screen selects on waterSourceType value <waterSourceType>
    And user on WCMSNewConnection screen selects on storageReservoir value <storageReservoir>
    And user on WCMSNewConnection screen selects on waterTreatment value <waterTreatment>
    And user on WCMSNewConnection screen types on sumpCapacityDataEntry value 3 random numbers
    And user on WCMSNewConnection screen types on sequenceNumber value 3 random numbers
    And user on WCMSNewConnection screen types on plumberName value "Plumber ", 4 random characters
    And user on WCMSNewConnection screen types on noOfTaps value 2 random numbers
    And user on WCMSNewConnection screen types on buildingName value "BuildingName ",2 random numbers
    And user on WCMSNewConnection screen types on buildingAddress value "BuildingAddress ",2 random numbers
    And user on WCMSNewConnection screen types on roadName value "RoadName ",2 random numbers
    And user on WCMSNewConnection screen types on gisNumber value 5 random numbers
    And user on WCMSNewConnection screen types on noOfPersonsDataEntry value 2 random numbers

    ### On Create New Connection entering approval details ###
    And user on WCMSNewConnection screen selects on approverDepartment value Water Department
    And user on WCMSNewConnection screen selects on approverDesignation value Junior Engineer
    And user on WCMSNewConnection screen selects on approverAssignee value Shohra
    And user on WCMSNewConnection screen clicks on text value Create


    ### On View Screen ###
    And user on WCMSNewConnection screen will wait until the page loads
    And user on WCMSNewConnection screen scroll to the ackNumber
    And user on WCMSNewConnection screen verifies text has visible value Application Particular
    And user on WCMSNewConnection screen copies the ackNumber to ackNumber

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Search Connection
    And user on Home screen clicks on firstMenuItem

    ### On Search Connection Screen ###
    And user on WCMSNewConnection screen will wait until the page loads
    And user on WCMSNewConnection screen refresh's the webpage
    And user on WCMSNewConnection screen verifies text has visible value Search New Connection
    And user on WCMSNewConnection screen types on ackNumberSearch value ackNumber
    And user on WCMSNewConnection screen clicks on text value Search
    And user on WCMSNewConnection screen verifies text has visible value ackNumber

    ### Logout ###
    And Intent:LogoutIntentTest

    Examples:
      | usageType | usageSubType | connectionSize | connectionType | connectionStatus | waterSourceType | storageReservoir           | waterTreatment               |
      | Mixed     | Mixed        | 0.5            | NONMETERED     | PERMANENT        | RIVER           | Elevates Storage Reservoir | bhuvaneshwar jal shudhikaran |
      | Mixed     | Mixed        | 1              | METERED        | PERMANENT        | RIVER           | Storage          | bhuvaneshwar jal shudhikaran |

  Scenario Outline: Create Legacy Connection With Property

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value 1234
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen verifies myTasks has visible value My Tasks
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create New Connection
    And user on Home screen clicks on firstMenuItem

    ### On Create New Connection entering applicant details ###
    And user on WCMSNewConnection screen forceClicks on withProperty
    And user on WCMSNewConnection screen types on ptAssessmentNumber value roh000008262
    And user on WCMSNewConnection screen clicks on ptSearch

    ### On Create New Connection entering connection details ###
    And user on WCMSNewConnection screen selects on usageType value <usageType>
    And user on WCMSNewConnection screen selects on usageSubType value <usageSubType>
    And user on WCMSNewConnection screen selects on connectionSize value <connectionSize>
    And user on WCMSNewConnection screen selects on connectionType value <connectionType>
    And user on WCMSNewConnection screen selects on connectionStatus value <connectionStatus>
    And user on WCMSNewConnection screen selects on waterSourceType value <waterSourceType>
    And user on WCMSNewConnection screen selects on storageReservoir value <storageReservoir>
    And user on WCMSNewConnection screen selects on waterTreatment value <waterTreatment>
    And user on WCMSNewConnection screen types on sumpCapacityDataEntry value 3 random numbers
    And user on WCMSNewConnection screen types on sequenceNumber value 3 random numbers
    And user on WCMSNewConnection screen types on plumberName value "Plumber ", 4 random characters
    And user on WCMSNewConnection screen types on noOfTaps value 2 random numbers
    And user on WCMSNewConnection screen types on buildingName value "BuildingName ",2 random numbers
    And user on WCMSNewConnection screen types on buildingAddress value "BuildingAddress ",2 random numbers
    And user on WCMSNewConnection screen types on roadName value "RoadName ",2 random numbers
    And user on WCMSNewConnection screen types on gisNumber value 5 random numbers
    And user on WCMSNewConnection screen types on noOfPersonsDataEntry value 2 random numbers

    ### On Create New Connection entering approver details ###
    And user on WCMSNewConnection screen selects on approverDepartment value Water Department
    And user on WCMSNewConnection screen selects on approverDesignation value Water Supply And Sanitation Engineer
    And user on WCMSNewConnection screen selects on approverAssignee value Vinay
    And user on WCMSNewConnection screen clicks on text value Create

    ### On View Screen ###
    And user on WCMSNewConnection screen will wait until the page loads
    And user on WCMSNewConnection screen scroll to the topOfThePage
    And user on WCMSNewConnection screen verifies text has visible value Application Particular
    And user on WCMSNewConnection screen copies the ackNumberWithProperty to ackNumberWithProperty

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Search Connection
    And user on Home screen clicks on firstMenuItem

    ### On Search Connection Screen ###
    And user on WCMSNewConnection screen will wait until the page loads
    And user on WCMSNewConnection screen refresh's the webpage
    And user on WCMSNewConnection screen verifies text has visible value Search New Connection
    And user on WCMSNewConnection screen types on ackNumberSearch value ackNumberWithProperty
    And user on WCMSNewConnection screen clicks on text value Search
    And user on WCMSNewConnection screen verifies text has visible value ackNumberWithProperty

    ### Logout ###
    And Intent:LogoutIntentTest

    Examples:
      | usageType | usageSubType | connectionSize | connectionType | connectionStatus | waterSourceType | storageReservoir | waterTreatment               |
      | Mixed     | Mixed        | 1              | NONMETERED     | PERMANENT        | RIVER           | Storage          | bhuvaneshwar jal shudhikaran |
      | Mixed     | Mixed        | 1              | METERED        | PERMANENT        | RIVER           | Storage          | bhuvaneshwar jal shudhikaran |