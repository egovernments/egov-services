import React, {Component} from 'react';
import {connect} from 'react-redux';

import TextField from 'material-ui/TextField';
import SelectField from 'material-ui/SelectField'
import RaisedButton from 'material-ui/RaisedButton';
import { RadioButton, RadioButtonGroup } from 'material-ui/RadioButton'
import MenuItem from 'material-ui/MenuItem'

import {Collapse,Grid, Row, Col, Table, DropdownButton,Button} from 'react-bootstrap';

import {Card, CardHeader, CardText} from 'material-ui/Card';

import _ from "lodash";

import {translate} from '../../../common/common';
import Api from '../../../../api/api';
import UiButton from '../../../framework/components/UiButton';


class kpivalues  extends Component{
    constructor(props) {
      super(props);

    this.state = {
            showResult: false,
            data: [],
            header:[],
            KPIs:[],
            Department:[],
            FinantialYear:[],
            KPIs:[],
            collapse:[],
            selectedDeptId:'',
            selectedFinYear:'',
            selectedKpiCode:'',

            viewFirst:true,
            viewSecond:false,
            viewThird:false,
            viewFour:false,
            next:true,
            prev:false,
            KPIResult:[],
            errorInput:'No record found'
     };

     this.handleChange = this.handleChange.bind(this);
     this.handleSubmit = this.handleSubmit.bind(this);
     this.searchKPIValues = this.searchKPIValues.bind(this);
     this.clearSearch = this.clearSearch.bind(this);
     this.nextSection = this.nextSection.bind(this);
     this.prevSection = this.prevSection.bind(this);

   }



   componentDidMount() {

     var url = "egov-mdms-service/v1/_get?moduleName=common-masters&masterName=Department";
     var query = [];
     let self = this;
     Api.commonApiPost(url, query, {}, false, false).then(function(res){
         if (res.MdmsRes) {
           //res.MdmsRes.`common-masters
           self.setState({Department:res.MdmsRes['common-masters'].Department});
           //self.state.Department = res.MdmsRes['common-masters'].Department;
         }

       }, function(err){

       });


        url = "egf-master/financialyears/_search";
        query = [];
       Api.commonApiPost(url, query, {}, false, false).then(function(res){
           if (res) {

             //res.MdmsRes.`common-masters
             self.setState({FinantialYear:res.financialYears});
             //self.state.Department = res.MdmsRes['common-masters'].Department;
           }

         }, function(err){

         });

   }



   prepareTableHeader()
   {
     return [{'id':1,1:'',2:'April',3:'May',4:'June',5:'July',6:'Augest',7:'Sep',8:'Oct',9:'Nov',10:'Dec',11:'Jan',12:'Feb',13:'March',}];
   }

   prepareTableBody(response,self)
 	  {
 	    var data = {};

 	    var kpi = new Array();
      var accordian = new Array();



 	    for (var i = 0; i < response.length; i++) {
 	      //var row = {};
 	      var key = i+1;
 	      data[key] = {id :i + 2};
 	      data[key][1] = {kpiName : response[i].kpi.name , kpiid: response[i].kpi.id, targetType:response[i].kpi.targetType, instructions: response[i].kpi.instructions, documentsReq:response[i].kpi.documentsReq, kpiTarget:response[i].kpi.kpiTarget,cellheader:true};
        kpi[response[i].kpi.id] = [];
        accordian[response[i].kpi.id] = false;



 	      for (var j = 0; j < response[i].kpiValue.valueList.length; j++) {
 	        var index = j+2;

 	        data[key][index] = {
 	        						targetType:response[i].kpi.targetType,
 	        						val:response[i].kpiValue.valueList[j].value,
 	        						kpiid: response[i].kpi.id ,
 	        						kpivalueid : response[i].kpiValue.valueList[j].period,
                      valueid : response[i].kpiValue.valueList[j].valueid
 	        					}

 	        kpi[response[i].kpi.id][response[i].kpiValue.valueList[j].period] = response[i].kpiValue.valueList[j].value;

 	      }


 	    }

 	    self.setState({KPIs:kpi});
      self.setState({collapse:accordian});
       //self.state.KPIs = kpi;
 	    var result = [];

     Object.keys(data).forEach(function(item) {
       result.push(data[item]);
     })

     return result;
   }

