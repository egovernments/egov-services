import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Grid, Row, Col, Table} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import TextField from 'material-ui/TextField';
import {brown500, red500,white,orange800} from 'material-ui/styles/colors';
import DatePicker from 'material-ui/DatePicker';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import RaisedButton from 'material-ui/RaisedButton';
import Api from '../../../api/api';
import ShowField from './showField';
import {translate} from '../../common/common';
import jp from "jsonpath";
import _ from 'lodash';
import styles from '../../../styles/material-ui';

class ShowForm extends Component {
  constructor(props) {
       super(props);
       this.state = {
         searchBtnText : 'Generate Report'
       }
       this.search=this.search.bind(this);
   }

  handleChange=(e, property, isRequired, pattern)=>
  {

    // console.log(e);
    let {metaData,setMetaData}=this.props;
    // console.log(e, property, isRequired, pattern);
    if(property === 'fromDate' || property === 'toDate'){
      this.checkDate(e.target.value, property, isRequired, pattern);
    }else{
      this.props.handleChange(e, property, isRequired, pattern);
    }

    // console.log(metaData);
    if(metaData.hasOwnProperty("reportDetails") && metaData.reportDetails.searchParams.length > 0)
    {
      for (var i = 0; i < metaData.reportDetails.searchParams.length; i++) {
        if (metaData.reportDetails.searchParams[i].type=="url" && (typeof(metaData.reportDetails.searchParams[i].defaultValue)!="object" || metaData.reportDetails.searchParams[i].hasOwnProperty("pattern"))) {
            if (!metaData.reportDetails.searchParams[i].hasOwnProperty("pattern")) {
              metaData.reportDetails.searchParams[i]["pattern"]=metaData.reportDetails.searchParams[i].defaultValue;
            }

            if(metaData.reportDetails.searchParams[i].pattern.search(property)>-1) {
              let splitArray = metaData.reportDetails.searchParams[i].pattern.split("?");
              let url = splitArray[0];
              let queryString = splitArray[1].split("|")[0].split("&");
              let queryJSON = {};

              for(var key in queryString) {
                let dat = queryString[key].split("=");
                if(dat[0] == "tenantId") continue;
                if(dat[1].indexOf("{") > -1) {
                  var path = dat[1].split("{")[1].split("}")[0];
                  var pat = new RegExp("\{" + path + "\}", "g");
                  queryJSON[dat[0]] = dat[1].replace(pat, e.target.value);
                } else {
                  queryJSON[dat[0]] = dat[1];
                }
              }

              var response = Api.commonApiPost(url, queryJSON).then(function(response) {
                  let keys = jp.query(response, splitArray[1].split("|")[1]);
                  let values = jp.query(response, splitArray[1].split("|")[2]);
                  let defaultValue = {};
                  for (var k = 0; k < keys.length; k++) {
                    defaultValue[keys[k]] = values[k];
                  }
                  for (var l=0; l<metaData.reportDetails.searchParams.length; l++) {
                    if (metaData.reportDetails.searchParams[l].type == "url" && metaData.reportDetails.searchParams[l].pattern.search(property) > -1) {
                        metaData.reportDetails.searchParams[l].defaultValue = defaultValue;
                    }
                  }
                  setMetaData(metaData);
              },function(err) {
                  alert("Somthing went wrong while loading depedant dropdown");
              });
            }
        }
      }
    }

  }

  checkDate = (value, name, required, pattern) => {
    let e={
      target:{
        value:value
      }
    }
    if(name == 'fromDate'){
      let startDate = value;
      if(this.props.searchForm){
        let endDate = this.props.searchForm.toDate;
        this.props.handleChange(e, name, required, pattern);
        this.validateDate(startDate, endDate, required, 'fromDate');//3rd param to denote whether field fails
      }else{
        this.props.handleChange(e, name, required, pattern);
      }
    }else{
      let endDate = value;
      if(this.props.searchForm){
        let startDate = this.props.searchForm.fromDate;
        this.props.handleChange(e, name, required, pattern);
        this.validateDate(startDate, endDate, required, 'toDate');//3rd param to denote whether field fails
      }
    }
  }

