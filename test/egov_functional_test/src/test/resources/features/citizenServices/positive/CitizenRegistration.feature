Feature: Citizen Registration into the Citizen Service Portal

  ##Positive Scenario for Citizen Registration##
  Scenario: Citizen Registration with valid details in mandatory fields
    Given user on login screen clicks on CreateAnAccountLink
#    And user switch to CreateAnAccount screen
    And user on CreateAnAccount screen types on mobileNumber value 9876543210
    And user on CreateAnAccount screen types on password value eGov@123
    And user on CreateAnAccount screen types on confirmPassword value eGov@123
    And user on CreateAnAccount screen types on fullName value Akhila GD
    And user on CreateAnAccount screen clicks on generateOTP
    And user on CreateAnAccount screen types on OTP value OTP received to mobile number
    And user on CreateAnAccount screen clicks on signUp
    And user on CreateAnAccount screen verifies successMSg has visible value Your account created successfully
    And user on CreateAnAccount screen clicks on OK

  Scenario: Registered citizen logging into the account
    Given user on login screen types on username value 9876543210
    And user on login screen types on password value eGov@123
    And user on login screen clicks on signIn
    And user on dashboard screen verifies inbox has visible value MY REQUEST

  ##Field level Validations for Citizen Registration Page##
  Scenario: Citizen Registration with valid details in mandatory fields and non mandatory fields
    Given user on login screen clicks on CreateAnAccountLink
#    And user switch to CreateAnAccount screen
    And user on CreateAnAccount screen types on mobileNumber value 9876543210
    And user on CreateAnAccount screen types on password value eGov@123
    And user on CreateAnAccount screen types on confirmPassword value eGov@123
    And user on CreateAnAccount screen types on fullName value Akhila GD
    And user on CreateAnAccount screen types on emailAddress value abc@xyz.com
    And user on CreateAnAccount screen clicks on generateOTP
    And user on CreateAnAccount screen types on OTP value OTP received to mobile number
    And user on CreateAnAccount screen clicks on signUp
    And user on CreateAnAccount screen verifies successMSg has visible value Your account created successfully
    And user on CreateAnAccount screen clicks on OK

  Scenario: Citizen Registration page field validations
    Given user on login screen clicks on CreateAnAccountLink
#    And user switch to CreateAnAccount screen
    And user on CreateAnAccount screen types on mobileNumber value 987654321
    And user on CreateAnAccount screen verifies validationMSGforMobNum has visible value Enter valid 10 digit mobile number
    And user on CreateAnAccount screen types on password value demo
    And user on CreateAnAccount screen verifies validationMSGforPassword has visible value Should contain upper and lower case alphabet, number and special character. Please use between 8 and 20 characters.
    And user on CreateAnAccount screen types on password value eGov@123
    And user on CreateAnAccount screen types on confirmPassword value demo
    And user on CreateAnAccount screen verifies validationMSGforConfirmPassword has visible value Password should match
    And user on CreateAnAccount screen types on fullName value 1234
    And user on CreateAnAccount screen verifies validationMSGforName has visible value Should contain only alphabets and space. Max: 50 Characters
    And user on CreateAnAccount screen types on emailAddress value abc@xyz
    And user on CreateAnAccount screen verifies validationMSGforEmail has visible value Enter valid email ID
    And user on CreateAnAccount screen verifies generateOTP is invisible

  Scenario:  Citizen Registration with already existing mobile number
    Given user on login screen clicks on CreateAnAccountLink
#    And user switch to CreateAnAccount screen
    And user on CreateAnAccount screen types on mobileNumber value 9876543210
    And user on CreateAnAccount screen types on password value eGov@123
    And user on CreateAnAccount screen types on confirmPassword value eGov@123
    And user on CreateAnAccount screen types on fullName value Akhila GD
    And user on CreateAnAccount screen clicks on generateOTP
    And user on CreateAnAccount screen types on OTP value OTP received to mobile number
    And user on CreateAnAccount screen clicks on signUp
    And user on CreateAnAccount screen verifies successMSg has visible value User with Entered Mobile Number is already Registered
    And user on CreateAnAccount screen clicks on ok

  Scenario:  Citizen Registration with OTP received on mobile number which is incorrect/expired
    Given user on login screen clicks on CreateAnAccountLink
#    And user switch to CreateAnAccount screen
    And user on CreateAnAccount screen types on mobileNumber value 9876543210
    And user on CreateAnAccount screen types on password value eGov@123
    And user on CreateAnAccount screen types on confirmPassword value eGov@123
    And user on CreateAnAccount screen types on fullName value Akhila GD
    And user on CreateAnAccount screen clicks on generateOTP
    And user on CreateAnAccount screen types on OTP value OTP received to mobile number with 2 minute delay
    And user on CreateAnAccount screen clicks on signUp
    And user on CreateAnAccount screen verifies successMSg has visible value OTP validation unsuccessful
    And user on CreateAnAccount screen clicks on ok

    Given user on login screen clicks on CreateAnAccountLink
#    And user switch to CreateAnAccount screen
    And user on CreateAnAccount screen types on mobileNumber value 9876543210
    And user on CreateAnAccount screen types on password value eGov@123
    And user on CreateAnAccount screen types on confirmPassword value eGov@123
    And user on CreateAnAccount screen types on fullName value Akhila GD
    And user on CreateAnAccount screen clicks on generateOTP
    And user on CreateAnAccount screen types on OTP value Invalid OTP
    And user on CreateAnAccount screen clicks on signUp
    And user on CreateAnAccount screen verifies successMSg has visible value OTP validation unsuccessful
    And user on CreateAnAccount screen clicks on ok