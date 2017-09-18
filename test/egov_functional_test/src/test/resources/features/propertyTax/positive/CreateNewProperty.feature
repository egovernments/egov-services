Feature: Property Tax

  Scenario: CreateNewProperty

    Given Intent: LoginIntent
    And user on home screen clicks on menu
    And user on home screen type on applicationSearchBox value Create New Property
    And user on home screen click on ApplicationLink

    #OWNER DETAILS
    And user on createNewProperty screen type on aadhaarNumber value 123456789012
    And user on createnewproperty screen type on mobilenumber value 9876789543
    And user on createnewproperty screen type on ownername value bimal
    And user on createnewproperty screen select on gender value Male
    And user on createnewproperty screen type on emailaddress value bimal@gmail.com
    And user on createnewproperty screen select on guardianrelation value Father
    And user on createnewproperty screen type on guardian value Blenka
    And user on createnewproperty screen clicks on yes
    And user on createnewproperty screen clicks on No
    And user on createnewproperty screen select on ownertype value Ex-Service man
    And user on createnewproperty screen type on percentageofownership value 100
    And user on createnewproperty screen clicks on add

    #PROPERTY ADDRESS
    And user on createnewproperty screen type on referencepropertynumber value 23456
    And user on createnewproperty screen select on locality value 3
    And user on createnewproperty screen select on appartmentcomplexname value None
    And user on createnewproperty screen select on zoneno value 2
    And user on createnewproperty screen select on wardno value 1
    And user on createnewproperty screen select on blockno value 1
    And user on createnewproperty screen select on street value 1
    And user on createnewproperty screen select on revenuecircle value 1
    And user on createnewproperty screen select on electionward value 1
    And user on createnewproperty screen type on doorno value 23467
    And user on createnewproperty screen type on pincode value 560037
    And user on createnewproperty screen clicks on checkox
    And user on createnewproperty screen type on doorno value #20087
    And user on createnewporperty screen type on adddress value bangalore
    And user on createnewproperty screen type on pincode value 556677

    #AMENITIES
    And user on createnewproperty screen clicks on lift
    And user on createnewproperty screen clicks on Toilet
    And user on createnewproperty screen clicks on watertap
    And user on createnewproperty screen clicks on electricity
    And user on createnewproperty screen clicks on Attachedbathroom
    And user on createnewproperty screen clicks on cableconnection
    And user on createnewproperty screen clicks on Waterharvesting

    #ASSESSMENT DETAILS
    And user on createnewproperty screen select on floortype value 2
    And user on createnewproperty screen select on rooftype value 1
    And user on createnewproperty screen select on walltype value 1
    And user on createnewproperty screen select on woodtype value 1
    And user on createnewproperty screen select on reasonforcreation value New Property
    And user on createnewproperty screen select on propertytype value Building
    And user on createnewproperty screen select on propertysubtype value Options
    And user on createnewproperty screen type on extentofsite value 600

    #FLOORDETAILS
    And user on createnewproperty screen select on floornumber value 5
    And user on createnewproperty screen select on unittype value 1
    And user on createnewproperty screen type on unitnumber value 2211
    And user on createnewproperty screen select on constructiontype value Huts
    And user on createnewproperty screen select on usagetype value 2
    And user on createnewproperty screen select on usagesubtype value 2
    And user on createnewproperty screen type on firmname value bangalore
    And user on createnewproperty screen select on occupancy value Owner
    And user on createnewproperty screen type on occupantname value egov
    And user on createnewproperty screen type on annualrental value 4000
    And user on createnewproperty screen type on manualarv value 2000
    And user on createnewproperty screen type on constructiondate value 9/2/2017
    And user on createnewproperty screen type on effectivedate value 9/3/2017
    And user on createnewproperty screen select on unstructuredland value Yes
    And user on createnewproperty screen type on length value 20
    And user on createnewproperty screen type on breadth value 30
    And user on createnewproperty screen type on plintharea value 600
    And user on createnewproperty screen type on occupancycertificatenumber value 334455
    And user on createnewproperty screen type on buildingpermisssionnumber value 5566
    And user on createnewproperty screen type on buildingpermissiondate value 9/4/2017
    And user on createnewproperty screen type on plinthareainbuildingplan value 9999
    And user on createnewproperty screen clicks on addroom

    #APPROVER DETAILS
    And user on createnewproperty screen select on departmentname value REVENUE
    And user on createnewproperty screen select on designationname value Revenue Officer
    And user on createnewproperty screen select on approvername value 1
    And user on createnewproperty screen type on comments value createnewpropertyformcompleted