  validateDate = (startDate, endDate, required, field) => {
    if(startDate && endDate){
      let sD = new Date(startDate);
      sD.setHours(0, 0, 0, 0);
      let eD = new Date(endDate);
      eD.setHours(0, 0, 0, 0);
      if(eD >= sD){
          this.setState({datefield : ''})
          this.setState({dateError : ''})
      }else{
        let e={
          target:{
            value:''
          }
        }
        this.props.handleChange(e, field, required, '');
        this.setState({datefield : field});
        this.setState({dateError :
          field === 'toDate' ? translate('pgr.lbl.dategreater') : translate('pgr.lbl.datelesser')
        })
      }
    }
  }

  handleFormFields = () => {
    let {currentThis}=this;
    let {metaData,searchForm}=this.props;
    // console.log(metaData);
    if(!_.isEmpty(metaData) && metaData.reportDetails && metaData.reportDetails.searchParams && metaData.reportDetails.searchParams.length > 0)
    {
      return metaData.reportDetails.searchParams.map((item,index) =>
      {
        item["value"]=_.isEmpty(searchForm)?"":searchForm[item.name];
        return (
          <ShowField key={index} obj={item} dateField={this.state.datefield} dateError={this.state.dateError} handler={this.handleChange}/>
        );
      })
    }
  }

  componentWillReceiveProps(nextProps){
    let {changeButtonText,clearReportHistory} = this.props;
    if(nextProps.metaData.reportDetails && (nextProps.metaData.reportDetails !== this.props.metaData.reportDetails)){
      changeButtonText("Generate Report");
      this.setState({reportName : nextProps.metaData.reportDetails.reportName});
      this.setState({moduleName : this.props.match.params.moduleName});
      let {initForm,setForm} = this.props;
      let {searchParams}=!_.isEmpty(nextProps.metaData) ? nextProps.metaData.reportDetails : {searchParams:[]};
      let required=[];
      for (var i = 0; i < searchParams.length; i++) {
        if(searchParams[i].isMandatory) {
          required.push(searchParams[i].name)
        }
      }
      setForm(required);
      clearReportHistory();

    }
  }

  componentDidMount()
  {
    let {initForm,metaData,setForm, changeButtonText,clearReportHistory} = this.props;
    changeButtonText("Generate Report");
    let searchParams =!_.isEmpty(metaData)?metaData.reportDetails:{searchParams:[]};
    let required=[];
    this.setState({reportName : this.props.match.params.reportName});
    this.setState({moduleName : this.props.match.params.moduleName});
    if (searchParams) {
      for (var i = 0; i < searchParams.length; i++) {
        if(searchParams[i].isMandatory) {
          required.push(searchParams[i].name)
        }
      }
    }

    setForm(required);
    clearReportHistory();
    //setForm(required);
  }

