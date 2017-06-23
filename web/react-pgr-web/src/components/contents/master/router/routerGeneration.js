import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import TextField from 'material-ui/TextField';
import {brown500, red500,white,orange800} from 'material-ui/styles/colors';
import DatePicker from 'material-ui/DatePicker';
import SelectField from 'material-ui/SelectField';
import AutoComplete from 'material-ui/AutoComplete';
import MenuItem from 'material-ui/MenuItem';
import Dialog from 'material-ui/Dialog';
import RaisedButton from 'material-ui/RaisedButton';
import FlatButton from 'material-ui/FlatButton';
import Api from '../../../../api/api';
import DataTable from '../../../common/Table';

const $ = require('jquery');
$.DataTable = require('datatables.net');
const dt = require('datatables.net-bs');


const buttons = require('datatables.net-buttons-bs');

require('datatables.net-buttons/js/buttons.colVis.js'); // Column visibility
require('datatables.net-buttons/js/buttons.html5.js'); // HTML 5 file export
require('datatables.net-buttons/js/buttons.flash.js'); // Flash file export
require('datatables.net-buttons/js/buttons.print.js'); // Print view button

var _this = 0;
var flag = 0;

const getNameByBoundary = function(object, id) {
  if (id == "" || id == null) {
        return "";
    }
    for (var i = 0; i < object.length; i++) {
            if (object[i].id == id) {
                return object[i].boundaryType.name;
            }
        }
}

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

