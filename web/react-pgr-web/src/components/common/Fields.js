import React, {Component} from 'react';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import TextField from 'material-ui/TextField';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import DatePicker from 'material-ui/DatePicker';
import {translate} from './common';
import areIntlLocalesSupported from 'intl-locales-supported';

export default class Fields extends Component{
  renderFields = (obj) =>{
    //console.log('came to renderfields',obj.code);
    let des = translate(obj.description);
    let mandatory = (obj.required == true) ? " *" : ""
    let description = des + mandatory;

    let DateTimeFormat;
    if (areIntlLocalesSupported(['fr', 'fa-IR'])) {
      DateTimeFormat = global.Intl.DateTimeFormat;
    }
    switch(obj.dataType){
      case "string":
        return (
          <Col xs={12} md={3}>
            <TextField fullWidth={true} floatingLabelText={description} onChange={(e) => this.props.handler(e, obj.code, obj.required, '')} />
          </Col>
        );
      case "number":
        return(
          <Col xs={12} md={3}>
            <TextField fullWidth={true} floatingLabelText={description} onChange={(e) => this.props.handler(e, obj.code, obj.required, /^[+-]?\d+(\.\d+)?$/)}   />
          </Col>
        );
      case "date":
        return(
          <Col xs={12} md={3}>
            <DatePicker fullWidth={true} DateTimeFormat={DateTimeFormat} locale="fr" floatingLabelText={description}  />
          </Col>
        );
      case "singlevaluelist":
        return(
          <Col xs={12} md={3}>
            <SelectField fullWidth={true} floatingLabelText={description} value={this.props.value} onChange={(event, key, value) => {
              this.props.handler(value, obj.code, obj.required, "")
            }} >
            {obj.attribValues.map((dd, index) => (
                <MenuItem value={translate(dd.key)} key={index} primaryText={translate(dd.name)} />
            ))}
            </SelectField>
          </Col>
        );
      case "multivaluelist":
        return(
          <Col xs={12} md={3}>
            <SelectField fullWidth={true} multiple={true} floatingLabelText={description}  />
          </Col>
        );
    }
  }
  render(){
    return (
      <div>
        {this.renderFields(this.props.obj)}
      </div>
    );
  }
}
