import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Grid, Row, Col, DropdownButton, ListGroup, ListGroupItem} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import {List, ListItem} from 'material-ui/List';
import TextField from 'material-ui/TextField';
import Checkbox from 'material-ui/Checkbox';
import DatePicker from 'material-ui/DatePicker';
import SelectField from 'material-ui/SelectField';
import AutoComplete from 'material-ui/AutoComplete';
import MenuItem from 'material-ui/MenuItem';
import Dialog from 'material-ui/Dialog';
import RaisedButton from 'material-ui/RaisedButton';
import FlatButton from 'material-ui/FlatButton';
import Api from '../../../../../api/api';
import styles from '../../../../../styles/material-ui';
import {translate} from '../../../../common/common';

var _this;

class ViewServiceGroup extends Component {
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

            Api.commonApiPost("/pgr-master/serviceGroup/v1/_search",{id:this.props.match.params.id},{}).then(function(response){
                current.setState({data:response.ServiceGroups})
                setForm(response.ServiceGroups[0])
            }).catch((error)=>{
            })
        }

    }

    componentDidMount() { }

    render() {

      let {
        viewServiceGroup ,
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
        <div className="viewServiceGroup">
        <Grid style={{width:'100%'}}>
          <Card style={{margin:'15px 0'}}>
            <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} >
               {translate('pgr.lbl.view')} {translate('pgr.lbl.grievance.category') }
             < /div>}/>
             <CardText style={{padding:'8px 16px 0'}}>
               <Row>
                 <Col xs={6} md={3}>
                  {translate("core.lbl.add.name")}
                 </Col>
                 <Col xs={6} md={3}>
                  {viewServiceGroup.name ? viewServiceGroup.name : ''}
                 </Col>
                 <Col xs={6} md={3}>
                   {translate("core.lbl.code")}
                 </Col>
                 <Col xs={6} md={3}>
                  {viewServiceGroup.code ? viewServiceGroup.code : ''}
                 </Col>
               </Row>
               <Row>
                 <Col xs={6} md={3}>
                  {translate("core.lbl.description")}
                 </Col>
                 <Col xs={6} md={3}>
                  {viewServiceGroup.description? viewServiceGroup.description : ''}
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
  return ({viewServiceGroup : state.form.form, files: state.form.files, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid});
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

export default connect(mapStateToProps, mapDispatchToProps)(ViewServiceGroup);
