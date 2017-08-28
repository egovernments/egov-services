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
import {translate} from '../../../common/common';
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
  if (id == "" || id == null || typeof(object) !== 'object') {
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
  if (code == "" || code == null || typeof(object) !== 'object') {
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
		 propertytypes:[],
		 showDcb : false,
		 demands:''
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
	  
	  let current = this;
	  
	var query = propertyTaxSearch;
	  
      Api.commonApiPost('pt-property/properties/_search', query,{}, false, true).then((res)=>{   
		setLoadingStatus('hide');
		if(res.hasOwnProperty('Errors')){
			toggleSnackbarAndSetText(true, "Something went wrong. Please try again.")
			current.setState({
				resultList:[]
			})
		} else {
			
			if(res.hasOwnProperty('properties') && res.properties.length !=0 && res.properties[0].channel == 'DATA_ENTRY') {
				current.setState({
					showDcb: true
				})
			} else {
				current.setState({
					showDcb: false
				})
			}
			
			flag=1;
			changeButtonText("Search Again");
			current.setState({
				searchBtnText:'Search Again',
				resultList:res.properties
			})
			
			if(res.hasOwnProperty('properties') && res.properties.length !=0){
				var tQuery = {
					businessService :'PT',
					consumerCode: res.properties[0].upicNumber || res.properties[0].propertyDetail.applicationNo
				}		
		
				Api.commonApiPost('billing-service/demand/_dues', tQuery, {}).then((res)=>{
					console.log('demands',res);
					current.setState({demands : res.DemandDue})
				}).catch((err)=> {
					current.setState({demands : ''})
					console.log(err)
				})
			}
			
			showTable(true);
		}
	
      }).catch((err)=> {
			setLoadingStatus('hide');
//toggleSnackbarAndSetText(true, err.message);
			current.setState({
				resultList:[]
			})
      })
		 
     
  }

  componentWillUpdate() {
      $('#propertyTaxTable').dataTable().fnDestroy();
  }

  componentDidUpdate(prevProps, prevState) {
 
          $('#propertyTaxTable').DataTable({
            dom: 'lBfrtip',
            buttons: [
                     'copy', 'csv', 'excel', 'pdf', 'print'
             ],
             ordering: false,
             bDestroy: true,

          });
  }

  render() {
	  
	  console.log(this.state.resultList);
	  
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
					  <td>{(item.hasOwnProperty('owners') && item.owners.length != 0) &&  item.owners.map((item, index)=>{
						  return(<span>{item.name}</span>)
					  })}</td>
					  <td>{item.address.addressNumber || ''}</td>
					  <td>{getNameById(this.state.location,item.address.addressLine1) || ''}</td>
					  <td>{item.address.addressNumber? item.address.addressNumber+', ' : ''} {item.address.addressLine1 ? item.address.addressLine1+', ' : ''} 
						  {item.address.addressLine2 ? item.address.addressLine2+', ':''}{item.address.landmark ? item.address.landmark+',':''}
						  {item.address.city ? item.address.city : ''}
						  </td>
					  <td>{this.state.demands.hasOwnProperty('consolidatedTax') ? this.state.demands.consolidatedTax.currentDemand : ''}</td>
					  <td>{this.state.demands.hasOwnProperty('consolidatedTax') ? this.state.demands.consolidatedTax.arrearsDemand : ''}</td>
					  <td>{getNameByCode(this.state.propertytypes, item.propertyDetail.propertyType) || ''}</td>
					  <td>{item.propertyDetail.category || ''}</td>
					  <td>
						<DropdownButton title="Action" id="dropdown-3" pullRight>
							{this.state.showDcb ? 
							<span><MenuItem onClick={()=>{
								history.push(`/propertyTax/view-property/${item.id}/view`);
							}}>View</MenuItem>
							<MenuItem onClick={()=>{
								history.push(`/propertyTax/addDemand/${item.upicNumber}`);
							}}>Add/Edit DCB</MenuItem></span>
							: 
							<MenuItem onClick={()=>{
								history.push(`/propertyTax/view-property/${item.id}/view`);
							}}>View</MenuItem> }
							
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
            <CardHeader title={< span style = {{ color: 'rgb(53, 79, 87)',fontSize: 18, margin: '8px 0px', fontWeight: 500,}} >{translate('pt.search.searchProperty')}< /span>}/>
            <CardText>
                  <Grid>
					<Row>
						<Col xs={12} md={6}>
                          <TextField errorText={fieldErrors.applicationNo
                          ? fieldErrors.applicationNo
                          : ""} id="applicationNo" floatingLabelFixed={true} value={propertyTaxSearch.applicationNo?propertyTaxSearch.applicationNo:""} onChange={(e) => handleChange(e, "applicationNo", false, '')} hintText="AP-PT-2017/07/29-004679-17" floatingLabelText={translate('pt.search.searchProperty.fields.applicationNumber')} />
                      </Col>
					</Row>
                    <Row>
                      <Col xs={12} md={6}>
                        <TextField errorText={fieldErrors.houseNoBldgApt
                          ? fieldErrors.houseNoBldgApt
                          : ""} id="houseNoBldgApt" maxLength={12} floatingLabelFixed={true} value={propertyTaxSearch.houseNoBldgApt?propertyTaxSearch.houseNoBldgApt:""} onChange={(e) => handleChange(e, "houseNoBldgApt", false, "")} hintText="654654" floatingLabelText={translate('pt.create.groups.propertyAddress.fields.doorNo')}/>
                      </Col>

                      <Col xs={12} md={6}>
                        <TextField errorText={fieldErrors.upicNumber
                          ? fieldErrors.upicNumber
                          : ""} maxLength={12} floatingLabelFixed={true} value={propertyTaxSearch.upicNumber?propertyTaxSearch.upicNumber:""} onChange={(e) => handleChange(e, "upicNumber", false, /^[a-zA-Z0-9]+$/g)} hintText="1000120015" floatingLabelText={translate('pt.create.groups.propertyDetails.assessmentNumber')}/>
                      </Col>
                    </Row>

                    <Row>
                      <Col xs={12} md={6}>
                        <TextField errorText={fieldErrors.mobileNumber
                          ? fieldErrors.mobileNumber
                          : ""} floatingLabelFixed={true} maxLength={10} value={propertyTaxSearch.mobileNumber?propertyTaxSearch.mobileNumber:""} onChange={(e) => handleChange(e, "mobileNumber", false, /^\d{10}$/g)} hintText="9584323454" floatingLabelText={translate('pt.create.groups.propertyAddress.mobileNumber')} />
                      </Col>

                      <Col xs={12} md={6}>
                        <TextField errorText={fieldErrors.aadhaarNumber
                          ? fieldErrors.aadhaarNumber
                          : ""} floatingLabelFixed={true} maxLength={12} value={propertyTaxSearch.aadhaarNumber?propertyTaxSearch.aadhaarNumber:""} onChange={(e) => handleChange(e, "aadhaarNumber", false, /^\d{12}$/g)} hintText={translate('pt.create.groups.ownerDetails.fields.aadhaarNumber')} floatingLabelText={translate('pt.create.groups.ownerDetails.fields.aadhaarNumber')} />
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
                          : ""} floatingLabelFixed={true} value={propertyTaxSearch.ownerName?propertyTaxSearch.ownerName:""} onChange={(e) => handleChange(e, "ownerName", false, "")} hintText={translate('pt.create.groups.ownerDetails.fields.ownerName')} floatingLabelText={translate('pt.create.groups.ownerDetails.fields.ownerName')} />
                      </Col>

                      <Col xs={12} md={6}>
                        <TextField errorText={fieldErrors.oldUpicNo
                          ? fieldErrors.oldUpicNo
                          : ""} floatingLabelFixed={true} maxLength={15} value={propertyTaxSearch.oldUpicNo?propertyTaxSearch.oldUpicNo:""} onChange={(e) => handleChange(e, "oldUpicNo", false, /^[a-zA-Z0-9]{3,}$/g)} hintText={translate('pt.create.groups.propertyDetails.oldAssessmentNumber')} floatingLabelText={translate('pt.create.groups.propertyDetails.oldAssessmentNumber')} />
                      </Col>
					  <Col xs={12} md={6}>
							<SelectField errorText={fieldErrors.usage
							  ? fieldErrors.usage
							  : ""} floatingLabelFixed={true} value={propertyTaxSearch.usage?propertyTaxSearch.usage:""} onChange={(event, index, value) => {
								var e = {
								  target: {
									value: value
								  }
								};
								handleChange(e, "usage", false, "")}} floatingLabelText={translate('pt.create.groups.assessmentDetails.fields.usageType')}
							    dropDownMenuProps={{animated: false, targetOrigin: {horizontal: 'left', vertical: 'bottom'}}}								>
								{renderOption(this.state.usage)}
							</SelectField>
                      </Col>
                    </Row>
                    <Row>
						<br/>
                      <Card>
                        <CardHeader title={<span style = {{ color: 'rgb(53, 79, 87)',fontSize: 18, margin: '8px 0px', fontWeight: 500,}}> {translate('pt.search.searchProperty.fields.boundary')} < /span>}/>
                        <CardText>
                          <Grid>
                            <Row>
                              <Col xs={12} md={6}>

                                <SelectField errorText={fieldErrors.revenueZone
                                  ? fieldErrors.revenueZone
                                  : ""} 
								  dropDownMenuProps={{animated: false, targetOrigin: {horizontal: 'left', vertical: 'bottom'}}}
								  floatingLabelFixed={true}
								  value={propertyTaxSearch.revenueZone?propertyTaxSearch.revenueZone:""} onChange={(event, index, value) => {
                                    var e = {
                                      target: {
                                        value: value
                                      }
                                    };
                                    handleChange(e, "revenueZone", false, "")}} floatingLabelText={translate('wc.create.groups.fields.zone')} >
									{renderOption(this.state.zone)}
                                </SelectField>
                              </Col>
                              <Col xs={12} md={6}>
                                <SelectField errorText={fieldErrors.revenueWard
                                  ? fieldErrors.revenueWard
                                  : ""} 
								  dropDownMenuProps={{animated: false, targetOrigin: {horizontal: 'left', vertical: 'bottom'}}}
								  floatingLabelFixed={true}
								  value={propertyTaxSearch.revenueWard?propertyTaxSearch.revenueWard:""} onChange={(event, index, value) =>{
                                    var e = {
                                      target: {
                                        value: value
                                      }
                                    };
                                    handleChange(e, "revenueWard", false, "")}
                                  } floatingLabelText={translate('wc.create.groups.fields.ward')} >
                                  {renderOption(this.state.ward)}
                                </SelectField>
                              </Col>
                            </Row>
                            <Row>
                              <Col xs={12} md={6}>
                                <SelectField errorText={fieldErrors.locality
                                  ? fieldErrors.locality
                                  : ""} 
								  dropDownMenuProps={{animated: false, targetOrigin: {horizontal: 'left', vertical: 'bottom'}}}
								  floatingLabelFixed={true}
								  value={propertyTaxSearch.locality?propertyTaxSearch.locality:""} onChange={(event, index, value) => {
                                    var e = {
                                      target: {
                                        value: value
                                      }
                                    };
                                    handleChange(e, "locality", false, "")}} floatingLabelText={translate('pt.search.searchProperty.fields.location')}>
									{renderOption(this.state.location)}
                                </SelectField>
                              </Col>
							  <Col xs={12} md={6}>
                                <SelectField errorText={fieldErrors.revenueCircle
                                  ? fieldErrors.revenueCircle
                                  : ""} 
								  dropDownMenuProps={{animated: false, targetOrigin: {horizontal: 'left', vertical: 'bottom'}}}
								  floatingLabelFixed={true}
								  value={propertyTaxSearch.revenueCircle?propertyTaxSearch.revenueCircle:""} onChange={(event, index, value) => {
                                    var e = {
                                      target: {
                                        value: value
                                      }
                                    };
                                    handleChange(e, "revenueCircle", false, "")}} floatingLabelText={translate('pt.create.groups.propertyAddress.fields.revenueCircle')} >
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
                        <CardHeader title={<span style = {{ color: 'rgb(53, 79, 87)',fontSize: 18, margin: '8px 0px', fontWeight: 500,}} > {translate('pt.search.searchProperty.fields.searchByDemand')} < /span>}/>
                        <CardText>
                          <Grid>
                            <Row>
                              <Col xs={12} md={6}>
                              <TextField errorText={fieldErrors.demandFrom
                                ? fieldErrors.demandFrom
                                : ""} floatingLabelFixed={true} value={propertyTaxSearch.demandFrom?propertyTaxSearch.demandFrom:""} onChange={(e) => handleChange(e, "demandFrom", false, /^\d$/g)} hintText={translate('pt.search.searchProperty.fields.demandFrom')} floatingLabelText={translate('pt.search.searchProperty.fields.demandFrom')} />
                              </Col>

                              <Col xs={12} md={6}>
                              <TextField errorText={fieldErrors.demandTo
                                ? fieldErrors.demandTo
                                : ""} floatingLabelFixed={true} value={propertyTaxSearch.demandTo?propertyTaxSearch.demandTo:""} onChange={(e) => handleChange(e, "demandTo", false, /^\d$/g)} hintText={translate('pt.search.searchProperty.fields.demandTo')} floatingLabelText={translate('pt.search.searchProperty.fields.demandTo')} />
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
			<RaisedButton type={translate('ui.framework.submit')} disabled={!isFormValid} primary={true} label={buttonText} />
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
