import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Grid, Row, Col, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import TextField from 'material-ui/TextField';
import DatePicker from 'material-ui/DatePicker';
import SelectField from 'material-ui/SelectField';
import AutoComplete from 'material-ui/AutoComplete';
import MenuItem from 'material-ui/MenuItem';
import Dialog from 'material-ui/Dialog';
import RaisedButton from 'material-ui/RaisedButton';
import FlatButton from 'material-ui/FlatButton';
import Api from '../../../../../api/api';
import {translate} from '../../../../common/common';

var _this;
let searchTextCom = "",
    searchTextBoun = "",
    searchTextPos = "";

const styles = {
  headerStyle : {
    fontSize : 19
  },
  addBorderBottom:{
    borderBottom: '1px solid #eee',
    padding: '10px'
  },
  marginStyle:{
    margin: '15px'
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
       	readonly: false,
        updateonly: true
       }
       this.loadBoundaries = this.loadBoundaries.bind(this);
       this.create = this.create.bind(this);
       this.handleOpenNClose = this.handleOpenNClose.bind(this);
       this.close = this.close.bind(this);
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
    self.props.setLoadingStatus('loading');
    searchTextCom = "";
    searchTextBoun = "";
    searchTextPos = "";
    const checkIfUpdateOrView = function() {
      if(type === "edit" || type === "view") {
        var id=self.props.match.params.id;
        Api.commonApiPost("/workflow/router/v1/_search", {id}).then(function(response) {
          self.props.setLoadingStatus('hide');
          var routerType = {
            id: response.RouterTypRes[0].id,
            position: response.RouterTypRes[0].position,
         		complaintType: response.RouterTypRes[0].service.id,
         		boundary: response.RouterTypRes[0].boundary.boundaryType,
            boundaryType: getIdByBoundary(self.state.boundaryInitialList, response.RouterTypRes[0].boundary.boundaryType)
        	}

          self.loadBoundaries(getIdByBoundary(self.state.boundaryInitialList, response.RouterTypRes[0].boundary.boundaryType));
          searchTextCom = response.RouterTypRes[0].service.serviceName || "";
          searchTextBoun = getNameById(self.state.boundaryInitialList, response.RouterTypRes[0].boundary.boundaryType) || "";
          searchTextPos = getNameById(self.state.positionSource, response.RouterTypRes[0].position) || "";
          setForm(routerType);
          if(type == "view"){
            console.log('view page');
            self.setState({
              readonly: true
            });
          }else {
            console.log('edit page');
            self.setState({
              updateonly: true
            });
          }
        }, function(err) {
          self.props.setLoadingStatus('hide');
        })
      } else {
        self.props.setLoadingStatus('hide');
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

    Api.commonApiPost("/pgr-master/service/v1/_search", {type: "all", keywords : 'complaint'}).then(function(response) {
        self.setState({
          complaintSource: response.Service
        });
    }, function(err) {
      self.setState({
          complaintSource: []
        })
    });
  }

  loadBoundaries(value) {
  	 var self = this;
     Api.commonApiPost("/egov-location/boundarys/getByBoundaryType", {"boundaryTypeId": value, "Boundary.tenantId": localStorage.getItem("tenantId")}).then(function(response) {
       self.setState({boundarySource : response.Boundary});
     },function(err) {

     });
  }

  handleOpenNClose() {
    this.setState({
    	open: !this.state.open
    });
  };

  close() {
    window.open(window.location, "_self").close();
  }

  create(e) {
  	e.preventDefault();
    this.props.setLoadingStatus('loading');
  	var self = this;
  	var routerType = {
  		position: self.props.routerCreateSet.position,
      active: true,
   		id: self.props.routerCreateSet.id || "",
   		services: [{
   				id: self.props.routerCreateSet.complaintType
   	    }],
   		boundaries: [{
   			boundarytype: self.props.routerCreateSet.boundary
   		}],
   		tenantId: localStorage.getItem("tenantId")
  	};

  	Api.commonApiPost("/workflow/router/v1/" + (self.props.routerCreateSet.id ? "_update" : "_create"), {}, {routertype: routerType}).then(function(response) {
  		if(!self.props.routerCreateSet.id) {
        self.props.initForm();
        searchTextCom = "";
        searchTextBoun = "";
        searchTextPos = "";
      }

      //self.props.initForm();
  		self.setState({
  			open: true
  		});
      self.props.setLoadingStatus('hide');
  	}, function(err) {
      self.props.setLoadingStatus('hide');
      self.props.toggleSnackbarAndSetText(true, err.message);
  	});
  }

  getComplaintTypeName(id){
    if(id){
      var data = this.state.complaintSource.find( function( ele ) {
          return ele.id == id;
      } );
      console.log(id, data);
      if(data){
        return (
          <div>
          {data.serviceName}
          </div>
        );
      }else{
      return(
        null
      )
    }
    }
  }

  getBoundaryTypeName(id){
    if(id){
      var data = this.state.boundaryTypeList.find( function( ele ) {
          return ele.id == id;
      } );
      console.log(id, data);
      if(data){
        return (
          <div>
          {data.name}
          </div>
        );
      }else{
        return (
          null
        );
      }
    }
  }

  getBoundaryName(id){
    if(id){
      var data = this.state.boundarySource.find( function( ele ) {
          return ele.id == id;
      } );
      console.log(id, data);
      if(data){
        return (
          <div>
          {data.name}
          </div>
        );
      }else{
        return (
          null
        );
      }
    }
  }

  getPositionName(id){
    if(id){
      var data = this.state.positionSource.find( function( ele ) {
          return ele.id == id;
      } );
      console.log(id, data);
      if(data){
        return (
          <div>
          {data.name}
          </div>
        );
      }else{
        return (
          null
        );
      }
    }
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
  		handleOpenNClose,
      close
  	} = this;
  	let {
  		allSourceConfig,
       	positionSource,
       	complaintSource,
       	boundarySource,
       	boundaryTypeList,
        boundaryInitialList,
       	open,
       	readonly,
        updateonly
  	} = this.state;

  	const showBtn = function() {
  		if(!readonly) {

  			return (<RaisedButton style={{margin:'15px 5px'}} type="submit" label={match.params && match.params.type == "edit" ? translate("pgr.lbl.update") : translate("pgr.lbl.create")} disabled={!isFormValid} primary={true}/>);

  		}
  	}

    if(this.state.readonly){
      return(
        <div>
          <Grid style={{width:'100%'}}>
            <Card style={{margin:'15px 0'}}>
              <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} >
                 {(match.params && match.params.type == "view" ? translate("pgr.lbl.view.router") : match.params && match.params.type == "edit" ? translate("pgr.lbl.edit.router") : translate("pgr.lbl.create.router"))}
               < /div>}/>
               <CardText style={{padding:'8px 16px 0'}}>
                 <Row style={styles.addBorderBottom}>
                   <Col xs={6} md={3}>
                    {translate("pgr.lbl.grievance.type")}
                   </Col>
                   <Col xs={6} md={3}>
                    {this.getComplaintTypeName(routerCreateSet.complaintType)}
                   </Col>
                   <Col xs={6} md={3}>
                     {translate("pgr.lbl.boundarytype")}
                   </Col>
                   <Col xs={6} md={3}>
                    {this.getBoundaryTypeName(routerCreateSet.boundaryType)}
                   </Col>
                 </Row>
                 <Row style={styles.addBorderBottom}>
                   <Col xs={6} md={3}>
                    {translate("pgr.lbl.boundary")}
                   </Col>
                   <Col xs={6} md={3}>
                    {this.getBoundaryName(routerCreateSet.boundary)}
                   </Col>
                   <Col xs={6} md={3}>
                    {translate("pgr.lbl.position")}
                   </Col>
                   <Col xs={6} md={3}>
                    {this.getPositionName(routerCreateSet.position)}
                   </Col>
                 </Row>
               </CardText>
            </Card>
          </Grid>
        </div>
      )
    }else if(this.state.updateonly){
      return (
        <div className="routerGeneration">
           <form autoComplete="off" onSubmit={(e) => {create(e)}}>
             <Card style={styles.marginStyle}>
              <CardHeader style={{paddingBottom:0}} title={<div style = {styles.headerStyle} > {(match.params && match.params.type == "view" ? translate("pgr.lbl.view.router") : match.params && match.params.type == "edit" ? translate("pgr.lbl.edit.router") : translate("pgr.lbl.create.router"))} </div>}/>
                <CardText style={{padding:0}}>
                   <Grid>
                     <Row>
                     <Col xs={12} sm={6} md={6} lg={6}>
                      <AutoComplete
                          fullWidth={true}
                          floatingLabelText={translate("pgr.lbl.grievance.type") + " *"}
                          filter={AutoComplete.caseInsensitiveFilter}
                          dataSource={this.state.complaintSource}
                          dataSourceConfig={this.state.complaintSourceConfig}
                          menuStyle={{overflow:'auto', maxHeight: '150px'}}  listStyle={{overflow:'auto'}}
                          errorText={fieldErrors.complaintType || ""}
                          searchText={searchTextCom}
                          value={routerCreateSet.complaintType || ""}
                          onKeyUp={(e) => {handleAutoCompleteKeyUp(e, "complaintType")}}
                          ref="complaintType"
                          onNewRequest={(chosenRequest, index) => {
                            if(index === -1){
                              this.refs['complaintType'].setState({searchText:''});
                            }else{
                              searchTextCom = chosenRequest.serviceName;
                              var e = {
                                target: {
                                  value: chosenRequest.id
                                }
                              };
                              handleChange(e, "complaintType", true, "");
                            }
                           }}
                          />
                     </Col>
                     <Col xs={12} sm={6} md={6} lg={6}>
                      <SelectField
                        fullWidth={true}
                        floatingLabelText={translate("pgr.lbl.boundarytype") + " *"}
                        errorText={fieldErrors.boundaryType || ""}
                        value={(routerCreateSet.boundaryType + "") || ""}
                        onChange={(e, i, val) => {
                              var e = {target: {value: val}};
                              loadBoundaries(val);
                              searchTextBoun = "";
                              handleChange(e, "boundaryType", true, "")}}>
                              {boundaryTypeList.map((item, index) => (
                                        <MenuItem value={item.id} key={index} primaryText={item.name} />
                                    ))}
                       </SelectField>
                     </Col>
                     <Col xs={12} sm={6} md={6} lg={6}>
                      <AutoComplete
                          hintText=""
                          floatingLabelText={translate("pgr.lbl.boundary") + " *"}
                          fullWidth={true}
                          filter={AutoComplete.caseInsensitiveFilter}
                          dataSource={this.state.boundarySource}
                          dataSourceConfig={this.state.allSourceConfig}
                          menuStyle={{overflow:'auto', maxHeight: '150px'}}  listStyle={{overflow:'auto'}}
                          errorText={fieldErrors.boundary || ""}
                          value={routerCreateSet.boundary || ""}
                          onKeyUp={(e) => {handleAutoCompleteKeyUp(e, "boundary")}}
                          searchText={searchTextBoun}
                          ref="boundary"
                          onNewRequest={(chosenRequest, index) => {
                            if(index === -1){
                              this.refs['boundary'].setState({searchText:''});
                            }else{
                              searchTextBoun = chosenRequest.name;
                              var e = {
                                target: {
                                  value: chosenRequest.id
                                }
                              };
                              handleChange(e, "boundary", true, "");
                            }
                           }}
                          />
                     </Col>
                     <Col xs={12} sm={6} md={6} lg={6}>
                      <AutoComplete
                          fullWidth={true}
                          floatingLabelText={translate("pgr.lbl.position") + " *"}
                          filter={AutoComplete.caseInsensitiveFilter}
                          dataSource={this.state.positionSource}
                          dataSourceConfig={this.state.allSourceConfig}
                          menuStyle={{overflow:'auto', maxHeight: '150px'}}  listStyle={{overflow:'auto'}}
                          errorText={fieldErrors.position || ""}
                          value={routerCreateSet.position || ""}
                          onKeyUp={(e) => {handleAutoCompleteKeyUp(e, "position")}}
                          searchText={searchTextPos}
                          ref="position"
                          onNewRequest={(chosenRequest, index) => {
                            if(index === -1){
                              this.refs['position'].setState({searchText:''});
                            }else{
                              searchTextPos = chosenRequest.name;
                              var e = {
                                target: {
                                  value: chosenRequest.id
                                }
                              };
                              handleChange(e, "position", true, "");
                            }}
                            }
                          />
                     </Col>
                    </Row>
                   </Grid>
                </CardText>
             </Card>
             <div style={{textAlign: 'center'}}>
               {showBtn()}
             </div>
           </form>
           <Dialog
            title="Success"
            actions={[<FlatButton
                  label={translate("core.lbl.close")}
                  primary={true}
                  onTouchTap={handleOpenNClose}
                />]}
            modal={false}
            open={open}
            onRequestClose={handleOpenNClose}
          >
            {match.params && match.params.type == "edit" ?  translate("pgr.lbl.router.update.success") : translate("pgr.lbl.router.create.success")}
          </Dialog>
          </div>
      );
    }else{
      return null;
    }
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
    dispatch({type: "HANDLE_CHANGE", property: type, value: '', isRequired : true, pattern: ''});
  },
  setForm: (data) => {
    dispatch({
      type: "SET_FORM",
      data,
      isFormValid: true,
      fieldErrors: {},
      validationData: {
        required: {
          current: ["complaintType", "boundaryType", "boundary", "position"],
          required: ["complaintType", "boundaryType", "boundary", "position"]
        },
        pattern: {
          current: [],
          required: []
        }
      }
    });
  },
  setLoadingStatus: (loadingStatus) => {
    dispatch({type: "SET_LOADING_STATUS", loadingStatus});
  },
  toggleSnackbarAndSetText: (snackbarState, toastMsg) => {
    dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState, toastMsg});
  }
});

export default connect(mapStateToProps, mapDispatchToProps)(createRouter);