  search(e,isDrilldown=false)
  {
    e.preventDefault();
      let {showTable,changeButtonText,setReportResult,searchForm,metaData,setFlag,setSearchParams,reportHistory,reportIndex,pushReportHistory,clearReportHistory,decreaseReportIndex}=this.props;
      let searchParams=[];
      var tenantId = localStorage.getItem("tenantId") ? localStorage.getItem("tenantId") : '';
      let self=this;

      if (!isDrilldown) {
        // (variable=="fromDate"?new Date(Date(searchForm[variable]).getFullYear()+"-"+Date(searchForm[variable]).getMonth()+"-"+Date(searchForm[variable]).getDate()+" "+"00:00:00").getTime():new Date(Date(searchForm[variable]).getFullYear()+"-"+Date(searchForm[variable]).getMonth()+"-"+Date(searchForm[variable]).getDate()+" "+"23:59:00").getTime())

        for (var variable in searchForm) {
          // console.log(variable);
          let input;
          //  = this.state.moduleName=="pgr"
          //       ?
          // (((typeof(searchForm[variable])=="object") && (variable=="fromDate" || variable=="toDate"))?variable=="fromDate"?(new Date(searchForm[variable]).getFullYear() + "-" + (new Date(searchForm[variable]).getMonth()>8?(new Date(searchForm[variable]).getMonth()+1):("0"+(new Date(searchForm[variable]).getMonth()+1))) + "-" +(new Date(searchForm[variable]).getDate()>9?new Date(searchForm[variable]).getDate():"0"+new Date(searchForm[variable]).getDate())+" "+"00:00:00"):(new Date(searchForm[variable]).getFullYear() + "-" + (new Date(searchForm[variable]).getMonth()>8?(new Date(searchForm[variable]).getMonth()+1):("0"+(new Date(searchForm[variable]).getMonth()+1))) + "-" +(new Date(searchForm[variable]).getDate()>9?new Date(searchForm[variable]).getDate():"0"+new Date(searchForm[variable]).getDate())+" "+"23:59:59")
          //       :
          // searchForm[variable]):(((typeof(searchForm[variable])=="object")&& (variable=="fromDate" || variable=="toDate"))?new Date(searchForm[variable]).getTime():searchForm[variable])

          // console.log(variable , input);

          if (this.state.moduleName=="pgr") {
            if (variable=="fromDate") {
                input=searchForm[variable].getFullYear()+"-"+(searchForm[variable].getMonth()>8?(searchForm[variable].getMonth()+1):("0"+parseInt(searchForm[variable].getMonth()+1)))+"-"+(searchForm[variable].getDate()>9?(searchForm[variable].getDate()):("0"+searchForm[variable].getDate()))+" 00:00:00";
            } else if(variable=="toDate") {
                input=searchForm[variable].getFullYear()+"-"+(searchForm[variable].getMonth()>8?(searchForm[variable].getMonth()+1):("0"+parseInt(searchForm[variable].getMonth()+1)))+"-"+(searchForm[variable].getDate()>9?(searchForm[variable].getDate()):("0"+searchForm[variable].getDate()))+" 23:59:59";
            } else {
              input=searchForm[variable];
            }
          } else {
            if (variable=="fromDate") {
                input=searchForm[variable].setHours(0);
                input=searchForm[variable].setMinutes(0);
                input=searchForm[variable].setSeconds(0);

            } else if(variable=="toDate") {
              input=searchForm[variable].setHours(23);
              input=searchForm[variable].setMinutes(59);
              input=searchForm[variable].setSeconds(59);
            } else {
              input=searchForm[variable];
            }
          }

          if(input){
            searchParams.push({
              name:variable,
              input
            });
          }
        }

        // searchParams.push(
        //   {
        //       "name": "complainttype",
        //       "value": "BRKNB"
        //   })

        setSearchParams(searchParams);


        clearReportHistory();
        let response=Api.commonApiPost("/report/"+this.state.moduleName+"/_get",{},{tenantId:tenantId,reportName:this.state.reportName,searchParams}).then(function(response)
        {
          // console.log(response)
          pushReportHistory({tenantId:tenantId,reportName:self.state.reportName,searchParams})
          setReportResult(response);
          // console.log("Show Table");
          showTable(true);
          setFlag(1);

        },function(err) {
            // console.log(err);
            showTable(false);
            alert("Something went wrong or try again later");

        });

      } else {
        let reportData=reportHistory[(reportIndex-1)-1];
        let response=Api.commonApiPost("/report/"+this.state.moduleName+"/_get",{},{...reportData}).then(function(response)
        {
          // console.log(response)
          decreaseReportIndex();
          setReportResult(response);
          // console.log("Show Table");
          showTable(true);
          setFlag(1);
        },function(err) {
            // console.log(err);
            showTable(false);
            alert("Something went wrong or try again later");

        });
      }


      changeButtonText("Generate Report");
      // this.setState({searchBtnText:'Search Again'})

      //call api
      // setReportResult(resForm);

  }

