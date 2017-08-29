import React, {Component} from 'react';
import {connect} from 'react-redux';
import RaisedButton from 'material-ui/RaisedButton';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardTitle, CardText} from 'material-ui/Card';
import Checkbox from 'material-ui/Checkbox';
import TextField from 'material-ui/TextField';
import SelectField from 'material-ui/SelectField';
import AutoComplete from 'material-ui/AutoComplete';
import MenuItem from 'material-ui/MenuItem';
import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';
import _ from "lodash";
import {translate} from '../../../common/common';
import Api from '../../../../api/api';
import styles from '../../../../styles/material-ui';


const patterns = {
  date:/^(((0[1-9]|[12]\d|3[01])\/(0[13578]|1[02])\/((19|[2-9]\d)\d{2}))|((0[1-9]|[12]\d|30)\/(0[13456789]|1[012])\/((19|[2-9]\d)\d{2}))|((0[1-9]|1\d|2[0-8])\/02\/((19|[2-9]\d)\d{2}))|(29\/02\/((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))))$/g
}

//titles
const labels={
  applyNewTradeLicense : 'Apply New Trade License',
  tradeOwnerDetails : 'Trade Owner Details',
  tradeLocationDetails : 'Trade Location Details',
  tradeDetails : 'Trade Details',
  agreementDetailsSection : 'Agreement Details section',
  supportingDocuments : 'Supporting Documents'
}

