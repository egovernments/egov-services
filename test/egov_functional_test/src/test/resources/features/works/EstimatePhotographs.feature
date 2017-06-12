Feature: Upload/View estimate photographs
  As a registered user of the system
  I am able to upload and view photos of estimate progress

  @Works
  Scenario: Upload/View estimate photographs

    Given assis_Engineer logs in
    And user will select the required screen as "Upload Estimate Photographs"
    And he search for estimate in estimate search result
    And he upload the estimate photos for physical progress track
    And he close the acknowledgement page
    And current user logs out