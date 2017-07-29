import React, {Component} from 'react';
import {connect} from 'react-redux';
import ImagePreview from '../../../../common/ImagePreview.js';
import SimpleMap from '../../../../common/GoogleMaps.js';
import {Link, Route} from 'react-router-dom';

import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import TextField from 'material-ui/TextField';
import {brown500, red500,white,orange800} from 'material-ui/styles/colors';
import Checkbox from 'material-ui/Checkbox';
import DatePicker from 'material-ui/DatePicker';
import SelectField from 'material-ui/SelectField';
import AutoComplete from 'material-ui/AutoComplete';
import MenuItem from 'material-ui/MenuItem';
import Dialog from 'material-ui/Dialog';
import RaisedButton from 'material-ui/RaisedButton';
import FlatButton from 'material-ui/FlatButton';
import Api from '../../../../../api/api';
import {translate} from '../../../../common/common';

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
  headerStyle : {
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
  },
  checkbox: {
    marginTop: 37
  }
};

var _this;

class ViewEditServiceGroup extends Component {
    constructor(props) {
      super(props);
      this.state = {
        data:'',
        modify: false
      }
    }

    componentWillMount() {
        $('#searchTable').DataTable({
             dom: 'lBfrtip',
             buttons: [],
              bDestroy: true,
              language: {
                 "emptyTable": "No Records"
              }
        });
    }

    componentWillUpdate() {
      if(flag == 1) {
        flag = 0;
        $('#searchTable').dataTable().fnDestroy();
      }
    }

    componentWillUnmount(){
       $('#searchTable')
       .DataTable()
       .destroy(true);
    }

    componentDidUpdate() {
      if(this.state.modify)
        $('#searchTable').DataTable({
             dom: 'lBfrtip',
             buttons: [],
              bDestroy: true,
              language: {
                 "emptyTable": "No Records"
              }
        });
    }

    componentDidMount() {
      let {initForm}=this.props;
      initForm();
      var body = {}
      let  current = this;
      current.props.setLoadingStatus("loading");
      Api.commonApiPost("/pgr-master/serviceGroup/v1/_search",{keyword : 'complaint'},body).then(function(response){
          current.setState({
            data:response.ServiceGroups,
            modify: true
          });
          current.props.setLoadingStatus("hide");
      }).catch((error)=>{
          current.setState({
            modify: true
          })
          current.props.setLoadingStatus("hide");
      })
    }

    handleNavigation = (type, id) => {
      this.props.history.push(type+id);
    }


    render() {

      let {
        viewEditServiceGroup,
        fieldErrors,
        isFormValid,
        isTableShow,
        handleUpload,
        files,
        handleChange,
        handleMap,
        handleChangeNextOne,
        handleChangeNextTwo,
        buttonText
      } = this.props;



      let url = this.props.location.pathname;

      return(
        <div className="viewEditServiceGroup">
            <Card style={styles.marginStyle}>
                <CardHeader style={{paddingBottom:0}}  title={<div style={styles.headerStyle}>All Categories</div>} />
                <CardText style={{padding:0}}>
                    <Grid>
                        <Row>
                            <Col xs={12} md={12}>
                                <Table id="searchTable" bordered responsive className="table-striped">
                                    <thead>
                                        <tr>
                                          <th>ID</th>
                                          <th>{translate("core.lbl.add.name")}</th>
                                          <th>{translate("core.lbl.code")}</th>
                                          <th>{translate("core.lbl.description")}</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {this.state.data && this.state.data.map((e,i)=>{
                                          return(
                                            <tr key={i}onClick={()=>{
                        if(url == '/pgr/serviceGroup/view'){
                          this.props.history.push('/pgr/viewServiceGroup/'+e.id);
                        } else {
                          this.props.history.push('/pgr/updateServiceGroup/'+e.id);
                        }
                      }}>
                                              <td>{e.id}</td>
                                              <td>{e.name}</td>
                                              <td>{e.code}</td>
                                              <td>{e.description}</td>
                                            </tr>
                                          )
                                        })}
                                    </tbody>
                                </Table>
                            </Col>
                        </Row>
                    </Grid>
                </CardText>
            </Card>
        </div>)
    }

}

const mapStateToProps = state => {
  return ({viewEditServiceGroup: state.form.form, files: state.form.files, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid,isTableShow:state.form.showTable,buttonText:state.form.buttonText});
}

const mapDispatchToProps = dispatch => ({
  initForm: () => {
    dispatch({
      type: "RESET_STATE",
      validationData: {
        required: {
          current: [],
          required: ["name","code","description"]
        },
        pattern: {
          current: [],
          required: []
        }
      }
    });
  },

  handleChange: (e, property, isRequired, pattern) => {
    console.log("handlechange"+e+property+isRequired+pattern);
    dispatch({
      type: "HANDLE_CHANGE",
      property,
      value: e.target.value,
      isRequired,
      pattern
    });
  },

  setLoadingStatus: (loadingStatus) => {
    dispatch({type: "SET_LOADING_STATUS", loadingStatus});
  }
})

export default connect(mapStateToProps, mapDispatchToProps)(ViewEditServiceGroup);