const tradeOwnerDetailsCardFields = [
  {label : "Aadhaar Number", type:"text", code:"adhaarNumber", isMandatory:false, maxLength:12, pattern:/^\d{12}$/g, errorMsg:"Invalid Aadhaar Card Number"},
  {label : "Mobile Number", type:"text", code:"mobileNumber", isMandatory:true, maxLength:10, pattern:/^\d{10}$/g, errorMsg:"Invalid Mobile Number"},
  {label : "Trade Owner Name", type:"text", code:"ownerName", isMandatory:true, maxLength:100, pattern:""},
  {label : "Father / Spouse Name", type:"text", code:"fatherSpouseName", isMandatory:true, maxLength:100, pattern:""},
  {label : "Email Id", code:"emailId", type:"text", isMandatory:true, maxLength:50, pattern:/^(?=.{6,64}$)(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/, errorMsg:"Invalid Email ID"},
  {label : "Trade Owner Address", type:"textarea", code:"ownerAddress", isMandatory:true, maxLength:250, pattern:""}
]

const tradeLocationDetails = [
  {label : "Property Assessment No", type:"text", code:"propertyAssesmentNo", isMandatory:false, maxLength:20, pattern:/^[+-]?\d+$/, errorMsg:"Invalid Assessment Number"},
  {label : "Locality", type:"dropdown", code:"localityId", codeName:"localityName", isMandatory:true, maxLength:50, pattern:""},
  {label : "Admin Ward", type:"dropdown", code:"adminWardId", codeName:"adminWardName", isMandatory:true, maxLength:50, pattern:""},
  {label : "Revenue Ward", type:"dropdown", code:"revenueWardId", codeName:"revenueWardName", isMandatory:true, maxLength:100, pattern:""},
  {label : "Ownership Type", code:"ownerShipType", type:"dropdown", isMandatory:true, maxLength:50, pattern:""},
  {label : "Trade Address", type:"textarea", code:"tradeAddress", isMandatory:true, maxLength:250, pattern:""}
]

const tradeDetails = [
  {label : "Trade Title", type:"text", code:"tradeTitle", isMandatory:true, maxLength:100, pattern:""},
  {label : "Trade Type", type:"dropdown", code:"tradeType", isMandatory:true, maxLength:50, pattern:""},
  {label : "Trade Category", type:"dropdown", code:"categoryId", codeName:'category', codeName:"adminWardName", isMandatory:true, maxLength:50, pattern:""},
  {label : "Trade Sub-Category", type:"dropdown", code:"subCategoryId", codeName:"subCategory", isMandatory:true, maxLength:100, pattern:""},
  {label : "UOM", code:"uomId", codeName:"uom", type:"text", isMandatory:true, maxLength:50, pattern:"", isDisabled:true},
  {label : "Trade Value for the UOM", type:"text", code:"quantity", isMandatory:true, maxLength:50, pattern:/^[+-]?\d+(\.\d{2})?$/, errorMsg:"UOM accepts only numbers"},
  {label : "Validity Years", type:"text", code:"validityYears", isMandatory:true, maxLength:50, pattern:"", isDisabled:true},
  {label : "Remarks", type:"textarea", code:"remarks", isMandatory:false, maxLength:1000, pattern:""},
  {label : "Trade Commencement Date", type:"date", code:"tradeCommencementDate", isMandatory:true, maxLength:1000, pattern:patterns.date, errorMsg:"Invalid Date"},
  {label : "Trader is not the owner of the Property", type:"checkbox", code:"isPropertyOwner", isMandatory:false}
]

const agreementDetailsSection = [
  {label : "Date of Execution", type:"date", code:"agreementDate", isMandatory:true, maxLength:10, pattern:patterns.date,  errorMsg:"Invalid Date"},
  {label : "Registered/Non Registered Document No", type:"text", code:"agreementNo", isMandatory:true, maxLength:30, pattern:""}
]

const customStyles={
  cardTitle:{
    padding: '16px 16px 0'
  },
  th:{
    padding:'15px 10px !important'
  }
}

function compareStrings(a, b) {
  // Assuming you want case-insensitive comparison
  a = a.toLowerCase();
  b = b.toLowerCase();

  return (a < b) ? -1 : (a > b) ? 1 : 0;
}

function sortArrayByAlphabetically(arry, keyToSort){
  return arry.sort(function(a, b) {
    return compareStrings(a[keyToSort], b[keyToSort]);
  });
}



class NewTradeLicense extends Component {

  constructor(){
    super();
    this.customHandleChange = this.customHandleChange.bind(this);
    this.state={
      isPropertyOwner:false,
      documentTypes:[], //supporting documents
      autocompleteDataSource:{
        localityId:[], //assigning datasource by field code
        localityIdConfig: { //autocomplete config
         text: 'name',
         value: 'id'
        }
      },
      dropdownDataSource:{
        adminWardId:[],
        adminWardIdConfig: {
          text: 'name',
          value: 'id'
        },
        revenueWardId:[],
        revenueWardIdConfig: {
          text: 'name',
          value: 'id'
        },
        categoryId:[],
        categoryIdConfig: {
          text: 'name',
          value: 'id'
        },
        subCategoryId:[],
        subCategoryIdConfig:{
          text: 'name',
          value: 'id'
        },
        tradeType:[{
          id:"PERMANENT",
          name:"PERMANENT"
        },
        {
          id:"TEMPORARY",
          name:"TEMPORARY"
        }],
        tradeTypeConfig: {
          text: 'name',
          value: 'id'
        },
        ownerShipType:[{
          id:"STATE GOVERNMENT OWNED",
          name:"STATE GOVERNMENT OWNED"
        },
        {
          id:"RENTED",
          name:"RENTED"
        },
        {
          id:"CENTER GOVERNMENT OWNED",
          name:"CENTER GOVERNMENT OWNED"
        },
        {
          id:"ULB",
          name:"ULB"
        }],
        ownerShipTypeConfig: {
          text: 'name',
          value: 'id'
        },
        localityId:[],
        localityIdConfig:{
          text: 'name',
          value: 'id'
        }
      }
    }
  }



  componentWillMount(){
    let requiredFields = [];
    this.props.initForm(requiredFields);
    //?tenantId=default&boundaryTypeName=WARD&hierarchyTypeName=REVENUE

    var tenantId = localStorage.getItem("tenantId") || "default";

    this.props.setLoadingStatus('loading');
    Promise.all([
      Api.commonApiPost("/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName",{boundaryTypeName:"WARD", hierarchyTypeName:"REVENUE"},{tenantId:tenantId}),
      Api.commonApiPost("/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName",{boundaryTypeName:"Ward", hierarchyTypeName:"ADMINISTRATION"},{tenantId:tenantId}),
      Api.commonApiPost("/tl-masters/category/v1/_search",{type:"category"},{tenantId:tenantId, pageSize:"500"}, false, true),
      Api.commonApiPost("/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName",{boundaryTypeName:"LOCALITY", hierarchyTypeName:"LOCATION"},{tenantId:tenantId}),
      Api.commonApiPost("/tl-masters/documenttype/v2/_search",{applicationType:"NEW"},{tenantId:tenantId})
    ])
    .then((responses)=>{
      //if any error occurs
      // if(!responses || responses.length ===0 || !responses[0] || !responses[1]){
      //   return;
      // }
      this.props.setLoadingStatus('hide');
      try{
        let revenueWardId = responses[0].Boundary.sort(function(a, b) {
            return parseFloat(a.name) - parseFloat(b.price);
        });
        let adminWardId = sortArrayByAlphabetically(responses[1].Boundary, "name");
        let categoryId = sortArrayByAlphabetically(responses[2].categories, "name");
        let localityId = sortArrayByAlphabetically(responses[3].Boundary, "name");
        let documentTypes = sortArrayByAlphabetically(responses[4].documentTypes, "name");
        let dropdownDataSource = {...this.state.dropdownDataSource};
        dropdownDataSource = {...dropdownDataSource, revenueWardId, adminWardId, categoryId, localityId};
        this.setState({dropdownDataSource, documentTypes});
      }
      catch(e){
        console.log('error', e);
      }

    });

  }

  customHandleChange = (value, field)=>{

    var tenantId = localStorage.getItem("tenantId") || "default";
    var _this=this;

    if(field.type === "dropdown"){
      var values=value.split("~");
      var id = value.indexOf("~") > -1 ? values[0] : value;
      this.props.handleChange(id, field.code, field.isMandatory, "", "");
      if(values.length > 1){
        if(field.hasOwnProperty("codeName"))
          this.props.handleChange(values[1], field.codeName, field.isMandatory, "", "");
      }

      if(field.code === "categoryId"){
        const dropdownDataSource = {...this.state.dropdownDataSource, subCategoryId:[]}
        this.setState({dropdownDataSource});
        this.props.handleChange("", "subCategoryId", field.isMandatory, "", "");
        this.props.handleChange("", "validityYears", field.isMandatory, "", "");
        this.props.handleChange("", "uomId", field.isMandatory, "", "");

        Api.commonApiPost("tl-masters/category/v1/_search",{type:"subcategory", categoryId:id},{tenantId:tenantId}, false, true).then(function(response){
          const dropdownDataSource = {..._this.state.dropdownDataSource, subCategoryId:sortArrayByAlphabetically(response.categories, "name")};
          _this.setState({dropdownDataSource});
        }, function(err) {
            console.log(err);
        });
      }
      else if(field.code === "subCategoryId"){
        // /tl-masters/category/v1/_search
        this.props.handleChange("", "validityYears", field.isMandatory, "", "");
        this.props.handleChange("", "uomId", field.isMandatory, "", "");

        Api.commonApiPost("tl-masters/category/v1/_search",{type:"subcategory", ids:id},{tenantId:tenantId}, false, true).then(function(response){
          var category=response.categories[0];
          _this.props.handleChange(category.validityYears, "validityYears", field.isMandatory, "", "");
          _this.props.handleChange(category.details[0].uomName, "uomId", field.isMandatory, "", "");
        }, function(err) {
            console.log(err);
        });
      }

    }
    else{
      this.props.handleChange(value, field.code, field.isMandatory || false, field.pattern || "", field.errorMsg || "");
      if(field.code === 'isPropertyOwner'){
        this.setState({isPropertyOwner:value});
      }
    }

  }

  customAutoCompleteKeyUpEvent = (e, field) =>{

    var _this=this;
    //reset autocomplete value
    this.props.handleChange("", field.code, field.isMandatory, field.pattern, field.errorMsg || "");

    if(e.target.value && field.code === 'localityId'){
      Api.commonApiGet("/egov-location/boundarys/getLocationByLocationName", {locationName : e.target.value}).then(function(response)
      {
        const autocompleteDataSource  = _this.state.autocompleteDataSource;
        autocompleteDataSource[field.code]=response;
        _this.setState({autocompleteDataSource});
      },function(err) {
        _this.handleError(err.message);
      });
    }

  }

  render(){

    var agreementCard=null;
    var brElement=null;

    if(this.state["isPropertyOwner"]){
      console.log('coming inside');
      agreementCard=<CustomCard title={labels.agreementDetailsSection} form={this.props.form}
        fields={agreementDetailsSection}
        fieldErrors = {this.props.fieldErrors}
        handleChange={this.customHandleChange}></CustomCard>;
      brElement=<br/>;
    }

    return(
      <Grid fluid={true}>
        <h2 className="application-title">{translate(labels.applyNewTradeLicense)}</h2>
        <CustomCard title={labels.tradeOwnerDetails} form={this.props.form}
          fields={tradeOwnerDetailsCardFields}
          fieldErrors = {this.props.fieldErrors}
          handleChange={this.customHandleChange}></CustomCard>
        <br/>
        <CustomCard title={labels.tradeLocationDetails} form={this.props.form}
            fields={tradeLocationDetails}
            fieldErrors = {this.props.fieldErrors}
            autocompleteDataSource={this.state.autocompleteDataSource}
            autoCompleteKeyUp = {this.customAutoCompleteKeyUpEvent}
            dropdownDataSource={this.state.dropdownDataSource}
            handleChange={this.customHandleChange}></CustomCard>
        <br/>
        <CustomCard title={labels.tradeDetails} form={this.props.form}
            fields={tradeDetails}
            fieldErrors = {this.props.fieldErrors}
            dropdownDataSource={this.state.dropdownDataSource}
            handleChange={this.customHandleChange}></CustomCard>
        <br/>

        {agreementCard}
        {brElement}

        <SupportingDocuments title={labels.supportingDocuments} docs={this.state.documentTypes}>
        </SupportingDocuments>
        <br/>
        <div style={{textAlign: 'center'}}>
          {/* <RaisedButton style={{margin:'15px 5px'}} label="Reset"/> */}
          <RaisedButton style={{margin:'15px 5px'}} label="Submit" primary={true}/>
        </div>
     </Grid>
    )
  }

}

class CustomCard extends Component {

  constructor(){
    super()
  }

  render(){

    const renderedFields = this.props.fields.map((field, index)=>{
      if(field.type === "autocomplete")
        return <CustomField key={index} field={field} error={this.props.fieldErrors[field.code] || ""}
           autocompleteDataSource = {this.props.autocompleteDataSource[field.code] || []}
           autocompleteDataSourceConfig = {this.props.autocompleteDataSource[field.code+"Config"]}
           autocompleteKeyUp = {this.props.autoCompleteKeyUp}
           value={this.props.form[field.code] || ""} handleChange={this.props.handleChange}></CustomField>
      if(field.type === "dropdown")
         return <CustomField key={index} field={field} error={this.props.fieldErrors[field.code] || ""}
                dropdownDataSource = {this.props.dropdownDataSource[field.code] || []}
                nameValue = {field.codeName ? this.props.form[field.codeName] || "" : ""}
                dropdownDataSourceConfig = {this.props.dropdownDataSource[field.code+"Config"]}
                value={this.props.form[field.code] || ""} handleChange={this.props.handleChange}></CustomField>
      else
         return <CustomField key={index} field={field} error={this.props.fieldErrors[field.code] || ""}
                value={this.props.form[field.code] || ""} handleChange={this.props.handleChange}></CustomField>

    });

    return(
      <Card>
        <CardTitle style={customStyles.cardTitle} title={translate(this.props.title)}></CardTitle>
        <CardText>
          <Grid fluid={true}>
            <Row>
              {renderedFields}
            </Row>
          </Grid>
        </CardText>
      </Card>
    )
  }
}

class CustomField extends Component {

  constructor(){
    super()
  }

  shouldComponentUpdate(nextProps, nextState){
    //console.log('customField should update', !(_.isEqual(this.props, nextProps) && _.isEqual(this.state, nextState)));
    return !(_.isEqual(this.props, nextProps) && _.isEqual(this.state, nextState));
  }

  renderCustomField = ({field, handleChange, error, value, nameValue, autocompleteDataSource, autocompleteKeyUp, autocompleteDataSourceConfig, dropdownDataSourceConfig, dropdownDataSource})=>{

    switch(field.type)
    {
      case "text":
        return(
          <Col xs={12} sm={4} md={4} lg={4}>
            <TextField floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true}
              floatingLabelText={translate(field.label) + (field.isMandatory ? " *":"")}
              fullWidth={true}
              maxLength={field.maxLength}
              value={value || ""}
              errorText={error ||  ""}
              disabled={field.isDisabled || false}
              onChange={(event, value) => handleChange(value, field)} />
          </Col>
        )
       case "textarea":
        return(
            <Col xs={12} sm={4} md={4} lg={4}>
              <TextField fullWidth={true}
                floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true}
                floatingLabelText={translate(field.label) + (field.isMandatory ? " *":"")} multiLine={true}
                value={value || ""}
                disabled={field.isDisabled || false}
                errorText={error ||  ""}
                maxLength={field.maxLength}
                onChange={(event, value) => handleChange(value, field)}/>
           </Col>
        )
        case "date":
         return(
             <Col xs={12} sm={4} md={4} lg={4}>
               <TextField fullWidth={true}
                 fullWidth={true}
                 hintText="DD/MM/YYYY"
                 disabled={field.isDisabled || false}
                 floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true}
                 floatingLabelText={translate(field.label) + (field.isMandatory ? " *":"")} multiLine={true}
                 value={value || ""}
                 errorText={error ||  ""}
                 maxLength={field.maxLength}
                 onChange={(event, value) => handleChange(value, field)}/>
            </Col>
         )
        case "autocomplete":
         return(
           <Col xs={12} sm={4} md={4} lg={4}>
             <AutoComplete
              fullWidth={true}
              disabled={field.isDisabled || false}
              floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true}
              dataSourceConfig={autocompleteDataSourceConfig}
              dataSource={autocompleteDataSource}
              onKeyUp={(e)=> autocompleteKeyUp(e, field)}
              floatingLabelText={translate(field.label) + (field.isMandatory ? " *":"")}/>
           </Col>
         )
        case "dropdown":
         value = value + (nameValue ? "~" + nameValue : "");
         //console.log('value for ', field.code, value);
         return(
           <Col xs={12} sm={4} md={4} lg={4}>
             <SelectField
               fullWidth={true}
               disabled={field.isDisabled || false}
               floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true}
               floatingLabelText={translate(field.label) + (field.isMandatory ? " *":"")}
               value={value || ""}
               onChange={(event, key, value) => {
                 handleChange(value, field)
               }}>
               {dropdownDataSource && dropdownDataSource.map((item, index) =>{
                 if(field.codeName){
                   return (<MenuItem value={`${item[dropdownDataSourceConfig.value]}~${translate(item[dropdownDataSourceConfig.text])}`} key={index} primaryText={translate(item[dropdownDataSourceConfig.text])} />)
                 }
                 else{
                   return (<MenuItem value={item[dropdownDataSourceConfig.value]} key={index} primaryText={translate(item[dropdownDataSourceConfig.text])} />)
                 }

               })
              }
            </SelectField>
           </Col>
         )
         case "checkbox":
          return(
            <Col xs={12} sm={4} md={4} lg={4}>
              <Checkbox label={translate(field.label) + (field.isMandatory ? " *":"")}
                checked={value || false}
                onCheck={(e, isChecked)=>{
                handleChange(isChecked? true : false, field);
              }} />
            </Col>
          )
    }

    return null;

  }

  render(){
    return this.renderCustomField(this.props);
  }

}




  const SupportingDocuments = (props) => {
    console.log('docs', props.docs);
    return(
      <Card>
        <CardTitle style={customStyles.cardTitle} title={translate(props.title)}></CardTitle>
        <CardText>
          <Grid fluid={true}>
            <Row>
              <Col xs={12} lg={12} sm={12} md={12}>
                <Table style={{width:'100%'}} responsive>
                 <thead>
                   <tr>
                     <th style={customStyles.th}>#</th>
                     <th style={customStyles.th}>Document Name</th>
                     <th style={customStyles.th}></th>
                     <th style={customStyles.th}>Comments</th>
                   </tr>
                 </thead>
                 <tbody>
                   {props.docs && props.docs.map((doc, index)=>{
                     return (<tr>
                       <td>{index+1}</td>
                       <td>{`${doc.name} ${doc.mandatory ? " *":""}`}</td>
                       <td>
                         <RaisedButton label="Browse Files" />
                       </td>
                       <td>
                         <TextField
                            hintText="Comments"
                            multiLine={true}
                            fullWidth={true}
                          />
                       </td>
                     </tr>);
                   })}
                 </tbody>
               </Table>
              </Col>
            </Row>
          </Grid>
        </CardText>
      </Card>
    )
  }