   getFieldRow(item)
   {
     if (item.cellheader) {
       return <div >
       {item.kpiName}&nbsp; &nbsp;
       <span className="glyphicon glyphicon-menu-down" aria-hidden="true" onClick={(e) => this.toggle(item.kpiid, e)} ></span>
       </div>;
     }
     if (item.targetType) {
      //this.setState({KPIs:kpi});
       if (item.targetType == 'TEXT') {

         /*return <TextField className="custom-form-control-for-textfield"
         floatingLabelStyle={{"color": "#696969", "fontSize": "20px", "white-space": "nowrap"}}
         inputStyle={{"color": "#5F5C57","textAlign":"left"}}
         style={{"display": 'inline-block'}}
         errorStyle={{"float":"left"}}
         value={this.state.KPIs[item.kpiid][item.kpivalueid]}
         onChange={(e) => this.handleChange(item.kpiid, item.kpivalueid, e)} />*/

           return <input type="text" value={this.state.KPIs[item.kpiid][item.kpivalueid]} onChange={(e) => this.handleChange(item.kpiid, item.kpivalueid,item.valueid, e)} />;
           //return <TextField id='kpival_{item.id}' name='kpival_{item.id}'  value={item.val} onChange={this.handleChange} />;
       }
       if (item.targetType == 'VALUE') {

         /*return <TextField className="custom-form-control-for-textfield"
         floatingLabelStyle={{"color": "#696969", "fontSize": "20px", "white-space": "nowrap"}}
         inputStyle={{"color": "#5F5C57","textAlign":"left"}}
         style={{"display": 'inline-block'}}
         errorStyle={{"float":"left"}}
         value={this.state.KPIs[item.kpiid][item.kpivalueid]}
         onChange={(e) => this.handleChange(item.kpiid, item.kpivalueid, e)} />*/
         return <input type="text" pattern="[0-9]*" value={this.state.KPIs[item.kpiid][item.kpivalueid]} onChange={(e) => this.handleChange(item.kpiid, item.kpivalueid ,item.valueid, e)} />;
         //return <TextField id='kpival_{item.id}' name='kpival_{item.id}' value={item.val} onChange={this.handleChange}/>;
       }
       if (item.targetType == 'OBJECTIVE') {

           return <RadioButtonGroup name={item.kpiid+'-'+item.kpivalueid} defaultSelected={this.state.KPIs[item.kpiid][item.kpivalueid]}  valueSelected={this.state.KPIs[item.kpiid][item.kpivalueid]} onChange={(e) => this.handleChange(item.kpiid, item.kpivalueid ,item.valueid, e)} >
                    <RadioButton  value="1" label="YES" disabled={false} />
                    <RadioButton  value="2" label="NO" disabled={false} />
                    <RadioButton  value="3" label="INPROGRESS" disabled={false}/>
                 </RadioButtonGroup>;
       }
       if (item.field == 'BUTTON') {
         return <RaisedButton id="savekpival{item.id}" name="savekpival{item.id}" type="button" label="SAVE" />
       }

       return item.value;
     }
     return <div >{item}</div>;
     //return item;
   }



   toggle(kpiId,event) {
    //console.log(kpiId);
     let accordianClone = this.state.collapse.slice();
     //console.log(accordianClone);
     accordianClone[kpiId] = !accordianClone[kpiId];
     this.setState({ collapse: accordianClone });
   }

