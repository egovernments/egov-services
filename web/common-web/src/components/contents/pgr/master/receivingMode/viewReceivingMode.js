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

class viewReceivingSet extends Component {
    constructor(props) {
      super(props);
      this.state = {
          id:'',
          data:''
      }
    }

    close(){
     window.open(window.location, '_self').close();
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
              <Card style={styles.marginStyle}>
                  <CardHeader  style={{paddingBottom:0}} title={< div style = {styles.headerStyle} >View Receiving Mode< /div>} />
                  <CardText style={{padding:0}}>

                      <Grid>
                          <br/>
                          <Card>
                              <CardText>
                                  <ListGroup>
                                      <ListGroupItem>ID <span style={{float:'right'}}>{viewReceivingSet.id ? viewReceivingSet.id : ''}</span></ListGroupItem>
                                      <ListGroupItem>Name <span style={{float:'right'}}>{viewReceivingSet.name ? viewReceivingSet.name : ''}</span></ListGroupItem>
                                      <ListGroupItem>Code <span style={{float:'right'}}>{viewReceivingSet.code ? viewReceivingSet.code : ''}</span></ListGroupItem>
                                      <ListGroupItem>Description <span style={{float:'right'}}>{viewReceivingSet.description? viewReceivingSet.description : ''}</span></ListGroupItem>
                                      <ListGroupItem>Active <span style={{float:'right'}}>{viewReceivingSet.active? "True" : "False"}</span></ListGroupItem>
                                      <ListGroupItem>Channel<span style={{float:'right'}}>{viewReceivingSet.channels ? viewReceivingSet.channels : ''}</span></ListGroupItem>
                                  </ListGroup>
                              </CardText>
                          </Card>
                      </Grid>
                  </CardText>
              </Card>
              <div style={{textAlign:'center'}}>
                   <RaisedButton style={{margin:'15px 5px'}} label="Close" onClick={(e)=>{this.close()}}/>
                 </div>
        </div>)
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
