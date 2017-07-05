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

class viewOrUpdateReceivingMode extends Component {
    constructor(props) {
      super(props);
      this.state = {
        data:''
      }
    }

    componentWillMount() {
        var body = {}
        let  current = this;
        Api.commonApiPost("/pgr-master/receivingmode/v1/_search",{},body).then(function(response){
            console.log(response.ReceivingModeType);
            current.setState({data:response.ReceivingModeType});
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
        receivingCenterCreate,
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
        <div className="receivingModeCreate">
            <Card style={styles.marginStyle}>
                <CardHeader style={{paddingBottom:0}}  title={<div style={styles.headerStyle}>All Receiving Mode</div>} />
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
                                          <th>Description</th>
                                          <th>Channel</th>
                                          <th>Active</th>
                                          <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {this.state.data && this.state.data.map((e,i)=>{
                                          return(
                                            <tr key={i}>
                                              <td>{i+1}</td>
                                              <td>{e.name}</td>
                                              <td>{e.code}</td>
                                              <td>{e.description}</td>
                                              <td>{e.channels}</td>
                                              <td>{e.active?"True":"False"}</td>
                                              {url == '/pgr/viewOrUpdateReceivingMode/view' && <td><Link to={`/pgr/viewReceivingMode/${this.props.match.params.type}/${e.id}`} target="_blank"><RaisedButton style={{margin:'0 3px'}} label="View"/></Link></td>}
                                              {url == '/pgr/viewOrUpdateReceivingMode/edit' && <td><Link  to={`/pgr/receivingModeCreate/${this.props.match.params.type}/${e.id}`} target="_blank"><RaisedButton style={{margin:'0 3px'}} label="Edit"/></Link></td>}
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
  return ({receivingCenterCreate: state.form.form, files: state.form.files, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid,isTableShow:state.form.showTable,buttonText:state.form.buttonText});
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

export default connect(mapStateToProps, mapDispatchToProps)(viewOrUpdateReceivingMode);
