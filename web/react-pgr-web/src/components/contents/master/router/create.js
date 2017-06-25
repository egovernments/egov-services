import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Grid, Row, Col, DropdownButton} from 'react-bootstrap';
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

var _this;

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
const getIdByBoundary = function(object, id) {
  if (id == "" || id == null) {
        return "";
    }
    for (var i = 0; i < object.length; i++) {
            if (object[i].id == id) {
                return object[i].boundaryType.id;
            }
        }
}

class createRouter extends Component {
  constructor(props) {
       super(props);
       this.state = {
       	allSourceConfig: {
       		text: 'name',
          value: 'id'
       	},
       	positionSource: [],
       	complaintSource: [],
        complaintSourceConfig: {
          text: 'serviceName',
          value: 'serviceCode'
        },
       	boundarySource: [],
       	boundaryTypeList: [],
        boundaryInitialList: [],
       	open: false,
       	readonly: false
       }
       this.loadBoundaries = this.loadBoundaries.bind(this);
       this.create = this.create.bind(this);
       this.handleOpenNClose = this.handleOpenNClose.bind(this);
  }

  componentDidMount() {
  	var self = this;
    var type = this.props.match.params.type;
    var routerType = {}, count = 3;
    let {setForm}= this.props;
    let {
        allSourceConfig,
        complaintSource,
        boundarySource,
        boundaryTypeList,
        open,
        resultList,
        isSearchClicked,
        boundaryInitialList,
        positionSource
    } = this.state;

  	this.props.initForm();
    const checkIfUpdateOrView = function() {
      if(type === "edit" || type === "view") {
        var id=self.props.match.params.id;
        Api.commonApiPost("/workflow/router/_search", {id}).then(function(response) {
          var routerType = {
            id: response.RouterTypRes[0].id,
            position: getNameById(self.state.positionSource, response.RouterTypRes[0].position),
         		complaintType: response.RouterTypRes[0].service.serviceName,
         		boundary: getNameById(self.state.boundaryInitialList, response.RouterTypRes[0].boundary[0].boundaryType),
            boundaryType: getIdByBoundary(self.state.boundaryInitialList, response.RouterTypRes[0].boundary[0].boundaryType)
        	}

          setForm(routerType);
          if(type == "view")
            self.setState({
              readonly: true
            });
        }, function(err) {

        })
    }
  }

  	Api.commonApiPost("egov-location/boundarytypes/getByHierarchyType", {hierarchyTypeName: "ADMINISTRATION"}).then(function(response) {
      	self.setState({
      		boundaryTypeList: response.BoundaryType
      	}, function(){
          count--;
          if(count == 0)
            checkIfUpdateOrView();
        });

    }, function(err) {
    	self.setState({
      		boundaryTypeList: []
      	}, function(){
          count--;
          if(count == 0)
            checkIfUpdateOrView();
        });
    });

    Api.commonApiPost("/hr-masters/positions/_search").then(function(response) {

        self.setState({
          positionSource: response.Position
        }, function(){
          count--;
          if(count == 0)
            checkIfUpdateOrView();
        })
    }, function(err) {
      self.setState({
          positionSource: []
        }, function(){
          count--;
          if(count == 0)
            checkIfUpdateOrView();
        })
    });


    Api.commonApiGet("/egov-location/boundarys", {"Boundary.tenantId": localStorage.getItem("tenantId")}).then(function(response) {
        self.setState({
          boundaryInitialList: response.Boundary
        }, function(){
          count--;
          if(count == 0)
            checkIfUpdateOrView();
        })
    }, function(err) {
        self.setState({
          boundaryInitialList: []
        }, function(){
          count--;
          if(count == 0)
            checkIfUpdateOrView();
        })
    });

    Api.commonApiPost("/pgr/services/_search", {type: "all"}).then(function(response) {
        self.setState({
          complaintSource: response.complaintTypes
        })
    }, function(err) {
      self.setState({
          complaintSource: []
        })
    });
  }

  loadBoundaries(value) {
  	 var self = this;
     Api.commonApiGet("/egov-location/boundarys", {"Boundary.id": value, "Boundary.tenantId": localStorage.getItem("tenantId")}).then(function(response) {
       self.setState({boundarySource : response.Boundary});
     },function(err) {

     });
  }

  handleOpenNClose() {
    this.setState({
    	open: !this.state.open
    });
  };

  create(e) {
  	e.preventDefault();
  	var self = this;
  	var routerType = {
  		position: self.props.routerCreateSet.position,
   		id: self.props.routerCreateSet.id || "",
   		services: [{
   				serviceCode: self.props.routerCreateSet.complaintType
   	    }],
   		boundary: [{
   			boundarytype: self.props.routerCreateSet.boundary
   		}],
   		tenantId: localStorage.getItem("tenantId")
  	};

  	Api.commonApiPost("/workflow/router/" + (self.props.routerCreateSet.id ? "_update" : "_create"), {}, {routertype: routerType}).then(function(response) {
  		self.props.initForm();
  		self.setState({
  			open: true
  		});
  	}, function(err) {

  	})
  }

