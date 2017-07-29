import React, {Component} from 'react';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import TextField from 'material-ui/TextField';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import DatePicker from 'material-ui/DatePicker';
import TimePicker from 'material-ui/TimePicker';
import MaterialDateTimePicker from 'material-datetime-picker';
import {translate} from './common';

export default class Fields extends Component{
  constructor(props){
    super(props);
    this.state={};
  }
  renderFields = (obj) =>{
    let des = translate(obj.description);
    let mandatory = (obj.required == true) ? " *" : ""
    let description = des + mandatory;
    if(obj.variable){
      switch(obj.dataType){
        case "string":
          return (
            <Col xs={12} md={3}>
              <TextField fullWidth={true} ref={obj.code} floatingLabelText={description} value={this.props.value} onChange={(e,newValue) => this.props.handler(newValue, obj.code, obj.required, '')} errorText={this.props.error ? this.props.error : ""}/>
            </Col>
          );
        case "integer":
          return (
            <Col xs={12} md={3}>
              <TextField fullWidth={true} ref={obj.code} floatingLabelText={description} value={this.props.value} onChange={(e,newValue) => this.props.handler(newValue, obj.code, obj.required, /^[+-]?\d+$/)} errorText={this.props.error ? this.props.error : ""} />
            </Col>
          );
        case "double":
          return (
            <Col xs={12} md={3}>
              <TextField fullWidth={true} ref={obj.code} floatingLabelText={description} value={this.props.value} onChange={(e,newValue) => this.props.handler(newValue, obj.code, obj.required, /^[+-]?\d+(\.\d+)?$/)} errorText={this.props.error ? this.props.error : ""}/>
            </Col>
          );
        case "date":
          return(
            <Col xs={12} md={3}>
              <DatePicker fullWidth={true} ref={obj.code} floatingLabelText={description} value={this.props.value ? new Date(this.props.value.split('-')[2], this.props.value.split('-')[1]-1, this.props.value.split('-')[0]) : ''}
              formatDate={(date)=>{
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
          //console.log(this.props.value, this.props.value.split(' '));
          let date, time;
          if(this.props.value){
            date = this.props.value.split(' ')[0];
            time = this.props.value.split(' ')[1];
            //console.log((this.props.value && date && time) ? new Date(date.split('-')[2], date.split('-')[1]-1, date.split('-')[0], time.split(':')[0], time.split(':')[1], time.split(':')[2]) : '');
          }
          return (
            <Col xs={12} md={3}>
              <DatePicker fullWidth={true} ref={obj.code} floatingLabelText={description} value={(this.props.value && date && time) ? new Date(date.split('-')[2], date.split('-')[1]-1, date.split('-')[0], time.split(':')[0], time.split(':')[1], time.split(':')[2]) : ''}
              formatDate={(date)=>{
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
                this.refs[obj.code+'timepicker'].openDialog();
              }}/>
              <TimePicker ref={obj.code+'timepicker'} format="24hr" style={{display: 'none'}} onChange={(nothing, time)=>{
                this.props.handler(this.props.value+' '+time.getHours()+':'+time.getMinutes()+':00', obj.code, obj.required, '');
              }}/>
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
