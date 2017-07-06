import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import TextField from 'material-ui/TextField';
import {brown500, red500,white,orange800} from 'material-ui/styles/colors';
import DatePicker from 'material-ui/DatePicker';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import RaisedButton from 'material-ui/RaisedButton';
import Api from '../../../api/api';
import ShowField from './showField';


const styles = {
  errorStyle: {
    color: red500
  },
  underlineStyle: {
    borderColor: brown500
  },
  underlineFocusStyle: {
    borderColor: brown500
  },
  floatingLabelStyle: {
    color: brown500
  },
  floatingLabelFocusStyle: {
    color: brown500
  },
  customWidth: {
    width:100
  }
};

class ShowForm extends Component {
  constructor(props) {
       super(props);
       this.state = {
         searchBtnText : 'Search'
       }
       this.search=this.search.bind(this);
   }


  handleFormFields = () => {
    let {currentThis}=this;
    let {metaData}=this.props;
    if(metaData.reportDetails.searchParams.length > 0)
    {
      return metaData.reportDetails.searchParams.map((item,index) =>
      {
        return (
          <ShowField key={index} obj={item} handler={this.props.handleChange}/>
        );
      })
    }
  }

  componentDidMount()
  {
    let {initForm,metaData,setForm} = this.props;
    let {searchParams}=metaData.reportDetails;
    let required=[];
    for (var i = 0; i < searchParams.length; i++) {
      if (searchParams.isMandatory) {
        required.push(searchParams.name)
      }
    }
    initForm();
    setForm(required);
  }


  search(e)
  {
      let {showTable,changeButtonText,setReportResult}=this.props;
      let reqForm={
              "responseInfo": {
                "apiId": "emp",
                "ver": "1.0",
                "ts": "Thu Mar 09 18:30:00 GMT 2017",
                "resMsgId": "uief87324",
                "msgId": "20170310130900",
                "status": "200"
              },
              "tenantId": "tenantid",
              "reportName": "GrievanceByType",
              "searchParams": [
                {
                  "name": "fromDate",
                  "value": "03-06-2017"
                },
                {
                  "name": "toDate",
                  "value": "03-07-2017"
                },
                {
                  "name": "complainttype",
                  "value": "DKK3"
                }
              ]
        }
      let resForm=[
                    ["Nuisance by garbage tractors or trucks",
                        0,
                        0,
                        0,
                        0,
                        0,
                        2,
                        0,
                        2
                      ],
                      ["Repairs to existing footpath",
                        0,
                        0,
                        0,
                        0,
                        0,
                        2,
                        0,
                        2
                      ],
                      ["Non Receipt of Pensions (Disabled/ Old age/ Widow)",
                        0,
                        0,
                        0,
                        0,
                        0,
                        2,
                        0,
                        2
                      ],
                      ["Engineering-Others",
                        0,
                        0,
                        0,
                        0,
                        0,
                        2,
                        0,
                        2
                      ],
                      ["Death of Stray Animals",
                        0,
                        0,
                        0,
                        0,
                        0,
                        2,
                        0,
                        2
                      ],
                      ["Complaints regarding Schools",
                        0,
                        0,
                        0,
                        0,
                        0,
                        2,
                        0,
                        2
                      ],
                      ["Illegal slaughtering",
                        0,
                        0,
                        0,
                        0,
                        0,
                        2,
                        0,
                        2
                      ],
                      ["New Drainage Connection",
                        0,
                        0,
                        0,
                        0,
                        0,
                        2,
                        0,
                        2
                      ],
                      ["Revenue-Others",
                        0,
                        0,
                        0,
                        0,
                        0,
                        2,
                        0,
                        2
                      ]
      ]



      e.preventDefault();
      // console.log("Show Table");

      changeButtonText("Search Again");
      // this.setState({searchBtnText:'Search Again'})

      //call api
      setReportResult(resForm);
      showTable(true);
  }

  render() {
    let {
      propertyTaxSearch,
      fieldErrors,
      isFormValid,
      isTableShow,
      handleChange,
      handleChangeNextOne,
      handleChangeNextTwo,
      buttonText,
      metaData
    } = this.props;
    let {search} = this;
    // console.log(metaData);
    console.log(propertyTaxSearch);
    return (
      <div className="PropertyTaxSearch">
        <form onSubmit={(e) => {
          search(e)
        }}>

        <Card style={styles.marginStyle}>
          <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} > {metaData.reportDetails?metaData.reportDetails
.summary:""} < /div>}/>
          <CardText style={{padding:0}}>
          <Grid>
            <Row>
              {this.handleFormFields()}
            </Row>
          </Grid>
          </CardText>
        </Card>
        <RaisedButton type="submit" disabled={isFormValid}  label={buttonText} />
        </form>

      </div>
    );
  }
}

const mapStateToProps = state => ({propertyTaxSearch: state.form.form, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid,isTableShow:state.form.showTable,buttonText:state.form.buttonText,metaData:state.report.metaData});

const mapDispatchToProps = dispatch => ({
  setForm: (required=[],pattern=[]) => {
    dispatch({
      type: "SET_FORM",
      form:{},
      fieldErrors:{},
      isFormValid:false,
      validationData: {
        required: {
          current: [],
          required: required
        },
        pattern: {
          current: [],
          required: pattern
        }
      }
    });
  },
  initForm: () => {
    dispatch({
      type: "RESET_STATE",
      validationData: {
        required: {
          current: [],
          required: [ ]
        },
        pattern: {
          current: [],
          required: ["assessmentNo", "oldAssessmentNo", "mobileNo", "aadharNo", "doorNo"]
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
