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

var _this;
var flag = 0;

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

var flag = 0;
class searchRouter extends Component {
  constructor(props) {
      super(props);
      this.state = {
          allSourceConfig: {
            text: 'name',
            value: 'id'
          },
          complaintSource: [],
          boundarySource: [],
          boundaryTypeList: [],
          isSearchClicked: false,
          resultList: [],
          boundariesList: [],
          boundaryInitialList: []
       }
      this.loadBoundaries = this.loadBoundaries.bind(this);
      this.search = this.search.bind(this);
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

    Api.commonApiGet("/egov-location/boundarys", {"Boundary.tenantId": localStorage.getItem("tenantId")}).then(function(response) {
        self.setState({
          boundaryInitialList: response.Boundary
        })
    }, function(err) {
        self.setState({
          boundaryInitialList: []
        })
    });

    Api.commonApiPost("/pgr/services/_search", {type:'all'}).then(function(response) {
       self.setState({
        complaintSource : response.complaintTypes
       });
    },function(err) {
       self.setState({
        complaintSource : []
       });
    });
  }

  loadBoundaries(value) {
     var self = this;
     Api.commonApiGet("/egov-location/boundarys", {"Boundary.id": value, "Boundary.tenantId": localStorage.getItem("tenantId")}).then(function(response) {
       self.setState({boundariesList : response.Boundary});
     },function(err) {

     });
  }

  search(e) {
    e.preventDefault();
    var self = this;
    var searchSet = Object.assign({}, self.props.routerSearchSet);
    Api.commonApiPost("/workflow/router/_search", searchSet).then(function(response) {
      flag = 1;
      self.setState({
        resultList: response.RouterTypes,
        isSearchClicked: true
      })
    }, function(err) {

    })
  }

  render() {
    _this = this;
    let {
      routerSearchSet,
      handleAutoCompleteKeyUp,
      handleChange
    } = this.props;
    let {
      loadBoundaries,
      search
    } = this;
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

    const renderBody = function() {
      if(resultList && resultList.length)
      return resultList.map(function(val, i) {
        return (
          <tr key={i}>
            <td>{i+1}</td>
            <td>{val.grievancetype.serviceName}</td>
            <td>{getNameById(boundaryTypeList, val.boundaryType)}</td>
            <td>{getNameById(boundaryInitialList, val.boundary)}</td>
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
                  <th>#</th>
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
      <div className="searchRouter">
         <form autoComplete="off" onSubmit={(e) => {search(e)}}>
           <Card style={styles.marginStyle}>
            <CardHeader style={{paddingBottom:0}} title={<div style = {styles.headerStyle} > Search Grievance Router </div>}/>
              <CardText style={{padding:0}}>
                 <Grid>
                   <Row>
                   <Col xs={12} md={8}>
                    <AutoComplete
                        hintText=""
                        floatingLabelText="Grievance Type"
                        filter={AutoComplete.noFilter}
                        fullWidth={true}
                        dataSource={this.state.complaintSource}
                        dataSourceConfig={this.state.allSourceConfig}
                        menuStyle={{overflow:'auto', maxHeight: '150px'}}  listStyle={{overflow:'auto'}}
                        onKeyUp={(e) => {handleAutoCompleteKeyUp(e, "complaintType")}}
                        value={routerSearchSet.complaintType}
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
                    <SelectField maxHeight={200} fullWidth={true} floatingLabelText="Boundary Type" value={routerSearchSet.boundaryType} onChange={(e, i, val) => {
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
                        hintText=""
                        floatingLabelText="Boundary"
                        filter={AutoComplete.noFilter}
                        fullWidth={true}
                        dataSource={this.state.boundarySource}
                        dataSourceConfig={this.state.allSourceConfig}
                        menuStyle={{overflow:'auto', maxHeight: '150px'}}  listStyle={{overflow:'auto'}}
                        onKeyUp={(e) => {handleAutoCompleteKeyUp(e, "boundary")}}
                        value={routerSearchSet.boundary}
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
                  </Row>
                 </Grid>
              </CardText>
           </Card>
           <div style={{textAlign: 'center'}}>
             <RaisedButton style={{margin:'15px 5px'}} type="submit" label="Search" backgroundColor={"#5a3e1b"} labelColor={white}/>
             <RaisedButton style={{margin:'15px 5px'}} label="Close"/>
           </div>
         </form>
         {viewTable()}
        </div>
    );
  }
}

const mapStateToProps = state => {
  return ({routerSearchSet: state.form.form});
};
const mapDispatchToProps = dispatch => ({
  initForm: (type) => {
        dispatch({
        type: "RESET_STATE",
        validationData: {
          required: {
            current: [],
            required: []
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
      var self = _this;
      dispatch({type: "HANDLE_CHANGE", property: type, value: '', isRequired : true, pattern: ''});
  }
});

export default connect(mapStateToProps, mapDispatchToProps)(searchRouter);