// class CustomSelectField extends Component{
//
//     constructor(){
//       super();
//       this.state={
//         attribValues:[],
//         loadingText:"",
//         isLoading:true
//       }
//     }
//
//     componentWillMount(){
//       this.renderAttributes(this.props.obj);
//       this.setState({loadingText:translate("csv.lbl.loading")});
//     }
//
//     shouldComponentUpdate(nextProps, nextState){
//       //console.log('nextState', nextState, this.state);
//       //console.log('nextProps', nextProps, this.props);
//       //console.log('ItShouldUpdate', !(_.isEqual(this.props, nextProps) && _.isEqual(this.state, nextState)));
//       return !(_.isEqual(this.props, nextProps) && _.isEqual(this.state, nextState));
//     }
//
//     renderAttributes = (obj) => {
//       var urlsPart=obj.url.split("?");
//       var queryParams={};
//       var url="";
//       let _this=this;
//       if(urlsPart.length === 2){
//         queryParams = JSON.parse('{"' + urlsPart[1].replace(/&/g, '","').replace(/=/g,'":"') + '"}',
//                    function(key, value) { return key===""?value:decodeURIComponent(value) });
//         url = urlsPart[0];
//       }
//       else
//         url=obj.url;
//       Api.commonApiPost(url, queryParams, {}).then(function(response){
//         var jsonArryKey = Object.keys(response).length > 1 ?  Object.keys(response)[1]:"";
//         if(jsonArryKey){
//           const attribValues=[];
//           response[jsonArryKey].map((item)=>{
//             attribValues.push({key:item.id, name:item.name});
//           });
//           _this.setState({attribValues, isLoading:false, loadingText:""});
//         }
//         }, function(err){
//       });
//     }
//
//     renderMenuItems =(props)=>{
//       return this.state.attribValues.map((dd, index) =>{
//          if(this.props.isMultiple)
//           return (<MenuItem value={translate(dd.key)} insetChildren={true}
//                checked={this.props.value && this.props.value.indexOf(dd.key) > -1} key={index} primaryText={translate(dd.name)} />)
//         else
//           return (<MenuItem value={translate(dd.key)} key={index} primaryText={translate(dd.name)} />)
//         });
//     }
//
//     render(){
//       const obj=this.props.obj;
//       const description=this.props.description;
//       const menuItems=this.renderMenuItems(this.props.isMultiple) || null;
//       return (
//         <Col xs={12} sm={4} md={3} lg={3}>
//           <SelectField fullWidth={true} ref={obj.code} multiple={this.props.isMultiple}
//             floatingLabelText={description} floatingLabelFixed={this.state.isLoading}
//             hintText={this.state.loadingText} value={this.props.value} onChange={(event, key, value) => {
//             this.props.handler(value, obj.code, obj.required, "")
//           }}>
//             {menuItems}
//           </SelectField>
//         </Col>
//       )
//     }
//
//   }



