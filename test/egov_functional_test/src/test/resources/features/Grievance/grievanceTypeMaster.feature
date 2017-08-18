
Feature: Grievance Type Creation

  Scenario: Create Grievance Type with valid details

    Given grievanceAdmin on Login screen types on username value narasappa
    And grievanceAdmin on Login screen types on password value demo
    And grievanceAdmin on Login screen clicks on signIn
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value Create Grievance Type
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on createGrievanceType screen types on grievanceTypeName value Unneccessary Traffic Fines
    And grievanceAdmin on createGrievanceType screen types on grievanceTypeCode value UTF
    And grievanceAdmin on createGrievanceType screen types on grievanceTypeDescription value Unneccessary Traffic Fines
    And grievanceAdmin on createGrievanceType screen types on grievanceTypeSLAHours value 1
    And grievanceAdmin on createGrievanceType screen selects grievanceCategory with value as Bangalore City Problems
    And grievanceAdmin on createGrievanceType screen clicks on Create
    And grievanceAdmin on createGrievanceType screen verifies successMSG has visible value Success
    And grievanceAdmin on createGrievanceType screen clicks on closeButton
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value View Grievance Type
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on viewGrievanceType screen types on searchGrievanceTypeBox value Unneccessary Traffic Fines
    And grievanceAdmin on viewGrievanceType screen clicks on grievanceTypeList
    And grievanceAdmin on viewGrievanceType screen verifies name has visible value Unneccessary Traffic Fines
    And grievanceAdmin on viewGrievanceType screen verifies code has visible value UTF
    And grievanceAdmin on viewGrievanceType screen verifies SLAHours has visible value 1
    And grievanceAdmin on viewGrievanceType screen verifies active has visible value Yes
    And grievanceAdmin on viewGrievanceType screen verifies hasFinancialImpact has visible value No
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value Officials Register Grievance
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on Grievance screen clicks on grievanceCategory
    And grievanceAdmin on Grievance screen clicks on visible value Bangalore City Problems
    And grievanceAdmin on Grievance screen clicks on grievanceType
    And grievanceAdmin on Grievance screen verifies grievanceType has visible value Unneccessary Traffic Fines
    And Intent:LogoutIntentTest

  Scenario: Create Grievance Type with already existing name

    Given grievanceAdmin on Login screen types on username value narasappa
    And grievanceAdmin on Login screen types on password value demo
    And grievanceAdmin on Login screen clicks on signIn
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value Create Grievance Type
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on createGrievanceType screen types on grievanceTypeName value Unneccessary Traffic Fines
    And grievanceAdmin on createGrievanceType screen types on grievanceTypeCode value UTFS
    And grievanceAdmin on createGrievanceType screen types on grievanceTypeDescription value Unneccessary Traffic Fines
    And grievanceAdmin on createGrievanceType screen types on grievanceTypeSLAHours value 1
    And grievanceAdmin on createGrievanceType screen selects grievanceCategory with value as Bangalore City Problems
    And grievanceAdmin on createGrievanceType screen clicks on Create
    And grievanceAdmin on createGrievanceType screen verifies validationMSG has visible value Grievance Type Name already exists
    And Intent:LogoutIntentTest


  Scenario: Create Grievance Type with already existing code
    Given grievanceAdmin on Login screen types on username value narasappa
    And grievanceAdmin on Login screen types on password value demo
    And grievanceAdmin on Login screen clicks on signIn
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value Create Grievance Type
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on createGrievanceType screen types on grievanceTypeName value Unneccessary Traffic Fines in Bangalore
    And grievanceAdmin on createGrievanceType screen types on grievanceTypeCode value UTF
    And grievanceAdmin on createGrievanceType screen types on grievanceTypeDescription value Unneccessary Traffic Fines
    And grievanceAdmin on createGrievanceType screen types on grievanceTypeSLAHours value 1
    And grievanceAdmin on createGrievanceType screen selects grievanceCategory with value as Bangalore City Problems
    And grievanceAdmin on createGrievanceType screen clicks on Create
    And grievanceAdmin on createGrievanceType screen verifies validationMSG has visible value Grievance Type Code already exists
    And Intent:LogoutIntentTest


  Scenario:  Create Grievance Type as inactive
    Given grievanceAdmin on Login screen types on username value narasappa
    And grievanceAdmin on Login screen types on password value demo
    And grievanceAdmin on Login screen clicks on signIn
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value Create Grievance Type
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on createGrievanceType screen types on grievanceTypeName value Drainage overflow
    And grievanceAdmin on createGrievanceType screen types on grievanceTypeCode value DO
    And grievanceAdmin on createGrievanceType screen types on grievanceTypeDescription value Drainage overflow
    And grievanceAdmin on createGrievanceType screen clicks on activeCheckBox
    And grievanceAdmin on createGrievanceType screen types on grievanceTypeSLAHours value 1
    And grievanceAdmin on createGrievanceType screen selects grievanceCategory with value as Bangalore City Problems
    And grievanceAdmin on createGrievanceType screen clicks on Create
    And grievanceAdmin on createGrievanceType screen verifies successMSG has visible value Success
    And grievanceAdmin on createGrievanceType screen clicks on closeButton
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value View Grievance Type
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on viewGrievanceType screen types on searchGrievanceTypeBox value Drainage overflow
    And grievanceAdmin on viewGrievanceType screen clicks on grievanceTypeList
    And grievanceAdmin on viewGrievanceType screen verifies name has visible value Drainage overflow
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value Officials Register Grievance
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on Grievance screen clicks on grievanceCategory
    And grievanceAdmin on Grievance screen clicks on visible value Bangalore City Problems
    And grievanceAdmin on Grievance screen clicks on grievanceType
    And grievanceAdmin on Grievance screen verifies grievanceType has notvisible value Drainage overflow
    And Intent:LogoutIntentTest

  Scenario: Create Grievance Type with already existing code and name

    Given grievanceAdmin on Login screen types on username value narasappa
    And grievanceAdmin on Login screen types on password value demo
    And grievanceAdmin on Login screen clicks on signIn
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value Create Grievance Type
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on createGrievanceType screen types on grievanceTypeName value Unneccessary Traffic Fines
    And grievanceAdmin on createGrievanceType screen types on grievanceTypeCode value UTF
    And grievanceAdmin on createGrievanceType screen types on grievanceTypeDescription value Unneccessary Traffic Fines
    And grievanceAdmin on createGrievanceType screen types on grievanceTypeSLAHours value 1
    And grievanceAdmin on createGrievanceType screen selects grievanceCategory with value as Bangalore City Problems
    And grievanceAdmin on createGrievanceType screen clicks on Create
    And grievanceAdmin on createGrievanceType screen verifies validationMSG has visible value Combination of code and name already exists
    And Intent:LogoutIntentTest


  Scenario: Create Grievance Type screen field validation

    Given grievanceAdmin on Login screen types on username value narasappa
    And grievanceAdmin on Login screen types on password value demo
    And grievanceAdmin on Login screen clicks on signIn
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value Create Grievance Type
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on createGrievanceType screen types on grievanceTypeName value @
    And grievanceAdmin on createGrievanceType screen verifies nameValidationMSG has visible value Please use only alphabets, space and special characters
    And grievanceAdmin on createGrievanceType screen types on grievanceTypeCode value u
    And grievanceAdmin on createGrievanceType screen verifies codeValidationMSG has visible value Please use only upper case alphabets and numbers
    And grievanceAdmin on createGrievanceType screen types on grievanceTypeDescription value Unneccessary Traffic Fines
    And grievanceAdmin on createGrievanceType screen types on grievanceTypeSLAHours value a
    And grievanceAdmin on createGrievanceType screen verifies SLAHoursValidationMSG has visible value Please use only numbers
    And Intent:LogoutIntentTest


  Scenario: Modify Grievance Type with valid data for name, SLAHours and Description

    Given grievanceAdmin on Login screen types on username value narasappa
    And grievanceAdmin on Login screen types on password value demo
    And grievanceAdmin on Login screen clicks on signIn
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value Update Grievance Type
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on updateGrievanceType screen types on grievanceTypeName value Unneccessary Traffic Fines by police
    And grievanceAdmin on updateGrievanceType screen types on grievanceTypeDescription value Unneccessary Traffic Fines by police
    And grievanceAdmin on updateGrievanceType screen types on grievanceTypeSLAHours value 2
    And grievanceAdmin on updateGrievanceType screen selects grievanceCategory with value as Bangalore City Problems
    And grievanceAdmin on updateGrievanceType screen clicks on Update
    And grievanceAdmin on updateGrievanceType screen verifies successMSG has visible value Success
    And grievanceAdmin on updateGrievanceType screen clicks on closeButton
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value View Grievance Type
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on viewGrievanceType screen types on searchGrievanceTypeBox value Unneccessary Traffic Fines by police
    And grievanceAdmin on viewGrievanceType screen clicks on grievanceTypeList
    And grievanceAdmin on viewGrievanceType screen verifies name has visible value Unneccessary Traffic Fines by police
    And grievanceAdmin on viewGrievanceType screen verifies description has visible value Unneccessary Traffic Fines by police
    And grievanceAdmin on viewGrievanceType screen verifies SLAHours has visible value 2
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value Officials Register Grievance
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on Grievance screen clicks on grievanceCategory
    And grievanceAdmin on Grievance screen clicks on visible value Bangalore City Problems
    And grievanceAdmin on Grievance screen clicks on grievanceType
    And grievanceAdmin on Grievance screen verifies grievanceType has visible value Unneccessary Traffic Fines by police
    And Intent:LogoutIntentTest


  Scenario:  Modify Grievance Type to make type inactive

    Given grievanceAdmin on Login screen types on username value narasappa
    And grievanceAdmin on Login screen types on password value demo
    And grievanceAdmin on Login screen clicks on signIn
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value Update Grievance Type
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on updateGrievanceType screen clicks on activeCheckBox
    And grievanceAdmin on updateGrievanceType screen clicks on Update
    And grievanceAdmin on updateGrievanceType screen verifies successMSG has visible value Success
    And grievanceAdmin on updateGrievanceType screen clicks on closeButton
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value View Grievance Type
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on viewGrievanceType screen types on searchGrievanceTypeBox value Unneccessary Traffic Fines
    And grievanceAdmin on viewGrievanceType screen clicks on grievanceTypeList
    And grievanceAdmin on viewGrievanceType screen verifies active has visible value NO
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value Officials Register Grievance
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on Grievance screen clicks on grievanceCategory
    And grievanceAdmin on Grievance screen clicks on visible value Bangalore City Problems
    And grievanceAdmin on Grievance screen clicks on grievanceType
    And grievanceAdmin on Grievance screen verifies grievanceType has notvisible value Unneccessary Traffic Fines by police
    And Intent:LogoutIntentTest


  Scenario: Modify Grievance Type with already existing name

    Given grievanceAdmin on Login screen types on username value narasappa
    And grievanceAdmin on Login screen types on password value demo
    And grievanceAdmin on Login screen clicks on signIn
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value Update Grievance Type
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on updateGrievanceType screen types on grievanceTypeName value Unneccessary Traffic Fines by police
    And grievanceAdmin on updateGrievanceType screen types on grievanceTypeDescription value Unneccessary Traffic Fines by police
    And grievanceAdmin on updateGrievanceType screen clicks on Update
    And grievanceAdmin on updateGrievanceType screen verifies validationMSG has visible value Grievance Type Name already exists
    And Intent:LogoutIntentTest

  Scenario: Modify Grievance Type to make Has financial impact Yes

    Given grievanceAdmin on Login screen types on username value narasappa
    And grievanceAdmin on Login screen types on password value demo
    And grievanceAdmin on Login screen clicks on signIn
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value Update Grievance Type
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on updateGrievanceType screen clicks on hasFinancialImpact
    And grievanceAdmin on updateGrievanceType screen clicks on Update
    And grievanceAdmin on updateGrievanceType screen verifies successMSG has visible value Success
    And grievanceAdmin on updateGrievanceType screen clicks on closeButton
    And grievanceAdmin on home screen clicks on menu
    And grievanceAdmin on home screen types on applicationSearchBox value View Grievance Type
    And grievanceAdmin on home screen clicks on applicationLink
    And grievanceAdmin on viewGrievanceType screen types on searchGrievanceTypeBox value Unneccessary Traffic Fines
    And grievanceAdmin on viewGrievanceType screen clicks on grievanceTypeList
    And grievanceAdmin on viewGrievanceType screen verifies hasFinancialImpact has visible value Yes
    And Intent:LogoutIntentTest

