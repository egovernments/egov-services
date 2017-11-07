Feature: Detailed Estimate is prepared by the clerk to cost estimate for executing the work.

  Narration: A detailed estimate is first created based on the parameters of the work to be carried out.
  This is followed by creation of an Abstract estimate

  Pre-Condition: System should have Name of Work,  Estimate no, proceeding order no, admin sanction from date and to date,
  Estimate template code ,SOR code, Unit, Overheads Name, Deductions Name, Deductions Account Head.

  Background:
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value clerk
    And user on Login screen types on password value eGov@123
    And user on Login screen clicks on signIn

  ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen verifies myTasks has visible value My Tasks
    And user on Home screen clicks on menu

  Scenario Outline: Create Detailed Estimate
    Given user on Home screen types on menuSearch value Create Detailed Estimate
    And user on Home screen clicks on firstMenuItem
    And user on CreateDetailedEstimate screen will see the createDetailedEstimate
    And user on CreateDetailedEstimate screen types on nameOfWork value <nameOfWork>
    And user on CreateDetailedEstimate screen types on department value <department>
    And user on CreateDetailedEstimate screen types on estimateNumber value <estimateNumber>
    And user on CreateDetailedEstimate screen types on proceedingOrderNumber value <proceedingOrderNumber>
    And user on CreateDetailedEstimate screen types on adminSanctionFromDate value <adminSanctionFromDate>
    And user on CreateDetailedEstimate screen types on adminSanctionToDate value <adminSanctionToDate>
    And user on CreateDetailedEstimate screen clicks on searchButton
    And user on CreateDetailedEstimate screen selects on estimate

    Examples: Search With Parameters
      | nameOfWork | department  | estimateNumber | proceedingOrderNumber | adminSanctionFromDate | adminSanctionToDate |
      | Estimate   | NA          | NA             | NA                    | NA                    | NA                  |
      | NA         | Engineering | NA             | NA                    | NA                    | NA                  |
      | NA         | NA          | LA Number      | NA                    | NA                    | NA                  |
      | NA         | NA          | NA             | PNumber               | NA                    | NA                  |
      | NA         | NA          | NA             | NA                    | From Date             | NA                  |
      | NA         | NA          | NA             | NA                    | NA                    | To Date             |

