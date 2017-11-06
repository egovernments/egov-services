# Auto generation of UI
### ui-auto-gen is tool to generate reactJS UI specs.

### How to use
- Create Contract yml
 In contract we have a section where we put ui-info.yml reference.
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
- clone this repository
- go to folder and execute following commands

``` 
    npm i
    node server
   ```
   This will start node server on port 4002.
   - go to http://localhost:4002/ui-auto-gen
   - put yml url, module code & click "generate specs".
   - You will get a zipped folder containing 2 files.
   
   TODO - Sourabh please add remainging items.


#### Hosted:
- URL : http://egov-micro-dev.egovernments.org/ui-auto-gen
- same as above
