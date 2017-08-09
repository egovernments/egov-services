import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Grid, Row, Col, DropdownButton, ListGroup, ListGroupItem} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import {List, ListItem} from 'material-ui/List';
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

class viewReceivingSet extends Component {
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
            Api.commonApiPost("/pgr-master/receivingmode/v1/_search",{id:this.props.match.params.id},{}).then(function(response){
                current.setState({data:response.ReceivingModeType})
                setForm(response.ReceivingModeType[0])
            }).catch((error)=>{
                console.log(error);
            })
        }
    }


    render() {

      let {
        viewReceivingSet ,
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

      console.log(viewReceivingSet);

      let {submitForm} = this;

      return(
        <div className="viewReceivingSet">
          <Grid style={{width:'100%'}}>
            <Card style={{margin:'15px 0'}}>
              <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} >
                 {translate("pgr.lbl.view")} {translate("pgr.lbl.receivingmode")}
               < /div>}/>
               <CardText style={{padding:'8px 16px 0'}}>
                 <Row style={styles.addBorderBottom}>
                   <Col xs={6} md={3}>
                    {translate("core.lbl.add.name")}
                   </Col>
                   <Col xs={6} md={3}>
                    {viewReceivingSet.name ? viewReceivingSet.name : ''}
                   </Col>
                   <Col xs={6} md={3}>
                     {translate("core.lbl.code")}
                   </Col>
                   <Col xs={6} md={3}>
                    {viewReceivingSet.code ? viewReceivingSet.code : ''}
                   </Col>
                 </Row>
                 <Row style={styles.addBorderBottom}>
                   <Col xs={6} md={3}>
                    {translate("core.lbl.description")}
                   </Col>
                   <Col xs={6} md={3}>
                    {viewReceivingSet.description? viewReceivingSet.description : ''}
                   </Col>
                   <Col xs={6} md={3}>
                     {translate("pgr.lbl.active")}
                   </Col>
                   <Col xs={6} md={3}>
                    {viewReceivingSet.active? "True" : "False"}
                   </Col>
                 </Row>
                 <Row style={styles.addBorderBottom}>
                   <Col xs={6} md={3}>
                    Channel
                   </Col>
                   <Col xs={6} md={3}>
                    {viewReceivingSet.channels ? viewReceivingSet.channels : ''}
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
  return ({viewReceivingSet : state.form.form, files: state.form.files, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid});
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

export default connect(mapStateToProps, mapDispatchToProps)(viewReceivingSet);