   handleChange(kpiId,kpiValueId, valueid, event) {
    let KPIsClone = this.state.KPIs.slice();

    //console.log(event.target.validity.valid);

    KPIsClone[kpiId][kpiValueId] = (event.target.validity.valid)?event.target.value:KPIsClone[kpiId][kpiValueId];

    /*this.state.KPIResult[kpiId]['kpiValue']['valueList'].map(p =>{
      if (p['period'] == kpiValueId) {
        p['value'] = KPIsClone[kpiId][kpiValueId];
      }
    });*/


    let resultsClone = this.state.KPIResult.slice();
    for (var i = 0; i < resultsClone.length; i++) {
      if(valueid == resultsClone[i].kpiValue.id)
      {
          resultsClone[i].kpiValue.valueList.map(p =>{
            if (p['period'] == kpiValueId) {
              p['value'] = KPIsClone[kpiId][kpiValueId];
            }
        });
      }
    }

    //console.log(this.state.KPIResult);

    this.setState({KPIs : KPIsClone, KPIResult:resultsClone});


     //console.log(this.state.KPIs);
   }

   handleSubmit(event)
   {
       this.props.setLoadingStatus('loading');
       let url = "perfmanagement/v1/kpivalue/_create";
       let query = [];
       let body = {'kpiValues' : this.state.KPIResult};
       console.log(this.state.KPIResult);
       console.log(JSON.stringify(body));
       let self = this;
      Api.commonApiPost(url, query, body , false, true).then(function(res){
          self.props.setLoadingStatus('hide');
          if (res) {
            self.props.toggleSnackbarAndSetText(true, translate("perfManagement.update.KPIs.groups.updatekpivalue"), true);
          }

        }, function(err){
            self.props.setLoadingStatus('hide');
            self.props.toggleSnackbarAndSetText(true, err.message);
        });



   }

   handleSearch( event )
   {
     let args = [];
     if (this.state.selectedDeptId) {
       args.push('departmentId='+this.state.selectedDeptId);
     }
     if (this.state.selectedFinYear) {
       args.push('finYear='+this.state.selectedFinYear);
       args['finYear'] = this.state.selectedFinYear;
     }
     if (this.state.selectedKpiCode) {
       args.push('kpiCodes='+this.state.selectedKpiCode);
     }

     let self = this;
     var url = 'perfmanagement/v1/kpivalue/_search?'+args.join('&')
     this.props.setLoadingStatus('loading');
     Api.commonApiPost(url, {}, {}, false, true).then(function(res){
        self.props.setLoadingStatus('hide');
        var response = res.kpiValues;

         var header = self.prepareTableHeader();

         var data   = self.prepareTableBody(response,self);

         self.setState({data: data,header:header,showResult: true,KPIResult:response});

       }, function(err){
         self.props.setLoadingStatus('hide');
       });




   }

   clearSearch()
   {
     this.setState({selectedDeptId: '',selectedFinYear:'', selectedKpiCode:'',showResult: false});
   }

    searchKPIValues(event,key,value,type)
    {
      switch (type) {
        case 'DEPT':
         this.setState({selectedDeptId:value});
        break;
        case 'FINYEAR':
         this.setState({selectedFinYear:event.target.innerHTML});
          break;
       case 'KPI':
         this.setState({selectedKpiCode:value});
         break;
        default:
      }

      let args = [];
      if (type == 'DEPT') {
        args.push('departmentId='+value);
      }
      else if (this.state.selectedDeptId) {
        args.push('departmentId='+this.state.selectedDeptId);
      }
      if (type == 'FINYEAR') {
        args.push('finYear='+event.target.innerHTML);
      }
      else if (this.state.selectedFinYear) {
        args.push('finYear='+this.state.selectedFinYear);
        //args['finYear'] = this.state.selectedFinYear;
      }


      let self = this;
      let url = 'perfmanagement/v1/kpimaster/_search?'+args.join('&');

      Api.commonApiPost(url, {}, {}, false, true).then(function(res){

         //console.log(res);
         self.setState({KPIs : res.KPIs})

        }, function(err){

        });

    }

   nextSection()
   {
     this.setState({prev:true})
     if (this.state.viewFirst) {
       this.setState({viewFirst:false,viewSecond:true})
     }
     if (this.state.viewSecond) {
       this.setState({viewSecond:false,viewThird:true,next:false})
     }

   }
   prevSection()
   {
     this.setState({next:true})
     if (this.state.viewThird) {
       this.setState({viewThird:false,viewSecond:true})
     }
     if (this.state.viewSecond) {
       this.setState({viewSecond:false,viewFirst:true,prev:false})
     }

   }



