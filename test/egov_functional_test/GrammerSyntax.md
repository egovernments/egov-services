<p align="center"><h8>Cucumber Syntax For UI Automation<h8></p>
----------------------------------------------------------------

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
* clicks , doubleclicks 
* types
* selects
* uploads
* assert
* copies a text from web page and re-using
* clicks on radiobutton/checkbox
* force clicks on the element
* Enter text in suggestion box

<p align="center"><b>Some Examples</b></p>                           

#### Basic Notation

1) And "Consumer" on "ScreenName" screen "Action" on "ElementName" --- Consumer On Screen performs some action.
2) And "Consumer" on "ScreenName" screen "Action" on "ElementName" value "Value" --- Consumer On Screen performs some action with value.

##### Clicks:

1) And user on Login screen clicks on signInButton
2) And user on Login screen doubleclicks on signInButton
3) And user on Login screen clicks on text value SignIn

##### Types:

1) And user on PTISDataEntry screen types on phoneNumber value 1234567890

##### Dropdown:

1) And user on PTISDataEntry screen types on gender value MALE (Give Exact Text Present in dropdown)

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

##### Action on Radio/Checkbox:

1) And user on PGRReceivingMode screen clicks radio button or checkbox on receivingModeActive

##### Force Clicks On Element (JSClick):

1) And user on Home screen force clicks on menuButton
2) And user on Home screen force clicks on text value SignOut

##### Enter text in Suggestion Box:

1) And user on Grievance screen types on grievanceLocation suggestion box with value Bank Road

##### Some Other Useful Statements: 

1) And user on Home screen will wait until the page loads (No need to pass any fields, it just waits until the page loads)
2) And user on Home screen refresh's the webpage (No need to pass any fields, it just refresh's the web page)

<p align="center"><b>AutoGenerating Data</b></p>                      

##### Random Number:

1) And user on PTISDataEntry screen types on phoneNumber value --10 digit random numbers  (For Mobile Number)                     
2) And user on PTISDataEntry screen types on aadharNumber value --12 digit random numbers  (For Aadhar Number)                     
3) And user on PTISDataEntry screen types on phoneNumber value --"1",9 digit random numbers (Here "1" is appended with 9 random numbers)

##### Random Characters And Email:

1) And user on PTISDataEntry screen types on OwnerName value --"Owner ", 4 random characters  (Here name is having random characters)                  
2) And user on PTISDataEntry screen types on OwnerName value --"Owner ", 4 digit random numbers  (Here name is having random numbers)                    
3) And user on PTISDataEntry screen types on email value --email (Random Email)
                     






