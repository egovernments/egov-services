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
import Api from '../../../../api/api';


const $ = require('jquery');
$.DataTable = require('datatables.net');
const dt = require('datatables.net-bs');


const buttons = require('datatables.net-buttons-bs');

require('datatables.net-buttons/js/buttons.colVis.js'); // Column visibility
require('datatables.net-buttons/js/buttons.html5.js'); // HTML 5 file export
require('datatables.net-buttons/js/buttons.flash.js'); // Flash file export
require('datatables.net-buttons/js/buttons.print.js'); // Print view button

var flag = 0;
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


const getNameById = function(object, id, property = "") {
  if (id == "" || id == null) {
        return "";
    }
    for (var i = 0; i < object.length; i++) {
        if (property == "") {
            if (object[i].id == id) {
                return object[i].name;
            }
        } else {
            if (object[i].hasOwnProperty(property)) {
                if (object[i].id == id) {
                    return object[i][property];
                }
            } else {
                return "";
            }
        }
    }
    return "";
}

const getNameByCode = function(object, code, property = "") {
  if (code == "" || code == null) {
        return "";
    }
    for (var i = 0; i < object.length; i++) {
        if (property == "") {
            if (object[i].code == code) {
                return object[i].name;
            }
        } else {
            if (object[i].hasOwnProperty(property)) {
                if (object[i].code == code) {
                    return object[i][property];
                }
            } else {
                return "";
            }
        }
    }
    return "";
}

class PropertyTaxSearch extends Component {
  constructor(props) {
       super(props);
       this.state = {
         searchBtnText : 'Search',
		 zone:[],
		 ward:[],
		 location:[],
		 revenueCircle:[],
		 resultList:[],
		 usage:[],
		 propertytypes:[]
       }
       this.search=this.search.bind(this);
   }

  componentWillMount()
  {

    //call boundary service fetch wards,location,zone data
  }

