import React, {Component} from 'react';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import TextField from 'material-ui/TextField';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import DatePicker from 'material-ui/DatePicker';
import MaterialDateTimePicker from 'material-datetime-picker';
import {translate} from './common';

export default class Fields extends Component{
  renderFields = (obj) =>{
    //console.log('came to renderfields',obj.code);
    let des = translate(obj.description);
    let mandatory = (obj.required == true) ? " *" : ""
    let description = des + mandatory;
    if(obj.variable){
      switch(obj.dataType){
        case "string":
          return (
            <Col xs={12} md={3}>
              <TextField fullWidth={true} ref={obj.code} floatingLabelText={description} onChange={(e,newValue) => this.props.handler(newValue, obj.code, obj.required, '')} errorText={this.props.error ? this.props.error : ""}/>
            </Col>
          );
        case "integer":
          return (
            <Col xs={12} md={3}>
              <TextField fullWidth={true} ref={obj.code} floatingLabelText={description} onChange={(e,newValue) => this.props.handler(newValue, obj.code, obj.required, /^[+-]?\d+$/)} errorText={this.props.error ? this.props.error : ""} />
            </Col>
          );
        case "double":
          return (
            <Col xs={12} md={3}>
              <TextField fullWidth={true} ref={obj.code} floatingLabelText={description} onChange={(e,newValue) => this.props.handler(newValue, obj.code, obj.required, /^[+-]?\d+(\.\d+)?$/)} errorText={this.props.error ? this.props.error : ""}/>
            </Col>
          );
        case "date":
          return(
            <Col xs={12} md={3}>
              <DatePicker fullWidth={true} ref={obj.code} floatingLabelText={description} formatDate={(date)=>{
                let dateObj = new Date(date);
                let year = dateObj.getFullYear();
                let month = dateObj.getMonth()+1;
                let dt = dateObj.getDate();
                dt =  dt < 10 ? '0' + dt : dt;
                month = month < 10 ? '0' + month : month;
                return dt + '-' + month + '-' + year;
              }}
              onChange={(nothing,date) => {
                let dateObj = new Date(date);
                let year = dateObj.getFullYear();
                let month = dateObj.getMonth()+1;
                let dt = dateObj.getDate();
                dt =  dt < 10 ? '0' + dt : dt;
                month = month < 10 ? '0' + month : month;
                this.props.handler(dt + '-' + month + '-' + year, obj.code, obj.required, '');
              }}
              />
            </Col>
          );
        case "datetime":
          return (
            <Col xs={12} md={3}>
            </Col>
          );
        case "singlevaluelist":
          return(
            <Col xs={12} md={3}>
              <SelectField fullWidth={true} ref={obj.code} floatingLabelText={description} value={this.props.value} onChange={(event, key, value) => {
                this.props.handler(value, obj.code, obj.required, "")
              }} >
              {obj.attribValues.map((dd, index) => (
                  dd.isActive ? <MenuItem value={translate(dd.key)} key={index} primaryText={translate(dd.name)} /> : ''
              ))}
              </SelectField>
            </Col>
          );
        case "multivaluelist":
          return(
            <Col xs={12} md={3}>
              <SelectField fullWidth={true} ref={obj.code} multiple={true} floatingLabelText={description}  value={this.props.value} onChange={(event, key, value) => {
                this.props.handler(value, obj.code, obj.required, "")
              }} >
              {obj.attribValues.map((dd, index) => (
                  dd.isActive ? <MenuItem value={translate(dd.key)} key={index} primaryText={translate(dd.name)} /> : ''
              ))}
              </SelectField>
            </Col>
          );
        case "file":
          return (
            <Col xs={12} md={3}>
              <div>{description}</div>
              <input type="file" className="form-control"/>
            </Col>
          );
        case "multifile":
          return (
            <Col xs={12} md={3}>
              <div>{description}</div>
              <input type="file" multiple className="form-control"/>
            </Col>
          );
      }
    }else{
      return (
        <Col xs={12} md={3}>
          {des}
        </Col>
      )
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
