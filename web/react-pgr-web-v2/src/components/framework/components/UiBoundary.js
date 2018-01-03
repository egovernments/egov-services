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
import {withRouter}  from 'react-router';


class UiBoundary extends Component {
  
  constructor(props) {
    super(props);
    this.state ={
      boundaryData: [],
      dropDownData: [],
      dropDownDataVal: {},
      labelArr: [],
      viewLabels: []
    };
  }

  componentDidMount() {
    this.fetchLocations(this.props.item);
  }

  componentWillReceiveProps(nextProps, nextState) {
    
  }
  shouldComponentUpdate(nextProps, nextState) {
    return true;
  }

  renderView = (labelData) => {
    return(
      <div>
          <Row>
            {!_.isEmpty(labelData) ? Object.keys(labelData).map((key, index) => {
              return (<Col
              id={key+index}
              // style={}
              xs={3} md={3}
            >
              <label> <span style={{fontSize: "13px", fontWeight: 600}}> {key} </span>
              <br/> {labelData[key]}</label>
            </Col>)
            }) : ''}
              
          </Row>
      </div>
    )
  }
  
  initDropdownValues = (boundaryData, bdryCode) => {
    var ddArr = [];
    var jPath = '';
    var viewLabels = {};
    var pathArr = jp.paths(boundaryData, `$..[?(@.code=='${bdryCode}')]`);
    console.log(pathArr)
    pathArr = pathArr[0];
    for(var i=0; i<(pathArr.length);) {
      ddArr.push(pathArr[i]+'[' + pathArr[i+1] + ']');
      jPath = ddArr.join('.');
      if(i>1) {
        var code = jp.query(boundaryData, jPath+'.code');
        var label = jp.query(boundaryData, jPath+'.label');
        var name = jp.query(boundaryData, jPath+'.name');
        viewLabels[label]= name[0];
        
        //for update screen
        if(this.props.match.url.split('/')[1] != 'view') {
          this.handler(code[0], label[0]);
        }
      }
      i+=2;
    }
    this.setState({
      viewLabels: viewLabels
    })
  }
  
  fetchLocations = (item) => {
    var queryObj = {
      hierarchyTypeCode: item.hierarchyType
    };
    var cityBdry;
    Api.commonApiPost('/egov-location/location/v11/boundarys/_search?', queryObj, {}, false, true)
    .then((res) => {
      var jpath = "";
      cityBdry = jp.query(res, `$.TenantBoundary[?(@.hierarchyType.name=="${item.hierarchyType}")].boundary[?(@.label=='City')]`);
      var labelArr = this.fetchLabels(cityBdry[0]);
      this.setState({
        boundaryData: cityBdry,
        labelArr: labelArr
      });
      this.setFirstDropDownData(cityBdry);
      if(window.location.hash.split('/')[1] != 'create') {
        console.log(this.props.formData, this.props.item.jsonPath);
        if(!_.isEmpty(this.props.formData)) {
          if(typeof(_.get(this.props.formData, this.props.item.jsonPath)) != 'undefined') {
            this.initDropdownValues(cityBdry, _.get(this.props.formData, this.props.item.jsonPath));
          }
        }
      }
    });
  } 


  

  getDepth = (obj) => {
    var depth = 0;
    if (obj.children) {
        obj.children.forEach((d) => {
            var tmpDepth = this.getDepth(d)
            if (tmpDepth > depth) {
                depth = tmpDepth
            }
        })
    }
    return (1 + depth)
  }

  getLabelName = (obj) => {
    var label;
      for(var i=0; i<obj.length-1; i++) {
        if(obj[i].code && obj[i].name && obj[i].label && obj[i].code != ''&& obj[i].name != '' && obj[i].label != '') {
          return obj[i].label;
        }
      }
    return null;
  }

  fetchLabels = (cityBdry) => {
    var depth = this.getDepth(cityBdry);
    var labelArr = [];  
    var str = '';

    var bdryArr = jp.query(cityBdry, `$.children..label`);
    for(var i=0; i<bdryArr.length-1; i++) {
      if(bdryArr[i] !== "") {
        labelArr.push(bdryArr[i]);
      }
    }
    function onlyUnique(value, index, self) { 
      return self.indexOf(value) === index;
    }
    
    labelArr = labelArr.filter( onlyUnique );
    return labelArr;
  }

