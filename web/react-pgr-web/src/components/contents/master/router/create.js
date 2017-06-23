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
       	boundarySource: [],
       	boundaryTypeList: [],
       	open: false,
       	readonly: false
       }
       this.loadBoundaries = this.loadBoundaries.bind(this);
       this.create = this.create.bind(this);
       this.handleOpenNClose = this.handleOpenNClose.bind(this);
  }

  componentDidMount() {
  	var self = this;
  	this.props.initForm();
  	Api.commonApiPost("egov-location/boundarytypes/getByHierarchyType", {hierarchyTypeName: "ADMINISTRATION"}).then(function(response) {
      	self.setState({
      		boundaryTypeList: response.BoundaryType
      	})
    }, function(err) {
    	self.setState({
      		boundaryTypeList: []
      	})
    });

    Api.commonApiPost("/hr-masters/positions/_search").then(function(response) {
        self.setState({
          positionSource: response.Position
        })
    }, function(err) {
      self.setState({
          positionSource: []
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
   		id: "",
   		grievancetype: [{
   				serviceCode: self.props.routerCreateSet.complaintType
   	    }],
   		boundary: [{
   			boundarytype: self.props.routerCreateSet.boundary
   		}],
   		tenantId: localStorage.getItem("tenantId")
  	};

  	Api.commonApiPost("/pgr/router/_create", {}, {routertype: routerType}).then(function(response) {
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
      handleAutoCompleteKeyUp
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
       	open,
       	readonly
  	} = this.state;

  	const showBtn = function() {
  		if(!readonly) {
  			return (<RaisedButton style={{margin:'15px 5px'}} type="submit" label="Create" disabled={!isFormValid} backgroundColor={"#5a3e1b"} labelColor={white}/>);
  		}
  	}

  	return (
  		<div className="routerGeneration">
         <form autoComplete="off" onSubmit={(e) => {create(e)}}>
           <Card style={styles.marginStyle}>
            <CardHeader style={{paddingBottom:0}} title={<div style = {styles.headerStyle} > Create Grievance Router </div>}/>
              <CardText style={{padding:0}}>
                 <Grid>
                   <Row>
                   <Col xs={12} md={8}>
                   	<AutoComplete
                        hintText=""
                        floatingLabelText="Grievance Type"
                        fullWidth={true}
                        filter={function filter(searchText, key) {
                                  return key.toLowerCase().includes(searchText.toLowerCase());
                               }}
                        dataSource={this.state.complaintSource}
                        dataSourceConfig={this.state.allSourceConfig}
                        menuStyle={{overflow:'auto', maxHeight: '150px'}}  listStyle={{overflow:'auto'}}
                        disabled={readonly}
                        errorText={fieldErrors.complaintType || ""}
                        value={routerCreateSet.complaintType}
                        onKeyUp={(e) => {handleAutoCompleteKeyUp(e, "complaintType")}}
                        onNewRequest={(chosenRequest, index) => {
	                        var e = {
	                          target: {
	                            value: chosenRequest
	                          }
	                        };
	                        handleChange(e, "complaintType", true, "");
	                       }}
	                      />
                   </Col>
                   <Col xs={12} md={8}>
                   	<SelectField disabled={readonly} maxHeight={200} fullWidth={true} floatingLabelText="Boundary Type" errorText={fieldErrors.boundaryType || ""} value={routerCreateSet.boundaryType} onChange={(e, i, val) => {
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
                        filter={function filter(searchText, key) {
                                  return key.toLowerCase().includes(searchText.toLowerCase());
                               }}
                        dataSource={this.state.boundarySource}
                        dataSourceConfig={this.state.allSourceConfig}
                        menuStyle={{overflow:'auto', maxHeight: '150px'}}  listStyle={{overflow:'auto'}}
                        errorText={fieldErrors.boundary || ""} value={routerCreateSet.boundary}
                        onKeyUp={(e) => {handleAutoCompleteKeyUp(e, "boundary")}}
                        onNewRequest={(chosenRequest, index) => {
	                        var e = {
	                          target: {
	                            value: chosenRequest
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
                        filter={function filter(searchText, key) {
                                  return key.toLowerCase().includes(searchText.toLowerCase());
                               }}
                        dataSource={this.state.positionSource}
                        dataSourceConfig={this.state.allSourceConfig}
                        menuStyle={{overflow:'auto', maxHeight: '150px'}}  listStyle={{overflow:'auto'}}
                        errorText={fieldErrors.position || ""} value={routerCreateSet.position}
                        onKeyUp={(e) => {handleAutoCompleteKeyUp(e, "position")}}
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
          Grievance router created successfully.
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
	},
  handleAutoCompleteKeyUp : (e, type) => {
    var currentThis = _this;
    dispatch({type: "HANDLE_CHANGE", property: type, value: e.target.value, isRequired : true, pattern: ''});
  }
});

export default connect(mapStateToProps, mapDispatchToProps)(createRouter);
