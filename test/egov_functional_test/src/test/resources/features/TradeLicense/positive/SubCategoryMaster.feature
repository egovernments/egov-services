Feature: Trade License

  Scenario: SubCategoryMaster

    Given Intent: LoginIntentTest
    And user on home screen clicks on menu
    And user on home screen type on applicationSearchBox value Create Sub Category
    And user on home screen click on ApplicationLink

    #CreateSubCategory

    And user on subcategorymaster screen select on category value eGov
    And user on subcategorymaster screen type on name value NewTrade
    And user on subcategorymaster screen type on code value E0045
    And user on subcategorymaster screen clicks on Active
    And user on subcategorymaster screen type on validityyears value 5
    And user on subcategorymaster screen clicks on create

    #ViewSubCategory

#   And user on subcategorymaster screen displays on categoryname value as readonly
#   And user on subcategorymaster screen displays on subcategorycode value as readyonly
#   And user on subcategorymaster screen displays on subcategoryname value as readonly
    And user on subcategorymaster screen clicks on active
    And user on subcategorymaster screen clicks on search

    #ModifySubCateogry

#   And user on subcategorymaster screen displays on category value as readyonly
    And user on subcategorymaster screen type on name value eGov
#   And user on subcategorymaster screen displays on code value as readonly
    And user on subcategorymaster screen clicks on active
    And user on subcategorymaster screen type on validityyear value 5