  componentDidMount()
  {
    let {initForm} = this.props;
    initForm();
    let {toggleDailogAndSetText}=this.props;
   
	 var currentThis = this;

        Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"LOCALITY", hierarchyTypeName:"LOCATION"}).then((res)=>{
          currentThis.setState({location : res.Boundary})
        }).catch((err)=> {
           currentThis.setState({
            location : []
          })
          console.log(err)
        })
		
		
        Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"ZONE", hierarchyTypeName:"REVENUE"}).then((res)=>{
          console.log(res);
          currentThis.setState({zone : res.Boundary})
        }).catch((err)=> {
           currentThis.setState({
            zone : []
          })
          console.log(err)
        })

        Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"WARD", hierarchyTypeName:"REVENUE"}).then((res)=>{
          currentThis.setState({ward : res.Boundary})
        }).catch((err)=> {
          currentThis.setState({
            ward : []
          })
        })
		
		Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"REVENUE", hierarchyTypeName:"REVENUE"}).then((res)=>{
			currentThis.setState({revenueCircle : res.Boundary})
        }).catch((err)=> {
			currentThis.setState({revenueCircle : []})
        })
		
		Api.commonApiPost('pt-property/property/usages/_search').then((res)=>{
			currentThis.setState({usage : res.usageMasters})
        }).catch((err)=> {
			currentThis.setState({usage : []})
          console.log(err)
        })
		
		Api.commonApiPost('pt-property/property/propertytypes/_search',{}, {},false, true).then((res)=>{
			currentThis.setState({propertytypes:res.propertyTypes})
		}).catch((err)=> {
			currentThis.setState({
			  propertytypes:[]
			})
			console.log(err)
		})
		
   
  }

  componentWillUnmount(){
     $('#propertyTaxTable')
     .DataTable()
     .destroy(true);
  }
  
  handleSearchValue = () => {
	  
  }



  search(e)
  {
      let {showTable,changeButtonText, propertyTaxSearch, setLoadingStatus, toggleSnackbarAndSetText }=this.props;
      e.preventDefault();
	  
	  setLoadingStatus('loading');
	  
	var query = propertyTaxSearch;
	  
      Api.commonApiPost('pt-property/properties/_search', query,{}, false, true).then((res)=>{   
		setLoadingStatus('hide');
		if(res.hasOwnProperty('Errors')){
			toggleSnackbarAndSetText(true, "Something went wrong. Please try again.")
		} else {
			
			console.log(res.properties[0].channel)
			
			flag=1;
			changeButtonText("Search Again");
			this.setState({
				searchBtnText:'Search Again',
				resultList:res.properties
			})
			showTable(true);
		}
	
      }).catch((err)=> {
			setLoadingStatus('hide');
			toggleSnackbarAndSetText(true, err.message)
      })
		 
     
  }

  componentWillUpdate() {
    if(flag == 1) {
      flag = 0;
      $('#propertyTaxTable').dataTable().fnDestroy();
    }
  }

  componentDidUpdate(prevProps, prevState) {
      if (true) {
          $('#propertyTaxTable').DataTable({
            dom: 'lBfrtip',
            buttons: [
                     'copy', 'csv', 'excel', 'pdf', 'print'
             ],
             ordering: false,
             bDestroy: true,

          });
      }
  }

  render() {
	  
	  const renderOption = function(list,listName="") {
        if(list)
        {
            return list.map((item)=>
            {
                return (<MenuItem key={item.id} value={item.id} primaryText={item.name}/>)
            })
        }
    }
	  
    let {
      propertyTaxSearch,
      fieldErrors,
      isFormValid,
      isTableShow,
      handleChange,
      handleChangeNextOne,
      handleChangeNextTwo,
      buttonText
    } = this.props;
	
    let {search} = this;

	let currentThis = this;
	
	let {history} = this.props;
	
    const viewTable=()=>
    {
      return (
        <Card className="uiCard">
          <CardHeader title={< span style = {{ color: 'rgb(53, 79, 87)',fontSize: 18, margin: '8px 0px', fontWeight: 500,}} > Result < /span>}/>
          <CardText>
        <Table id="propertyTaxTable" style={{color:"black",fontWeight: "normal"}} bordered responsive>
          <thead>
            <tr>
              <th>#</th>
              <th>Assessment Number</th>
              <th>Owner Name</th>
			  <th>Door Number</th>
			  <th>Locality</th>
			  <th>Revenue Circle</th>
              <th>Address</th>
              <th>Current Demand</th>
              <th>Arrears Demand</th>
			  <th>Property Type</th>
              <th>Property sub type</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
          {this.state.resultList.length != 0 && this.state.resultList.map((item, index)=>{
			  if(item){
				  return(<tr>
					  <td>{index+1}</td> 
					  <td style={{color:'blue'}} onClick={() => {
						   history.push(`/propertyTax/view-property/${item.upicNumber}`);
					  }}>{item.upicNumber || ''}</td>
					  <td>{(item.owners.length != 0) &&  item.owners.map((item, index)=>{
						  return(<span>{item.name}</span>)
					  })}</td>
					  <td>{item.address.addressNumber || ''}</td>
					  <td>{item.address.addressLine1 || ''}</td>
					  <td>-</td>
					  <td>{item.address.addressNumber? item.address.addressNumber+', ' : ''} {item.address.addressLine1 ? item.address.addressLine1+', ' : ''} 
						  {item.address.addressLine2 ? item.address.addressLine2+', ':''}{item.address.landmark ? item.address.landmark+',':''}
						  {item.address.city ? item.address.city : ''}
						  </td>
					  <td>0</td>
					  <td>0</td>
					  <td>{getNameByCode(currentThis.state.propertytypes ,item.propertyDetail.propertyType) || ''}</td>
					  <td>{item.propertyDetail.category || ''}</td>
					  <td>
						<DropdownButton title="Action" id="dropdown-3" pullRight>
							<MenuItem onClick={()=>{
								history.push(`/propertyTax/view-property/${item.id}/view`);
							}}>View</MenuItem>
						</DropdownButton>
					  </td>
					</tr>)
			  }
		  })}
          </tbody>
        </Table>
      </CardText>
      </Card>
      )
    }
    return (
      <div className="PropertyTaxSearch">
        <form onSubmit={(e) => {
          search(e)
        }}>
          <Card className="uiCard">
            <CardHeader title={< span style = {{ color: 'rgb(53, 79, 87)',fontSize: 18, margin: '8px 0px', fontWeight: 500,}} >  Search Property < /span>}/>
            <CardText>
             
                  <Grid>
					<Row>
						<Col xs={12} md={6}>
                          <TextField errorText={fieldErrors.applicationNo
                          ? fieldErrors.applicationNo
                          : ""} id="applicationNo" value={propertyTaxSearch.applicationNo?propertyTaxSearch.applicationNo:""} onChange={(e) => handleChange(e, "applicationNo", false, '')} hintText="AP-PT-2017/07/29-004679-17" floatingLabelText="Application number" />
                      </Col>
					</Row>
                    <Row>
                      <Col xs={12} md={6}>
                        <TextField errorText={fieldErrors.houseNoBldgApt
                          ? fieldErrors.houseNoBldgApt
                          : ""} id="houseNoBldgApt" value={propertyTaxSearch.houseNoBldgApt?propertyTaxSearch.houseNoBldgApt:""} onChange={(e) => handleChange(e, "houseNoBldgApt", false, /^\d{1,10}$/g)} hintText="eg:-3233312323" floatingLabelText="Door number" />
                      </Col>

                      <Col xs={12} md={6}>
                        <TextField errorText={fieldErrors.upicNumber
                          ? fieldErrors.upicNumber
                          : ""} value={propertyTaxSearch.upicNumber?propertyTaxSearch.upicNumber:""} onChange={(e) => handleChange(e, "upicNumber", false, /^\d{3,15}$/g)} hintText="eg:-123456789123456" floatingLabelText="Assessment number"/>
                      </Col>
                    </Row>

                    <Row>
                      <Col xs={12} md={6}>
                        <TextField errorText={fieldErrors.mobileNumber
                          ? fieldErrors.mobileNumber
                          : ""} value={propertyTaxSearch.mobileNumber?propertyTaxSearch.mobileNumber:""} onChange={(e) => handleChange(e, "mobileNumber", false, /^\d{10}$/g)} hintText="Mobile number" floatingLabelText="Mobile number" />
                      </Col>

                      <Col xs={12} md={6}>
                        <TextField errorText={fieldErrors.aadhaarNumber
                          ? fieldErrors.aadhaarNumber
                          : ""} value={propertyTaxSearch.aadhaarNumber?propertyTaxSearch.aadhaarNumber:""} onChange={(e) => handleChange(e, "aadhaarNumber", false, /^\d{12}$/g)} hintText="Aadhar number " floatingLabelText="Aadhar number " />
                      </Col>
                    </Row>
                  </Grid>
                </CardText>
              </Card>

              <Card className="uiCard">
                <CardHeader title={<span style = {{ color: 'rgb(53, 79, 87)',fontSize: 18, margin: '8px 0px', fontWeight: 500,}} >  Advance Search < /span>} actAsExpander={true} showExpandableButton={true}/>

                <CardText expandable={true}>
                  <Grid>
                    <Row>
                      <Col xs={12} md={6}>
                        <TextField errorText={fieldErrors.ownerName
                          ? fieldErrors.ownerName
                          : ""} value={propertyTaxSearch.ownerName?propertyTaxSearch.ownerName:""} onChange={(e) => handleChange(e, "ownerName", false, "")} hintText="Owner Name" floatingLabelText="Owner Name" />
                      </Col>

                      <Col xs={12} md={6}>
                        <TextField errorText={fieldErrors.oldUpicNo
                          ? fieldErrors.oldUpicNo
                          : ""} value={propertyTaxSearch.oldUpicNo?propertyTaxSearch.oldUpicNo:""} onChange={(e) => handleChange(e, "oldUpicNo", false, /^\d{3,15}$/g)} hintText="Old Assessment Number" floatingLabelText="Old Assessment Number" />
                      </Col>
					  <Col xs={12} md={6}>
							<SelectField errorText={fieldErrors.usage
							  ? fieldErrors.usage
							  : ""} value={propertyTaxSearch.usage?propertyTaxSearch.usage:""} onChange={(event, index, value) => {
								var e = {
								  target: {
									value: value
								  }
								};
								handleChange(e, "usage", false, "")}} floatingLabelText="Usage Type" >
								{renderOption(this.state.usage)}
							</SelectField>
                      </Col>
                    </Row>

                    <Row>
						<br/>
                      <Card>
                        <CardHeader title={<span style = {{ color: 'rgb(53, 79, 87)',fontSize: 18, margin: '8px 0px', fontWeight: 500,}}> Boundary < /span>}/>

                        <CardText>
                          <Grid>
                            <Row>
                              <Col xs={12} md={6}>

                                <SelectField errorText={fieldErrors.revenueZone
                                  ? fieldErrors.revenueZone
                                  : ""} value={propertyTaxSearch.revenueZone?propertyTaxSearch.revenueZone:""} onChange={(event, index, value) => {
                                    var e = {
                                      target: {
                                        value: value
                                      }
                                    };
                                    handleChange(e, "revenueZone", false, "")}} floatingLabelText="Zone " >
									{renderOption(this.state.zone)}
                                </SelectField>

                              </Col>

                              <Col xs={12} md={6}>
                                <SelectField errorText={fieldErrors.revenueWard
                                  ? fieldErrors.revenueWard
                                  : ""} value={propertyTaxSearch.revenueWard?propertyTaxSearch.revenueWard:""} onChange={(event, index, value) =>{
                                    var e = {
                                      target: {
                                        value: value
                                      }
                                    };
                                    handleChange(e, "revenueWard", false, "")}
                                  } floatingLabelText="Ward" >
                                  {renderOption(this.state.ward)}
                                </SelectField>
                              </Col>
                            </Row>

                            <Row>
                              <Col xs={12} md={6}>
                                <SelectField errorText={fieldErrors.locality
                                  ? fieldErrors.locality
                                  : ""} value={propertyTaxSearch.locality?propertyTaxSearch.locality:""} onChange={(event, index, value) => {
                                    var e = {
                                      target: {
                                        value: value
                                      }
                                    };
                                    handleChange(e, "locality", false, "")}} floatingLabelText="Location" >
									{renderOption(this.state.location)}
                                </SelectField>
                              </Col>
							  
							  <Col xs={12} md={6}>
                                <SelectField errorText={fieldErrors.revenueCircle
                                  ? fieldErrors.revenueCircle
                                  : ""} value={propertyTaxSearch.revenueCircle?propertyTaxSearch.revenueCircle:""} onChange={(event, index, value) => {
                                    var e = {
                                      target: {
                                        value: value
                                      }
                                    };
                                    handleChange(e, "revenueCircle", false, "")}} floatingLabelText="Revenue Circle" >
									{renderOption(this.state.revenueCircle)}
                                </SelectField>
                              </Col>

                            </Row>
                          </Grid>

                        </CardText>
                      </Card>

                    </Row>

                    <Row>
						<br/>
                      <Card>
                        <CardHeader title={<span style = {{ color: 'rgb(53, 79, 87)',fontSize: 18, margin: '8px 0px', fontWeight: 500,}} > Search Property by Demand < /span>}/>
                        <CardText>
                          <Grid>
                            <Row>
                              <Col xs={12} md={6}>
                              <TextField errorText={fieldErrors.demandFrom
                                ? fieldErrors.demandFrom
                                : ""} value={propertyTaxSearch.demandFrom?propertyTaxSearch.demandFrom:""} onChange={(e) => handleChange(e, "demandFrom", false, /^\d$/g)} hintText="Demand From" floatingLabelText="Demand From" />


                              </Col>

                              <Col xs={12} md={6}>
                              <TextField errorText={fieldErrors.demandTo
                                ? fieldErrors.demandTo
                                : ""} value={propertyTaxSearch.demandTo?propertyTaxSearch.demandTo:""} onChange={(e) => handleChange(e, "demandTo", false, /^\d$/g)} hintText="Demand To" floatingLabelText="Demand To" />


                              </Col>
                            </Row>
                          </Grid>
                        </CardText>
                      </Card>
                    </Row>
					

                  </Grid>
              
             
            </CardText>
          </Card>
		  
			 <div style={{
                float: "center",
				margin:'15px',
				textAlign:'right'
              }}>
                <RaisedButton type="submit" disabled={!isFormValid} primary={true} label={buttonText} />
                
              </div>


                  {isTableShow?viewTable():""}



        </form>

      </div>
    );
  }
}

const mapStateToProps = state => ({propertyTaxSearch: state.form.form, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid,isTableShow:state.form.showTable,buttonText:state.form.buttonText});

const mapDispatchToProps = dispatch => ({
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
          required: ["upicNumber", "oldUpicNo", "mobileNumber", "aadhaarNumber", "houseNoBldgApt"]
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
  setLoadingStatus: (loadingStatus) => {
     dispatch({type: "SET_LOADING_STATUS", loadingStatus});
   },
  toggleDailogAndSetText: (dailogState,msg) => {
    dispatch({type: "TOGGLE_DAILOG_AND_SET_TEXT", dailogState,msg});
  },
    toggleSnackbarAndSetText: (snackbarState, toastMsg) => {
     dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState, toastMsg});
   }
});

export default connect(mapStateToProps, mapDispatchToProps)(PropertyTaxSearch);
