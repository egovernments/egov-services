import React, { Component } from 'react';
import { connect } from 'react-redux';
import TextField from 'material-ui/TextField';
import { translate } from '../../common/common';
var DateTime = require('react-datetime');
var moment = require('moment');

const datePat = /^(((0[1-9]|[12]\d|3[01])\/(0[13578]|1[02])\/((19|[2-9]\d)\d{2}))|((0[1-9]|[12]\d|30)\/(0[13456789]|1[012])\/((19|[2-9]\d)\d{2}))|((0[1-9]|1\d|2[0-8])\/02\/((19|[2-9]\d)\d{2}))|(29\/02\/((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))))$/g;
class UiDatePicker extends Component {
  constructor(props) {
    super(props);
  }

  /*calcMinMaxDate = (dateStr) => {
   		if(dateStr) {
   			if(dateStr == "today") {
   				return new Date();
   			} else if(dateStr.indexOf("+") > -1) {
   				var oneDay = 24 * 60 * 60 * 1000;
   				dateStr = dateStr.split("+")[1];
   				return new Date(new Date().getTime() + (Number(dateStr) * oneDay)).getTime();
   			} else {
   				var oneDay = 24 * 60 * 60 * 1000;
   				dateStr = dateStr.split("-")[1];
   				return new Date(new Date().getTime() - (Number(dateStr) * oneDay)).getTime();
   			}
   		} else {
   			return "";
   		}
   	}*/

  calcMinMaxDate = dateStr => {
    if (dateStr) {
      if (dateStr == 'today') {
        return moment();
      } else if (dateStr.indexOf('+') > -1) {
        var oneDay = 24 * 60 * 60 * 1000;
        dateStr = dateStr.split('+')[1];
        return moment(new Date(new Date().getTime() + Number(dateStr) * oneDay));
      } else {
        var oneDay = 24 * 60 * 60 * 1000;
        dateStr = dateStr.split('-')[1];
        return moment(new Date(new Date().getTime() - Number(dateStr) * oneDay));
      }
    } else {
      return '';
    }
  };

  getDateFormat = timeLong => {
    if (timeLong) {
      if ((timeLong.toString().length == 12 || timeLong.toString().length == 13) && new Date(Number(timeLong)).getTime() > 0) {
        var _date = new Date(Number(timeLong));
        return ('0' + _date.getDate()).slice(-2) + '/' + ('0' + (_date.getMonth() + 1)).slice(-2) + '/' + _date.getFullYear();
      } else {
        return timeLong;
      }
    } else if (!timeLong) return '';
  };