const styles = {
  headerStyle : {
    color: 'rgb(90, 62, 27)',
    fontSize : 19
  },
  marginStyle:{
    margin: '15px'
  },
  paddingStyle:{
    padding: '15px'
  },
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

class routerGeneration extends Component {
  constructor(props) {
    super(props);
    this.state = {
    	categoryList: [],
    	typeList: [],
    	boundaryTypeList: [],
    	boundariesList: [],
    	positionSource: [],
      boundaryInitialList: [],
    	positionSourceConfig: {
          text: 'name',
          value: 'id',
        },
        isSearchClicked: false,
        resultList: [],
        open: false,
        open2: false
    };

    this.search = this.search.bind(this);
    this.setInitialState = this.setInitialState.bind(this);
    this.loadGrievanceType = this.loadGrievanceType.bind(this);
    this.loadBoundaries = this.loadBoundaries.bind(this);
    this.save = this.save.bind(this);
    this.handleOpenNClose = this.handleOpenNClose.bind(this);
    this.handleOpenNClose2 = this.handleOpenNClose2.bind(this);
  }



  setInitialState(_state) {
  	this.setState(_state);
  }

  handleOpenNClose() {
    this.setState({
    	open: !this.state.open
    });
  };

  handleOpenNClose2() {
  	this.setState({
    	open2: !this.state.open2
    });
  }

  loadGrievanceType(value){
     var self = this;
     Api.commonApiPost("/pgr/services/_search", {type:'category', categoryId : value}).then(function(response)
     {
       self.setState({typeList : response.complaintTypes});
     },function(err) {

     });
  }

  loadBoundaries(value) {
  	 var self = this;
     Api.commonApiGet("/egov-location/boundarys", {"Boundary.id": value, "Boundary.tenantId": localStorage.getItem("tenantId")}).then(function(response) {
       self.setState({boundariesList : response.Boundary});
     },function(err) {

     });
  }

  componentWillUpdate() {
    if(flag == 1) {
      flag = 0;
      $('#searchTable').dataTable().fnDestroy();
    }
  }

  componentWillMount() {
  	$('#searchTable').DataTable({
         dom: 'lBfrtip',
         buttons: [
                   'excel', 'pdf', 'print'
          ],
          ordering: false,
          bDestroy: true,
    });
  }

  componentWillUnmount(){
     $('#searchTable')
     .DataTable()
     .destroy(true);
  }

  componentDidMount() {
  	var self = this, count = 4, _state = {};
  	self.props.initForm();
  	const checkCountAndCall = function(key, res) {
  		_state[key] = res;
  		count--;
  		if(count == 0) {
  			self.setInitialState(_state);
  		}
  	}

  	Api.commonApiPost("/pgr/servicecategories/_search").then(function(response) {
      	checkCountAndCall("categoryList", response.serviceTypeCategories);
    }, function(err) {
    	checkCountAndCall("categoryList", []);
    });

	  Api.commonApiPost("egov-location/boundarytypes/getByHierarchyType", {hierarchyTypeName: "ADMINISTRATION"}).then(function(response) {
      	checkCountAndCall("boundaryTypeList", response.BoundaryType);
    }, function(err) {
    	checkCountAndCall("boundaryTypeList", []);
    });

    Api.commonApiPost("/hr-masters/positions/_search").then(function(response) {
        checkCountAndCall("positionSource", response.Position);
    }, function(err) {
        checkCountAndCall("positionSource", []);
    });

    Api.commonApiGet("/egov-location/boundarys", {"Boundary.tenantId": localStorage.getItem("tenantId")}).then(function(response) {
       checkCountAndCall("boundaryInitialList", response.Boundary);
    },function(err) {
       checkCountAndCall("boundaryInitialList", []);
    });
  }

  search(e) {
  	e.preventDefault();
  	var self = this;
  	var searchSet = Object.assign({}, self.props.routerCreateSet);
  	Api.commonApiPost("/workflow/router/_search", searchSet).then(function(response) {
  		flag = 1;
  		self.setState({
  			resultList: response.RouterTypRes,
  			isSearchClicked: true
  		})
  	}, function(err) {

  	})
  }

  save(e) {
  	if(e) {
  	 e.preventDefault();
  	 this.setState({
  	 	open: true
  	 });
  	} else {
  	 var self = this;
  	 this.setState({
  	 	open: false
  	 }, function() {
  	 	var routerType = {
  	 		position: self.props.routerCreateSet.position,
  	 		id: "",
  	 		grievancetype: [],
  	 		boundary: [],
  	 		tenantId: localStorage.getItem("tenantId")
  	 	};

  	 	for(var i=0; i<self.props.routerCreateSet.complaintTypes.length; i++) {
  	 		routerType.grievancetype.push({
  	 			serviceCode: self.props.routerCreateSet.complaintTypes[i]
  	 		});
  	 	}

  	 	for(var i=0; i<self.props.routerCreateSet.boundaries.length; i++) {
  	 		routerType.boundary.push({
  	 			boundarytype: self.props.routerCreateSet.boundaries[i]
  	 		});
  	 	}

  	 	Api.commonApiPost("/pgr/router/_create", {}, {routertype: routerType}).then(function(response) {
	  		self.props.routerCreateSet = {};
	  		self.setState({
	  			resultList: [],
	  			isSearchClicked: false,
	  			flag: 1,
	  			positionSource: [],
	  			boundariesList: []
	  		});


		  }, function(err) {

		  })
	   });
    }
  }

  render() {
   _this = this;
   let {
   	fieldErrors,
   	routerCreateSet,
   	handleChange,
   	handleAutoCompleteKeyUp,
   	isFormValid
   } = this.props;
   let {
   	search,
   	loadGrievanceType,
   	loadBoundaries,
   	save,
   	handleOpenNClose,
   	handleOpenNClose2
   } = this;
   let {
   	boundariesList,
   	boundaryTypeList,
   	typeList,
   	categoryList,
   	resultList,
   	isSearchClicked,
   	open,
   	open2,
    positionSource,
    boundaryInitialList
   } = this.state;

   const showSaveButton = function() {
   		if(resultList && resultList.length) {
   			return (
   				<div style={{textAlign: 'center'}}>
	   				<RaisedButton style={{margin:'15px 5px'}} type="button" label="Save" backgroundColor={"#5a3e1b"} labelColor={white} onClick={(e) => {save(e)}}/>
	   				<RaisedButton style={{margin:'15px 5px'}} label="Close"/>
   				</div>
   			)
   		}
   }

   const renderBody = function() {
   	  if(resultList && resultList.length)
   		return resultList.map(function(val, i) {
   			return (
   				<tr key={i}>
   					<td>{i+1}</td>
            <td>{val.services.serviceName}</td>
            <td>{getNameByBoundary(boundaryInitialList, val.boundary.boundaryType)}</td>
            <td>{getNameById(boundaryInitialList, val.boundary.boundaryType)}</td>
            <td>{getNameById(positionSource, val.position)}</td>
   				</tr>
   			)
   		})
   }

   const viewTable = function() {
   	  if(isSearchClicked)
   		return (
	        <Card>
	          <CardHeader title={<strong style = {{color:"#5a3e1b"}} > Search Result </strong>}/>
	          <CardText>
		        <Table id="searchTable" style={{color:"black",fontWeight: "normal"}} bordered responsive>
		          <thead style={{backgroundColor:"#f2851f",color:"white"}}>
		            <tr>
		              <th>Sl No</th>
		              <th>Grievance Type</th>
		              <th>Boundary Type</th>
		              <th>Boundary</th>
		              <th>Position</th>
		            </tr>
		          </thead>
		          <tbody>
		          	{renderBody()}
		          </tbody>
		        </Table>
		       </CardText>
		    </Card>
		)
   }

   return (
    <div className="routerGeneration">
         <form autoComplete="off" onSubmit={(e) => {search(e)}}>
           <Card style={styles.marginStyle}>
            <CardHeader style={{paddingBottom:0}} title={<div style = {styles.headerStyle} > Create Grievance Router </div>}/>
              <CardText style={{padding:0}}>
                 <Grid>
                   <Row>
                   <Col xs={12} md={8}>
                     <SelectField maxHeight={200} fullWidth={true} floatingLabelText="Grievance Category" errorText={fieldErrors.complaintTypeCategory} value={routerCreateSet.complaintTypeCategory} onChange={(e, i, val) => {
	                					var e = {target: {value: val}};
	                					loadGrievanceType(val);
	                					handleChange(e, "complaintTypeCategory", true, "")}}>
	                					{categoryList.map((item, index) => (
			                                <MenuItem value={item.id} key={index} primaryText={item.name} />
			                            ))}
                     </SelectField>
                   </Col>
                   <Col xs={12} md={8}>
                    <SelectField maxHeight={200} fullWidth={true} floatingLabelText="Grievance Type" errorText={fieldErrors.complaintTypes} value={routerCreateSet.complaintTypes} onChange={(e, i, val) => {
	                					var e = {target: {value: val}};
	                					handleChange(e, "complaintTypes", true, "")}} multiple>
	                					{typeList.map((item, index) => (
			                                <MenuItem value={item.serviceName} key={index} primaryText={item.serviceName} />
			                            ))}
                    </SelectField>
                   </Col>
                   <Col xs={12} md={8}>
                     <SelectField maxHeight={200} fullWidth={true} floatingLabelText="Boundary Type" errorText={fieldErrors.boundaryType || ""} value={routerCreateSet.boundaryType} onChange={(e, i, val) => {
	                					var e = {target: {value: val}};
	                					loadBoundaries(val);
	                					handleChange(e, "boundaryType", true, "")}}>
	                					{boundaryTypeList.map((item, index) => (
			                                <MenuItem value={item.id} key={index} primaryText={item.name} />
			                            ))}
                     </SelectField>
                   </Col>
                   <Col xs={12} md={8}>
                    <SelectField maxHeight={200} fullWidth={true} floatingLabelText="Boundary" errorText={fieldErrors.boundaries || ""} value={routerCreateSet.boundaries} onChange={(e, i, val) => {
	                					var e = {target: {value: val}};
	                					handleChange(e, "boundaries", true, "")}} multiple>
	                					{boundariesList.map((item, index) => (
			                                <MenuItem value={item.id} key={index} primaryText={item.name} />
			                            ))}
                    </SelectField>
                   </Col>
                   </Row>
                   <Row>
                   <Col xs={12} md={8}>
                    	<AutoComplete
                        hintText=""
                        floatingLabelText="Position"
                        filter={function filter(searchText, key) {
                                  return key.toLowerCase().includes(searchText.toLowerCase());
                               }}
                        fullWidth={true}
                        dataSource={this.state.positionSource}
                        dataSourceConfig={this.state.positionSourceConfig}
                        menuStyle={{overflow:'auto', maxHeight: '150px'}}  listStyle={{overflow:'auto'}}
                        onKeyUp={handleAutoCompleteKeyUp}
                        errorText={fieldErrors.position || ""} value={routerCreateSet.position}
                        onNewRequest={(chosenRequest, index) => {
	                        var e = {
	                          target: {
	                            value: chosenRequest
	                          }
	                        };
	                        handleChange(e, "position", true, "");
	                       }}
	                      />
                   </Col>
                   </Row>
                 </Grid>
              </CardText>
           </Card>
           <div style={{textAlign: 'center'}}>
             <RaisedButton style={{margin:'15px 5px'}} type="submit" label="Search" disabled={!isFormValid} backgroundColor={"#5a3e1b"} labelColor={white}/>
             <RaisedButton style={{margin:'15px 5px'}} label="Close"/>
           </div>
           {viewTable()}
           {showSaveButton()}
         </form>
         <Dialog
          title="Confirm"
          actions={
          	[<FlatButton
				        label="Close"
				        primary={false}
				        onTouchTap={handleOpenNClose}
				      />, <FlatButton
				        label="Yes"
				        primary={true}
				        onTouchTap={search}
				      />]}
          modal={false}
          open={open}
          onRequestClose={handleOpenNClose}
        >
          Existing Router Data will be overridden, Are you sure?
        </Dialog>
        <Dialog
          title="Success"
          actions={[<FlatButton
				        label="Close"
				        primary={true}
				        onTouchTap={handleOpenNClose2}
				      />]}
          modal={false}
          open={open2}
          onRequestClose={handleOpenNClose2}
        >
          Grievance routers created successfully.
        </Dialog>
        </div>
   );
  }
}




const mapStateToProps = state => {
	return ({routerCreateSet: state.form.form, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid});
};
const mapDispatchToProps = dispatch => ({
	initForm: (type) => {
        dispatch({
	      type: "RESET_STATE",
	      validationData: {
	        required: {
	          current: [],
	          required: ["complaintTypeCategory", "complaintTypes", "boundaryType", "boundaries", "position"]
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
	handleAutoCompleteKeyUp : (e) => {
	    var self = _this;
	    dispatch({type: "HANDLE_CHANGE", property: 'position', value: '', isRequired : true, pattern: ''});
	}
});



export default connect(mapStateToProps, mapDispatchToProps)(routerGeneration);
