# Auto generation of UI
### ui-auto-gen is a tool to generate reactJS UI specs.

### How to use
- Create Contract yml
 In contract we have a section where we put ui-info.yml reference. (Refer section **How to create ui-info.yaml**)
 Example contract (SWM): https://github.com/egovernments/egov-services/blob/master/docs/swm/contract/v1-0-0.yaml 
 ``` x-ui-info:
  $ref: https://raw.githubusercontent.com/egovernments/egov-services/master/docs/swm/contract/vfd-ui-info.yaml

```
 Reference of ui-info-yml : https://github.com/egovernments/egov-services/blob/master/web/ui-auto-gen/docs/ui-info-schema.yml. This reference file will explain you the terminologies used while writing ui-info yml.
 SWM example : https://github.com/egovernments/egov-services/blob/master/docs/swm/contract/vfd-ui-info.yaml
 - Once you are done with writing contract and ui-info.yml, run this tool to generate ui component.

#### Local Setup
- Prerequisite : Node.js
- How to Install npm package: https://docs.npmjs.com/getting-started/installing-npm-packages-locally
- Clone this repository
- Go to folder and execute following commands

``` 
    npm i
    node server
   ```
   This will start node server on port 4002.
   - go to http://localhost:4002/ui-auto-gen
   
#### How to use the auto-gen tool
   - Put yml url, module code/name & click "generate specs".
   - You will get a zipped folder containing 2 files (_SOME_YAML_NAME.js & default.json). Give a intuitive name to _SOME_YAML_NAME.js.
   - Go to react-pgr-web -> src -> components -> framework -> specs, create a folder with module code/name and put renamed _SOME_YAML_NAME.js file in module folder or if you generated for master, then create *master* folder inside your module folder and paste your renamed _SOME_YAML_NAME.js.
   - Copy default.json (formatted one), change the default generated labels(if required) and paste it in react-pgr-web -> src -> components -> common -> common.js under localization data.
   - Lets say your module name is X. Now run react-pgr-web and navigate to http://localhost:3000/create/X if your specs in inside your module folder, else navigate to http://localhost:3000/create/X/Y, if your specs is inside X->master. Here Y is your renamed _SONE_YAML_NAME.js. This should load the create page for that reference PATH. Follow similar steps for SEARCH/UPDATE/VIEW.
   
