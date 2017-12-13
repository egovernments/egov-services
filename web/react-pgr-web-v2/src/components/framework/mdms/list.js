import React, { Component } from 'react';
import { connect } from 'react-redux';
import RaisedButton from 'material-ui/RaisedButton';
import UiMultiFieldAddToTableForMDMS from '../components/UiMultiFieldAddToTableForMDMS';
import { translate } from '../../common/common';
import Api from '../../../api/api';
import _ from 'lodash';

class MdmsComponent extends Component {
  constructor(props) {
    super(props);
    this.state = {
      item: {},
      isBtnDisabled: false,
      valueList: [],
      pathname: '',
    };
    this.initData = this.initData.bind(this);
  }

  // checkColumnName(headers,column)
  // {
  // 	for (var i = 0; i < headers.length; i++) {
  // 		if(headers[i].label.split(".")[3]==column)
  // 		{
  // 			return true
  // 		}
  // 	}
  // 	return false
  // }

  componentDidMount() {
    //Make call to node server
    //Make call to MDMS service
    //URL path to this page /mdms/:module/:master
    //Get all the fields and pass to table component directly
    //After successful edit/create, make call to mdms _create/_update

    // Api.commonApiPost("/specs/yaml/_search", {
    // 	module,
    // 	master
    // }).then(function(res) {
    //
    // }).catch(function(err) {
    // 	self.props.setLoadingStatus('hide');
    // 	self.props.toggleSnackbarAndSetText(true, err.message);
    // })
    this.initData(this.props);
  }

  initData(props) {
    let self = this;

    let module = props.match.params.module;
    let master = props.match.params.master;
    this.masterAllLowerCase = props.match.params.master.toLowerCase();
    this.masterMapping = {
      wastetype: {
        addupdate: 'WasteType',
      },
      vehicletype: {
        addupdate: 'VehicleType',
      },
      wastesubtype: {
        addupdate: 'WasteSubType',
      },
      shifttype: {
        addupdate: 'ShiftType',
      },
      collectiontype: {
        addupdate: 'CollectionType',
      },
      population: {
        addupdate: 'Population',
      },
      toilet: {
        addupdate: 'Toilet',
      },
      casestatus: {
        addupdate: 'caseStatus',
      },
      court: {
        addupdate: 'court',
      },
      side: {
        addupdate: 'side',
      },
      casetype: {
        addupdate: 'caseType',
      },
      bench: {
        addupdate: 'bench',
      },
      register: {
        addupdate: 'stamp',
      },
      casecategory: {
        addupdate: 'caseCategory',
      },
    };

    let data = {
      MdmsCriteria: {
        tenantId: localStorage.tenantId,
        moduleDetails: [
          {
            moduleName: module,
            masterDetails: [
              {
                name: master,
              },
            ],
          },
        ],
      },
    };

    let formData = {
      MasterMetaData: {
        tenantId: localStorage.tenantId,
        moduleName: module,
        masterName: master,
        masterData: [],
      },
    };

    if (this.masterMapping[this.masterAllLowerCase] !== undefined) {
      formData.MasterMetaData.masterName = this.masterMapping[this.masterAllLowerCase].addupdate;
    }
    props.setLoadingStatus('loading');

    //Fetch specs from specs service

    // court,caseType,side,caseType,caseCategory,bench,stamp and charge
    let lcms = {
      Court: 'court',
      Side: 'side',
      CaseType: 'caseType',
      Bench: 'bench',
      Register: 'stamp',
      CaseStatus: 'caseStatus',
      CaseCategory: 'caseCategory',
    };
    Api.commonApiPost('/spec-directory/' + module + '/' + master, {})
      .then(function(res) {
        master = lcms[res.header[0].label.split('.')[3]] || res.header[0].label.split('.')[3];

        data.MdmsCriteria.moduleDetails[0].masterDetails[0].name = master;
        let headers = [];
        for (var i = 0; i < res.header.length; i++) {
          headers.push(res.header[i].label.split('.')[4]);
        }
        // headers.push("modify");
        Api.commonApiPost('/egov-mdms-service/v1/_search', {}, data, false, true)
          .then(function(res2) {
            let arr = _.get(res2, 'MdmsRes.' + module + '.' + master) || [];
            let temp = [];
            if (arr && arr.length) {
              // let temp=[];

              // self.props.setFormData(formData);
              for (let i = 0; i < arr.length; i++) {
                // arr[i].modify = true;
                temp.push(_.pick(arr[i], headers));
              }

              // self.setState({
              // 	valueList: temp
              // })

              self.setState({
                valueList: temp,
              });
            }

            res.jsonPath = 'MdmsMetadata.masterData';
            self.setState({
              item: res,
              pathname: props.history.location.pathname,
            });

            formData.MasterMetaData.masterData = arr;
            console.log(formData);
            props.setFormData(formData);
            props.setLoadingStatus('hide');
          })
          .catch(function(err) {
            props.setLoadingStatus('hide');
            props.toggleSnackbarAndSetText(true, err.message);
          });
      })
      .catch(function(err) {
        props.setLoadingStatus('hide');
        props.toggleSnackbarAndSetText(true, err.message);
      });
  }

  componentWillReceiveProps(nextProps) {
    if (this.state.pathname && this.state.pathname != nextProps.history.location.pathname) {
      this.initData(nextProps);
    }
  }

  handleChange = (e, property) => {
    let { formData } = this.props;
    _.set(formData, property, e.target.value);
    this.props.setFormData(formData);
  };

  setDisabled = bool => {
    this.setState({
      isBtnDSisabled: bool || false,
    });
  };

  addOrUpdate = () => {
    let { formData } = this.props;
    let self = this;
    // console.log({...formData.MasterMetaData,formData.MdmsMetadata});
    formData.MasterMetaData.masterData = formData.hasOwnProperty('MdmsMetadata') ? formData.MdmsMetadata.masterData : [];
    self.props.setLoadingStatus('loading');
    Api.commonApiPost('/egov-mdms-create/v1/_create', {}, { MasterMetaData: formData.MasterMetaData }, false, true)
      .then(function(res) {
        console.log(res);
        self.props.setLoadingStatus('hide');
      })
      .catch(function(err) {
        self.props.setLoadingStatus('hide');
        self.props.toggleSnackbarAndSetText(true, err.message);
      });
  };

  render() {
    let { item, isBtnDisabled, valueList } = this.state;
    let { handleChange, setDisabled, addOrUpdate } = this;
    // console.log(item);
    // console.log(valueList);
    return (
      <div style={{ margin: '20px' }}>
        {item && (Object.keys(item).length && item.values && item.values.length) ? (
          <UiMultiFieldAddToTableForMDMS
            ui="google"
            useTimestamp={true}
            handler={handleChange}
            item={item}
            valueList={valueList}
            setDisabled={setDisabled}
          />
        ) : (
          <div />
        )}
        <br />
        {/*<RaisedButton
					label={translate("ui.framework.submit")}
					onClick={addOrUpdate}
					primary={true}
					disabled={isBtnDisabled}/>*/}
      </div>
    );
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
  handleChange: (e, property, isRequired, pattern, requiredErrMsg, patternErrMsg) => {
    dispatch({
      type: 'HANDLE_CHANGE_FRAMEWORK',
      property,
      value: e.target.value,
      isRequired,
      pattern,
      requiredErrMsg,
      patternErrMsg,
    });
  },
});

export default connect(mapStateToProps, mapDispatchToProps)(MdmsComponent);
