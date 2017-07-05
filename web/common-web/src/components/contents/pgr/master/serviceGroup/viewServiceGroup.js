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
                console.log(response);
                current.setState({data:response.ServiceGroups})
                setForm(response.ServiceGroups[0])
            }).catch((error)=>{
                console.log(error);
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
              <Card style={styles.marginStyle}>
                  <CardHeader  style={{paddingBottom:0}} title={< div style = {styles.headerStyle} >View Service Group< /div>} />
                  <CardText style={{padding:0}}>

                      <Grid>
                          <br/>
                          <Card>
                              <CardText>
                                  <ListGroup>
                                      <ListGroupItem>ID <span style={{float:'right'}}>{viewServiceGroup.id ? viewServiceGroup.id : ''}</span></ListGroupItem>
                                      <ListGroupItem>{translate("core.lbl.add.name")} <span style={{float:'right'}}>{viewServiceGroup.name ? viewServiceGroup.name : ''}</span></ListGroupItem>
                                      <ListGroupItem>{translate("core.lbl.code")} <span style={{float:'right'}}>{viewServiceGroup.code ? viewServiceGroup.code : ''}</span></ListGroupItem>
                                      <ListGroupItem>{translate("core.lbl.description")} <span style={{float:'right'}}>{viewServiceGroup.description? viewServiceGroup.description : ''}</span></ListGroupItem>
                                  </ListGroup>
                              </CardText>
                          </Card>
                      </Grid>
                  </CardText>
              </Card>
        </div>)
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
