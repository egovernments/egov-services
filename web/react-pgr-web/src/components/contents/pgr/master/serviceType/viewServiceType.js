import React, {Component} from 'react';
import {connect} from 'react-redux';
import SimpleMap from '../../../../common/GoogleMaps.js';
import Divider from 'material-ui/Divider';
import {Grid, Row, Col, DropdownButton, ListGroup, ListGroupItem} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import {List, ListItem} from 'material-ui/List';
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

var _this;

const getNameById = function(object, id, property = "") {
  console.log(object,id,property);
  if (id == "" || id == null) {
        return "";
    }
    for (var i = 0; i < object.length; i++) {
        if (property == "") {
            if (object[i].id == id) {
              console.log(object[i].name);
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


class ViewServiceType extends Component {
    constructor(props) {
      super(props);
      this.state = {
          id:'',
          data:''
      }
    }

    componentWillMount() {

        if(this.props.match.params.id) {

            this.setState({id:this.props.match.params.id});
            let  current = this;
            let {setForm} = this.props;

            Api.commonApiPost("/pgr-master/service/v1/_search",{id:this.props.match.params.id},{}).then(function(response){
                console.log(response);
                current.setState({data:response.Service})
                setForm(response.Service[0])
            }).catch((error)=>{
                console.log(error);
            })
        }

    }

    componentDidMount() {
        let {setForm} = this.props;

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

    render() {
      let {categorySource}=this.state;
      let {
        viewServiceType ,
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

      let {submitForm} = this;

      console.log(isFormValid);

      return(
        <div className="viewServiceType">
          <Grid style={{width:'100%'}}>
            <Card style={{margin:'15px 0'}}>
              <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} >
                 View Grievance Type
               < /div>}/>
               <CardText style={{padding:'8px 16px 0'}}>
                 <Row style={styles.addBorderBottom}>
                   <Col xs={6} md={3}>
                    {translate("core.lbl.add.name")}
                   </Col>
                   <Col xs={6} md={3}>
                    {viewServiceType.serviceName ? viewServiceType.serviceName : ''}
                   </Col>
                   <Col xs={6} md={3}>
                     {translate("core.lbl.code")}
                   </Col>
                   <Col xs={6} md={3}>
                    {viewServiceType.serviceCode ? viewServiceType.serviceCode : ''}
                   </Col>
                 </Row>
                 <Row style={styles.addBorderBottom}>
                   <Col xs={6} md={3}>
                    {translate("core.lbl.description")}
                   </Col>
                   <Col xs={6} md={3}>
                    {viewServiceType.description? viewServiceType.description : ''}
                   </Col>
                   <Col xs={6} md={3}>
                     {translate("pgr.lbl.active")}
                   </Col>
                   <Col xs={6} md={3}>
                    {viewServiceType.active? 'true' : 'false'}
                   </Col>
                 </Row>
                 <Row style={styles.addBorderBottom}>
                   <Col xs={6} md={3}>
                    Category
                   </Col>
                   <Col xs={6} md={3}>
                    {getNameById(categorySource,viewServiceType.category) ? getNameById(categorySource,viewServiceType.category) : ''}
                   </Col>
                   <Col xs={6} md={3}>
                     SLA Hours
                   </Col>
                   <Col xs={6} md={3}>
                    {viewServiceType.slaHours? viewServiceType.slaHours : ''}
                   </Col>
                 </Row>
                 <Row style={styles.addBorderBottom}>
                   <Col xs={6} md={3}>
                    Has Financial Impact
                   </Col>
                   <Col xs={6} md={3}>
                    {viewServiceType.hasFinancialImpact? 'true' : 'false'}
                   </Col>
                 </Row>
               </CardText>
            </Card>
          </Grid>
        </div>
      )
    }
}

const mapStateToProps = state => {
  return ({viewServiceType : state.form.form, files: state.form.files, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid});
}

const mapDispatchToProps = dispatch => ({

  setForm: (data) => {
    dispatch({
      type: "SET_FORM",
      data,
      isFormValid:true,
      fieldErrors: {},
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
  }

})

export default connect(mapStateToProps, mapDispatchToProps)(ViewServiceType);