  render() {
  	_this = this;
  	let {
  		isFormValid,
  		routerCreateSet,
  		fieldErrors,
  		handleChange,
      handleAutoCompleteKeyUp,
      match
  	} = this.props;
  	let {
  		loadBoundaries,
  		create,
  		handleOpenNClose
  	} = this;
  	let {
  		allSourceConfig,
       	positionSource,
       	complaintSource,
       	boundarySource,
       	boundaryTypeList,
        boundaryInitialList,
       	open,
       	readonly
  	} = this.state;

  	const showBtn = function() {
  		if(!readonly) {
  			return (<RaisedButton style={{margin:'15px 5px'}} type="submit" label={match.params && match.params.type == "edit" ? "Update" : "Create"} disabled={!isFormValid} backgroundColor={"#5a3e1b"} labelColor={white}/>);
  		}
  	}

  	return (
  		<div className="routerGeneration">
         <form autoComplete="off" onSubmit={(e) => {create(e)}}>
           <Card style={styles.marginStyle}>
            <CardHeader style={{paddingBottom:0}} title={<div style = {styles.headerStyle} > {(match.params && match.params.type == "view" ? "View " : match.params && match.params.type == "edit" ? "Edit " : "Create ") + "Grievance Router"} </div>}/>
              <CardText style={{padding:0}}>
                 <Grid>
                   <Row>
                   <Col xs={12} md={8}>
                   	<AutoComplete
                        hintText=""
                        floatingLabelText="Grievance Type"
                        fullWidth={true}
                        filter={AutoComplete.caseInsensitiveFilter}
                        dataSource={this.state.complaintSource}
                        dataSourceConfig={this.state.complaintSourceConfig}
                        menuStyle={{overflow:'auto', maxHeight: '150px'}}  listStyle={{overflow:'auto'}}
                        disabled={readonly}
                        errorText={fieldErrors.complaintType || ""}
                        value={routerCreateSet.complaintType || ""}
                        onKeyUp={(e) => {handleAutoCompleteKeyUp(e, "complaintType")}}
                        onNewRequest={(chosenRequest, index) => {
	                        var e = {
	                          target: {
	                            value: chosenRequest.id
	                          }
	                        };
	                        handleChange(e, "complaintType", true, "");
	                       }}
	                      />
                   </Col>
                   <Col xs={12} md={8}>
                   	<SelectField 
                      disabled={readonly}
                      fullWidth={true} 
                      floatingLabelText="Boundary Type" 
                      errorText={fieldErrors.boundaryType || ""} 
                      value={(routerCreateSet.boundaryType + "") || ""} 
                      onChange={(e, i, val) => {
	                					var e = {target: {value: val}};
	                					loadBoundaries(val);
	                					handleChange(e, "boundaryType", true, "")}}>
	                					{boundaryTypeList.map((item, index) => (
			                                <MenuItem value={item.id} key={index} primaryText={item.name} />
			                            ))}
                     </SelectField>
                   </Col>
                   <Col xs={12} md={8}>
                   	<AutoComplete
                   		disabled={readonly}
                        hintText=""
                        floatingLabelText="Boundary"
                        fullWidth={true}
                        filter={AutoComplete.caseInsensitiveFilter}
                        dataSource={this.state.boundarySource}
                        dataSourceConfig={this.state.allSourceConfig}
                        menuStyle={{overflow:'auto', maxHeight: '150px'}}  listStyle={{overflow:'auto'}}
                        errorText={fieldErrors.boundary || ""} 
                        value={routerCreateSet.boundary || ""}
                        onKeyUp={(e) => {handleAutoCompleteKeyUp(e, "boundary")}}
                        onNewRequest={(chosenRequest, index) => {
	                        var e = {
	                          target: {
	                            value: chosenRequest.id
	                          }
	                        };
	                        handleChange(e, "boundary", true, "");
	                       }}
	                      />
                   </Col>
                   <Col xs={12} md={8}>
                   	<AutoComplete
                   		disabled={readonly}
                        hintText=""
                        floatingLabelText="Position"
                        fullWidth={true}
                        filter={AutoComplete.caseInsensitiveFilter}
                        dataSource={this.state.positionSource}
                        dataSourceConfig={this.state.allSourceConfig}
                        menuStyle={{overflow:'auto', maxHeight: '150px'}}  listStyle={{overflow:'auto'}}
                        errorText={fieldErrors.position || ""} 
                        value={routerCreateSet.position || ""}
                        onKeyUp={(e) => {handleAutoCompleteKeyUp(e, "position")}}
                        onNewRequest={(chosenRequest, index) => {

	                        var e = {
	                          target: {
	                            value: chosenRequest.id
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
             {showBtn()}
             <RaisedButton style={{margin:'15px 5px'}} label="Close"/>
           </div>
         </form>
         <Dialog
          title="Success"
          actions={[<FlatButton
				        label="Close"
				        primary={true}
				        onTouchTap={handleOpenNClose}
				      />]}
          modal={false}
          open={open}
          onRequestClose={handleOpenNClose}
        >
          Grievance router {match.params && match.params.type == "edit" ? "updated" : "created"} successfully.
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
	          required: ["complaintType", "boundaryType", "boundary", "position"]
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

      if(property == "boundaryType")
        dispatch({type: "HANDLE_CHANGE", property: "boundary", value: "", isRequired: true, pattern: ""});        
	},
  handleAutoCompleteKeyUp : (e, type) => {
    var currentThis = _this;
    dispatch({type: "HANDLE_CHANGE", property: type, value: e.target.value, isRequired : true, pattern: ''});
  },
  setForm: (data) => {
    dispatch({
      type: "SET_FORM",
      data,
      isFormValid:true,
      fieldErrors: {},
      validationData: {
        required: {
          current: [],
          required: ["complaintType", "boundaryType", "boundary", "position"]
        },
        pattern: {
          current: [],
          required: []
        }
      }
    });
  }
});

export default connect(mapStateToProps, mapDispatchToProps)(createRouter);