  render() {
    let {
      searchForm,
      fieldErrors,
      isFormValid,
      isTableShow,
      handleChange,
      handleChangeNextOne,
      handleChangeNextTwo,
      buttonText,
      metaData,
      reportHistory,
      reportIndex
    } = this.props;
    let {search} = this;
    //console.log(metaData);
    // console.log(searchForm);
    console.log(reportHistory);
    console.log(reportIndex);
    return (
      <div className="">
        <form onSubmit={(e) => {
          search(e)
        }}>

        <Card style={styles.marginStyle}>
          <CardHeader style={{paddingBottom:0}} title={!_.isEmpty(metaData) && metaData.reportDetails ? metaData.reportDetails
.summary : ""}/>
          <CardText style={{padding:0}}>
          <Grid>
            <Row>
              {this.handleFormFields()}
            </Row>
          </Grid>
          </CardText>
        </Card>
        <div style={{"textAlign": "center"}}>
          <br/>
            <RaisedButton type="submit" disabled={!isFormValid} primary={true} label={buttonText} />
          <br/>
          <br/>
        </div>
        </form>
        {reportIndex>1 && <div style={{"textAlign": "right",
    "paddingRight": "15px"}}>
          <br/>
            <RaisedButton type="button" onClick={(e)=>{
              search(e,true)
            }} primary={true} label={"Back"} />
          <br/>
          <br/>
        </div>}
      </div>
    );
  }
}

const mapStateToProps = state => {
  // console.log(state.form.buttonText);
  return ({searchForm: state.form.form, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid,isTableShow:state.form.showTable,buttonText:state.form.buttonText,metaData:state.report.metaData,reportHistory:state.report.reportHistory,reportIndex:state.report.reportIndex});
}

const mapDispatchToProps = dispatch => ({
  setForm: (required=[],pattern=[]) => {
    dispatch({
      type: "SET_FORM",
      form:{},
      fieldErrors:{},
      isFormValid: required.length > 0 ? false : true,
      validationData: {
        required: {
          current: [],
          required: required
        },
        pattern: {
          current: [],
          required: []
        }
      }
    });
  },
  initForm: (required=[]) => {
    dispatch({
      type: "RESET_STATE",
      validationData: {
        required: {
          current: [],
          required: required
        },
        pattern: {
          current: [],
          required: []
        }
      }
    });
  },
  handleChange: (e, property, isRequired, pattern) => {
      dispatch({type: "HANDLE_CHANGE", property, value: e.target.value, isRequired, pattern});
  },
  handleChangeNextOne: (e, property, propertyOne, isRequired, pattern) => {
    dispatch({
      type: "HANDLE_CHANGE_NEXT_ONE",
      property,
      propertyOne,
      value: e.target.value,
      isRequired,
      pattern
    })
  },
  handleChangeNextTwo: (e, property, propertyOne, propertyTwo, isRequired, pattern) => {
    dispatch({
      type: "HANDLE_CHANGE_NEXT_ONE",
      property,
      propertyOne,
      propertyTwo,
      value: e.target.value,
      isRequired,
      pattern
    })
  },
  showTable:(state)=>
  {
    dispatch({type:"SHOW_TABLE",state});
  },
  changeButtonText:(text)=>
  {
    dispatch({type:"BUTTON_TEXT",text});
  },
  toggleDailogAndSetText: (dailogState,msg) => {
    dispatch({type: "TOGGLE_DAILOG_AND_SET_TEXT", dailogState,msg});
  },
  setReportResult:(reportResult)=>{
    dispatch({type:"SET_REPORT_RESULT",reportResult})
  },
  setFlag:(flag)=>{
      dispatch({type:"SET_FLAG",flag})
  },
  setMetaData:(metaData)=>{
    dispatch({type:"SET_META_DATA",metaData})
  },
  setSearchParams:(searchParams)=>{
    dispatch({type:"SET_SEARCH_PARAMS",searchParams})
  },
  pushReportHistory:(history)=>{
    dispatch({type:"PUSH_REPORT_HISTORY",reportData:history})
  },
  clearReportHistory:()=>{
    dispatch({type:"CLEAR_REPORT_HISTORY"})
  },
  decreaseReportIndex:()=>{
    dispatch({type:"DECREASE_REPORT_INDEX"})
  }
});

