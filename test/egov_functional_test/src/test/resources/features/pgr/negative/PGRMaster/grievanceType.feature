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
