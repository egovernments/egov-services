import React, { Component } from 'react';
import { connect } from 'react-redux';
import UiSelectField from './UiSelectField';
import { translate, validate_fileupload } from '../../common/common';
import RaisedButton from 'material-ui/RaisedButton';
import {SelectField, MenuItem} from 'material-ui';
import { Grid, Row, Col, DropdownButton } from 'react-bootstrap';
import _ from 'lodash';
import Api from '../../../api/api';
import jp from 'jsonpath';
import Boundary from './UiBoundary';

class UiCollectionRoute extends Component {

  constructor(props) {
    super(props);
    this.state = {
      collectionPointData: [],
      collectionPointVal:"",
      dataFromBoundary: '',
      level: ''
    }
  }

  componentDidMount() {
    // var level = this.props.item.levelNamesInOrder[this.props.item.levelNamesInOrder.length-1]
    // this.fetchCollectionPoint(this.state.dataFromBoundary[this.state.level])
  }

  fetchCollectionPoint = (levelName) => {
    var queryObj = {
      locationCode: levelName
    }
    
    Api.commonApiPost('/swm-services/collectionpoints/_search?', queryObj, {}, false, true)
    .then((res) => {
      var jpath = "";
      // console.log(res);
      this.setState({
        collectionPointData: res.collectionPoints
      });
    });
  }

  handlerCollectionPoint = (key, value) => {
    this.setState({
      collectionPointVal: value
    })
    this.props.handler(
      {
        target: {
          value: this.state.collectionPointVal ? this.state.collectionPointVal : '',
        },
      },
      this.props.item.jsonPath,
      this.props.item.isRequired ? true : false,
      '',
      ''
    )
    let _formData = _.cloneDeep(this.props.formData);
      _.set(_formData, this.props.item.jsonPath, key);
      this.props.setFormData(_formData);
  }

  renderCollectionPoint = () => {
    let {collectionPointVal} = this.state;
    var ddArr = [];
    this.state.collectionPointData.map((item) => {
      let ddObj = {};
      ddObj.key = item.code;
      ddObj.value = item.name;
      ddArr.push(ddObj);
    })
    let labelProperty = {
      floatingLabelFixed: true,
      floatingLabelText: (
        <span>
          {"Collection Points"} <span style={{ color: '#FF0000' }}>{this.props.item.isRequired ? ' *' : ''}</span>
        </span>
      ),
      hintText: '-- Please Select --',
    };
    return (
      <Row>
          <Col xs={3} md={3} key=''>
            <SelectField
              // className="custom-form-control-for-select"
              // id="sdf"
              floatingLabelStyle={{
                color: '#696969',
                fontSize: '20px',
                'white-space': 'nowrap',
              }}
              labelStyle={{ color: '#5F5C57' }}
              dropDownMenuProps={{
                animated: false,
                targetOrigin: { horizontal: 'left', vertical: 'bottom' },
              }}
              style={{ display: 'inline-block' }}
              errorStyle={{ float: 'left' }}
              fullWidth={true}
              underlineDisabledStyle={{ backgroundColor: '#eee!important' }}
              maxHeight={200}
              {...labelProperty}
              value={!_.isEmpty(collectionPointVal) && collectionPointVal} 
              onChange={(event, key, value) => {
                this.handlerCollectionPoint(key, value);
              }}
            > 
              {ddArr.map( (dd, index) =>  <MenuItem value={dd.key} key={index} primaryText={dd.value}/> )}
              
            </SelectField>
          </Col>
      </Row>
      
    )

    
  }

  boundaryDropDownDataVal = (dataFromBoundary, labelArr) => {
    console.log(dataFromBoundary, labelArr);
    var level = labelArr[labelArr.length-1]
    console.log(level)
    if(!_.isEmpty(dataFromBoundary[level])) {
      this.fetchCollectionPoint(dataFromBoundary[level])
    }
  }

  render() {
    return (
      <div> 
        <Boundary 
          callbackFromCollectionRoute={this.boundaryDropDownDataVal}
          item={this.props.item}
          ui={this.props.ui}
          screen={this.props.screen}/>
        {this.renderCollectionPoint()}
      </div>
    )
  }
}

const mapStateToProps = state => ({
  formData: state.frameworkForm.form,
});

const mapDispatchToProps = dispatch => ({
  setFormData: data => {
    dispatch({ type: 'SET_FORM_DATA', data });
  },
  toggleSnackbarAndSetText: (snackbarState, toastMsg, isSuccess, isError) => {
    dispatch({
      type: 'TOGGLE_SNACKBAR_AND_SET_TEXT',
      snackbarState,
      toastMsg,
      isSuccess,
      isError,
    });
  },
  setLoadingStatus: loadingStatus => {
    dispatch({ type: 'SET_LOADING_STATUS', loadingStatus });
  },
});


export default connect(mapStateToProps, mapDispatchToProps)(UiCollectionRoute);