const mapStateToProps = state => {
  return ({form: state.form.form, files: state.form.files, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid});
}

const mapDispatchToProps = dispatch => ({
  initForm: (requiredArray) => {

    if(!requiredArray)
        requiredArray=[];

    dispatch({
      type: "RESET_STATE",
      validationData: {
        required: {
          current: [],
          required: requiredArray
        },
        pattern: {
          current: [],
          required: []
        }
      }
    });
  },
  toggleDailogAndSetText: (dailogState,msg) => {
    dispatch({type: "TOGGLE_DAILOG_AND_SET_TEXT", dailogState,msg});
  },
  setLoadingStatus: (loadingStatus) => {
    dispatch({type: "SET_LOADING_STATUS", loadingStatus});
  },
  handleChange: (value, property, isRequired, pattern, errorMsg) => {
    dispatch({type: "HANDLE_CHANGE", property, value, isRequired, pattern, errorMsg});
  },
  addFile: (action) => {
    dispatch({type: 'FILE_UPLOAD_BY_CODE', isRequired:action.isRequired, code: action.code, files : action.files});
  },
  removeFile: (action) => {
    dispatch({type: 'FILE_REMOVE_BY_CODE', isRequired:action.isRequired, code: action.code, name : action.name});
  }
});

const VisibleNewTradeLicense = connect(
  mapStateToProps,
  mapDispatchToProps
)(NewTradeLicense)

export default VisibleNewTradeLicense;
