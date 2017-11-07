Feature: Create, Search, and Update of Asset Category Masters of Asset Management

  Narration: This master will create Asset category and Asset sub-category.
  This will required as Pre-Request:
  Asset Sub Category Name, Asset Category Type, Asset Category Name,Depreciation Method,
  Unit of Measurement, Is Depreciation applicable? , Depreciation rate (%).

  Background:
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value superAdmin
    And user on Login screen types on password value eGov@123
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen verifies myTasks has visible value My Tasks
    And user on Home screen clicks on menu

  Scenario Outline: Create Asset Category
    Given user on Home screen types on menuSearch value Create Asset Category
    And user on Home screen clicks on firstMenuItem
    And user on CreateAssetCategory screen will see the createAssetCategory
    And user on CreateAssetCategory screen selects on assetCategoryType value as <assetCategoryType>
    And user on CreateAssetCategory screen types on assetCategoryName value as <assetCategoryName>
    And user on CreateAssetCategory screen types on isDepreciationAllowed value as <isDepreciationAllowed>
    And user on CreateAssetCategory screen types on assetSubCategoryNames value as <assetSubCategoryNames>
    And user on CreateAssetCategory screen types on depreciationRatio value as <depreciationRatio>
    And user on CreateAssetCategory screen types on depreciationMethod value as <depreciationMethod>

    #Additional Fields Required as per sub asset type#
    And user on CreateAssetCategory screen clicks on addAssetSubTypeFields
    And user on CreateAssetCategory screen performs following actions
      | types   | name      | <filedName> |
      | selects | dataType  | <dataType>  |
      | types   | mandatory | <mandatory> |
      | types   | active    | <active>    |
      | types   | order     | <oder>      |
      | types   | value     | <value>     |
    And user on CreateAssetCategory screen clicks on addButton
    And user on CreateAssetCategory screen clicks on createButton

    Examples:
      | assetCategoryType | assetCategoryName     | isDepreciationAllowed | assetSubCategoryNames   | depreciationRatio | depreciationMethod   |
      | Land              | Land                  | NO                    | Open Land               | NA                | NA                   |
      | Land              | Land                  | NO                    | Play ground             | NA                | NA                   |
      | Land              | Land                  | NO                    | Open Market             | NA                | NA                   |
      | Land              | Land                  | NO                    | Dumping Ground          | NA                | NA                   |
      | Land              | Land                  | NO                    | Lakes and ponds         | NA                | NA                   |
      | Land              | Land                  | NO                    | Open Space              | NA                | NA                   |
      | Land              | Land                  | NO                    | Land with Encroachments | NA                | NA                   |
      | Land              | Land                  | NO                    | Open Theatre            | NA                | NA                   |
      | Immovable Assets  | Building              | YES                   | Office Building         | 6.66              | Straight Line Method |
      | Immovable Assets  | Building              | YES                   | School                  | 6.66              | Straight Line Method |
      | Immovable Assets  | Building              | YES                   | Fire Station            | 6.66              | Straight Line Method |
      | Immovable Assets  | Building              | YES                   | Gym                     | 6.66              | Straight Line Method |
      | Immovable Assets  | Building              | YES                   | Library                 | 6.66              | Straight Line Method |
      | Immovable Assets  | Building              | YES                   | Hall                    | 6.66              | Straight Line Method |
      | Immovable Assets  | Building              | YES                   | Hospitals               | 6.66              | Straight Line Method |
      | Immovable Assets  | Infrastructure assets | YES                   | Roads and Bridges       | 6.66              | Straight Line Method |
      | Immovable Assets  | Infrastructure assets | YES                   | Sewerage and Drainage   | 6.66              | Straight Line Method |
      | Immovable Assets  | Infrastructure assets | YES                   | Water Ways              | 6.66              | Straight Line Method |
      | Immovable Assets  | Plant and Machinery   | YES                   | Water Treatment Plant   | 10                | Straight Line Method |
      | Immovable Assets  | Plant and Machinery   | YES                   | Generator Plant         | 10                | Straight Line Method |
      | Immovable Assets  | Plant and Machinery   | YES                   | Public Lighting         | 15                | Straight Line Method |
      | Immovable Assets  | Plant and Machinery   | YES                   | Public Lighting         | 15                | Straight Line Method |

  Scenario Outline: Search/Update Asset Category
    #Search Asset Category#
    Given user on Home screen types on menuSearch value Search Asset Category
    And user on Home screen clicks on firstMenuItem
    And user on SearchAssetCategory screen will see the searchAssetCategory
    And user on SearchAssetCategory screen types on assetCategoryName value <assetCategoryName>
    And user on SearchAssetCategory screen selects on assetCategoryType value <assetCategoryType>
    And user on SearchAssetCategory screen clicks on searchButton

    #Update Asset Category
    And user on UpdateAssetCategory screen clicks on searchResult
    And user on UpdateAssetCategory screen selects on assetCategoryType value as <assetCategoryType>
    And user on UpdateAssetCategory screen types on assetCategoryName value as <assetCategoryName>
    And user on UpdateAssetCategory screen types on isDepreciationAllowed value as <isDepreciationAllowed>
    And user on UpdateAssetCategory screen types on assetSubCategoryNames value as <assetSubCategoryNames>
    And user on UpdateAssetCategory screen types on depreciationRatio value as <depreciationRatio>
    And user on UpdateAssetCategory screen types on depreciationMethod value as <depreciationMethod>
        #Additional Fields Required as per sub asset type#
    And user on UpdateAssetCategory screen clicks on addAssetSubTypeFields
    And user on UpdateAssetCategory screen performs following actions
      | types   | name      | <filedName> |
      | selects | dataType  | <dataType>  |
      | types   | mandatory | <mandatory> |
      | types   | active    | <active>    |
      | types   | order     | <oder>      |
      | types   | value     | <value>     |
    And user on UpdateAssetCategory screen clicks on addButton
    And user on UpdateAssetCategory screen clicks on updateButton

    Examples:
      | assetCategoryName | assetCategoryType     | isDepreciationAllowed | assetSubCategoryNames   | depreciationRatio | depreciationMethod   |
      | Land              | Land                  | NO                    | Open Land               | NA                | NA                   |
      | Land              | Land                  | NO                    | Play ground             | NA                | NA                   |
      | Land              | Land                  | NO                    | Open Market             | NA                | NA                   |
      | Land              | Land                  | NO                    | Dumping Ground          | NA                | NA                   |
      | Land              | Land                  | NO                    | Lakes and ponds         | NA                | NA                   |
      | Land              | Land                  | NO                    | Open Space              | NA                | NA                   |
      | Land              | Land                  | NO                    | Land with Encroachments | NA                | NA                   |
      | Land              | Land                  | NO                    | Open Theatre            | NA                | NA                   |
      | Immovable Assets  | Building              | YES                   | Office Building         | 6.66              | Straight Line Method |
      | Immovable Assets  | Building              | YES                   | School                  | 6.66              | Straight Line Method |
      | Immovable Assets  | Building              | YES                   | Fire Station            | 6.66              | Straight Line Method |
      | Immovable Assets  | Building              | YES                   | Gym                     | 6.66              | Straight Line Method |
      | Immovable Assets  | Building              | YES                   | Library                 | 6.66              | Straight Line Method |
      | Immovable Assets  | Building              | YES                   | Hall                    | 6.66              | Straight Line Method |
      | Immovable Assets  | Building              | YES                   | Hospitals               | 6.66              | Straight Line Method |
      | Immovable Assets  | Infrastructure assets | YES                   | Roads and Bridges       | 6.66              | Straight Line Method |
      | Immovable Assets  | Infrastructure assets | YES                   | Sewerage and Drainage   | 6.66              | Straight Line Method |
      | Immovable Assets  | Infrastructure assets | YES                   | Water Ways              | 6.66              | Straight Line Method |
      | Immovable Assets  | Plant and Machinery   | YES                   | Water Treatment Plant   | 10                | Straight Line Method |
      | Immovable Assets  | Plant and Machinery   | YES                   | Generator Plant         | 10                | Straight Line Method |
      | Immovable Assets  | Plant and Machinery   | YES                   | Public Lighting         | 15                | Straight Line Method |
      | Immovable Assets  | Plant and Machinery   | YES                   | Public Lighting         | 15                | Straight Line Method |