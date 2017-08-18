Feature: Trade License

  Scenario: CategoryMaster

    Given Intent: LoginIntentTest
    And user on home screen clicks on menu
    And user on home screen type on applicationSearchBox value Create Category
    And user on home screen click on ApplicationLink

    #CREATE CATEGORY

    And user on createcategory screen type on code value CODE001
    And user on createcategory screen type on name value eGov
    And user on createcategory screen clicks on active

    #Modify Category

    Given Intent:LoginIntentTest
    And user on home screen clicks on menu
    And user on home screen type on applicationSearchBox value Modify Category
    And user on home screen click on ApplicationLink

    And user on modifycategory screen type on code value CODE001
    And user on modifycategory screen type on name value eGov
    And user on modifycategory screen clicks on active
    And user on modifycategory screen clicks on submit

    #View Category

    Given Intent:LoginIntentTest
    And user on home screen clicks on menu
    And user on home screen type on applicationSearchBox value View Category
    And user on home screen click on ApplicationLink

    And user on viewcategory screen type on code value CODE001
    And user on viewcategory screen type on name value eGov
    And user on viewcategory screen clicks on active