#### Example
  Find example YAML and its ui-info YAML [here.](https://github.com/egovernments/egov-services/tree/master/web/ui-auto-gen/docs/example) 
  > It is recommended that use the example ui-info file as your template file and start modifing it (Recommended). 

#### How to write ui-info.yaml/Elements in ui-info (*Refer example for additional help*)
   - Create a YAML file with YOUR_CHOSEN_NAME-ui-info.yml.
   - ##### UiInfo (**_MANDATORY_**)
     Add **UiInfo** at the top level as an array.
   - ##### referencePath
     Add **referencePath** inside UiInfo. Reference paths are the API paths for which there will be _search/_update/_create and screens are designed. 
   
       > For ex. If /vehicleFuellingDetails/_create is your endpoint and you are designing a screen for VehicleFuellingDetails then your reference path will be /vehicleFuellingDetails
  
  - ##### groups (**_MANDATORY_**)
    Every referencePath will have **groups**. This refers to the cards that are painted on the screen and tells the tool what all fields needs to be placed in a specific card.
  
    ```
    groups:
      Group1:
        fields: //fields in a group
        - autoUi[0].textField
        - autoUi[0].textArea
      Group2:
        fields:
        - autoUi[0].parentDropDown
        - autoUi[0].childDropDown
    ```
  - ##### autoFills
    Auto fills are required when on typing/filling one field, it should make an API call and fill data for other fields.
    ```
    autoFills:
    - onChangeField: autoUi[0].autoFill //Field that should trigger API call
      affectedFields: //Array of affected fields
        - autoUi[0].childAutoFill
      affectJSONPath: //Array of fields from the URL response that should fill the above fields in sequence
        - test
      url: /test/test //URL to call
    ```
  - ##### dependents
    Dependents are required, if based on one dropdown selection other dropdown(s) data should load.
    ```
    dependents:
    - onChangeField: autoUi[0].parentDropDown
      affectedFields:
        autoUi[0].chaildDropDown:
          pattern: /egov-mdms-service/v1/_get?&moduleName=SWM&masterName=Vehicle&filter=[?(@.vehicleType == {autoUi[0].parentDropDown})]|$..id|$..regNumber //URL to call. JSONPATH after first | reflects ID path and second JSONPATH after second | refers to display name in dropdown.
          type: dropDown
    ```
  - ##### externalData
    External data specifies drowndown/autocomplete data URLs, the ID/CODE/VALUE of a particular item in dropdown and its DISPLAY NAME.
    ```
    externalData:
    - fieldName: autoUi[0].dropDown //For this field, load data from external URL
      url: "/egov-mdms-service/v1/_get?&moduleName=SWM&masterName=VehicleType"
      keyPath: "$..id" //VALUES JSON PATH
      valPath: "$..name" //DISPLAY NAME JSON PATH
    ```
  - ##### radios
    Radio button's true/false labels need to be mentioned here.
    ```
    radios:
    - jsonPath: autoUi[0].radio
      trueLabel: isActive
      falseLabel: isNotActive
    ```
  - ##### searchResult (**_MANDATORY_** if _search is present)
    This field in mandatory if for the corrosponding reference path has _search (if not provided, tool will throw error).
    This specifies the column labels(localization keys) & their values.
    ```
    searchResult:
     rowClickUrlUpdate: "/update/autoui/{textField}" //When the search table appears, on clicking a row, which update page to navigate to
     rowClickUrlView: "/view/autoui/{textField}" //When the search table appears, on clicking a row, which view page to navigate to
     columns: //Column localization labels
     - textField
     - textArea
     - datePicker
     values: //JSONPATH of values from the response object to be shown
     - textField
     - textArea
     - datePicker
     resultObjectName: autoUi //Object name from which above values will be picked from response
    ```
  - ##### showHideFields
    Show OR hide fields/groups based on certain conditions such as, show a text field only on click of a checkbox or hide a text field based on certain value of a dropdown and so on.
    ```
    showHideFields:
    autoUi[0].textField: //JsonPath based on which you want to show/hide other fields/groups
    - ifValue: abcd //Value @ in that JsonPath to trigger show/hide
      showFields: //Show these fields if the value is abcd
      - autoUi[0].textArea
      hideFields: //Hide these fields if the value is abcd
      - autoUi[0].datePicker
      showGroups: //Show these groups if the value is abcd
      - Group3
      hideGroups: //Hide these groups if the value is abcd
      - Group4
    ```
  - ##### tables
    To add table components in UI which have dynamic fields such as dropdowns, textFields, etc.
    ```
    tables:
    autoUi[0].table: \\Json path for the table component
      group: Group2 \\In which group this table will be added(From the above mentioned groups)
      columns: \\Table column header\labels
      - pinCode
      - email
      values: \\Json path of the fields to be included in the table rows, these json paths will be replaced with their complete field objects that includes name, jsonPath, pattern, type, patternErrorMsg, etc
      - autoUi[0].table[0].pinCode
      - autoUi[0].table[0].email
    ```
#### Things to add/change in CONTRACT.yaml (*Refer example for additional help*)
- ##### x-ui-info (**_MANDATORY_**)
  To refer the ui-info file in your contract, this is required.
  ```
  x-ui-info:
   $ref: https://raw.githubusercontent.com/egovernments/egov-services/master/web/ui-auto-gen/docs/example/autoui-info.yaml
  ```
- ##### format
  YAML **type** (types) has limited number of types provided which are not enough to specify the tool the desired component that should render on the screen. Hence, we use **format** field which accepts any string to specify these components.
  Following formats are supported by the tool:
  - date
  - email
  - pan
  - pinCode
  - mobileNumber
  - autoComplete
  - aadhar
  - checkbox
  - singleValueList
  - multiValueList
  
 > NOTE: format needs to be specified at the end field level and not its parent

#### Hosted:
- URL : http://egov-micro-dev.egovernments.org/ui-auto-gen
- same as above

> As a side note, do let us know via mail/JIRA ticket if you find something is missing in documentation.
