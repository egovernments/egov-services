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


var flag = 0;
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
  },
  checkbox: {
    marginTop: 37
  }
};

var _this;

class ViewEditReceivingCenter extends Component {
    constructor(props) {
      super(props);
      this.state = {
        data:''
      }
    }

    componentWillMount() {
        var body = {}
        let  current = this;
        Api.commonApiPost("/pgr-master/serviceGroup/v1/_search",{},body).then(function(response){
            console.log(response);
            current.setState({data:response.ServiceGroups});
        }).catch((error)=>{
            console.log(error);
        })
    }

    componentDidMount() {
     let {initForm}=this.props;
     initForm();
    }

    handleNavigation = (type, id) => {
      window.open(type+id, "_blank", "location=yes, height=760, width=800, scrollbars=yes, status=yes");
    }


    render() {

      let {
        serviceGroupCreate,
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
        <div className="serviceGroupCreate">
            <Card style={styles.marginStyle}>
                <CardHeader style={{paddingBottom:0}}  title={<div style={styles.headerStyle}>All Service Group</div>} />
                <CardText style={{padding:0}}>
                    <Grid>
                        <Row>
                            <Col xs={12} md={12}>
                                <Table>
                                    <thead>
                                        <tr>
                                          <th>ID</th>
                                          <th>{translate("core.lbl.add.name")}</th>
                                          <th>{translate("core.lbl.code")}</th>
                                          <th>{translate("core.lbl.description")}</th>
                                          <th></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {this.state.data && this.state.data.map((e,i)=>{
                                          return(
                                            <tr key={i}>
                                              <td>{e.id}</td>
                                              <td>{e.name}</td>
                                              <td>{e.code}</td>
                                              <td>{e.description}</td>
                                              <td>{e.active}</td>
                                              <td>{e.auditDetails}</td>
                                              <td>{e.iscrnrequired}</td>
                                              <td>{e.orderno}</td>
                                              {url == '/pgr/serviceGroup/view' && <td><RaisedButton style={{margin:'0 3px'}} label={translate("pgr.lbl.view")} onClick={()=> {
                                                let id = e.id;
                                                this.handleNavigation("/pgr/viewServiceGroup/", id);
                                              }}/></td>}
                                              {url == '/pgr/serviceGroup/edit' && <td><RaisedButton style={{margin:'0 3px'}} label={translate("pgr.lbl.edit")} onClick={()=> {
                                                let id = e.id;
                                                this.handleNavigation("/pgr/createServiceGroup/", id);
                                              }}/></td>}
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
  return ({serviceGroupCreate: state.form.form, files: state.form.files, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid,isTableShow:state.form.showTable,buttonText:state.form.buttonText});
}

const mapDispatchToProps = dispatch => ({
  initForm: () => {
    dispatch({
      type: "RESET_STATE",
      validationData: {
        required: {
          current: [],
          required: ["name","code","orderno", "description"]
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
  }
})

export default connect(mapStateToProps, mapDispatchToProps)(ViewEditReceivingCenter);