   render() {
     let {mockData, moduleName, actionName, formData, fieldErrors, isFormValid} = this.props;
     let {create, handleChange, getVal, addNewCard, removeCard, autoComHandler, initiateWF} = this;

     let tableStyle = {
                align:"center"
           };

           let list = this.state.header.map(p =>{
             //console.log('here');
             //console.log(p);
                return (
                  <thead>
                     <tr className="grey2" key={p.id}>
                         <th style={{"width":"10px"}}>
                         {this.state.prev && <span className="glyphicon glyphicon-menu-left" aria-hidden="true" onClick={(e) => this.prevSection(e)} ></span>}
                         </th>
                          {Object.keys(p).filter(k => k !== 'id').map(k => {

                             var className = '';
                             // first section
                             if(k>=2 && k <= 5 && !this.state.viewFirst)
                             {
                               className = 'hidden';
                             }
                             if(k>=6 && k <= 9 && !this.state.viewSecond)
                             {
                               className = 'hidden';
                             }
                             if(k>=10 && k <= 13 && !this.state.viewThird)
                             {
                               className = 'hidden';
                             }

                                return (
                                  <th className={className} key={p.id+''+k}>
                                  <div suppressContentEditableWarning="true"   value={k} >{this.getFieldRow(p[k])}</div>
                               </th>);
                          })}
                          <th style={{"width":"10px"}}>
                          {this.state.next &&  <span className="glyphicon glyphicon-menu-right" aria-hidden="true" onClick={(e) => this.nextSection(e)} ></span>}
                          </th>
                     </tr>
                     </thead>
                );
           });

          list.push(this.state.data.map(p =>{
             //console.log('here');
                return (
                   <tbody>
                     <tr className="grey2" key={p.id}>
                         <td></td>
                          {Object.keys(p).filter(k => k !== 'id').map(k => {
                                var className = '';
                                // first section
                                if(k>=2 && k <= 5 && !this.state.viewFirst)
                                {
                                  className = 'hidden';
                                }
                                if(k>=6 && k <= 9 && !this.state.viewSecond)
                                {
                                  className = 'hidden';
                                }
                                if(k>=10 && k <= 13 && !this.state.viewThird)
                                {
                                  className = 'hidden';
                                }
                                return (
                                  <td className={className} key={p.id+''+k}>
                                  <div suppressContentEditableWarning="true"  value={k} >{this.getFieldRow(p[k])}</div>
                               </td>);
                          })}
                          <td></td>
                     </tr>
                     <tr>
                     		<td colSpan="13">
                     		<Collapse in={this.state.collapse[p[1].kpiid]} >
 					          <Card className="kpi">
 						          <CardText>
                           <Row>
                           <Col xs={6} md={6}>
                              <label>KPI Target Type</label>
                              <RadioButtonGroup name={'target_type_'+p[1].kpiid} defaultSelected={p[1].targetType.toString()} >
                                 <RadioButton  value={"TEXT"} label="TEXT" disabled={true} />
                                	<RadioButton  value={"VALUE"} label="VALUE" disabled={true} />
                                	<RadioButton  value={"OBJECTIVE"} label="OBJECTIVE" disabled={true}/>
                              </RadioButtonGroup>
                            </Col>
                            <Col xs={6} md={6}>

                            <TextField className="custom-form-control-for-textfield"
                            floatingLabelStyle={{"color": "#A9A9A9", "fontSize": "20px", "white-space": "nowrap"}}
                            inputStyle={{"color": "#5F5C57","textAlign":"left"}}
                            floatingLabelText={<span> Target Value </span>}
                            floatingLabelFixed={true}
                            style={{"display": 'inline-block'}}
                            errorStyle={{"float":"left"}}
                            fullWidth={true}
                            disabled={true}
                            value={p[1].kpiTarget.targetValue} />
                           </Col>
                           <Col xs={6} md={6}>

                              <TextField className="custom-form-control-for-textfield"
                              floatingLabelStyle={{"color": "#A9A9A9", "fontSize": "20px", "white-space": "nowrap"}}
                              inputStyle={{"color": "#5F5C57","textAlign":"left"}}
                              floatingLabelText={<span> Instructions </span>}
                              floatingLabelFixed={true}
                              style={{"display": 'inline-block'}}
                              errorStyle={{"float":"left"}}
                              fullWidth={true}
                              disabled={true}
                              value={p[1].instructions} />

                            </Col>

                            </Row>
 						           </CardText>
 					          </Card>
 					        </Collapse>
 					        </td>
                     </tr>
                   </tbody>
                );
           }));


     //console.log(list);
     return (
       <div className="SearchResult">
       <Row>
           <Col xs={6} md={6}>
             <h3 style={{paddingLeft: 15, "marginBottom": "0"}}>{!_.isEmpty(mockData) && moduleName && actionName && mockData[`${moduleName}.${actionName}`] && mockData[`${moduleName}.${actionName}`].title ? translate(mockData[`${moduleName}.${actionName}`].title) : ""}</h3>
           </Col>
           <Col xs={6} md={6}>
             <div style={{"textAlign": "right", "color": "#FF0000", "marginTop": "15px", "marginRight": "15px", "paddingTop": "8px"}}><i>( * ) {translate("framework.required.note")}</i></div>
           </Col>
         </Row>

         <Card className="uiCard">
           <CardHeader title={<strong> KPI Value Search </strong>}/>
           <CardText>
              <Row className="show-grid">
                 <Col xs={4} md={4}>
                   <SelectField value={this.state.selectedDeptId}
                   className="custom-form-control-for-select"
                   floatingLabelStyle={{"color": "#696969", "fontSize": "20px", "white-space": "nowrap"}}
                   floatingLabelFixed={true}
                   dropDownMenuProps={{animated: false, targetOrigin: {horizontal: 'left', vertical: 'bottom'}}}
                   style={{"display":  'inline-block'}}
                   errorStyle={{"float":"left"}}
                   fullWidth={true}
                   hintText="Please Select"
                   labelStyle={{"color": "#5F5C57"}}
                   floatingLabelText={<span>Department</span>}
                   onChange={(event, key, value) => this.searchKPIValues(event, key, value,'DEPT')}>

                     {this.state.Department && this.state.Department.map((dd, index) => (
                         <MenuItem value={dd.id && dd.id.toString()} key={index} primaryText={dd.name} />
                     ))}
                   </SelectField>
                   </Col>

                   <Col xs={4} md={4}>
                   <SelectField  value={this.state.selectedFinYear}
                   className="custom-form-control-for-select"
                   className="custom-form-control-for-select"
                   floatingLabelStyle={{"color": "#696969", "fontSize": "20px", "white-space": "nowrap"}}
                   floatingLabelFixed={true}
                   dropDownMenuProps={{animated: false, targetOrigin: {horizontal: 'left', vertical: 'bottom'}}}
                   style={{"display":  'inline-block'}}
                   errorStyle={{"float":"left"}}
                   fullWidth={true}
                   hintText="Please Select"
                   labelStyle={{"color": "#5F5C57"}}
                   floatingLabelText={<span>Finantial Year</span>}
                   onChange={(event, key, value) => this.searchKPIValues(event, key, value,'FINYEAR')}>
                       {this.state.FinantialYear && this.state.FinantialYear.map((dd, index) => (
                           <MenuItem value={dd.finYearRange && dd.finYearRange.toString()} key={index} primaryText={dd.finYearRange} />
                       ))}
                   </SelectField>
                   </Col>

                   <Col xs={4} md={4}>

                   <SelectField  value={this.state.selectedKpiCode}
                   className="custom-form-control-for-select"
                   floatingLabelStyle={{"color": "#696969", "fontSize": "20px", "white-space": "nowrap"}}
                   floatingLabelFixed={true}
                   dropDownMenuProps={{animated: false, targetOrigin: {horizontal: 'left', vertical: 'bottom'}}}
                   style={{"display":  'inline-block'}}
                   errorStyle={{"float":"left"}}
                   fullWidth={true}
                   hintText="Please Select"
                   labelStyle={{"color": "#5F5C57"}}
                   floatingLabelText={<span>KPI Code</span>}
                   onChange={(event, key, value) => this.searchKPIValues(event, key, value,'KPI')}>

                     {this.state.KPIs && this.state.KPIs.map((dd, index) => (
                         <MenuItem value={dd.code && dd.code.toString()} key={index} primaryText={dd.code} />
                     ))}
                   </SelectField>


                   </Col>

                  </Row>
             </CardText>
           </Card>

           <Row className="show-grid">

                <Col xs={6} xsOffset={6}>
                 <UiButton item={{"label": "Search","uiType":"button", "primary": true}} ui="google" handler={(e) => this.handleSearch(e)} />&nbsp;&nbsp;
                 <UiButton item={{"label": "Reset", "uiType":"button", "primary": false}} ui="google" handler={this.clearSearch}/>&nbsp;&nbsp;
               </Col>
            </Row>

         {this.state.showResult  &&
 	        <Card className="uiCard">
 	            <CardHeader title={<strong> Report </strong>} />

 	            <CardText>

                     <Table id="searchTable" className="table table-striped table-bordered table dataTable no-footer" cellspacing="0" width="100%" style={tableStyle} responsive>
                         {list}
                    </Table>

                    <br/>
                    <Row>
                      <Col xs={2} xsOffset={10}>
                         <UiButton item={{"label": "Save","uiType":"button", "primary": true}} ui="google" handler={(e) => this.handleSubmit(e)} />&nbsp;&nbsp;
                       </Col>
                     </Row>
 	        </CardText>
 	        </Card>}

       </div>
     );
   }
 }

