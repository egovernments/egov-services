<h4><p align="center">Cucumber Syntax For UI Automation</p></h4>

This file explains about, how to write an English Statement in such a way that can be automated by following some simple conventions.

#### Terminology:
Generally we use to deal UI Automation with based on five functionality

1. Consumer - The Person who is interacting with the web page like Admin, User
2. Screen   - On Which Screen Consumer is interacting.
3. Action   - And what type of action, Consumer is performing.
4. Element  - On which field Consumer is performing the action
5. Value    - Consumer wants to enter the data.

<p align="center"><b>Getting Started</b></p>

#### Actions
* clicks , doubleClicks 
* types
* selects , dropdown
* uploads
* assert
* copies a text from web page and re-using
* clicks on radiobutton/checkbox - ForceClick
* force clicks on the element
* Enter text in suggestion box

<p align="center"><b>Some Examples</b></p>                           

#### Basic Notation

1) And "Consumer" on "ScreenName" screen "Action" on "ElementName" --- Consumer On Screen performs some action.
2) And "Consumer" on "ScreenName" screen "Action" on "ElementName" value "Value" --- Consumer On Screen performs some action with value.

##### Clicks:

1) And user on Login screen clicks on signInButton
2) And user on Login screen doubleClicks on signInButton
3) And user on Login screen clicks on text value SignIn

##### Types:

1) And user on PTISDataEntry screen types on phoneNumber value 1234567890

##### Select/Dropdown:

1) And user on PTISDataEntry screen selects on gender value MALE (Give Exact Text Present in dropdown) -- For eGov Websites
1) And user on PTISDataEntry screen dropdown on gender value MALE (Give Exact Text Present in dropdown) -- For Non - eGov Websites

##### Uploading A File:

1) And user on Grievance screen uploads on document value pgrDocument.jpg

##### Assert Text: 

1) And user on OfficialRegisterComplaint screen verifies contactInfo has visible value Contact Information (Text is present)
2) And user on OfficialRegisterComplaint screen verifies contactInfo has notvisible value Contact Information (Text is Not Present)
3) And user on OfficialRegisterComplaint screen verifies contactInfo is enabled (displayed)
4) And user on OfficialRegisterComplaint screen verifies contactInfo is notenabled (not displayed)
5) And user on Home screen will see the menu (Here driver will wait until it find's the menu)

##### Copies Text From Web and Re-Using:

1) And user on Grievance screen copies the crnNumber to crnNo
2) And user on Grievance screen types on search value crnNo (Give same name used in above statement , it should be unique in the scenario)

##### Force Clicks On Element or Action on Radio/Checkbox(JSClick):

1) And user on PGRReceivingMode screen forceClicks on receivingModeActive
1) And user on Home screen forceClicks on menuButton

##### Enter text in Suggestion Box:

1) And user on Grievance screen types on grievanceLocation suggestion box with value Bank Road

##### Some Other Useful Statements: 

1) And user on Home screen will wait until the page loads (No need to pass any fields, it just waits until the page loads)
2) And user on Home screen refresh's the webpage (No need to pass any fields, it just refresh's the web page)

<p align="center"><b>AutoGenerating Data</b></p>                      

##### Random Number: (Use Random Keyword for random generation)

1) And user on PTISDataEntry screen types on phoneNumber value 10 digit random numbers  (For Mobile Number)                     
2) And user on PTISDataEntry screen types on aadharNumber value 12 digit random numbers  (For Aadhar Number)                     
3) And user on PTISDataEntry screen types on phoneNumber value "1",9 digit random numbers (Here "1" is appended with 9 random numbers)

##### Random Characters: (Use Random Keyword for random generation)

1) And user on PTISDataEntry screen types on OwnerName value "Owner ", 4 random characters  (Here name is having both lower & upper case random characters)                  
2) And user on PTISDataEntry screen types on OwnerName value "Owner ", 4 random upper case characters  (Here name is having only upper case random characters)                  
3) And user on PTISDataEntry screen types on OwnerName value "Owner ", 4 random lower case characters  (Here name is having random characters)                  
4) And user on PTISDataEntry screen types on OwnerName value "Owner ", 4 digit random numbers  (Here name is having random numbers)                    

##### Random Email: (Use Random Keyword for random generation)

1) And user on PTISDataEntry screen types on email value random email (Random Email)

##### Date & Time Generation: (Use Random Keyword for random generation)

1) And user on PTISDataEntry screen types on effectiveDate value current date  (Current Date)                    
2) And user on PTISDataEntry screen types on effectiveDate value previous date  (Previous Date)                    
3) And user on PTISDataEntry screen types on effectiveDate value past 5 dates  (Past 5th Date Calculated Based on Current Date)                    
4) And user on PTISDataEntry screen types on effectiveDate value future 5 dates  (Future 5th Date Calculated Based on Current Date)                    
5) And user on PTISDataEntry screen types on effectiveDate value current year  (Current Year)                    
6) And user on PTISDataEntry screen types on effectiveDate value current time  (Current Time)                    

##### Scroll to the Element:

1) And user on "ScreenName" screen scroll to the "ElementName" (Scroll To the Particular Element)
                     

##### Handling Popup and Click on OK

1) And user on "ScreenName' screen accepts the popup (Driver will move to the Popup and Clicks on OK)