export default connect(mapStateToProps, mapDispatchToProps)(ShowForm);


// <Card>
//     <CardHeader title={< strong style = {{color:"#5a3e1b"}} > Search Property < /strong>}/>
//
//     <CardText>
//       <Card>
//         <CardText>
//           <Grid>
//             <Row>
//               <Col xs={12} md={6}>
//                 <TextField errorText={fieldErrors.doorNo
//                   ? fieldErrors.doorNo
//                   : ""} id="doorNo" value={propertyTaxSearch.doorNo?propertyTaxSearch.doorNo:""} onChange={(e) => handleChange(e, "doorNo", false, /^([\d,/.\-]){6,10}$/g)} hintText="eg:-3233312323" floatingLabelText="Door number" />
//               </Col>
//
//               <Col xs={12} md={6}>
//                 <TextField errorText={fieldErrors.assessmentNo
//                   ? fieldErrors.assessmentNo
//                   : ""} value={propertyTaxSearch.assessmentNo?propertyTaxSearch.assessmentNo:""} onChange={(e) => handleChange(e, "assessmentNo", false, /^\d{3,15}$/g)} hintText="eg:-123456789123456" floatingLabelText="Assessment number"/>
//               </Col>
//             </Row>
//
//             <Row>
//               <Col xs={12} md={6}>
//                 <TextField errorText={fieldErrors.mobileNo
//                   ? fieldErrors.mobileNo
//                   : ""} value={propertyTaxSearch.mobileNo?propertyTaxSearch.mobileNo:""} onChange={(e) => handleChange(e, "mobileNo", false, /^\d{10}$/g)} hintText="Mobile number" floatingLabelText="Mobile number" />
//               </Col>
//
//               <Col xs={12} md={6}>
//                 <TextField errorText={fieldErrors.aadharNo
//                   ? fieldErrors.aadharNo
//                   : ""} value={propertyTaxSearch.aadharNo?propertyTaxSearch.aadharNo:""} onChange={(e) => handleChange(e, "aadharNo", false, /^\d{12}$/g)} hintText="Aadhar number " floatingLabelText="Aadhar number " />
//               </Col>
//             </Row>
//           </Grid>
//
//         </CardText>
//       </Card>
//
//       <Card>
//         <CardHeader title={< strong style = {{color:"#5a3e1b"}} > Advance Search < /strong>} actAsExpander={true} showExpandableButton={true}/>
//
//         <CardText expandable={true}>
//           <Grid>
//             <Row>
//               <Col xs={12} md={6}>
//                 <TextField errorText={fieldErrors.ownerName
//                   ? fieldErrors.ownerName
//                   : ""} value={propertyTaxSearch.ownerName?propertyTaxSearch.ownerName:""} onChange={(e) => handleChange(e, "ownerName", false, "")} hintText="Owner Name" floatingLabelText="Owner Name" />
//               </Col>
//
//               <Col xs={12} md={6}>
//                 <TextField errorText={fieldErrors.oldAssessmentNo
//                   ? fieldErrors.oldAssessmentNo
//                   : ""} value={propertyTaxSearch.oldAssessmentNo?propertyTaxSearch.oldAssessmentNo:""} onChange={(e) => handleChange(e, "oldAssessmentNo", false, /^\d{3,15}$/g)} hintText="Old Assessment Number" floatingLabelText="Old Assessment Number" />
//               </Col>
//             </Row>
//
//             <Row>
//
//               <Card>
//                 <CardHeader title={< strong style = {{color:"#5a3e1b"}} > Boundary < /strong>}/>
//
//                 <CardText>
//                   <Grid>
//                     <Row>
//                       <Col xs={12} md={6}>
//
//                         <SelectField errorText={fieldErrors.zone
//                           ? fieldErrors.zone
//                           : ""} value={propertyTaxSearch.zone?propertyTaxSearch.zone:""} onChange={(event, index, value) => {
//                             var e = {
//                               target: {
//                                 value: value
//                               }
//                             };
//                             handleChange(e, "zone", false, "")}} floatingLabelText="Zone	Drop " >
//                           <MenuItem value={1} primaryText="Never"/>
//                           <MenuItem value={2} primaryText="Every Night"/>
//                           <MenuItem value={3} primaryText="Weeknights"/>
//                           <MenuItem value={4} primaryText="Weekends"/>
//                           <MenuItem value={5} primaryText="Weekly"/>
//                         </SelectField>
//
//                       </Col>
//
//                       <Col xs={12} md={6}>
//                         <SelectField errorText={fieldErrors.ward
//                           ? fieldErrors.ward
//                           : ""} value={propertyTaxSearch.ward?propertyTaxSearch.ward:""} onChange={(event, index, value) =>{
//                             var e = {
//                               target: {
//                                 value: value
//                               }
//                             };
//                             handleChange(e, "ward", false, "")}
//                           } floatingLabelText="Ward" >
//                           <MenuItem value={1} primaryText="Never"/>
//                           <MenuItem value={2} primaryText="Every Night"/>
//                           <MenuItem value={3} primaryText="Weeknights"/>
//                           <MenuItem value={4} primaryText="Weekends"/>
//                           <MenuItem value={5} primaryText="Weekly"/>
//                         </SelectField>
//                       </Col>
//                     </Row>
//
//                     <Row>
//                       <Col xs={12} md={6}>
//                         <SelectField errorText={fieldErrors.location
//                           ? fieldErrors.location
//                           : ""} value={propertyTaxSearch.location?propertyTaxSearch.location:""} onChange={(event, index, value) => {
//                             var e = {
//                               target: {
//                                 value: value
//                               }
//                             };
//                             handleChange(e, "location", false, "")}} floatingLabelText="Location" >
//                           <MenuItem value={1} primaryText="Never"/>
//                           <MenuItem value={2} primaryText="Every Night"/>
//                           <MenuItem value={3} primaryText="Weeknights"/>
//                           <MenuItem value={4} primaryText="Weekends"/>
//                           <MenuItem value={5} primaryText="Weekly"/>
//                         </SelectField>
//                       </Col>
//
//                     </Row>
//                   </Grid>
//
//                 </CardText>
//               </Card>
//
//             </Row>
//
//             <Row>
//               <Card>
//                 <CardHeader title={< strong style = {{color:"#5a3e1b"}} > Search Property by Demand < /strong>}/>
//                 <CardText>
//                   <Grid>
//                     <Row>
//                       <Col xs={12} md={6}>
//                       <TextField errorText={fieldErrors.demandFrom
//                         ? fieldErrors.demandFrom
//                         : ""} value={propertyTaxSearch.demandFrom?propertyTaxSearch.demandFrom:""} onChange={(e) => handleChange(e, "demandFrom", false, /^\d$/g)} hintText="Demand From" floatingLabelText="Demand From" />
//
//
//                       </Col>
//
//                       <Col xs={12} md={6}>
//                       <TextField errorText={fieldErrors.demandTo
//                         ? fieldErrors.demandTo
//                         : ""} value={propertyTaxSearch.demandTo?propertyTaxSearch.demandTo:""} onChange={(e) => handleChange(e, "demandTo", false, /^\d$/g)} hintText="Demand To" floatingLabelText="Demand To" />
//
//
//                       </Col>
//                     </Row>
//                   </Grid>
//                 </CardText>
//               </Card>
//             </Row>
//
//           </Grid>
//         </CardText>
//       </Card>
//       <div style={{
//         float: "center"
//       }}>
//         <RaisedButton type="submit" disabled={!isFormValid} label={buttonText} backgroundColor={"#5a3e1b"} labelColor={white}/>
//         <RaisedButton label="Close"/>
//       </div>
//     </CardText>
//   </Card>