const mapStateToProps = state => ({
  metaData:state.framework.metaData,
  mockData: state.framework.mockData,
  moduleName:state.framework.moduleName,
  actionName:state.framework.actionName,
  formData:state.frameworkForm.form,
  fieldErrors: state.frameworkForm.fieldErrors,
  isFormValid: state.frameworkForm.isFormValid,
  requiredFields: state.frameworkForm.requiredFields,
  dropDownData: state.framework.dropDownData
});

const mapDispatchToProps = dispatch => ({
  initForm: (requiredFields) => {
    dispatch({
      type: "SET_REQUIRED_FIELDS",
      requiredFields
    });
  },
  setMetaData: (metaData) => {
    dispatch({type:"SET_META_DATA", metaData})
  },
  setMockData: (mockData) => {
    dispatch({type: "SET_MOCK_DATA", mockData});
  },
  setFormData: (data) => {
    dispatch({type: "SET_FORM_DATA", data});
  },
  setModuleName: (moduleName) => {
    dispatch({type:"SET_MODULE_NAME", moduleName})
  },
  setActionName: (actionName) => {
    dispatch({type:"SET_ACTION_NAME", actionName})
  },
  handleChange: (e, property, isRequired, pattern, requiredErrMsg, patternErrMsg)=>{
    dispatch({type:"HANDLE_CHANGE_FRAMEWORK", property,value: e.target.value, isRequired, pattern, requiredErrMsg, patternErrMsg});
  },
  setLoadingStatus: (loadingStatus) => {
    dispatch({type: "SET_LOADING_STATUS", loadingStatus});
  },
  toggleSnackbarAndSetText: (snackbarState, toastMsg, isSuccess, isError) => {
    dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState, toastMsg, isSuccess, isError});
  },
  setDropDownData:(fieldName, dropDownData) => {
    dispatch({type: "SET_DROPDWON_DATA", fieldName, dropDownData})
  },
  setRoute: (route) => dispatch({type: "SET_ROUTE", route}),
  delRequiredFields: (requiredFields) => {
    dispatch({type: "DEL_REQUIRED_FIELDS", requiredFields})
  },
  addRequiredFields: (requiredFields) => {
    dispatch({type: "ADD_REQUIRED_FIELDS", requiredFields})
  },
  removeFieldErrors: (key) => {
    dispatch({type: "REMOVE_FROM_FIELD_ERRORS", key})
  }
});

export default connect(mapStateToProps, mapDispatchToProps)(kpivalues);
