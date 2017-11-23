import React, {Component} from 'react';
import RaisedButton from 'material-ui/RaisedButton';
import {translate} from 'egov-common-utility';

export default class UiSingleFileUpload extends Component {
  constructor(props) {
       super(props);
    }

  renderSingleFileUpload = (item) => {
    switch (this.props.ui) {
      case 'google':
      if(item.readonly) {
        return (
        <a href={
          window.location.origin + "/filestore/v1/files/id?tenantId=" + localStorage.tenantId + "&fileStoreId=" + this.props.getVal(item.jsonPath)} target="_blank">{translate(item.label)}</a>
      );
      } else {
        /*
          <RaisedButton
            floatingLabelStyle={{"color": "#696969"}}
            style={{"display": (item.hide ? 'none' : 'block')}}
            containerElement='label'
            fullWidth={true}
            value={this.props.getVal(item.jsonPath)}
            disabled={item.isDisabled}
            label={item.label}>
              <input id={item.jsonPath.split(".").join("-")}   type="file" style={{ display: 'none' }} onChange={(e) => this.props.handler({target:{value: e.target.files[0]}}, item.jsonPath, item.isRequired ? true : false, '', item.requiredErrMsg, item.patternErrMsg)}/>
          </RaisedButton>
        */
        return (
          <div style={{"marginTop": "17px", "display": (item.hide ? 'none' : 'inline-block')}}>
            <label>{item.label} <span style={{"color": "#FF0000"}}>{item.isRequired ? " *" : ""}</span></label><br/>
            <input 
              id={item.jsonPath.split(".").join("-")} 
              disabled={item.isDisabled} 
              type="file" 
              style={{"marginTop": "10px"}}
              onChange={(e) => this.props.handler({target:{value: e.target.files[0]}}, item.jsonPath, item.isRequired ? true : false, '', item.requiredErrMsg, item.patternErrMsg)}/>
          </div>
        );
       }
    }
  }

  render () {
    return (
        <div>
          {this.renderSingleFileUpload(this.props.item)}
        </div>
      );
  }
}