Feature: Create/Search for assetCategory

  As a registered user of the system
  I should able to create/search assetCategory

  Scenario Outline: Create assetCategory

    Given assistant logs in
    And user will select the required screen as "Create asset category"
    And user will enter the details of asset category as <assetCategory>
    And user will enter the details of custom fields as <customFields>
    And user create the asset category

    Examples:
      | assetCategory    | customFields         |
      | categoryDetails1 | 7                    |