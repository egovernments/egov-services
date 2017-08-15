import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Grid, Row, Col} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import TextField from 'material-ui/TextField';
import AutoComplete from 'material-ui/AutoComplete';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import RaisedButton from 'material-ui/RaisedButton';
import Dialog from 'material-ui/Dialog';
import Api from '../../../../api/api';
import {translate} from '../../../common/common';

const styles = {
  headerStyle : {
    fontSize : 19
  },
  marginStyle:{
    margin: '15px'
  }
};

var _this;

class searchUserRole extends Component {
  constructor(props) {
    super(props);
    this.state={
      autocompleteSourceConfig: {
       text: 'userName',
       value: 'id',
      },
    };
  }
  componentDidMount(){
    _this = this;
    let {initForm} = this.props;
    initForm();
    let tenantId = localStorage.getItem('tenantId') || 'default';
    _this.props.setLoadingStatus('loading');
    Api.commonApiPost("/user/v1/_search",{},{tenantId : tenantId}).then(function(response) {
      _this.props.setLoadingStatus('loading');
      //list only employee type users
      var EMP_USER = response.user.filter(function (el){
        return el.type !== 'CITIZEN';
      })
      _this.setState({users: EMP_USER});
      _this.props.setLoadingStatus('hide');
    }, function(err) {
      _this.props.setLoadingStatus('hide');
      _this.handleError(err.message);
    });
  }

  handleError = (msg) => {
    this.props.setLoadingStatus('loading');
    let {toggleDailogAndSetText, toggleSnackbarAndSetText}=this.props;
    toggleDailogAndSetText(true, msg);
  }

  search = () => {
    let {setRoute} = this.props;
    setRoute("/administration/updateUserRole/"+this.props.searchUserRole.userName);
  }

  render(){
    let {search} = this;
    let {handleAutoCompleteKeyUp, handleChange, isFormValid} = this.props;
    return(
      <div className="userRole">
        <Card style={styles.marginStyle}>
          <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} > User Role< /div>}/>
            <CardText style={{paddingTop:0}}>
              <Grid>
                <Row>
                  <Col md={4}>
                    <AutoComplete
                      ref="userName"
                      floatingLabelText="User Name *"
                      filter={function filter(searchText, key) {
                        return key.toLowerCase().includes(searchText.toLowerCase());
                      }}
                      fullWidth={true}
                      dataSource={this.state.users ? this.state.users : []}
                      dataSourceConfig={this.state.autocompleteSourceConfig}
                      menuStyle={{overflow:'auto', maxHeight: '150px'}}
                      onKeyUp={(e) => {handleAutoCompleteKeyUp(e, "userName")}}
                      onNewRequest={(chosenRequest, index) => {
                        if(index === -1){
                          this.refs['userName'].setState({searchText:''});
                        }else{
                          handleChange(chosenRequest.id, "userName", true, "");
                        }
                     }}
                    />
                  </Col>
                </Row>
              </Grid>
            </CardText>
        </Card>
        <div style={{textAlign: 'center'}}>
          <RaisedButton style={{margin:'15px 5px'}} onTouchTap={(e) => search(e)} disabled={!isFormValid} label="View" primary={true}/>
        </div>
      </div>
    )
  }
}

const mapStateToProps = state => {
  return ({searchUserRole: state.form.form, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid});
}

const mapDispatchToProps = dispatch => ({
  initForm: () => {
    dispatch({
      type: "RESET_STATE",
      validationData: {
        required: {
          current: [],
          required: ['userName']
        },
        pattern: {
          current: [],
          required: []
        }
      }
    });
  },
  handleChange: (value, property, isRequired, pattern, errorMsg) => {
    dispatch({
      type: "HANDLE_CHANGE",
      property,
      value,
      isRequired,
      pattern,
      errorMsg
    });
  },
  handleAutoCompleteKeyUp : (e, type) => {
    dispatch({
      type: "HANDLE_CHANGE",
      property: type,
      value: '',
      isRequired : true,
      pattern: ''
    });
  },
  toggleDailogAndSetText: (dailogState,msg) => {
    dispatch({type: "TOGGLE_DAILOG_AND_SET_TEXT", dailogState,msg});
  },
  setLoadingStatus: (loadingStatus) => {
    dispatch({type: "SET_LOADING_STATUS", loadingStatus});
  },
  setRoute:(route)=>dispatch({type:'SET_ROUTE',route})
});

export default connect(mapStateToProps, mapDispatchToProps)(searchUserRole);
