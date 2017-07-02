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

class viewOrUpdateServiceType extends Component {
    constructor(props) {
      super(props);
      this.state = {
        data:'',
        categorySource:[]
      }
    }

    componentWillMount() {
        var body = {}
        let  current = this;
        Api.commonApiPost("/pgr-master/service/v1/_search",{},body).then(function(response){
            console.log(response.Service);
            current.setState({data:response.Service});
        }).catch((error)=>{
            console.log(error);
        })
    }

    componentDidMount() {
     let {initForm}=this.props;
     initForm();
     _this = this;

     Api.commonApiPost("/pgr-master/serviceGroup/v1/_search").then(function(response) {
         _this.setState({
           categorySource: response.ServiceGroups
         })
     }, function(err) {
       _this.setState({
           categorySource: []
         })
     });
    }

    handleNavigation = (type, id) => {
      window.open(type+id, "_blank", "location=yes, height=760, width=800, scrollbars=yes, status=yes");
    }



    render() {
      let {categorySource}=this.state;
      let {
        serviceTypeCreate,
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
        <div className="serviceTypeCreate">
            <Card style={styles.marginStyle}>
                <CardHeader style={{paddingBottom:0}}  title={<div style={styles.headerStyle}>All Service</div>} />
                <CardText style={{padding:0}}>
                    <Grid>
                        <Row>
                            <Col xs={12} md={12}>
                                <Table>
                                    <thead>
                                        <tr>
                                          <th>Sl No</th>
                                          <th>Name</th>
                                          <th>Code</th>
                                          <th>Category</th>
                                          <th>Active</th>
                                          <th>Description</th>
                                          <th>SLA Hour</th>
                                          <th>Has Financial Impact</th>
                                          <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {this.state.data && this.state.data.map((e,i)=>{
                                          return(
                                            <tr key={i}>
                                              <td>{i+1}</td>
                                              <td>{e.serviceName}</td>
                                              <td>{e.serviceCode}</td>
                                              <td>{getNameById(categorySource,e.category)}</td>
                                              <td>{e.active?"True":"False"}</td>
                                              <td>{e.description}</td>
                                              <td>{e.slaHours}</td>
                                              <td>{e.hasFinancialImpact?"True":"False"}</td>
                                              {url == '/pgr/viewOrUpdateServiceType/view' && <td><RaisedButton style={{margin:'0 3px'}} label={translate("pgr.lbl.view")} onClick={()=> {
                                                let id = e.id;
                                                this.handleNavigation("/pgr/viewServiceType/view/", id);
                                              }}/></td>}
                                              {url == '/pgr/viewOrUpdateServiceType/edit' && <td><RaisedButton style={{margin:'0 3px'}} label={translate("pgr.lbl.edit")} onClick={()=> {
                                                let id = e.id;
                                                this.handleNavigation("/pgr/serviceTypeCreate/edit/", id);
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
  return ({serviceTypeCreate: state.form.form, files: state.form.files, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid,isTableShow:state.form.showTable,buttonText:state.form.buttonText});
}

const mapDispatchToProps = dispatch => ({
  initForm: () => {
    dispatch({
      type: "RESET_STATE",
      validationData: {
        required: {
          current: [],
          required: ["name","code","channel","description"]
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

export default connect(mapStateToProps, mapDispatchToProps)(viewOrUpdateServiceType);
