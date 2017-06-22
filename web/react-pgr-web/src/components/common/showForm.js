import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import Checkbox from 'material-ui/Checkbox';
import {brown500, red500,white,orange800} from 'material-ui/styles/colors';
import RaisedButton from 'material-ui/RaisedButton';
import FlatButton from 'material-ui/FlatButton';
import ActionAndroid from 'material-ui/svg-icons/action/android';
import FontIcon from 'material-ui/FontIcon';
import Fields from '../common/Fields';
import {translate} from './common';

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
    marginBottom: 16,
  },
  uploadButton: {
    verticalAlign: 'middle',
  },
  uploadInput: {
    cursor: 'pointer',
    position: 'absolute',
    top: 0,
    bottom: 0,
    right: 0,
    left: 0,
    width: '100%',
    opacity: 0,
  }
};

class ShowForm extends Component{
  constructor(props){
    super(props);
    this.state={};
  }
  componentDidMount(){
    let {initForm} = this.props;
    initForm();
  }
  handleFormFields = () => {
    var currentThis = this;
    let FormFields = this.props.data.filter(function (el) {
      return (el.code != 'CHECKLIST' && el.code != 'DOCUMENTS') ;
    });

    if(FormFields.length > 0){
      return FormFields.map((item,index) =>
      {
        return (
          <Fields key={index} obj={item} handler={(e, property, isRequired, pattern) => { currentThis.props.handleChange(e, property, isRequired, pattern)}}/>
        );
      })
    }
  }
  handleCheckList = () => {
    let Checklist = this.props.data.filter(function (el) {
      return el.code == 'CHECKLIST';
    });
    if(Checklist.length > 0){
      let attribValues = Checklist[0].attribValues;
      return attribValues.map((item,index)=>
      {
        if(item.isActive){
          let description = translate(item.name);
          return (
            <Row style={styles.marginStyle} key={index}>
              <Col xs={1} md={1}>
                <Checkbox />
              </Col>
              <Col xs={11} md={11}>
              {description}
              </Col>
            </Row>
          );
        }
      })
    }
  }
  handleDocuments = () => {
    let Documents = this.props.data.filter(function (el) {
      return el.code == 'DOCUMENTS';
    });
    if(Documents.length > 0){
      let attribValues = Documents[0].attribValues;
      return attribValues.map((item,index)=>
      {
        if(item.isActive){
          let description = translate(item.name);
          return (
            <Col xs={4} style={styles.checkbox} key={index}>
              <div style={{marginBottom:'5px',fontSize:'16px'}}>{description}</div>
                <RaisedButton label="Upload" labelPosition="before" fullWidth={true} containerElement="label" primary={true} icon={<FontIcon className="material-icons" >file_upload</FontIcon>}>
                  <input type="file" style={{display:'none'}} />
                </RaisedButton>
            </Col>
          );
        }
      })
    }
  }
  render(){
    return (<div>
      <Card style={styles.marginStyle}>
        <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} > Application Details < /div>}/>
        <CardText style={{padding:0}}>
        <Grid>
          <Row>
            {this.handleFormFields()}
          </Row>
        </Grid>
        </CardText>
      </Card>
      <Card style={styles.marginStyle}>
        <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} > Checklist < /div>}/>
        <CardText style={{padding:0}}>
        <Grid>
          {this.handleCheckList()}
        </Grid>
        </CardText>
      </Card>
      <Card style={styles.marginStyle}>
        <CardHeader title={< div style = {styles.headerStyle} > Documents < /div>}/>
        <CardText style={{padding:0}}>
        <Grid>
          <Row>
            {this.handleDocuments()}
          </Row>
        </Grid>
        </CardText>
      </Card>
    </div>);
  }
}

const mapStateToProps = state => {
  console.log(state.form.form)
  return ({ShowForm: state.form.form, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid});
}

const mapDispatchToProps = dispatch => ({
  initForm: () => {
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
  }
});

export default connect(mapStateToProps, mapDispatchToProps)(ShowForm);