  handler = (key, property) => {
    let {dropDownDataVal, dropDownData}=this.state;
    console.log(key,property)
    this.setState({
      dropDownDataVal:{
      ...dropDownDataVal,
      [property]:key}      
    }, console.log(this.state.dropDownDataVal))
    //below runs for create & update only
    this.populateNextDropDown(key,property);
    console.log(key, property)
    console.log(this.state.labelArr);

    if(property == this.state.labelArr[this.state.labelArr.length-1]) {
      let formData = _.cloneDeep(this.props.formData);

      _.set(formData, this.props.item.jsonPath, key);
      this.props.setFormData(formData);
      
    }
    
  }

  setFirstDropDownData = (cityBdry) => {
    var objArr, ddData = [];
    objArr = (jp.query(cityBdry, `$.*.children[?(@.label=='${this.state.labelArr[0]}')]`));
    if(objArr.length >0) {
      objArr.map((v) => {
        var dd = {};
        dd.key= v.code;
        dd.value = v.name;
        ddData.push(dd);
      })
    }
    this.setState({
      dropDownData:{
        ...this.state.dropDownData,
        [this.state.labelArr[0]]: ddData
      }
    })
  }
  
  populateNextDropDown = (key, property) => {
    var index = this.state.labelArr.indexOf(property);
    if(index> -1) {
      var objArr, ddData = [];
      var str = "";
      for(var i=0; i<index; i++) {
        str = str+".*.children";
      }
      var jPath = "$.*.children" + str + `[?(@.code=='${key}')]`;
      objArr = jp.query(this.state.boundaryData, jPath + `.children[?(@.label=='${this.state.labelArr[index+1]}')]`);
        if(objArr.length >0) {
          objArr.map((v) => {
            if(v.label == this.state.labelArr[index+1]) {
              var dd = {};
              dd.key= v.code;
              dd.value = v.name;
              ddData.push(dd);
            }
          })
        }
    }
    this.setState({
      dropDownData:{
        ...this.state.dropDownData,
        [this.state.labelArr[index + 1]]: ddData
      }
    })
  }

  renderFields = (level, screen) => {

    let {dropDownDataVal, dropDownData} = this.state;
    let labelProperty = {
      floatingLabelFixed: true,
      floatingLabelText: (
        <span>
          {level} <span style={{ color: '#FF0000' }}>{this.props.item.isRequired ? ' *' : ''}</span>
        </span>
      ),
      hintText: '-- Please Select --',
    };
    return (
      <SelectField
        className="custom-form-control-for-select"
        id="hbjfs"
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
        // {...labelProperty}
        maxHeight={200}
        {...labelProperty}
        value={!_.isEmpty(dropDownDataVal) && dropDownDataVal.hasOwnProperty(level) && dropDownDataVal[level]} 
        onChange={(event, key, value) => {
          this.handler(value, level);
        }}
      >
        {dropDownData[level] && dropDownData[level].map((dd, index) => <MenuItem value={dd.key} key={index} primaryText={dd.value} />)}

      </SelectField>
    );
  }

  renderBoundary = (item) => {
    if(window.location.hash.split('/')[1] != 'view') {
      switch (this.props.ui) {
        case 'google':
          return (
            <div>
              <Row>
                  {this.state.labelArr.map((v, i) => {
                    return (
                      <Col xs={3} md={3} key={i}>
                        {this.renderFields(v, this.props.screen)}
                      </Col>
                    ); 
                  })}
                </Row>
                <br />
            </div>
          )
      }
    }
    else {}
  }


  render() {
    // console.log(this.props.item)
    // alert("rendering")
    return <div>{(this.props.match.url.split('/')[1] == 'view' && typeof(_.get(this.props.formData, this.props.item.jsonPath)) != 'undefined') ? this.renderView(this.state.viewLabels) : this.renderBoundary(this.props.item)}
      {this.props.item.type == 'boundary' ? null : this.props.callbackFromCollectionRoute(this.state.dropDownDataVal)}
    </div>
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


export default withRouter (connect(mapStateToProps, mapDispatchToProps)(UiBoundary));
