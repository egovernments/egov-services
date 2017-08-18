Feature: Generic Feature

  @Custom
  Scenario Outline: Login
    Given Intent:LoginIntentTest
    And user on Home screen clicks on menu
    And user on Home screen types on search value Create New Connection
    And user on Home screen clicks on firstMenuItem
    And user on WaterConnection screen performs following actions
      | clicks | propertyType         |                  |
      | clicks | propertyTypeDropdown | Private          |
      | types  | sumpCapacity         | <sumpCapacity>   |
      | types  | noOfPersons          | --2 digit number |
#    And user on WaterConnection screen types on noOfPersons value --1 digit number

    Examples:
      | sumpCapacity |
      | 100          |

  @Custom
  Scenario: LoginSample
    Given Intent:LoginIntentTestSample
      | narasappa | demo |