  renderDatePicker = item => {
    /*<TextField
											id={item.jsonPath.split(".").join("-")}
											className="custom-form-control-for-textfield"
											style={{"display": (item.hide ? 'none' : 'inline-block')}}
											floatingLabelStyle={{"color": item.isDisabled ? "#A9A9A9" : "#696969", "fontSize": "20px", "white-space": "nowrap"}}
											inputStyle={{"color": "#5F5C57"}}
											floatingLabelFixed={true}
											disabled={item.isDisabled}
											hintText="DD/MM/YYYY"
											maxLength={10}
											floatingLabelText={<span>{item.label} <span style={{"color": "#FF0000"}}>{item.isRequired ? " *" : ""}</span></span>}
											errorText={this.props.fieldErrors[item.jsonPath]}
											value={this.getDateFormat(this.props.getVal(item.jsonPath))}
											onChange={(e) => {
												var val = e.target.value;
												if(e.target.value.length == 2 && !e.target.value.match('/')){
													val+='/';
												} else if(e.target.value.length == 5) {
													var a = e.target.value.split('/');
													if(!a[1].match('/')){
														val+='/';
													}
												}
												if(e.target.value) {
													e.target.value = e.target.value.trim();
													if(datePat.test(e.target.value)){
														var _date = e.target.value;
														_date = _date.split("/");
														var newDate = _date[1]+"-"+_date[0]+"-"+_date[2];
														val = Number(new Date(newDate).getTime());
														if(item.minDate && val< this.calcMinMaxDate(item.minDate)) {
														return ;
													} else if(item.maxDate && val > this.calcMinMaxDate(item.maxDate)) {
															return ;
														}
													}
												}

												//check hasOwnProperty of epresssion
												//check isEnabled true
												//if() put and expression
												//if true overide item.isRequired=true and item.requiredErrMsg=""
												//if false
					                            this.props.handler({target: {value: val}}, item.jsonPath, item.isRequired ? true : false, /\d{12,13}/, item.requiredErrMsg, (item.patternErrMsg || translate("framework.date.error.message")), item.expression, item.expressionMsg, true)
					                        }}/>*/
    // console.log(item);
    //       console.log(item.showCurrentDate);

    // if(item.showCurrentDate === true){
    //     var defaultdate = new Date();
    // 	let day = defaultdate.getDate();
    // 	let month = defaultdate.getMonth()+1;
    // 	let year = defaultdate.getFullYear();
    // 	defaultdate = day+'/'+month+'/'+year;
    // }else{
    // 	var defaultdate = '';
    // }

    switch (this.props.ui) {
      case 'google':
        let label = !item.isHideLabel && (
          <div>
            <label>
              {item.label} <span style={{ color: '#FF0000' }}>{item.isRequired ? ' *' : ''}</span>
            </label>
            <br />
          </div>
        );

        return (
          <div
            style={{
              width: '100%',
              marginTop: '17px',
              display: item.hide ? 'none' : 'inline-block',
            }}
            className="custom-form-control-for-datepicker"
          >
            {label}
            <DateTime
              value={this.props.getVal(item.jsonPath)}
              dateFormat="DD/MM/YYYY"
              // defaultValue={defaultdate}
              timeFormat={false}
              inputProps={{
                placeholder: 'DD/MM/YYYY',
                id: item.jsonPath.split('.').join('-'),
                disabled: item.isDisabled,
              }}
              isValidDate={currentDate => {
                if (item.minDate && item.maxDate) {
                  return this.calcMinMaxDate(item.minDate).isBefore(currentDate) && this.calcMinMaxDate(item.maxDate).isAfter(currentDate);
                } else if (item.minDate) {
                  return this.calcMinMaxDate(item.minDate).isBefore(currentDate);
                } else if (item.maxDate) {
                  return this.calcMinMaxDate(item.maxDate).isAfter(currentDate);
                } else return true;
              }}
              closeOnSelect={true}
              closeOnTab={true}
              onChange={e => {
                this.minMaxvalidation(e, item);
              }}
            />
            <div
              style={{
                height: '23px',
                visibility: this.props.fieldErrors && this.props.fieldErrors[item.jsonPath] ? 'visible' : 'hidden',
                position: 'relative',
                fontSize: '12px',
                lineHeight: '23px',
                color: 'rgb(244, 67, 54)',
                transition: 'all 450ms cubic-bezier(0.23, 1, 0.32, 1) 0ms',
                float: 'left',
              }}
            >
              {this.props.fieldErrors && this.props.fieldErrors[item.jsonPath] ? this.props.fieldErrors[item.jsonPath] : ' '}
            </div>
          </div>
        );
    }
  };

  minMaxvalidation = (e, item) => {
    //chosen date
    if (typeof e === 'object'  || (typeof e === "string" && e.length === 0)) {
      if (item.minDate || item.maxDate) {
        if (moment().diff(moment(e.toDate()), 'days') < 0 && item.maxDate === 'today') {
          this.props.handler(
            { target: { value: '' } },
            item.jsonPath,
            item.isRequired ? true : false,
            /\d{12,13}/,
            item.requiredErrMsg,
            item.patternErrMsg || translate('framework.date.error.message'),
            item.expression,
            item.expressionMsg,
            true
          );
          let { toggleDailogAndSetText } = this.props;
          toggleDailogAndSetText(true, translate('framework.date.futureEror.message'));
        } else {
          //random string - not proper date
          this.props.handler(
            { target: { value: typeof e == 'string' ? e : e.valueOf() } },
            item.jsonPath,
            item.isRequired ? true : false,
            /\d{12,13}/,
            item.requiredErrMsg,
            item.patternErrMsg || translate('framework.date.error.message'),
            item.expression,
            item.expressionMsg,
            true
          );
        }
      } else {
        this.props.handler(
          { target: { value: typeof e == 'string' ? e : e.valueOf() } },
          item.jsonPath,
          item.isRequired ? true : false,
          /\d{12,13}/,
          item.requiredErrMsg,
          item.patternErrMsg || translate('framework.date.error.message'),
          item.expression,
          item.expressionMsg,
          true
        );
      }
    }
  };

  render() {
    return this.renderDatePicker(this.props.item);
  }
}

const mapStateToProps = state => {
  return {
    formData: state.frameworkForm.form
  }
};

const mapDispatchToProps = dispatch => ({
  toggleDailogAndSetText: (dailogState, msg) => {
    dispatch({ type: 'TOGGLE_DAILOG_AND_SET_TEXT', dailogState, msg });
  },
});

export default connect(mapStateToProps, mapDispatchToProps)(UiDatePicker);
